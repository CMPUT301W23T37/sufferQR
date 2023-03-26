package com.example.sufferqr;

import android.content.Context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



/**
 * Tests the EmojiDraw class to make sure it functions properly
 */
public class EmojiDrawTest {

    /**
     * Tests to make sure that a visual representation is printed
     */
    @Test
    void testDraw() {
        Context context = ApplicationProvider.getApplicationContext();
        EmojiDraw emojiDraw = new EmojiDraw("test", context);
        String emoji = emojiDraw.draw();
        assertNotNull(emoji);
        assertFalse(emoji.isEmpty());
    }

    /**
     * Tests to make sure that a visual representation is unique for every qrcode by making
     * sure it equals the same value as before
     */
    @Test
    public void testUnique() {
        String qrhash = "rickroll";
        Context context = ApplicationProvider.getApplicationContext();
        EmojiDraw emojiDraw = new EmojiDraw(qrhash, context);
        String expectedOutput =
                        "OOOOOOOOOOOOOOOOOOOOO\n" +
                        "O                   O\n" +
                        "O                   O\n" +
                        "O                   O\n" +
                        "O  (((((   )))))    O\n" +
                        "O                   O\n" +
                        "O   $$$   $$$       O\n" +
                        "O  --       --      O\n" +
                        "O       <           O\n" +
                        "O      ####         O\n" +
                        "O  {EEEEEEEEEE}     O\n" +
                        "O                   O\n" +
                        "O      VVV          O\n" +
                        "O                   O\n" +
                        "O                   O\n" +
                        "OOOOOOOOOOOOOOOOOOOOO\n";
        assertEquals(expectedOutput, emojiDraw.draw());
    }
}