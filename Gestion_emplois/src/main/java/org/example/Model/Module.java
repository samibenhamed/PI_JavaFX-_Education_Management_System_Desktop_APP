package org.example.Model;


public class Module {
    private int id;
    private String nom;
    private int volumeHoraire;

    public Module(int id, String nom, int volumeHoraire) {
        this.id = id;
        this.nom = nom;
        this.volumeHoraire = volumeHoraire;
    }

    public Module(String nom, int volumeHoraire) {
        this.nom = nom;
        this.volumeHoraire = volumeHoraire;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public int getVolumeHoraire() { return volumeHoraire; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setVolumeHoraire(int volumeHoraire) { this.volumeHoraire = volumeHoraire; }


}
