package blockchain.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CryptoSecurity {

    private static final int keyLength = 4096;
    private static final String encoding = "UTF-8";
    private static final String algorithm = "RSA";
    private static Cipher cipher;
    private static KeyPairGenerator keyGen;
    private static KeyFactory keyFactory;

    static {
        try {
            keyGen = KeyPairGenerator.getInstance(algorithm);
            keyGen.initialize(keyLength);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        try {
            keyFactory = KeyFactory.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private final KeyPair pair;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public CryptoSecurity() {
        this.pair = keyGen.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public static String encryptText(String msg, String private_key)
        throws UnsupportedEncodingException, IllegalBlockSizeException,
        BadPaddingException, InvalidKeyException, InvalidKeySpecException {

        if (msg == null || private_key == null) {
            throw new NullPointerException(msg == null ? "Message to encrypt is null" : "Private key is null");
        }

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(private_key));
        cipher.init(Cipher.ENCRYPT_MODE, keyFactory.generatePrivate(keySpec));
        return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes(encoding)));
    }

    public static void validatePublicKey(String key) throws InvalidKeySpecException, InvalidKeyException {
        byte[] encodedKey = Base64.getDecoder().decode(key.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(keySpec));
    }

    public static String decryptText(String msg, String public_key)
        throws InvalidKeyException, UnsupportedEncodingException,
        IllegalBlockSizeException, BadPaddingException,
        InvalidKeySpecException {

        if (msg == null || public_key == null) {
            throw new NullPointerException(msg == null ? "Message to decrypt is null" : "Private key is null");
        }
        String key =
            String.format("-----BEGIN CERTIFICATE-----\\n%s\\n-----END CERTIFICATE-----", public_key);
        byte[] encodedKey = Base64.getDecoder().decode(public_key.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
        cipher.init(Cipher.DECRYPT_MODE, keyFactory.generatePublic(keySpec));
        return new String(cipher.doFinal(Base64.getDecoder().decode(msg)), encoding);
    }

    public byte[] getPublicKey() throws InvalidKeySpecException {
        return keyFactory.getKeySpec(publicKey,
                                     X509EncodedKeySpec.class).getEncoded();
    }

    public String encryptText(String msg)
        throws UnsupportedEncodingException, IllegalBlockSizeException,
        BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(msg.getBytes(encoding)));
    }

    public String fetchPublicKey (String x509Cert) throws CertificateException {
        byte[] certder = Base64.getDecoder().decode(x509Cert.trim().getBytes());
        InputStream certstream = new ByteArrayInputStream(certder);
        Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(certstream);
        return Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded());
    }

    public void writeToFile(String path, byte[] key) throws IOException {
        File f = new File(path);
        f.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }
}
