import java.io.Serializable;

public class Message implements Serializable{
	public static final long serialVersionUID = 1L;
	
	protected String message;
	Message(){	
	}
	
	Message(String message)
	{
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}


