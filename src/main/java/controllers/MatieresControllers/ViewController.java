package controllers.MatieresControllers;

import API.GeminiAPI;
import entities.Matiere;
import services.MatiereService;
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

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewController implements Initializable {
    @FXML
    private TextField searchField;
    @FXML
    private Label LogoLabel;
    @FXML
    private ImageView logoImageView;
    private ObservableList<Matiere> fullMatiereList;
    @FXML
    private ImageView returnImageView;
    @FXML
    private Label ReturnLabel;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Matiere> matiereTable;

    @FXML
    private TableColumn<Matiere, Integer> idColumn;

    @FXML
    private TableColumn<Matiere, String> nomColumn;

    @FXML
    private TableColumn<Matiere, String> niveauColumn;

    @FXML
    private TableColumn<Matiere, String> descriptionColumn;
    @FXML
    private TableColumn<Matiere, String> professeurColumn;

    private MatiereService matiereService = new MatiereService();

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

        // Link table columns to Matiere properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        niveauColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        professeurColumn.setCellValueFactory(new PropertyValueFactory<>("professeurName"));

        // Load data from the database
        loadMatiereData();
        // Search logic
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterMatiereList(newValue);
        });

    }

    private void loadMatiereData() {
        List<Matiere> matiereList = matiereService.getAllMatieres();
        fullMatiereList = FXCollections.observableArrayList(matiereList);
        matiereTable.setItems(fullMatiereList);
    }
    private void filterMatiereList(String query) {
        if (query == null || query.isEmpty()) {
            matiereTable.setItems(fullMatiereList);
            return;
        }

        String lowerCaseQuery = query.toLowerCase();
        System.out.println(lowerCaseQuery);
        ObservableList<Matiere> filteredList = fullMatiereList.filtered(m ->
                m.getNom().toLowerCase().contains(lowerCaseQuery) ||
                        m.getNiveau().toLowerCase().contains(lowerCaseQuery) ||
                        (m.getProfesseurName() != null && m.getProfesseurName().toLowerCase().contains(lowerCaseQuery))
        );

        matiereTable.setItems(filteredList);
    }


    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Matieres/MatiereAdd.fxml"));
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
        Matiere selectedMatiere = matiereTable.getSelectionModel().getSelectedItem();

        if (selectedMatiere != null) {
            matiereService.deleteMatiere(selectedMatiere.getId());
            matiereTable.getItems().remove(selectedMatiere);
            System.out.println("Matière supprimée avec succès !");
        } else {
            System.out.println("Veuillez sélectionner une matière à supprimer.");
        }
    }
    @FXML
    private void handleUpdate(ActionEvent event) {
        Matiere selected = matiereTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/mariem/Matieres/MatiereUpdate.fxml"));
                Parent updateView = loader.load();

                UpdateController controller = loader.getController();
                controller.setMatiere(selected);

                Stage stage = (Stage) matiereTable.getScene().getWindow();
                stage.setScene(new Scene(updateView));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Veuillez sélectionner une matière à modifier.");
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
    private void handleGenerateQuiz() {
        Matiere selectedMatiere = matiereTable.getSelectionModel().getSelectedItem();

        if (selectedMatiere != null) {
            String courseTitle = String.valueOf(selectedMatiere.getNom());  // Get the selected course name
            String prompt = "Generate a simple quiz with 3 multiple-choice questions based on the topic: " + courseTitle;

            String quiz = GeminiAPI.sendToGemini(prompt);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Quiz Generated");
            alert.setHeaderText("Quiz based on the course: " + courseTitle);
            alert.setContentText(quiz);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No subject selected");
            alert.setContentText("Please select a subject from the table to generate a quiz.");
            alert.showAndWait();
        }
    }

}
