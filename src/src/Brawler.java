import java.io.Serializable;
import java.util.ArrayList;

public class Brawler implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private String type;
	private Stats stats;
	private ArrayList<Ability> abilities;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Stats getStats() {
		return stats;
	}
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	public ArrayList<Ability> getAbilities() {
		return abilities;
	}
	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}
	
	public void printBrawler() {
		System.out.println(this.name);
		System.out.println(this.type);
		this.stats.printStats();
		for(int i = 0; i < abilities.size();i++)
		{
			System.out.println("Ability " + i);
			abilities.get(i).printAbility();
		}
		
	}
	
}
