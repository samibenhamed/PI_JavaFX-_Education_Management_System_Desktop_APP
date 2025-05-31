/*package org.example.controller;

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




        // Remplir les champs quand on sélectionne une ligne
        tableEnseignants.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomField.setText(newSelection.getNom());
                prenomField.setText(newSelection.getPrenom());
                emailField.setText(newSelection.getEmail());
                departementField.setText(newSelection.getDepartement());
            }
        });


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


    @FXML
    public void modifierEnseignant() {
        Enseignant selected = tableEnseignants.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Veuillez sélectionner un enseignant à modifier.");
            return;
        }

        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String departement = departementField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || departement.isEmpty()) {
            showAlert("Tous les champs sont requis pour la modification.");
            return;
        }

        // Update the selected enseignant
        selected.setNom(nom);
        selected.setPrenom(prenom);
        selected.setEmail(email);
        selected.setDepartement(departement);

        // Update in database
        dao.update(selected); // You need to implement this method in your DAO

        // Refresh table
        tableEnseignants.refresh();

        showAlert("L'enseignant a été modifié avec succès.");
        clearFields();
    }
}
*/
package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.Model.Enseignant;

import java.io.IOException;

public class MainViewController {
    @FXML private TabPane mainTabPane;
    @FXML private Tab enseignantsTab;
    @FXML private Tab sallesTab;
    @FXML private Tab seancesTab;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField departementField;
    @FXML private TableView<Enseignant> tableEnseignants;
     private ObservableList<Enseignant> enseignants = FXCollections.observableArrayList();
    @FXML
    private AnchorPane seanceInclude;
    @FXML
    public void initialize() {
        // Optional: Add logic if needed when switching tabs

    }


}
