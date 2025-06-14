/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.model;

/**
 *
 * @author Admin
 */
public class Produit {
    private int id;
    private String nom;
    private String categorie;
    private double prixUnitaire;
    private int quantiteEnStock;

    public Produit(int id, String nom, String categorie, double prixUnitaire, int quantiteEnStock) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.prixUnitaire = prixUnitaire;
        this.quantiteEnStock = quantiteEnStock;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getQuantiteEnStock() {
        return quantiteEnStock;
    }

    public void setQuantiteEnStock(int quantiteEnStock) {
        this.quantiteEnStock = quantiteEnStock;
    }
    
    public boolean estEnRupture(int seuil) {
        return this.quantiteEnStock <= seuil;
    }
    
    @Override
    public String toString() {
       // return "Produit [id=" + id + ", nom=" + nom + ", categorie=" + categorie +
          //     ", prixUnitaire=" + prixUnitaire + ", quantiteEnStock=" + quantiteEnStock + "]";
          return this.nom;
    }
}
