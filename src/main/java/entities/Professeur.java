package entities;

public class Professeur {
    private int id;
    private String nom;
    private String email;
    private String specialite;

    public Professeur(int id, String nom, String email, String specialite) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.specialite = specialite;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getSpecialite() {
        return specialite;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return nom;
    }
}

