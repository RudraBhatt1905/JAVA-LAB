public class PartTime extends Employee {

    private double hours;
    private double rate;

    public PartTime(String name, int id, double hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    @Override
    public double monthlySalary() {
        return hours * rate;
    }
}