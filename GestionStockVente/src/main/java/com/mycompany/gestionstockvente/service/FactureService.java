/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.service;
import com.mycompany.gestionstockvente.dao.FactureDAO;
import com.mycompany.gestionstockvente.model.Facture;

import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author Admin
 */
public class FactureService {
        private final FactureDAO factureDAO;

    public FactureService(FactureDAO factureDAO) {
        this.factureDAO = factureDAO;
    }

    // Crée une facture si elle n'existe pas déjà pour la commande
    public void creerFacture(Facture facture) throws SQLException {
        int idCommande = facture.getCommande().getId();
        if (!factureDAO.existeFacturePourCommande(idCommande)) {
            factureDAO.enregistrerFacture(facture);
        } else {
            throw new SQLException("Une facture existe déjà pour cette commande (ID: " + idCommande + ").");
        }
    }

    // Supprime une facture par son ID
    public void supprimerFacture(int idFacture) throws SQLException {
        factureDAO.supprimerFacture(idFacture);
    }
    

    // Récupère toutes les factures
    public List<Facture> getToutesLesFactures() throws SQLException {
        return factureDAO.getToutesLesFactures();
    }

    // Supprime une facture associée à une commande
    public boolean supprimerFactureParCommande(int idCommande) throws SQLException {
        return factureDAO.supprimerParCommande(idCommande);
    }
}
