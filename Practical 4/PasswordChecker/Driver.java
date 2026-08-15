public class Driver {

    public static void main(String[] args) {

        String[] passwords = {"abc","password","Password","Pass1234", "Abcd1234!"};

        for (String pw : passwords) {

            System.out.println("Password: " + pw);
            System.out.println("Length >= 8: " + PasswordChecker.hasLength(pw));
            System.out.println("Contains Uppercase: " + PasswordChecker.hasUpperCase(pw));
            System.out.println("Contains Digit: " + PasswordChecker.hasDigit(pw));
            System.out.println("Contains Special Character: " + PasswordChecker.hasSpecialChar(pw));
            System.out.println("Strength: " + PasswordChecker.strength(pw));
            System.out.println();
        }
    }
}