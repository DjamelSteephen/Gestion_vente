/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.service;

import com.mycompany.gestionstockvente.dao.ProduitDAO;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.sql.Connection;

//import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Admin
 */
public class ProduitService {
 
    private final ProduitDAO produitDAO;

    public ProduitService() throws SQLException {
        // On récupère la connexion unique à la base pour ce service
        Connection conn = DatabaseConnection.getConnection();
        this.produitDAO = new ProduitDAO(conn);
    }

    /**
     * Ajoute un nouveau produit en base.
     */
    public void ajouterProduit(Produit p) throws SQLException {
        produitDAO.ajouterProduit(p);
    }

    /**
     * Modifie un produit existant en base.
     */
    public void modifierProduit(Produit p) throws SQLException {
        produitDAO.modifierProduit(p);
    }

    /**
     * Supprime un produit (et ses lignes associées) en base.
     */
    public void supprimerProduit(int id) throws SQLException {
        produitDAO.supprimerProduit(id);
    }

    /**
     * Récupère la liste de tous les produits.
     */
    public List<Produit> listerProduits() throws SQLException {
        return produitDAO.listerProduits();
    }

    /**
     * Recherche un produit par son ID.
     */
    public Produit trouverProduitParId(int id) throws SQLException {
        return produitDAO.rechercherProduitParId(id);
    }

    /**
     * Recherche un produit par son nom exact.
     */
    public Produit trouverProduitParNom(String nom) throws SQLException {
        return produitDAO.trouverParNom(nom);
    }
    
}
