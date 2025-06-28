package dao;

import entities.Soumission;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SoumissionDAO {

    public static List<Soumission> getAllSoumissions() throws SQLException {
        List<Soumission> list = new ArrayList<>();
        String sql = "SELECT * FROM soumission";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Soumission s = new Soumission(
                        rs.getInt("id"),
                        rs.getInt("devoir_id"),
                        rs.getInt("eleve_id"),
                        rs.getTimestamp("date_soumission").toLocalDateTime(),
                        rs.getString("contenu"),
                        rs.getString("chemin_fichier"),
                        rs.getInt("score") // 🆕 ajout du score
                );
                list.add(s);
            }
        }
        return list;
    }

    public static List<Soumission> getSoumissionsByDevoirId(int devoirId) throws SQLException {
        List<Soumission> list = new ArrayList<>();
        String sql = "SELECT * FROM soumission WHERE devoir_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, devoirId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Soumission s = new Soumission(
                            rs.getInt("id"),
                            rs.getInt("devoir_id"),
                            rs.getInt("eleve_id"),
                            rs.getTimestamp("date_soumission").toLocalDateTime(),
                            rs.getString("contenu"),
                            rs.getString("chemin_fichier"),
                            rs.getInt("score") // 🆕 ajout du score
                    );
                    list.add(s);
                }
            }
        }
        return list;
    }

    public static void insertSoumission(Soumission soumission) throws SQLException {
        String sql = "INSERT INTO soumission (devoir_id, eleve_id, date_soumission, contenu, chemin_fichier, score) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, soumission.getDevoirId());
            stmt.setInt(2, soumission.getEleveId());
            stmt.setTimestamp(3, Timestamp.valueOf(soumission.getDateSoumission()));
            stmt.setString(4, soumission.getContenu());
            stmt.setString(5, soumission.getCheminFichier());
            stmt.setInt(6, soumission.getScore()); // 🆕 ajout du score

            stmt.executeUpdate();
        }
    }

    public static void updateSoumission(Soumission soumission) throws SQLException {
        String sql = "UPDATE soumission SET devoir_id = ?, eleve_id = ?, date_soumission = ?, contenu = ?, chemin_fichier = ?, score = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, soumission.getDevoirId());
            stmt.setInt(2, soumission.getEleveId());
            stmt.setTimestamp(3, Timestamp.valueOf(soumission.getDateSoumission()));
            stmt.setString(4, soumission.getContenu());
            stmt.setString(5, soumission.getCheminFichier());
            stmt.setInt(6, soumission.getScore()); // 🆕 ajout du score
            stmt.setInt(7, soumission.getId());

            stmt.executeUpdate();
        }
    }

    public static void deleteSoumission(int id) throws SQLException {
        String sql = "DELETE FROM soumission WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
