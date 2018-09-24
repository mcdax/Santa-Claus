import java.util.Random;

public class Rentier extends Thread {

	private SantaClaus santa;
	private Random rnd;
	public boolean warten;

	public Rentier(SantaClaus santa) {
		this.santa = santa;
		rnd = new Random();
		warten = false;
	}

	@Override
	public void run() {
		while (true) {
			try {
				int pause = rnd.nextInt(25001) + 5000; // 5 - 30s 
				sleep(pause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("Rentier " + getId() + " betritt den Stall");
			// Wenn ein Rentier den Stall betritt, wartet es auf seinen Einsatz
			// --> in diesem Thread wird "wait()" aufgerufen
			santa.stallBetreten(this);
			warten = true;
			synchronized(this){
				while(warten){
					try{
						wait();
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
			System.out.println("Rentier " + getId() + " fliegt nach seinem langen Arbeitstag in den Urlaub");
			urlaub();
		}

	}

	public void urlaub() {
		try {
			int pause = rnd.nextInt(30001) + 8000; // 8 - 38s
			sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}