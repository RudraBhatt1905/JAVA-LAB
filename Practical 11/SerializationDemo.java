
import java.io.*;

class Student implements Serializable {
    int id;
    String name;
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    void display() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Password: " + password);
        System.out.println();
    }
}

public class SerializationDemo {
    public static void main(String[] args) {
        try {
            Student[] students = {
                new Student(1, "Rahul", "rahul123"),
                new Student(2, "Priya", "priya456")
            };

            ObjectOutputStream out =
                new ObjectOutputStream(new FileOutputStream("students.dat"));

            out.writeObject(students);
            out.close();

            System.out.println("Objects saved successfully!");

            ObjectInputStream in =
                new ObjectInputStream(new FileInputStream("students.dat"));

            Student[] loadedStudents = (Student[]) in.readObject();
            in.close();

            System.out.println("\nLoaded objects:");
            for (Student s : loadedStudents) {
                s.display();
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
