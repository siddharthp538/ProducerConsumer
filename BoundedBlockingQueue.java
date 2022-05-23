package producerconsumer;

import java.util.LinkedList;
import java.util.Queue;

class BoundedBlockingQueue {
    private Queue<Integer> q;
    int capacity;
    public BoundedBlockingQueue(int capacity) {
        q = new LinkedList<>();
        this.capacity = capacity;
    }

    public void enqueue(int element) throws InterruptedException {
        synchronized(q){
            while(q.size() == this.capacity){
                q.wait();
            }
            q.add(element);
            q.notifyAll();
        }
    }

    public int dequeue() throws InterruptedException {
        synchronized(q){
            while(q.size() == 0){
                q.wait();
            }
            int x = q.remove();
            q.notifyAll();
            return x;
        }
    }

    public int size() {
        synchronized(q){
            return q.size();
        }
    }
}