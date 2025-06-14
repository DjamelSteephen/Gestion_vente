/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.service;
import com.mycompany.gestionstockvente.dao.UtilisateurDAO;
import com.mycompany.gestionstockvente.model.Utilisateur;
/**
 *
 * @author Admin
 */
public class UtilisateurService {
    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurService() {
        utilisateurDAO = new UtilisateurDAO();
    }

    public Utilisateur authentifier(String identifiant, String motDePasse) {
        return utilisateurDAO.trouverParIdentifiants(identifiant, motDePasse);
    }
    
    
}
