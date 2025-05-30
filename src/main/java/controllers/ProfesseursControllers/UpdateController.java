package controllers.ProfesseursControllers;

import entities.*;
import services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateController {

    @FXML
    private TextField nomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField specialiteField;

    private ProfesseurService professeurService = new ProfesseurService();  // Use ProfesseurService

    private Professeur selectedProfesseur;

    // This method is called from the previous controller (ViewController)
    public void setProfesseur(Professeur professeur) {
        this.selectedProfesseur = professeur;
        nomField.setText(professeur.getNom());
        emailField.setText(professeur.getEmail());
        specialiteField.setText(professeur.getSpecialite());
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        if (selectedProfesseur != null) {
            selectedProfesseur.setNom(nomField.getText());
            selectedProfesseur.setEmail(emailField.getText());
            selectedProfesseur.setSpecialite(specialiteField.getText());

            professeurService.updateProfesseur(selectedProfesseur);  // Call the update method from ProfesseurService

            // Return to view page (optional)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Professeurs/ProfesseurView.fxml"));
                Parent viewRoot = loader.load();
                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.setScene(new Scene(viewRoot));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleReturn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Professeurs/ProfesseurView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
