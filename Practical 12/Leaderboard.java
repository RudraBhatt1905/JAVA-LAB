
import java.util.*;

public class Leaderboard {
    public static void main(String[] args) {

        // Store player names and scores
        Map<String, Integer> scores = new HashMap<>();

        scores.put("Amit", 980);
        scores.put("Priya", 920);
        scores.put("Rahul", 920);
        scores.put("Neha", 780);
        scores.put("Aarav", 850);
        scores.put("Karan", 850);

        // Convert map entries into a list
        List<Map.Entry<String, Integer>> players =
                new ArrayList<>(scores.entrySet());

        // Sort by score (highest first), then by name
        Collections.sort(players,
            new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> a,
                                   Map.Entry<String, Integer> b) {

                    // Higher score comes first
                    int result = b.getValue().compareTo(a.getValue());

                    // If scores are equal, sort names alphabetically
                    if (result == 0) {
                        return a.getKey().compareTo(b.getKey());
                    }

                    return result;
                }
            });

        // Print the top 3 players
        int topN = 3;

        System.out.println("Leaderboard - Top " + topN);

        for (int i = 0; i < topN && i < players.size(); i++) {
            Map.Entry<String, Integer> player = players.get(i);

            System.out.println(
                player.getKey() + " : " + player.getValue()
            );
        }
    }
}
