import java.util.concurrent.locks.ReentrantLock;

public class Bank {
    String accountNumber;
    OperationsQueue operationsQueue;
    int balance = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public Bank(String accountNumber, OperationsQueue operationsQueue) {
        this.accountNumber = accountNumber;
        this.operationsQueue = operationsQueue;
    }

    // A deposit function that will run in parallel on a separate thread. It will be a loop where in each iteration, it reads the amount from the operationQueue and deposits the amount.
    public void deposit() {
        while (true) {
            lock.lock();
            try {
                int amount = operationsQueue.getNextItem();
                if (amount == -9999) {
                    operationsQueue.add(-9999);

                    break;
                }
                if (amount > 0) {
                    balance = balance + amount;
                    System.out.println("Deposited: " + amount + " Balance: " + balance);
                } else {
                    operationsQueue.add(amount);
                    System.out.println("Operation added back: " + amount);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    // A withdraw function that will run in parallel on a separate thread. It will be a loop where in each iteration, it reads the amount from the operationQueue and withdraws the amount.
    public void withdraw() {
        while (true) {
            lock.lock();
            try {
                int amount = operationsQueue.getNextItem();
                if (amount == -9999) {
                    operationsQueue.add(-9999);
                    break;
                }

                if (balance + amount < 0) {
                    System.out.println("Not enough balance to withdraw: " + amount);
                    // continue;
                    break;
                }

                if (amount < 0) {
                    balance = balance + amount;
                    System.out.println("Withdrawn: " + amount + " Balance: " + balance);
                } else {
                    operationsQueue.add(amount);
                    System.out.println("Operation added back: " + amount);
                }
            } finally {
                lock.unlock();
            }
        }
    }
}