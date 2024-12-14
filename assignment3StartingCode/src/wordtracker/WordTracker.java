package wordtracker;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * WordTracker
 *
 * Loads an existing BST (of WordInfo) from repository.ser if available.
 * Processes input files to track word occurrences (file and line numbers),
 * saves results back to repository.ser, and generates reports.
 *
 * Usage:
 *   java -jar WordTracker.jar <input.txt> -pf|-pl|-po [-f <output.txt>]
 */
public class WordTracker {
    private static final String REPOSITORY_FILE = "repository.ser";
    private BSTree<WordInfo> wordTree;

    public WordTracker() {
        loadRepository();
    }

    @SuppressWarnings("unchecked")
    private void loadRepository() {
        if (Files.exists(Paths.get(REPOSITORY_FILE))) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(REPOSITORY_FILE))) {
                wordTree = (BSTree<WordInfo>) ois.readObject();
            } catch (Exception e) {
                System.err.println("Error loading repository: " + e.getMessage());
                wordTree = new BSTree<>();
            }
        } else {
            wordTree = new BSTree<>();
        }
    }

    private void saveRepository() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPOSITORY_FILE))) {
            oos.writeObject(wordTree);
        } catch (IOException e) {
            System.err.println("Error saving repository: " + e.getMessage());
        }
    }

    public void processFile(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));
            Pattern wordPattern = Pattern.compile("[^a-zA-Z]+");

            for (int lineNum = 0; lineNum < lines.size(); lineNum++) {
                String[] words = wordPattern.split(lines.get(lineNum).toLowerCase());
                for (String w : words) {
                    if (!w.isEmpty()) {
                        WordInfo temp = new WordInfo(w);
                        BSTreeNode<WordInfo> existingNode = wordTree.search(temp);

                        if (existingNode == null) {
                            wordTree.add(temp);
                            existingNode = wordTree.search(temp);
                        }

                        existingNode.getElement().addLocation(filename, lineNum + 1);
                    }
                }
            }
            saveRepository();
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }

    public void generateReport(String reportType, String outputFile) {
        final PrintStream output;
        if (outputFile != null) {
            try {
                output = new PrintStream(new FileOutputStream(outputFile));
            } catch (FileNotFoundException e) {
                System.err.println("Error creating output file: " + e.getMessage());
                return;
            }
        } else {
            output = System.out;
        }

        Iterator<WordInfo> it = wordTree.inorderIterator();
        while (it.hasNext()) {
            WordInfo info = it.next();
            switch (reportType) {
                case "-pf":
                    // Word and the list of files it appears in
                    output.printf("Word: %s, Files: %s%n",
                            info.getWord(),
                            String.join(", ", info.getFileLocations().keySet()));
                    break;

                case "-pl":
                    // Word, and each file with line numbers
                    output.printf("Word: %s%n", info.getWord());
                    info.getFileLocations().forEach((file, lines) -> {
                        output.printf("  File: %s, Lines: %s%n", file, lines);
                    });
                    break;

                case "-po":
                    // Word, total occurrences, and each file with line numbers
                    output.printf("Word: %s, Occurrences: %d%n", info.getWord(), info.getOccurrences());
                    info.getFileLocations().forEach((file, lines) -> {
                        output.printf("  File: %s, Lines: %s%n", file, lines);
                    });
                    break;

                default:
                    System.err.println("Invalid report type: " + reportType);
                    break;
            }
        }

        if (output != System.out) {
            output.close();
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar WordTracker.jar <input.txt> -pf|-pl|-po [-f <output.txt>]");
            return;
        }

        WordTracker tracker = new WordTracker();
        String inputFile = args[0];
        String reportType = args[1];

        // Check if an output file is specified
        String outputFile = null;
        if (args.length > 3 && args[2].equals("-f")) {
            outputFile = args[3];
        }

        tracker.processFile(inputFile);
        tracker.generateReport(reportType, outputFile);
    }
}
