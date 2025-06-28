package controllers.GestionDevoirsControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import dao.DevoirDAO;
import dao.SoumissionDAO;
import entities.Devoir;
import entities.Soumission;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class SoumissionController {

    @FXML private TableView<Soumission> tableSoumissions;
    @FXML private TableColumn<Soumission, Integer> devoirIdCol;
    @FXML private TableColumn<Soumission, Integer> eleveIdCol;
    @FXML private TableColumn<Soumission, LocalDateTime> dateSoumissionCol;
    @FXML private TableColumn<Soumission, String> contenuCol;
    @FXML private TableColumn<Soumission, String> cheminFichierCol;
    @FXML private TableColumn<Soumission, Integer> scoreCol;

    @FXML private ComboBox<Devoir> devoirComboBox;
    @FXML private DatePicker dateSoumissionPicker;
    @FXML private TextArea contenuField;
    @FXML private TextField cheminFichierField;
    @FXML private TextField scoreField; // ✅ Ajout du champ scoreField
    @FXML private Button boutonAjouter, boutonModifier, boutonSupprimer;

    private final ObservableList<Soumission> soumissions = FXCollections.observableArrayList();
    private final ObservableList<Devoir> devoirs = FXCollections.observableArrayList();
    private boolean isInternalUpdate = false;

    @FXML
    public void initialize() {
        devoirIdCol.setCellValueFactory(new PropertyValueFactory<>("devoirId"));
        eleveIdCol.setCellValueFactory(new PropertyValueFactory<>("eleveId"));
        dateSoumissionCol.setCellValueFactory(new PropertyValueFactory<>("dateSoumission"));
        contenuCol.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        cheminFichierCol.setCellValueFactory(new PropertyValueFactory<>("cheminFichier"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        tableSoumissions.setItems(soumissions);
        devoirComboBox.setItems(devoirs);

        chargerDevoirs();
        chargerSoumissions();

        tableSoumissions.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (isInternalUpdate) return;
            isInternalUpdate = true;
            try {
                if (newSel != null) {
                    boutonModifier.setDisable(false);
                    boutonSupprimer.setDisable(false);
                    contenuField.setText(newSel.getContenu());
                    cheminFichierField.setText(newSel.getCheminFichier());
                    dateSoumissionPicker.setValue(newSel.getDateSoumission().toLocalDate());
                    devoirComboBox.setValue(trouverDevoirParId(newSel.getDevoirId()));
                    scoreField.setText(String.valueOf(newSel.getScore())); // ✅
                } else {
                    clearFields();
                }
            } finally {
                isInternalUpdate = false;
            }
        });

        boutonModifier.setDisable(true);
        boutonSupprimer.setDisable(true);
    }

    private void chargerSoumissions() {
        try {
            List<Soumission> list = SoumissionDAO.getAllSoumissions();
            soumissions.setAll(list);
        } catch (SQLException e) {
            showWarning("Erreur lors du chargement des soumissions : " + e.getMessage());
        }
    }

    private void chargerDevoirs() {
        try {
            List<Devoir> list = DevoirDAO.getAllDevoirs();
            devoirs.setAll(list);
        } catch (SQLException e) {
            showWarning("Erreur lors du chargement des devoirs : " + e.getMessage());
        }
    }

    private void clearFields() {
        contenuField.clear();
        cheminFichierField.clear();
        dateSoumissionPicker.setValue(null);
        devoirComboBox.getSelectionModel().clearSelection();
        scoreField.clear(); // ✅
        boutonModifier.setDisable(true);
        boutonSupprimer.setDisable(true);
    }

    @FXML
    private void ajouterSoumission() {
        Devoir selectedDevoir = devoirComboBox.getValue();
        if (selectedDevoir == null || dateSoumissionPicker.getValue() == null || scoreField.getText().isEmpty()) {
            showWarning("Veuillez remplir tous les champs !");
            return;
        }

        int score;
        try {
            score = Integer.parseInt(scoreField.getText());
            if (score < 0 || score > 20) {
                showWarning("Le score doit être entre 0 et 20.");
                return;
            }
        } catch (NumberFormatException e) {
            showWarning("Score invalide !");
            return;
        }

        Soumission soumission = new Soumission(
                0,
                selectedDevoir.getId(),
                1,
                dateSoumissionPicker.getValue().atStartOfDay(),
                contenuField.getText(),
                cheminFichierField.getText(),
                score
        );

        try {
            SoumissionDAO.insertSoumission(soumission);
            chargerSoumissions();
            clearFields();
        } catch (SQLException e) {
            showWarning("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    @FXML
    private void modifierSoumission() {
        Soumission selected = tableSoumissions.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        int score;
        try {
            score = Integer.parseInt(scoreField.getText());
            if (score < 0 || score > 20) {
                showWarning("Le score doit être entre 0 et 20.");
                return;
            }
        } catch (NumberFormatException e) {
            showWarning("Score invalide !");
            return;
        }

        selected.setContenu(contenuField.getText());
        selected.setCheminFichier(cheminFichierField.getText());
        selected.setDateSoumission(dateSoumissionPicker.getValue().atStartOfDay());
        selected.setDevoirId(devoirComboBox.getValue().getId());
        selected.setScore(score);

        try {
            SoumissionDAO.updateSoumission(selected);
            chargerSoumissions();
            clearFields();
        } catch (SQLException e) {
            showWarning("Erreur lors de la modification : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerSoumission() {
        Soumission selected = tableSoumissions.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            SoumissionDAO.deleteSoumission(selected.getId());
            chargerSoumissions();
            clearFields();
        } catch (SQLException e) {
            showWarning("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    private void handleUploadPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
        File file = fileChooser.showOpenDialog(cheminFichierField.getScene().getWindow());
        if (file != null) {
            cheminFichierField.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void filtrerParDevoir() {
        if (isInternalUpdate) return;

        Devoir selectedDevoir = devoirComboBox.getSelectionModel().getSelectedItem();
        if (selectedDevoir != null) {
            try {
                isInternalUpdate = true;
                List<Soumission> filtered = SoumissionDAO.getSoumissionsByDevoirId(selectedDevoir.getId());
                soumissions.setAll(filtered);
                selectFirstIfExists();
            } catch (SQLException e) {
                showWarning("Erreur lors du filtrage : " + e.getMessage());
            } finally {
                isInternalUpdate = false;
            }
        } else {
            try {
                isInternalUpdate = true;
                chargerSoumissions();
                selectFirstIfExists();
            } finally {
                isInternalUpdate = false;
            }
        }
    }

    private Devoir trouverDevoirParId(int id) {
        for (Devoir d : devoirComboBox.getItems()) {
            if (d.getId() == id) return d;
        }
        return null;
    }

    private void selectFirstIfExists() {
        if (!soumissions.isEmpty()) {
            tableSoumissions.getSelectionModel().selectFirst();
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Avertissement");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
