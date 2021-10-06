package blockchain.core;

public class Transaction implements AutoCloseable {

    private String fromAddress;
    private String toAddress;
    private String signature;
    private String publicKey;
    private double amount;

    public Transaction(String fromAddress,
                       String toAddress,
                       String signature,
                       String publicKey, double amount) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.signature = signature;
        this.publicKey = publicKey;
        this.amount = amount;
    }

    public Transaction() {
    }

    public String getSignature() {
        return signature;
    }

    public Transaction setSignature(String signature) {
        this.signature = signature;
        return this;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public Transaction setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
        return this;
    }

    public String getToAddress() {
        return toAddress;
    }

    public Transaction setToAddress(String toAddress) {
        this.toAddress = toAddress;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public Transaction setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public String toString() {
        return String.format("%s sent %f VC to %s", fromAddress, amount, toAddress);
    }

    @Override
    public void close() throws Exception {

    }
}
