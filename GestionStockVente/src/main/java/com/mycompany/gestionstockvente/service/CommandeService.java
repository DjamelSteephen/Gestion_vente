/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.service;
import com.mycompany.gestionstockvente.dao.CommandeDAO;
import com.mycompany.gestionstockvente.model.Commande;
import com.mycompany.gestionstockvente.model.ContientCommandeProduit;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author Admin
 */
public class CommandeService {
    private final CommandeDAO commandeDAO;

    public CommandeService(Connection connection) {
        this.commandeDAO = new CommandeDAO(connection);
    }

    public void creerCommande(String nomClient, List<ContientCommandeProduit> produits) {
        Commande commande = new Commande();
        commande.setNomClient(nomClient);
        commande.setDateCommande(LocalDate.now());

        for (ContientCommandeProduit ccp : produits) {
            commande.ajouterProduit(ccp);
        }

        try {
            commandeDAO.creerCommande(commande);
            System.out.println("✅ Commande créée avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la création de la commande : " + e.getMessage());
        }
    }

    public Commande getCommandeParId(int idCommande) {
        try {
            return commandeDAO.getCommandeById(idCommande);
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération de la commande : " + e.getMessage());
            return null;
        }
    }

    public List<Commande> getToutesLesCommandes() {
        try {
            return commandeDAO.getToutesLesCommandes();
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des commandes : " + e.getMessage());
            return null;
        }
    }

    public void supprimerCommande(int idCommande) {
        commandeDAO.supprimerCommande(idCommande);
        System.out.println("✅ Commande supprimée avec succès !");
    }
    
    
}
