package producerConsumer;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer {

	public static void main(String[] args) {
		Thread[] producers = new Thread[5];
		Thread[] consumers = new Thread[5];
		
		Lock lock = new ReentrantLock();
		LinkedList<Integer> buffer = new LinkedList<Integer>();
		int max = 5;
		
		for(int i = 0; i < 5; i++) {
			producers[i] = new Thread(new Producer(buffer, max, lock));
			producers[i].start();
			consumers[i] = new Thread(new Consumer(buffer, lock));
			consumers[i].start();
		}
		
		for(int i = 0; i < 5; i ++) {
			try {
				producers[i].join();
				consumers[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("finished");
	}

}
