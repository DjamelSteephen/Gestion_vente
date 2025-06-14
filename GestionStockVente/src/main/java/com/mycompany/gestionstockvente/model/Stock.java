/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.model;

/**
 *
 * @author Admin
 */
public class Stock {
    private Produit produit;
    private int seuilReapprovisionnement;

    public Stock() {}

    public Stock(Produit produit, int seuilReapprovisionnement) {
        this.produit = produit;
        this.seuilReapprovisionnement = seuilReapprovisionnement;
    }

    // Getters & setters
    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public int getSeuilReapprovisionnement() { return seuilReapprovisionnement; }
    public void setSeuilReapprovisionnement(int seuilReapprovisionnement) {
        this.seuilReapprovisionnement = seuilReapprovisionnement;
    }

    public boolean doitEtreReapprovisionne() {
        return produit.getQuantiteEnStock() <= seuilReapprovisionnement;
    }
}
