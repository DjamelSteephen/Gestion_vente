/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.gestionstockvente.model;

/**
 *
 * @author Admin
 */
public class Utilisateur {
    private final int id;
    private final String nom;
    private final String identifiant;
    private final String motdepasse;

    public Utilisateur(int id, String nom, String identifiant, String motdepasse) {
        this.id = id;
        this.nom = nom;
        this.identifiant = identifiant;
        this.motdepasse = motdepasse;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getIdentifiant() { return identifiant; }
    public String getMotdepasse() { return motdepasse; }
}
