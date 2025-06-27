package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.Model.Enseignant;
import org.example.Model.Seance;
import org.example.dao.EnseignantDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EnseignantController implements Initializable {

    @FXML
    private TableColumn<Enseignant, String> nomCol, prenomCol, emailCol, departementCol;

    @FXML
    private TextField nomField, prenomField, emailField, departementField;

    @FXML
    private TableView<Enseignant> tableEnseignants;

    @FXML
    private Button modifierButton;

    private Enseignant selectedEnseignant;


    private ObservableList<Enseignant> enseignants = FXCollections.observableArrayList();
    private final EnseignantDAO enseignantDAO = new EnseignantDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        departementCol.setCellValueFactory(new PropertyValueFactory<>("departement"));

        loadEnseignantsFromDB();
        tableEnseignants.setItems(enseignants);

        tableEnseignants.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomField.setText(newSelection.getNom());
                prenomField.setText(newSelection.getPrenom());
                emailField.setText(newSelection.getEmail());
                departementField.setText(newSelection.getDepartement());
            }
        });

        // ✅ Fix: Assign correct action
        modifierButton.setOnAction(this::modifierEnseignant);

        tableEnseignants.setRowFactory(tv -> {
            TableRow<Enseignant> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    tableEnseignants.getSelectionModel().clearSelection(); // ensure selection is cleared
                    clearFormFields(); // clear form
                }
            });
            return row;
        });
    }

    private void clearFormFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        departementField.clear();

        selectedEnseignant = null;
    }

    private void loadEnseignantsFromDB() {
        enseignants.clear();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/schedule_management?useSSL=false&serverTimezone=UTC",
                "root", // Username
                ""

        )) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nom, prenom, email, departement FROM enseignant");

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
    }

    @FXML
    private void ajouterEnseignant(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String departement = departementField.getText();

        if (!nom.isEmpty() && !prenom.isEmpty() && !email.isEmpty() && !departement.isEmpty()) {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/schedule_management?useSSL=false&serverTimezone=UTC",
                    "root", // Username
                    "")) {

                String sql = "INSERT INTO enseignant (nom, prenom, email, departement) VALUES (?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                stmt.setString(3, email);
                stmt.setString(4, departement);

                stmt.executeUpdate();

                enseignants.add(new Enseignant(nom, prenom, email, departement));

                nomField.clear();
                prenomField.clear();
                emailField.clear();
                departementField.clear();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Tous les champs doivent être remplis.");
        }
    }

    @FXML
    public void supprimerEnseignant(ActionEvent event) {
        Enseignant selected = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cet enseignant ?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try (Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/schedule_management?useSSL=false&serverTimezone=UTC",
                        "root", // Username
                        "")) {

                    String sql = "DELETE FROM enseignant WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();

                    enseignants.remove(selected);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void modifierEnseignant(ActionEvent event) {
        Enseignant selected = tableEnseignants.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Aucun enseignant sélectionné", "Veuillez sélectionner un enseignant à modifier.");
            return;
        }

        String newNom = nomField.getText().trim();
        String newPrenom = prenomField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newDepartement = departementField.getText().trim();

        if (newNom.isEmpty() || newPrenom.isEmpty() || newEmail.isEmpty() || newDepartement.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs avant de modifier.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setContentText("Confirmez-vous la modification ?");
        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            selected.setNom(newNom);
            selected.setPrenom(newPrenom);
            selected.setEmail(newEmail);
            selected.setDepartement(newDepartement);

            enseignantDAO.update(selected);

            refreshTableEnseignants();
            nomField.clear();
            prenomField.clear();
            emailField.clear();
            departementField.clear();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "L'enseignant a été modifié avec succès.");
        }
    }

    private void refreshTableEnseignants() {
        List<Enseignant> list = EnseignantDAO.getAll();
        enseignants.setAll(list);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    public void ouvrirSeances(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/seance.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Séances");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void ouvrirSalles(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/salle.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Salles");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
