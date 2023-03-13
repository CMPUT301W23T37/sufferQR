package com.example.sufferqr;
import java.util.HashMap;

/**
 * Calculates the total score that the player gets from a qrcode
 */
public class ScoreCounter {

    private String qrhash;

    /**
     * sets a qrhhash variable
     * @param qrhash the hash value
     */
    public ScoreCounter(String qrhash) {
        this.qrhash = qrhash;
    }

    /**
     * this method mathematically calculates the score of the player
     * @return integer
     */
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
