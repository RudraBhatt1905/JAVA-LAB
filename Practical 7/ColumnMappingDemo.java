
import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

// Custom annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface Column {
    String name();
}

// Model class
class Person {
    @Column(name = "full_name")
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "email")
    private String email;

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("Email: " + email);
    }
}

// Class to populate object fields using reflection
class RowMapper {

    public static void populate(Object obj, String[] headers, String[] row)
            throws IllegalAccessException {

        // Map column names to their positions
        Map<String, Integer> columnIndexes = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            columnIndexes.put(headers[i].trim(), i);
        }

        // Read fields using reflection
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {

            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }

            Column column = field.getAnnotation(Column.class);
            String columnName = column.name();

            // Find the matching column
            Integer index = columnIndexes.get(columnName);

            // Handle a missing column
            if (index == null) {
                System.out.println("Warning: Missing column: " + columnName);
                System.out.println("Field left unchanged: " + field.getName());
                continue;
            }

            // Handle a missing value in the data row
            if (index >= row.length) {
                System.out.println("Warning: Missing value for: " + columnName);
                continue;
            }

            String value = row[index].trim();

            field.setAccessible(true);

            // Convert the value to the field's data type
            if (field.getType() == String.class) {
                field.set(obj, value);
            } else if (field.getType() == int.class) {
                field.setInt(obj, Integer.parseInt(value));
            } else if (field.getType() == double.class) {
                field.setDouble(obj, Double.parseDouble(value));
            } else if (field.getType() == boolean.class) {
                field.setBoolean(obj, Boolean.parseBoolean(value));
            } else {
                throw new IllegalArgumentException(
                    "Unsupported field type: " + field.getType()
                );
            }
        }
    }
}

// Main class
public class ColumnMappingDemo {
    public static void main(String[] args) {

        // Header order is different from the field order.
        // The email column is intentionally missing.
        String[] headers = {"age", "full_name"};
        String[] data = {"21", "Rahul"};

        Person person = new Person();

        try {
            RowMapper.populate(person, headers, data);

            System.out.println("\nPerson Details:");
            person.display();

        } catch (IllegalAccessException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
