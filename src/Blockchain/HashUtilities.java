package Blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtilities {
    public static String SHA256(String input) {
        try {
            MessageDigest message = MessageDigest.getInstance("SHA-256");
            byte[] hash = message.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
