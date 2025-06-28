package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/main_view.fxml"));
        primaryStage.setTitle("Gestion Devoirs");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


}
