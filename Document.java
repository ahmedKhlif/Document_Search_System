package index;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Document {
    private String filename;
    private String content;

    public Document(String filename, String content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public static List<Document> readTextFiles(String directoryPath) throws IOException {
        List<Document> documents = new ArrayList<>();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String content = Files.readString(file.toPath());
                    documents.add(new Document(file.getName(), content));
                }
            }
        }

        return documents;
    }

    public static List<String> preprocessText(String text) {
        String[] words = text.toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");
        // Implement additional preprocessing steps like removing stopwords, stemming, etc.
        // Return a list of preprocessed words
        return List.of(words);
    }

    public static Map<String, List<String>> createIndex(String directoryPath) throws IOException {
        Map<String, List<String>> index = new HashMap<>();
        List<Document> documents = readTextFiles(directoryPath);

        for (Document document : documents) {
            List<String> processedText = preprocessText(document.getContent());
            index.put(document.getFilename(), processedText);
        }

        return index;
    }

    // Save index to a file for future use
    public static void saveIndexToFile(Map<String, List<String>> index, String filePath) throws IOException {
        List<String> indexData = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : index.entrySet()) {
            String filename = entry.getKey();
            String content = String.join(" ", entry.getValue());
            indexData.add(filename + ":" + content);
        }
        Files.write(Path.of(filePath), indexData);
    }

    // Load index from a file
    public static Map<String, List<String>> loadIndexFromFile(String filePath) throws IOException {
        List<String> indexData = Files.readAllLines(Path.of(filePath));
        Map<String, List<String>> index = new HashMap<>();
        for (String data : indexData) {
            String[] parts = data.split(":");
            String filename = parts[0];
            String[] words = parts[1].split(" ");
            index.put(filename, List.of(words));
        }
        return index;
    }
}



    

