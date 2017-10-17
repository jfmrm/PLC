import java.util.Random;


public class ArvoreBusca implements Runnable{
	int n;
	ArvoreBusca r;
	ArvoreBusca l;

	static Random random = new Random();
	static ArvoreBusca btree = null;
	
	public ArvoreBusca() {
		this.n = -1;
	}
	
	public void insert(int n){
		if (this.l == null && n <= this.n) {
			this.l = new ArvoreBusca();
			l.n = n;
		} else if (this.r == null && n > this.n) {
			this.r = new ArvoreBusca();
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
		
		int n = this.random.nextInt();
		
		for (int i = 0; i < 2000; i++) {
			if(btree == null) {
				btree = new ArvoreBusca();
			} 
			
			if (this.n == -1) {
				btree.n = n;
			} else  {
				this.btree.insert(random.nextInt(100));
			}
		}
	}
	
	public static void main(String[] args) {
		for(int i = 0; i < 50; i ++ ){
			new Thread(new ArvoreBusca()).start();
		}
	}
}

