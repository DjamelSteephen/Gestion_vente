/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.model;

/**
 *
 * @author Admin
 */
public class ContientCommandeProduit {
    private Produit produit;
    private int quantite;

    public ContientCommandeProduit() {}

    public ContientCommandeProduit(Produit produit, int quantite) {
        this.produit = produit;
        this.quantite = quantite;
    }

    // Getters & setters
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public double calculerSousTotal() {
        return produit.getPrixUnitaire() * quantite;
    }
}
