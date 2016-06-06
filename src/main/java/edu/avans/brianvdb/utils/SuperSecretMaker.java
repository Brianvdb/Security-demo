package edu.avans.brianvdb.utils;

import edu.avans.brianvdb.model.Secret;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.KeySpec;

/**
 * Created by Brian on 6-6-2016.
 */
public class SuperSecretMaker {

    public static Secret encrypt(String message, String password) {
        Security.addProvider(new BouncyCastleProvider());
        try {
            // maak een nieuw unieke salt aan
            byte[] salt = new byte[8];
            SecureRandom rnd = new SecureRandom();
            rnd.nextBytes(salt);

            // maak een nieuwe secret key aan, gebaseerd op een random salt en het password
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // versleutel uiteindelijk de message met de aangemaakte sleutel
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] ciphertext = cipher.doFinal(message.getBytes("UTF-8"));

            // sla de versleutelde data op in een model
            Secret secretObj = new Secret();
            secretObj.setSalt(Base64.toBase64String(salt));
            secretObj.setSecretMessageCiphered(Base64.toBase64String(ciphertext));
            secretObj.setSecretMessageIv(Base64.toBase64String(iv));

            return secretObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(Secret secretObj, String password) {
        try {
            // verkrijg de sleutel waarmee de message gencrypt is, op basis van het wachtwoord en de salt
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.decode(secretObj.getSalt()), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            // ontsteutel het geheime bericht
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(Base64.decode(secretObj.getSecretMessageIv())));
            String plaintext = new String(cipher.doFinal(Base64.decode(secretObj.getSecretMessageCiphered())), "UTF-8");

            return plaintext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
