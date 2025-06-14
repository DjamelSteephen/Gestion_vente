/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.ui;

import com.mycompany.gestionstockvente.dao.ProduitDAO;
import com.mycompany.gestionstockvente.form.Home;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.model.Utilisateur;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Admin
 */
public class ReportingUI extends JFrame{
    
    private final JTextArea texteRapport;

    public ReportingUI() {
        setTitle("Reporting des Ventes");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // === Zone de texte pour l'affichage du rapport ===
        texteRapport = new JTextArea();
        texteRapport.setEditable(false);
        texteRapport.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(texteRapport);
        add(scrollPane, BorderLayout.CENTER);

        // === Panneau des boutons ===
        JPanel panelBoutons = new JPanel();
        JButton btnRetour = new JButton("⬅ Retour à l'accueil");
        JButton btnEffacer = new JButton("🗑 Effacer le rapport");

        // Action du bouton retour
        btnRetour.addActionListener((ActionEvent e) -> {
            this.dispose(); // ferme la fenêtre actuelle
            Utilisateur user = new Utilisateur(2, "Zongo", "steph", "root");
            try {
                new Home(user).setVisible(true);
                //new Home();   // ouvre l'accueil (assure-toi que HomeUI existe bien)
            } catch (SQLException ex) {
                System.getLogger(ReportingUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

        // Action du bouton effacer
        btnEffacer.addActionListener((ActionEvent e) -> {
            texteRapport.setText(""); // efface le contenu de la zone de texte
        });

        panelBoutons.add(btnRetour);
        panelBoutons.add(btnEffacer);
        add(panelBoutons, BorderLayout.SOUTH);

        // === Génération automatique du rapport ===
        genererReporting();

        setVisible(true);
    }

    private void genererReporting() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            ProduitDAO produitDAO = new ProduitDAO(conn);

            // Chiffre d'affaires total
            double ca = calculerChiffreAffaires(conn);
            texteRapport.append("=== 📊 RAPPORT DE VENTES ===\n");
            texteRapport.append(String.format("Chiffre d'affaires total : %.2f FCFA\n\n", ca));

            // Top 5 des produits
            texteRapport.append("🏆 Top 5 des produits les plus vendus :\n");
            afficherTopProduits(conn);

            // Stock restant
            texteRapport.append("\n📦 Stock actuel des produits :\n");
            List<Produit> produits = produitDAO.listerProduits();
            for (Produit p : produits) {
                texteRapport.append(String.format("- %s (Catégorie: %s) ➜ %d unités en stock\n",
                        p.getNom(), p.getCategorie(), p.getQuantiteEnStock()));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du reporting : " + e.getMessage(),
                    "Erreur SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculerChiffreAffaires(Connection conn) throws SQLException {
        String sql = """
            SELECT SUM(ccp.quantite_voulue * ccp.prix_unitaire) AS total
            FROM contient_commande_produit ccp
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getDouble("total") : 0.0;
        }
    }

    private void afficherTopProduits(Connection conn) throws SQLException {
        String sql = """
            SELECT p.nom, SUM(ccp.quantite_voulue) AS total_vendu
            FROM contient_commande_produit ccp
            JOIN produit p ON ccp.id_produit = p.id_produit
            GROUP BY p.nom
            ORDER BY total_vendu DESC
            LIMIT 5
        """;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int rang = 1;
            while (rs.next()) {
                texteRapport.append(String.format("%d. %s ➜ %d unités vendues\n",
                        rang++, rs.getString("nom"), rs.getInt("total_vendu")));
            }
        }
    }

    // === Point d'entrée pour tester l'interface ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReportingUI());
    }
}
