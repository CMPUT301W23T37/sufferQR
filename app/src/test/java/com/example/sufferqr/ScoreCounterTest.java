package com.example.sufferqr;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


/**
 * Test to see if the ScoreCounter functions properly
 */
public class ScoreCounterTest {
    @Test
    public void testCalculateScoreWithEmptyString() {
        ScoreCounter counter = new ScoreCounter("");
        int score = counter.calculateScore();
        assertEquals(0, score);
    }

    @Test
    public void testCalculateScoreWithUniqueCharacters() {
        ScoreCounter counter = new ScoreCounter("abcd");
        int score = counter.calculateScore();
        assertEquals(0, score);
    }

    @Test
    public void testCalculateScoreWithRepeatedCharacters() {
        ScoreCounter counter = new ScoreCounter("aabbccdd");
        int score = counter.calculateScore();
        assertEquals(960, score);
    }

    @Test
    public void testCalculateScoreWithMixedCharacters() {
        ScoreCounter counter = new ScoreCounter("aabbccDDEEFF1122");
        int score = counter.calculateScore();
        assertEquals(19680, score);
    }

}
