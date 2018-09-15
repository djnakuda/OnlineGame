
public class GameStartMessage extends Message {
	private static final long serialVersionUID = 1L;
	String yourBrawlersName;
	String opponentsBrawlersName;
	
	public GameStartMessage(String yourBrawlersName, String opponnentsBrawlersName) {
		this.yourBrawlersName = yourBrawlersName;
		this.opponentsBrawlersName = opponnentsBrawlersName;
	}
	
	public String getYourBrawlersName() {
		return yourBrawlersName;
	}

	public void setYourBrawlersName(String yourBrawlersName) {
		this.yourBrawlersName = yourBrawlersName;
	}

	public String getOpponentsBrawlersName() {
		return opponentsBrawlersName;
	}

	public void setOpponentsBrawlersName(String opponentsBrawlersName) {
		this.opponentsBrawlersName = opponentsBrawlersName;
	}
}
