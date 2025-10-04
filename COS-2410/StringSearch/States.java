package StringSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * <h2>States</h2>
 *
 * <p>Utility class containing the names of the 50 U.S. states and helpers to:
 * (1) display them, and (2) search for a substring within each name using the
 * Boyer–Moore <em>bad-character</em> heuristic.</p>
 *
 * <h3>Search semantics</h3>
 * <ul>
 *   <li>Matching is <strong>case-insensitive</strong> (both pattern and state names
 *       are lower-cased for matching).</li>
 *   <li>Only the <em>bad-character</em> rule of Boyer–Moore is used, which is the
 *       algorithm required by the assignment.</li>
 *   <li>Results are the <strong>array indices</strong> (0–49) of the states where the
 *       pattern occurs as a substring.</li>
 * </ul>
 *
 * <p>The class is stateless; all methods are static.</p>
 */
public final class States {

    /** ASCII table size used for last-occurrence preprocessing. */
    private static final int ASCII_SIZE = 256;

    /** The 50 U.S. states, in a fixed order. */
    private static final String[] STATES = {
            "Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado",
            "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho",
            "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana",
            "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
            "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
            "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon",
            "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
            "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
            "West Virginia", "Wisconsin", "Wyoming"
    };

    /** Private constructor to prevent instantiation. */
    private States() {}

    /**
     * Prints all state names as a comma-separated list with simple word wrapping.
     *
     * <p>Formatting is performed with a 120-character soft wrap to keep lines readable
     * in a typical console.</p>
     *
     * <p><strong>Side effect:</strong> writes formatted output to {@code System.out}.</p>
     */
    public static void display() {
        StringBuilder output = new StringBuilder("\nUS States:\n");
        int lineLength = 0;
        final int maxLineLength = 120;

        for (int i = 0; i < STATES.length; i++) {
            String state = STATES[i];
            String toAppend = (i == STATES.length - 1) ? state : state + ", ";

            if (lineLength + toAppend.length() > maxLineLength) {
                output.append('\n');
                lineLength = 0;
            }
            output.append(toAppend);
            lineLength += toAppend.length();
        }

        System.out.println(output);
    }

    /**
     * Prompts the user for a search pattern and prints matching state indexes.
     *
     * <p>Reads a single line from {@code scanner}. The search is case-insensitive.
     * If the input is blank, an informational message is printed and the method returns.</p>
     *
     * <p><strong>Side effect:</strong> writes results to {@code System.out}.</p>
     *
     * @param scanner a non-null {@link Scanner} to read user input
     * @throws NullPointerException if {@code scanner} is {@code null}
     */
    public static void findStateIndexesByPattern(Scanner scanner) {
        if (scanner == null) {
            throw new NullPointerException("scanner");
        }

        System.out.print("Enter search pattern (substring): ");
        String pattern = scanner.nextLine().trim();

        if (pattern.isEmpty()) {
            System.out.println("Pattern cannot be empty.");
            return;
        }

        List<Integer> hits = findStateIndexesByPattern(pattern);

        if (hits.isEmpty()) {
            System.out.println("No matches found.");
        } else {
            System.out.println("\nMatches (index: name):");
            for (int idx : hits) {
                System.out.println(idx + ":\t" + STATES[idx]);
            }
        }
    }

    /**
     * Finds the indexes of all state names that contain {@code pattern} as a substring.
     *
     * <p>Uses the Boyer–Moore <em>bad-character</em> heuristic only.
     * Matching is case-insensitive: both the pattern and state names are lower-cased
     * prior to matching.</p>
     *
     * @param pattern the substring to search for; must be non-null and non-empty
     * @return a list of indexes into {@link #STATES} whose names contain the pattern
     * @throws IllegalArgumentException if {@code pattern} is {@code null} or empty
     */
    public static List<Integer> findStateIndexesByPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            throw new IllegalArgumentException("pattern must be non-null and non-empty");
        }

        // Normalize to lower case for case-insensitive matching
        final String normalizedPattern = pattern.toLowerCase();
        final char[] patternChars = normalizedPattern.toCharArray();

        // Preprocess the pattern once for all text candidates
        int[] lastOccurrence = buildLastOccurrenceTable(patternChars);

        List<Integer> matchingIndexes = new ArrayList<>();
        for (int stateIndex = 0; stateIndex < STATES.length; stateIndex++) {
            String normalizedState = STATES[stateIndex].toLowerCase();
            if (boyerMooreContains(normalizedState, normalizedPattern, lastOccurrence)) {
                matchingIndexes.add(stateIndex);
            }
        }
        return matchingIndexes;
    }

    /**
     * Returns {@code true} if {@code text} contains {@code pattern}, using the
     * Boyer–Moore <em>bad-character</em> rule only.
     *
     * <p>Both arguments are expected to be lower-cased by the caller for
     * case-insensitive matching.</p>
     *
     * @param text           the text to search (already normalized to lower case)
     * @param pattern        the pattern to find (already normalized to lower case)
     * @param lastOccurrence the precomputed last-occurrence table for {@code pattern}
     * @return {@code true} if {@code pattern} occurs in {@code text}; {@code false} otherwise
     */
    private static boolean boyerMooreContains(String text, String pattern, int[] lastOccurrence) {
        char[] textChars = text.toCharArray();
        char[] patternChars = pattern.toCharArray();

        int patternLength = patternChars.length;
        int textLength = textChars.length;

        if (patternLength > textLength) return false;

        // offset is the current alignment of the pattern against the text
        int offset = 0;

        while (offset <= textLength - patternLength) {
            int j = patternLength - 1;

            // Move left while characters match
            while (j >= 0 && patternChars[j] == textChars[offset + j]) {
                j--;
            }

            if (j < 0) {
                // Full match at the current offset
                return true;
            } else {
                // Bad character rule: align the mismatched text char with its
                // last occurrence in the pattern
                char bad = textChars[offset + j];
                int last = (bad < ASCII_SIZE) ? lastOccurrence[bad] : -1;
                int shift = Math.max(1, j - last);
                offset += shift;
            }
        }
        return false;
    }

    /**
     * Builds a “last occurrence” table for the Boyer–Moore bad-character rule.
     *
     * <p>For each ASCII character, stores the rightmost index at which it occurs
     * in the pattern, or -1 if it does not occur. Characters with code points
     * ≥ 256 are treated as absent and map to -1.</p>
     *
     * @param patternChars the pattern as a character array (already lower-cased)
     * @return an array of length 256 mapping ASCII code → last index in the pattern
     */
    private static int[] buildLastOccurrenceTable(char[] patternChars) {
        int[] last = new int[ASCII_SIZE];
        for (int i = 0; i < ASCII_SIZE; i++) last[i] = -1;

        for (int i = 0; i < patternChars.length; i++) {
            char c = patternChars[i];
            if (c < ASCII_SIZE) {
                last[c] = i;
            }
            // Non-ASCII characters are left as -1
        }
        return last;
    }
}
