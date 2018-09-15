import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client{
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Brawlers thisPlayersBrawlers;
	private Scanner in;
	
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
			System.out.println("In read message exception");
		}
		return null;
	}
	
	public int printMenu(int currentMonster) 
	{
		int option = -1;
		ArrayList<Ability> firstMonstersAttacks = thisPlayersBrawlers.getBrawlers().get(currentMonster).getAbilities();
		while(option > firstMonstersAttacks.size() || option <= 0)
		{	
			System.out.println("Choose a move:");
			for(int i = 0; i < firstMonstersAttacks.size();i++ )
			{
				Ability thisAbility = firstMonstersAttacks.get(i);
				System.out.println(i+1 + ") " + thisAbility.getName() + ", " + thisAbility.getType() + ", " + thisAbility.getDamage());
			}
			option = in.nextInt();
		}
		
		return option;
	}
	
	public Client()
	{
		in = new Scanner(System.in);
		String IPAddress = "";
		String option = "";
		int port = 0;
		Socket s = null;
		String joinOrstart = "";
		String nameOfGame = "";
		String howManyPlayers = "";
		int playerNumber = 0;
		thisPlayersBrawlers = new Brawlers();
		boolean unableToConnect = true;
		
		while(unableToConnect) {
			System.out.println("Enter an IP address: ");
			IPAddress = in.next();
			in.nextLine();
			
			while(port < 1024 || port > 49151)
			{
				System.out.println("Please enter a valid port:  ");
				option = in.next();
				in.nextLine();
				
				try 
				{	
					port = Integer.parseInt(option);
				}
				catch(NumberFormatException nfe)
				{
					System.out.println("Invalid Port!\n");
					continue;
				}
				
				if(port < 1024 || port > 49151)System.out.println("Invalid Port!\n");
			}
			
			try {
				s = new Socket(IPAddress,port);
				unableToConnect = false;
			}
			catch(IOException ioe){
				System.out.println("Unable to connect!");
				port = -1;
			}
			
		}
	
		
		try
		{
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
		} 
		catch (IOException e) {
			System.out.println("Can't initialize ObjectInputStream or ObjectOutputStream");
		}
		
		while(true)
		{
			Message m = readMessage();
			if(m instanceof StartOrJoinMessage) {
				while(!joinOrstart.trim().equals("1") && !joinOrstart.trim().equals("2")){
					System.out.println(m.getMessage());
					joinOrstart = in.next();
					in.nextLine();
				}
				if(joinOrstart.trim().equals("1")) playerNumber = 1;
				else 
				{
					playerNumber = 2;
				}
				sendMessage(new Message(joinOrstart));
			}
			else if(m instanceof WhatNameForGameMessage)
			{
				Message p = new ErrorMessage("");
				while(p instanceof ErrorMessage)
				{
					System.out.println(m.message);
					nameOfGame = in.next();
					in.nextLine();
					sendMessage(new Message(nameOfGame));
					p = readMessage();
					if(p instanceof ErrorMessage)
					{
						System.out.println(p.getMessage());
						m = readMessage();
					}
				}
			}
			else if(m instanceof HowManyPlayersMessage) {
				while(!howManyPlayers.trim().equals("1") && !howManyPlayers.trim().equals("2")){
					System.out.println(m.getMessage());
					howManyPlayers = in.next();
					in.nextLine();
				}
				sendMessage(new Message(howManyPlayers));
				if(howManyPlayers.equals("2")) System.out.println("Waiting for players to connect...");
			}				
			else if(m instanceof PrintAllBrawlers)
			{
				if(playerNumber == 1 && howManyPlayers.equals("2")) System.out.println("Player 2 connected!");
				System.out.println("Starting game...");
				Brawlers ourBrawlers = ((PrintAllBrawlers) m).getCurrentRoster();
				String chosen3 = "";
				boolean valid = false;
				String [] arrOfStr = chosen3.split(",");
				int [] arrOfInt = new int[]{0, 0, 0};
				

				while(!valid) 
				{
					try {
						System.out.println("Choose 3 Brawlers:");
						for(int i = 0; i < ourBrawlers.getBrawlers().size();i++)
						{
							System.out.println(i+1 + ") " + ourBrawlers.getBrawlers().get(i).getName());
						}
						
						chosen3 = in.next();
						in.nextLine();
						
						arrOfStr = chosen3.split(",");
						if(arrOfStr.length > 3 ) continue; 
						
						valid = true; //preset the value to true and then check if any of them are out of bound
						for(int i = 0; i < arrOfStr.length;i++)
						{
							arrOfInt[i] = Integer.parseInt(arrOfStr[i]);
							if(arrOfInt[i] > ourBrawlers.getBrawlers().size() || arrOfInt[i] <= 0) valid = false;
						}
					}
					catch(NumberFormatException nfe){
						valid = false;
					}
					
					if(!valid)
					{
						System.out.println("Invalid!\n");
					}
				}
				
				for(int i = 0; i < arrOfStr.length;i++)
				{
					thisPlayersBrawlers.getBrawlers().add(ourBrawlers.getBrawlers().get(arrOfInt[i]-1));
				}
				
				sendMessage(new SendBackPlayersBrawlersMessage(thisPlayersBrawlers,playerNumber));
			}
			else if(m instanceof GameStartMessage)
			{
				System.out.println("\nExcellent!");
				System.out.println("You send " + ((GameStartMessage) m).getYourBrawlersName() + "!");
				System.out.println("Your opponenet sends " + ((GameStartMessage) m).getOpponentsBrawlersName() + "!\n");
				//Print the first monsters move
				int attackChosen = printMenu(0);
				sendMessage(new AttackMessage(attackChosen - 1, playerNumber));
			}
			else if(m instanceof AftermathOfBattleMessage)
			{
				System.out.println(m.getMessage());
				int attackChosen = printMenu(((AftermathOfBattleMessage) m).getCurrMonster());
				sendMessage(new AttackMessage(attackChosen - 1, playerNumber));
			}
			else if(m instanceof EndGameMessage)
			{
				System.out.println(m.getMessage());
				//kill the thread
				try {
					s.close();
					break;
				} 
				catch (IOException e) {
				}
			}
			else
			{
				System.out.println("In the else clause");
			}
		}
	}
	
	public static void main(String [] args ) {
		new Client();
	}
}
