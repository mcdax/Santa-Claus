import java.util.Random;

public class Elf extends Thread {

	private SantaClaus santa;
	private Random rnd;
	
	public boolean warten;

	public Elf(SantaClaus santa) {
		this.santa = santa;
		rnd = new Random();
		warten = false;
	}

	@Override
	public void run(){
		while (true) {
			try {
				int pause = rnd.nextInt(20001) + 1000; // 1 - 21 s
				sleep(pause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Elf " + getId() + " braucht Hilfe");
			// Wenn ein Elf Hilfe benoetigt, fragt er diese bei Santa an
			// --> in diesem Thread wird "wait()" aufgerufen
			santa.anfragen(this);
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
		}
	}
}