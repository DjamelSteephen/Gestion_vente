/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.test;

import com.mycompany.gestionstockvente.dao.ContientCommandeProduitDAO;
import com.mycompany.gestionstockvente.dao.ProduitDAO;
import com.mycompany.gestionstockvente.dao.StockDAO;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.service.VenteService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.sql.Connection;

/**
 *
 * @author Admin
 */
public class VenteServiceTest {
    
//    public static void main(String[] args) {
//        try {
//            Connection connection =DatabaseConnection.getConnection(); // à adapter
//            StockDAO stockDAO = new StockDAO(connection);
//            ContientCommandeProduitDAO commandeProduitDAO = new ContientCommandeProduitDAO(connection);
//            VenteService venteService = new VenteService(connection, stockDAO, commandeProduitDAO);
//
//            // Test manuel
//            Produit produit = new ProduitDAO(connection).rechercherProduitParId(1); // exemple produit avec id = 1
//            int idCommande = 101; // par exemple
//            int quantite = 3;
//
//            boolean result = venteService.vendreProduit(idCommande, produit, quantite);
//            System.out.println(result ? "✅ Vente réussie" : "❌ Vente échouée");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
