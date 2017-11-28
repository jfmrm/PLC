package br.cin.ufpe.bTreeSerch;
import java.util.Random;

public class MultiThreadSearchTreeSyncOptmized implements Runnable{
	int n;
	MultiThreadSearchTreeSyncOptmized r;
	MultiThreadSearchTreeSyncOptmized l;
	boolean locked;
	
	static int numberOfNodes = 0;
	static Random random = new Random();
	static MultiThreadSearchTreeSyncOptmized btree = null;
	
	public MultiThreadSearchTreeSyncOptmized() {
		this.n = -1;
		this.locked = false;
	}

	public synchronized void put(int n, MultiThreadSearchTreeSyncOptmized node) {
		while (node.locked) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		node.locked = true;
		node.n = n;
		node.locked = false;
		notifyAll();
	}
	
	public void insert(int n){
		if (this.l == null && n <= this.n) {
			this.l = new MultiThreadSearchTreeSyncOptmized();
			put(n, this.l);
		} else if (this.r == null && n > this.n) {
			this.r = new MultiThreadSearchTreeSyncOptmized();
			put(n, this.r);
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
				btree = new MultiThreadSearchTreeSyncOptmized();
			} 
			
			if (btree.n == -1) {
				btree.n = n;
			} else  {
				this.btree.insert(random.nextInt(100));
			}
		}
	}
	
	public static void count(MultiThreadSearchTreeSyncOptmized node) {
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
			threadList[i] = new Thread(new MultiThreadSearchTreeSyncOptmized());
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

