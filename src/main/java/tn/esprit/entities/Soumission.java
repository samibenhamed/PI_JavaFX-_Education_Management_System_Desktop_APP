package tn.esprit.entities;

import java.time.LocalDateTime;

public class Soumission {
    private int id;
    private int devoirId;
    private int eleveId;
    private LocalDateTime dateSoumission;
    private String contenu;
    private String cheminFichier;

    // Default Constructor
    public Soumission() {}

    // Full Constructor
    public Soumission(int id, int devoirId, int eleveId, LocalDateTime dateSoumission, String contenu, String cheminFichier) {
        this.id = id;
        this.devoirId = devoirId;
        this.eleveId = eleveId;
        this.dateSoumission = dateSoumission;
        this.contenu = contenu;
        this.cheminFichier = cheminFichier;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDevoirId() { return devoirId; }
    public void setDevoirId(int devoirId) { this.devoirId = devoirId; }

    public int getEleveId() { return eleveId; }
    public void setEleveId(int eleveId) { this.eleveId = eleveId; }

    public LocalDateTime getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDateTime dateSoumission) { this.dateSoumission = dateSoumission; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }
}
