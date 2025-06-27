package controllers;

import entities.Student;
import entities.StudentClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ClassesServices;
import services.StudentService;
import utils.UIUtils;
import utils.Validators;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UpdateClassController {
    //
    private ClassesServices  classesServices = new ClassesServices();
    private StudentService studentService = new StudentService();
    private StudentClass  studentClass;
    private Set<Student> students = FXCollections.observableSet();
    private ObservableList<Student> allStudents= FXCollections.observableArrayList();
    //
    @FXML
    private TextField nameField;
    @FXML private ComboBox<String> typeBox;
    @FXML private TextField levelField;
    @FXML private TextField fieldField;
    @FXML private TextField specialityField;
    @FXML private TextField academicYearField;
    @FXML private TextArea descriptionArea;
    // Table 1 :  THIS table Has All the Students in the selected class
    @FXML private TableView<Student> classStudents;
    @FXML private TableColumn<Student, String> tab1idColumn;
    @FXML private TableColumn<Student, String> tab1firstNameColumn;
    @FXML private TableColumn<Student, String> tab1lastNameColumn;
    @FXML private TableColumn<Student, String> tab1emailColumn;

    @FXML private TableColumn<Student, Date> tab1birthdateColumn;
    @FXML private TableColumn<Student, String> tab1genderColumn;
    @FXML private TableColumn<Student, String> tab1phoneColumn;
    @FXML private TableColumn<Student, String> tab1nattionalIdColumn;

   // tab2 : this table has all the students in the db
   @FXML private TableView<Student> allStudentsTab;
    @FXML private TableColumn<Student, String> tab2idColumn;
    @FXML private TableColumn<Student, String> tab2firstNameColumn;
    @FXML private TableColumn<Student, String> tab2lastNameColumn;
    @FXML private TableColumn<Student, String> tab2emailColumn;

    @FXML private TableColumn<Student, Date> tab2birthdateColumn;
    @FXML private TableColumn<Student, String> tab2genderColumn;
    @FXML private TableColumn<Student, String> tab2phoneColumn;
    @FXML private TableColumn<Student, String> tab2nationalIdColumn;
    @FXML private TableColumn<Student, String> tab2currentClassdColumn;

    private Student tab1SelectedUser =  null ;
    private Student tab2SelectedUser =  null ;



    public void initData(StudentClass studentClass){
        this.studentClass = studentClass;
        // fill form fields with the data of the class
        nameField.setText(studentClass.getName());
        typeBox.setValue(studentClass.getType());
        levelField.setText(studentClass.getLevel());
        fieldField.setText(studentClass.getField());
        specialityField.setText(studentClass.getSpeciality());
        academicYearField.setText(studentClass.getAcademicYear());
        descriptionArea.setText(studentClass.getDescription());
        // get the class Student from
        allStudents.setAll(studentService.getAllStudents());
        for (Student student : allStudents) {
            if( student.getStudentClass()!=null && student.getStudentClass().getId() == this.studentClass.getId()){
                students.add(student);
            }
        }
        classStudents.setItems(FXCollections.observableArrayList(students));
        allStudentsTab.setItems(FXCollections.observableArrayList(allStudents));




    }
    @FXML
    public void initialize() {
        if(typeBox !=null){
            typeBox.getItems().addAll("bachelor s degree", "master degree", "engineering degree");
        }else {
            System.err.println("typeBox is null – check your FXML fx:id binding.");
        }
        // Table 1 :  THIS table Has All the Students in the selected class
        tab1idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tab1firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tab1lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tab1emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        tab1birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        tab1genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tab1phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tab1nattionalIdColumn.setCellValueFactory(new PropertyValueFactory<>("nationalId"));
        classStudents.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        // Table 2 :  THIS table Has All the Students in the selected class
        tab2idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            tab2firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tab2lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tab2emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            tab2birthdateColumn.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
            tab2genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
            tab2phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            tab2nationalIdColumn.setCellValueFactory(new PropertyValueFactory<>("nationalId"));
            tab2currentClassdColumn.setCellValueFactory(new PropertyValueFactory<>("studentClass"));
            allStudentsTab.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    @FXML
    public void setTab1SelectedUser() {
        tab1SelectedUser = classStudents.getSelectionModel().getSelectedItem();
    }
    @FXML
    public void onRemoveStudentFromClass(){
        if(tab1SelectedUser!=null){
            boolean result = UIUtils.showConfirmationDialog("remove Student Confirlation: " , "Are you sure you want to remove this student","") ;
            if(result){
                studentService.setStudentClassIdToNull(tab1SelectedUser.getId());
                students.remove(tab1SelectedUser);
                classStudents.setItems(FXCollections.observableArrayList(students));
            }
        }

    }
    @FXML
    public  void setTab2SelectedUser(){
        tab2SelectedUser = allStudentsTab.getSelectionModel().getSelectedItem();
    }
    @FXML
    public  void onAddStudent(){
        if(tab2SelectedUser!=null){
            students.add(tab2SelectedUser);
            classStudents.setItems(FXCollections.observableArrayList(students));
            studentService.setStudentClassId(tab2SelectedUser.getId(),this.studentClass.getId());

        }


    }
    // input Controlls
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
        if(!classesServices.isNewClassNameUnique(nameField.getText(), studentClass.getId())){
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
    public void onUpdateClass(){
        if(!addClassFormIsValid()) return ;
        String name = nameField.getText();
        String type = typeBox.getValue();
        String level = levelField.getText();
        String field = fieldField.getText();
        String speciality = specialityField.getText();
        String academicYear = academicYearField.getText();
        String description = descriptionArea.getText();
        StudentClass newStudentClass = new StudentClass(studentClass.getId(),name, type, level, field, speciality, academicYear, description);
        boolean success= classesServices.updateStudentClass(newStudentClass);
        if (success) {
            UIUtils.showSuccessAlert("Succes !" ,"Class updated successfully!") ;
        } else {
            UIUtils.showErrorAlert("Error", "Failed to update class.");
        }
    }
}
