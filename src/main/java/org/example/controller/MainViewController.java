package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Model.Enseignant;
import org.example.dao.EnseignantDAO;

public class MainViewController {

    @FXML private TableView<Enseignant> tableEnseignants;
    @FXML private TableColumn<Enseignant, String> nomCol;
    @FXML private TableColumn<Enseignant, String> prenomCol;
    @FXML private TableColumn<Enseignant, String> emailCol;
    @FXML private TableColumn<Enseignant, String> departementCol;

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField departementField;

    private ObservableList<Enseignant> enseignants;
    private final EnseignantDAO dao = new EnseignantDAO();

    @FXML
    public void initialize() {
        // Initialiser les colonnes
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        prenomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        departementCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartement()));

        // Charger les enseignants depuis la base (ou autre source)
        enseignants = FXCollections.observableArrayList(dao.getAll());
        tableEnseignants.setItems(enseignants);
    }

    @FXML
    private void ajouterEnseignant() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String departement = departementField.getText();


        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || departement.isEmpty()) {
            showAlert("Tous les champs sont requis.");
            return;
        }

        int id = 0;
        Enseignant e = new Enseignant(id, nom, prenom, email, departement);
        dao.add(e); // si ta DAO l’enregistre en base
        enseignants.add(e);

        clearFields();
    }

    @FXML
    private void supprimerEnseignant() {
        Enseignant selected = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dao.delete(selected.getId()); // facultatif selon DAO
            enseignants.remove(selected);
        } else {
            showAlert("Veuillez sélectionner un enseignant à supprimer.");
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        departementField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
