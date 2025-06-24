package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Model.Salle;
import org.example.dao.SalleDao;
import javafx.beans.property.SimpleBooleanProperty;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SalleController {

    @FXML private TableView<Salle> tableSalles;
    @FXML private TableColumn<Salle, String> nomCol;
    @FXML private TableColumn<Salle, String> capaciteCol;

    @FXML private TextField nomField;
    @FXML private TextField capaciteField;

    private Salle selectedSalle;

    @FXML
    private TableColumn<Salle, Boolean> disponibleCol;

    private ObservableList<Salle> salles;
    private final SalleDao dao = new SalleDao();
    @FXML
    public void initialize() {
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        capaciteCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacite())));

        // ✅ Cellule logique pour afficher Oui/Non ou bouton plus bas
        disponibleCol.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isDisponible()).asObject());

        // ✅ Cellule personnalisée avec bouton dynamique
        disponibleCol.setCellFactory(column -> new TableCell<Salle, Boolean>() {
            private final Button statusButton = new Button();
            private final SalleDao salleDAO = new SalleDao();

            @Override
            protected void updateItem(Boolean disponible, boolean empty) {
                super.updateItem(disponible, empty);

                if (empty || disponible == null) {
                    setGraphic(null);
                } else {
                    Salle salle = getTableView().getItems().get(getIndex());

                    if (salle.isDisponible()) {
                        statusButton.setText("Disponible");
                        statusButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    } else {
                        statusButton.setText("Indisponible");
                        statusButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    }

                    statusButton.setOnAction(event -> {
                        boolean newStatus = !salle.isDisponible();
                        salle.setDisponible(newStatus);
                        salleDAO.updateDisponibilite(salle.getId(), newStatus);
                        getTableView().refresh();
                    });

                    setGraphic(statusButton);
                }
            }
        });

        // ✅ Chargement des données
        salles = FXCollections.observableArrayList(dao.getAll());
        tableSalles.setItems(salles);

        // ✅ Listener de sélection
        tableSalles.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nomField.setText(newSel.getNom());
                capaciteField.setText(String.valueOf(newSel.getCapacite()));
            }
        });

        // ✅ Vider les champs si clic hors ligne
        tableSalles.setRowFactory(tv -> {
            TableRow<Salle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    tableSalles.getSelectionModel().clearSelection();
                    clearFormFields();
                }
            });
            return row;
        });
    }


    private void clearFormFields() {
        nomField.clear();
        capaciteField.clear();


        selectedSalle = null;

    }




    @FXML
    private void ajouterSalle() {
        if (nomField.getText().isEmpty() || capaciteField.getText().isEmpty()) {
            showAlert("Tous les champs sont requis.");
            return;
        }

        try {
            int capacite = Integer.parseInt(capaciteField.getText());
            String nom = nomField.getText();

            // ✅ Par défaut, une salle ajoutée est disponible
            Salle salle = new Salle(0, nom, capacite, true);

            dao.add(salle);
            salles.add(salle);
            clearFields();

            showConfirmation("Salle ajoutée avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Capacité doit être un nombre valide.");
        }
    }

    @FXML
    private void modifierSalle() {
        Salle selected = tableSalles.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélectionnez une salle.");
            return;
        }

        String nom = nomField.getText();
        String capaciteText = capaciteField.getText();
        Boolean disponible = selected.isDisponible();

        if (nom.isEmpty() || capaciteText.isEmpty()) {
            showAlert("Tous les champs doivent être remplis.");
            return;
        }

        try {
            int capacite = Integer.parseInt(capaciteText);
            selected.setNom(nom);
            selected.setCapacite(capacite);
            selected.setDisponible(disponible);
            dao.update(selected);
            tableSalles.refresh();
            clearFields();
            showConfirmation("Salle modifiée avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Capacité doit être un nombre valide.");
        }
    }


    @FXML
    private void supprimerSalle() {
        Salle selected = tableSalles.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dao.delete(selected.getId());
            salles.remove(selected);
        }
    }

    private void clearFields() {
        nomField.clear();
        capaciteField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private void showConfirmation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
