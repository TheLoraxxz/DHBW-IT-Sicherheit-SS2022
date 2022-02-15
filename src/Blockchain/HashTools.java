package Blockchain;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class HashTools {
    public static String SHA256(String input) {
        try {
            MessageDigest diges = MessageDigest.getInstance("SHA-256");
            byte[] hash =diges.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff&b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }
    public static byte[] signTransaction(PrivateKey key,String data) {
        Signature sign;
        byte[] output;

        try {
            sign = Signature.getInstance("ECDSA","BC");
            sign.initSign(key);
            byte[] dataToStr =data.getBytes(StandardCharsets.UTF_8);
            sign.update(dataToStr);
            output = sign.sign();
        }catch (Exception e) {
            throw new RuntimeException();
        }
        return output;
    }
    public static String StringToKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static boolean verifiyTransaction(PublicKey key,String data, byte[] signature) {
        try {
            Signature toVerify = Signature.getInstance("ECDSA","BC");
            toVerify.initVerify(key);
            toVerify.update(data.getBytes(StandardCharsets.UTF_8));
            return toVerify.verify(signature);
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
