package entities;

import java.time.LocalDate;

public class Devoir {
    private int id;
    private String titre;
    private String description;
    private LocalDate dateLimite;
    private String cheminFichier;
    private int matiereId;
    private int enseignantId;
    private int classeId;

    public Devoir(int id, String titre, String description, LocalDate dateLimite, String cheminFichier, int matiereId, int enseignantId, int classeId) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateLimite = dateLimite;
        this.cheminFichier = cheminFichier;
        this.matiereId = matiereId;
        this.enseignantId = enseignantId;
        this.classeId = classeId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateLimite() { return dateLimite; }
    public void setDateLimite(LocalDate dateLimite) { this.dateLimite = dateLimite; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public int getMatiereId() { return matiereId; }
    public void setMatiereId(int matiereId) { this.matiereId = matiereId; }

    public int getEnseignantId() { return enseignantId; }
    public void setEnseignantId(int enseignantId) { this.enseignantId = enseignantId; }

    public int getClasseId() { return classeId; }
    public void setClasseId(int classeId) { this.classeId = classeId; }

    // Affichage dans ComboBox
    @Override
    public String toString() {
        return titre;
    }
}
