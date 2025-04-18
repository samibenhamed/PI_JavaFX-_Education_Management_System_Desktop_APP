import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.Model.Enseignant;
import org.example.dao.EnseignantDAO;

public class Main extends Application {

    private TableView<Enseignant> table;
    private TextField nomField, prenomField, emailField, departementField;
    private ObservableList<Enseignant> enseignants;
    private final EnseignantDAO dao = new EnseignantDAO();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Ensure the path to the FXML file is correct
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/view/GestionEnseignants.fxml"));
        primaryStage.setTitle("Gestion des Enseignants");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        // Charger les données depuis la BDD
        enseignants = FXCollections.observableArrayList(dao.getAll());

        table = new TableView<>(enseignants);

        TableColumn<Enseignant, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));

        TableColumn<Enseignant, String> prenomCol = new TableColumn<>("Prénom");
        prenomCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrenom()));

        TableColumn<Enseignant, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Enseignant, String> departementCol = new TableColumn<>("Département");
        departementCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartement()));

        table.getColumns().addAll(nomCol, prenomCol, emailCol, departementCol);

        // Champs de saisie
        nomField = new TextField();
        nomField.setPromptText("Nom");

        prenomField = new TextField();
        prenomField.setPromptText("Prénom");

        emailField = new TextField();
        emailField.setPromptText("Email");

        departementField = new TextField();
        departementField.setPromptText("Département");

        HBox inputBox = new HBox(10, nomField, prenomField, emailField, departementField);

        // Boutons
        Button ajouterBtn = new Button("Ajouter");
        ajouterBtn.setOnAction(e -> ajouterEnseignant());

        Button supprimerBtn = new Button("Supprimer");
        supprimerBtn.setOnAction(e -> supprimerEnseignant());

        HBox buttonBox = new HBox(10, ajouterBtn, supprimerBtn);

        VBox layout = new VBox(10, table, inputBox, buttonBox);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gestion des Enseignants");
        primaryStage.show();
    }

    private void ajouterEnseignant() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String departement = departementField.getText();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || departement.isEmpty()) {
            showAlert("Tous les champs doivent être remplis !");
            return;
        }

        Enseignant enseignant = new Enseignant(0, nom, prenom, email, departement);
        dao.add(enseignant);
        enseignants.setAll(dao.getAll());
        clearFields();
    }

    private void supprimerEnseignant() {
        Enseignant selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            dao.delete(selected.getId());
            enseignants.setAll(dao.getAll());
        } else {
            showAlert("Veuillez sélectionner un enseignant à supprimer.");
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        departementField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
