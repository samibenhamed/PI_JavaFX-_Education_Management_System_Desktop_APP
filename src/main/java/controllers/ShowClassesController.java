package controllers;

import entities.Student;
import entities.StudentClass;
import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import services.ClassesServices;
import utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @FXML
    private TextField  searchKey;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<String> levelComboBox;
    @FXML
    private ComboBox<String> fieldComboBox;
    @FXML
    private ComboBox<String> specialtyCombobox;
    @FXML
private ComboBox<String> yearComboBox;


    private ObservableList<StudentClass> classesList = FXCollections.observableArrayList();
    private  List<StudentClass> allClasses = new ArrayList<>();
    private StudentClass selectedClass = null;

    @FXML
    public void initialize() {
        //                    Type ComboBox

        if(typeComboBox !=null){
            typeComboBox.getItems().addAll("All", "bachelor s degree", "master degree", "engineering degree");
        }else {
            System.err.println("typeBox is null – check your FXML fx:id binding.");
        }
        // load Claasses
        allClasses = classesServices.getAllClasses();
        classesList.setAll(allClasses);
        classesTable.setItems(classesList);
        // set table colums
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        level.setCellValueFactory(new PropertyValueFactory<>("level"));
        field.setCellValueFactory(new PropertyValueFactory<>("field"));
        speciality.setCellValueFactory(new PropertyValueFactory<>("speciality"));
        academic_year.setCellValueFactory(new PropertyValueFactory<>("academicYear"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        classesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //           Level ComboBox
        List<String> levels = new ArrayList<>(
                allClasses.stream()
                        .map(StudentClass::getLevel)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
        );
        levels.add(0,"All");

        if(levelComboBox !=null){
            levelComboBox.getItems().setAll(levels) ;

        }else {
            System.err.println("levelComboBox is null – check your FXML fx:id binding.");
        }
       //   Field ComboBox
        List<String> fields = new ArrayList<>(
                allClasses.stream()
                        .map(StudentClass::getField)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
        );
        fields.add(0, "All");

        if(fieldComboBox !=null){
            fieldComboBox.getItems().setAll(fields) ;


        }else {
            System.err.println("fieldComboBox is null – check your FXML fx:id binding.");
        }
//    yearComboBox    //specialtyCombobox

        //  specialtyCombobox
        List<String> specialties = new ArrayList<>(
                allClasses.stream()
                        .map(StudentClass::getSpeciality)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
        );
        specialties.add(0, "All");
        specialtyCombobox.getItems().setAll(specialties) ;
        //  yearComboBox
        List<String> years = new ArrayList<>(
                allClasses.stream()
                        .map(StudentClass::getAcademicYear)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
        );
        years.add(0, "All");
        if(yearComboBox !=null){
            yearComboBox.getItems().setAll(years) ;


        }else {
            System.err.println("yearComboBox is null – check your FXML fx:id binding.");
        }



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
    @FXML
    void onAddClass() {
        UIUtils.openWindow("/main/admin_views/addClass.fxml", controller -> {
            AddClassController addClassController = (AddClassController) controller;
        });
        classesTable.getItems().setAll(classesServices.getAllClasses());
        allClasses = classesServices.getAllClasses();
}
    @FXML
    void  onHome(ActionEvent event ){
        UIUtils.switchScene(event, "/main/admin-home-view.fxml");
    }

    @FXML
    public  void  onDeleteClass() {
        if (selectedClass != null) {
            boolean confirmed = UIUtils.showConfirmationDialog("Confirm Deletion",
                    "Are you sure you want to delete this class?",
                    "This action cannot be undone.");
            if (!confirmed) {
                return;
            }
            boolean succes = classesServices.deleteClass(selectedClass.getId());
            if (!succes) {
                UIUtils.showErrorAlert("Error", "Failed to delete class.");
                return;
            }else {
                UIUtils.showSuccessAlert("Success", "Class deleted successfully.");
                classesTable.getItems().setAll(classesServices.getAllClasses());

            }
        } else {
            System.err.println("No user selected for deletion.");
        }
    }
    @FXML
    public void onSearch() {
        List<StudentClass > data =filterBySearchKey(allClasses) ;
        data = filterByType(data);
        data = filterByLevel(data);
        data = filterByField(data);
        data = filterBySpecialty(data);
        data = filterByYear(data);
        classesTable.getItems().setAll(filterBySearchKey(data)); // ✅ update table with results
    }

     // Filters
    List<StudentClass> filterBySearchKey(List<StudentClass> data ) {
        String key = searchKey.getText();
       if(!key.isEmpty()) {
            List<StudentClass> searchResult = data.stream()
                    .filter(studentClass -> studentClass.getName().toLowerCase().contains(key.toLowerCase()))
                    .toList();
            return searchResult ;
        }
        return data ;
    }
    List<StudentClass> filterByType(List<StudentClass> data ) {
        String  type = typeComboBox.getValue();
        if( type!=null && !type.equals("All")  )  {
            List<StudentClass> searchResult = data.stream()
                    .filter(studentClass -> studentClass.getType().equals(type))
                    .toList();
            return searchResult ;
        }
        return data ;
    }

    List<StudentClass> filterByLevel(List<StudentClass> data ) {
        String  level = levelComboBox.getValue();
        if( level!=null && !level.equals("All")  )  {
            List<StudentClass> searchResult = data.stream()
                    .filter(studentClass -> studentClass.getLevel().equals(level))
                    .toList();
            return searchResult ;
        }
        return data ;
    }
    List<StudentClass> filterByField(List<StudentClass> data ) {
        String  field = fieldComboBox.getValue();
        if( field!=null && !field.equals("All")  )  {
            List<StudentClass> searchResult = data.stream()
                    .filter(studentClass -> studentClass.getField().equals(field))
                    .toList();
            return searchResult ;
        }
        return data ;
    }
    List<StudentClass> filterBySpecialty(List<StudentClass> data ) {
        String  specialty = specialtyCombobox.getValue();
        if( specialty!=null && !specialty.equals("All")  )  {
            List<StudentClass> searchResult = data.stream()
                    .filter(studentClass -> studentClass.getSpeciality().equals(specialty))
                    .toList();
            return searchResult ;
        }
        return data ;
    }
    List<StudentClass> filterByYear(List<StudentClass> data ) {
        String  year = yearComboBox.getValue();
        if( year!=null && !year.equals("All")  )  {
            List<StudentClass> searchResult = data.stream()
                    .filter(studentClass -> studentClass.getAcademicYear().equals(year))
                    .toList();
            return searchResult ;
        }
        return data ;
    }



}