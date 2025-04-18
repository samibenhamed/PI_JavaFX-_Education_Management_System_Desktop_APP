package tn.esprit.Conroller;

import tn.esprit.entities.Matiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MatiereController {

    @FXML
    private TableView<Matiere> tableMatiere;

    @FXML
    private TableColumn<Matiere, Integer> colId;

    @FXML
    private TableColumn<Matiere, String> colNom;

    @FXML
    private TableColumn<Matiere, Integer> colCoef;

    @FXML
    private TableColumn<Matiere, Integer> colHeures;

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtCoef;

    @FXML
    private TextField txtHeures;

    private ObservableList<Matiere> listeMatieres = FXCollections.observableArrayList();

    private int idCounter = 1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colCoef.setCellValueFactory(new PropertyValueFactory<>("coefficient"));
        colHeures.setCellValueFactory(new PropertyValueFactory<>("nbHeures"));

        tableMatiere.setItems(listeMatieres);
    }

    @FXML
    private void ajouterMatiere() {
        String nom = txtNom.getText().trim();
        String coefText = txtCoef.getText().trim();
        String heuresText = txtHeures.getText().trim();

        if (nom.isEmpty() || coefText.isEmpty() || heuresText.isEmpty()) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            int coef = Integer.parseInt(coefText);
            int nbHeures = Integer.parseInt(heuresText);
            if (coef <= 0 || nbHeures <= 0) throw new NumberFormatException();

            Matiere m = new Matiere(idCounter++, nom, coef, nbHeures);
            listeMatieres.add(m);
            reinitialiser();
        } catch (NumberFormatException e) {
            showAlert("Erreur de saisie", "Le coefficient et le nombre d'heures doivent être des entiers positifs.");
        }
    }

    @FXML
    private void modifierMatiere() {
        Matiere selected = tableMatiere.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String nom = txtNom.getText().trim();
            String coefText = txtCoef.getText().trim();
            String heuresText = txtHeures.getText().trim();

            if (nom.isEmpty() || coefText.isEmpty() || heuresText.isEmpty()) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            try {
                int coef = Integer.parseInt(coefText);
                int nbHeures = Integer.parseInt(heuresText);
                if (coef <= 0 || nbHeures <= 0) throw new NumberFormatException();

                selected.setNom(nom);
                selected.setCoefficient(coef);
                selected.setNbHeures(nbHeures);
                tableMatiere.refresh();
                reinitialiser();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le coefficient et le nombre d'heures doivent être des entiers positifs.");
            }
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une matière à modifier.");
        }
    }

    @FXML
    private void supprimerMatiere() {
        Matiere selected = tableMatiere.getSelectionModel().getSelectedItem();
        if (selected != null) {
            listeMatieres.remove(selected);
            reinitialiser();
        } else {
            showAlert("Aucune sélection", "Veuillez sélectionner une matière à supprimer.");
        }
    }

    @FXML
    private void reinitialiser() {
        txtNom.clear();
        txtCoef.clear();
        txtHeures.clear();
        tableMatiere.getSelectionModel().clearSelection();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
