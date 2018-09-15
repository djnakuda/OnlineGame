
public class SendBackPlayersBrawlersMessage extends Message {
	private static final long serialVersionUID = 1L;	
	Brawlers thisPlayersBrawlers;
	int playerNumber;
	
	public SendBackPlayersBrawlersMessage(Brawlers thisPlayersBrawlers, int playerNumber) {
		this.thisPlayersBrawlers = thisPlayersBrawlers;
		this.playerNumber = playerNumber;
	}
	
	public Brawlers getThisPlayersBrawlers() {
		return thisPlayersBrawlers;
	}
	public void setThisPlayersBrawlers(Brawlers thisPlayersBrawlers) {
		this.thisPlayersBrawlers = thisPlayersBrawlers;
	}
	
	public int getPlayerNumber() {
		return playerNumber;
	}

	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	

}
