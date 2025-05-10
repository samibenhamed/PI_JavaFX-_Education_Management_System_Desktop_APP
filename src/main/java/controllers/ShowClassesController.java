package controllers;

import entities.Student;
import entities.StudentClass;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ClassesServices;
import utils.UIUtils;

public class ShowClassesController {
    private ClassesServices classesServices = new ClassesServices();
    @FXML
    private TableView<StudentClass> classesTable;
    @FXML
    private TableColumn<StudentClass, String> id;
    @FXML
    private TableColumn<StudentClass, String> name;
    @FXML
    private TableColumn<StudentClass, String> type;
    @FXML
    private TableColumn<StudentClass, String> level;
    @FXML
    private TableColumn<StudentClass, String> field;
    @FXML
    private TableColumn<StudentClass, String> speciality;
    @FXML
    private TableColumn<StudentClass, String> academic_year;
    @FXML
    private TableColumn<StudentClass, String> description;
    private ObservableList<StudentClass> classesList = FXCollections.observableArrayList();
    private StudentClass selectedClass = null;

    @FXML
    public void initialize() {
        // load Claasse
        classesList.setAll(classesServices.getAllClasses());
        classesTable.setItems(classesList);
        //
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        level.setCellValueFactory(new PropertyValueFactory<>("level"));
        field.setCellValueFactory(new PropertyValueFactory<>("field"));
        speciality.setCellValueFactory(new PropertyValueFactory<>("speciality"));
        academic_year.setCellValueFactory(new PropertyValueFactory<>("academicYear"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        classesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    public void setSelectedClass() {
        selectedClass = classesTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    void onUpdateClass() {
        if (selectedClass != null) {
            UIUtils.openWindow("/main/admin_views/updateClass.fxml", controller -> {
                UpdateClassController updateClassController = (UpdateClassController) controller;
                updateClassController.initData(selectedClass);
            });
            classesTable.getItems().setAll(classesServices.getAllClasses());

        } else {
            System.err.println("No user selected for update.");

        }

    }


}