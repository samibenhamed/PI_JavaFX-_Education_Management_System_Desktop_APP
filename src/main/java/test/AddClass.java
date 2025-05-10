package test;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
public class AddClass extends Application  {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.HelloApplication.class.getResource("/main/admin_views/addClass.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Education Management System ");
        // stage.setMaximized(true); // Maximizes The Window to full Screan
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
