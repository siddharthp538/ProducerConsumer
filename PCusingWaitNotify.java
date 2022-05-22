package producerconsumer;


import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PCusingWaitNotify {
    static BlockingQueue<Integer> bq = new BlockingQueue<>(3);
    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    static class Producer implements Runnable{

        @Override
        public void run() {
            while(bq.q.size() != 3){
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
    private ReentrantLock lock = new ReentrantLock(true);
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();
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
