package blockchain.utils;

import blockchain.core.Block;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CryptoUtils {

    public static final String hashAlgorythm = "SHA-256";
    public static final String encoding = "UTF-8";
    public static Random random = new Random();

    public static String formatMessages(List<String> messages) {
        return messages.stream()
                       .collect(Collectors.joining("\n"));
    }

    public static String generateBlockHash(Block block) {
        return CryptoUtils.applySha256(String.valueOf(block.getId())
                                             .concat(String.valueOf(block.getTimeStamp()))
                                             .concat(block.getPreviousBlockHash())
                                             .concat(String.valueOf(block.getNonce()))
                                             .concat(block.getTransactionAsString())
                                             .concat(Double.toString(block.getMinersReward())));
    }

    public static String generateRandomString(int length) {
        return random.ints('A', 'z')
                     .limit(30)
                     .collect(StringBuilder::new,
                              StringBuilder::appendCodePoint,
                              StringBuilder::append)
                     .toString();
    }

    /* Applies Sha256 to a string and returns a hash. */
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorythm);
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes(encoding));
            StringBuilder hexString = new StringBuilder();
            for (byte elem : hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMd5Hash(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(text.getBytes(StandardCharsets.UTF_8));
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}