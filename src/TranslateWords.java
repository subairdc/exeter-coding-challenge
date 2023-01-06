import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TranslateWords {
    public static void main(String[] args) throws IOException {
        // Read in the find words list
        BufferedReader findWordsReader = new BufferedReader(new FileReader("src/find_words.txt"));
        Map<String, Boolean> findWords = new HashMap<>();
        String line = findWordsReader.readLine();
	        while (line != null) {
	            findWords.put(line, true);
	            line = findWordsReader.readLine();
	        }
        findWordsReader.close();

        // Read in the dictionary
        BufferedReader dictionaryReader = new BufferedReader(new FileReader("src/french_dictionary.csv"));
        Map<String, String> dictionary = new HashMap<>();
        line = dictionaryReader.readLine(); // skip the first line
        while ((line = dictionaryReader.readLine()) != null) {
            String[] parts = line.split(",");
            String english = parts[0];
            String french = parts[1];
            dictionary.put(english, french);
        }
        dictionaryReader.close();

        // Read in the input text file
        BufferedReader textReader = new BufferedReader(new FileReader("src/t8.shakespeare.txt"));

        // Write the translated text to a new file
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/t8.shakespeare.translated.txt"));

        // Map to store the frequency of each word replacement
        Map<String, Integer> frequency = new HashMap<>();

        line = textReader.readLine();
        while (line != null) {
            // Split the line into words
            String[] words = line.split("[ ,]+");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                // Check if the word is in the find words list and if it has a corresponding French translation
	             if (findWords.containsKey(word) && dictionary.containsKey(word)) {
		             // Replace the word with its French translation
		             String frenchWord = dictionary.get(word);
		             words[i] = frenchWord;
		             // Update the frequency map
		             int count = frequency.getOrDefault(word, 0);
		             frequency.put(word, count + 1);
		          }
             }
            
             // Write the translated line to the output file
             String translatedLine = String.join(" ", words);
             writer.write(translatedLine);
             writer.newLine();
             line = textReader.readLine();
       }
       textReader.close();
       writer.close();
             
       // Write the frequency map to the frequency.csv file
       BufferedWriter frequencyWriter = new BufferedWriter(new FileWriter("src/frequency.csv"));
       frequencyWriter.write("English Word,French Word,Frequency");
       frequencyWriter.newLine();
       for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
    	   String englishWord = entry.getKey();
           String frenchWord = dictionary.get(englishWord);
           int count = entry.getValue();
           frequencyWriter.write(englishWord + "," + frenchWord + "," + count);
           frequencyWriter.newLine();
       }
       frequencyWriter.close();
       
       // Write performance to performance.txt
       long startTime = System.currentTimeMillis();
       
       // Calculate time taken and memory used
       long endTime = System.currentTimeMillis();
       long timeTaken = endTime - startTime;
       long memoryUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

       // Write performance to performance.txt
       String performanceOutput = "Time to process: " + timeTaken + " milliseconds\nMemory used: " + memoryUsed + " bytes\n";
       Files.write(Paths.get("src/performance.txt"), performanceOutput.getBytes());
    }
}
