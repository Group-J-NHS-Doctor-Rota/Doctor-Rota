package edu.uob;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    String pwd;
    public Encryption(String password) {
        pwd = password;
    }

    private byte[] getMD5(String s) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        byte[] bytesOfMessage = s.getBytes("UTF-8");
        //    Uses MD5 Crypt as the hash algorithm.
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] theMD5digest = md.digest(bytesOfMessage);
        return theMD5digest;
    }

    private String getSalt() {
        //todo salt should be unique for each user
        String salt = "";
        return salt;
    }

    public byte[] getFinalPwd() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // todo pepper should be unique for each user??
        String pepper = "";
        String s = pwd + getSalt();
        byte[] finalPwd = getMD5(s);
        getMD5(pepper);
        return finalPwd;
    }
}
