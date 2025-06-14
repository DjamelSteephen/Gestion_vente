/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.test;

import com.mycompany.gestionstockvente.model.Produit;
import com.mycompany.gestionstockvente.model.Stock;
import com.mycompany.gestionstockvente.service.StockService;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Admin
 */
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StockServiceTest {
    private static Connection connection;
    private StockService stockService;

    @BeforeAll
    public static void setupDatabase() throws SQLException {
        connection = DatabaseConnection.getConnection();
    }

    @BeforeEach
    public void init() {
        stockService = new StockService(connection);
    }

    @Test
    public void testAjouterStock() throws SQLException {
        Produit produit = new Produit(999, "ProduitTest", "CategorieTest", 100.0, 50);
        Stock stock = new Stock(produit, 10);
        assertDoesNotThrow(() -> stockService.ajouterStock(stock));
    }

    @Test
    public void testMettreAJourQuantiteEtSeuil() throws SQLException {
        stockService.mettreAJourQuantiteEtSeuil(999, 70, 15);
        Stock stock = stockService.trouverStockParProduitId(999);
        assertEquals(70, stock.getProduit().getQuantiteEnStock());
        assertEquals(15, stock.getSeuilReapprovisionnement());
    }

    @Test
    public void testSupprimerStock() throws SQLException {
        stockService.supprimerStockParProduit(999);
        assertThrows(IllegalArgumentException.class, () -> stockService.trouverStockParProduitId(999));
    }
}
