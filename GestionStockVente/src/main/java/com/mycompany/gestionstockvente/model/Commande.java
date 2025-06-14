/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class Commande {
    private int id;
    private String nomClient;
    private LocalDate dateCommande;
    private List<ContientCommandeProduit> produits;

    public Commande() {
        this.produits = new ArrayList<>();
    }

    public Commande(int id, String nomClient, LocalDate dateCommande) {
        this.id = id;
        this.nomClient = nomClient;
        this.dateCommande = dateCommande;
        this.produits = new ArrayList<>();
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public LocalDate getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDate dateCommande) { this.dateCommande = dateCommande; }

    public List<ContientCommandeProduit> getProduits() { return produits; }

    public void ajouterProduit(ContientCommandeProduit p) {
        produits.add(p);
    }

    public double calculerMontantTotal() {
        return produits.stream()
            .mapToDouble(ContientCommandeProduit::calculerSousTotal)
            .sum();
    }
    
//    @Override
//public String toString() {
//    return "#" + getId() + " – " + getNomClient() + " (" + getDateCommande() + ")";
//}

}
