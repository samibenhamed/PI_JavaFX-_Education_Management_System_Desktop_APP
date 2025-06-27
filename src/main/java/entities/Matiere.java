package entities;

public class Matiere {
    private int id;
    private Type nom;
    private String niveau;
    private String description;
    private Professeur professeur;

    public Matiere(int id, Type nom, String niveau, String description,Professeur professeur) {
        this.id = id;
        this.nom = nom;
        this.niveau = niveau;
        this.description = description;
        this.professeur = professeur;
    }
    public Matiere(Type nom, String niveau, String description) {
        this.nom = nom;
        this.niveau = niveau;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public Type getNom() {
        return nom;
    }

    public String getNiveau() {
        return niveau;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(Type nom) {
        this.nom = nom;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Professeur getProfesseur() {
        return professeur;
    }

    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
    }
    public String getProfesseurName() {
        return professeur != null ? professeur.getNom() : "";
    }

}
