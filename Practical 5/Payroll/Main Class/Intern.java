public class Intern extends Employee {

    private double stipend;

    public Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }

    @Override
    public double monthlySalary() {
        return stipend;
    }
}