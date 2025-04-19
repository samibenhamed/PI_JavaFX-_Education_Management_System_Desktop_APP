package utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.HelloApplication;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class UIUtils {
    public static void switchScene(ActionEvent event, String fxmlPath) {
        try{
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setPrefWidth(400);
        alert.getDialogPane().setPrefHeight(200);
        alert.showAndWait();
    }

    public static boolean highlightTextFieldIfNull(TextField field) {
        if (field.getText().isEmpty()) {
            field.setStyle("-fx-border-color: red;");
            return true;
        } else {
            field.setStyle("");
            return false;
        }
    }

    public static  boolean highlightComboBoxIfNull(ComboBox<?> comboBox ) {
        if (comboBox.getValue() == null) {
            comboBox.setStyle("-fx-border-color: red;");
            return true ;
        }
        comboBox.setStyle("");  // reset style
        return false ;
    }

    public static boolean highlightDatePickerIfNull(DatePicker datePicker) {
        if (datePicker.getValue() == null) {
            datePicker.setStyle("-fx-border-color: red;");
            return true ;
        }
        datePicker.setStyle("");  // reset style
        return false ;
    }
    public static boolean showConfirmationDialog(String title, String headerText, String contentText) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void showInfoDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void openWindow(String fxmlPath, Consumer<Object> controllerInitializer) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlPath));
            Parent root = loader.load();

            if (controllerInitializer != null) {
                controllerInitializer.accept(loader.getController());
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update User");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
