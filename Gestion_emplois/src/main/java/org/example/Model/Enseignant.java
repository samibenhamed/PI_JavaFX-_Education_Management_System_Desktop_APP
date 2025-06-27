package org.example.Model;

public class Enseignant {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String departement;

    public Enseignant(int id, String nom, String prenom, String email, String departement) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.departement = departement;
    }




    public Enseignant(String nom, String prenom, String email, String departement) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.departement = departement;
    }

    // Getters & Setters
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }


}

