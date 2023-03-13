package com.example.sufferqr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmojiDrawTest {

    /**
     * Tests to make sure that a visual representation is printed
     */
    @Test
    void testDraw() {
        EmojiDraw emojiDraw = new EmojiDraw("test");
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
        EmojiDraw emojiDraw = new EmojiDraw(qrhash);
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
