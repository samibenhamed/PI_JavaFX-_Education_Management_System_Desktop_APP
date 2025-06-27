package org.example.Model;

public class Seance {
    private String enseignant;
    private int id;
    private String date;
    private String heureDebut;
    private String heureFin;
    private int salleId;
    private String module;
    private String email;

    public Seance() {
    }

    public Seance(String date, String heureDebut, String heureFin, int salleId, String module, String enseignant, String email) {
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.salleId = salleId;
        this.module = module;
        this.enseignant = enseignant;
        this.email = email;
    }

    public Seance(int id, String date, String heureDebut, String heureFin, int salleId, String module, String enseignant, String email) {
        this.id = id;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.salleId = salleId;
        this.module = module;
        this.enseignant = enseignant;
        this.email = email;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public int getSalleId() {
        return salleId;
    }

    public void setSalleId(int salleId) {
        this.salleId = salleId;
    }


    public void setModule(String module) {
        this.module = module;
    }

    public String getModule() {
return module;
    }

    public String getEnseignant() {
        return enseignant;
    }

public void setEnseignant(String enseignant) {
    this.enseignant = enseignant;
}

public String getEmail() {  return email; }
    public void setEmail(String email) { this.email = email; }
}
