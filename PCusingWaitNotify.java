package producerconsumer;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PCusingWaitNotify {

    static BlockingQueue<Integer> bq = new BlockingQueue<>(3);
    public static void main(String[] args) {
        new Thread(new Producer(bq)).start();
        new Thread(new Consumer(bq)).start();
    }

    static class Producer implements Runnable{
        BlockingQueue<Integer> bq;
        Producer(BlockingQueue<Integer> bq){
            this.bq = bq;
        }
        @Override
        public void run() {
            while(bq.q.size() < bq.maxSize){
                bq.put(createItem());
            }
        }

        static int createItem(){
            int x = new Random().nextInt();
            System.out.println("Producing " + x);
            return x;
        }
    }

    static class Consumer implements Runnable{
        BlockingQueue<Integer> bq;
        Consumer(BlockingQueue<Integer> bq){
            this.bq = bq;
        }
        @Override
        public void run() {
            while (bq.q.size() > 0){
                int i = bq.take();
                process(i);
            }
        }

        public static void process(int x){
            System.out.println("Consuming " + x);
        }
    }

}


class BlockingQueue<E>{
    Queue<E> q;
    int maxSize = 16;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition notEmpty = lock.newCondition();
    private final Condition notFull = lock.newCondition();
    BlockingQueue(int size){
        q = new LinkedList<>();
        this.maxSize = size;
    }


    public void put(E e){
        lock.lock();
        try {
            if (q.size() == this.maxSize) {
                notFull.await();
            }
            q.add(e);
            notEmpty.signalAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public E take(){
        lock.lock();
        E e = null;
        try {
            if (q.size() == 0)
                notEmpty.await();
            e = q.remove();
            notFull.signalAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return e;
    }
}
