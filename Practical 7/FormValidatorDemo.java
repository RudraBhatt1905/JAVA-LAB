
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

// Custom annotation to check empty fields
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface NotBlank {
}

// Custom annotation to set maximum length
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MaxLength {
    int value();
}

// Signup form model
class SignupForm {
    @NotBlank
    @MaxLength(15)
    private String username;

    @NotBlank
    @MaxLength(30)
    private String email;

    @NotBlank
    @MaxLength(8)
    private String password;

    public SignupForm(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

// Validator using reflection
class FormChecker {

    public List<String> validate(Object obj) {
        List<String> errors = new ArrayList<>();

        if (obj == null) {
            errors.add("Object cannot be null");
            return errors;
        }

        // Read all fields of the object
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(obj);

                // Check @NotBlank
                if (field.isAnnotationPresent(NotBlank.class)) {
                    if (value == null ||
                        (value instanceof String &&
                         ((String) value).trim().isEmpty())) {

                        errors.add(field.getName() + " must not be blank");
                    }
                }

                // Check @MaxLength
                if (field.isAnnotationPresent(MaxLength.class)) {
                    MaxLength annotation =
                        field.getAnnotation(MaxLength.class);

                    int max = annotation.value();

                    if (value instanceof String &&
                        ((String) value).length() > max) {

                        errors.add(field.getName()
                            + " must not exceed " + max + " characters");
                    }
                }

            } catch (IllegalAccessException e) {
                errors.add("Cannot access field: " + field.getName());
            }
        }

        return errors;
    }
}

public class FormValidatorDemo {
    public static void main(String[] args) {

        SignupForm form = new SignupForm(
            " ",
            "this-email-address-is-too-long@example.com",
            "123456789"
        );

        FormChecker checker = new FormChecker();

        List<String> errors = checker.validate(form);

        if (errors.isEmpty()) {
            System.out.println("Form is valid!");
        } else {
            System.out.println("Validation errors:");

            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}
