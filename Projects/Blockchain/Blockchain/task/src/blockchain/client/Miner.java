package blockchain.client;

import blockchain.core.Block;
import blockchain.core.Blockchain;
import blockchain.core.CryptoWallet;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Miner extends CryptoWallet {

    private Blockchain blockchain;
    private Future<?> executor;
    private boolean exit = false;

    public Miner(Blockchain ledger) throws NoSuchPaddingException,
        NoSuchAlgorithmException,
        InvalidKeySpecException {
        super();
        this.blockchain = ledger;
    }

    public void transferMoneyTo(String to, double amount) throws UnsupportedEncodingException,
        IllegalBlockSizeException,
        InvalidKeySpecException,
        BadPaddingException,
        InvalidKeyException {

        transferTo(blockchain, to, amount);
    }

    public boolean addBlockToBlockchain(Block block) throws NoSuchAlgorithmException {
        return blockchain.addNextBlock(block);
    }

    public Block generateNewBlock() throws IllegalBlockSizeException,
        BadPaddingException,
        InvalidKeyException,
        UnsupportedEncodingException {
        int difficulty = blockchain.getDifficultyTarget();
        long indexId = blockchain.getBlockIndexNumber() + 1;
        Block block = new Block();
        initializeBlock(block);
        Date start = new Date();
        while (!block.getCurrentBlockHash()
                     .substring(0, difficulty)
                     .matches("[0]+")) {
            mineNewBlocksHash(block);
            if (blockchain.getBlockIndexNumber() == indexId) {
                start = new Date();
                indexId = blockchain.getBlockIndexNumber() + 1;
                difficulty = blockchain.getDifficultyTarget();
                initializeBlock(block);
            }
        }
        Date end = new Date();
        block.setMiningTime(end.getTime() - start.getTime());
        block.setEncryptedBlockHash(encryptText(block.getCurrentBlockHash()));
        block.setMinerPublicKey(publicKey);
        return block;
    }

    private void initializeBlock(Block block) {
        block.setId(blockchain.getBlockIndexNumber() + 1)
            .setPreviousBlockHash(blockchain.getHashIndex())
            .setNonce(0L)
            .setMinerId(getAddress())
            .setTimeStamp(new Date().getTime())
            .setTransactions(blockchain.getTransactionLinkedList())
            .setMinersReward(blockchain.getRewardForBlock())
            .calculateHash();
    }

    private String mineNewBlocksHash(Block block) {
        return block.setNonce(block.getNonce() + 1)
                    .calculateHash()
                    .getCurrentBlockHash();
    }

    public Object stop() {
        this.exit = true;
        return this;
    }

    public Future<?> start(ExecutorService executor) {
        this.executor = executor.submit(() -> {
            // try {
            //     System.out.println("Running " + getPublicKey());
            // } catch (InvalidKeySpecException e) {
            //     e.printStackTrace();
            // } catch (InvalidKeyException e) {
            //     e.printStackTrace();
            // }
            while (exit != true) {
                try (Block block = generateNewBlock()) {
                    // if (blockchain.getTransactionLinkedList().size() > 13) {
                        addBlockToBlockchain(block);
                        //System.out.println("Miner " + getAddress() + " has mined a new block!");
                    // }
                } catch (Exception e) {
                    //e.printStackTrace();
                    //System.out.println("Miner " + address + " was stopped");
                }
            }
        });
        return this.executor;
    }

    public String toString() {
        return String.format("Public key: %s\n" +
            "Wallet address: %s", publicKey, address);
    }
}
