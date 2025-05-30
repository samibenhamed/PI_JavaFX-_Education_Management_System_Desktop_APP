package controllers.MatieresControllers;

import entities.*;
import services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class AddController {
    @FXML
    private ComboBox<Professeur> ProfesseurField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button returnButton;
    @FXML
    private Button ajouterButton;
    @FXML
    private ComboBox<Type> nomField;
    @FXML
    private TextField niveauField;
    @FXML
    private TextField descriptionField;

    private final MatiereService matiereService = new MatiereService();
    private final ProfesseurService professeurService = new ProfesseurService();

    @FXML
    public void initialize() {
        // Populate the ComboBoxes
        nomField.getItems().setAll(Type.values());
        List<Professeur> professeurs = professeurService.getAllProfesseurs();
        ProfesseurField.getItems().setAll(professeurs);

        // Set how the Professeur is displayed in ComboBox (by name)
        ProfesseurField.setConverter(new StringConverter<Professeur>() {
            @Override
            public String toString(Professeur prof) {
                return prof != null ? prof.getNom() : "";
            }

            @Override
            public Professeur fromString(String string) {
                // Usually not needed unless editable ComboBox
                return null;
            }
        });
    }

    @FXML
    private void handleReturn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Matieres/MatiereView.fxml"));
            Parent matiereView = loader.load();
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(matiereView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAjouter(ActionEvent event) throws MessagingException {
        Type selectedType = nomField.getValue();
        Professeur selectedProfesseur = ProfesseurField.getValue();
        String niveau = niveauField.getText().trim();
        String description = descriptionField.getText().trim();

        // Validation
        if (selectedType == null || selectedProfesseur == null || niveau.isEmpty() || description.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        if (niveau.length() > 10) {
            errorLabel.setText("Le niveau est trop long (max 10 caractères).");
            return;
        }

        if (description.length() > 255) {
            errorLabel.setText("La description est trop longue (max 255 caractères).");
            return;
        }

        // Success
        Matiere newMatiere = new Matiere(selectedType, niveau, description);
        newMatiere.setProfesseur(selectedProfesseur);


        String object = "You have been added to a new subject";
        String profName = capitalizeEachWord(selectedProfesseur.getNom());  // Capitalize name

        String message = "Hello Mr/Ms " + profName + "\n"
                + "You have been selected for the following subject: " + newMatiere.getNom() + "\n"
                + "For class level: " + newMatiere.getNiveau();

        GmailService mailService = new GmailService();
        mailService.sendEmail(
                selectedProfesseur.getEmail(),
                object,
                message
        );

        matiereService.addMatiere(newMatiere);


        System.out.println("Matière ajoutée avec succé !");
        errorLabel.setText("");
        clearFields();
    }

    private void clearFields() {
        nomField.setValue(null);
        ProfesseurField.setValue(null);
        niveauField.clear();
        descriptionField.clear();
    }
    public String capitalizeEachWord(String input) {
        if (input == null || input.isEmpty()) return input;
        String[] words = input.trim().split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }

}
