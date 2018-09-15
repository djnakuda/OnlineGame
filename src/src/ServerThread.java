import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class ServerThread extends Thread {
	
	private Socket s;
	private Server server;
	private int threadId;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Game game;
		
	public ServerThread(Socket s, Server server, int i)
	{
		this.s = s;
		this.server = server;
		threadId = i;
		
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			this.start();
		}
		catch(IOException ioe)
		{
			System.out.println("ioe in ServerThread constructor "+ ioe.getMessage());
			server.removeServerThread(this);
		}
	}
	
	public void sendMessage(Message message)
	{
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
	}
	
	public Message readMessage()
	{
		try {
			return (Message)ois.readObject();
		} 
		catch (ClassNotFoundException | IOException e) 
		{
			server.removeServerThread(this);
		}
		return null;
	}
	
	public void run() {
		Message m = null;
		sendMessage(new StartOrJoinMessage());
		String joinOrstart = "";
		String nameOfGame = "";
		String howManyPlayers = "";
		this.game = null;
		try 
		{	
			while(true)
			{
				m = readMessage();
				
				if(m instanceof SendBackPlayersBrawlersMessage)
				{
					if(!this.game.isOnePlayer())
					{
						int playerNumber = ((SendBackPlayersBrawlersMessage) m).getPlayerNumber();
						if(playerNumber == 1)
						{
							this.game.setPlayer1Brawlers(((SendBackPlayersBrawlersMessage) m).getThisPlayersBrawlers());
						}
						else //player number must equal 2
						{
							this.game.setPlayer2Brawlers(((SendBackPlayersBrawlersMessage) m).getThisPlayersBrawlers());
						}
						
						if(this.game.getPlayer1Brawlers() != null && this.game.getPlayer2Brawlers() != null)
						{
							//call some function to start the game
							String player1MonstersName = this.game.getPlayer1Brawlers().getBrawlers().get(0).getName();
							String player2MonstersName = this.game.getPlayer2Brawlers().getBrawlers().get(0).getName();
							this.game.getPlayer1().sendMessage(new GameStartMessage(player1MonstersName,player2MonstersName));
							this.game.getPlayer2().sendMessage(new GameStartMessage(player2MonstersName,player1MonstersName));
						}
					}
					else { //game is one player
						this.game.setPlayer1Brawlers(((SendBackPlayersBrawlersMessage) m).getThisPlayersBrawlers());
						String player1MonstersName = this.game.getPlayer1Brawlers().getBrawlers().get(0).getName();
						String player2MonstersName = this.game.getPlayer2Brawlers().getBrawlers().get(0).getName();
						this.game.getPlayer1().sendMessage(new GameStartMessage(player1MonstersName,player2MonstersName));
					}
				}
				else if(m instanceof AttackMessage)
				{
					if(!this.game.isOnePlayer())
					{
						int playerNumber = ((AttackMessage) m).getPlayerNumber();
						if(playerNumber == 1)
						{
							this.game.setP1AttackMessage((AttackMessage) m);
						}
						else //player number must be equal 2
						{
							this.game.setP2AttackMessage((AttackMessage) m);
						}
						if(this.game.getP1AttackMessage() != null && this.game.getP2AttackMessage() != null) 
						{
							int p1move = this.game.getP1AttackMessage().getAttackUsed();
							int p2move = this.game.getP2AttackMessage().getAttackUsed();
							this.game.setP1AttackMessage(null);
							this.game.setP2AttackMessage(null);
							game.makeMove(p1move, p2move);	
						}
					}
					else//oneplayer game
					{
						Random generator = new Random();
						int p2move = generator.nextInt(this.game.getPlayer2Brawlers().getBrawlers().get(this.game.getP2currentMonster()).getAbilities().size());
						game.makeMove(((AttackMessage) m).getAttackUsed(), p2move);
					}
				}
				else if(m instanceof Message)
				{	
					joinOrstart = m.message;
					if(joinOrstart.equals("1")) //means start a new game
					{
						while(this.game == null)
						{
							sendMessage(new WhatNameForGameMessage(1));
							m = readMessage();
							nameOfGame = m.getMessage();
							this.game = server.addGame(nameOfGame, this);
							if(this.game == null) sendMessage(new ErrorMessage("This game already exists"));
						}
						
						sendMessage(new Message()); //dummy message to validate
						sendMessage(new HowManyPlayersMessage());
						m = readMessage();
						howManyPlayers = m.getMessage();
						if(howManyPlayers.equals("1")) //start a one player game
						{
							game.onePlayerstartGame(server.getOurBrawlers());
						}
					}
						else //joinOrstart must equal 2, which means join game
						{
							
							while(this.game == null || this.game.isFull())
							{
								sendMessage(new WhatNameForGameMessage(2) );
								m = readMessage();
								nameOfGame = m.getMessage();
								this.game = server.gameExists(nameOfGame);
								if(this.game == null) sendMessage(new ErrorMessage("This game doesn't exist"));
								else if(this.game.isFull()) sendMessage(new ErrorMessage("This game is full"));
							}
							sendMessage(new Message(""));	//Dummy Message for verification
							game.twoPlayerstartGame(this, server.getOurBrawlers());//code to add to existing game and send entire roster information
						}
				}
			}
		}
		finally {
			try 
			{
				ois.close();
				oos.close();
				s.close();	
			}
			catch(IOException ioe)
			{
				server.removeServerThread(this);
			}
		}
	}

	public int getThreadId() {
		return threadId;
	}

	public void setThreadId(int threadId) {
		this.threadId = threadId;
	}
}
