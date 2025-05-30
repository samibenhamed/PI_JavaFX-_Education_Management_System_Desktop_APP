package controllers.ProfesseursControllers;

import entities.*;
import services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AddController {
    @FXML
    private Label errorLabel;

    @FXML
    private Button returnButton;

    @FXML
    private Button ajouterButton;

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField specialiteField;

    private final ProfesseurService professeurService = new ProfesseurService();  // Use ProfesseurService

    @FXML
    private void handleReturn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Professeurs/ProfesseurView.fxml"));
            Parent professeurView = loader.load();

            // Get the current stage using the return button
            Stage stage = (Stage) returnButton.getScene().getWindow();
            Scene scene = new Scene(professeurView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        String nom = nomField.getText().trim();
        String email = emailField.getText().trim();
        String specialite = specialiteField.getText().trim();

        clearFieldStyles();
        errorLabel.setVisible(false);

        if (nom.isEmpty() || nom.length() < 2 || nom.length() > 10 || !nom.matches("[a-zA-ZÀ-ÿ\\s\\-']+")) {
            nomField.getStyleClass().add("error");
            errorLabel.setText("Le nom est requis et doit contenir uniquement des lettres (2-50 caractères).");
            errorLabel.setVisible(true);
            return;
        }

        if (email.isEmpty() || !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            emailField.getStyleClass().add("error");
            errorLabel.setText("L'adresse email n'est pas valide.");
            errorLabel.setVisible(true);
            return;
        }

        if (specialite.isEmpty() || specialite.length() > 25) {
            specialiteField.getStyleClass().add("error");
            errorLabel.setText("La spécialité est requise (maximum 100 caractères).");
            errorLabel.setVisible(true);
            return;
        }

        // Success
        Professeur newProfesseur = new Professeur(0, nom, email, specialite);
        professeurService.addProfesseur(newProfesseur);

        errorLabel.setVisible(false);
        clearFields();
        System.out.println("Professeur ajouté !");
    }



    private void clearFields() {
        nomField.clear();
        emailField.clear();
        specialiteField.clear();
    }
    private void clearFieldStyles() {
        nomField.getStyleClass().remove("error");
        emailField.getStyleClass().remove("error");
        specialiteField.getStyleClass().remove("error");
    }


}
