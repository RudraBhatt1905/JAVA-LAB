
import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {
        String paragraph = "java is easy and java is powerful";

        // Split paragraph into words
        String[] words = paragraph.toLowerCase().split("\\s+");

        // Count word frequency using HashMap
        HashMap<String, Integer> map = new HashMap<>();

        for (String word : words) {
            map.put(word, map.getOrDefault(word, 0) + 1);
        }

        // Convert HashMap entries into a list
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(map.entrySet());

        // Sort by frequency in descending order using Comparator
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a,
                               Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });

        // Print words sorted by frequency
        System.out.println("Word frequencies:");

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
