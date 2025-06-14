/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.ui;
import com.mycompany.gestionstockvente.dao.ProduitDAO;
import com.mycompany.gestionstockvente.form.Home;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.model.Utilisateur;
import com.mycompany.gestionstockvente.service.ProduitService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Admin
 */
public class ProduitUI extends JFrame{
   private JTextField nomField, categorieField, prixField, quantiteField;
    private JTable table;
    private DefaultTableModel model;
    private ProduitService produitService;

    public ProduitUI() throws SQLException {
        setTitle("Gestion des Produits");
        setSize(900, 520);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        produitService = new ProduitService();

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel haut (formulaire + boutons)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Formulaire
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        nomField = new JTextField();
        categorieField = new JTextField();
        prixField = new JTextField();
        quantiteField = new JTextField();

        formPanel.add(new JLabel("Nom :"));
        formPanel.add(nomField);
        formPanel.add(new JLabel("Catégorie :"));
        formPanel.add(categorieField);
        formPanel.add(new JLabel("Prix unitaire :"));
        formPanel.add(prixField);
        formPanel.add(new JLabel("Quantité en stock :"));
        formPanel.add(quantiteField);

        // Boutons
        JButton ajouterBtn = new JButton("Ajouter");
        JButton modifierBtn = new JButton("Modifier");
        JButton supprimerBtn = new JButton("Supprimer");
        JButton retourBtn = new JButton("Retour à l'accueil");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(ajouterBtn);
        buttonPanel.add(modifierBtn);
        buttonPanel.add(supprimerBtn);
        buttonPanel.add(retourBtn);

        // Ajout des sous-panels au panel haut
        topPanel.add(formPanel);
        topPanel.add(buttonPanel);

        // Table
        model = new DefaultTableModel(new Object[]{"ID", "Nom", "Catégorie", "Prix", "Quantité"}, 0);
        table = new JTable(model);
        // Masquer la colonne ID
table.getColumnModel().getColumn(0).setMinWidth(0);
table.getColumnModel().getColumn(0).setMaxWidth(0);
table.getColumnModel().getColumn(0).setPreferredWidth(0);
        JScrollPane scrollPane = new JScrollPane(table);

        // Placement dans le panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Ajout du panel principal à la fenêtre
        add(mainPanel);

        // Chargement des données
        chargerProduits();

        // Actions boutons
        ajouterBtn.addActionListener(e -> {
            try {
                Produit p = new Produit(
                        0,
                        nomField.getText().trim(),
                        categorieField.getText().trim(),
                        Double.parseDouble(prixField.getText().trim()),
                        Integer.parseInt(quantiteField.getText().trim())
                );
                produitService.ajouterProduit(p);
                chargerProduits();
                viderChamps();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        });

        modifierBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                try {
                    int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                    Produit p = new Produit(
                            id,
                            nomField.getText().trim(),
                            categorieField.getText().trim(),
                            Double.parseDouble(prixField.getText().trim()),
                            Integer.parseInt(quantiteField.getText().trim())
                    );
                    produitService.modifierProduit(p);
                    chargerProduits();
                    viderChamps();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                }
            }
        });

        supprimerBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                try {
                    int id = Integer.parseInt(model.getValueAt(row, 0).toString());
                    produitService.supprimerProduit(id);
                    chargerProduits();
                    viderChamps();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                }
            }
        });

        retourBtn.addActionListener(e -> {
            dispose();
            Utilisateur user = new Utilisateur(2, "Zongo", "steph", "root");
            try {
                new Home(user).setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(ProduitUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                nomField.setText(model.getValueAt(row, 1).toString());
                categorieField.setText(model.getValueAt(row, 2).toString());
                prixField.setText(model.getValueAt(row, 3).toString());
                quantiteField.setText(model.getValueAt(row, 4).toString());
            }
        });
    }

    private void chargerProduits() {
        try {
            List<Produit> produits = produitService.listerProduits();
            model.setRowCount(0);
            for (Produit p : produits) {
                model.addRow(new Object[]{p.getId(), p.getNom(), p.getCategorie(), p.getPrixUnitaire(), p.getQuantiteEnStock()});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void viderChamps() {
        nomField.setText("");
        categorieField.setText("");
        prixField.setText("");
        quantiteField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ProduitUI().setVisible(true);
            } catch (SQLException ex) {
                System.getLogger(ProduitUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }
}
