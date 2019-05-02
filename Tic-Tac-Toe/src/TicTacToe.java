
import java.util.Scanner;
import java.util.ArrayList;

/**
 * begins a tic-tac-toe game with COMPUTER
 * player X/player 1 begins first
 * the gameboard will be printed in the beginning, ending, and every turn's end
 * @author Dikai Fang
 *
 */

public class TicTacToe {
	private static final int PLAYER_X = 0;
	private static final int PLAYER_O = 1;
	
	private static int COMPUTER;
	
	
	/**
	 * the game loop: input players' names(no blank line); human player choose first or second turn; player X begins first
	 * @param args n/a
	 */
	public static void main(String[] args)
	{
		char[][] board = new char[3][3];
		
		Scanner keyboard = new Scanner(System.in);

		String playerX_name = "";
		String playerO_name = "";
		String humanName = askHumanName();
		
		
		int humanTurn = 0;
		int computerTurn = 1;
		
		boolean decided = false;
		while (!decided)
		{
			System.out.println("\nFrist[X] or Second[O]: ");
			if (keyboard.hasNext())
			{
				String intention = keyboard.next().toLowerCase();
				if (intention.equals("x"))
				{
					humanTurn = 0;
					computerTurn = 1;
					decided = true;
				}
				else if (intention.equals("o"))
				{
					humanTurn = 1;
					computerTurn = 0;
					decided = true;
				}
				else
				{
					System.out.println("Please enter X or O.");
				}
			}
		}
		COMPUTER = computerTurn;
		
		
		
		if (humanTurn == PLAYER_X)
		{
			playerX_name = humanName;
			playerO_name = "Computer";
		}
		else if (humanTurn == PLAYER_O)
		{
			playerO_name = humanName;
			playerX_name = "Computer";
		}
		
		
		int player = PLAYER_X;
		boolean done = false;
		while (!done)
		{
			System.out.println();
			printBoard(board);
			
			if (player == humanTurn)
			{
				if (player == PLAYER_X)
					System.out.println("Player X(" + playerX_name + ")'s turn.");
				else
					System.out.println("Player O(" + playerO_name + ")'s turn.");
				int row = getRow(keyboard);
				int col = getCol(keyboard);
				
				if (!isLegalMove(board, row, col))
				{
					System.out.println("Invalid move, try again");
				}
				else
				{
					placeMarker(board, row, col, player);
					if (playerWins(board, player))
					{
						printBoard(board);
						if (player == PLAYER_X)
							System.out.println("Player X(" + playerX_name + ") win.");
						else
							System.out.println("Player O(" + playerO_name + ") win.");
						done = true;
					}
					else if (isDraw(board))
					{
						printBoard(board);
						System.out.println("It's a draw!");
						done = true;
					}
					else
					{
						player = (player == PLAYER_X) ? PLAYER_O : PLAYER_X;
					}
				}

			}
			else
			{
				computerMove(board, computerTurn);
				if (playerWins(board, player))
				{
					printBoard(board);
					if (player == PLAYER_X)
						System.out.println("Player X(" + playerX_name + ") win.");
					else
						System.out.println("Player O(" + playerO_name + ") win.");
					done = true;
				}
				else if (isDraw(board))
				{
					printBoard(board);
					System.out.println("It's a draw!");
					done = true;
				}
				else
				{
					player = (player == PLAYER_X) ? PLAYER_O : PLAYER_X;
				}
			}
			
			
		}
		System.out.println("Thanks for playing!");
	}
	
	
	//==================================================
	//				 helpful functions
	//==================================================
	
	/**
	 * call miniMax to find the best move
	 * @param gameboard char[3][3]
	 * @param emptyBoard boolean, if the board is empty, COMPUTER goes first
	 * @param computerTurn int
	 */
	private static void computerMove(char[][] gameboard, int computerTurn )
	{
		int player = computerTurn;
		int[] emptyMove = new int[] {-1,-1};
		int[] bestMove = miniMax(gameboard, depth(gameboard), emptyMove, player);
		placeMarker(gameboard, bestMove[0], bestMove[1], computerTurn);
	}
	
	/**
	 * ask input to set human player's name
	 * @return String, the input name
	 */
	public static String askHumanName()
	{
		Scanner S = new Scanner(System.in);
		System.out.print("Player's name: ");
		String name = S.nextLine();
		while (name.isEmpty())
		{
			System.out.println("Player name cannot be a blank line.");
			System.out.print("Player X's name: ");
			name = S.nextLine();
		}
		return name;
	}
	
	/**
	 * print the gameboard
	 * @param board a gameboard represented by a 2D char array 
	 */
	public static void printBoard(char[][] board)
	{
		int col = board.length;
		int row = board[0].length;
		System.out.println(" --|--|-- ");
		for (int i=0; i<col; ++i)
		{
			System.out.print("|");
			for (int j=0; j<row; ++j)
			{
				if (board[i][j] == 0)
				{
					System.out.print("  |");
				}
				else
				{
					System.out.print(board[i][j] + " |");
				}
			}
			System.out.print("\n --|--|-- \n");
		}
	}
	
	/**
	 * ask input of an int in [0,2] representing the player's marker's row index
	 * ask again if input is not qualified
	 * @param keyboard Scanner object
	 * @return player's input (int)
	 */
	public static int getRow(Scanner keyboard)
	{
		int row = -1;
		boolean validInput = false;
		while (!validInput)
		{
			System.out.print("Enter row (0-2): ");
			if (keyboard.hasNextInt())
			{
				row = keyboard.nextInt();
				if ( 0 <= row && row <= 2)
					validInput = true;
			}
			else
			{
				keyboard.next();
				System.out.println("please enter an integer between 0 and 2 (inclusive)");
			}
		}
		return row;
	}
	
	/**
	 * ask input of an int in [0,2] representing the player's marker's column index
	 * ask again if input is not qualified
	 * @param keyboard Scanner object
	 * @return player's input (int)
	 */
	public static int getCol(Scanner keyboard)
	{
		int col = -1;
		boolean validInput = false;
		while (!validInput)
		{
			System.out.print("Enter col (0-2): ");
			if (keyboard.hasNextInt())
			{
				col = keyboard.nextInt();
				if ( 0 <= col && col <= 2 )
					validInput = true;
			}
			else
			{
				keyboard.next();
				System.out.println("please enter an integer between 0 and 2 (inclusive)");
			}
		}
		return col;
	}
	
	
	//==================================================
	//					game logic
	//==================================================
	
	/**
	 * check if the gameboard is empty at the specified row and column
	 * @param board gameboard
	 * @param row the row index
	 * @param col the column index
	 * @return true if the tile has no tile, otherwise false
	 */
	public static boolean isLegalMove(char[][] board, int row, int col)
	{
		return (board[row][col] == 0);
	}
	
	
	
	/**
	 * place the player's marker at row and column on gameboard
	 * @param board gameboard
	 * @param row row index
	 * @param col column index
	 * @param player int representing the player
	 */
	public static void placeMarker(char[][] board, int row, int col, int player)
	{
		char marker = (player == 0) ? 'X' : 'O';
		board[row][col] = marker;
	}
	
	
	/** 
	 * check if the player has  three of its markers in a line by vertical, horizontal and diagonal
	 * @param board gameboard
	 * @param player int representing player
	 * @return true if there is a line of three markers in vertical, horizontal or diagonal direction
	 */
	public static boolean playerWins(char[][] board, int player)
	{
		int rowWin = 0;
		int colWin = 0;
		int diaWin = 0;
		char marker = (player == 0) ? 'X' : 'O';
		for (int c=0; c<3; ++c)
		{
			for (int r=0; r<3; ++r)
			{
				rowWin += ((board[c][r] == marker) ? 1 : 0);
				colWin += ((board[r][c] == marker) ? 1 : 0);
			}
			
			//did this row win?
			if (rowWin == 3)
				return true;
			else
				rowWin = 0;

			//did this row win?
			if (colWin == 3)
				return true;
			else
				colWin = 0;
		}
		
		
		//did diagonal win?
		for (int i=0; i<3; ++i)
		{
			diaWin += ((board[i][i]) == marker ? 1 : 0);
		}
		
		if (diaWin == 3)
			return true;
		else
			diaWin = 0;
		
		for (int i=0; i<3; ++i)
		{
			diaWin += ((board[i][2-i]) == marker ? 1 : 0);
		}
		
		if (diaWin == 3)
			return true;
		
		return false;
	}
	
	
	/**
	 * check if the gameboard is full
	 * @param board gameboard
	 * @return true if the gameboard is full
	 */
	public static boolean isDraw(char[][] board)
	{
		boolean draw = true;
		for (int i=0; i<3; ++i)
		{
			for (int j=0; j<3; ++j)
			{
				draw = (draw && (board[i][j] != 0));
			}
		}
		return draw;
	}
	
	//==================================================
	//				Minimax Logic
	//==================================================
	
	
	/**
	 * calculate the gameboard's empty slots
	 * @param gameboard char[3][3]
	 * @return int, the amount of empty slots
	 */
	public static int depth(char[][] gameboard)
	{
		int d = 0;
		for (int i=0; i<3; ++i)
		{
			for (int j=0; j<3; ++j)
			{
				if (gameboard[i][j] == 0)
					++d;
			}
			
		}
		return d; 
	}
	
	/**
	 * find all available space on gameboard
	 * @param gameboard char[][], the gameboard
	 * @return ArrayList of int[], each int[] represents a free space {row, col}
	 */
	public static ArrayList<int[]> freeSlots(char[][] gameboard)
	{
		ArrayList<int[]> result = new ArrayList<int[]> ();
		
		for (int i=0; i<3; ++i)
		{
			for (int j=0; j<3; ++j)
			{
				if (gameboard[i][j] == 0)
				{
					int[] fs = new int[]{i, j};
					result.add(fs);
				}
			}
		}
		return result;
	}
	
	/**
	 * evaluate the move the player played on gameboard
	 * @param gameboard char[][], the game board
	 * @param player int, represent the player or COMPUTER by 0 or 1
	 * @param move int[], {row, col}
	 * @param depth int, the depth of the tree
	 * @return int, heuristic value for the move
	 */
	public static int heuristic(char[][] gameboard, int player, int[] move, int depth)
	{
		int h = 0;
		char pMarker = (player == 0 ? 'X' : 'O');
		//offensive: how many line-to-be the computer has?
		if (move[0] == -1 || move[1] == -1) //initial emptyMove
			h = -1;
		else
		{
			boolean colLine = true;
			boolean rowLine = true;
			for (int i=0; i<3; ++i)
			{
				if (gameboard[move[0]][i] == pMarker ||  gameboard[move[0]][i] == 0)
					colLine = colLine && true;
				else
					colLine = colLine && false; 
				
				
				if (gameboard[i][move[1]] == pMarker || gameboard[i][move[1]] == 0)
					rowLine = rowLine && true;
				else
					rowLine = rowLine && false;

			}
			
			if (colLine ) //any opponent marker will make value less than 2
			{
				if (player == COMPUTER)
					h += 1;
				else
					h -= 1;
			}
			
			if (rowLine)
			{
				if (player == COMPUTER)
					h += 1;
				else
					h -= 1;
			}
			
			
			boolean diaLine = true;
			
			if ((move[0] == 0 && move[1] == 0) || (move[0] == 1 && move[1] == 1) || (move[0] == 2 && move[1] == 2))
			{
				for (int i=0; i<3; ++i)
				{
					if (gameboard[i][i] == pMarker || gameboard[i][i] == 0)
						diaLine = diaLine && true; 
					else
						diaLine = diaLine && false;
				}
				if (diaLine)
				{
					if (player == COMPUTER)
						h += 1;
					else
						h -= 1;
				}
					
			}
			
			diaLine = true;
			if ((move[0] == 0 && move[1] == 2) || (move[0] == 1 && move[1] == 1) || (move[0] == 2 && move[1] == 0))
			{
				for (int i=0; i<3; ++i)
				{
					if (gameboard[i][2-i] == pMarker || gameboard[i][2-i] == 0)
						diaLine = diaLine && true; 
					else
						diaLine = diaLine && false;
				}
				if (diaLine)
				{
					if (player == COMPUTER)
						h += 1;
					else
						h -= 1;
				}
					
			}
		}
		return h;
	}
	
	
	/**
	 * minimax search, use backtracking to evaluate every possible next move, if the next move's heuristic value is bigger, take it as next-move-to-be
	 * @param gameboard char[][] gameboard
	 * @param depth int, the depth of the tree
	 * @param move int[], the move the player going to play
	 * @param player int, representing the player or COMPUTER by 0 or 1
	 * @return int[], the bestMove can make for player, {row, col}
	 */
	public static int[] miniMax(char[][] gameboard, int depth, int[] move, int player)
	{	
		int[] bestValue = new int[] {-1, -1, 0};
		
		if (player == COMPUTER)
		{
			bestValue[2] = -1000; //max
		}
		else
		{
			bestValue[2] = 1000; //min
		}
		
		int oppo = (player == 0 ? 1 : 0);
		
		

		ArrayList<int[]> freeSlots = freeSlots(gameboard);
		for (int[] fs : freeSlots)
		{
			if (isLegalMove(gameboard, fs[0], fs[1]))
			{
				char marker = (player == 0 ? 'X' : 'O');
				
				//place the next possible step
				gameboard[fs[0]][fs[1]] = marker;
				
				//if this step wins, return it
				if (playerWins(gameboard, player) && player == COMPUTER)
				{
					gameboard[fs[0]][fs[1]] = 0; //backtracking
					bestValue[0] = fs[0];
					bestValue[1] = fs[1];
					bestValue[2] = 10;
					return bestValue;
				}
				//if this step lose, stop and 
				else if (playerWins(gameboard, player) && player != COMPUTER)
				{
					gameboard[fs[0]][fs[1]] = 0;
					bestValue[0] = fs[0];
					bestValue[1] = fs[1];
					bestValue[2] = -10; 
					return bestValue;
				}
				//if this step gives draw, do it anyway.
				else if (isDraw(gameboard))
				{
					gameboard[fs[0]][fs[1]] = 0; 
					bestValue[0] = fs[0];
					bestValue[1] = fs[1];
					bestValue[2] = 0;
					return bestValue;
				}
				else
				{
					int[] m = new int[] {fs[0], fs[1]};
					
					int h = heuristic(gameboard, player, m, depth);
					
					
					int[] eval = miniMax(gameboard, depth-1,  m, oppo); //begin another minimax search on opponent
					gameboard[fs[0]][fs[1]] = 0; //backtracking
					
					if ( player == COMPUTER )
					{
						if (eval[2] + h > bestValue[2]) //maximize the min
						{
							bestValue[0] = fs[0];
							bestValue[1] = fs[1];
							bestValue[2] = eval[2] + h;
								
						}
					}
					else
					{	
						if (eval[2] < bestValue[2]) //minimize the max
						{
							bestValue[0] = fs[0];
							bestValue[1] = fs[1];
							bestValue[2] = eval[2] + h;
						}
					}
				}
			}
		}
		return bestValue;
	}
	
}
