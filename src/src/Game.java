import java.util.Random;

public class Game {
	private String gameName;
	private ServerThread player1;
	private ServerThread player2;
	private Brawlers player1Brawlers;
	private Brawlers player2Brawlers;
	private int p1currentMonster;
	private int p2currentMonster;
	private AttackMessage p1AttackMessage;
	private AttackMessage p2AttackMessage;
	private boolean full;
	private boolean onePlayer;
	private boolean brawler1died;
	private boolean brawler2died;
	private String toSendtoP1;
	private String toSendtoP2;
	
	
	public boolean isOnePlayer() {
		return onePlayer;
	}

	public void setOnePlayer(boolean onePlayer) {
		this.onePlayer = onePlayer;
	}

	Game(String gameName, ServerThread player1)
	{
		this.gameName = gameName;
		this.player1 = player1;
		this.player2 = null;
		this.player1Brawlers=null;
		this.player2Brawlers=null;
		this.setP1currentMonster(0);
		this.setP2currentMonster(0);
		this.p1AttackMessage = null;
		this.p2AttackMessage = null;
		this.full = false;
		this.onePlayer = false;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	
	public void setBrawlers() {
		
	}

	public ServerThread getPlayer2() {
		return player2;
	}

	public void setPlayer2(ServerThread player2) {
		this.player2 = player2;
	}

	public ServerThread getPlayer1() {
		return player1;
	}

	public void setPlayer1(ServerThread player1) {
		this.player1 = player1;
	}

	public Brawlers getPlayer1Brawlers() {
		return player1Brawlers;
	}

	public void setPlayer1Brawlers(Brawlers player1Brawlers) {
		this.player1Brawlers = player1Brawlers;
	}

	public Brawlers getPlayer2Brawlers() {
		return player2Brawlers;
	}

	public void setPlayer2Brawlers(Brawlers player2Brawlers) {
		this.player2Brawlers = player2Brawlers;
	}

	public int getP1currentMonster() {
		return p1currentMonster;
	}

	public void setP1currentMonster(int p1currentMonster) {
		this.p1currentMonster = p1currentMonster;
	}

	public int getP2currentMonster() {
		return p2currentMonster;
	}

	public void setP2currentMonster(int p2currentMonster) {
		this.p2currentMonster = p2currentMonster;
	}

	public AttackMessage getP1AttackMessage() {
		return p1AttackMessage;
	}

	public void setP1AttackMessage(AttackMessage p1AttackMessage) {
		this.p1AttackMessage = p1AttackMessage;
	}

	public AttackMessage getP2AttackMessage() {
		return p2AttackMessage;
	}

	public void setP2AttackMessage(AttackMessage p2AttackMessage) {
		this.p2AttackMessage = p2AttackMessage;
	}
	
	public boolean isFull() {
		return full;
	}

	public void setFull(boolean full) {
		this.full = full;
	}
	
	public void twoPlayerstartGame(ServerThread player2, Brawlers ourBrawlers)
	{
		setPlayer2(player2);
		setFull(true);
		getPlayer2().sendMessage(new PrintAllBrawlers(ourBrawlers));
		getPlayer1().sendMessage(new PrintAllBrawlers(ourBrawlers));	
	}
	
	public void onePlayerstartGame(Brawlers ourBrawlers) {
		setFull(true); //set the isFull variable to true
		setOnePlayer(true);
		Brawlers CPUBrawlers = new Brawlers();
		Random generator = new Random();
		for(int i = 0;i < 3;i++)
		{
			int num = generator.nextInt(ourBrawlers.getBrawlers().size());
			CPUBrawlers.getBrawlers().add(ourBrawlers.getBrawlers().get(num)); // get three random monsters from the roster
		}
		setPlayer2Brawlers(CPUBrawlers);
		getPlayer1().sendMessage(new PrintAllBrawlers(ourBrawlers));	
	}
	
	public void makeMove(int p1move, int p2move) {
		Brawler p1Brawler = getPlayer1Brawlers().getBrawlers().get(p1currentMonster);
		Brawler p2Brawler = getPlayer2Brawlers().getBrawlers().get(p2currentMonster);
		double p1AttackStat = p1Brawler.getStats().getAttack();
		double p2AttackStat = p2Brawler.getStats().getAttack();	
		double p1DefenseStat = p1Brawler.getStats().getDefense();
		double p2DefenseStat = p2Brawler.getStats().getDefense();
		int p1HealthStat = p1Brawler.getStats().getHealth();
		int p2HealthStat = p2Brawler.getStats().getHealth();
		int p1Speed = p1Brawler.getStats().getSpeed();
		int p2Speed = p2Brawler.getStats().getSpeed();
		Ability p1Attack = p1Brawler.getAbilities().get(p1move);
		Ability p2Attack = p2Brawler.getAbilities().get(p2move);
		double p1AttackPower = p1Attack.getDamage();
		double p2AttackPower = p2Attack.getDamage();
		String p1AttackType = p1Brawler.getAbilities().get(p1move).getType();
		String p2AttackType = p2Brawler.getAbilities().get(p2move).getType();
		String p1MonsterType = p1Brawler.getType();
		String p2MonsterType = p2Brawler.getType();
		int attack1to2 = (int) Math.floor((((p1AttackStat*(p1AttackPower/p2DefenseStat))/5)*(damageMultilplier(p1AttackType, p2MonsterType))));
		int attack2to1 = (int) Math.floor((((p2AttackStat*(p2AttackPower/p1DefenseStat))/5)*(damageMultilplier(p2AttackType, p1MonsterType))));
		
		if(attack2to1 > p1HealthStat) attack2to1 = p1HealthStat;
		if(attack1to2 > p2HealthStat) attack1to2 = p2HealthStat;
		
		String firstPlayerAttack = "\n";
		String secondPlayerAttack = "\n";
		toSendtoP1 = "";
		toSendtoP2 = "";
		brawler2died = false;
		brawler1died = false;
		
		firstPlayerAttack  += p1Brawler.getName() + " used " +  p1Attack.getName() + "!\n";
		if(damageMultilplier(p1AttackType, p2MonsterType) == 2.0) firstPlayerAttack += "It was super effective!\n";
		else if (damageMultilplier(p1AttackType, p2MonsterType) == 0.5)firstPlayerAttack += "It was not very effective!\n";
		firstPlayerAttack  += "It did " +  attack1to2 + " damage!\n";	
		
		secondPlayerAttack += p2Brawler.getName() + " used " +  p2Attack.getName() + "!\n";
		if(damageMultilplier(p2AttackType, p1MonsterType) == 2.0)secondPlayerAttack += "It was super effective!\n";
		else if (damageMultilplier(p2AttackType, p1MonsterType) == 0.5)secondPlayerAttack += "It was not very effective!\n";
		secondPlayerAttack += "It did " +  attack2to1 + " damage!\n";	
		
		if( p1Speed > p2Speed ) player1GoesFirst(firstPlayerAttack, secondPlayerAttack, attack1to2, attack2to1, p1HealthStat, p2HealthStat);
		else if ( p1Speed < p2Speed) player2GoesFirst(firstPlayerAttack, secondPlayerAttack, attack1to2, attack2to1, p1HealthStat, p2HealthStat);
		else //speed stat is equal, randomly choose who goes first
		{
			Random generator = new Random();
			int i = generator.nextInt(2);
			
			if(i == 1) player1GoesFirst(firstPlayerAttack, secondPlayerAttack, attack1to2, attack2to1, p1HealthStat, p2HealthStat);
			else player2GoesFirst(firstPlayerAttack, secondPlayerAttack, attack1to2, attack2to1, p1HealthStat, p2HealthStat);
		}
		
		toSendtoP2 = toSendtoP1; //at this point p1 and p2's output is the same
		
		finalizeAndSendMessage(p1Brawler, p2Brawler);
	}

	public double damageMultilplier(String attackType, String monstersType)
	{
		if(attackType.equals("water") && monstersType.equals("fire") ||
			attackType.equals("fire") && monstersType.equals("air") ||
			attackType.equals("air") && monstersType.equals("earth") ||
			attackType.equals("earth") && monstersType.equals("lightning")||
			attackType.equals("lightning") && monstersType.equals("water")){
			return 2.0;
		}
		else if(attackType.equals("water") && monstersType.equals("lightning") ||
				attackType.equals("lightning") && monstersType.equals("earth") ||
				attackType.equals("air") && monstersType.equals("fire") ||
				attackType.equals("earth") && monstersType.equals("air")||
				attackType.equals("fire") && monstersType.equals("water")){
			return 0.5;
		}
		
		return 1.0;
	}
	
	public boolean p1MakesMove(int p2HealthStat, int attack1to2) { //returns true if brawler2 died
		
		p2HealthStat -= attack1to2;
		player2Brawlers.getBrawlers().get(p2currentMonster).getStats().setHealth(p2HealthStat);
		if(p2HealthStat == 0)return true;
		return false;
	}
	
	public boolean p2MakesMove(int p1HealthStat, int attack2to1) { //returns true if brawler1 died
		
		p1HealthStat -= attack2to1;
		player1Brawlers.getBrawlers().get(p1currentMonster).getStats().setHealth(p1HealthStat);
		if(p1HealthStat == 0)return true;
		return false;
	}
	
	public void player1GoesFirst(String firstPlayerAttack, String secondPlayerAttack, int attack1to2, int attack2to1, int p1HealthStat, int p2HealthStat) {
		
		toSendtoP1 = firstPlayerAttack;
		brawler2died = p1MakesMove(p2HealthStat, attack1to2);
		
		if(brawler2died)++p2currentMonster; // add 1 to p2 current monster		
		else //brawler2 did not die
		{
			toSendtoP1 += secondPlayerAttack;
			brawler1died = p2MakesMove(p1HealthStat, attack2to1);
			
			if(brawler1died)++p1currentMonster; 
		}
		
	}
	
	public void player2GoesFirst(String firstPlayerAttack, String secondPlayerAttack, int attack1to2, int attack2to1, int p1HealthStat, int p2HealthStat) {
		toSendtoP1 = secondPlayerAttack;
		brawler1died = p2MakesMove(p1HealthStat, attack2to1);
		
		if(brawler1died)++p1currentMonster; // add 1 to p1 current monster
		else
		{
			toSendtoP1 += firstPlayerAttack;
			brawler2died = p1MakesMove(p2HealthStat, attack1to2);
			if(brawler2died)++p2currentMonster; // add 1 to p1 current monster
		}
	}

	public void finalizeAndSendMessage(Brawler p1Brawler, Brawler p2Brawler)
	{
		int p1HealthStat = 0;
		int p2HealthStat = 0;
		if(!brawler1died)p1HealthStat = getPlayer1Brawlers().getBrawlers().get(getP1currentMonster()).getStats().getHealth();
		if(!brawler2died)p2HealthStat = getPlayer2Brawlers().getBrawlers().get(getP2currentMonster()).getStats().getHealth();
		
		if(!brawler1died && !brawler2died)
		{
			toSendtoP1 += "\n" + p1Brawler.getName() + " has " + p1HealthStat  + " health!\n";
			toSendtoP2 += "\n" + p2Brawler.getName() + " has " + p2HealthStat + " health!\n";
		}
		else if(brawler1died)
		{
			toSendtoP2 += "\n" + p2Brawler.getName() + " has " + p2HealthStat + " health!\n";
			toSendtoP1 += "\n" + p1Brawler.getName() + " was defeated!\n";
			toSendtoP2 += "\n" + p1Brawler.getName() + " was defeated!\n";
			
			if(p1currentMonster < getPlayer1Brawlers().getBrawlers().size()){
				toSendtoP1 += "You sent out " + player1Brawlers.getBrawlers().get(p1currentMonster).getName() + "!\n";
				toSendtoP2 += "Your opponent sent out " + player1Brawlers.getBrawlers().get(p1currentMonster).getName() + "!\n";
			}
			else
			{
				toSendtoP1 += "You are out of brawlers!\n" + "\nYou Lose!\n";
				toSendtoP2 += "Your opponent is out of brawlers!\n" + "\nYou Win!\n";
				player1.sendMessage(new EndGameMessage(toSendtoP1));
				if(!isOnePlayer())player2.sendMessage(new EndGameMessage(toSendtoP2));
				return;
			}
			
		}
		else if(brawler2died)
		{
			toSendtoP1 += "\n" + p1Brawler.getName() + " has " + p1HealthStat + " health!\n";
			toSendtoP2 += "\n" + p2Brawler.getName() + " was defeated!\n";
			toSendtoP1 += "\n" + p2Brawler.getName() + " was defeated!\n";
			
			if(p2currentMonster < getPlayer2Brawlers().getBrawlers().size()){
				toSendtoP2 += "You sent out " + player2Brawlers.getBrawlers().get(p2currentMonster).getName() + "!\n";
				toSendtoP1 += "Your opponent sent out " +  player2Brawlers.getBrawlers().get(p2currentMonster).getName() + "!\n";
			}
			else
			{	
				toSendtoP2 += "You are out of brawlers!\n" + "\nYou Lose!\n";
				toSendtoP1 += "Your opponent is out of brawlers!\n" + "\nYou Win!\n";
				player1.sendMessage(new EndGameMessage(toSendtoP1));
				if(!isOnePlayer())player2.sendMessage(new EndGameMessage(toSendtoP2));
				return;
			}
		}
		
		
		getPlayer1().sendMessage(new AftermathOfBattleMessage(toSendtoP1,p1currentMonster));
		if(!isOnePlayer())getPlayer2().sendMessage(new AftermathOfBattleMessage(toSendtoP2,p2currentMonster));
	}
}
