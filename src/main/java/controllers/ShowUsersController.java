package controllers;

import entities.User;
import enums.UserType;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.AdminService;
import utils.UIUtils;

import java.util.ArrayList;
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
    @FXML
    private ComboBox<String> typeComboBox ;
    @FXML
    private TextField searchKey ;
    List<User> users = new ArrayList<>();


    private AdminService service;

    @FXML
    void initialize() {
        if (usersTable != null) {
            service = new AdminService();
            List<User> userList = service.getAllUsers();
            users = userList;
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

            usersTable.getItems().setAll(userList);

            usersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    System.out.println("Selected User: " + newSelection.getId());
                }
            });
        } else {
            System.err.println("usersTable is null – check your FXML fx:id binding.");
        }
//
        typeComboBox.getItems().setAll(List.of(UserType.ADMIN.toString(), UserType.STUDENT.toString(), UserType.TEACHER.toString() ,"All"));


    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
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
    @FXML
    private void handleUpdateUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            UIUtils.openWindow("/main/user_views/updateUser.fxml", controller -> {
                UpdateUserController updateController = (UpdateUserController) controller;
                updateController.initData(selectedUser);
            });


            // Refresh table after update
            usersTable.getItems().setAll(service.getAllUsers());
        } else {
            System.err.println("No user selected for update.");
        }
    }
    @FXML
    void  onHome(ActionEvent event ){
        UIUtils.switchScene(event, "/main/admin-home-view.fxml");
    }

    @FXML
    public void onAddUser() {
            UIUtils.openWindow("/main/admin_views/add-user.fxml", controller -> {
                AdminController  adminController = (AdminController) controller;
            });
        users = service.getAllUsers();
        usersTable.getItems().setAll(service.getAllUsers()) ;
    }
    @FXML
    public void onSearch() {
        List<User> data = fiterByType(users);
        System.out.println(data.size());
        data = filterBySearchKey(data);
        System.out.println(data.size());
        usersTable.getItems().setAll(data);
    }
    public  List<User>  fiterByType(   List<User> data){
        String selectedType = typeComboBox.getValue();
        if ("All".equals(selectedType) || selectedType ==null) {
            return data;
        } else {
            return data.stream().filter(user -> user.getType().toString().equals(selectedType)).toList();
        }
    }
    public   List<User>   filterBySearchKey(  List<User>  data){
        String key = searchKey.getText();
        if(key.isEmpty()){
            return  data ;
        }else{
            return  data.stream().filter(user -> user.getFirstName().contains(key) || user.getLastName().contains(key) || user.getEmail().contains(key) || user.getPhone().contains(key) || user.getNationalId().contains(key)).toList();
        }
    }



}
