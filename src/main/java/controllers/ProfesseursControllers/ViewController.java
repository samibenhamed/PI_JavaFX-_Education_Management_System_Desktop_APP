package controllers.ProfesseursControllers;

import entities.*;
import services.*;
import com.lowagie.text.*;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    @FXML
    private TextField searchField;
    @FXML
    private ImageView returnImageView;
    @FXML
    private ImageView logoImageView;
    @FXML
    private Button PDFButton;
    @FXML
    private Label ReturnLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button updateButton;  // Add Update button

    @FXML
    private TableView<Professeur> ProfesseurTable;  // Change to Professeur

    @FXML
    private TableColumn<Professeur, Integer> idColumn;  // Update to Professeur
    @FXML
    private TableColumn<Professeur, String> nomColumn;  // Update to Professeur
    @FXML
    private TableColumn<Professeur, String> emailColumn;  // Add Email column
    @FXML
    private TableColumn<Professeur, String> specialiteColumn;  // Add Specialite column

    private ProfesseurService professeurService = new ProfesseurService();  // Change to ProfesseurService
    private ObservableList<Professeur> professeurList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Load the image from your resources (put the image in your resources folder, like 'images/return.png')
        Image returnImage = new Image(getClass().getResource("/com/example/mariem/Images/return.png").toExternalForm());
        returnImageView.setImage(returnImage);
        Image logoLoad = new Image(getClass().getResource("/com/example/mariem/Images/logo.png").toExternalForm());
        logoImageView.setImage(logoLoad);

        // Add a simple bounce animation (ScaleTransition)
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), returnImageView);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(Animation.INDEFINITE);
        st.play();
        ReturnLabel.setOnMouseClicked(event -> handleReturn());

        // Link table columns to Professeur properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        specialiteColumn.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProfesseurs(newValue);
        });

        // Load data from the database
        loadProfesseurData();
    }

    private void loadProfesseurData() {
        List<Professeur> professeurs = professeurService.getAllProfesseurs();
        professeurList = FXCollections.observableArrayList(professeurs);
        ProfesseurTable.setItems(professeurList);
    }


    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Professeurs/ProfesseurAdd.fxml"));
            Parent addView = loader.load();

            // Get the current stage using the button
            Stage stage = (Stage) addButton.getScene().getWindow();
            Scene scene = new Scene(addView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Professeur selectedProfesseur = ProfesseurTable.getSelectionModel().getSelectedItem();

        if (selectedProfesseur != null) {
            professeurService.deleteProfesseur(selectedProfesseur.getId());  // Call delete method from ProfesseurService
            ProfesseurTable.getItems().remove(selectedProfesseur);
            System.out.println("Professeur supprimé avec succès !");
        } else {
            System.out.println("Veuillez sélectionner un professeur à supprimer.");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Professeur selectedProfesseur = ProfesseurTable.getSelectionModel().getSelectedItem();

        if (selectedProfesseur != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Professeurs/ProfesseurUpdate.fxml"));
                Parent updateView = loader.load();

                // Pass selected Professeur to the update controller
                UpdateController controller = loader.getController();
                controller.setProfesseur(selectedProfesseur);  // Set Professeur to be updated

                Stage stage = (Stage) ProfesseurTable.getScene().getWindow();
                stage.setScene(new Scene(updateView));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner un professeur à modifier.");
        }
    }
    @FXML
    private void handleReturn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Home.fxml"));
            Parent matiereView = loader.load();
            Stage stage = (Stage) ReturnLabel.getScene().getWindow();
            stage.setScene(new Scene(matiereView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void GeneratePDF(ActionEvent event) {
        Professeur selectedProfesseur = ProfesseurTable.getSelectionModel().getSelectedItem();

        if (selectedProfesseur == null) {
            System.out.println("Veuillez sélectionner un professeur pour générer le PDF.");
            return;
        }

        MatiereService matiereService = new MatiereService();
        List<Matiere> matieres = matiereService.getMatieresByProfesseurId(selectedProfesseur.getId());

        if (matieres.isEmpty()) {
            System.out.println("Aucune matière trouvée pour ce professeur.");
            return;
        }

        try {
            Document document = new Document();
            String fileName = "Matieres_" + selectedProfesseur.getNom() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Add title
            com.lowagie.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, com.lowagie.text.Font.BOLD);
            Paragraph title = new Paragraph("Liste des matières de : " + selectedProfesseur.getNom(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Table with columns: Nom, Niveau, Description
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            table.addCell("Nom");
            table.addCell("Niveau");
            table.addCell("Description");

            for (Matiere matiere : matieres) {
                table.addCell(matiere.getNom().name());
                table.addCell(matiere.getNiveau());
                table.addCell(matiere.getDescription());
            }

            document.add(table);
            document.close();

            System.out.println("PDF généré avec succès : " + fileName);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la génération du PDF.");
        }
    }
    private void filterProfesseurs(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            ProfesseurTable.setItems(professeurList);
            return;
        }

        ObservableList<Professeur> filteredList = FXCollections.observableArrayList();

        for (Professeur professeur : professeurList) {
            if (professeur.getNom().toLowerCase().contains(keyword.toLowerCase()) ||
                    professeur.getSpecialite().toLowerCase().contains(keyword.toLowerCase())) {
                filteredList.add(professeur);
            }
        }

        ProfesseurTable.setItems(filteredList);
    }

}
