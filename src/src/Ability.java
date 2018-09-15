import java.io.Serializable;

public class Ability implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private int damage;
	private String type;

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
	
	public void printAbility() {
		System.out.println("Ability Name: " + name);
		System.out.println("Strength: " + damage);
		System.out.println("Type: "+ type);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
