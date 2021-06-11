package blockchain;

import blockchain.client.Miner;
import blockchain.core.Blockchain;
import blockchain.utils.CryptoSecurity;
import blockchain.utils.CryptoUtils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        CryptoSecurity security = new CryptoSecurity();
        String cert = "MIIC3DCCAcSgAwIBAgIQUpEN" +
            "/yLzFolLkGc1AzSoUTANBgkqhkiG9w0BAQsFADApMScwJQYDVQQDEx5BREZTIFNpZ25pbmcgLSBzc28uaXQuaGVyZS5jb20wIBcNMTUwNDMwMTg1NzEyWhgPMjExNTA0MDYxODU3MTJaMCkxJzAlBgNVBAMTHkFERlMgU2lnbmluZyAtIHNzby5pdC5oZXJlLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMkhl0niWxmccqSVVvpfTq04SdceotgNt7ruyuRjGNIjp0e2P0F70XL7XG7RJ9qZuuNOxHlkjlq8mFGtafW09eJLWbROsbuLYwYufsqY9fR5UC/kqW7ysUmjXV+/zZ6YAdwzffYWwS/0bwXIV4P/hViuhDfTtMPUtEzk4ienCGJS4AWZIrg+qWMSVyZ1ZMbzENGYQve8TCU6j3PMxLJSwtmhc9ECyQyrmZt+H1vIMvTMRZtaWeIvnR9br4wZeQSF+Ysv/110sQ+5A4wEM518z0oHWNVERdN3tXkNiVo7ak60zHvw3CWYHbTPyLQLrcOsF96bQXIvFmoFkyecWHDUznUCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAjwcsyQWgLHYiqMReZwK/V//eQaXeE1bkWqoytCbRdnLCOU9ynfCQTPB/CPTcmUCaxRS9kUsuz30iASoWYZlZuEq2CYtENGgs7AHr/zazaChzBzJnOTeVpsTqetEYuajpYkSliyhdsYPB0GhvdW6betF2XtDFqcGtsmxLuU30m9vp2okNEHmMq1zeG01q18onhfW3P4/J2NOm9nrSVnpUq1PUExy5zTAXVhoXQKV0LcYhcZl6WI7/itgHI+DRUlq3pTY8JzIEuSMz09jV5kQqlKFbcz8qvk9XPFkzyxsW/8xEVCqZs/9aeVd/bsdSkeLlDEVRvlHT9KQjYQ3hVqPYEg==";
        // System.out.println(security.fetchPublicKey(scan.nextLine()));
        String message = "BP76Obz2KN0U+2Xc8OBCT/XrLzHZJb8D0GQIRp+FIFzilbct6FE07BSuLFzLnjHkQdGwYVZXOZpPnmEoNI04" +
            "+KtfmTeJkNgY70JYAJwx8noacrdUAOajrzN5Jqcz0E5wus1q9Od7E/Q4/cR9TTcJO8jWqKMN8QFmzUyvkodr6eDPX831QC4x+T41kzSJsD/KuHjYaugnjLObALPvakroRjhVXyc8l9U+Kh2gOr0rlhNOzJFaHqcZ4Y9I8qxP+BDoYdIt5r72UGrGzAZA0jtB/gUrooJWQmTT5yPuJsaHxT9WGpy4GrhNnAy3abuZZWDMtLe+pHNU6fbMxwxZT0dinA==";
        System.out.println(security.fetchPublicKey(cert));
        // System.out.println("-------------------------------------------");
        // byte[] s1 = security.decryptText(message, security.fetchPublicKey(cert)).getBytes();
        // String signature = new String(Base64.getEncoder().encode(s1));
        // System.out.println(signature);
        //
        // System.out.println("-------------------------------------------");
        // byte[] s2 = security.decryptText(signature, security.fetchPublicKey(cert)).getBytes();
        // System.out.println(new String(Base64.getEncoder().encode(s2)));
    }
/*
    public static void main(String[] args) throws Exception {
        // Run on all CPU cores in parallel
        int poolSize = 2;//Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        try (Blockchain blockchain = Blockchain.getInstance()) {

            List<Miner> minersPool = IntStream.range(0, poolSize)
                                              .mapToObj(miner -> {
                                                  try {
                                                      return new Miner(blockchain);
                                                  } catch (NoSuchPaddingException e) {
                                                      e.printStackTrace();
                                                  } catch (NoSuchAlgorithmException e) {
                                                      e.printStackTrace();
                                                  } catch (InvalidKeySpecException e) {
                                                      e.printStackTrace();
                                                  }
                                                  return null;
                                              }).collect(Collectors.toList());

            List<Future<?>> futures = minersPool.stream()
                                                .map(miner -> miner.start(executor))
                                                .collect(Collectors.toList());

            while (blockchain.getBlockIndexNumber() < 15) {
                minersPool.stream()
                          .filter(miner -> blockchain.getBalance(miner.getAddress()) > 0.0d)
                          .forEach(miner -> {
                              minersPool.stream().forEach(receiver -> {
                                  try {
                                      if (blockchain.getTransactionLinkedList().size() < 10) {
                                          double balance = blockchain.getBalance(miner.getAddress());
                                          miner.transferMoneyTo(receiver.getAddress(), ThreadLocalRandom.current()
                                                                                        .nextDouble(balance * 0.01,
                                                                                                                    balance * 0.1));
                                      }
                                  } catch (UnsupportedEncodingException e) {
                                      e.printStackTrace();
                                  } catch (IllegalBlockSizeException e) {
                                      e.printStackTrace();
                                  } catch (InvalidKeySpecException e) {
                                      e.printStackTrace();
                                  } catch (BadPaddingException e) {
                                      e.printStackTrace();
                                  } catch (InvalidKeyException e) {
                                      e.printStackTrace();
                                  } catch (IllegalStateException e) {
                                      e.printStackTrace();
                                  }
                              });
                          });
                Thread.sleep(1);
            }
            minersPool.stream().forEach(miner -> miner.stop());
            blockchain.validateBlockchain();
            System.out.println(blockchain);
        } catch (IllegalStateException e) {
            System.out.println("Blockchain wasn't initialized. Exit.");
            e.printStackTrace();
        }
        executor.shutdown();
    }

 */
}
