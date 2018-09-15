import java.io.Serializable;
import java.util.ArrayList;

public class Brawlers implements Serializable {
	private static final long serialVersionUID = 1L;
	ArrayList<Brawler> Brawlers;
	
	public Brawlers() {
		this.Brawlers = new ArrayList<Brawler>();
	}

	public ArrayList<Brawler> getBrawlers() {
		return Brawlers;
	}

	public void setBrawlers(ArrayList<Brawler> brawlers) {
		this.Brawlers = brawlers;
	}
	
	public void printAllBrawlers() {
		for(int i = 0; i < Brawlers.size();i++)
		{
			System.out.println("Brawler " + i);
			Brawlers.get(i).printBrawler();
		}
	}

}
