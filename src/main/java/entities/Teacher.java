package entities;

import enums.Gender;
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
    public Teacher(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, String specialty , Gender gender ) {
        super(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type , gender );
        this.specialty = specialty;
    }

    public Teacher(String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type, String specialty , Gender gender) {
        super(firstName, lastName, email, password, birthdate, address, phone, nationalId, type , gender );
        this.specialty = specialty;
    }
    public Teacher() {
        super();
    }
    public  Teacher(User user) {
        super(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getBirthdate(), user.getAddress(), user.getPhone(), user.getNationalId(), user.getType() , user.getGender());
    }


//    toString
    @Override
    public String toString() {
        return
                super.toString() +"\n"+
                "specialty='" + specialty  ;
    }
}
