package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    public void initialize() {
        showDevoirView(); // Charge la vue des devoirs au lancement
    }

    @FXML
    public void showDevoirView() {
        loadView("/devoir_view.fxml");  // ✅ Mets ici le chemin vers ton fichier FXML des devoirs
    }

    @FXML
    public void showSoumissionView() {
        loadView("/soumission_view.fxml");  // ✅ Mets ici le chemin vers ton fichier FXML des soumissions
    }

    private void loadView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
