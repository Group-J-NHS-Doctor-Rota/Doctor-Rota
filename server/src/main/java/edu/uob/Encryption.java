package edu.uob;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

public class Encryption {
    private final String pwd;
    private static final Random RANDOM = new SecureRandom();
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    public Encryption(String password) {
        pwd = password;
    }

//    private byte[] getMD5(String s) throws NoSuchAlgorithmException,
//            UnsupportedEncodingException {
//        byte[] bytesOfMessage = s.getBytes("UTF-8");
//        //    Uses MD5 Crypt as the hash algorithm.
//        MessageDigest md = MessageDigest.getInstance("MD5");
//        byte[] theMD5digest = md.digest(bytesOfMessage);
//        return theMD5digest;
//    }

//    https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    private byte[] getSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
        byte[] pwdHash = hash(password, salt);
        Arrays.fill(password, Character.MIN_VALUE);
        if (pwdHash.length != expectedHash.length) return false;
        for (int i = 0; i < pwdHash.length; i++) {
            if (pwdHash[i] != expectedHash[i]) return false;
        }
        return true;
    }

//    public byte[] getFinalPwd() throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        // todo pepper should be unique for each user??
//        String pepper = "";
//        String s = pwd + getSalt();
//        byte[] finalPwd = getMD5(s);
//        getMD5(pepper);
//        return finalPwd;
//    }
}
