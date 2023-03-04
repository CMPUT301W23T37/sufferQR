package com.example.sufferqr;
import java.util.HashMap;

/**
 * This is a class will take a qrcode as input in the form of a dictionary and create an emoji
 * composed of text with it
 */
public class EmojiDraw {
    private String qrcode;
    private String[] bits;
    private HashMap<String, String[]> dict;

    public EmojiDraw(String qrcode) {
        this.qrcode = qrcode;
        this.bits = qrcodeToBits(qrcode);
        this.dict = createDict();
    }

    public void draw() {
        String head = dict.get("head")[Integer.parseInt(bits[0], 2)];
        String eyebrows = dict.get("eyebrows")[Integer.parseInt(bits[1], 2)];
        String eyes = dict.get("eyes")[Integer.parseInt(bits[2], 2)];
        String nose = dict.get("nose")[Integer.parseInt(bits[3], 2)];
        String facialHair = dict.get("facialHair")[Integer.parseInt(bits[4], 2)];
        String mouth = dict.get("mouth")[Integer.parseInt(bits[5], 2)];
        String ears = dict.get("ears")[Integer.parseInt(bits[6], 2)];
        String[] headLines = head.split("\n");
        int headWidth = headLines[0].length();
        int headHeight = headLines.length;
        int centerX = headWidth / 2;
        int centerY = headHeight / 2;
        int startX = centerX - (ears.length() > 6 ? 2 : 1);
        int endX = centerX + (ears.length() > 6 ? 2 : 1);
        for (int y = 0; y < headHeight; y++) {
            for (int x = 0; x < headWidth; x++) {
                if ((x >= startX && x <= endX) && (y == 0 || y == headHeight - 1)) {
                    System.out.print(ears.charAt(x - startX));
                } else if (x > 0 && x < headWidth - 1 && y > 0 && y < headHeight - 1) {
                    if (y == 1 && x > 1 && x < headWidth - 2) {
                        System.out.print(eyebrows.charAt(x - 2));
                    } else if (y == centerY - 1 && x == centerX - 1) {
                        System.out.print(eyes.charAt(0));
                    } else if (y == centerY - 1 && x == centerX) {
                        System.out.print(eyes.charAt(1));
                    } else if (y == centerY - 1 && x == centerX + 1) {
                        System.out.print(eyes.charAt(2));
                    } else if (y == centerY && x == centerX - 1) {
                        System.out.print(nose.charAt(0));
                    } else if (y == centerY && x == centerX) {
                        System.out.print(facialHair.charAt(0));
                    } else if (y == centerY && x == centerX + 1) {
                        System.out.print(nose.charAt(1));
                    } else if (y == centerY + 1 && x == centerX) {
                        System.out.print(mouth);
                    } else {
                        System.out.print(headLines[y].charAt(x));
                    }
                } else {
                    System.out.print(headLines[y].charAt(x));
                }
            }
            System.out.println();
        }
    }

    private String[] qrcodeToBits(String qrcode) {
        // Split qrcode into 7-bit chunks
        String[] chunks = qrcode.split("(?<=\\G.{7})");
        // Convert each chunk to binary string
        String[] bits = new String[chunks.length];
        for   (int i = 0; i < chunks.length; i++) {
            bits[i] = Integer.toBinaryString(Integer.parseInt(chunks[i], 16));
            // Add leading zeros if necessary
            bits[i] = "0".concat(bits[i]);
            while (bits[i].length() < 8) {
                bits[i] = "0".concat(bits[i]);
            }
        }
        return bits;
    }

    private HashMap<String, String[]> createDict() {
        HashMap<String, String[]> dict = new HashMap<>();
        dict.put("head", new String[] {
                "  /\\  \n" +
                        " /  \\ \n" +
                        "/____\\",
                "  ___ \n" +
                        " / _ \\\n" +
                        "( (_) )\n" +
                        " \\___/",
                "   /\\  \n" +
                        "  /  \\ \n" +
                        " /____\\"
        });
        dict.put("eyebrows", new String[] {
                "\\   /", "---   ---", "### ###"
        });
        dict.put("eyes", new String[] {
                "X     X", "0     0", "3     3"
        });
        dict.put("nose", new String[] {
                "W", "O", "\\/"
        });
        dict.put("facialHair", new String[] {
                "", "=======", "VVVVVVV"
        });
        dict.put("mouth", new String[] {
                "*", ":P"
        });
        dict.put("ears", new String[] {
                "**  **", "()  ()", ""
        });
        return dict;
    }
}





