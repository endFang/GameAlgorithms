package assignment4;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * A poker game
 * @author Dikai Fang
 *
 */

public class VideoPoker {
	
	private static final int DECK_SIZE = 52;
	private static final int RANKS_SIZE = 12;
	private static final int INITIAL_TOKENS = 10;
	
	private static final String[] suits = {"Heart", "Spade", "Club", "Diamonds"};
	private static final String[] ranks = {"Ace", "King", "Queen", "Jack", "10", "9", "8", "7", "6", "5", "4", "3", "2"};
	private static final String[] royalFlushRanks = {"Ace", "King", "Queen", "Jack", "10"};
	
	
	/**
	 * begin a game session with asking input and print output;
	 */
	public static void launcher()
	{
		System.out.println("Let's play poker games. Start with 10 tolens");
		int playedTimes = 0;
		boolean stopGame = false;
		
		int tokens = INITIAL_TOKENS;
		ArrayList<String[]> deck = new ArrayList<String[]>();
		ArrayList<String[]> topFive = new ArrayList<String[]>();
		
		while (!stopGame)
		{	
			++playedTimes;
			--tokens;
			System.out.println("Game " + playedTimes);
			printTokens(tokens);
			
			shuffleDeck(deck);
			giveTopFive(deck, topFive);
			
			reject(deck, topFive);
			
			tokens += scores(topFive);
			System.out.println();
			
			
			
			Scanner keyboard = new Scanner(System.in);
			String intention;
			boolean decided = false;
			while (!decided)
			{
				System.out.println("\nDo you want to continue by paying 1 token? Y/N: ");
				intention = keyboard.next().toLowerCase();
				
				if (tokens == 0)
				{
					System.out.println("\nNot enough tokens, game over. Good bye.");
					decided = true;
					stopGame = true;
				}
				else if (intention.toLowerCase().equals("y"))
				{
					decided = true;
					stopGame = false;
				}
				else if (intention.toLowerCase().equals("n"))
				{
					decided = true;
					stopGame = true;
					printTokens(tokens);
					System.out.println("You've palyed " + playedTimes + " times. Goody bye.");
				}
			}
			
		}
		
	}
	
	
	
	/**
	 * add 52 String[] into deck
	 * @param deck ArrayList of String[]
	 */
	public static void createDeck( ArrayList<String[]> deck )
	{
		
		for (String s : suits)
		{
			for ( String r : ranks)
			{
				String[] card = {s, r}; 
				deck.add(card);
			}
		}
	}
	
	
	
	/**
	 * create a deck and shuffle it
	 * @param deck an empty ArrayListl of String[]
	 */
	public static void shuffleDeck( ArrayList<String[]> deck)
	{
		deck.clear();
		Random randomGenerator = new Random();
		
		for (String s : suits)
		{
			for ( String r : ranks)
			{
				String[] card = {s, r}; 
				deck.add(card);
			}
		}
		
		for (int i=0; i<DECK_SIZE; ++i)
		{
			int n = i + randomGenerator.nextInt(DECK_SIZE-i);
			String[] tmp = deck.get(n);
			deck.set(n, deck.get(i));
			deck.set(i, tmp);
		}
	}
	
	/**
	 * pick top five cards from a given deck and add them to topFive
	 * @param deck ArrayList of String[]
	 * @param topFive an empty ArrayList of String[]
	 */
	public static void giveTopFive( ArrayList<String[]> deck, ArrayList<String[]> topFive )
	{
		topFive.clear();
		for (int i=0; i<5; ++i)
		{
			topFive.add(deck.get(i));
		}
		for (int i=0; i<5; ++i)
		{
			deck.remove(i);
		}
	}
	
	
	/**
	 * remove "i"s card in topFive, and add the card to deck;
	 * @param i int, the index of the card in topFive;
	 * @param deck ArrayList of String[]
	 * @param topFive ArrayList of String[]
	 */
	public static void removeCardFromTopFive(int i, ArrayList<String[]> deck, ArrayList<String[]> topFive)
	{
		String[] tmp = topFive.get(i);
		topFive.remove(i);
		deck.add(tmp);
	}
	
	
	/**
	 * remove the "i"s card in topFive, and add it to deck;
	 * @param deck ArrayList of String[]
	 * @param topFive ArrayList of String[]
	 */
	private static void reject( ArrayList<String[]> deck, ArrayList<String[]> topFive )
	{
		Scanner keyboard = new Scanner(System.in);
		int totalRejection = 0;
		
		System.out.println("\nYour five cards are: ");
		
		printCards(topFive);
		
		String intention;
		boolean decided = false;
		while (!decided)
		{
			System.out.println("\nDo you want to reject card(s)? Y/N: ");
			intention = keyboard.next().toLowerCase();
			if (intention.equals("y"))
			{
				decided = true;
			}
			else if (intention.toLowerCase().equals("n"))
			{
				return;
			}
		}
		
		decided = false;
		while (!decided)
		{
			System.out.println("\nHow many cards do you want to reject[1-5]: ");
			
			if (keyboard.hasNextInt())
			{
				totalRejection = keyboard.nextInt();
				if (totalRejection < 1 || totalRejection > 5)
				{
					System.out.println("\nPlease enter a number between 1 and 5.");
				}
				else
				{
					decided = true;
				}
			}
			else
			{
				keyboard.next();
			}
		}
		
		while (totalRejection > 0)
		{
			boolean rejected = false;
			int cardIndex;
			rejected = false;
			while (!rejected)
			{
				System.out.println("\nWhich card:  ");
				printCards(topFive);
				 
				if (keyboard.hasNextInt())
				{
					if (topFive.size() > 0)
					{
						cardIndex = keyboard.nextInt();
						if (cardIndex < 1 || cardIndex > topFive.size() )
						{
							System.out.println("Out of range, select again. ");
							break;
						}
						else
						{
							removeCardFromTopFive(cardIndex-1, deck, topFive);
							printCards(topFive);
							--totalRejection;
							rejected = true;
						}
						
					}
					
				}
				else
				{
					keyboard.next();
					System.out.println("Please enter an integer 1 to 5.");
				}
			}
		}
		
		System.out.println("\nRejection complete.");
		
	}
	
	
	
	/**
	 * check if topFive match any rules and calculate tokens. Print out result at the end;
	 * @param topFive ArrayList of String[], capacity is 5 or less
	 * @return int, the amount of tokens awarded.
	 */
	public static int scores(ArrayList<String[]> topFive)
	{
		if (topFive.size() == 5)
		{
			int i = pairsThreeFourAndFull(topFive);
			if (royalFlush(topFive))
			{
				System.out.println("Royal Flush! 250 Tokens");
				return 250;
			}
			else if (straightFlush(topFive))
			{
				System.out.println("Straight Flush! 50 Tokens");
				return 50;
			}
			else if (i == 4)
			{
				System.out.println("Four of Kind! 25 Tokens");
				return 25;
			}
			else if (flush(topFive))
			{
				System.out.println("Flush! 5 Tokens");
				return 5;
			}
			else if (straight(topFive))
			{
				System.out.println("Straight! 4 Tokens");
				return 4;
			}
			else
			{
				switch(i)
				{
					case 5:
						System.out.println("Full House! 6 Tokens");
						return 6;
					case 3:
						System.out.println("Three of Kind! 2 Tokens");
						return 2;
					case 2:
						System.out.println("Two pairs! 2 Tokens");
						return 2;
					case 1:
						System.out.println("One pair! 1 Tokens");
						return 1;
					case 0:
						System.out.println("No Pair! 0 Tokens");
						return 0;
				}
			}
		}
		else if (topFive.size() == 0)
		{
			System.out.println("No cards! 0 Tokens");
			return 0;
		}
		else
		{
			int i = pairsThreeFourAndFull(topFive);
			switch(i)
			{
				case 4:
					System.out.println("Four of Kind! 25 Tokens");
					return 25;
				case 5:
					System.out.println("Full House! 6 Tokens");
					return 6;
				case 3:
					System.out.println("Three of Kind! 3 Tokens");
					return 2;
				case 2:
					System.out.println("Two Pairs! 2 Tokens");
					return 2;
				case 1:
					System.out.println("One Pair! 1 Tokens");
					return 1;
				case 0:
					System.out.println("No Pair! 0 Tokens");
					return 0;
			}
		}
		return 0;
	}
	
	
	
	/**
	 * check Full House, Three of Kind, Two Pairs, One Pair, and No Pair in topFive
	 * @param topFiveArrayList of String[], capacity is 5 or less
	 * @return int, number code: 0 - no pair, 1 - one pair, 2- two pairs, 3 - three of kind, 4 - four of kind, 5 - full house
	 */
	private static int pairsThreeFourAndFull(ArrayList<String[]> topFive)
	{
		ArrayList<String> same1 = new ArrayList<String>();
		ArrayList<String> same2 = new ArrayList<String>();
		ArrayList<String> same3 = new ArrayList<String>();
		ArrayList<String> same4 = new ArrayList<String>();
		ArrayList<String> same5 = new ArrayList<String>();
		
		for ( String r : ranks)
		{
			for ( String[] c : topFive)
			{
				if (c[1] == r)
				{
					if (same1.size() == 0 || same1.get(0) == r)
					{
						same1.add(c[1]);
					}
					else if (same2.size() == 0 || same2.get(0) == r)
					{
						same2.add(c[1]);
					}
					else if (same3.size() == 0 || same3.get(0) == r)
					{
						same3.add(c[1]);
					}
					else if (same4.size() == 0 || same4.get(0) == r)
					{
						same4.add(c[1]);
					}
					else if (same5.size() == 0 || same5.get(0) == r)
					{
						same5.add(c[1]);
					}
				}
				
			}
		}
		
		int[] sameRank = new int[5];
		sameRank[0] = same1.size();
		sameRank[1] = same2.size();
		sameRank[2] = same3.size();
		sameRank[3] = same4.size();
		sameRank[4] = same5.size();
		
		
		//check three of kind, two pairs, one pair, no pair
		int numOfPairs = 0;
		boolean three = false;
		boolean four = false;
		for (int i : sameRank)
		{
			if (i == 4)
				four = true;
			else if (i == 3)
				three = true;
			else if (i == 2 )
				++numOfPairs;
		}
		
		if (four)
			return 4;
		if (three && numOfPairs == 1)
			return 5;
		if (three)
			return 3;
		return numOfPairs;
	}
	
	
	
	/**
	 * check if five cards ranks are consecutive in topFive
	 * @param topFive ArrayList of String[], capacity is 5 or less
	 * @return boolean, true if cards are consecutive
	 */
	private static boolean straight(ArrayList<String[]> topFive)
	{
		String[] topFiveRanks = new String[5];
		for (int i=0; i<5;++i)
		{
			topFiveRanks[i] = topFive.get(i)[1];
		}
		
		Arrays.sort(topFiveRanks);
		for (String s : topFiveRanks)
		{
			if (_consecutive( s, topFiveRanks) )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * helper functions for straight()
	 * @param e String, card's rank
	 * @param topFiveRanks String[], five cards' ranks
	 * @return boolean, true if they are consecutive
	 */
	private static boolean _consecutive(String e, String[] topFiveRanks)
	{
		//find the idealConsecutive elements for e, then find out if topFive has the rest 4 elements 
		int i = 0;
		for (; i<RANKS_SIZE+1; ++i)
		{
			if (ranks[i] == e) break;
		}
		
		String[] idealConsecutive = new String[5];
		if ( i <= 8)
		{
			for (int j=0; j<5; ++j)
			{
				idealConsecutive[j] = ranks[i+j];
			}
		}
		else
		{
			int index = 0;
			int offset = 4 - (RANKS_SIZE - i);
			
			for (; i<=RANKS_SIZE; ++i)
			{
				idealConsecutive[index] = ranks[i];
				++index;
			}
			for (int j=0; j < offset; ++j)
			{
				idealConsecutive[index] = ranks[j];
				++index;
			}
			
		}
		
		
		Arrays.sort(idealConsecutive); 
		return (Arrays.equals(topFiveRanks, idealConsecutive));
	}
	
	
	/**
	 * check if five cards are in the same suit in topFive (flush)
	 * @param topFive ArrayList of String[], capacity is 5
	 * @return boolean, true if flush
	 */
	private static boolean flush(ArrayList<String[]> topFive)
	{
		int same = 0;
		String firstCardSuit = topFive.get(0)[0];
		for ( String[] c : topFive)
		{
			if (c[0] == firstCardSuit)
			{
				++same;
			}
		}
		return (same == 5);
	}
	
	/**
	 * check if fives cards are in the same suit and have consecutive values in topFive (Straight Flush)
	 * @param topFive ArrayList of String[], capacity is 5 
	 * @return boolean, true if Straight Flush
	 */
	private static boolean straightFlush(ArrayList<String[]> topFive)
	{
		return (straight(topFive)&& flush(topFive));
	}
	
	
	/**
	 * check if fives cards are Ace, King, Queen, Jack, and 10, also are in the same suit in topFive (Royal Flush)
	 * @param topFiveArrayList of String[], capacity is 5
	 * @return boolean, true if Royal Flush
	 */
	private static boolean royalFlush(ArrayList<String[]> topFive )
	{
		
		boolean same = flush(topFive);
		
		int size = 5;
		ArrayList<String[]> cards = new ArrayList<String[]>(topFive);
		for (String r : royalFlushRanks)
		{
			for (String[] c : cards)
			{
				if ( c[1] == r)
				{
					--size;
					break;
				}
			}
		}
		
		return (same && size == 0) ? true : false;
	}

	
	//utilities
	/**
	 * print out how any tokens
	 * @param tokens
	 */
	private static void printTokens(int tokens)
	{
		System.out.println("You have: " + tokens + " tokens.");
	}
	
	
	/**
	 * print out what cards in topFive
	 * @param cards ArrayList of String[], capacity is 5 or less
	 */
	public static void printCards(ArrayList<String[]> cards)
	{
		int i = 1;
		for (String[] s : cards)
		{
			System.out.println(i + ". " + s[0] + " " + s[1]);
			++i;
		}
	}

}
