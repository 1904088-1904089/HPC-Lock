import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class OperationsQueue {
    private final List<Integer> operations = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    public void addSimulation(int totalSimulation) {
        // Add 50 random numbers to the operations list. The numbers will range from -100 to 100. They cannot be zero.
        for (int i = 0; i < totalSimulation; i++) {
            lock.lock();
            try {
                int random = (int) (Math.random() * 200) - 100;
                if (random != 0) {
                    operations.add(random);
                }
                System.out.println(i + ". New operation added: " + random);
                // add a small delay to simulate the time taken for a new customer to arrive
                try {
                    Thread.sleep((int) (Math.random() * 80));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                lock.unlock();
            }
        }
        operations.add(-9999);
    }

    public void add(int amount) {
        lock.lock();
        try {
            operations.add(amount);
        } finally {
            lock.unlock();
        }
    }

    public synchronized int getNextItem() {
        // add a small delay to simulate the time taken to get the next operation.
        while (operations.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        lock.lock();
        try {
            return operations.remove(0);
        } finally {
            lock.unlock();
        }
    }
}