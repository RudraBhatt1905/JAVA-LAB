
import java.util.Arrays;
import java.util.NoSuchElementException;

class GenericStack<T> {
    private Object[] arr = new Object[5];
    private int top = -1;

    // Push an element onto the stack
    public void push(T value) {
        if (top == arr.length - 1) {
            arr = Arrays.copyOf(arr, arr.length * 2);
        }

        arr[++top] = value;
    }

    // Remove and return the top element
    @SuppressWarnings("unchecked")
    public T pop() {
        if (top == -1) {
            throw new NoSuchElementException("Stack is empty!");
        }

        T value = (T) arr[top];
        arr[top] = null;
        top--;

        return value;
    }

    // Check whether the stack is empty
    public boolean isEmpty() {
        return top == -1;
    }
}

public class GenericStackDemo {
    public static void main(String[] args) {

        // Stack of Strings
        GenericStack<String> names = new GenericStack<>();
        names.push("Rahul");
        names.push("Priya");

        System.out.println("String Stack:");
        System.out.println(names.pop());
        System.out.println(names.pop());

        // Stack of Integers
        GenericStack<Integer> numbers = new GenericStack<>();
        numbers.push(10);
        numbers.push(20);

        System.out.println("\nInteger Stack:");
        System.out.println(numbers.pop());
        System.out.println(numbers.pop());

        // Handle empty stack
        try {
            numbers.pop();
        } catch (NoSuchElementException e) {
            System.out.println("\n" + e.getMessage());
        }
    }
}
