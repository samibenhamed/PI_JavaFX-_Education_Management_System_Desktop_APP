package org.example.Model;


public class Salle {
    private int id;
    private String nom;
    private int capacite;

    public Salle(int id, String nom, int capacite) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
    }

    public Salle(String nom, int capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }

    public Salle(int id, String text, String text1) {
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getCapacite() { return capacite; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
}
