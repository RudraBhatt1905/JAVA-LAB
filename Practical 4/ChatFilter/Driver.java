import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        String[] logs = {"10:05 Rudra Hello there","10:10 Rudra How are you?","10:15 Rudra Hello everyone","InvalidLine"};

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        String result = ChatFilter.filterChats(logs, keyword);

        System.out.println(result);

        sc.close();
    }
}