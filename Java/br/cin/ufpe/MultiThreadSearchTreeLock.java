package br.cin.ufpe;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MultiThreadSearchTreeLock implements Runnable{
	int n;
	MultiThreadSearchTreeLock r;
	MultiThreadSearchTreeLock l;
	Lock lock;
	
	static Random random = new Random();
	static MultiThreadSearchTreeLock btree = null;
	
	public MultiThreadSearchTreeLock() {
		this.n = -1;
		this.lock = new ReentrantLock();
	}
	
	public void insert(int n){
			if (this.l == null && n <= this.n) {
				boolean locked = this.lock.tryLock();
				try {
					while (!(locked)) {
						locked = this.lock.tryLock();
					}
					this.l = new MultiThreadSearchTreeLock();
					l.n = n;
				} finally {
					this.lock.unlock();
				}
				
			} else if (this.l != null && n <= this.n) {
				this.l.insert(n);
			}
			
		
			if (this.r == null && n > this.n) {
				boolean locked = this.lock.tryLock();
				try {
					while(!(locked)){
						locked = this.lock.tryLock();
					}
					this.r = new MultiThreadSearchTreeLock();
					r.n = n;
				} finally {
					this.lock.unlock();
				}
				
			}  else if (this.r != null && n > this.n) {
				this.r.insert(n);
			}
			
	}
	
	public void run() {
		
		for (int i = 0; i < 2000; i++) {
			int n = this.random.nextInt();
			
			if(btree == null) {
				btree = new MultiThreadSearchTreeLock();
			} 
			
			if(btree.n == -1){
				btree.n = n;
			} else {
				btree.insert(random.nextInt(10000));
			}
		}
	}
	
	public static void main(String[] args) {
		Thread[] threadList = new Thread[50];
		long t0 = System.currentTimeMillis();
		
		for(int i = 0; i < 50; i ++ ){
			threadList[i] = new Thread(new MultiThreadSearchTreeLock());
			threadList[i].start();
		}
		
		for(int i = 0; i < 50; i++) {
			try {
				threadList[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Time: " + (System.currentTimeMillis() - t0 + " milisseconds"));
	}
}

