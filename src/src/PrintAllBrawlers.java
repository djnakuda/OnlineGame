
public class PrintAllBrawlers extends Message {
	private static final long serialVersionUID = 1L;
	
	private Brawlers currentRoster;
	
	public PrintAllBrawlers(Brawlers brawlers) {
		this.currentRoster = brawlers;
	}

	public Brawlers getCurrentRoster() {
		return currentRoster;
	}

	public void setCurrentRoster(Brawlers currentRoster) {
		this.currentRoster = currentRoster;
	}

}
