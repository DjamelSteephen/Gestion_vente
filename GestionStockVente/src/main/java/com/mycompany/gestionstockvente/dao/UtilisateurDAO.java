/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.dao;
import com.mycompany.gestionstockvente.model.Utilisateur;
import com.mycompany.gestionstockvente.util.DatabaseConnection;
import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class UtilisateurDAO {
    public Utilisateur trouverParIdentifiants(String identifiant, String motdepasse) {
        //Utilisateur utilisateur = null;
        Utilisateur utilisateur =new Utilisateur(2, "Zongo", "steph", "root");
        String sql = "SELECT * FROM utilisateurs WHERE identifiant = ? AND motdepasse = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, identifiant);
            stmt.setString(2, motdepasse);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    utilisateur = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("identifiant"),
                        rs.getString("motdepasse")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utilisateur;
    }
}
