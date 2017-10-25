package br.cin.ufpe;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadSearchTreeSyncEvenMoreOptmized implements Runnable{
	int n;
	MultiThreadSearchTreeSyncEvenMoreOptmized r;
	MultiThreadSearchTreeSyncEvenMoreOptmized l;
	boolean locked;
	Lock lock;
	Condition condition;
	
	static int numberOfNodes = 0;
	static Random random = new Random();
	static MultiThreadSearchTreeSyncEvenMoreOptmized btree = null;
	
	public MultiThreadSearchTreeSyncEvenMoreOptmized() {
		this.n = -1;
		this.locked = false;
		this.lock = new ReentrantLock();
		this.condition = lock.newCondition();
	}

	public void put(int n, MultiThreadSearchTreeSyncEvenMoreOptmized node, boolean left) {
		node.lock.lock();
		while (node.locked) {
			try {
				node.condition.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		node.locked = true;
		if(left) {
			node.l = new MultiThreadSearchTreeSyncEvenMoreOptmized();
			node.l.n = n;
		} else {
			node.r = new MultiThreadSearchTreeSyncEvenMoreOptmized();
			node.r.n = n;
		}
		node.locked = false;
		node.condition.signalAll();
	}
	
	public void insert(int n){
		if (this.l == null && n <= this.n) {
			put(n, this, true);
		} else if (this.r == null && n > this.n) {
			put(n, this, false);
		} else if (this.l != null && n <= this.n) {
			this.l.insert(n);
		} else if (this.r != null && n > this.n) {
			this.r.insert(n);
		}
	}
	
	public void run() {
		
		
		for (int i = 0; i < 2000; i++) {
			int n = this.random.nextInt();
			
			if(btree == null) {
				btree = new MultiThreadSearchTreeSyncEvenMoreOptmized();
			} 
			
			if (btree.n == -1) {
				btree.n = n;
			} else  {
				this.btree.insert(random.nextInt(100));
			}
		}
	}
	
	public static void count(MultiThreadSearchTreeSyncEvenMoreOptmized node) {
		if (node.l != null) {
			count(node.l);
		} 
		if (node.r != null) {
			count(node.r);
		}
		 numberOfNodes += 1;
	}
	
	public static void main(String[] args) {
		Thread[] threadList = new Thread[50];
		long t0 = System.currentTimeMillis();
		
		for(int i = 0; i < 50; i ++ ){
			threadList[i] = new Thread(new MultiThreadSearchTreeSyncEvenMoreOptmized());
			threadList[i].start();
		}
		
		for(int i = 0; i < 50; i ++ ){
			try {
				threadList[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Time: " + (System.currentTimeMillis() - t0) + " milisseconds");
		count(btree);
		System.out.println("Foram inseridos " + numberOfNodes + " elementos");
	}
}

