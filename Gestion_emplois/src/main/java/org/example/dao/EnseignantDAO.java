package org.example.dao;

import org.example.Model.Enseignant;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDAO {

    // 🔁 Lire tous les enseignants
    public static List<Enseignant> getAll() {
        List<Enseignant> enseignants = new ArrayList<>();
        String sql = "SELECT * FROM enseignant";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                enseignants.add(new Enseignant(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("departement")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enseignants;
    }

    // ➕ Ajouter un enseignant
    public void add(Enseignant e) {
        String sql = "INSERT INTO enseignant (nom, prenom, email, departement) VALUES (?, ?, ?, ?)";

        System.out.println("Inserting enseignant: " + e.getNom());
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNom());
            stmt.setString(2, e.getPrenom());
            stmt.setString(3, e.getEmail());
            stmt.setString(4, e.getDepartement());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // ✏️ Modifier un enseignant
    public void update(Enseignant e) {
        String sql = "UPDATE enseignant SET nom=?, prenom=?, email=?, departement=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, e.getNom());
            stmt.setString(2, e.getPrenom());
            stmt.setString(3, e.getEmail() != null ? e.getEmail() : "");
            stmt.setString(4, e.getDepartement() != null ? e.getDepartement() : "");
            stmt.setInt(5, e.getId());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Modifié !");
            } else {
                System.out.println("Aucune ligne modifiée.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    // ❌ Supprimer un enseignant
    public void delete(int id) {
        String sql = "DELETE FROM enseignant WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static List<String> getAllEmails() {
        List<String> emails = new ArrayList<>();
        String query = "SELECT email FROM enseignant";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                emails.add(rs.getString("email"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emails;
    }


}