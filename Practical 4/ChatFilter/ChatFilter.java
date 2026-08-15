public class ChatFilter {

    public static String filterChats(String[] logs, String keyword) {

        int count = 0;
        StringBuilder report = new StringBuilder();

        keyword = keyword.toLowerCase();

        for (String line : logs) {

            String[] parts = line.split(" ", 3);

            if (parts.length < 3) {
                continue;
            }

            String time = parts[0];
            String user = parts[1];
            String message = parts[2];

            if (message.toLowerCase().contains(keyword)) {
                count++;
                report.append(time)
                report.append(" ")
                report.append(user)
                report.append(": ")
                report.append(message)
                report.append("\n");
            }
        }

        return "Matches: " + count + "\n" + report.toString();
    }
}
