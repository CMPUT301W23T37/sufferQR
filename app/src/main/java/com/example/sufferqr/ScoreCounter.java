package com.example.sufferqr;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreCounter {

        private String hexString;

        public ScoreCounter(String qrCode) {
            // Convert the QR code to a hexadecimal string
            BigInteger qrBigInt = new BigInteger(qrCode, 16);
            this.hexString = qrBigInt.toString(16);
        }

        public int getScore() {
            int score = 0;
            // Use regex to find repeated digits and count them
            Pattern pattern = Pattern.compile("(.)\\1{2,}");
            Matcher matcher = pattern.matcher(hexString);
            while (matcher.find()) {
                int count = matcher.group().length();
                score += (count - 1) * 10; // Add 10 points for each repeated digit after the first
            }
            return score;
        }

}
