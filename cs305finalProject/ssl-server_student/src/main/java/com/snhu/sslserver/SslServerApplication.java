package com.snhu.sslserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@RestController
public class SslServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SslServerApplication.class, args);
    }

    @GetMapping("/hash")
    public String calculateChecksum() {
        try {
            // Original data
            String data = "Hello Mr. DiMarzio from my secure world!" + "\n" +
                    "This is Tammy Hartline's test for the checksum verification." + "\n";

            // Encrypt data using AES
            byte[] encryptedBytes = encryptAES(data);

            // Calculate MD5 checksum on encrypted data
            String checksum = calculateMD5Checksum(encryptedBytes);

            return "Original Data: " + data + "\n"
                    + "Encrypted Data: (Hexadecimal format) " + bytesToHexString(encryptedBytes) + "\n"
                    + "Checksum: " + checksum;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error calculating checksum";
        }
    }

    // Encrypt data using AES encryption algorithm
    private byte[] encryptAES(String data) throws Exception {
        // Generate a secret key using AES algorithm
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecretKey secretKey = keyGen.generateKey();

        // Create a cipher instance for AES encryption
        Cipher cipher = Cipher.getInstance("AES");

        // Initialize the cipher with the secret key for encryption
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encrypt the data
        return cipher.doFinal(data.getBytes());
    }

    // Calculate MD5 checksum for a given byte array
    private String calculateMD5Checksum(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashBytes = md.digest(data);
        StringBuilder checksum = new StringBuilder();
        for (byte b : hashBytes) {
            checksum.append(String.format("%02x", b & 0xff));
        }
        return checksum.toString();
    }

    // Convert a byte array to a hexadecimal string
    private String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b & 0xff));
        }
        return hexString.toString();
    }
}
