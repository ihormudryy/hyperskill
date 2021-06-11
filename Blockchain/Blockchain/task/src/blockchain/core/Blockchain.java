package blockchain.core;

import blockchain.utils.CryptoSecurity;
import blockchain.utils.CryptoUtils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public class Blockchain implements AutoCloseable {

    private static Blockchain blockchain;
    private final String zeroAddress = "NONE";
    private final int[] targetNewBlockTimeInterval = {1, 100}; // Generate new block between 1 and 2 seconds
    private final LinkedList<Block> chainOfBlocks = new LinkedList();
    private final Map<String, Double> balances = new HashMap<>(); // All non-zero wallet balances
    private long blockIndexNumber = 0; // Latest block index
    private int difficultyTarget = 0; // Amount of zeros is hash prefix
    private String hashIndex;
    private List<Transaction> transactionLinkedList = new LinkedList<>(); // Transactions list
    private Map<String, ShadowBalance> shadowBalances = new HashMap<>(); // Balances that will be deducted or added

    // when the next block is mined and added to the chain
    private double rewardForBlock = 100.0d;

    private Blockchain() throws NoSuchAlgorithmException {
        Block genesisBlock = new Block().setId(1L)
                                        .setPreviousBlockHash("0")
                                        .setNonce(0)
                                        .setMinerPublicKey(zeroAddress)
                                        .setMinerId(zeroAddress)
                                        .setTimeStamp(new Date().getTime())
                                        .setMinersReward(Math.round(rewardForBlock))
                                        .calculateHash();
        if (!addNextBlock(genesisBlock)) {
            throw new IllegalStateException("Blockchain was not initialized correctly and is in failed state!");
        }
    }

    public static Blockchain getInstance() throws NoSuchAlgorithmException {
        if (blockchain == null) {
            blockchain = new Blockchain();
        }
        return blockchain;
    }

    public double getRewardForBlock() {
        return rewardForBlock;
    }

    public double getBalance(String address) {
        return balances.get(address) != null ? balances.get(address) : 0.0d;
    }

    private boolean validateBlocksSignature(Block block) throws UnsupportedEncodingException,
        IllegalBlockSizeException,
        BadPaddingException,
        InvalidKeySpecException,
        InvalidKeyException {
        return CryptoSecurity.decryptText(block.getEncryptedBlockHash(), block.getMinerPublicKey())
                             .equals(block.getCurrentBlockHash());
    }

    public List<Transaction> getTransactionLinkedList() {
        return transactionLinkedList;
    }

    public String getHashIndex() {
        return hashIndex;
    }

    public int getDifficultyTarget() {
        return difficultyTarget;
    }

    public long getBlockIndexNumber() {
        return blockIndexNumber;
    }

    private void synchronizeBalancesSheet(Block block) {
        block.getTransactionsList().stream()
             .forEach(tx -> {
                 String from = tx.getFromAddress();
                 String to = tx.getToAddress();
                 balances.put(from, shadowBalances.get(from) != null ? shadowBalances.get(from).getBalance() : 0.0d);
                 balances.put(to, shadowBalances.get(to) != null ? shadowBalances.get(to).getBalance() : 0.0d);
             });
    }

    public synchronized void submitTransactionRequest(Transaction tx) throws
        UnsupportedEncodingException,
        IllegalBlockSizeException,
        BadPaddingException,
        InvalidKeySpecException,
        InvalidKeyException {

        String from = tx.getFromAddress();
        String to = tx.getToAddress();
        double checkSum = Double.valueOf(CryptoSecurity.decryptText(tx.getSignature(), tx.getPublicKey()));

        if (checkSum != tx.getAmount()) {
            throw new IllegalArgumentException("Transaction was rejected due to incorrect signature.");
        }

        if (!balances.containsKey(from)) {
            throw new IllegalArgumentException("Transaction was rejected due to insufficient amount the balance.");
        }

        if (balances.containsKey(from) &&
            !shadowBalances.containsKey(from)) {
            ShadowBalance sb = (new ShadowBalance(from)).setAmount(balances.get(from));
            shadowBalances.put(from, sb);
        }

        if (shadowBalances.get(from).getDeduction() - tx.getAmount() >= 0.0d) {
            if (!from.equals(zeroAddress)) {
                shadowBalances.get(from).deductFromBalance(tx.getAmount());
            }
            if (!shadowBalances.containsKey(to)) {
                shadowBalances.put(to,
                                   new ShadowBalance(to).setAmount(balances.containsKey(to) ? balances.get(to) : 0.0d));
            }
            shadowBalances.get(to).accumulateToBalance(tx.getAmount());
            transactionLinkedList.add(tx);
        }
    }

    public synchronized boolean addNextBlock(Block block) throws NoSuchAlgorithmException {

        if (isNewBlockValid(block)) {
            String minersAddress = block.getMinerId();
            if (block.getMiningTime() > targetNewBlockTimeInterval[1]) {
                difficultyTarget = difficultyTarget - 1 < 0 ? 0 : difficultyTarget--;
                rewardForBlock /= 2.0d;
                block.setDifficultyTarget("N was decreased by 1");
            } else if (block.getMiningTime() < targetNewBlockTimeInterval[0]) {
                difficultyTarget++;
                rewardForBlock *= 2.0d;
                block.setDifficultyTarget("N was increased to " + difficultyTarget);
            } else {
                block.setDifficultyTarget("N stays the same");
            }
            chainOfBlocks.add(block);
            this.hashIndex = block.getCurrentBlockHash();
            synchronizeBalancesSheet(block);
            transactionLinkedList = new LinkedList<>();

            transactionLinkedList.add(new Transaction().setFromAddress("[BLOCK_CREATION_REWARD]")
                                                       .setSignature("[BLOCK_CREATION_REWARD]")
                                                       .setAmount(rewardForBlock)
                                                       .setToAddress(minersAddress)
                                                       .setPublicKey(block.getMinerPublicKey()));
            shadowBalances = new HashMap<>();
            if (balances.get(minersAddress) == null) {
                balances.put(minersAddress, 0.0d);
            }
            shadowBalances.put(minersAddress, new ShadowBalance(minersAddress)
                                                .setAmount(balances.get(minersAddress) + rewardForBlock));
            blockIndexNumber++;
            return true;
        }
        return false;
    }

    public boolean isNewBlockValid(Block block) {
        String hash = CryptoUtils.generateBlockHash(block);
        try {
            if (block.getId() > 1) {
                if (!validateBlocksSignature(block)) {
                    throw new IllegalStateException("Block signature is wrong. Block is rejected.");
                }
            }
        } catch (UnsupportedEncodingException
            | IllegalBlockSizeException
            | BadPaddingException
            | InvalidKeySpecException
            | InvalidKeyException e) {
            //e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            return false;
        }

        return block.getId() == blockIndexNumber + 1 &&
            hash.equals(block.getCurrentBlockHash()) &&
            (difficultyTarget == 0 || hash.substring(0, difficultyTarget).matches("[0]+"));
    }

    public boolean validateBlockchain() {
        Block index = chainOfBlocks.getFirst();
        for (int i = 1; i < chainOfBlocks.size(); i++) {
            String hashOriginal = CryptoUtils.generateBlockHash(index);
            if (!hashOriginal.equals(chainOfBlocks.get(i).getPreviousBlockHash())) {
                System.out.println("Blockchain is invalid or corrupted.");
                return false;
            }
            index = chainOfBlocks.get(i);
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chainOfBlocks.size(); i++) {
            Block index = chainOfBlocks.get(i);
            result.append(index.toString());
        }
        return result.toString();
    }

    @Override
    public void close() throws Exception {
    }

}
