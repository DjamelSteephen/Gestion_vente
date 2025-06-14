/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.ui;

import com.mycompany.gestionstockvente.dao.CommandeDAO;
import com.mycompany.gestionstockvente.dao.FactureDAO;
import com.mycompany.gestionstockvente.model.Commande;
import com.mycompany.gestionstockvente.model.ContientCommandeProduit;
import com.mycompany.gestionstockvente.model.Facture;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.service.CommandeService;
import com.mycompany.gestionstockvente.service.FactureService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class FactureUI extends JFrame{
    private final CommandeService commandeService;
    private final JTable tableCommandes;
    private final DefaultTableModel modelCommandes;
    private final JButton btnGenererFacture;

    public FactureUI(Connection connection) throws SQLException {
        super("Facturation");

        this.commandeService = new CommandeService(connection);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null); 
        //setLocationRelativeTo(null);

        modelCommandes = new DefaultTableModel(new Object[]{
            "ID Commande", "Nom Client", "Date Commande", "Nb Produits"
        }, 0);

        tableCommandes = new JTable(modelCommandes);
        JScrollPane scrollPane = new JScrollPane(tableCommandes);

        btnGenererFacture = new JButton("Générer la facture sélectionnée");
        btnGenererFacture.addActionListener(this::genererFacture);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(btnGenererFacture);

        getContentPane().add(new JLabel("Commandes validées :"), BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        chargerCommandes();

        setVisible(true);
    }

    private void chargerCommandes() {
        modelCommandes.setRowCount(0);
        List<Commande> commandes = commandeService.getToutesLesCommandes();
        for (Commande c : commandes) {
            modelCommandes.addRow(new Object[]{
                c.getId(),
                c.getNomClient(),
                c.getDateCommande(),
                c.getProduits().size()
            });
        }
    }

    private void genererFacture(ActionEvent e) {
        int selectedRow = tableCommandes.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une commande dans la liste.",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCommande = (int) modelCommandes.getValueAt(selectedRow, 0);
        String nomClient = modelCommandes.getValueAt(selectedRow, 1).toString();
        LocalDate dateCommande = (LocalDate) modelCommandes.getValueAt(selectedRow, 2);

        try {
            Commande commande = commandeService.getCommandeParId(idCommande);

            if (commande == null || commande.getProduits().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Aucun article dans cette commande.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Chemin complet du dossier de destination
String dossierFactures = "C:\\Users\\Admin\\Documents\\NetBeansProjects\\GestionStockVente\\src\\main\\java\\Ressources";

// Création du répertoire s’il n’existe pas
File dossier = new File(dossierFactures);
if (!dossier.exists()) {
    dossier.mkdirs();
}

// Construction du nom de fichier complet
String nomFichier = "Facture_" + nomClient.replaceAll("\\s+", "_") + "_"
        + dateCommande.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt";
String cheminComplet = dossierFactures + File.separator + nomFichier;

try (PrintWriter writer = new PrintWriter(new FileWriter(cheminComplet))) {
    writer.println("=== FACTURE ===");
    writer.println("Client        : " + nomClient);
    writer.println("Date commande : " + dateCommande);
    writer.println();
    writer.printf("%-25s %-10s %-10s %-10s%n",
            "Produit", "P. Unitaire", "Quantité", "Sous-total");
    writer.println("----------------------------------------------------");

    double total = 0.0;
    for (ContientCommandeProduit ccp : commande.getProduits()) {
        Produit p = ccp.getProduit();
        int qt = ccp.getQuantite();
        double prix = p.getPrixUnitaire();
        double sousTotal = prix * qt;
        total += sousTotal;

        writer.printf("%-25s %-10.2f %-10d %-10.2f%n",
                p.getNom(), prix, qt, sousTotal);
    }

    writer.println("----------------------------------------------------");
    writer.printf("TOTAL FACTURE : %.2f FCFA%n", total);
}

JOptionPane.showMessageDialog(this,
    "✅ Facture générée :\n" + cheminComplet,
    "Succès",
    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la génération de la facture :\n" + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
        public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        try {
            Connection connection = DatabaseConnection.getConnection(); // suppose que tu as une méthode utilitaire
            new FactureUI(connection);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données : " + ex.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    });
}


}
