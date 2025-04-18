package entities;

import enums.UserType;

import java.time.LocalDate;

public class Student extends User {
    private StudentClass studentClass;
    public StudentClass getStudentClass() {
        return studentClass;
    }
    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }

    // constructors
    public Student(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, StudentClass studentClass) {
        super(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
        this.studentClass = studentClass;
    }
    public Student(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type) {
        super(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
    }
    public Student(String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type) {
        super( firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
    }
    public Student(String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, StudentClass studentClass) {
        super( firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
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
