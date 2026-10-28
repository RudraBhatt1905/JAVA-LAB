
class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {
    private A first;
    private B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}

public class GenericContainerDemo {

    // Generic method to return the first element of an array
    public static <T> T getFirst(T[] array) {
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        return array[0];
    }

    public static void main(String[] args) {

        // Box with String
        Box<String> b1 = new Box<>();
        b1.set("Hello Java");
        System.out.println("String Box: " + b1.get());

        // Box with Integer
        Box<Integer> b2 = new Box<>();
        b2.set(100);
        System.out.println("Integer Box: " + b2.get());

        // Pair with different types
        Pair<String, Integer> p1 = new Pair<>("Rahul", 21);
        System.out.println("Pair: " + p1.getFirst()
                           + ", " + p1.getSecond());

        // Generic method with different arrays
        String[] names = {"Amit", "Priya", "Rahul"};
        Integer[] numbers = {10, 20, 30};

        System.out.println("First name: " + getFirst(names));
        System.out.println("First number: " + getFirst(numbers));
    }
}
