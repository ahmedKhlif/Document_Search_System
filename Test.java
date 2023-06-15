package index;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the directory path: ");
        String directoryPath = scanner.nextLine();

        System.out.print("Enter the word to search: ");
        String wordToSearch = scanner.nextLine();

        try {
            List<Path> textFiles = findTextFiles(directoryPath);

            for (Path filePath : textFiles) {
                List<String> lines = readLinesFromFile(filePath.toString());
                List<Integer> matchingLineNumbers = getMatchingLineNumbers(lines, wordToSearch);

                if (matchingLineNumbers.isEmpty()) {
                    System.out.println("The word '" + wordToSearch + "' was not found in the file: " + filePath);
                } else {
                    System.out.println("Search Results for the word '" + wordToSearch + "' in file: " + filePath);
                    for (int lineNumber : matchingLineNumbers) {
                        System.out.println("Line Number: " + lineNumber);
                        System.out.println("Line: " + lines.get(lineNumber - 1)); // -1 because line numbers start from 1
                        System.out.println();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Path> findTextFiles(String directoryPath) throws IOException {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directoryPath), "*.txt")) {
            List<Path> textFiles = new ArrayList<>();
            for (Path path : directoryStream) {
                textFiles.add(path);
            }
            return textFiles;
        }
    }

    private static List<String> readLinesFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readAllLines(path);
    }

    private static List<Integer> getMatchingLineNumbers(List<String> lines, String wordToSearch) {
        List<Integer> matchingLineNumbers = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.toLowerCase().contains(wordToSearch.toLowerCase())) {
                matchingLineNumbers.add(i + 1); // +1 because line numbers start from 1
            }
        }
        return matchingLineNumbers;
    }
}
