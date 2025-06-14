/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.service;
import com.mycompany.gestionstockvente.dao.StockDAO;
import com.mycompany.gestionstockvente.model.Stock;
//import com.mycompany.gestionstockvente.model.Produit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Admin
 */
public class StockService {
    private final StockDAO stockDAO;

    public StockService(Connection connection) {
        this.stockDAO = new StockDAO(connection);
    }

    public void ajouterStock(Stock stock) throws SQLException {
        if (stock.getProduit() == null || stock.getProduit().getId() <= 0) {
            throw new IllegalArgumentException("Produit invalide pour le stock.");
        }
        if (stock.getProduit().getQuantiteEnStock() < 0) {
            throw new IllegalArgumentException("Quantité en stock ne peut pas être négative.");
        }
        if (stock.getSeuilReapprovisionnement() < 0) {
            throw new IllegalArgumentException("Le seuil de réapprovisionnement ne peut pas être négatif.");
        }
        stockDAO.ajouterStock(stock);
    }

    public List<Stock> listerStocks() throws SQLException {
        return stockDAO.listerStocks();
    }

    public Stock trouverStockParProduitId(int idProduit) throws SQLException {
        if (idProduit <= 0) {
            throw new IllegalArgumentException("ID du produit invalide.");
        }
        Stock stock = stockDAO.trouverStockParProduitId(idProduit);
        if (stock == null) {
            throw new IllegalArgumentException("Aucun stock trouvé pour le produit ID : " + idProduit);
        }
        return stock;
    }

    public void mettreAJourQuantiteEtSeuil(int idProduit, int nouvelleQuantite, int nouveauSeuil) throws SQLException {
        if (idProduit <= 0) {
            throw new IllegalArgumentException("ID du produit invalide.");
        }
        if (nouvelleQuantite < 0 || nouveauSeuil < 0) {
            throw new IllegalArgumentException("Quantité ou seuil invalide.");
        }
        stockDAO.mettreAJourQuantiteEtSeuil(idProduit, nouvelleQuantite, nouveauSeuil);
    }

    public void supprimerStockParProduit(int idProduit) throws SQLException {
        if (idProduit <= 0) {
            throw new IllegalArgumentException("ID du produit invalide.");
        }
        stockDAO.supprimerStockParProduit(idProduit);
    }
}
