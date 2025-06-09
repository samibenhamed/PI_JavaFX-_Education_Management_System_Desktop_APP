package org.example.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.Model.Enseignant;
import org.example.Model.Salle;
import org.example.dao.SalleDao;
import javafx.beans.property.SimpleBooleanProperty;

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
/*
    @FXML
    public void initialize() {
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        capaciteCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacite())));
        salles = FXCollections.observableArrayList(dao.getAll());
        tableSalles.setItems(salles);

        tableSalles.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nomField.setText(newSel.getNom());
                capaciteField.setText(String.valueOf(newSel.getCapacite()));
            }
        });

        tableSalles.setRowFactory(tv -> {
            TableRow<Salle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    tableSalles.getSelectionModel().clearSelection(); // ensure selection is cleared
                    clearFormFields(); // clear form
                }
            });
            return row;
        });
    }
*/
@FXML
public void initialize() {
    nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
    capaciteCol.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacite())));

    salles = FXCollections.observableArrayList(dao.getAll());
    tableSalles.setItems(salles);

    tableSalles.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
        if (newSel != null) {
            nomField.setText(newSel.getNom());
            capaciteField.setText(String.valueOf(newSel.getCapacite()));
        }
    });

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

    // 💡 Ajout du bouton "Disponible" pour chaque ligne
    disponibleCol.setCellFactory(column -> new TableCell<Salle, Boolean>() {
        private final Button statusButton = new Button();
/*
        {
            /
            button.setOnAction(e -> {
                Salle salle = getTableView().getItems().get(getIndex());
                salle.setDisponible(!salle.isDisponible()); // Toggle disponibilité
                updateButtonText(salle);
                getTableView().refresh(); // Rafraîchir l'affichage
            });
        }

        private void updateButtonText(Salle salle) {
            button.setText(salle.isDisponible() ? "Oui" : "Non");
        }
*/
        @Override
        protected void updateItem(Boolean disponible, boolean empty) {
            super.updateItem(disponible, empty);

            if (empty || disponible == null) {
                setGraphic(null);
            } else {
                Salle salle = getTableView().getItems().get(getIndex());

                // Définir le texte et la couleur
                if (disponible) {
                    statusButton.setText("Disponible");
                    statusButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                } else {
                    statusButton.setText("Indisponible");
                    statusButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                }

                // Action pour basculer l'état
                statusButton.setOnAction(event -> {
                    boolean newStatus = !salle.isDisponible();
                    salle.setDisponible(newStatus);
                    getTableView().refresh(); // Mise à jour de l'affichage
                });

                setGraphic(statusButton);
            }
        }
    });



    // Si tu veux afficher initialement "Oui"/"Non" dans la colonne
    disponibleCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isDisponible()).asObject());
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
        int capacite = Integer.parseInt(capaciteField.getText());
        Salle salle = new Salle(0, nomField.getText(), capacite);
        dao.add(salle);
        salles.add(salle);
        clearFields();
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

        if (nom.isEmpty() || capaciteText.isEmpty()) {
            showAlert("Tous les champs doivent être remplis.");
            return;
        }

        try {
            int capacite = Integer.parseInt(capaciteText);
            selected.setNom(nom);
            selected.setCapacite(capacite);
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
