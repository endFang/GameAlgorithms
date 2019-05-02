package assignment4;

import java.util.ArrayList;

public class Ex1 {
	
//	private static ArrayList<String[]> deck = new ArrayList<String[]>();
//	private static ArrayList<String[]> topFive = new ArrayList<String[]>();
	
	private static String[] suits = {"Heart", "Spade", "Club", "Diamonds"};
	private static String[] ranks = {"Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
	
	
	/**
	 * main method, execute a sequence of examine functions
	 * @param args N/A
	 */
	public static void main(String[] args)
	{
		exCreateDeck();
		exShuffle();
		exGiveTopFive();
		exRemoveCardFromTopFive();
		exScore();
		exLauncher();
	}
	
	/**
	 * examine the createDeck();
	 */
	public static void exCreateDeck()
	{
		ArrayList<String[]> deck = new ArrayList<String[]>();
		
		System.out.println("\nexamining createDeck() ... ");
		System.out.println("\ndeck before create: ");
	
		VideoPoker.printCards(deck);
		
		VideoPoker.createDeck(deck);
		
		System.out.println("\ndeck after create: ");
		VideoPoker.printCards(deck);
	}
	
	/**
	 * examine the Shuffle()
	 */
	public static void exShuffle()
	{
		ArrayList<String[]> deck = new ArrayList<String[]>();
		ArrayList<String[]> topFive = new ArrayList<String[]>();
		
		
		System.out.println("\nexaming shuffleDeck()...");
		VideoPoker.createDeck(deck);
		System.out.println("\ndeck before shuffle: ");
		VideoPoker.printCards(deck);
		
		VideoPoker.shuffleDeck(deck);
		
		System.out.println("\ndeck after shuffle: ");
		VideoPoker.printCards(deck);
		
	}
	
	/**
	 * examine the gitTopFive()
	 */
	public static void exGiveTopFive()
	{
		ArrayList<String[]> deck = new ArrayList<String[]>();
		ArrayList<String[]> topFive = new ArrayList<String[]>();
		System.out.println("\nexaming giveTopFive()...");
		VideoPoker.createDeck(deck);
		VideoPoker.shuffleDeck(deck);
		
		System.out.println("\nFirst five cards on deck top are: ");
		for (int i=0; i< 5; ++i)
		{
			System.out.println(deck.get(i)[0] + " " + deck.get(i)[1]);
		}
		
		VideoPoker.giveTopFive(deck, topFive);
		
		System.out.println("\ndeck has " + deck.size() + " cards now");
		System.out.println("\ntopFive has " + topFive.size() + " cards now");
		System.out.println("\ntopFive has: ");
		VideoPoker.printCards(topFive);
		
	}
	
	/**
	 * examine the removeCardFromTopFive()
	 */
	public static void exRemoveCardFromTopFive()
	{
		ArrayList<String[]> deck = new ArrayList<String[]>();
		ArrayList<String[]> topFive = new ArrayList<String[]>();
		
		VideoPoker.createDeck(deck);
		VideoPoker.shuffleDeck(deck);
		VideoPoker.giveTopFive(deck, topFive);
		
		System.out.println("\nexaming removeCardFromTopFive()");
		System.out.println("\nTop Five cards are: ");
		VideoPoker.printCards(topFive);
		System.out.println("\nrAfter remove the first card: ");
		VideoPoker.removeCardFromTopFive(0, deck, topFive);
		VideoPoker.printCards(topFive);
		System.out.println("\nrAfter remove the third card: ");
		VideoPoker.removeCardFromTopFive(2, deck, topFive);
		VideoPoker.printCards(topFive);
		
	}
	
	/**
	 * examine the Pairs, Three/Four of Kind, Straight, Flush, Full House and Royal Flush
	 */
	public static void exScore()
	{
		System.out.println("examing score()...");
		
		int tokens = 0;
		
		String[] HeartAce = {"Heart", "Ace"};
		String[] HeartKing = {"Heart", "King"};
		String[] HeartQueen = {"Heart", "Queen"};
		String[] HeartJack = {"Heart", "Jack"};
		String[] Heart10 = {"Heart", "10"};
		String[] Heart2 = {"Heart", "2"};
		
		String[] Club4 = {"Club", "4"};
		String[] Club3 = {"Club", "3"};
		String[] Club2 = {"Club", "2"};
		String[] ClubAce = {"Club", "Ace"};
		String[] ClubKing = {"Club", "King"};
		
		String[] Diamond2 = {"Diamond", "2"};
		String[] Diamond3 = {"Diamond", "3"};
		String[] Diamond4 = {"Diamond", "4"};
		String[] Diamond5 = {"Diamond", "5"};
		String[] Diamond6 = {"Diamond", "6"};
		String[] DiamondJack = {"Diamond", "Jack"};
		
		String[] Spade4 = {"Spade", "4"};
		String[] Spade3 = {"Spade", "3"};
		String[] Spade2 = {"Spade", "2"};
		String[] SpadeAce = {"Spade", "Ace"};
		String[] SpadeKing = {"Spade", "King"};
		
		ArrayList<String[]> exFive = new ArrayList<String[]>();
		
		
		/*
		 * no pair, 1 pair, 2 pairs
		 */
		System.out.println("\nPairs Test 1: 1 card, no pair");
		exFive.add(Diamond2);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 0"); // 1 card, no pair
		
		tokens = 0;
		System.out.println("\nPairs Test 2: 2 card, 1 pair");
		exFive.add(Club2);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 1"); 
		
		
		tokens = 0;
		System.out.println("\nPairs Test 3: 2 card, no pair");
		exFive.set(1, Club4);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 0"); 
		
		tokens = 0;
		System.out.println("\nPairs Test 4: 4 card, 2 pair");
		exFive.set(1, Club2);
		exFive.add(HeartKing);
		exFive.add(SpadeKing);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 2");
		
		
		tokens = 0;
		System.out.println("\nPairs Test 5: 5 card, 2 pair");
		exFive.add(HeartAce);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 2"); 
		
		
		/*
		 * Straight, Flush
		 */
		
		tokens = 0;
		exFive.clear();
		System.out.println("\nStraight and Flush Test 1: Flush");
		exFive.add(Club2);
		exFive.add(Diamond3);
		exFive.add(Diamond4);
		exFive.add(Diamond5);
		exFive.add(Diamond6);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 4"); 
		
		
		tokens = 0;
		System.out.println("\nStraight and Flush Test 2: Straight");
		exFive.set(0, DiamondJack);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 5"); 
		
		tokens = 0;
		System.out.println("\nStraight and Flush Test 3: Straight Flush");
		exFive.set(0, Spade4);
		exFive.set(1, Spade3);
		exFive.set(2, Spade2);
		exFive.set(3, SpadeAce);
		exFive.set(4, SpadeKing);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 50"); 
		
		tokens = 0;
		System.out.println("\nStraight and Flush Test 4: Not Straight Flush");
		exFive.set(3, HeartQueen);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 0"); 
		
		
		/*
		 * Three/Four of Kind
		 */
		tokens = 0;
		exFive.clear();
		System.out.println("\nThree/Four of Kind Test 1: 3 cards, Three of Kind");
		exFive.add(Diamond2);
		exFive.add(Club2);
		exFive.add(Spade2);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 2"); 
		
		
		tokens = 0;
		System.out.println("\nThree/Four of Kind Test 2: 4 cards, Four of Kind");
		exFive.add(Heart2);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 25");
		
		tokens = 0;
		System.out.println("\nThree/Four of Kind Test 3: 5 cards, Four of Kind");
		exFive.add(HeartQueen);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 25");
		
		tokens = 0;
		System.out.println("\nThree/Four of Kind Test 4: 5 cards, Three of Kind");
		exFive.set(3, HeartKing);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 2");
		
		
		/*
		 * Full House
		 */
		tokens = 0;
		System.out.println("\nFull House Test 1");
		exFive.set(4, ClubKing);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 6"); 
		
		
		/*
		 * Royal Flush
		 */
		tokens = 0;
		System.out.println("\nRoyal Flush Test 1");
		exFive.clear();
		exFive.add(HeartKing);
		exFive.add(HeartAce);
		exFive.add(HeartQueen);
		exFive.add(HeartJack);
		exFive.add(Heart10);
		tokens += VideoPoker.scores(exFive);
		System.out.println(tokens);
		System.out.println("Expecting: 250"); // Royal Flush, Flush
		
	}
	
	/**
	 * exam the VideoPoker's launcher
	 */
	public static void exLauncher()
	{
		VideoPoker.launcher();
	}
	
	

}
