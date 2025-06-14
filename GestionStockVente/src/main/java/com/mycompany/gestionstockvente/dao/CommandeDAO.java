/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.dao;
import com.mycompany.gestionstockvente.model.Commande;
import com.mycompany.gestionstockvente.model.ContientCommandeProduit;
import com.mycompany.gestionstockvente.model.Produit;
import java.sql.*;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class CommandeDAO {
    private final Connection connection;

    public CommandeDAO(Connection connection) {
        this.connection = connection;
    }

    // 1) Création d’une commande avec ses lignes (produits)
    public void creerCommande(Commande commande) throws SQLException {
        String sqlCommande =
            "INSERT INTO commande (nom_client, date_commande) VALUES (?, ?) RETURNING id_commande";

        try (PreparedStatement stmt = connection.prepareStatement(sqlCommande)) {
            stmt.setString(1, commande.getNomClient());
            stmt.setDate(2, Date.valueOf(commande.getDateCommande()));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idCommande = rs.getInt("id_commande");
                commande.setId(idCommande);

                // Insérer chaque ligne dans contient_commande_produit
                for (ContientCommandeProduit ccp : commande.getProduits()) {
                    ajouterProduitCommande(idCommande, ccp);
                }
            }
        }
    }

    private void ajouterProduitCommande(int idCommande, ContientCommandeProduit ccp) throws SQLException {
        String sql =
            "INSERT INTO contient_commande_produit (id_commande, id_produit, quantite_voulue, prix_unitaire) " +
            "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCommande);
            stmt.setInt(2, ccp.getProduit().getId());
            stmt.setInt(3, ccp.getQuantite());
            stmt.setDouble(4, ccp.getProduit().getPrixUnitaire());
            stmt.executeUpdate();
        }
    }

    // 2) Lecture d’une commande par son ID
    public Commande getCommandeById(int idCommande) throws SQLException {
        String sql = "SELECT * FROM commande WHERE id_commande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCommande);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Commande commande = new Commande();
                    commande.setId(rs.getInt("id_commande"));
                    commande.setNomClient(rs.getString("nom_client"));
                    commande.setDateCommande(rs.getDate("date_commande").toLocalDate());
                    chargerProduitsCommande(commande);
                    return commande;
                }
            }
        }
        return null;
    }

    // 3) Lecture de toutes les commandes
    public List<Commande> getToutesLesCommandes() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String sql = "SELECT * FROM commande ORDER BY id_commande";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Commande commande = new Commande();
                commande.setId(rs.getInt("id_commande"));
                commande.setNomClient(rs.getString("nom_client"));
                commande.setDateCommande(rs.getDate("date_commande").toLocalDate());
                chargerProduitsCommande(commande);
                commandes.add(commande);
            }
        }
        return commandes;
    }

    // 4) Charger les lignes d’une commande donnée
    private void chargerProduitsCommande(Commande commande) throws SQLException {
        String sql =
            "SELECT p.id_produit, p.nom, p.categorie, p.prix_unitaire, ccp.quantite_voulue " +
            "FROM contient_commande_produit ccp " +
            "JOIN produit p ON ccp.id_produit = p.id_produit " +
            "WHERE ccp.id_commande = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, commande.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produit produit = new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getDouble("prix_unitaire"),
                        0 // quantité en stock éventuellement ignorée ici
                    );
                    int quantite = rs.getInt("quantite_voulue");
                    ContientCommandeProduit ccp = new ContientCommandeProduit(produit, quantite);
                    commande.ajouterProduit(ccp);
                }
            }
        }
    }

    // 5) Suppression d’une commande (avec ses lignes) : CORRECTION du problème de clé étrangère
    public boolean supprimerCommande(int idCommande) {
    String sqlSupprimerProduits = "DELETE FROM contient_commande_produit WHERE id_commande = ?";
    String sqlSupprimerCommande = "DELETE FROM commande WHERE id = ?";

    try (PreparedStatement ps1 = connection.prepareStatement(sqlSupprimerProduits);
         PreparedStatement ps2 = connection.prepareStatement(sqlSupprimerCommande)) {

        ps1.setInt(1, idCommande);
        ps1.executeUpdate();

        ps2.setInt(1, idCommande);
        int rowsAffected = ps2.executeUpdate();

        return rowsAffected > 0;
    } catch (SQLException ex) {
        ex.printStackTrace();
        return false;
    }
}


}
