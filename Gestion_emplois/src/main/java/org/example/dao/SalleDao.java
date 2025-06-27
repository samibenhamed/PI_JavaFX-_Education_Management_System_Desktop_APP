package org.example.dao;


import org.example.Model.Salle;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDao {

    public List<Salle> getAll() {
        List<Salle> list = new ArrayList<>();
        String sql = "SELECT * FROM salle";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Salle salle = new Salle(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("capacite"),
                        rs.getBoolean("disponible")  // 👈 C’est ici qu'on récupère l'état !
                );
                list.add(salle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

public void add(Salle salle) {
    String sql = "INSERT INTO salle (nom, capacite, disponible) VALUES (?, ?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


        // Vérification des valeurs à insérer
        System.out.println("Nom: " + salle.getNom());
        System.out.println("Capacité: " + salle.getCapacite());

        stmt.setString(1, salle.getNom());
        stmt.setInt(2, salle.getCapacite());
        stmt.setBoolean(3, salle.isDisponible());
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
            stmt.setBoolean(4, s.isDisponible());
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


    public void updateDisponibilite(int salleId, boolean disponible) {
        String sql = "UPDATE salle SET disponible = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, disponible);
            stmt.setInt(2, salleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
