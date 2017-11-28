package producerConsumer;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class Producer implements Runnable {
	LinkedList<Integer> buffer;
	int max;
	Lock lock;
	static Random rand = new Random();
	
	public Producer(LinkedList<Integer> buffer, int max, Lock lock) {
		this.buffer = buffer;
		this.max = max;
		this.lock = lock;
	}
	
	public void produce() {
		lock.lock();
		if(buffer.size() < max) {
			this.buffer.addFirst(rand.nextInt(100));
			System.out.println("Producing on thread " + Thread.currentThread().getId());
			lock.unlock();
		} else {
			lock.unlock();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public void run() {
		for(int i = 0; i < 10; i++) {
			this.produce();
		}
	}
	
}
