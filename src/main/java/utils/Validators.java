package utils;

public class Validators {
    public static boolean isEmailValid(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isPasswordStrong(String password) {
        return password != null &&
                password.length() >= 8 && // Minimum length check
                password.matches(".*[A-Z].*") && // At least one uppercase letter
                password.matches(".*[0-9].*") && // At least one digit
                password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");// At least one special character
    }
    public static boolean isValidNationalId(String nationalId) {
        return nationalId != null && nationalId.matches("\\d{8}");
    }
    public static boolean isValidAcademicYear(String year) {
        // Regex for format: 4 digits, slash, 4 digits
        String regex = "^(\\d{4})/(\\d{4})$";

        if (year == null || !year.matches(regex)) {
            return false;
        }

        String[] parts = year.split("/");
        int start = Integer.parseInt(parts[0]);
        int end = Integer.parseInt(parts[1]);

        return end == start + 1; // Ensure it's consecutive years
    }




}
