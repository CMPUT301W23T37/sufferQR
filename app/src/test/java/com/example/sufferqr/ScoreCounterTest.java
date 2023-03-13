package com.example.sufferqr;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Test to see if the ScoreCounter functions properly
 */
public class ScoreCounterTest {

    /**
     * Tests to make sure no input is given a score of 0
     */
    @Test
    public void testCalculateScoreWithEmptyString() {
        ScoreCounter counter = new ScoreCounter("");
        int score = counter.calculateScore();
        assertEquals(0, score);
    }
    /**
     * Tests to make sure if no character is repeating, you will score a 0
     */
    @Test
    public void testCalculateScoreWithUniqueCharacters() {
        ScoreCounter counter = new ScoreCounter("abcd");
        int score = counter.calculateScore();
        assertEquals(0, score);
    }
    /**
     * Tests to make sure if one repetition is counted,it will still go and not stop
     */
    @Test
    public void testCalculateScoreWithRepeatedCharacters() {
        ScoreCounter counter = new ScoreCounter("aabbccdd");
        int score = counter.calculateScore();
        assertEquals(960, score);
    }

    /**
     * Tests to make sure the repetition of a character is counted more than just once
     */
    @Test
    public void testCalculateScoreWithMoreThanDoubleRepetition() {
        ScoreCounter counter = new ScoreCounter("aaaabbbcccc");
        int score = counter.calculateScore();
        assertEquals(1078, score);
    }
    /**
     * Tests to make sure class any random combination of characters can work
     */
    @Test
    public void testCalculateScoreWithMixedCharacters() {
        ScoreCounter counter = new ScoreCounter("aabbccDDEEFF1122");
        int score = counter.calculateScore();
        assertEquals(19680, score);
    }

}
