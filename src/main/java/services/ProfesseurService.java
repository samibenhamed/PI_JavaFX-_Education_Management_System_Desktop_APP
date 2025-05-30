package services;


import utils.DataBase;
import entities.Professeur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesseurService {

    private Connection con;

    public ProfesseurService() {
        this.con = DataBase.getInstance().getConnection();
    }

    // CREATE
    public void addProfesseur(Professeur professeur) {
        String query = "INSERT INTO Professeur (nom, email, specialite) VALUES (?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, professeur.getNom());
            pst.setString(2, professeur.getEmail());
            pst.setString(3, professeur.getSpecialite());
            pst.executeUpdate();
            System.out.println("Professeur added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ (Get all Professeurs)
    public List<Professeur> getAllProfesseurs() {
        List<Professeur> professeurs = new ArrayList<>();
        String query = "SELECT * FROM Professeur";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Professeur professeur = new Professeur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("specialite")
                );
                professeurs.add(professeur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professeurs;
    }

    // UPDATE
    public void updateProfesseur(Professeur professeur) {
        String query = "UPDATE Professeur SET nom = ?, email = ?, specialite = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, professeur.getNom());
            pst.setString(2, professeur.getEmail());
            pst.setString(3, professeur.getSpecialite());
            pst.setInt(4, professeur.getId());
            pst.executeUpdate();
            System.out.println("Professeur updated successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteProfesseur(int id) {
        String query = "DELETE FROM Professeur WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Professeur deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Get only the names of all Professeurs
    public List<String> getAllProfesseurNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT nom FROM Professeur";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                names.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

}

