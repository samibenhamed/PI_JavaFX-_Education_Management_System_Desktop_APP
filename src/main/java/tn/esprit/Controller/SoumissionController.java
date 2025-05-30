package tn.esprit.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.entities.Soumission;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SoumissionController {

    @FXML private TableView<Soumission> tableSoumissions;
    @FXML private TableColumn<Soumission, Integer> devoirIdCol;
    @FXML private TableColumn<Soumission, Integer> eleveIdCol;
    @FXML private TableColumn<Soumission, LocalDateTime> dateSoumissionCol;
    @FXML private TableColumn<Soumission, String> contenuCol;
    @FXML private TableColumn<Soumission, String> cheminFichierCol;

    @FXML private TextArea contenuField;
    @FXML private TextField cheminFichierField;
    @FXML private DatePicker dateSoumissionPicker;

    @FXML private Button boutonAjouter;
    @FXML private Button boutonModifier;
    @FXML private Button boutonSupprimer;

    private final ObservableList<Soumission> soumissions = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Bind columns
        devoirIdCol.setCellValueFactory(new PropertyValueFactory<>("devoirId"));
        eleveIdCol.setCellValueFactory(new PropertyValueFactory<>("eleveId"));
        dateSoumissionCol.setCellValueFactory(new PropertyValueFactory<>("dateSoumission"));
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        cheminFichierCol.setCellValueFactory(new PropertyValueFactory<>("cheminFichier"));

        // Example initial data (can be removed)
        soumissions.add(new Soumission(1, 101, 202, LocalDateTime.now(), "Contenu de la soumission", "/chemin/fichier.pdf"));

        tableSoumissions.setItems(soumissions);

        boutonModifier.setDisable(true);
        boutonSupprimer.setDisable(true);

        // Update fields when a row is selected
        tableSoumissions.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                boutonModifier.setDisable(false);
                boutonSupprimer.setDisable(false);

                contenuField.setText(newSelection.getContenu());
                cheminFichierField.setText(newSelection.getCheminFichier());
                dateSoumissionPicker.setValue(newSelection.getDateSoumission().toLocalDate());
            } else {
                boutonModifier.setDisable(true);
                boutonSupprimer.setDisable(true);

                contenuField.clear();
                cheminFichierField.clear();
                dateSoumissionPicker.setValue(null);
            }
        });

        // Button actions
        boutonAjouter.setOnAction(e -> ajouterSoumission());
        boutonModifier.setOnAction(e -> modifierSoumission());
        boutonSupprimer.setOnAction(e -> supprimerSoumission());
    }

    @FXML
    public void ajouterSoumission() {
        if (!validerChamps()) return;

        int newId = soumissions.size() + 1;
        int devoirId = 101; // à adapter selon contexte réel
        int eleveId = 202;  // à adapter selon contexte réel

        LocalDate date = dateSoumissionPicker.getValue();
        LocalDateTime dateSoumission = LocalDateTime.of(date, LocalTime.now());

        Soumission nouvelle = new Soumission(newId, devoirId, eleveId, dateSoumission,
                contenuField.getText().trim(), cheminFichierField.getText().trim());

        soumissions.add(nouvelle);
        clearFields();
        showInfo("Soumission ajoutée avec succès.");
    }

    @FXML
    public void modifierSoumission() {
        Soumission selected = tableSoumissions.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Veuillez sélectionner une soumission à modifier.");
            return;
        }

        if (!validerChamps()) return;

        LocalDate date = dateSoumissionPicker.getValue();
        LocalDateTime dateSoumission = LocalDateTime.of(date, LocalTime.now());

        selected.setContenu(contenuField.getText().trim());
        selected.setCheminFichier(cheminFichierField.getText().trim());
        selected.setDateSoumission(dateSoumission);

        tableSoumissions.refresh();
        clearFields();
        showInfo("Soumission modifiée avec succès.");
    }

    @FXML
    public void supprimerSoumission() {
        Soumission selected = tableSoumissions.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("Veuillez sélectionner une soumission à supprimer.");
            return;
        }
        soumissions.remove(selected);
        clearFields();
        showInfo("Soumission supprimée.");
    }

    private boolean validerChamps() {
        String contenu = contenuField.getText();
        String cheminFichier = cheminFichierField.getText();
        LocalDate date = dateSoumissionPicker.getValue();

        if (contenu == null || contenu.trim().isEmpty()) {
            showWarning("Le champ 'Contenu' est obligatoire.");
            return false;
        }
        if (cheminFichier == null || cheminFichier.trim().isEmpty()) {
            showWarning("Le champ 'Chemin du fichier' est obligatoire.");
            return false;
        }
        if (date == null) {
            showWarning("La 'Date de soumission' est obligatoire.");
            return false;
        }
        if (date.isAfter(LocalDate.now())) {
            showWarning("La date de soumission ne peut pas être dans le futur.");
            return false;
        }
        return true;
    }

    private void clearFields() {
        contenuField.clear();
        cheminFichierField.clear();
        dateSoumissionPicker.setValue(null);
        tableSoumissions.getSelectionModel().clearSelection();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Attention");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
