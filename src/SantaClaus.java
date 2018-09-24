import java.util.Random;
import java.util.Vector;

public class SantaClaus extends Thread {
	
	private static final int ELFEN_ANZAHL = 3;
	private static final int RENTIER_ANZAHL = 9;
	
	private Random rnd;
	// Vector because Thread safe!
	private Vector<Elf> elfenWarteliste; // Liste von Elfen, die Santas Hilfe benötigen
	private Vector<Rentier> rentierWarteliste; // Liste von Rentieren, die bereit sind um mit Santa Geschenke auszuliefern

	public SantaClaus() {
		rnd = new Random();
		elfenWarteliste = new Vector<Elf>();
		rentierWarteliste = new Vector<Rentier>();
	}

	@Override
	public void run() {
		while (true) {
			synchronized (this) {
				while (elfenWarteliste.size() < ELFEN_ANZAHL && rentierWarteliste.size() < RENTIER_ANZAHL) {
					try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if (rentierWarteliste.size() >= RENTIER_ANZAHL) { // Das Geschenkeverteilen hat die hoehere Prioritaet
				System.out.println("Santa ist aufgewacht um die Geschenke zu verteilen");
				geschenkeVerteilen();
				System.out.println("Santa ist vom Geschenkeverteilen zurückgekehrt und geht wieder schlafen");
			} else if (elfenWarteliste.size() >= ELFEN_ANZAHL) {
				System.out.println("Santa ist aufgewacht um den Elfen zu helfen");
				beraten();
				System.out.println("Santa hat allen Elfen geholfen und geht jetzt wieder schlafen");
			}

		}
	}

	public void anfragen(Elf elf) {
		this.elfenWarteliste.add(elf); // already synchronized (Vector)
		synchronized (this) {
			this.notify();
		}
	}

	public void stallBetreten(Rentier rentier) {
		this.rentierWarteliste.add(rentier); // already synchronized (Vector)
		synchronized (this) {
			this.notify();
		}
	}

	private void beraten() {
		for (int i = 0; i < elfenWarteliste.size();) { // die Groesse der Liste verringert sich um 1 mit jeder Iteration, deshalb kein "i++"
			Elf elf = elfenWarteliste.get(0);
			synchronized (elf) {
				try {
					sleep(1000); // Santa beraet einen Elfen eine Zeit lang
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Elf " + elf.getId() + " wurde beraten");
				elf.warten = false; // Der Elf-Thread kann fortgefuehrt werden
				elf.notify();
			}
			elfenWarteliste.remove(0); // Der Elf wurde beraten. Er wird aus der Liste entfernt
		}
	}

	private void geschenkeVerteilen() {
		// Verteile Geschenke mit den Rentieren
		try {
			int pause = rnd.nextInt(8001) + 8000; // 8 - 16 s
			sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Fuehre die Rentier-Threads fort (Entlasse die Rentiere in ihren wohlverdienten Urlaub)
		for (int i = 0; i < RENTIER_ANZAHL; i++) {
			Rentier rentier = rentierWarteliste.get(0);
			synchronized (rentier) {
				rentier.warten = false;
				rentier.notify();
			}
			rentierWarteliste.remove(0);
		}
	}

}