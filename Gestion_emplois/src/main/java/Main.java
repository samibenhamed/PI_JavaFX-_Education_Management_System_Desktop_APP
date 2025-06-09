import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.Model.Enseignant;
import org.example.controller.SeanceController;
import org.example.dao.EnseignantDAO;

import java.net.URL;
import java.util.Objects;

public class Main extends Application {


    private TableView<Enseignant> table;
    private TextField nomField, prenomField, emailField, departementField;
    private ObservableList<Enseignant> enseignants;
    private final EnseignantDAO dao = new EnseignantDAO();


    @Override
    public void start(Stage primaryStage) {
        System.out.println("java.library.path = " + System.getProperty("java.library.path"));

        try {
            // --- Choose which view to load ---
            // Change this to "/org/example/view/seance.fxml" if you want to start directly with the Seance view
            URL fxmlUrl = getClass().getResource("/org/example/view/MainView.fxml");
            // URL fxmlUrl = getClass().getResource("/org/example/view/seance.fxml"); // Uncomment this to load seance.fxml

            if (fxmlUrl == null) {
                throw new RuntimeException("FXML file not found.");
            }

            System.out.println("Loading FXML from: " + fxmlUrl);

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();


            Scene scene = new Scene(root, 800, 600);

            try {
                String cssPath = Objects.requireNonNull(getClass().getResource("/style.css"), "CSS file not found").toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (NullPointerException e) {
                System.err.println("Error loading CSS: " + e.getMessage());
            }

            primaryStage.setTitle("Gestion des Enseignants");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Failed to launch application:");
            e.printStackTrace();
        }
    }


}
