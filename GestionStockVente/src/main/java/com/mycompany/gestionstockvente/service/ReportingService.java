/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
 /*
 * @author Admin
 */
public class ReportingService {
    private final Connection connection;

    public ReportingService(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour générer un rapport de ventes journalier (ou à la demande)
    public void genererRapportVentes(String cheminFichier) throws SQLException, IOException {
        int nombreCommandes = 0;
        double chiffreAffaires = 0.0;
        Map<String, Integer> topProduits = new LinkedHashMap<>();

        String requeteCommandes = "SELECT COUNT(*) FROM commande WHERE date_commande = CURRENT_DATE";
        String requeteCA = "SELECT SUM(montant_total) FROM facture WHERE date_facture = CURRENT_DATE";
        String requeteTopProduits = """
            SELECT p.nom, SUM(ccp.quantite) as quantite_totale
            FROM contient_commande_produit ccp
            JOIN produit p ON p.id_produit = ccp.id_produit
            JOIN commande c ON c.id_commande = ccp.id_commande
            WHERE c.date_commande = CURRENT_DATE
            GROUP BY p.nom
            ORDER BY quantite_totale DESC
            LIMIT 5
        """;

        try (
            Statement stmt = connection.createStatement();
            ResultSet rsCommandes = stmt.executeQuery(requeteCommandes)
        ) {
            if (rsCommandes.next()) {
                nombreCommandes = rsCommandes.getInt(1);
            }
        }

        try (
            Statement stmt = connection.createStatement();
            ResultSet rsCA = stmt.executeQuery(requeteCA)
        ) {
            if (rsCA.next()) {
                chiffreAffaires = rsCA.getDouble(1);
            }
        }

        try (
            Statement stmt = connection.createStatement();
            ResultSet rsTop = stmt.executeQuery(requeteTopProduits)
        ) {
            while (rsTop.next()) {
                String nomProduit = rsTop.getString("nom");
                int quantiteTotale = rsTop.getInt("quantite_totale");
                topProduits.put(nomProduit, quantiteTotale);
            }
        }

        // Génération du fichier texte
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cheminFichier))) {
            writer.write("=== RAPPORT DES VENTES DU JOUR ===\n");
            writer.write("Date : " + LocalDate.now() + "\n\n");

            writer.write("Nombre total de commandes traitées : " + nombreCommandes + "\n");
            writer.write("Chiffre d'affaires total : " + chiffreAffaires + " FCFA\n\n");

            writer.write("Top 5 des produits les plus vendus :\n");
            for (Map.Entry<String, Integer> entry : topProduits.entrySet()) {
                writer.write("- " + entry.getKey() + " : " + entry.getValue() + " unités\n");
            }

            writer.write("\nRapport généré automatiquement.\n");
        }
    }
}
