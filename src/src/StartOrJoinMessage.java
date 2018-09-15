
public class StartOrJoinMessage extends Message {
	private static final long serialVersionUID = 1L;

	StartOrJoinMessage() {
		this.message = "Please make a choice:\r\n" + "1) Start Game\r\n" + "2) Join Game";
	}

}
