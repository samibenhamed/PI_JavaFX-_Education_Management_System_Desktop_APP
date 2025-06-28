package dao;

import entities.Devoir;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DevoirDAO {

    public static List<Devoir> getAllDevoirs() throws SQLException {
        List<Devoir> list = new ArrayList<>();
        String sql = "SELECT * FROM devoir";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Devoir d = new Devoir(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getDate("date_limite").toLocalDate(),
                        rs.getString("chemin_fichier"),
                        rs.getInt("matiere_id"),
                        rs.getInt("enseignant_id"),
                        rs.getInt("classe_id")
                );
                list.add(d);
            }
        }
        return list;
    }

    public static void insertDevoir(Devoir d) throws SQLException {
        String sql = "INSERT INTO devoir (titre, description, date_limite, chemin_fichier, matiere_id, enseignant_id, classe_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getTitre());
            stmt.setString(2, d.getDescription());
            stmt.setDate(3, Date.valueOf(d.getDateLimite()));
            stmt.setString(4, d.getCheminFichier());
            stmt.setInt(5, d.getMatiereId());
            stmt.setInt(6, d.getEnseignantId());
            stmt.setInt(7, d.getClasseId());
            stmt.executeUpdate();
        }
    }

    public static void updateDevoir(Devoir d) throws SQLException {
        String sql = "UPDATE devoir SET titre = ?, description = ?, date_limite = ?, chemin_fichier = ?, matiere_id = ?, enseignant_id = ?, classe_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getTitre());
            stmt.setString(2, d.getDescription());
            stmt.setDate(3, Date.valueOf(d.getDateLimite()));
            stmt.setString(4, d.getCheminFichier());
            stmt.setInt(5, d.getMatiereId());
            stmt.setInt(6, d.getEnseignantId());
            stmt.setInt(7, d.getClasseId());
            stmt.setInt(8, d.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteDevoirById(int id) throws SQLException {
        String sql = "DELETE FROM devoir WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Méthode optionnelle pour récupérer uniquement les IDs des devoirs
    public static List<Integer> getAllDevoirIds() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT id FROM devoir";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
        }

        return ids;
    }
}
