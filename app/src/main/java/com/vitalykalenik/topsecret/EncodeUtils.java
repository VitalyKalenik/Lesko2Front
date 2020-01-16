package com.vitalykalenik.topsecret;

import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Vitaly Kalenik
 */
public class EncodeUtils {

    public static String encodeAES(String text, String secretKey) {
        String encryptedString;
        SecretKeySpec skeySpec;
        byte[] encryptText = text.getBytes();
        Cipher cipher;
        try {
            skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encryptedString = new String(cipher.doFinal(encryptText));
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return text;
    }

    public static String decodeAES(String text, String secretKey) {
        Cipher cipher;
        String decryptedString;
        byte[] encryptText = null;
        SecretKeySpec skeySpec;
        try {
            skeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            encryptText = text.getBytes();
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            decryptedString = new String(cipher.doFinal(encryptText));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return text;
    }

    public static String encodeSha256(String originalString, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            originalString = addSalt(originalString, salt);
            byte[] encodedHash = digest.digest(
                    originalString.getBytes(StandardCharsets.UTF_8));
            originalString = bytesToHex(encodedHash);
            Log.d("myLogs", originalString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return originalString;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static String addSalt(String originalString, String salt) {
        return originalString.concat(salt);
    }
}
