public class Main {

    public static void main(String[] args) {

        Notifier email = message -> System.out.println("Email: " + message);

        Notifier sms = (Notifier & Urgent) message ->
            System.out.println("SMS: " + message);

        Notifier[] senders = {
            email,
            sms
        };

        String message = "Exam tomorrow!";

        for (Notifier sender : senders) {

            sender.send(message);

            if (sender instanceof Urgent) {
                sender.send(message);
            }
        }
    }
}
