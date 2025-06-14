/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.util;
import java.sql.Connection;
/**
 *
 * @author Admin
 */
public class TestConnection {
    public static void main(String[] args) {

            try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connexion à PostgreSQL réussie !");
                // Ici, tu peux poursuivre vers le menu login ou l'application
            } else {
                System.err.println("❌ Connexion établie mais inactive !");
                System.exit(1); // Refus d'accès
            }
        } catch (Exception e) {
            System.err.println("❌ Échec de la connexion à la base de données !");
            System.err.println("Raison : " + e.getMessage());
            System.exit(1); // Refus d'accès explicite
        }
    }
}
