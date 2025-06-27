package org.example.controller;



import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Model.Enseignant;
import org.example.dao.ModuleDAO;
import org.example.Model.Module;

import java.net.URL;
import java.util.ResourceBundle;

public class ModuleController implements Initializable {
    @FXML private TableView<Module> tableModule;
    @FXML private TableColumn<Module, String> colNom;
    @FXML private TableColumn<Module, Integer> volumeHoraireCol;
    @FXML private TextField tfNom;
    @FXML private TextField tfVolumeHoraire;

    private  Module selectedModule;


    private final ModuleDAO moduleDAO = new ModuleDAO();
    private final ObservableList<Module> moduleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        volumeHoraireCol.setCellValueFactory(new PropertyValueFactory<>("volumeHoraire"));
        refreshTable();

        tableModule.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tfNom.setText(newSelection.getNom());
                tfVolumeHoraire.setText(String.valueOf(newSelection.getVolumeHoraire()));
            }
        });

        tableModule.setRowFactory(tv -> {
            TableRow<Module> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (row.isEmpty()) {
                    tableModule.getSelectionModel().clearSelection(); // ensure selection is cleared
                    clearFormFields(); // clear form
                }
            });
            return row;
        });

    }


    private void clearFormFields() {
        tfNom.clear();
        tfVolumeHoraire.clear();


        selectedModule = null;
    }


    @FXML
    private void refreshTable() {
        moduleList.setAll(moduleDAO.getAll());
        tableModule.setItems(moduleList);

    }

    @FXML
    private void modifierModule() {
        Module selected = tableModule.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Sélectionnez un module.");
            return;
        }

        String nom = tfNom.getText();
        String volumeHoraireText = tfVolumeHoraire.getText();

        if (nom.isEmpty() || volumeHoraireText.isEmpty()) {
            showAlert("Tous les champs doivent être remplis.");
            return;
        }

        try {
            int volumeHoraire = Integer.parseInt(volumeHoraireText);
            selected.setNom(nom);
            selected.setVolumeHoraire(volumeHoraire);

            moduleDAO.update(selected);
            refreshTable();
            clearFields();
            showConfirmation("Module modifié avec succès !");
        } catch (NumberFormatException e) {
            showAlert("Le volume horaire doit être un entier valide.");
        }
    }



    private void clearFields() {
        tfNom.clear();
        tfVolumeHoraire.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
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

    @FXML
        public void addModule() {
            try {
                String nom = tfNom.getText();
                int volumeHoraire = Integer.parseInt(tfVolumeHoraire.getText());

                moduleDAO.add(new Module(nom, volumeHoraire));
                refreshTable();

                tfNom.clear();
                tfVolumeHoraire.clear();
            } catch (NumberFormatException e) {
                System.err.println("Volume horaire must be a valid number.");
                // Optional: show error dialog to user
            }

        }

    @FXML
    public void deleteModule() {
        Module selected = tableModule.getSelectionModel().getSelectedItem();
        if (selected != null) {
            moduleDAO.delete(selected.getId());
            refreshTable();
        }
    }
}
