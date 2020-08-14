package models;

public class Transaction {
	private int fromAccount;
	private int toAccount;
	private double amount;
	
	public Transaction(int from, int to, double amount) {
		this.fromAccount = from;
		this.toAccount = to;
		this.amount = amount;
	}
	
	// Getters
	public int getFromAccount() {
        return fromAccount;
    }

    public double getAmount() {
        return amount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public String toString() {
        return " Transaction: from = " + fromAccount + ", to = " + toAccount + " amount = " + amount;
    }
}
