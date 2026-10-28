
import java.util.*;

public class UniqueVisitors {
    public static void main(String[] args) {
        List<String> logins = Arrays.asList(
            "Rahul", "Priya", "Amit", "Rahul",
            "Neha", "Priya", "Karan"
        );

        // HashSet counts unique users
        HashSet<String> uniqueUsers = new HashSet<>(logins);

        // LinkedHashSet keeps first-seen order
        LinkedHashSet<String> orderedUsers = new LinkedHashSet<>(logins);

        // Print unique user count
        System.out.println("Unique visitors: " + uniqueUsers.size());

        // Print the first three distinct names
        System.out.println("First three visitors:");

        int count = 0;
        for (String name : orderedUsers) {
            if (count == 3) {
                break;
            }

            System.out.println(name);
            count++;
        }
    }
}
