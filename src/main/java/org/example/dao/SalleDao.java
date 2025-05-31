package org.example.dao;


import org.example.Model.Salle;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDao {

    public List<Salle> getAll() {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salle";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salles.add(new Salle(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("capacite")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salles;
    }

public void add(Salle salle) {
    String sql = "INSERT INTO salle (nom, capacite) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


        // Vérification des valeurs à insérer
        System.out.println("Nom: " + salle.getNom());
        System.out.println("Capacité: " + salle.getCapacite());

        stmt.setString(1, salle.getNom());
        stmt.setInt(2, salle.getCapacite());
        System.out.println("Ajout salle - nom: " + salle.getNom() + ", capacité: " + salle.getCapacite());

        stmt.executeUpdate();

        // Récupère l’ID généré
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                salle.setId(generatedKeys.getInt(1));
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    public void update(Salle s) {
        String sql = "UPDATE salle SET nom=?, capacite=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, s.getNom());
            stmt.setInt(2, s.getCapacite());
            stmt.setInt(3, s.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM salle WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
