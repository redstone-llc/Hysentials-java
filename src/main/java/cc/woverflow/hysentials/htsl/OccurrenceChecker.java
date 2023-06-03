package cc.woverflow.hysentials.htsl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OccurrenceChecker {
    public static String checkOccurrences(List<Loader> arr) {
        // Define hardcoded limits for specific strings
        Map<String, Integer> limits = new HashMap<>();
        limits.put("applyLayout", 1);
        limits.put("applyPotion", 22);
        limits.put("globalstat", 5);
        limits.put("changeHealth", 1);
        limits.put("changePlayerGroup", 1);
        limits.put("stat", 5);
        limits.put("clearEffects", 1);
        limits.put("condition", 15);
        limits.put("actionBar", 1);
        limits.put("title", 1);
        limits.put("enchant", 23);
        limits.put("failParkour", 1);
        limits.put("fullHeal", 1);
        limits.put("xpLevel", 1);
        limits.put("giveItem", 20);
        limits.put("houseSpawn", 1);
        limits.put("kill", 1);
        limits.put("parkCheck", 1);
        limits.put("sound", 1);
        limits.put("random", 5);
        limits.put("removeItem", 20);
        limits.put("resetInventory", 1);
        limits.put("chat", 5);
        limits.put("lobby", 1);
        limits.put("compassTarget", 1);
        limits.put("gamemode", 1);
        limits.put("hungerLevel", 1);
        limits.put("maxHealth", 1);
        limits.put("tp", 1);
        limits.put("function", 10);
        limits.put("consumeItem", 1);

        // Create a map to keep track of the occurrences of each string
        Map<String, Integer> occurrences = new HashMap<>();

        // Loop through the array of arrays
        for (int i = 0; i < arr.size(); i++) {
            // Check the first element of each array
            String firstElement = arr.get(i).keyword;

            // Check if the first element is a string and if it has a hardcoded limit
            if (firstElement != null && limits.containsKey(firstElement)) {
                // Increment the occurrences of the string
                int count = occurrences.getOrDefault(firstElement, 0) + 1;
                occurrences.put(firstElement, count);

                // Check if the number of occurrences goes over the hardcoded limit
                if (count > limits.get(firstElement)) {
                    return firstElement;
                }
            }
        }
        return null;
    }
}
