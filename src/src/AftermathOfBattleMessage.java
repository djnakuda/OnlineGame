
public class AftermathOfBattleMessage extends Message {
	private static final long serialVersionUID = 1L;
	
	private int currMonster;
	
	public AftermathOfBattleMessage(String message,int currMonster) {
		this.message = message;
		this.setCurrMonster(currMonster);
	}

	public int getCurrMonster() {
		return currMonster;
	}

	public void setCurrMonster(int currMonster) {
		this.currMonster = currMonster;
	}

}
