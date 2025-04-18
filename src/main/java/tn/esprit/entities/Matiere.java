package tn.esprit.entities;

public class Matiere {

    private int id;
    private String nom;
    private int coefficient;
    private int nbHeures; // ✅ Nouveau champ

    // ✅ Constructeur avec nbHeures
    public Matiere(int id, String nom, int coefficient, int nbHeures) {
        this.id = id;
        this.nom = nom;
        this.coefficient = coefficient;
        this.nbHeures = nbHeures;
    }

    // Getter pour id
    public int getId() {
        return id;
    }

    // Setter pour id
    public void setId(int id) {
        this.id = id;
    }

    // Getter pour nom
    public String getNom() {
        return nom;
    }

    // Setter pour nom
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter pour coefficient
    public int getCoefficient() {
        return coefficient;
    }

    // Setter pour coefficient
    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    // ✅ Getter pour nbHeures
    public int getNbHeures() {
        return nbHeures;
    }

    // ✅ Setter pour nbHeures
    public void setNbHeures(int nbHeures) {
        this.nbHeures = nbHeures;
    }

    @Override
    public String toString() {
        return "Matiere{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", coefficient=" + coefficient +
                ", nbHeures=" + nbHeures +
                '}';
    }
}
