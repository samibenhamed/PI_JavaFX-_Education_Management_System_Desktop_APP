package entities;

import enums.Gender;
import enums.UserType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDate;

public class Student extends User {
    private StudentClass studentClass;
    public StudentClass getStudentClass() {
        return studentClass;
    }
    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    public BooleanProperty selectedProperty() {
        return selected;
    }
    public boolean isSelected() {
        return selected.get();
    }
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    // constructors
    public Student(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, StudentClass studentClass , Gender  gender) {
        super(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type , gender );
        this.studentClass = studentClass;
    }
    public Student(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type , Gender gender ) {
        super(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type , gender );
    }
    public Student(String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type , Gender gender ) {
        super( firstName, lastName, email, password, birthdate, address, phone, nationalId, type , gender );
    }
    public Student(String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, StudentClass studentClass , Gender gender ) {
        super( firstName, lastName, email, password, birthdate, address, phone, nationalId, type , gender );
        this.studentClass = studentClass;
    }
    public Student() {
        super();
    }
    public Student(User user ) {
        super(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getBirthdate(), user.getAddress(), user.getPhone(), user.getNationalId(), user.getType() ,user.getGender() ) ;
        this.studentClass = studentClass;
    }

    // toString
    @Override
    public String toString() {
        return
                super.toString() +"\n"+
                "studentClass=" + studentClass ;
    }


}
