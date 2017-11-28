package producerConsumer;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;

public class Consumer implements Runnable {
	LinkedList<Integer> buffer;
	Lock lock;
	
	public Consumer(LinkedList<Integer> buffer, Lock lock) {
		this.buffer = buffer;
		this.lock = lock;
	}
	
	public void consume() {
		this.lock.lock();
		if(this.buffer.size() > 0) {
			buffer.pollLast();
			System.out.println("Consuming on thread " + Thread.currentThread().getId());
			this.lock.unlock();
		} else {
			this.lock.unlock();
			System.out.println("waiting to consume");
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
			this.consume();
		}
	}
}
