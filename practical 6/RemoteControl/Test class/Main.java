public class Main {

    public static void main(String[] args) {

        Switchable[] devices = {
            new Fan(),
            new Light()
        };

        System.out.println("Toggling Devices:");

        for (Switchable device : devices) {
            device.toggle();
        }

        SwitchRule rule1 = new SwitchRule() {
            public boolean maySwitchOn(Switchable device, int hour) {
                return hour >= 6 && hour <= 22;
            }
        };

        SwitchRule rule2 = (device, hour) -> hour >= 8 && hour <= 20;

        System.out.println("\nAnonymous Class:");

        System.out.println(
            "Fan at 10 AM: " +
            rule1.maySwitchOn(new Fan(), 10)
        );

        System.out.println(
            "Light at 23 PM: " +
            rule1.maySwitchOn(new Light(), 23)
        );

        System.out.println("\nLambda:");

        System.out.println(
            "Fan at 10 AM: " +
            rule2.maySwitchOn(new Fan(), 10)
        );

        System.out.println(
            "Light at 23 PM: " +
            rule2.maySwitchOn(new Light(), 23)
        );
    }
}