package controllers.GestionDevoirsControllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import dao.DevoirDAO;
import entities.Devoir;

import java.sql.SQLException;
import java.time.LocalDate;

public class DevoirController {

    @FXML private TableView<Devoir> tableDevoirs;
    @FXML private TableColumn<Devoir, String> titreCol;
    @FXML private TableColumn<Devoir, String> descriptionCol;
    @FXML private TableColumn<Devoir, LocalDate> dateLimiteCol;

    @FXML private Button boutonAjouter;
    @FXML private Button boutonModifier;
    @FXML private Button boutonSupprimer;

    @FXML private TextField titreField;
    @FXML private TextArea descriptionField;
    @FXML private DatePicker dateLimitePicker;

    @FXML private Label erreurLabel;
    @FXML private HBox errorBox;

    private final ObservableList<Devoir> devoirs = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateLimiteCol.setCellValueFactory(new PropertyValueFactory<>("dateLimite"));

        try {
            devoirs.addAll(DevoirDAO.getAllDevoirs());
        } catch (SQLException e) {
            afficherMessage("Erreur de chargement des devoirs : " + e.getMessage(), true);
        }

        tableDevoirs.setItems(devoirs);

        boutonModifier.setDisable(true);
        boutonSupprimer.setDisable(true);

        tableDevoirs.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            boutonModifier.setDisable(!selected);
            boutonSupprimer.setDisable(!selected);

            if (selected) {
                titreField.setText(newSel.getTitre());
                descriptionField.setText(newSel.getDescription());
                dateLimitePicker.setValue(newSel.getDateLimite());
                clearFieldStyles();
                hideError();
            } else {
                reinitialiserChamps();
            }
        });
    }

    @FXML
    public void ajouterDevoir() {
        if (!validerChamps()) return;

        Devoir nouveau = new Devoir(0, titreField.getText().trim(), descriptionField.getText().trim(),
                dateLimitePicker.getValue(), "", 1, 1, 1);

        try {
            DevoirDAO.insertDevoir(nouveau);
            devoirs.clear();
            devoirs.addAll(DevoirDAO.getAllDevoirs());
            afficherMessage("Devoir ajouté avec succès.", false);
            reinitialiserChamps();
            appliquerSuccesStyle();
        } catch (SQLException e) {
            afficherMessage("Erreur lors de l'insertion en base de données : " + e.getMessage(), true);
        }
    }

    @FXML
    public void modifierDevoir() {
        Devoir selected = tableDevoirs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherMessage("Veuillez sélectionner un devoir à modifier.", true);
            return;
        }

        if (!validerChamps()) return;

        selected.setTitre(titreField.getText().trim());
        selected.setDescription(descriptionField.getText().trim());
        selected.setDateLimite(dateLimitePicker.getValue());

        try {
            DevoirDAO.updateDevoir(selected);
            devoirs.clear();
            devoirs.addAll(DevoirDAO.getAllDevoirs());
            afficherMessage("Devoir modifié avec succès.", false);
            appliquerSuccesStyle();
        } catch (SQLException e) {
            afficherMessage("Erreur lors de la mise à jour : " + e.getMessage(), true);
        }
    }

    @FXML
    public void supprimerDevoir() {
        Devoir selected = tableDevoirs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            afficherMessage("Veuillez sélectionner un devoir à supprimer.", true);
            return;
        }

        try {
            DevoirDAO.deleteDevoirById(selected.getId());
            devoirs.clear();
            devoirs.addAll(DevoirDAO.getAllDevoirs());
            afficherMessage("Devoir supprimé avec succès.", false);
            reinitialiserChamps();
        } catch (SQLException e) {
            afficherMessage("Erreur lors de la suppression : " + e.getMessage(), true);
        }
    }

    private void reinitialiserChamps() {
        titreField.clear();
        descriptionField.clear();
        dateLimitePicker.setValue(null);
        clearFieldStyles();
        hideError();
        tableDevoirs.getSelectionModel().clearSelection();
    }

    private boolean validerChamps() {
        clearFieldStyles();
        hideError();

        if (titreField.getText() == null || titreField.getText().trim().isEmpty()) {
            appliquerErreurStyle(titreField);
            afficherMessage("Le champ 'Titre' est obligatoire.", true);
            return false;
        }

        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            appliquerErreurStyle(descriptionField);
            afficherMessage("Le champ 'Description' est obligatoire.", true);
            return false;
        }

        if (dateLimitePicker.getValue() == null) {
            appliquerErreurStyle(dateLimitePicker);
            afficherMessage("Le champ 'Date limite' est obligatoire.", true);
            return false;
        }

        if (dateLimitePicker.getValue().isBefore(LocalDate.now())) {
            appliquerErreurStyle(dateLimitePicker);
            afficherMessage("La date limite ne peut pas être dans le passé.", true);
            return false;
        }

        return true;
    }

    private void afficherMessage(String message, boolean isErreur) {
        if (isErreur) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText("Attention");
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            erreurLabel.setText(message);
            errorBox.setVisible(true);
            errorBox.setManaged(true);
            erreurLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
        }
    }

    private void hideError() {
        errorBox.setVisible(false);
        errorBox.setManaged(false);
        erreurLabel.setText("");
    }

    private void appliquerErreurStyle(Control control) {
        if (!control.getStyleClass().contains("error-border")) {
            control.getStyleClass().add("error-border");
        }
    }

    private void appliquerSuccesStyle() {
        appliquerSuccesStyle(titreField);
        appliquerSuccesStyle(descriptionField);
        appliquerSuccesStyle(dateLimitePicker);

        new Thread(() -> {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {}
            Platform.runLater(this::clearFieldStyles);
        }).start();
    }

    private void appliquerSuccesStyle(Control control) {
        if (!control.getStyleClass().contains("success-border")) {
            control.getStyleClass().add("success-border");
        }
    }

    private void clearFieldStyles() {
        clearStyleClass(titreField, "error-border", "success-border");
        clearStyleClass(descriptionField, "error-border", "success-border");
        clearStyleClass(dateLimitePicker, "error-border", "success-border");
    }

    private void clearStyleClass(Control control, String... classes) {
        for (String c : classes) {
            control.getStyleClass().remove(c);
        }
    }
}



