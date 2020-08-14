package models;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
	private int idNumber;
	private double currentBalance;
	private int numberOfTransaction;
	// https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Lock.html
	// https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/ReentrantLock.html
	private Lock accountLock = new ReentrantLock();
	
	public Account(int idNumber){
		this.idNumber = idNumber;
		this.currentBalance = 1000;
		this.numberOfTransaction = 0;
	}
	
	// Getters
	public double getCurrentBalance() {
        return currentBalance;
    }


    public int getNumberOfTransaction() {
        return numberOfTransaction;
    }
    // Setters
    public void setCurrentBalance(double currentBalance) {
        accountLock.lock();
        this.currentBalance = currentBalance;
        accountLock.unlock();
    }

    public void setNumberOfTransaction(int numberOfTransaction) {
        accountLock.lock();
        this.numberOfTransaction = numberOfTransaction;
        accountLock.unlock();

    }

    @Override
    public String toString() {
        return "acctount: " + idNumber + " balance: " + currentBalance + " number of transactions: " + numberOfTransaction ;
    }

}
