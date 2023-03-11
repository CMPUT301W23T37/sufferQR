package com.example.sufferqr;

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




public class EmojiDraw {
    private final String qrHash;
    private final HashMap<String, String[]> featuresDict;

    public EmojiDraw(String qrHash) {
        this.qrHash = qrHash;
        this.featuresDict = initFeaturesDict();
    }


    // Initialize the dictionary with all the possible facial features
    private HashMap<String, String[]> initFeaturesDict() {
        HashMap<String, String[]> dict = new HashMap<>();

        // Shapes
        dict.put("00", new String[]{"*******", "*     *", "*     *", "*     *", "*     *", "*     *", "*******"});
        dict.put("01", new String[]{"  ***  ", " *   * ", "*     *", "*     *", "*     *", " *   * ", "  ***  "});

        // Eyebrows
        dict.put("00", new String[]{"     ", "=====", "     ", "     "});
        dict.put("01", new String[]{"     ", "     ", "     ", "︵︵︵︵"});
        dict.put("10", new String[]{"\\    ", "     ", "     ", "     "});
        dict.put("11", new String[]{"//   ", "     ", "     ", "     "});

        // Eyes
        dict.put("00", new String[]{"  >  ", "     ", "     ", "     "});
        dict.put("01", new String[]{"  <  ", "     ", "     ", "     "});
        dict.put("10", new String[]{"  ^  ", "     ", "     ", "     "});
        dict.put("11", new String[]{"  3  ", "     ", "     ", "     "});

        // Nose
        dict.put("00", new String[]{"     ", " / \\ ", "/   \\", "\\   /", " \\ / ", "     ", "     "});
        dict.put("01", new String[]{"  O  ", "     ", "     ", "     "});
        dict.put("10", new String[]{"  •  ", "     ", "     ", "     "});
        dict.put("11", new String[]{"  [] ", "     ", "     ", "     "});

        // Ears
        dict.put("00", new String[]{" \\  ", "@   ", " \\  ", "     "});
        dict.put("01", new String[]{" \\  ", "8   ", " /  ", "     "});
        dict.put("10", new String[]{" \\  ", "%   ", "/   ", "     "});
        dict.put("11", new String[]{" \\  ", "$   ", "\"   ", "     "});

        // Mustache
        dict.put("00", new String[]{"      VVVVVVVV      ", "                    "});
        dict.put("01", new String[]{"      ########      ", "                    "});
        dict.put("10", new String[]{"                      ", "                    "});
        dict.put("11", new String[]{"      -----------       ", "                    "});

        // Mouth
        dict.put("00", new String[]{"              [=====]              ", "                    "});
        dict.put("01", new String[]{"              (┬─┬)                   ", "                    "});
        dict.put("10", new String[]{"              {┻━┻)                    ", "                    "});
        dict.put("11", new String[]{"              |+++++|              ", "                   "});

        // Moustache
        dict.put("00", new String[]{" ", " VVVVV", " ", " "});
        dict.put("01", new String[]{" ", "######", " ", " "});
        dict.put("10", new String[]{" ", " ", " ", " "});
        dict.put("11", new String[]{" ", "------", " ", " "});

        // Mouth
        dict.put("00", new String[]{"      ", "      ", "[=====]", "      "});
        dict.put("01", new String[]{"      ", "(┬─┬)", "      ", "      "});
        dict.put("10", new String[]{"      ", "{┻━┻)", "      ", "      "});
        dict.put("11", new String[]{"      ", "|++++|", "      ", "      "});

        // Beard
        dict.put("0", new String[]{"      ", "      ", "      ", "      ", "      ", "      ", "VVVVV "});
        dict.put("1", new String[]{"      ", "      ", "      ", "      ", "      ", "      ", "      "});

        return dict;
    }

    private String getFeature(String bits, String[] options) {
        int index = Integer.parseInt(bits, 2) % options.length;
        return options[index];
    }

    public void draw() {
        String shapeBits = qrHash.substring(0, 2);
        String shape = getFeature(shapeBits, featuresDict.get(shapeBits));

        boolean isTriangle = shapeBits.charAt(0) == '0';
        int noseIndex = Integer.parseInt(qrHash.substring(4, 6), 2) % 4;
        int mouthIndex = Integer.parseInt(qrHash.substring(14, 15), 2);
        String beard = getFeature(qrHash.substring(15, 16), featuresDict.get(Integer.toString(mouthIndex)));
        String[] head = new String[7];
        if (isTriangle) {
            head[0] = "     *   ";
            head[1] = "    * *  ";
            head[2] = "   *   * ";
            head[3] = "  *     *";
            head[4] =     shape;
            head[5] = "*         *";
            head[6] = "*         *";
        } else {
            head[0] = "*******";
            head[1] = "*     *";
            head[2] = "*     *";
            head[3] = "*     *";
            head[4] =   shape;
            head[5] = "*     *";
            head[6] = "*******";

        }

        String[] leftEar = featuresDict.get(qrHash.substring(6, 8));
        String[] rightEar = featuresDict.get(qrHash.substring(8, 10));
        String[] eyebrows = featuresDict.get(qrHash.substring(2, 4));
        String[] eyes = featuresDict.get(qrHash.substring(10, 12));
        String[] nose = featuresDict.get(Integer.toString(noseIndex));
        String[] mustache = featuresDict.get(qrHash.substring(12, 14));
        String[] mouth = featuresDict.get(Integer.toString(mouthIndex));

        int noseY = isTriangle ? 4 : 3;

        // Draw face
        for (int i = 0; i < 7; i++) {
            String leftEarLine = i == noseY ? leftEar[0] : " ";
            String rightEarLine = i == noseY ? rightEar[0] : " ";
            System.out.print(leftEarLine + " " + eyebrows[i] + " " + eyes[i] + " " + nose[i] + " " + eyes[i] + " " + eyebrows[i] + " " + rightEarLine + "\n");
        }
        System.out.print(" " + mustache[0] + beard + mustache[0] + "\n");
        System.out.print(" " + mouth[0] + "\n");
    }



}


