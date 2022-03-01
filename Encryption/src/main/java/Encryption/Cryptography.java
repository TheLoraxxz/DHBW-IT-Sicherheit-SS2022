package Encryption;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cryptography {

    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int AES_KEY_BIT = 256;

    private SecretKey secretKey;
    private byte[] nonce;

    private boolean encrypted;

    public Cryptography() {
        encrypted = false;
        try {
            this.secretKey = CryptoHelper.getAESKey(AES_KEY_BIT);
            this.nonce = CryptoHelper.getRandomNonce(IV_LENGTH_BYTE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void encrypt(String folderPath) {
        if (encrypted) return;
        encrypted = true;
        File dir = new File(folderPath);
        File[] fileArr = null;
        try {
            fileArr = dir.listFiles();
        } catch (NullPointerException e) {
        }
        if (fileArr == null) {
            System.out.println("Failed to list Files");
            return;
        }
        List<File> files = new ArrayList<>(Arrays.asList(fileArr));

        files.forEach(f -> {
            try {
                FileInputStream fin = new FileInputStream(f);
                byte[] content = fin.readAllBytes();
                fin.close();

                Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, nonce));
                byte[] encContent = cipher.doFinal(content);

                File encFile = Path.of(f.getAbsolutePath() + ".mcg").toFile();
                encFile.createNewFile();

                FileOutputStream fout = new FileOutputStream(encFile);
                fout.write(encContent);
                fout.close();

                f.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public void decrypt(String folderPath) {
        if (!encrypted) return;
        File dir = new File(folderPath);
        File[] fileArr = null;
        List<File> files = new ArrayList<>();
        try {
            fileArr = dir.listFiles();
        } catch (NullPointerException e) {
        }
        if (fileArr == null) {
            System.out.println("Failed to list Files");
            return;
        }
        for (File f : fileArr) {
            if (f.getName().endsWith(".mcg")) files.add(f);
        }

        files.forEach(f -> {
            try {
                FileInputStream fin = new FileInputStream(f);
                byte[] content = fin.readAllBytes();
                fin.close();

                Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, nonce));
                byte[] decContent = cipher.doFinal(content);

                File decFile = Path.of(f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf('.'))).toFile();
                decFile.createNewFile();

                FileOutputStream fout = new FileOutputStream(decFile);
                fout.write(decContent);
                fout.close();

                f.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

}
