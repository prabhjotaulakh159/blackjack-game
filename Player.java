// This class represents a player that sits on a table and bets.
public class Player {
	private final int MIN_BET = 25;
	private final int START_CASH = 250;

	// If a player wins or losses, they're bet is multiplied by these amounts.
	private final int REWARD_NORMAL = 2;
	private final double REWARD_BLACKJACK = 1.5;
	private final double SURRENDER_AMOUNT = 0.5;

	// A player has a number attached to them.
	private int index;

	// Financial information.
	private double bet;
	private double cash;
	private double profit;

	// Current state of the player.
	private boolean participatingInRound;
	private boolean surrendered;
	private boolean gotBlackjack;
	private boolean busted;
	private boolean insured;
	private boolean left;

	// A player's previous action is recorded and outputted on the terminal.
	private String previousAction;

	private Hand hand;

	// Constructor. Throws an exception if index is negative or 0. Also sets the
	// default state of the player.
	public Player(int index) {
		if (index <= 0) {
			throw new IllegalArgumentException("A player cannot have a negative number associated to him/her.");
		}
		this.index = index;
		this.cash = this.START_CASH;
		this.bet = 0;
		this.profit = 0;
		this.participatingInRound = true;
		this.surrendered = false;
		this.gotBlackjack = false;
		this.busted = false;
		this.left = false;
		this.previousAction = "";
		this.hand = new Hand();
	}

	// Returns the players index.
	public int getIndex() {
		return this.index;
	}

	// Returns the players bet.
	public double getBet() {
		return this.bet;
	}

	// Returns how much cash a player has left.
	public double getCash() {
		return this.cash;
	}

	// Returns how much profit a player has made.
	public double getProfit() {
		return this.profit;
	}

	// Returns minimum betting amount for a player.
	public int getMinBet() {
		return this.MIN_BET;
	}

	// Returns whether or not a player is currently active in the round.
	public boolean isParticipatingInRound() {
		return this.participatingInRound;
	}

	// Returns what player's previous action was.
	public String getPreviousAction() {
		return this.previousAction;
	}

	// Returns the value of the players hand.
	public int getValue() {
		return this.hand.getValue();
	}

	// Returns true if the players hand is a blackjack.
	public boolean hasBlackjack() {
		return this.hand.checkBlackjack();
	}

	// Returns whether or not a player has surrendered.
	public boolean hasSurrendered() {
		return this.surrendered;
	}

	// Returns whether or not a player has busted.
	public boolean hasBusted() {
		return this.hand.checkBust();
	}

	// Returns whether or not a player has decided to leave.
	public boolean hasLeft() {
		return this.left;
	}

	// Returns whether or not a player is insured against the dealer.
	public boolean isInsured() {
		return this.insured;
	}

	// Takes as input a string, and sets the previous action of the player to that
	// string.
	public void setPreviousAction(String previousAction) {
		this.previousAction = previousAction;
	}

	// Returns whether or not the player can still play.
	public boolean isBroke() {
		return this.cash < this.MIN_BET;
	}

	// Takes as input a int bet amount. Throws an exception if the amount is too
	// little or above the players cash balance. Sets the bet of the player.
	public void setBet(double bet) {
		if (bet < this.MIN_BET || bet > this.cash) {
			throw new IllegalArgumentException("Invalid bet: It must be greater or equal to " + this.MIN_BET
					+ " and less or equal to " + this.cash + ".");
		}
		this.bet = bet;
	}

	// Sets the players state to have a blackjack and not participating in the
	// current round. Sets previous action accordingly.
	public void setBlackjack() {
		this.gotBlackjack = true;
		this.participatingInRound = false;
		this.previousAction = "PLAYER " + this.index + " HAS GOTTEN A BLACKJACK !";
	}

	// Sets the player profit and cash to what it would be if they got a blackjack.
	public void winBlackjack() {
		this.profit = this.bet * this.REWARD_BLACKJACK;
		this.cash += this.profit;
	}

	// Sets the players profit and cash to what it would be if they won normally.
	public void win() {
		this.profit = (this.bet * this.REWARD_NORMAL);
		this.cash += this.profit;
	}

	// Subtracts a players bet from his/her cash balance.
	public void lose() {
		this.cash -= this.bet;
	}

	// Takes as input a card object. Adds the card to the hand, and checks for a
	// bust.
	public void hit(Card card) {
		this.hand.updateHand(card);
		if (this.hand.checkBust()) {
			this.participatingInRound = false;
			this.busted = true;
			this.previousAction = "PLAYER #" + this.index + " BUSTED";
		} else {
			this.previousAction = "PLAYER #" + this.index + " HIT AND RECEIVED A " + card;
		}
	}

	// Skips the players turn.
	public void stay() {
		this.participatingInRound = false;
		this.previousAction = "PLAYER #" + this.index + " DECIDED TO SKIP THEIR TURN";
	}

	// Takes as input a card object. Throws an exception if the player does not have
	// enough cash to double their bets. Doubles the bets and adds a card to their
	// hand. Skips their turn.
	public void doubleDown(Card card) {
		double doubleBet = this.bet * 2;
		if (doubleBet > this.cash) {
			this.previousAction = "PLAYER #" + this.index
					+ " ATTEMPTED TO DOUBLE DOWN, BUT DOES HAVE ENOUGH CASH FOR IT";
		} else {
			this.hand.updateHand(card);
			this.bet = this.bet * 2;
			this.previousAction = "PLAYER #" + this.index + " DOUBLED THEIR BET TO " + this.bet + " HAS RECEIVED A " + card;
			this.participatingInRound = false;
		}
	}

	// Takes as input the value of the dealers hand. Gives the player insurance
	// against the dealer's possibility of getting a blackjack.
	public void insure(int dealerValue) {
		final int valueOfDealerHandIfTheyOnlyHaveAnAce = 11;
		if (dealerValue == valueOfDealerHandIfTheyOnlyHaveAnAce) {
			this.insured = true;
			this.previousAction = "PLAYER # " + this.index + "  GOT INSURANCE: IF THE DEALER HAS AN ACE AND A 10 POINT CARD, THEY LOSE NOTHING";
		} else {
			this.previousAction = "PLAYER # " + this.index + " ATTEMPTED TO GET INSURANCE, BUT THE DEALER DOES NOT HAVE A SINGLE ACE CARD";
		}
	}

	// Skips the players turn, and they lose half of their bet.
	public void surrender() {
		this.participatingInRound = false;
		this.surrendered = true;
		this.previousAction = "PLAYER #" + this.index + " SURRENDERED AND LOST 1/2 OF THEIR BET";
		this.bet = this.bet * this.SURRENDER_AMOUNT;
	}

	// Turns a players 1 point ace into an 11-point-ace.
	public void makeAceEleven() {
		if (this.hand.hasAce()) {
			this.hand.incrementAceValue();
			this.previousAction = "PLAYER #" + this.index + " TURNED THEIR 1-POINT-ACE INTO AN 11-POINT-ACE AND NOW HAS A HAND VALUE OF " + this.hand.getValue();
		} else {
			this.previousAction = "PLAYER #" + this.index + " ATTEMPTED TO TURN THEIR 1-POINT-ACE INTO AN 11-POINT-ACE, BUT THEY DID NOT HAVE AN ACE";
		}
	}

	// Sets the state of the player to leave on the next round.
	public void leave() {
		if (this.left) {
			this.previousAction = "PLAYER #" + this.index + ", I KNOW YOU WANT TO LEAVE, BUT ATLEAST FINISH THIS ROUND PLEASE";
		} else {
			this.left = true;
			this.previousAction = "PLAYER #" + this.index + " WILL LEAVE NEXT ROUND TO DO SOMETHING BETTER!";
		}
	}

	// Sets the players state to their default.
	public void reset() {
		this.hand.reset();
		this.participatingInRound = true;
		this.surrendered = false;
		this.gotBlackjack = false;
		this.busted = false;
		this.insured = false;
		this.left = false;
		this.previousAction = "";
		this.bet = 0;
	}

	// Returns a string representation of a player.
	public String toString() {
		String builder = "Player " + this.index + " ~ " + "Bet: " + this.bet + "$" + "\n" + this.hand;
		builder = buildDependingOnCurrentPlayerConditions(builder);
		return builder;
	}

	// Returns a string to add to the players representation according to their
	// current state.
	private String buildDependingOnCurrentPlayerConditions(String builder) {
		if (this.left) {
			builder += " WILL LEAVE NEXT ROUND";
		}
		if (this.busted) {
			builder += " BUSTED";
		}
		if (this.surrendered) {
			builder += " SURRENDERED";
		}
		if (this.gotBlackjack) {
			builder += " GOT BLACKJACK AND WON 1.5X HIS BET !";
		}
		return builder;
	}

	// This class is contained in Table.java
}
