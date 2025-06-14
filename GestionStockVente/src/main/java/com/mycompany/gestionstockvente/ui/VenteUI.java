/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.ui;

import com.mycompany.gestionstockvente.dao.CommandeDAO;
import com.mycompany.gestionstockvente.dao.ContientCommandeProduitDAO;
import com.mycompany.gestionstockvente.dao.ProduitDAO;
import com.mycompany.gestionstockvente.dao.StockDAO;
import com.mycompany.gestionstockvente.form.Home;
import com.mycompany.gestionstockvente.model.Commande;
import com.mycompany.gestionstockvente.model.ContientCommandeProduit;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.model.Stock;
import com.mycompany.gestionstockvente.model.Utilisateur;
import com.mycompany.gestionstockvente.service.CommandeService;
import com.mycompany.gestionstockvente.service.ProduitService;
import com.mycompany.gestionstockvente.service.VenteService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class VenteUI extends JFrame{
    private final CommandeService  commandeService;
    private final ProduitService    produitService;
    private final StockDAO          stockDAO;
    private final DefaultTableModel model;
    private final JTable            table;
    private final JButton           btnVendre;
    private final JButton           btnSupprimer;
    private final JButton           btnRetour;

    public VenteUI(Connection connection) throws SQLException {
        super("Interface de Vente");
        this.commandeService = new CommandeService(connection);
        this.produitService  = new ProduitService();
        this.stockDAO        = new StockDAO(connection);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 520);
        setLocationRelativeTo(null);

        // Définition des colonnes : Nom client | Nom produit | Quantité en stock | Date commande | Quantité commandée
        String[] colonnes = {
            "Nom client",
            "Nom produit",
            "Quantité en stock",
            "Date commande",
            "Quantité commandée"
        };
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                // Seule la colonne "Quantité commandée" est éditable directement
                return col == 4;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Boutons : Vendre | Supprimer la vente | Retour
        btnVendre    = new JButton("Vendre");
        btnSupprimer = new JButton("Supprimer la vente");
        btnRetour    = new JButton("Retour");

        btnVendre.addActionListener(e -> effectuerVente());
        btnSupprimer.addActionListener(e -> supprimerVente());
        btnRetour.addActionListener(e -> {
            this.dispose();
            // On revient à Home (utilisateur fictif à adapter si besoin)
            Utilisateur user = new Utilisateur(2, "Zongo", "steph", "root");
            try {
                new Home(user).setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(VenteUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelBas.add(btnVendre);
        panelBas.add(btnSupprimer);
        panelBas.add(btnRetour);

        getContentPane().setLayout(new BorderLayout(10, 10));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panelBas, BorderLayout.SOUTH);

        // Chargement initial des lignes de vente
        chargerLignesVente();

        setVisible(true);
    }

    private void chargerLignesVente() {
        try {
            model.setRowCount(0);

            List<Commande> toutes = commandeService.getToutesLesCommandes();
            if (toutes == null) return;

            for (Commande c : toutes) {
                String nomClient = c.getNomClient();
                LocalDate dateC  = c.getDateCommande();

                for (ContientCommandeProduit ccp : c.getProduits()) {
                    Produit p = ccp.getProduit();
                    int idProduit = p.getId();

                    // Lire la quantité en stock depuis ProduitDAO
                    Produit produitComplet = new ProduitDAO(DatabaseConnection.getConnection())
                                               .rechercherProduitParId(idProduit);
                    int qtStock = (produitComplet != null)
                                  ? produitComplet.getQuantiteEnStock()
                                  : 0;

                    // Qté déjà commandée (colonne quantite dans contient_commande_produit)
                    int qtCommandee = ccp.getQuantite();

                    model.addRow(new Object[] {
                        nomClient,
                        p.getNom(),
                        qtStock,
                        dateC,
                        qtCommandee
                    });
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des lignes de vente : " + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void effectuerVente() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner une ligne dans le tableau.",
                "Attention",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // Lecture des valeurs sur la ligne sélectionnée
            String nomClient  = model.getValueAt(row, 0).toString();
            String nomProduit = model.getValueAt(row, 1).toString();
            int qtStock       = Integer.parseInt(model.getValueAt(row, 2).toString());
            LocalDate dateC   = (LocalDate) model.getValueAt(row, 3);

            Object qtDesObj = model.getValueAt(row, 4);
            if (qtDesObj == null) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez d’abord saisir la quantité commandée dans la dernière colonne.",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int qtCommandee;
            try {
                qtCommandee = Integer.parseInt(qtDesObj.toString());
                if (qtCommandee <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Quantité commandée invalide (entrez un entier positif).",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (qtCommandee > qtStock) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuffisant : seules " + qtStock + " unité(s) disponible(s).",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Retrouver la commande en base (par nomClient + dateCommande)
            Commande commandeCourante = commandeService.getToutesLesCommandes().stream()
                .filter(c -> c.getNomClient().equals(nomClient)
                          && c.getDateCommande().equals(dateC))
                .findFirst()
                .orElse(null);

            if (commandeCourante == null) {
                JOptionPane.showMessageDialog(this,
                    "Impossible de retrouver la commande associée.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            int idCommande = commandeCourante.getId();

            // Retrouver l’objet Produit complet pour avoir l’ID exact
            Produit produit = produitService.listerProduits().stream()
                .filter(p -> p.getNom().equals(nomProduit))
                .findFirst()
                .orElse(null);

            if (produit == null) {
                JOptionPane.showMessageDialog(this,
                    "Produit introuvable en base !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            int idProduit = produit.getId();

            // Démarrer la transaction JDBC
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            try {
                // 1) Upsert dans contient_commande_produit
                ContientCommandeProduitDAO ccpDao = new ContientCommandeProduitDAO(conn);
                ccpDao.upsertProduitDansCommande(
                    idCommande,
                    produit,
                    qtCommandee,
                    produit.getPrixUnitaire()
                );

                // 2) Décrémenter le stock (table produit.quantite_en_stock + stock.seuil_alerte inchangé)
                int nouvelleQt = qtStock - qtCommandee;
                Stock ancienneLigneStock = stockDAO.trouverStockParProduitId(idProduit);
                int seuilActuel = (ancienneLigneStock != null)
                                  ? ancienneLigneStock.getSeuilReapprovisionnement()
                                  : 0;

                stockDAO.mettreAJourQuantiteEtSeuil(
                    idProduit,
                    nouvelleQt,
                    seuilActuel
                );

                // 3) Commit
                conn.commit();
                JOptionPane.showMessageDialog(this,
                    "✅ Vente effectuée : " + qtCommandee
                    + " unité(s) de « " + nomProduit + " » vendue(s).",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception txEx) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "La vente n’a pas pu être réalisée : " + txEx.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
            } finally {
                conn.setAutoCommit(true);
            }

            // 4) Recharger le tableau pour rafraîchir les stocks
            chargerLignesVente();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la vente :\n" + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void supprimerVente() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Sélectionnez une ligne d’abord.",
                "Attention",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String nomClient  = model.getValueAt(row, 0).toString();
            String nomProduit = model.getValueAt(row, 1).toString();
            int qtStock       = Integer.parseInt(model.getValueAt(row, 2).toString());
            LocalDate dateC   = (LocalDate) model.getValueAt(row, 3);

            Object qtDesObj = model.getValueAt(row, 4);
            if (qtDesObj == null) {
                JOptionPane.showMessageDialog(this,
                    "La quantité commandée est vide, rien à supprimer.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int qtCommandee;
            try {
                qtCommandee = Integer.parseInt(qtDesObj.toString());
                if (qtCommandee <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Quantité commandée invalide.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Retrouver la commande en base
            Commande commandeCourante = commandeService.getToutesLesCommandes().stream()
                .filter(c -> c.getNomClient().equals(nomClient)
                          && c.getDateCommande().equals(dateC))
                .findFirst()
                .orElse(null);

            if (commandeCourante == null) {
                JOptionPane.showMessageDialog(this,
                    "Impossible de retrouver la commande associée.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            int idCommande = commandeCourante.getId();

            // Retrouver l’objet Produit en base
            Produit produit = produitService.listerProduits().stream()
                .filter(p -> p.getNom().equals(nomProduit))
                .findFirst()
                .orElse(null);

            if (produit == null) {
                JOptionPane.showMessageDialog(this,
                    "Produit introuvable en base !",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            int idProduit = produit.getId();

            // Démarrer la transaction JDBC
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            try {
                // 1) Supprimer la ligne dans contient_commande_produit
                ContientCommandeProduitDAO ccpDao = new ContientCommandeProduitDAO(conn);
                ccpDao.supprimerProduitDeCommande(idCommande, idProduit);

                // 2) Réinjecter la quantité dans le stock
                int stockRestant = qtStock + qtCommandee;
                Stock ancienneLigneStock = stockDAO.trouverStockParProduitId(idProduit);
                int seuilActuel = (ancienneLigneStock != null)
                                  ? ancienneLigneStock.getSeuilReapprovisionnement()
                                  : 0;

                stockDAO.mettreAJourQuantiteEtSeuil(
                    idProduit,
                    stockRestant,
                    seuilActuel
                );

                // 3) Commit
                conn.commit();
                JOptionPane.showMessageDialog(this,
                    "✅ Vente supprimée et stock remis à jour (" 
                    + qtCommandee + " unité(s) réintroduite(s)).",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (Exception txEx) {
                conn.rollback();
                JOptionPane.showMessageDialog(this,
                    "La suppression de la vente a échoué :\n" + txEx.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
            } finally {
                conn.setAutoCommit(true);
            }

            // 4) Recharger le tableau
            chargerLignesVente();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la suppression :\n" + ex.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Connection conn = DatabaseConnection.getConnection();
                new VenteUI(conn);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    null,
                    "Impossible de démarrer VenteUI :\n" + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
    
}
