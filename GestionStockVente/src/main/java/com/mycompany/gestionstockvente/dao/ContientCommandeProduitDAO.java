/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.dao;
//import com.mycompany.gestionstockvente.model.ContientCommandeProduit;
import com.mycompany.gestionstockvente.model.Produit;
//import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class ContientCommandeProduitDAO {
    private final Connection connection;

    public ContientCommandeProduitDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Insère ou met à jour une ligne dans contient_commande_produit.
     * - Si (id_commande, id_produit) n'existe pas, on fait un INSERT.
     * - Sinon, ON CONFLICT DO UPDATE sur la colonne "quantite_voulue".
     */
    public void upsertProduitDansCommande(int idCommande,
                                          Produit produit,
                                          int quantite,
                                          double prixUnitaire) throws SQLException 
    {
        String sql =
            "INSERT INTO contient_commande_produit " +
            "  (id_commande, id_produit, quantite_voulue, prix_unitaire) " +
            "VALUES (?, ?, ?, ?) " +
            "ON CONFLICT (id_commande, id_produit) DO UPDATE " +
            "  SET quantite_voulue = EXCLUDED.quantite_voulue, " +
            "      prix_unitaire   = EXCLUDED.prix_unitaire";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ps.setInt(2, produit.getId());
            ps.setInt(3, quantite);
            ps.setDouble(4, prixUnitaire);
            ps.executeUpdate();
        }
    }

    /**
     * Récupère tous les produits (avec leur quantité voulue dans la commande) pour une commande donnée.
     * On retourne une liste d'objets Produit, où on stocke la quantité voulue dans un champ temporaire
     * (par exemple via setQuantiteEnStock() ou un setter dédié).
     */
    public List<Produit> listerProduitsDeCommande(int idCommande) throws SQLException {
        List<Produit> produits = new ArrayList<>();

        String sql =
            "SELECT p.id_produit, p.nom, p.categorie, ccp.quantite_voulue, " +
            "       ccp.prix_unitaire, s.quantite AS quantite_en_stock " +
            "FROM contient_commande_produit ccp " +
            "JOIN produit p ON ccp.id_produit = p.id_produit " +
            "LEFT JOIN stock s ON p.id_produit = s.id_produit " +
            "WHERE ccp.id_commande = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // 1. On récupère la quantité actuelle en stock
                    Produit produit = new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getDouble("prix_unitaire"),
                        rs.getInt("quantite_en_stock")
                    );
                    // 2. On applique la quantité voulue dans la commande
                    produit.setQuantiteEnStock(rs.getInt("quantite_voulue"));
                    produits.add(produit);
                }
            }
        }

        return produits;
    }

    /**
     * Supprime tous les produits d’une commande (DELETE WHERE id_commande = ?).
     */
    public void supprimerProduitsDeCommande(int idCommande) throws SQLException {
        String sql = "DELETE FROM contient_commande_produit WHERE id_commande = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ps.executeUpdate();
        }
    }

    /**
     * Supprime un produit spécifique d’une commande (DELETE WHERE id_commande = ? AND id_produit = ?).
     */
    public void supprimerProduitDeCommande(int idCommande, int idProduit) throws SQLException {
        String sql = "DELETE FROM contient_commande_produit WHERE id_commande = ? AND id_produit = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCommande);
            ps.setInt(2, idProduit);
            ps.executeUpdate();
        }
    }
}
