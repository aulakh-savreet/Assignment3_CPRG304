package wordtracker;

import java.io.Serializable;
import java.util.*;

/**
 * WordInfo
 *
 * This class stores information about a particular word, including:
 * - The word (as a String)
 * - A map from filenames to sets of line numbers where the word occurs
 *
 * It implements Comparable to allow ordering by the word itself, and
 * is Serializable to enable writing to and reading from a repository file.
 */
public class WordInfo implements Comparable<WordInfo>, Serializable {
    private static final long serialVersionUID = 1L;
    private String word;
    private Map<String, Set<Integer>> fileLocations;

    /**
     * Constructs a WordInfo for a given word.
     * @param word the word to track
     */
    public WordInfo(String word) {
        this.word = word;
        this.fileLocations = new HashMap<>();
    }

    /**
     * Adds a file and line number occurrence for the word.
     * @param filename the file where the word appears
     * @param lineNumber the line number on which the word appears
     */
    public void addLocation(String filename, int lineNumber) {
        fileLocations.computeIfAbsent(filename, k -> new TreeSet<>()).add(lineNumber);
    }

    /**
     * Gets the word being tracked.
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets a map of file locations for this word.
     * @return a map from filenames to sets of line numbers
     */
    public Map<String, Set<Integer>> getFileLocations() {
        return fileLocations;
    }

    /**
     * Calculates the total number of occurrences (sum of all line numbers across all files).
     * @return the total occurrence count
     */
    public int getOccurrences() {
        return fileLocations.values().stream()
            .mapToInt(Set::size)
            .sum();
    }

    /**
     * Compares two WordInfo objects by their word value.
     * @param other another WordInfo
     * @return a negative, zero, or positive integer if this word is less than, equal to,
     *         or greater than the other word.
     */
    @Override
    public int compareTo(WordInfo other) {
        return this.word.compareTo(other.word);
    }
}
