package entities;

import enums.UserType;

import java.time.LocalDate;

public class Admin extends  User {
    public Admin(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type) {
        super(id, firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
    }

    public Admin(String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type) {
        super(firstName, lastName, email, password, birthdate, address, phone, nationalId, type);
    }
    public Admin(User user ){

        super(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getBirthdate(), user.getAddress(), user.getPhone(), user.getNationalId(), user.getType());
    }
    public Admin() {
        super();
    }
}
