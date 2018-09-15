public class AttackMessage extends Message{
	public static final long serialVersionUID = 1L;
	
	int attackUsed;
	int playerNumber;
	
	AttackMessage(int attackUsed,int playerNumber) {
		this.attackUsed = attackUsed;
		this.playerNumber = playerNumber;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}

	public int getAttackUsed() {
		return attackUsed;
	}

	public void setAttackUsed(int attackUsed) {
		this.attackUsed = attackUsed;
	}

	
	
}
