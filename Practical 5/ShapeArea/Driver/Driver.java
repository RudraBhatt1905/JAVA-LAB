public class Driver {

    public static void main(String[] args) {

        Shape[] shapes = {
            new Circle(5),
            new Rectangle(4, 6),
            new Triangle(10, 5),
            new Circle(3)
        };

        double total = 0;
        double largest = 0;

        for (Shape shape : shapes) {

            double area = shape.area();

            System.out.println("Area: " + area);

            total += area;

            if (area > largest) {
                largest = area;
            }

            System.out.println("Running Total: " + total);
        }

        System.out.println("Total Area: " + total);
        System.out.println("Largest Area: " + largest);
    }
}