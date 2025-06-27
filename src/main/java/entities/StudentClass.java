package entities;

public class StudentClass {
    private int id;
    private String name;
    private String type;
    private String level;
    private String field;
    private String speciality;
    private String academicYear;
    private String description;

    public StudentClass(int id, String name, String type, String level, String field, String speciality, String academicYear, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.level = level;
        this.field = field;
        this.speciality = speciality;
        this.academicYear = academicYear;
        this.description = description;
    }

    public StudentClass(String name, String type, String level, String field, String speciality, String academicYear, String description) {
        this.name = name;
        this.type = type;
        this.level = level;
        this.field = field;
        this.speciality = speciality;
        this.academicYear = academicYear;
        this.description = description;
    }

    // Getters (add setters if you need them)
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getLevel() {
        return level;
    }

    public String getField() {
        return field;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name; // so ComboBox or ListView shows class name
    }

    public void showClassDetails() {
        System.out.println("Class Details:");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Type: " + type);
        System.out.println("Level: " + level);
        System.out.println("Field: " + field);
        System.out.println("Speciality: " + speciality);
        System.out.println("Academic Year: " + academicYear);
        System.out.println("Description: " + description);
    }
}
