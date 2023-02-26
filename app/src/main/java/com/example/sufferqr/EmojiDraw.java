package com.example.sufferqr;

/**
 * This is a class will take a qrcode as input in the form of a dictionary and create an emoji
 * composed of text with it
 */
public class EmojiDraw {
    /**
     * This method draws are template which consists of a Circle composed of asterisk
     */
    public static void drawCircleTemplate() {
        int radius = 5; // Set the radius of the circle
        int centerX = 5; // Set the X coordinate of the center of the circle
        int centerY = 5; // Set the Y coordinate of the center of the circle

        // Loop through each row and column to draw the circle
        for (int y = 0; y <= 10; y++) {
            for (int x = 0; x <= 10; x++) {
                double distance = Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
                if (Math.abs(distance - radius) < 0.5) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("");
        }
    }


}
