package br.cin.ufpe.bTreeSerch;
import java.util.Random;


public class MultiThreadSearchTreeSync implements Runnable{
	int n;
	MultiThreadSearchTreeSync r;
	MultiThreadSearchTreeSync l;

	static Random random = new Random();
	static MultiThreadSearchTreeSync btree = null;
	static int numberOfNodes = 0;
	
	public MultiThreadSearchTreeSync() {
		this.n = -1;
	}
	
	public void insert(int n){
		if (this.l == null && n <= this.n) {
			this.l = new MultiThreadSearchTreeSync();
			l.n = n;
		} else if (this.r == null && n > this.n) {
			this.r = new MultiThreadSearchTreeSync();
			r.n = n;
		} else if (this.l != null && n <= this.n) {
			synchronized (this.l){
				this.l.insert(n);
			}
		} else if (this.r != null && n > this.n) {
			synchronized (this.r){
				this.r.insert(n);
			}
		}
	}
	
	public void run() {
		
		
		for (int i = 0; i < 2000; i++) {
			int n = this.random.nextInt();
			
			if(btree == null) {
				btree = new MultiThreadSearchTreeSync();
			} 
			
			if (btree.n == -1) {
				btree.n = n;
			} else  {
				this.btree.insert(random.nextInt(100));
			}
		}
	}
	
	public static void count(MultiThreadSearchTreeSync node) {
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
			threadList[i] = new Thread(new MultiThreadSearchTreeSync());
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

