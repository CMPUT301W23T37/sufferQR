package com.example.sufferqr;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//Title: SHA-256 Hash in Java,Website: GeeksforGeeks,Link: https://www.geeksforgeeks.org/sha-256-hash-in-java,Author: Ankit Modi,Date Published: July 7, 2020
//this code borrowed elements from the above sited website author and post

// Java program to calculate SHA hash value

class  QRHash {
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into sig_num representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }


    // Driver code
    public static void main(String args[])
    {
        try
        {
            System.out.println("HashCode Generated by SHA-256 for:");

            String s1 = "301timeman";
            System.out.println("\n" + s1 + " : " + toHexString(getSHA(s1)));

            String s2 = "sufferqr";
            System.out.println("\n" + s2 + " : " + toHexString(getSHA(s2)));

            String s3 = "BFG5DGW54";
            System.out.println("\n" + s3 + " : " + toHexString(getSHA(s3)));
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown for incorrect algorithm: " + e);
        }
    }

}
