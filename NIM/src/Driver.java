//Name: Linh Lam - Duc Nguyen
//Project 5 - MACHINE LEARNING
//Professor Scott Thede
//April 15th, 2018

//Main class: This project will involve writing an algorithm
//to teach the computer to win the game of NIM. 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

public class Driver {
	public static int start = 12;							//initial number of sticks
	public static int move = 4;								//maximum number of sticks a player can take in each turn
	public static int [][] Qtable = new int[move][start];	//Q-Value Table
	public static int [][] visit;							//Mark the visited states
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("WELCOME TO THE NIM GAME!");
		System.out.println("START GAME? (y/n): " );
		Scanner input = new Scanner (System.in);		

		String enter = input.nextLine().toLowerCase().trim();
		while ( !enter.equals("n") && !enter.equals("y") ) {
			System.out.println( "ENTER y/n TO START/QUIT!" );
			enter = input.nextLine().toLowerCase().trim();
		}
		
		if ( enter.equals("n") ) {
            System.out.println( "THANKS FOR PLAYING!" );
            System.exit(1);
        }
		while (enter.equals("y")) {
			

		// TODO Auto-generated method stub	
			String file;
			int total = start;
			//Scanner key=new Scanner(System.in);
			//System.out.println( "Enter trained data file: " );
			//file = key.nextLine();
			file = "trainedData";	//name of the trained data
			
			FileReader inFile = null;
			int user; 			//user's choice
			int computer = 1; 	//computer's choice
			visit = new int [move][start];
			try
			{
				inFile = new FileReader( file );
			}
			catch( FileNotFoundException e )
			{
				//System.out.println( "That file could not be found." );
				PrintWriter outputStream = new PrintWriter(file);
				outputStream.println("0");	//number of game played
				outputStream.println();
				outputStream.println(" 1 -1 -1 -1 0 0 0 0 0 0 0 0");
				outputStream.println("-1  1 -1 -1 0 0 0 0 0 0 0 0");
				outputStream.println("-1 -1  1 -1 0 0 0 0 0 0 0 0");
				outputStream.println("-1 -1 -1  1 0 0 0 0 0 0 0 0");
				inFile = new FileReader( file );
				outputStream.close();
				//System.out.println("Wrote new file");
			}
			
			//Load the number of game played			
			Scanner in = new Scanner( inFile );
			int gamePlayed = Integer.parseInt(in.nextLine());
			//System.out.println("Trained session: " + gamePlayed);

			//Load the Q-Table						
			String line;
	        int k = 0;
	        while ( in.hasNextLine( ) )
			{
	        	line = in.nextLine().trim();
	        	if (line.equals(""))
	        		line = in.nextLine().trim();
				String[] num = line.split( "\\s+" );
				for (int i = 0; i<start; i++) {
					Qtable[k][i] = Integer.parseInt(num[i]);
				}
				k++;
			}
	        
	        // PRINT Q TABLE
	        /*
	        for (int i = 0; i< move; i++){
	        	System.out.println();
	        	for (int j = 0; j< start; j++)
	        		System.out.print(Qtable[i][j] + " ");} 
	        */
	        	        
			System.out.println("There is a pile of " + total + " sticks. 2 players take turn to take " + move + " or less sticks from the pile. "
					+ "\nThe winner of the game is the player who takes the last stick.");
			System.out.println("______________________________________________________________________________________________________________");	
			while (total > 0) {
				//if a new game (not trained), then pick randomly

				if (gamePlayed == 0 && total > move ) {
					Random r = new Random();
					computer = r.nextInt(move)+1;
				}
				//else choose the move with the highest Q-Value
				else {
					int maxQ = Qtable[0][total-1];
					computer = 1;
					for (int i = 1; i<move; i++) {
						if (Qtable[i][total-1] > maxQ) {
							maxQ = Qtable[i][total-1];
							computer = i+1;
						}
					}
				}
				
				//System.out.println("a = " + computer + " s = "+ total);
				if (total-1 > 3)
					visit[computer-1][total-1] = 10;
				total = total - computer; // the total number of sticks left after the computer's turn
				System.out.println("The computer took " + computer + " stick(s). There are " + total + " remaining." );
				if (total == 0) {
					System.out.println("-----YOU LOST!-----");
					for (int i = 0; i< move; i++)
			        	for (int j = 0; j< start; j++)
			        		if (visit[i][j] == 10)
			        			Qtable[i][j]+= 1;
					break;
				}
				
				System.out.println("YOUR TURN!");
				System.out.println("There are " + total + " stick(s) left. Please enter an integer between 1 and " + move + ".");
				while(!input.hasNextInt()) {
					System.out.println("The input must be an integer. Try again!");
				    input.next();
				}
				user = input.nextInt();
				while ( user <= 0 || user > move || user > total ) {				
					System.out.println("Invalid number. Please enter an integer between 1 and "+ move +" that is smaller than or equal to the remaining sticks. \nThere are " + total + " stick(s) left.");
					while(!input.hasNextInt()) {
						System.out.println("The input must be an integer. Try again! There are " + total + " stick(s) left.");
					    input.next();
					}
					user = input.nextInt();
				}
				total = total - user; // the total number of sticks left after the user's turn
				System.out.println("You took " + user + " stick(s). There are " + total + " remaining.");
				if ( total == 0 ) {
					System.out.println("-----YOU WON!-----");
					for (int i = 0; i< move; i++)
			        	for (int j = 0; j< start; j++) 
			        		if (visit[i][j] == 10)
			        			Qtable[i][j]-= 1;
					break;
				
				}
				System.out.println("______________________________________________________________________________________________________________");
			}
			
			//update new trained data
			PrintWriter outputStream = new PrintWriter(file);
			outputStream.println(gamePlayed+1);
			outputStream.println();
			for (int i = 0; i< move; i++){
	        	for (int j = 0; j< start; j++)
	        			outputStream.print(Qtable[i][j] + " ");
	        	outputStream.println();
			}
			outputStream.close();
		
			FileReader inFile1 = new FileReader( file );
			Scanner in1 = new Scanner( inFile1 );

			gamePlayed = Integer.parseInt(in1.nextLine());

			//Qtable = new int[move][total];
						
			String line1;
	        int k1 = 0;
	        while ( in1.hasNextLine( ) )
			{
	        	line1 = in1.nextLine().trim();
	        	if (line1.equals(""))
	        		line1 = in1.nextLine().trim();
				String[] num = line1.split( "\\s+" );
				for (int i = 0; i<start; i++) {
					Qtable[k1][i] = Integer.parseInt(num[i]);
				}
				k1++;
			}
	        System.out.println("REPLAY? (y/n): " );
			input = new Scanner (System.in);		

			enter = input.nextLine().toLowerCase().trim();
			while ( !enter.equals("n") && !enter.equals("y") ) {
				System.out.println( "ENTER y/n TO START/QUIT!" );
				enter = input.nextLine().toLowerCase().trim();
			}
			
			if ( enter.equals("n") ) {
	            System.out.println( "THANKS FOR PLAYING!" );
	            in.close();
		        input.close();
		        in1.close();
	            System.exit(1);
	        }
	        /* 
	        // PRINT Q TABLE
	        for (int i = 0; i< move; i++){
	        	System.out.println();
	        	for (int j = 0; j< start; j++)
	        		System.out.print(Qtable[i][j] + " ");}
	        */	        
		}
	}
}

