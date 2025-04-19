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



}
