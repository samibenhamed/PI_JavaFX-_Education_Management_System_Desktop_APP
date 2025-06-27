package controllers.MatieresControllers;

import entities.Matiere;
import entities.Professeur;
import entities.Type;
import services.GmailService;
import services.MatiereService;
import services.ProfesseurService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.util.List;

public class UpdateController {
    @FXML
    private ComboBox<Professeur> ProfesseurField;

    @FXML
    private ComboBox<Type> nomField;

    @FXML
    private TextField niveauField;

    @FXML
    private TextField descriptionField;

    private MatiereService matiereService = new MatiereService();

    private Matiere selectedMatiere;
    private final ProfesseurService professeurService = new ProfesseurService();


    // Initialize ComboBox on controller load
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

    // Set the existing matiere's data in the UI fields
    public void setMatiere(Matiere matiere) {
        this.selectedMatiere = matiere;
        nomField.setValue(matiere.getNom()); // setValue for ComboBox
        niveauField.setText(matiere.getNiveau());
        descriptionField.setText(matiere.getDescription());
    }

    @FXML
    public void handleUpdate(ActionEvent event) {
        if (selectedMatiere != null) {
            selectedMatiere.setNom(nomField.getValue()); // get selected enum
            selectedMatiere.setNiveau(niveauField.getText());
            selectedMatiere.setDescription(descriptionField.getText());

            Professeur selectedProfesseur = ProfesseurField.getValue();
            if (selectedProfesseur != null) {
                selectedMatiere.setProfesseur(selectedProfesseur); // Assign the full Professeur object

                // 💡 Send email to updated professor
                try {
                    String object = "Subject Assignment Updated";
                    String profName = capitalizeEachWord(selectedProfesseur.getNom());

                    String message = "Hello Mr/Ms " + profName + "\n"
                            + "Your subject assignment has been updated to: " + selectedMatiere.getNom() + "\n"
                            + "For class level: " + selectedMatiere.getNiveau();

                    GmailService mailService = new GmailService();
                    mailService.sendEmail(
                            selectedProfesseur.getEmail(),
                            object,
                            message
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Failed to send update email to professor.");
                }
            }

            matiereService.updateMatiere(selectedMatiere);
            navigateToMatiereView();
        }
    }


    @FXML
    public void handleReturn(ActionEvent event) {
        navigateToMatiereView();
    }

    private void navigateToMatiereView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Matieres/MatiereView.fxml"));
            Parent viewRoot = loader.load();
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.setScene(new Scene(viewRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
