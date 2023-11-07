import java.io.*;
import java.util.*;

public class TextFileProcessor {
    private String normalizeWord(String word) {
        // replaces the needed items and puts things into lower case
        return word.replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    // Processes the text filecounts word frequencies but does not count the stop words
    public Map<String, Integer> processFile(File file, Set<String> stopWords) throws IOException {
        Map<String, Integer> frequencyMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String rawWord : line.split("\\s+")) {
                    String word = normalizeWord(rawWord);
                    // Checks if the word is a stop word
                    if (!word.isEmpty() && !stopWords.contains(word)) {
                        // Update frequency map with word count
                        frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }
        return frequencyMap;
    }
}

