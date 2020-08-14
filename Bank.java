import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import models.Account;
import models.Transaction;

public class Bank {
	private Account[] listOfAccounts;
	// https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/BlockingQueue.html
	private BlockingQueue<Transaction> queue;
    private final Transaction nullTrans = new Transaction(-1,0,0);
    private final int NUMBER_OF_ACCOUNTS = 20;

	// Worker class to do work
    private class Worker extends Thread {
        public void run() {
            try {
                Transaction t;
                do {
                    t = queue.take();
                    if(t.getFromAccount() != -1) {
                        processTransaction(t);
                        System.out.println(this.getName() + " processed" + t);
                    }
                } while (t.getFromAccount() != -1);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            System.out.println(this.getName()  + " is exiting...");
        }
    }
	
	public Bank() {
		listOfAccounts = new Account[NUMBER_OF_ACCOUNTS];
        for(int i = 0;i < NUMBER_OF_ACCOUNTS;i++) {
        	listOfAccounts[i] = new Account(i); //initialize 20 accounts with beginning balance of $1000
        }
        // https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/LinkedBlockingQueue.html
        queue = new LinkedBlockingQueue();
    }
	 //method to process transaction
    public synchronized void processTransaction(Transaction t) {
        Account accFrom = listOfAccounts[t.getFromAccount()];
        Account accTo = listOfAccounts[t.getToAccount()];
        double money = t.getAmount();

        accFrom.setCurrentBalance(1.0 * accFrom.getCurrentBalance() - money);
        accFrom.setNumberOfTransaction(accFrom.getNumberOfTransaction() + 1);
        accTo.setCurrentBalance(1.0 * accTo.getCurrentBalance() + money);
        accTo.setNumberOfTransaction(accTo.getNumberOfTransaction() + 1);
    }
	public static void main(String[] args) {
		Bank bank = new Bank();
		ArrayList<Worker> listOfWorkers = new ArrayList<>();
		
		int numberOfThreadInput = Integer.parseInt(args[1]);
		
		for(int i = 0; i < numberOfThreadInput; i++)
        {
			listOfWorkers.add(bank.new Worker()); //adding workers depend on input
        }
		try {
            Iterator<Worker> itr = listOfWorkers.iterator();
            while (itr.hasNext()){
                itr.next().start(); //start each worker
            }

            bank.loadFile(args[0]); //load queue with transactions from text files

            for(int i = 0; i < numberOfThreadInput; i++)
            {
                bank.queue.put(bank.nullTrans); //putting null transactions depend on number of workers
            }

            Iterator<Worker> itrNew = listOfWorkers.iterator();
            while (itrNew.hasNext()){
                itrNew.next().join(); // workers wait for each other to exit at the same time
            }
        }catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
		System.out.println("\n----- All threads done -----");
        System.out.println("----- Accounts Summary -----");

        for(int i = 0 ; i < bank.getListOfAccount().length; i++){
            System.out.println(bank.getListOfAccount()[i].toString());
        }
		
	}
	
	public Account[] getListOfAccount() {
        return listOfAccounts;
    }
	// method to load text file of transactions on queue
    public void loadFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            
            String line = br.readLine();
            while (line != null) {
                String[] strOfNumbers = line.split("\\s");
                parseAccount(strOfNumbers);
                line = br.readLine();
            }
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
    private void parseAccount(String[] line) {
    	try {
    		int accountFrom = Integer.parseInt(line[0]);
            int accountTo = Integer.parseInt(line[1]);
            double amountOfMoney = Double.parseDouble(line[2]);
            Transaction transaction = new Transaction(accountFrom, accountTo, amountOfMoney);
            queue.put(transaction);	
    	} catch (InterruptedException e) {
            e.printStackTrace();
        }
    	 
    }
}
