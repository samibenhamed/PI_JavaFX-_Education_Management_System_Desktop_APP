package entities;

import enums.Gender;
import enums.UserType;

import java.time.LocalDate;

public class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected LocalDate birthdate;
    protected String address;
    protected String phone;
    protected String nationalId;
    protected UserType type;
    protected Gender gender;
    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // constructors
    public User(int id, String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type , Gender gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.address = address;
        this.phone = phone;
        this.nationalId = nationalId;
        this.type = type;
        this.gender = gender;
    }
    public User( String firstName, String lastName, String email, String password, LocalDate birthdate, String address, String phone, String nationalId, UserType type , Gender  gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
        this.address = address;
        this.phone = phone;
        this.nationalId = nationalId;
        this.type = type;
        this.gender = gender;
    }

    public User(){

    }


    // toString method
    @Override
    public String toString() {
        return
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthdate=" + birthdate +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", type=" + type  ;
    }
}
