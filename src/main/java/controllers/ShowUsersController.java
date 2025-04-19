package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import services.AdminService;
import utils.UIUtils;

import java.util.List;

public class ShowUsersController {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, Integer> id;
    @FXML
    private TableColumn<User, String> firstName;
    @FXML
    private TableColumn<User, String> lastName;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private TableColumn<User, String> birthdate;
    @FXML
    private TableColumn<User, String> gender;
    @FXML
    private TableColumn<User, String> address;
    @FXML
    private TableColumn<User, String> phone;
    @FXML
    private TableColumn<User, String> nationalId;
    @FXML
    private TableColumn<User, String> type;

    @FXML
    private Button deleteButton;

    private AdminService service;

    @FXML
    void initialize() {
        if (usersTable != null) {
            service = new AdminService();
            List<User> userList = service.getAllUsers();

            // Set cell value factories
            id.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
            firstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
            lastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
            email.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            birthdate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBirthdate().toString()));
            gender.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGender().toString()));
            address.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));
            phone.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));
            nationalId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationalId()));
            type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().toString()));

            // Populate the table
            usersTable.getItems().setAll(userList);

            // Optional: Add event for row selection
            usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    // Do something when a user is selected
                    System.out.println("Selected User: " + newSelection.getId());
                }
            });
        } else {
            System.err.println("usersTable is null – check your FXML fx:id binding.");
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Use the utility method to show the confirmation dialog
            boolean confirmed = UIUtils.showConfirmationDialog(
                    "Confirm Deletion",
                    "Are you sure you want to delete this user?",
                    "This action cannot be undone."
            );

            if (confirmed) {
                boolean success = service.deleteUser(selectedUser.getId());
                if (success) {
                    usersTable.getItems().remove(selectedUser);
                    System.out.println("User with ID " + selectedUser.getId() + " deleted.");
                } else {
                    System.err.println("Failed to delete user.");
                }
            } else {
                System.out.println("Deletion canceled.");
            }
        } else {
            System.err.println("No user selected.");
        }
    }
}
