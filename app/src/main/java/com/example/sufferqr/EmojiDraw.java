package com.example.sufferqr;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;



import java.util.Random;

/**
 * Draws the visual representation of th QRcode
 */
public class EmojiDraw {
    private final String qrhash;
    private final String[][] face;

    /**
     * stars the class by taking qrhash as input and generating a face
     * @param qrhash the hash value
     */
    //input
    public EmojiDraw(String qrhash) {
        this.qrhash = qrhash;
        this.face = generateFace();
    }

    /**
     * draws the visual representation using a 2D array
     * @return face string
     */
    //prints the visual representation
    public String draw() {
        int sizeX = face[0].length;
        int sizeY = face.length;


        // find the length of the longest row
        int longestRowLength = 0;
        for (String[] row : face) {
            if (row.length > longestRowLength) {
                longestRowLength = row.length;
            }
        }

        // print each row with leading spaces
        StringBuilder output= new StringBuilder();
        for (int i = 0; i < sizeY; i++) {
            int numLeadingSpaces = longestRowLength - face[i].length;
            for (int j = 0; j < numLeadingSpaces; j++) {
                output.append(" ");
                System.out.print(" ");
            }
            for (int j = 0; j < sizeX; j++) {
                String sa= face[i][j];
                output.append(sa);

            }
            System.out.println();
            output.append("\n");
        }
        return output.toString();
    }


    /**
     * generates the face itself by using 2D arrays to combine all the facial features into 1 2D list
     * @return string
     * */
    private String[][] generateFace() {
        Random random = new Random(qrhash.hashCode());
        int sizeY = 13 + random.nextInt(5); // random size between 13 and 18 for the y-axis
        int sizeX = 20 + random.nextInt(6); // random size between 18 and 24 for the x-axis
        String[][] face = new String[sizeY][sizeX];


        // draw face structure
        String structure = "#";
        switch (random.nextInt(4)) {
            case 0: // circle
                structure = "O";
                break;
            case 1: // square
                structure = "#";
                break;
            case 2: // rectangle
                structure = "=";
                break;
            case 3: //triangle
                structure = "&";
                break;
        }


        for (int i = 0; i < sizeY; i++) {
            for (int j = 0; j < sizeX; j++) {
                if (i == 0 || i == sizeY - 1 || j == 0 || j == sizeX - 1) {
                    face[i][j] = structure;
                } else {
                    face[i][j] = " ";
                }
            }
        }

        // draw eyebrows
        String[] eyebrowOptions = {"`````", "~~~~~~", "((((()))))","@@@@@"};
        String eyebrow = eyebrowOptions[random.nextInt(eyebrowOptions.length)];
        int eyebrowPos = (sizeY / 2) - 4; // above eyes

        switch (eyebrow) {
            case "`````":
                face[eyebrowPos][eyebrowPos-1] = "`````";
                face[eyebrowPos][eyebrowPos+3] = "`````";
                face[eyebrowPos][eyebrowPos+4] = "";face[eyebrowPos][eyebrowPos+5] = "";face[eyebrowPos][eyebrowPos+6] = "";//removed spaces occupied by eyebrows so structure stays intact
                face[eyebrowPos][eyebrowPos+7] = "";face[eyebrowPos][eyebrowPos+8] = "";face[eyebrowPos][eyebrowPos+9] = "";
                face[eyebrowPos][eyebrowPos+10] = "";face[eyebrowPos][eyebrowPos+11] = "";
                break;

            case "~~~~~~":
                face[eyebrowPos][eyebrowPos-1] = "~~~~~~";
                face[eyebrowPos][eyebrowPos+3] = "~~~~~~";
                face[eyebrowPos][eyebrowPos+4] = "";face[eyebrowPos][eyebrowPos+5] = "";face[eyebrowPos][eyebrowPos+6] = "";
                face[eyebrowPos][eyebrowPos+7] = "";face[eyebrowPos][eyebrowPos+8] = "";face[eyebrowPos][eyebrowPos+9] = "";
                face[eyebrowPos][eyebrowPos+10] = "";face[eyebrowPos][eyebrowPos+11] = "";face[eyebrowPos][eyebrowPos+12] = "";
                face[eyebrowPos][eyebrowPos+13] = "";
                break;

            case "((((()))))":
                face[eyebrowPos][eyebrowPos-1] = "(((((";
                face[eyebrowPos][eyebrowPos+3] = ")))))";
                face[eyebrowPos][eyebrowPos+4] = "";face[eyebrowPos][eyebrowPos+5] = "";face[eyebrowPos][eyebrowPos+6] = "";
                face[eyebrowPos][eyebrowPos+7] = "";face[eyebrowPos][eyebrowPos+8] = "";face[eyebrowPos][eyebrowPos+9] = "";
                face[eyebrowPos][eyebrowPos+10] = "";face[eyebrowPos][eyebrowPos+11] = "";
                break;

            case "@@@@@":
                face[eyebrowPos][eyebrowPos-1] = "@@@@@";
                face[eyebrowPos][eyebrowPos+3] = "@@@@@";
                face[eyebrowPos][eyebrowPos+4] = "";face[eyebrowPos][eyebrowPos+5] = "";face[eyebrowPos][eyebrowPos+6] = "";
                face[eyebrowPos][eyebrowPos+7] = "";face[eyebrowPos][eyebrowPos+8] = "";face[eyebrowPos][eyebrowPos+9] = "";
                face[eyebrowPos][eyebrowPos+10] = "";face[eyebrowPos][eyebrowPos+11] = "";
                break;

        }
        // draw eyes
        String[] eyeOptions = {"000", "$$$", "%%%","333"};
        String eye = eyeOptions[random.nextInt(eyeOptions.length)];
        int eyePos = (sizeY / 2) - 2; // above bags

        switch (eye) {
            case "000":
                face[eyePos][eyePos-2] = "000";
                face[eyePos][eyePos+2] = "000";
                face[eyePos][eyePos+3] = "";face[eyePos][eyePos+4] = "";face[eyePos][eyePos+5] = "";//removed spaces occupied by eyes so structure stays intact
                face[eyePos][eyePos+6] = "";
                break;

            case "$$$":
                face[eyePos][eyePos-2] = "$$$";
                face[eyePos][eyePos+2] = "$$$";
                face[eyePos][eyePos+3] = "";face[eyePos][eyePos+4] = "";face[eyePos][eyePos+5] = "";
                face[eyePos][eyePos+6] = "";
                break;

            case "%%%":
                face[eyePos][eyePos-2] = "%%%";
                face[eyePos][eyePos+2] = "%%%";
                face[eyePos][eyePos+3] = "";face[eyePos][eyePos+4] = "";face[eyePos][eyePos+5] = "";
                face[eyePos][eyePos+6] = "";
                break;

            case "333":
                face[eyePos][eyePos-2] = "333";
                face[eyePos][eyePos+2] = "333";
                face[eyePos][eyePos+3] = "";face[eyePos][eyePos+4] = "";face[eyePos][eyePos+5] = "";
                face[eyePos][eyePos+6] = "";
                break;

        }

        // draw bags/blush
        String[] bagOptions = {"++", "--", ".."," "};
        String bags = bagOptions[random.nextInt(bagOptions.length)];
        int bagPos = (sizeY / 2) - 1; // above nose

        switch (bags) {
            case "++":
                face[bagPos][bagPos-4] = "++";
                face[bagPos][bagPos+4] = "++";
                face[bagPos][bagPos+5] = "";face[bagPos][bagPos+6] = "";//removed spaces occupied by bags/blush so structure stays intact
                break;

            case "--":
                face[bagPos][bagPos-4] = "--";
                face[bagPos][bagPos+4] = "--";
                face[bagPos][bagPos+5] = "";face[bagPos][bagPos+6] = "";
                break;

            case "..":
                face[bagPos][bagPos-4] = "..";
                face[bagPos][bagPos+4] = "..";
                face[bagPos][bagPos+5] = "";face[bagPos][bagPos+6] = "";
                break;
            case " ":
                face[bagPos][bagPos-4] = " ";
                face[bagPos][bagPos+4] = " ";
                break;


        }


        // draw nose
        String[] noseOptions = {"<", ">", ",,", "*","[]"};
        String nose = noseOptions[random.nextInt(noseOptions.length)];
        int nosePos = sizeY / 2; // middle of the face
        switch (nose) {
            case "<":
                face[nosePos][nosePos] = "<";
                break;
            case ">":
                face[nosePos][nosePos] = ">";
                break;
            case ",,":
                face[nosePos][nosePos] = ",,";
                face[nosePos][nosePos+2] = "";//removed spaces occupied by nose so structure stays intact
                break;
            case "*":
                face[nosePos][nosePos] = "*";
                break;
            case "[]":
                face[nosePos][nosePos] = "[]";
                face[nosePos][nosePos+2] = "";
                break;
        }

        // draw moustache
        String[] moustacheOptions = {"####"," ","^^^^"};
        String moustache = moustacheOptions[random.nextInt(moustacheOptions.length)];
        int moustachePos = (sizeY / 2 ) + 1; //right above the mouth
        switch (moustache) {
            case "####":
                face[moustachePos][moustachePos-2] = "####";
                face[moustachePos][moustachePos-1] = "";face[moustachePos][moustachePos] = "";face[moustachePos][moustachePos+1] = "";//removed spaces occupied by moustache so structure stays intact
                break;
            case " ":
                face[moustachePos][moustachePos-2] = " ";
                break;
            case "^^^^":
                face[moustachePos][moustachePos-2] = "^^^^";
                face[moustachePos][moustachePos-1] = "";face[moustachePos][moustachePos] = "";face[moustachePos][moustachePos+1] = "";
                break;

        }

        // draw mouth
        String[] mouthOptions = {"{EEEEEEEEEE}", "(!!!!!!!!!!)","(DDDDDDDDDD)","[==========]","|++++++++++|"};
        String mouth = mouthOptions[random.nextInt(mouthOptions.length)];
        int mouthpos = (sizeY / 2 ) + 2; //right under the nose
        switch (mouth) {
            case "{EEEEEEEEEE}":
                face[mouthpos][mouthpos-7] = "{EEEEEEEEEE}";
                face[mouthpos][mouthpos-6] = "";face[mouthpos][mouthpos-5] = "";face[mouthpos][mouthpos-4] = "";//removed spaces occupied by mouth so structure stays intact
                face[mouthpos][mouthpos-3] = "";face[mouthpos][mouthpos-2] = "";face[mouthpos][mouthpos-1] = "";
                face[mouthpos][mouthpos] = "";face[mouthpos][mouthpos+1] = "";face[mouthpos][mouthpos+2] = "";
                face[mouthpos][mouthpos+3] = "";face[mouthpos][mouthpos+4] = "";
                break;
            case "(!!!!!!!!!!)":
                face[mouthpos][mouthpos-7] = "(!!!!!!!!!!)";
                face[mouthpos][mouthpos-6] = "";face[mouthpos][mouthpos-5] = "";face[mouthpos][mouthpos-4] = "";
                face[mouthpos][mouthpos-3] = "";face[mouthpos][mouthpos-2] = "";face[mouthpos][mouthpos-1] = "";
                face[mouthpos][mouthpos] = "";face[mouthpos][mouthpos+1] = "";face[mouthpos][mouthpos+2] = "";
                face[mouthpos][mouthpos+3] = "";face[mouthpos][mouthpos+4] = "";
                break;
            case "(DDDDDDDDDD)":
                face[mouthpos][mouthpos-7] = "(DDDDDDDDDD)";
                face[mouthpos][mouthpos-6] = "";face[mouthpos][mouthpos-5] = "";face[mouthpos][mouthpos-4] = "";
                face[mouthpos][mouthpos-3] = "";face[mouthpos][mouthpos-2] = "";face[mouthpos][mouthpos-1] = "";
                face[mouthpos][mouthpos] = "";face[mouthpos][mouthpos+1] = "";face[mouthpos][mouthpos+2] = "";
                face[mouthpos][mouthpos+3] = "";face[mouthpos][mouthpos+4] = "";
                break;
            case "[==========]":
                face[mouthpos][mouthpos-7] = "[==========]";
                face[mouthpos][mouthpos-6] = "";face[mouthpos][mouthpos-5] = "";face[mouthpos][mouthpos-4] = "";
                face[mouthpos][mouthpos-3] = "";face[mouthpos][mouthpos-2] = "";face[mouthpos][mouthpos-1] = "";
                face[mouthpos][mouthpos] = "";face[mouthpos][mouthpos+1] = "";face[mouthpos][mouthpos+2] = "";
                face[mouthpos][mouthpos+3] = "";face[mouthpos][mouthpos+4] = "";
                break;
            case "|++++++++++|":
                face[mouthpos][mouthpos-7] = "|++++++++++|";
                face[mouthpos][mouthpos-6] = "";face[mouthpos][mouthpos-5] = "";face[mouthpos][mouthpos-4] = "";
                face[mouthpos][mouthpos-3] = "";face[mouthpos][mouthpos-2] = "";face[mouthpos][mouthpos-1] = "";
                face[mouthpos][mouthpos] = "";face[mouthpos][mouthpos+1] = "";face[mouthpos][mouthpos+2] = "";
                face[mouthpos][mouthpos+3] = "";face[mouthpos][mouthpos+4] = "";
                break;

        }

        // draw beard
        String[] beardOptions = {"VVV"," "};
        String beard = beardOptions[random.nextInt(beardOptions.length)];
        int beardpos = (sizeY / 2 ) + 4; //right under the mouth
        switch (beard) {
            case "VVV":
                face[beardpos][beardpos-5] = "VVV";
                face[beardpos][beardpos-4] = "";face[beardpos][beardpos-3] = "";
                break;
            case " ":
                face[beardpos][beardpos] = " ";
                break;

        }


        return face;
    }


}


