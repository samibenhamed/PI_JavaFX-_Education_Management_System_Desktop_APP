package org.example.Model;


public class Salle {
    private int id;
    private String nom;
    private int capacite;
    private boolean disponible;

    public Salle(int id, String nom, int capacite, boolean disponible) {
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.disponible = true; // default to available
    }

    public Salle(String nom, int capacite, boolean disponible) {
        this.nom = nom;
        this.capacite = capacite;
        this.disponible = true; // default to available
    }

    public Salle(int id, String text, String text1) {
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getCapacite() { return capacite; }
    public boolean isDisponible() { return disponible; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
