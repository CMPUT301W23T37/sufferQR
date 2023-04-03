package com.example.sufferqr;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Java program to calculate SHA hash value
//The following article was referenced heavily in the creation of this hashing class
//Link: https://www.geeksforgeeks.org/sha-256-hash-in-java/
//Title: SHA-256 Hash in Java
//Author: Ayushya Gupta

/**
 * class provides hash value of a string in the form of hexidecimal values
 */
class  QRhash {
    /**
     *turns the string input into a hash value in the form of a byte array
     *@param input the input string
     *@return the hash value as a byte array
     *@throws NoSuchAlgorithmException if the specified algorithm is not available
     */
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
    /**
     *
     *Converts a byte array into a hexadecimal string.
     *@param hash the byte array to convert
     *@return the hexadecimal representation of the byte array
     */
    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
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


}