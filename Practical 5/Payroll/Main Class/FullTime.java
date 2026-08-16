public class FullTime extends Employee {

    private double fixedSalary;

    public FullTime(String name, int id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    @Override
    public double monthlySalary() {
        return fixedSalary;
    }
}