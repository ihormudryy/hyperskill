package blockchain.core;

public class ShadowBalance {

    private String address;
    private Double balance = 0.0d;
    private Double deduction = 0.0d;
    private Double profit = 0.0d;

    public ShadowBalance(String address) {
        this.address = address;
    }

    public Double getDeduction() {
        return deduction;
    }

    public ShadowBalance setAmount(double amount) {
        this.balance = amount;
        this.deduction = amount;
        this.profit = amount;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public ShadowBalance deductFromBalance(double n) {
        if (this.deduction - n < 0.0d) {
            throw new IllegalStateException("Deduction rejected. Not enough founds on the wallet.");
        }
        this.balance -= n;
        this.deduction -= n;
        return this;
    }

    public ShadowBalance accumulateToBalance(double n) {
        this.balance += n;
        this.profit += n;
        return this;
    }
}
