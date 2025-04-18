package entities;

import enums.UserType;

import java.time.LocalDate;

public class Teacher extends  User {
    private String   specialty  ;
   // getters and setters
    public String getSpecialty() {
        return specialty;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
    // constructors
    public Teacher(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, String specialty) {
        super(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
        this.specialty = specialty;
    }

    public Teacher(String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, String specialty) {
        super(firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
        this.specialty = specialty;
    }


//    toString
    @Override
    public String toString() {
        return
                super.toString() +"\n"+
                "specialty='" + specialty  ;
    }
}
