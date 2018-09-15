import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Vector;

import com.google.gson.Gson;

public class Server{
	private Vector<ServerThread> serverThreads;
	private Vector<Game> currentGames;
	private Brawlers ourBrawlers; 
	
	Server(){	
		int port = -1;
		String option = "";
		Scanner in = new Scanner(System.in);
		int assigningId = 1;
		ServerSocket ss = null;
		FileReader fr;
		BufferedReader br;
		Gson gson = new Gson();
		String line = "";
		String fileString = "";
		String fileName = "";
		currentGames = new Vector<Game>();
		serverThreads = new Vector<ServerThread>();
	
		System.out.print("What is the name of the input file? ");
		fileName = in.nextLine();
		
		try {
			
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			line = br.readLine();
			
			while (line != null) {
				fileString += line;
				fileString += "\n";
				line = br.readLine();
			}
			
			this.setOurBrawlers(gson.fromJson(fileString,Brawlers.class));
			fr.close();
			br.close();
		}
		catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
		
		while(port < 1024 || port > 49151)
		{
			System.out.println("Please enter a valid port:  ");
			option = in.next();
			in.nextLine();
	
			try {
				port = Integer.parseInt(option);
			}
			catch(NumberFormatException nfe)
			{
				System.out.println("Invalid Port!\n");
				continue;
			
			}
			if(port < 1024 || port > 49151) System.out.println("Invalid Port!\n");
		}
		
		in.close();
		try {
			ss = new ServerSocket(port);
			System.out.println("\nSuccess!");
			
			while(true)
			{
				System.out.println("Waiting for connection...");
				Socket s = ss.accept();
				System.out.println("Connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s, this, assigningId);
				assigningId++;
				serverThreads.add(st);	
			}
		}
		catch(IOException ioe)
		{
			System.out.println("ioe in Server constructor: " + ioe.getMessage());
		}
		
	}
	
	public void removeServerThread(ServerThread st) {
		serverThreads.remove(st);
	}
	
	public Game gameExists(String s) //returns true if game already Exists
	{
		for(int i = 0; i < currentGames.size();i++)
		{
			if(currentGames.get(i).getGameName().equalsIgnoreCase(s)) {
				return currentGames.get(i);
			}
		}
		return null;
	}
	
	public Game addGame(String s, ServerThread st) //if the game already exists returns true and doesn't do anything
	{
		if(gameExists(s) != null) // if the game already exists return null
		{
			return null;
		}
		
		currentGames.add(new Game(s, st)); // add the game
		
		for(int i = 0; i < currentGames.size();i++)
		{
			if(currentGames.get(i).getGameName().equalsIgnoreCase(s))
			{
				return currentGames.get(i);
			}
		}
		
		return null; //should never be able to get here
	}
	
	public static void main(String[] args) {
		new Server();
	}

	public Brawlers getOurBrawlers() {
		return ourBrawlers;
	}

	public void setOurBrawlers(Brawlers ourBrawlers) {
		this.ourBrawlers = ourBrawlers;
	}
}
