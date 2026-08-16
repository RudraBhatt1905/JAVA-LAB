public class Driver {

    public static void main(String[] args) {

        Employee[] employees = {
            new FullTime("Rudra", 101, 50000),
            new PartTime("Amit", 102, 80, 300),
            new Intern("Rahul", 103, 15000),
            new FullTime("Priya", 104, 60000),
            new Intern("Megh", 105, 12000)
        };

        double total = 0;

        for (Employee employee : employees) {

            double salary = employee.monthlySalary();

            System.out.println(
                employee.getName() + " (ID: " +
                employee.getId() + ") - Salary: " +
                salary
            );

            if (employee instanceof Intern) {
                System.out.println("  Note: This is an intern.");
            }

            total += salary;
        }

        System.out.println("--------------------");
        System.out.println("Total Payroll: " + total);
    }
}