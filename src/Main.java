
public class Main{
	public static void main(String[] args){
		
		// Santa
		SantaClaus santa = new SantaClaus();
		santa.start();
		
		// Elfen
		for(int i = 1; i <= 8; i++){
			Elf elf = new Elf(santa);
			elf.start();
		}
		
		// Rentiere
		for(int i = 1; i <= 40; i++){
			Rentier rentier = new Rentier(santa);
			rentier.start();
		}
		
	}
}