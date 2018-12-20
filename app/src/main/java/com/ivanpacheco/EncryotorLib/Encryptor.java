package com.ivanpacheco.EncryotorLib;

import android.util.Base64;

import com.ivanpacheco.LoggerLib.Logger;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by ivanpacheco on 14/02/18.
 *
 */

public class Encryptor implements Serializable {

    private  static final String iv = "aTrd7Sdfasqwedg3";
    private byte[] encodedKey;
    private final Logger LOG;

    public Encryptor(String password){
        LOG = new Logger(Encryptor.class.getName());
        try {
            encodedKey = generateKey(password).getEncoded();
        } catch (NoSuchAlgorithmException |UnsupportedEncodingException e) {
            LOG.e("Error crating key: "+e.getMessage(), e);
        }
    }

    public String encrypt(String str){
        try {
            byte[] btiv = iv.getBytes(StandardCharsets.UTF_8);
            byte[] btStr = str.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");


            IvParameterSpec ivParameterSpec = new IvParameterSpec(btiv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            return Base64.encodeToString(cipher.doFinal(btStr), Base64.DEFAULT);

        } catch (Exception ex) {
            LOG.e("Error encrypting: "+str+", Error: "+ex.getMessage(), ex);
        }
        return str;
    }

    public String decrypt(String str){
        try {
            byte[] btiv = iv.getBytes(StandardCharsets.UTF_8);
            byte[] btStr = Base64.decode(str, Base64.DEFAULT);
            SecretKeySpec keySpec = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(btiv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] original = cipher.doFinal(btStr);

            return new String(original);
        } catch (Exception ex) {
            LOG.e("Error decrypting: "+str+", Error: "+ex.getMessage());
        }
        return str;
    }

    private static SecretKeySpec generateKey(final String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();

        return new SecretKeySpec(key, "AES");
    }
}
