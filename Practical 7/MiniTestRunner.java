
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;

// Create a custom annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Run {
}

// Class containing test methods
class MyTests {

    @Run
    public void testAddition() {
        System.out.println("Addition test executed");
    }

    @Run
    public void testSubtraction() {
        System.out.println("Subtraction test executed");
    }

    @Run
    public void testMessage() {
        System.out.println("Message test executed");
    }

    // This method will not run because it has no @Run annotation
    public void helperMethod() {
        System.out.println("Helper method executed");
    }
}

// Mini test runner
public class MiniTestRunner {
    public static void main(String[] args) {

        MyTests tests = new MyTests();
        int count = 0;

        // Get all declared methods using reflection
        Method[] methods = MyTests.class.getDeclaredMethods();

        // Sort methods to make the output predictable
        Arrays.sort(methods, Comparator.comparing(Method::getName));

        for (Method method : methods) {

            // Run only methods marked with @Run and having no parameters
            if (method.isAnnotationPresent(Run.class)
                    && method.getParameterCount() == 0) {

                try {
                    method.invoke(tests);
                    count++;
                } catch (InvocationTargetException e) {
                    count++;
                    System.out.println("Test failed: " + method.getName());
                } catch (IllegalAccessException e) {
                    System.out.println("Cannot access: " + method.getName());
                }
            }
        }

        System.out.println("Total test methods executed: " + count);
    }
}
