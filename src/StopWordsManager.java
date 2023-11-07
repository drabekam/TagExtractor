//needed imports
import java.io.*;
import java.util.*;

public class StopWordsManager {

    //THis creates the stopwords so that they can be used later to handle the required process of using stop words
    public Set<String> loadStopWords(File file) throws IOException {
        // Create a new TreeSet to hold the stop words
        Set<String> stopWords = new TreeSet<>();

        // Open a BufferedReader to read the file line by line
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String word;

            // Read each line of the file until the end is reached
            while ((word = reader.readLine()) != null) {
                // Add each word to the set after trimming whitespace and converting to lowercase as needed
                stopWords.add(word.trim().toLowerCase());
            }
        }

        // Returns the treeset
        return stopWords;
    }
}

