package blockchain.core;

import blockchain.Main;
import blockchain.utils.CryptoUtils;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Block implements AutoCloseable {

    private long id;
    private String minerId;
    private String difficultyChange;
    private long timeStamp;
    private long miningTime; // In milliseconds
    private long nonce;
    private String previousBlockHash;
    private String currentBlockHash;
    private List<Transaction> transactions = new LinkedList<>();
    private String minerPublicKey;
    private String encryptedBlockHash;
    private double minersReward = 0.0d;

    public Block() {
    }

    public double getMinersReward() {
        return minersReward;
    }

    public Block setMinersReward(double minersReward) {
        this.minersReward = minersReward;
        return this;
    }

    public List<Transaction> getTransactionsList() {
        return transactions;
    }

    public String getTransactionAsString() {
        return transactions.stream()
                           .map(Transaction::toString)
                           .collect(Collectors.joining("\n"));
    }

    public Block setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public String getMinerPublicKey() {
        return minerPublicKey;
    }

    public Block setMinerPublicKey(String minerPublicKey) {

        this.minerPublicKey = minerPublicKey;
        return this;
    }

    public String getEncryptedBlockHash() {
        return encryptedBlockHash;
    }

    public Block setEncryptedBlockHash(String encryptedBlockHash) {

        this.encryptedBlockHash = encryptedBlockHash;
        return this;
    }

    public String getMinerId() {
        return this.minerId;
    }

    public Block setMinerId(String minerId) {
        this.minerId = minerId;
        return this;
    }

    public synchronized Block setDifficultyTarget(String difficultyChange) {
        this.difficultyChange = difficultyChange;
        return this;
    }

    public long getId() {
        return id;
    }

    public Block setId(long id) {
        this.id = id;
        return this;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public synchronized Block setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public synchronized Block setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
        return this;
    }

    public String getCurrentBlockHash() {
        return currentBlockHash;
    }

    public long getMiningTime() {
        return miningTime;
    }

    public synchronized Block setMiningTime(long miningTime) {
        this.miningTime = miningTime;
        return this;
    }

    public long getNonce() {
        return nonce;
    }

    public synchronized Block setNonce(long nonce) {
        this.nonce = nonce;
        return this;
    }

    public Block calculateHash() {
        currentBlockHash = CryptoUtils.generateBlockHash(this);
        return this;
    }

    @Override
    public String toString() {
        return String.format("Block:\n" +
                                 "Created by miner # %s\n" +
                                 "%s gets %d VC\n" +
                                 //"Miners public key: %s \n" +
                                 // "Encrypted block hash: %s \n" +
                                 "Id: %s\n" +
                                 "Timestamp: %s\n" +
                                 "Magic number: %s\n" +
                                 "Hash of the previous block:\n%s\n" +
                                 "Hash of the block:\n%s\n" +
                                 "Block data:\n%s\n" +
                                 "Block was generating for %s seconds\n" +
                                 "%s\n\n",
                             minerId,
                             minerId, 100,
                             // minerPublicKey == null ? "<empty>" : minerPublicKey,
                             // encryptedBlockHash == null ? "<empty>" : minerPublicKey,
                             id,
                             timeStamp,
                             nonce,
                             previousBlockHash,
                             currentBlockHash,
                             getTransactionAsString().length() == 0 ? "No transactions" : getTransactionAsString(),
                             miningTime / 1000,
                             difficultyChange);
    }

    @Override
    public void close() throws Exception {
    }
}
