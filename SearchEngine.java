package index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchEngine {
    public static List<String> booleanSearch(String query, Map<String, List<String>> index) {
        List<String> queryTerms = Document.preprocessText(query);
        List<String> results = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : index.entrySet()) {
            List<String> documentTerms = entry.getValue();
            if (documentTerms.containsAll(queryTerms)) {
                results.add(entry.getKey());
            }
        }

        return results;
    }

    // Calculate the term frequency (TF) of a term in a document
    public static double calculateTermFrequency(String term, List<String> document) {
        long count = document.stream().filter(word -> word.equals(term)).count();
        return (double) count / document.size();
    }

    // Calculate the inverse document frequency (IDF) of a term in the index
    public static double calculateInverseDocumentFrequency(String term, Map<String, List<String>> index) {
        long count = index.values().stream().filter(document -> document.contains(term)).count();
        return Math.log((double) index.size() / (count + 1));
    }

    // Calculate the TF-IDF score of a term in a document
    public static double calculateTfIdf(String term, List<String> document, Map<String, List<String>> index) {
        double tf = calculateTermFrequency(term, document);
        double idf = calculateInverseDocumentFrequency(term, index);
        return tf * idf;
    }

    public static List<String> vectorialSearch(String query, Map<String, List<String>> index) {
        List<String> queryTerms = Document.preprocessText(query);
        Map<String, Double> documentScores = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : index.entrySet()) {
            List<String> document = entry.getValue();
            double score = 0.0;
            for (String term : queryTerms) {
                double tfIdf = calculateTfIdf(term, document, index);
                score += tfIdf;
            }
            documentScores.put(entry.getKey(), score);
        }

        // Sort the documents based on their scores (highest to lowest)
        List<String> results = documentScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return results;
    } }