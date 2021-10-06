package blockchain.core;

import blockchain.utils.CryptoSecurity;
import blockchain.utils.CryptoUtils;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

public class CryptoWallet extends CryptoSecurity {

    public String address;
    public String publicKey;
    private List<Transaction> transactions;

    public CryptoWallet() throws NoSuchAlgorithmException, InvalidKeySpecException {
        super();
        this.publicKey = Base64.getEncoder().encodeToString(super.getPublicKey());
        this.address = CryptoUtils.getMd5Hash(publicKey);
        this.transactions = new LinkedList<>();
    }

    public String getAddress() {
        return address;
    }

    public void transferTo(Blockchain blockchain, String toAddress, double amount) {
        if (blockchain.getBalance(address) - amount >= 0) {
            try (Transaction transaction = new Transaction(address,
                                                           toAddress,
                                                           encryptText(String.valueOf(amount)),
                                                           publicKey,
                                                           amount)) {
                blockchain.submitTransactionRequest(transaction);
                transactions.add(transaction);
            } catch (IllegalStateException e) {
                System.out.println("Failed attempt to transfer crypto currency.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Not enough founds on your balance.");
        }
    }
}
