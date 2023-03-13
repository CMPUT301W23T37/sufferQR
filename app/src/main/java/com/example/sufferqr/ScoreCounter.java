package com.example.sufferqr;
import java.util.HashMap;

/**
 * qrcode points calcuate
 */
public class ScoreCounter {

    private String qrhash;

    public ScoreCounter(String qrhash) {
        this.qrhash = qrhash;
    }

    public int calculateScore() {
        HashMap<Character, Integer> charCount = new HashMap<Character, Integer>();
        int score = 0;

        // Count the number of occurrences of each character in the qrhash
        for (char c : qrhash.toCharArray()) {
            if (charCount.containsKey(c)) {
                charCount.put(c, charCount.get(c) + 1);
            } else {
                charCount.put(c, 1);
            }
        }

        // Calculate the score based on the number of occurrences of each character
        for (char c : charCount.keySet()) {
            int count = charCount.get(c);
            if (count > 1) {
                score += (int) c * count;
            }
        }

        return score;
    }

}
