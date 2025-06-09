package org.example.dao;

import org.example.Model.Seance;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeanceDAO {

    public static List<Seance> getAll() {
        List<Seance> seances = new ArrayList<>();
        String sql = "SELECT * FROM seance";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                seances.add(new Seance(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getString("heure_debut"),
                        rs.getString("heure_fin"),
                        rs.getInt("salle_id"),
                        rs.getString("module"),
                        rs.getString("enseignant")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }

    public void add(Seance s) {
        String sql = "INSERT INTO seance (date, heure_debut, heure_fin, salle_id, module, enseignant) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getDate());
            stmt.setString(2, s.getHeureDebut());
            stmt.setString(3, s.getHeureFin());
            stmt.setInt(4, s.getSalleId());
            stmt.setString(5, s.getModule());
            stmt.setString(6, s.getEnseignant());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Seance s) {
        String sql = "UPDATE seance SET date=?, heure_debut=?, heure_fin=?, salle_id=?, module=?, enseignant=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getDate());
            stmt.setString(2, s.getHeureDebut());
            stmt.setString(3, s.getHeureFin());
            stmt.setInt(4, s.getSalleId());
            stmt.setString(5, s.getModule());
            stmt.setString(6, s.getEnseignant());
            stmt.setInt(7, s.getId());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public void delete(int id) {
        String sql = "DELETE FROM seance WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
