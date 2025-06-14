/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.test;

import com.mycompany.gestionstockvente.model.Commande;
import com.mycompany.gestionstockvente.model.ContientCommandeProduit;
import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.service.CommandeService;
import com.mycompany.gestionstockvente.service.ProduitService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Admin
 */
public class CommandeServiceTest {
    private static Connection connection;
    private CommandeService commandeService;

    @BeforeAll
    static void init() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @BeforeEach
    void setup() {
        commandeService = new CommandeService(connection);
    }

    @Test
    void testCreerEtLireCommande() {
        Produit produit = new Produit(1, "Produit Test", "Catégorie A", 100.0, 10);
        ContientCommandeProduit ccp = new ContientCommandeProduit(produit, 2);

        commandeService.creerCommande("Client Test", Arrays.asList(ccp));

        List<Commande> commandes = commandeService.getToutesLesCommandes();
        assertFalse(commandes.isEmpty());

        Commande derniereCommande = commandes.get(commandes.size() - 1);
        assertEquals("Client Test", derniereCommande.getNomClient());
        assertFalse(derniereCommande.getProduits().isEmpty());
    }

    @Test
    void testSuppressionCommande() {
        Produit produit = new Produit(1, "Produit X", "Catégorie B", 50.0, 20);
        ContientCommandeProduit ccp = new ContientCommandeProduit(produit, 1);

        commandeService.creerCommande("Client Supprimer", List.of(ccp));

        List<Commande> commandes = commandeService.getToutesLesCommandes();
        Commande derniere = commandes.get(commandes.size() - 1);
        int id = derniere.getId();

        commandeService.supprimerCommande(id);

        Commande supprimee = commandeService.getCommandeParId(id);
        assertNull(supprimee);
    }
}
