import java.io.Serializable;

public class Stats implements Serializable {
	private static final long serialVersionUID = 1L;
	private int health;
	private int attack;
	private int defense;
	private int speed;
	
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void printStats() {
		System.out.println("health: " + health);
		System.out.println("attack: " + attack);
		System.out.println("defense: " + defense);
		System.out.println("speed: " + speed);
	}

}
