package controllers;

import entities.StudentClass;
import enums.Gender;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import services.ClassesServices;
import utils.UIUtils;
import utils.Validators;
import entities.Student;
import services.StudentService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddClassController {

    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeBox;
    @FXML private TextField levelField;
    @FXML private TextField fieldField;
    @FXML private TextField specialityField;
    @FXML private TextField academicYearField;
    @FXML private TextArea descriptionArea;
    // Tab 1 :  THIS table Has All the Students in the Data Base
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<StudentClass, String> studentClass;

    @FXML private TableColumn<Student, String> tab1emailColumn;

    @FXML private TableColumn<Student, Date> tab1birthdateColumn;
    @FXML private TableColumn<Student, String> tab1genderColumn;
    @FXML private TableColumn<Student, String> tab1phoneColumn;
    @FXML private TableColumn<Student, String> tab1nattionalIdColumn;
    // Tab2 This Table Shows the adde student to the class
    @FXML private TableView<Student> addedStudentsTable;
    @FXML private TableColumn<Student, String> tab2firstNameColumn;
    @FXML private TableColumn<Student, String> tab2lastNameColumn;
    @FXML private TableColumn<StudentClass, String> tab2studentClass;

    @FXML private TableColumn<Student, String> tab2emailColumn;

    @FXML private TableColumn<Student, Date> tab2birthdateColumn;
    @FXML private TableColumn<Student, String> tab2genderColumn;
    @FXML private TableColumn<Student, String> tab2phoneColumn;
    @FXML private TableColumn<Student, String> tab2nationalIdColumn;
    @FXML private TableColumn<Student, String> tab2currentClassdColumn;
    private List<Student> currentSelectedStudents = null;
    Set<Student> addedStudents = new HashSet<Student>() ;




    private final ClassesServices classService = new ClassesServices();
    private final StudentService studentService = new StudentService();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    // Input Controls And Form Validation
    public boolean requiredFieldsAreEmpty(){
        boolean hasEmptyFields = false;
        hasEmptyFields |= UIUtils.highlightTextFieldIfNull(nameField);
        hasEmptyFields |=  UIUtils.highlightComboBoxIfNull(typeBox);
        hasEmptyFields |= UIUtils.highlightTextFieldIfNull(levelField);
        hasEmptyFields |= UIUtils.highlightTextFieldIfNull(fieldField);
        hasEmptyFields |= UIUtils.highlightTextFieldIfNull(specialityField);
        hasEmptyFields |= UIUtils.highlightTextFieldIfNull(academicYearField);
        return hasEmptyFields;
    }

    public boolean addClassFormIsValid(){
        boolean requiredFieldsAreEmplty = requiredFieldsAreEmpty() ;
        if(requiredFieldsAreEmplty == true ){
            UIUtils.showErrorAlert("Error", "Please fill in all required fields.");
            return false ;
        }
        if(!classService.isClassNameUnique(nameField.getText())){
            UIUtils.showErrorAlert("Unique Field Error", "Class name already exists. Chose Another One ");
            return  false ;
        }
        if(!Validators.isValidAcademicYear(academicYearField.getText())){
            UIUtils.showErrorAlert("invalid format error ", "Invalid academic year format. Use YYYY/YYYY and ensure years are consecutive. ");
            return  false ;
        }


        return true ;
    }

    @FXML
    public void initialize() {
        if(typeBox !=null){
            typeBox.getItems().addAll("bachelor s degree", "master degree", "engineering degree");
        }else {
            System.err.println("typeBox is null – check your FXML fx:id binding.");
        }
        // tab1
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentClass.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        tab1emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tab1birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        tab1genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tab1phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tab1nattionalIdColumn.setCellValueFactory(new PropertyValueFactory<>("nationalId"));

        studentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // tab2
        tab2firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tab2lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tab2studentClass.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
        tab2emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tab2birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        tab2genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tab2phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tab2nationalIdColumn.setCellValueFactory(new PropertyValueFactory<>("nationalId"));


        loadStudents();
    }

    private void loadStudents() {
            studentList.setAll(studentService.getAllStudents());
            studentsTable.setItems(studentList);

    }

    @FXML
    private void handleAddClass() {

            if(!addClassFormIsValid()) return ;

        String name = nameField.getText();
        String type = typeBox.getValue();
        String level = levelField.getText();
        String field = fieldField.getText();
        String speciality = specialityField.getText();
        String academicYear = academicYearField.getText();
        String description = descriptionArea.getText();

        StudentClass studentClass = new StudentClass( name, type, level, field, speciality, academicYear, description);
        int newClassID =classService.addClass(studentClass);

        if (newClassID>=0 && studentService.updateStudentsClass(addedStudents,newClassID) ) {
            UIUtils.showSuccessAlert("Succes !" ,"Class added successfully!") ;
            clearForm();
        } else {
            UIUtils.showErrorAlert("Error", "Failed to add class.");
        }
    }

    private void clearForm() {
        nameField.clear();
        levelField.clear();
        fieldField.clear();
        specialityField.clear();
        academicYearField.clear();
        descriptionArea.clear();
        typeBox.getSelectionModel().clearSelection();
        studentsTable.getSelectionModel().clearSelection();
        addedStudentsTable.getItems().clear();
    }
    @FXML
    private  void  onSelectStudent(){

        currentSelectedStudents  = studentsTable.getSelectionModel().getSelectedItems();
    }

    @FXML
    private void onAddStudentToClass(){
        if(currentSelectedStudents!= null){
            for (Student currentSelectedStudent : currentSelectedStudents )  {
                if(!addedStudents.contains(currentSelectedStudent)){
                    addedStudents.add(currentSelectedStudent);
                }
            }
            addedStudentsTable.setItems(FXCollections.observableArrayList(addedStudents));
        }
    }
}
