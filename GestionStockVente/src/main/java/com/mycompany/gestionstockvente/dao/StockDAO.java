/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.dao;
import com.mycompany.gestionstockvente.model.Stock;
import com.mycompany.gestionstockvente.model.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class StockDAO {
    private final Connection connection;

    public StockDAO(Connection connection) {
        this.connection = connection;
    }

    public void ajouterStock(Stock stock) throws SQLException {
        String sql = "INSERT INTO stock (id_produit, quantite, seuil_alerte) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, stock.getProduit().getId()); // Assure-toi que Produit a bien un champ id
            stmt.setInt(2, stock.getProduit().getQuantiteEnStock());
            stmt.setInt(3, stock.getSeuilReapprovisionnement());
            stmt.executeUpdate();
        }
    }
    
    public Stock rechercherStockParProduitId(int produitId) throws SQLException {
    String sql = "SELECT * FROM stock WHERE id_produit = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, produitId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int seuil = rs.getInt("seuil_alerte");
            Produit produit = new ProduitDAO(connection).rechercherProduitParId(produitId);
            return new Stock(produit, seuil);
        }
    }
    return null; // Pas de stock trouvé
}


    public List<Stock> listerStocks() throws SQLException {
        List<Stock> stocks = new ArrayList<>();
        String sql = "SELECT s.quantite, s.seuil_alerte, p.id_produit, p.nom, p.categorie, p.prix_unitaire " +
                     "FROM stock s JOIN produit p ON s.id_produit = p.id_produit WHERE p.id_produit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getDouble("prix_unitaire"),
                        rs.getInt("quantite")
                );
                Stock stock = new Stock(produit, rs.getInt("seuil_alerte"));
                stocks.add(stock);
            }
        }
        return stocks;
    }

    public Stock trouverStockParProduitId(int idProduit) throws SQLException {
        String sql = "SELECT s.quantite, s.seuil_alerte, p.id_produit, p.nom, p.categorie, p.prix_unitaire " +
                     "FROM stock s JOIN produit p ON s.id_produit = p.id_produit " +
                     "WHERE p.id_produit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProduit);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produit produit = new Produit(
                            rs.getInt("id_produit"),
                            rs.getString("nom"),
                            rs.getString("categorie"),
                            rs.getDouble("prix_unitaire"),
                            rs.getInt("quantite")
                    );
                    return new Stock(produit, rs.getInt("seuil_alerte"));
                }
            }
        }
        return null;
    }

    public void mettreAJourQuantiteEtSeuil(int idProduit, int nouvelleQuantite, int nouveauSeuil) throws SQLException {
            String updateProduit = "UPDATE produit SET quantite_en_stock = ? WHERE id_produit = ?";
    // Mettre à jour le seuil dans la table stock
    String updateStock = "UPDATE stock SET seuil_alerte = ? WHERE id_produit = ?";
    
    try (
        PreparedStatement stmtProduit = connection.prepareStatement(updateProduit);
        PreparedStatement stmtStock = connection.prepareStatement(updateStock)
    ) {
        stmtProduit.setInt(1, nouvelleQuantite);
        stmtProduit.setInt(2, idProduit);
        stmtProduit.executeUpdate();

        stmtStock.setInt(1, nouveauSeuil);
        stmtStock.setInt(2, idProduit);
        stmtStock.executeUpdate();
    }
}


    public void supprimerStockParProduit(int idProduit) throws SQLException {
        String sql = "DELETE FROM stock WHERE id_produit = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idProduit);
            stmt.executeUpdate();
        }
    }
}
