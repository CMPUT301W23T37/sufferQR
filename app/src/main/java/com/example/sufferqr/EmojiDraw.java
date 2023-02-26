package com.example.sufferqr;


/**
 * This class will take in a QR code as an  in the form of a dictionary and return a visual representation of the
 * QR code.
 * @version 1.0
 */
public class EmojiDraw {
    /**
     * We will first have a template of a cirle for the emoji
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

    public static void main(String[] args) {
        drawCircleTemplate();
    }



}
