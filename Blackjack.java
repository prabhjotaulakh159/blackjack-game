import java.util.Scanner;

// This is the application class.
public class Blackjack {
	public static void main(String[] args) {
		// Dependencies.
		Scanner reader = new Scanner(System.in);
		Table table = new Table();
		Dealer dealer = new Dealer();
		
		greet();
		setupTable(table, reader);
		
		// Game is over when all players leave/are kicked out or the dealer has no more
		// cards.
		while (table.isNotEmpty()) {
			try {
				startRound(reader, dealer, table);
				gameLoop(reader, dealer, table);
				endRound(dealer, table);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("The dealer has run out of cards or players to serve");
				break;
			}
		}
		System.out.println("Goodbye ! I had fun stealing your money :)");
	}

	// Simply prints a greeting.
	private static void greet() {
		System.out.println("----------------------------------------");
		System.out.println("Welcome to JAVA II Blackjack casino !");
		System.out.println("DISCLAIMER: Gambling is bad !");
		System.out.println("Your dealer's name is Oracle. Gambling is his guilty passion.");
		System.out.println("And it will be yours too if you keep coming back here.");
		System.out.println("Get ready to waste some money, folks !");
		System.out.println("----------------------------------------");
	}

	// Tell users to enter a number of players repeadtly until they enter a valid
	// input.
	private static void setupTable(Table table, Scanner reader) {
		System.out.println("We have a maximum table capacity of " + table.getMaxSize() + " people.");
		System.out.print("Please enter how many players you are this evening >>>> ");
		boolean validInput = false;
		while (!validInput) {
			validInput = askNumberPlayers(table, reader);
		}
	}

	// Validates the players input and returns whether or not they entered a valid
	// number of players.
	private static boolean askNumberPlayers(Table table, Scanner reader) {
		String input = "";
		int numberOfPlayers = 0;
		try {
			input = reader.next().trim();
			numberOfPlayers = Integer.parseInt(input);
			table.addBulk(numberOfPlayers); // add them to the table.
			return true;
		} catch (NumberFormatException e) {
			System.out.println("Sorry, but you entered something that was not a valid number between 1 and " + table.getMaxSize() + ".");
			System.out.print("Please try again >>>> ");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage()); // Message is in the Table class.
			System.out.print("Please try again >>>> ");
		}
		return false;
	}

	// This is the start of the round. These sequence of events are executed.
	private static void startRound(Scanner reader, Dealer dealer, Table table) {
		placeBets(reader, table);
		initialDeal(dealer, table);
		checkForBlackjacks(table);
		showGame(dealer, table);
	}

	// Asks each individual player to place a bet.
	private static void placeBets(Scanner reader, Table table) {
		for (int i = 0; i < table.length(); i++) {
			askBet(reader, table.get(i));
		}
	}

	// Repeadlty asks a player to place a bet until they enter a valid value.
	private static void askBet(Scanner reader, Player player) {
		System.out.println("-------------------------------------------------");
		System.out.println("Player #" + player.getIndex() + ", it is your turn to place your bet !");
		System.out.println("MINIMUM BETTING AMOUNT : " + player.getMinBet() + "$");
		System.out.println("AVAILABLE CASH TO BET  : " + player.getCash() + "$");
		System.out.print("Please enter your bet >>>> ");
		boolean validInput = false;
		while (!validInput) {
			validInput = askBetAmount(reader, player);
		}
	}

	// Validates input froms the user and sets their bet. Return true if they
	// entered a good value.
	private static boolean askBetAmount(Scanner reader, Player player) {
		try {
			String input = reader.next();
			double bet = Double.parseDouble(input);
			player.setBet(bet);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("Sorry, but you did not enter a valid number between " + player.getMinBet() + " and " + player.getCash() + ".");
			System.out.print("Please try again >>>> ");
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.out.print("Please try again >>>> ");
		}
		return false;
	}

	// Deals a 1 card to the dealer and 2 to each players.
	private static void initialDeal(Dealer dealer, Table table) {
		dealer.dealToSelf();
		for (int i = 0; i < table.length(); i++) {
			table.get(i).hit(dealer.deal());
			table.get(i).hit(dealer.deal());
		}
	}

	// Displays the current state of the game (dealers cards + value and all players
	// cards + state + value).
	private static void showGame(Dealer dealer, Table table) {
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println(dealer);
		System.out.println(table);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	// Goes through the table after the initial deal, and checks if any player have
	// gotten a blackjack. If yes, then we set their blackjack state to be true.
	private static void checkForBlackjacks(Table table) {
		for (int i = 0; i < table.length(); i++) {
			if (table.get(i).hasBlackjack()) {
				table.get(i).setBlackjack();
			}
		}
	}

	// Goes through each player one by one, and repeadtly asks them what they want
	// to do, until they bust, surrender, or stay.
	private static void gameLoop(Scanner reader, Dealer dealer, Table table) {
		for (int i = 0; i < table.length(); i++) {
			if (table.get(i).isParticipatingInRound()) {
				playTurn(reader, dealer, table.get(i), table);
			}
		}
	}

	// Represents an individual turn for a player. They are asked over and over what
	// they wish to do, and the game executes their commands (unless it is a false one).
	private static void playTurn(Scanner reader, Dealer dealer, Player player, Table table) {
		char input = ' ';
		while (player.isParticipatingInRound()) {
			System.out.print("Player #" + player.getIndex() + ", enter an option listed from above >>>> ");
			input = reader.next().trim().toUpperCase().charAt(0);
			executeInput(dealer, player, input, table);
			showGame(dealer, table);
			System.out.println(player.getPreviousAction()); // Logs of previous actions
		}

	}

	// Dependending on the input received, a certain method is called on the player object.
	private static void executeInput(Dealer dealer, Player player, char input, Table table) {
		if (input == 'H') {
			player.hit(dealer.deal());
		} else if (input == 'S') {
			player.stay();
		} else if (input == 'D') {
			player.doubleDown(dealer.deal());
		} else if (input == 'X') {
			player.surrender();
		} else if (input == 'I') {
			player.insure(dealer.getValue());
		} else if (input == 'A') {
			player.makeAceEleven();
		} else if (input == 'L') {
			player.leave();
		} else {
			player.setPreviousAction("PLAYER #" + player.getIndex() + " HAS ENTERED AN INVALID OPTION !"); // logs error message
		}
	}

	// When all players have played their turn, these sequence of events happen.
	private static void endRound(Dealer dealer, Table table) {
		dealerTurn(dealer);
		showHands(dealer, table);
		determineWins(dealer, table);
		showBalance(table);
		removePlayersWhodecidedToLeave(table);
		kickBrokePlayers(table);
		resetEverything(dealer, table);
	}

	// The dealers deal to himself until he gets over 16 (rules of the game).
	private static void dealerTurn(Dealer dealer) {
		while (dealer.gotSixteenOrUnder()) {
			dealer.dealToSelf();
		}
	}

	// Shows the hands of everyone (dealer + players).
	private static void showHands(Dealer dealer, Table table) {
		System.out.println("-------------------------------------------------");
		System.out.println("******* RESULTS: *******");
		System.out.println("Dealer hand: " + dealer.showHand() + "~ Points " + dealer.getValue());
		System.out.println();
		System.out.println(table);
		System.out.println("-------------------------------------------------");
	}

	// Determines how each player should be rewarded for their hand.
	// If the dealers busts, everyone wins (almost)
	private static void determineWins(Dealer dealer, Table table) {
		boolean dealerBusted = dealer.hasBusted();
		if (dealerBusted) {
			rewardAllPlayers(table);
		} else {
			rewardIndividually(table, dealer.getValue(), dealer);
		}
	}

	// If the dealer busted, all players who did not bust or surrender win their bets. Else, they lose.
	private static void rewardAllPlayers(Table table) {
		System.out.println("THE DEALER HAS BUSTED AND EVERYONE WHO WAS HAS NOT BUSTED/SURRENDERED WIN TWICE THEIR BETS !");
		for (int i = 0; i < table.length(); i++) {
			if (table.get(i).hasBlackjack()) {
				table.get(i).winBlackjack();
				System.out.println("PLAYER #" + table.get(i).getIndex() + " HAS GAINED " + table.get(i).getProfit() + "$");
			} else if (playerDidNotBustOrSurrender(table.get(i))) {
				table.get(i).win();
				System.out.println("PLAYER #" + table.get(i).getIndex() + " HAS GAINED " + table.get(i).getProfit() + "$");
			} else {
				table.get(i).lose();
				System.out.println("PLAYER #" + table.get(i).getIndex() + " HAS LOST " + table.get(i).getBet() + "$");
			}
		}
	}

	// Returns whether or not a player has busted or surrendered.
	private static boolean playerDidNotBustOrSurrender(Player player) {
		return !player.hasBusted() && !player.hasSurrendered();
	}

	// If the dealer did not bust, we evaluate each hand seperately. 
	// Anyone who busted or surrendered loses.
	// Anyone who has a value < than the dealer loses.
	// Anyone who has a blackjack wins. 
	// Anyone who has a hand equal to the dealers stalemates.
	// Anyone who is insured, and the dealer has 21 points and only 2 cards stalemates.
	// Anyone who has a value > than the dealer simple wins. 
	private static void rewardIndividually(Table table, int dealersValue, Dealer dealer) {
		final int ONLY_2_CARDS = 2;
		final int BLACKJACK = 21;
		for (int i = 0; i < table.length(); i++) {
			if (!playerDidNotBustOrSurrender(table.get(i))) { // inverse (!) condition. if they DID bust or surrender 
				table.get(i).lose();
				System.out.println("PLAYER #" + table.get(i).getIndex() + " HAS BUSTED/SURRENDERED AND LOST " + table.get(i).getBet() + "$");
			} else if (table.get(i).getValue() < dealersValue) {
				table.get(i).lose();
				System.out.println("PLAYER #" + table.get(i).getIndex() + " HAS A INFERIOR HAND AND LOST " + table.get(i).getBet() + "$");
			} else if (table.get(i).hasBlackjack()) {
				table.get(i).winBlackjack();
				System.out.println("PLAYER #" + table.get(i).getIndex() + " GOT BLACKJACK AND WON " + table.get(i).getProfit() + "$");
			} else if (table.get(i).getValue() == dealersValue) {
				System.out.println("PLAYER #" + table.get(i).getIndex() + " HAND IS EQUAL TO THE DEALER. THEY HAVE NOT WON OR LOST ANYTHING");
			} else if (table.get(i).isInsured() && dealersValue == BLACKJACK && dealer.getSizeOfHand() == ONLY_2_CARDS) {
				System.out.println("PLAYER #" + table.get(i).getIndex() + " GOT INSURANCE AND LOSES NOTHING");
			} else {
				table.get(i).win();
				System.out.println("PLAYER #" + table.get(i).getIndex() + " HAND IS SUPERIOR TO THE DEALER AND WON " + table.get(i).getProfit() + "$");
			}
		}
	}

	// Shows much money each player has left.
	private static void showBalance(Table table) {
		System.out.println("-------------------------------------------------");
		for (int i = 0; i < table.length(); i++) {
			System.out.println("PLAYER #" + table.get(i).getIndex() + " CASH BALANCE IS NOW " + table.get(i).getCash() + "$");
		}
		System.out.println("-------------------------------------------------");
	}

	// Checks if any player on the table decided to leave on the next round, and removes them from the table.
	private static void removePlayersWhodecidedToLeave(Table table) {
		for (int i = 0; i < table.length(); i++) {
			if (table.get(i).hasLeft()) {
				System.out.println("Player #" + table.get(i).getIndex() + " HAS DECIDED TO LEAVE (THEY MADE A GOOD DECISION)");
				table.remove(i);
				i = -1; // reset because removing also shifts.
			}
		}
	}

	// Removes all players who have less than the minimum bet amount.
	private static void kickBrokePlayers(Table table) {
		boolean someoneWasKicked = false;
		for (int i = 0; i < table.length(); i++) {
			if (table.get(i).isBroke()) {
				someoneWasKicked = true;
				System.out.println("Player #" + table.get(i).getIndex()
						+ " HAS BEEN KICKED FROM THE TABLE BECAUSE THEY HAVE LESS THAN THE MINIMUM BET AMOUNT LEFT IN THEIR BALANCE");
				table.remove(i);
				i = -1; // reset because remvoing also shifts
			}
		}
		if (!someoneWasKicked) {
			System.out.println("No player was kicked from the table");
			System.out.println("Everyone still has some money (for now).");
		}
	}

	// Resets everything and a new round starts in the while in the main method.
	private static void resetEverything(Dealer dealer, Table table) {
		dealer.reset();
		for (int i = 0; i < table.length(); i++) {
			table.get(i).reset();
		}
	}
}