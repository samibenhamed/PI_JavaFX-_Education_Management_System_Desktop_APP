package org.example.dao;


import org.example.Model.Module;
import org.example.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleDAO {

    public List<Module> getAll() {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM module";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                modules.add(new Module(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("volume_horaire")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modules;
    }

    public void add(Module m) {
        String sql = "INSERT INTO module (nom, volume_horaire) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getNom());
            stmt.setInt(2, m.getVolumeHoraire());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Module m) {
        String sql = "UPDATE module SET nom=?, volume_horaire=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, m.getNom());
            stmt.setInt(2, m.getVolumeHoraire());
            stmt.setInt(3, m.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM module WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
