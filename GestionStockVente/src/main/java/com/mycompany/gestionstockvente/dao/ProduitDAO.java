/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.dao;
import com.mycompany.gestionstockvente.model.*;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
//import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.sql.*;
import java.util.*;
/**
 *
 * @author Admin
 */
public class ProduitDAO {
    
    private final Connection connection;

    public ProduitDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Recherche un produit par son ID.
     */
    public Produit rechercherProduitParId(int idProduit) throws SQLException {
        String sql = "SELECT * FROM produit WHERE id_produit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProduit);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getDouble("prix_unitaire"),
                        rs.getInt("quantite_en_stock")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Ajoute un nouveau produit en base.
     */
    public void ajouterProduit(Produit p) throws SQLException {
        String sql = "INSERT INTO produit (nom, categorie, prix_unitaire, quantite_en_stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getCategorie());
            ps.setDouble(3, p.getPrixUnitaire());
            ps.setInt(4, p.getQuantiteEnStock());
            ps.executeUpdate();
        }
    }

    /**
     * Modifie les informations d’un produit déjà existant.
     */
    public void modifierProduit(Produit p) throws SQLException {
        String sql = "UPDATE produit SET nom = ?, categorie = ?, prix_unitaire = ?, quantite_en_stock = ? WHERE id_produit = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNom());
            ps.setString(2, p.getCategorie());
            ps.setDouble(3, p.getPrixUnitaire());
            ps.setInt(4, p.getQuantiteEnStock());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Supprime un produit et toutes ses références dans contient_commande_produit.
     * Effectué dans une transaction pour maintenir la cohérence.
     */
    public void supprimerProduit(int idProduit) throws SQLException {
        String sqlDeleteLignes = "DELETE FROM contient_commande_produit WHERE id_produit = ?";
        String sqlDeleteProduit = "DELETE FROM produit WHERE id_produit = ?";

        Connection conn = DatabaseConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            // 1) Supprimer d’abord les lignes d’association
            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteLignes)) {
                ps1.setInt(1, idProduit);
                ps1.executeUpdate();
            }

            // 2) Puis supprimer le produit lui‑même
            try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteProduit)) {
                ps2.setInt(1, idProduit);
                ps2.executeUpdate();
            }

            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    /**
     * Retourne tous les produits existants.
     */
    public List<Produit> listerProduits() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produits.add(new Produit(
                    rs.getInt("id_produit"),
                    rs.getString("nom"),
                    rs.getString("categorie"),
                    rs.getDouble("prix_unitaire"),
                    rs.getInt("quantite_en_stock")
                ));
            }
        }
        return produits;
    }

    /**
     * Recherche un produit par son nom exact.
     */
    public Produit trouverParNom(String nom) throws SQLException {
        String sql = "SELECT * FROM produit WHERE nom = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getDouble("prix_unitaire"),
                        rs.getInt("quantite_en_stock")
                    );
                }
            }
        }
        return null;
    }
}
