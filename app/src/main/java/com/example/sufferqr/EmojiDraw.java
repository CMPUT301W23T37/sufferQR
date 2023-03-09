package com.example.sufferqr;
import java.util.HashMap;

/**
 * This is a class will take a qrcode as input in the form of a dictionary and create an emoji
 * composed of text with it
 */
import java.util.HashMap;
import java.util.Random;

public class EmojiDraw {
    private static final int SHAPE_BIT_LENGTH = 1;
    private static final int EYEBROW_BIT_LENGTH = 2;
    private static final int EYE_BIT_LENGTH = 2;
    private static final int NOSE_BIT_LENGTH = 2;
    private static final int EAR_BIT_LENGTH = 2;
    private static final int MUSTACHE_BIT_LENGTH = 2;
    private static final int MOUTH_BIT_LENGTH = 2;
    private static final int BEARD_BIT_LENGTH = 1;

    private static final String[] SHAPE_OPTIONS = {"square", "triangle"};
    private static final String[] EYEBROW_OPTIONS = {
            "=====          =====",
            "          \\          //          ",
            "~~~~~          ~~~~~",
            "︵︵︵︵        ︵︵︵︵"
    };
    private static final String[] EYE_OPTIONS = {
            "        >            <    ",
            "        ^            ^    ",
            "        3             3    ",
            "        0             0    "
    };
    private static final String[] NOSE_OPTIONS = {
            "              \\ /                ",
            "               O                 ",
            "               •                 ",
            "               []                  "
    };
    private static final String[] EAR_OPTIONS = {
            "\\  ",
            "@",
            "8",
            "%",
            "$",
            "/",
            " /"
    };
    private static final String[] MUSTACHE_OPTIONS = {
            "            VVVVVVVV       ",
            "            ########         ",
            "",
            "            -----------            "
    };
    private static final String[] MOUTH_OPTIONS = {
            "              [=====]       ",
            "              (┬─┬)            ",
            "              {┻━┻)              ",
            "              |+++++|        "
    };
    private static final String[] BEARD_OPTIONS = {
            "                    V                   ",
            ""
    };

    private static final HashMap<String, Integer> BIT_LENGTH_MAP = new HashMap<>();
    static {
        BIT_LENGTH_MAP.put("shape", SHAPE_BIT_LENGTH);
        BIT_LENGTH_MAP.put("eyebrow", EYEBROW_BIT_LENGTH);
        BIT_LENGTH_MAP.put("eye", EYE_BIT_LENGTH);
        BIT_LENGTH_MAP.put("nose", NOSE_BIT_LENGTH);
        BIT_LENGTH_MAP.put("ear", EAR_BIT_LENGTH);
        BIT_LENGTH_MAP.put("mustache", MUSTACHE_BIT_LENGTH);
        BIT_LENGTH_MAP.put("mouth", MOUTH_BIT_LENGTH);
        BIT_LENGTH_MAP.put("beard", BEARD_BIT_LENGTH);
    }

    private String qrCodeHashValue;

    public EmojiDraw(String qrCodeHashValue) {
        this.qrCodeHashValue = qrCodeHashValue;
    }

    public String draw() {
        System.out.println("Length of input hash: " + qrCodeHashValue.length());
        int[] bits = getBitsFromHash(qrCodeHashValue);
        int currentBitIndex = 0;

        String shapeOption = SHAPE_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += SHAPE_BIT_LENGTH;

        String eyebrowOption = EYEBROW_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += EYEBROW_BIT_LENGTH;

        String eyeOption = EYE_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += EYE_BIT_LENGTH;

        String noseOption = NOSE_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += NOSE_BIT_LENGTH;

        String earOption = EAR_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += EAR_BIT_LENGTH;

        String mustacheOption = MUSTACHE_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += MUSTACHE_BIT_LENGTH;

        String mouthOption = MOUTH_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += MOUTH_BIT_LENGTH;

        String beardOption = BEARD_OPTIONS[bits[currentBitIndex]];
        currentBitIndex += BEARD_BIT_LENGTH;

        // Build emoji face
        String emoji = shapeOption + "\n"
                + eyebrowOption + "\n"
                + earOption + eyeOption + noseOption + eyeOption + earOption + "\n"
                + mustacheOption + "\n"
                + mouthOption + "\n"
                + beardOption + "\n";

        // Print emoji face
        return  emoji;
    }


    private int[] getBitsFromHash(String hash) {
        int expectedLength = SHAPE_BIT_LENGTH + EYEBROW_BIT_LENGTH + EYE_BIT_LENGTH
                + NOSE_BIT_LENGTH + EAR_BIT_LENGTH + MUSTACHE_BIT_LENGTH
                + MOUTH_BIT_LENGTH + BEARD_BIT_LENGTH;

        if (hash.length() < expectedLength) {
            throw new IllegalArgumentException("Input hash is too short");
        }

        int[] bits = new int[expectedLength];

        int bitIndex = 0;

        for (char c : hash.toCharArray()) {
            int decimal;
            try {
                decimal = Integer.parseInt(String.valueOf(c), 16);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid hexadecimal character in input hash: " + c);
            }
            String binary = Integer.toBinaryString(decimal);

            while (binary.length() < 4) {
                binary = "0" + binary;
            }

            for (char b : binary.toCharArray()) {
                bits[bitIndex] = Character.getNumericValue(b);
                bitIndex++;
            }
        }
        return bits;
    }

    public static void main(String[] args) {
        String qrCodeHashValue = "8af44c49b08733e61ca2e90e9f0a93c6c6c6c6c";
        EmojiDraw emojiDraw = new EmojiDraw(qrCodeHashValue);
        emojiDraw.draw();
    }
}



