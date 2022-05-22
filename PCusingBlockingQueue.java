package producerconsumer;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class PCusingBlockingQueue {
    static BlockingQueue<Integer> q = new ArrayBlockingQueue<>(3);
    public static void main(String[] args) {
        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

    static class Producer implements Runnable{

        @Override
        public void run() {
            while (q.size() != 3){
                try {
                    q.put(createItem());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public int createItem(){
            int x = new Random().nextInt();
            System.out.println("Producing " + x);
            return x;
        }
    }

    static class Consumer implements Runnable{

        @Override
        public void run() {
            while (q.size() > 0){
                try {
                    Integer i = q.take();
                    process(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void process(Integer i) {
            System.out.println("Consuming " + i);
        }
    }
}