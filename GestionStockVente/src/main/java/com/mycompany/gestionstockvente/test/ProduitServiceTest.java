/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.test;

import com.mycompany.gestionstockvente.dao.ProduitDAO;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.service.ProduitService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
//import com.mycompany.gestionstockvente.service.ProduitService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*
/**
 *
 * @author Admin
 */
public class ProduitServiceTest {
    private static Connection connection;
    private ProduitService produitService;

    @BeforeAll
    static void initAll() throws SQLException {
        // Récupère la connexion à la base (ex. base de test)
        connection = DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setUp() throws SQLException {
        // On vide la table contient_commande_produit si besoin (FK vers produit)
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM contient_commande_produit");
            stmt.executeUpdate("DELETE FROM produit");
        }
        produitService = new ProduitService();
    }

    @Test
    @DisplayName("Ajouter un produit puis le retrouver dans la liste")
    void testAjouterEtListerProduit() throws SQLException {
        Produit p = new Produit(0, "TestProduit", "CatégorieX", 19.99, 100);
        produitService.ajouterProduit(p);

        List<Produit> produits = produitService.listerProduits();
        assertFalse(produits.isEmpty(), "La liste des produits ne doit pas être vide après ajout");

        boolean trouve = produits.stream()
                .anyMatch(prod -> prod.getNom().equals("TestProduit") &&
                                  prod.getCategorie().equals("CatégorieX") &&
                                  prod.getPrixUnitaire() == 19.99 &&
                                  prod.getQuantiteEnStock() == 100);
        assertTrue(trouve, "Le produit ajouté doit figurer dans la liste");
    }

    @Test
    @DisplayName("Modifier un produit existant et vérifier la mise à jour")
    void testModifierProduit() throws SQLException {
        // 1) Ajouter
        Produit p = new Produit(0, "AModifier", "CatA", 10.0, 50);
        produitService.ajouterProduit(p);

        // 2) Récupérer l'ID du produit (le dernier inséré)
        List<Produit> produits = produitService.listerProduits();
        assertFalse(produits.isEmpty());
        Produit dernier = produits.get(produits.size() - 1);

        // 3) Modifier les champs
        dernier.setNom("Modifié");
        dernier.setCategorie("CatMod");
        dernier.setPrixUnitaire(25.0);
        dernier.setQuantiteEnStock(75);
        produitService.modifierProduit(dernier);

        // 4) Recharger et vérifier
        Produit modifie = produitService.trouverProduitParId(dernier.getId());
        assertNotNull(modifie);
        assertEquals("Modifié", modifie.getNom());
        assertEquals("CatMod", modifie.getCategorie());
        assertEquals(25.0, modifie.getPrixUnitaire());
        assertEquals(75, modifie.getQuantiteEnStock());
    }

    @Test
    @DisplayName("Supprimer un produit et vérifier qu'il n'apparaît plus")
    void testSupprimerProduit() throws SQLException {
        // 1) Ajouter
        Produit p = new Produit(0, "ASupprimer", "CatB", 5.0, 10);
        produitService.ajouterProduit(p);

        // 2) Récupérer l'ID
        List<Produit> produits = produitService.listerProduits();
        assertFalse(produits.isEmpty());
        Produit dernier = produits.get(produits.size() - 1);
        int idASupprimer = dernier.getId();

        // 3) Supprimer
        produitService.supprimerProduit(idASupprimer);

        // 4) Vérifier qu'il n'existe plus
        Produit trouve = produitService.trouverProduitParId(idASupprimer);
        assertNull(trouve, "Le produit supprimé ne doit plus être trouvé par son ID");

        List<Produit> apresSupp = produitService.listerProduits();
        boolean existeEncore = apresSupp.stream()
                .anyMatch(prod -> prod.getId() == idASupprimer);
        assertFalse(existeEncore, "Le produit ne doit plus apparaître dans la liste");
    }
}
