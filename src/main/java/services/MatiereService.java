package services;


import utils.DataBase;
import entities.Matiere;
import entities.Professeur;
import entities.Type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereService {

    private Connection con;

    public MatiereService() {
        this.con = DataBase.getInstance().getConnection();
    }

    // CREATE
    public void addMatiere(Matiere matiere) {
        String query = "INSERT INTO Matiere (nom, niveau, description, professeur_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, matiere.getNom().name());  // convert enum to string
            pst.setString(2, matiere.getNiveau());
            pst.setString(3, matiere.getDescription());
            pst.setInt(4, matiere.getProfesseur().getId());
            pst.executeUpdate();
            System.out.println("Matiere added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // READ (Get all Matieres)
    public List<Matiere> getAllMatieres() {
        List<Matiere> matieres = new ArrayList<>();
        String query = "SELECT m.*, p.id as pid, p.nom as pnom, p.email, p.specialite " +
                "FROM Matiere m " +
                "LEFT JOIN Professeur p ON m.professeur_id = p.id";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Professeur prof = new Professeur(
                        rs.getInt("pid"),
                        rs.getString("pnom"),
                        rs.getString("email"),
                        rs.getString("specialite")
                );

                Matiere matiere = new Matiere(
                        rs.getInt("id"),
                        Type.valueOf(rs.getString("nom")),  // convert string to enum
                        rs.getString("niveau"),
                        rs.getString("description"),
                        prof
                );
                matieres.add(matiere);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matieres;
    }



    // UPDATE
    public void updateMatiere(Matiere matiere) {
        String query = "UPDATE Matiere SET nom = ?, niveau = ?, description = ?, professeur_id = ? WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, matiere.getNom().name());  // enum to string
            pst.setString(2, matiere.getNiveau());
            pst.setString(3, matiere.getDescription());
            pst.setInt(4, matiere.getProfesseur().getId());
            pst.setInt(5, matiere.getId());
            pst.executeUpdate();
            System.out.println("Matiere updated successfully with Professeur!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // DELETE
    public void deleteMatiere(int id) {
        String query = "DELETE FROM Matiere WHERE id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Matiere deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Matiere> getMatieresByProfesseurId(int professeurId) {
        List<Matiere> matieres = new ArrayList<>();
        String query = "SELECT m.*, p.id as pid, p.nom as pnom, p.email, p.specialite " +
                "FROM Matiere m " +
                "JOIN Professeur p ON m.professeur_id = p.id " +
                "WHERE m.professeur_id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, professeurId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Professeur prof = new Professeur(
                        rs.getInt("pid"),
                        rs.getString("pnom"),
                        rs.getString("email"),
                        rs.getString("specialite")
                );

                Matiere matiere = new Matiere(
                        rs.getInt("id"),
                        Type.valueOf(rs.getString("nom")),  // convert string to enum
                        rs.getString("niveau"),
                        rs.getString("description"),
                        prof
                );
                matieres.add(matiere);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matieres;
    }


}

