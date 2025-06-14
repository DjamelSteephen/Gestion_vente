/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.model;
//import java.util.List;
/**
 *
 * @author Admin
 */
public class Facture {
    private Commande commande;

    public Facture(Commande commande) {
        this.commande = commande;
    }
    
    public Commande getCommande() {
       return this.commande;
    }

    public String genererFactureTexte() {
        StringBuilder sb = new StringBuilder();
        sb.append("FACTURE POUR ").append(commande.getNomClient()).append("\n");
        sb.append("Date : ").append(commande.getDateCommande()).append("\n\n");
        sb.append(String.format("%-20s %-10s %-10s %-10s\n", "Produit", "PU", "Qté", "Sous-Total"));
        sb.append("----------------------------------------------------------\n");

        for (ContientCommandeProduit ligne : commande.getProduits()) {
            Produit p = ligne.getProduit();
            double sousTotal = ligne.calculerSousTotal();
            sb.append(String.format("%-20s %-10.2f %-10d %-10.2f\n", p.getNom(), p.getPrixUnitaire(), ligne.getQuantite(), sousTotal));
        }

        sb.append("\nTOTAL : ").append(commande.calculerMontantTotal()).append(" F CFA\n");
        return sb.toString();
    }
}
