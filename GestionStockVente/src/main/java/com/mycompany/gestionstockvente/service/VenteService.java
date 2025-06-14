/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.service;

import com.mycompany.gestionstockvente.dao.ContientCommandeProduitDAO;
import com.mycompany.gestionstockvente.dao.StockDAO;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.model.Stock;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class VenteService {
    private final StockDAO stockDAO;
    private final ContientCommandeProduitDAO commandeProduitDAO;
    private final Connection connection;

    public VenteService(Connection connection,
                        StockDAO stockDAO,
                        ContientCommandeProduitDAO commandeProduitDAO) {
        this.connection = connection;
        this.stockDAO = stockDAO;
        this.commandeProduitDAO = commandeProduitDAO;
    }

    public boolean vendreProduit(int idCommande, Produit produit, int quantiteSouhaitee) throws SQLException {
        // 1) Récupérer le stock actuel du produit
        Stock stock = stockDAO.trouverStockParProduitId(produit.getId());
        if (stock == null) {
            System.out.println("Produit introuvable dans le stock.");
            return false;
        }

        int quantiteEnStock = stock.getProduit().getQuantiteEnStock();
        if (quantiteSouhaitee > quantiteEnStock) {
            System.out.println("Stock insuffisant : " + quantiteEnStock + " unités disponibles.");
            return false;
        }

        // 2) Démarrer la transaction
        connection.setAutoCommit(false);
        try {
            // 2.a) Upsert dans contient_commande_produit (insert ou update si la paire existe)
            commandeProduitDAO.upsertProduitDansCommande(
                idCommande,
                produit,
                quantiteSouhaitee,
                produit.getPrixUnitaire()
            );

            // 2.b) Décrémenter la quantité en stock (table produit.quantite_en_stock + stock.seuil_alerte inchangé)
            int nouvelleQuantite = quantiteEnStock - quantiteSouhaitee;
            int seuilActuel = stock.getSeuilReapprovisionnement();
            stockDAO.mettreAJourQuantiteEtSeuil(
                produit.getId(),
                nouvelleQuantite,
                seuilActuel
            );

            // 3) Commit
            connection.commit();
            System.out.println("Vente réussie : " + quantiteSouhaitee + " unité(s) vendue(s).");
            return true;
        } catch (Exception ex) {
            // 4) En cas d’erreur, rollback
            connection.rollback();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }
}
