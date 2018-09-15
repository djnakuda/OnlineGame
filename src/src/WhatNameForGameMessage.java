
public class WhatNameForGameMessage extends Message {
	private static final long serialVersionUID = 1L;
	
	public WhatNameForGameMessage(int id) {
		if(id == 1) this.message = "What will you name your game?";
		else this.message = "What is the name of the game?";
	}

}
