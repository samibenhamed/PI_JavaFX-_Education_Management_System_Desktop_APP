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
                        rs.getString("enseignant"),
                        rs.getString("email")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }

    public void add(Seance s) {
        String sql = "INSERT INTO seance (date, heure_debut, heure_fin, salle_id, module, enseignant, email) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getDate());
            stmt.setString(2, s.getHeureDebut());
            stmt.setString(3, s.getHeureFin());
            stmt.setInt(4, s.getSalleId());
            stmt.setString(5, s.getModule());
            stmt.setString(6, s.getEnseignant());
            stmt.setString(7, s.getEmail());



            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Seance s) {
        String sql = "UPDATE seance SET date=?, heure_debut=?, heure_fin=?, salle_id=?, module=?, enseignant=?, email=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getDate());
            stmt.setString(2, s.getHeureDebut());
            stmt.setString(3, s.getHeureFin());
            stmt.setInt(4, s.getSalleId());
            stmt.setString(5, s.getModule());
            stmt.setString(6, s.getEnseignant());
            stmt.setString(7, s.getEmail());
            stmt.setInt(8, s.getId());


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


    public List<String> getAllDistinctEmails() {
        List<String> emails = new ArrayList<>();
        String query = "SELECT DISTINCT email FROM seance";

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
    public List<Seance> getSeancesByEmail(String email) {
        List<Seance> list = new ArrayList<>();
        String query = "SELECT * FROM seance WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Seance s = new Seance(
                        rs.getInt("id"),
                        rs.getString("date"),
                        rs.getString("heure_debut"),
                        rs.getString("heure_fin"),
                        rs.getInt("salle_id"),
                        rs.getString("module"),
                        rs.getString("enseignant"),
                        rs.getString("email")
                );
                s.setEmail(rs.getString("email"));
                list.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }


}
