package com.example.sufferqr;
import static java.lang.Math.abs;

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
        int base = 0;
        int pos = 0;

        // Count the number of occurrences of each character in the qrhash
        for (char c : qrhash.toCharArray()) {
            if (charCount.containsKey(c)) {
                charCount.put(c, charCount.get(c) + 1);
            } else {
                charCount.put(c, 1);
            }

            int ascii = (int) abs((int) c - 64) % 20;
            int divider = (pos % 4)+((int)pos/ 4)+1;
            int mini_point = ascii/divider;
            base+=(int) mini_point;
            pos=pos+3;
        }

        base = base % 5+ base /5+1;

//        // Calculate the score based on the number of occurrences of each character
        for (char c : charCount.keySet()) {
            int count = charCount.get(c);
            int ascii = (int) abs((int) c - 64) ;
            if (count > 1) {
                int nt = c * base;
                score += (int) nt / count;
            }
        }
        return score;
    }

}
