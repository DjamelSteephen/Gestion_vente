/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.dao;
import com.mycompany.gestionstockvente.model.Commande;
import com.mycompany.gestionstockvente.model.ContientCommandeProduit;
import com.mycompany.gestionstockvente.model.Facture;
import com.mycompany.gestionstockvente.model.Produit;
import java.sql.*;
//import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class FactureDAO {
    private final Connection connection;

    public FactureDAO(Connection connection) {
        this.connection = connection;
    }

    // Enregistrer une facture pour une commande
    public void enregistrerFacture(Facture facture) throws SQLException {
        String sql = "INSERT INTO facture (id_commande, montant_total) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, facture.getCommande().getId());
            stmt.setDouble(2, facture.getCommande().calculerMontantTotal());
            stmt.executeUpdate();
        }
    }

    // Vérifie si une facture existe déjà pour une commande donnée
    public boolean existeFacturePourCommande(int idCommande) throws SQLException {
        String sql = "SELECT COUNT(*) FROM facture WHERE id_commande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCommande);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    // Supprimer une facture
    public void supprimerFacture(int idFacture) throws SQLException {
        String sql = "DELETE FROM facture WHERE id_facture = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idFacture);
            stmt.executeUpdate();
        }
    }
    
    public boolean supprimerParCommande(int idCommande) throws SQLException {
        String sql = "DELETE FROM facture WHERE id_commande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCommande);
            return stmt.executeUpdate() > 0;
        }
    }

    // Récupérer toutes les factures avec les infos de commande associées
    public List<Facture> getToutesLesFactures() throws SQLException {
        List<Facture> factures = new ArrayList<>();

        String sql = "SELECT f.id_facture, f.montant_total, f.date_facture, " +
                     "c.id_commande, c.nom_client, c.date_commande " +
                     "FROM facture f JOIN commande c ON f.id_commande = c.id_commande";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int idCommande = rs.getInt("id_commande");
                String nomClient = rs.getString("nom_client");
                LocalDate dateCommande = rs.getDate("date_commande").toLocalDate();

                Commande commande = new Commande(idCommande, nomClient, dateCommande);
                chargerProduitsCommande(commande); // on ajoute les produits

                Facture facture = new Facture(commande);
                factures.add(facture);
            }
        }

        return factures;
    }
    
//    public boolean supprimerParIdCommande(int idCommande) throws SQLException {
//    String sql = "DELETE FROM facture WHERE id_commande = ?";
//    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//        stmt.setInt(1, idCommande);
//        int affected = stmt.executeUpdate();
//        return affected > 0;
//    }
//}


    // Méthode utilitaire : charger les produits associés à une commande
    private void chargerProduitsCommande(Commande commande) throws SQLException {
        String sql = "SELECT p.id_produit, p.nom, p.categorie, cp.quantite, cp.prix_unitaire " +
                     "FROM contient_commande_produit cp " +
                     "JOIN produit p ON cp.id_produit = p.id_produit " +
                     "WHERE cp.id_commande = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commande.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produit produit = new Produit(
                            rs.getInt("id_produit"),
                            rs.getString("nom"),
                            rs.getString("categorie"),
                            rs.getDouble("prix_unitaire"),
                            rs.getInt("quantiteEnStock")
                    );

                    ContientCommandeProduit ccp = new ContientCommandeProduit(
                            produit,
                            rs.getInt("quantite")
                            //rs.getDouble("prix_unitaire")
                    );

                    commande.ajouterProduit(ccp);
                }
            }
        }
    }
}
