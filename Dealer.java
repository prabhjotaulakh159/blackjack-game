// This class represents a dealer who draws from a pile and serves the players.
public class Dealer {
	// In blackjack, if a dealers hand is underneath this value, they must draw
	// again.
	private final int VALUE_DRAW_AGAIN = 16;

	// The dealer is in charge of a drawpile.
	private Drawpile drawpile;

	// The dealer also has a hand.
	private Hand hand;

	// Constructor
	public Dealer() {
		this.drawpile = new Drawpile();
		this.hand = new Hand();
	}

	// Adds a card to the dealers hand. Turns any Ace into 11-point-aces.
	public void dealToSelf() {
		Card fromPile = this.drawpile.drawTop();
		this.hand.updateHand(fromPile);
		makeAce11(fromPile);
	}

	// Takes as input a card object, and if it is an ace, it will make it an
	// 11-point-ace.
	private void makeAce11(Card card) {
		if (card.getValueInt() == Value.ACE.getValueInt()) {
			this.hand.incrementAceValue();
		}
	}

	// Returns a card from on top of the drawpile to serve to players.
	public Card deal() {
		return this.drawpile.drawTop();
	}

	// Returns true if the dealer's hand is below 16.
	public boolean gotSixteenOrUnder() {
		return this.hand.getValue() <= this.VALUE_DRAW_AGAIN;
	}

	// Returns the value of the dealers hand.
	public int getValue() {
		return this.hand.getValue();
	}

	// Returns true if the dealer has a hand value over 21 (busted).
	public boolean hasBusted() {
		return this.hand.checkBust();
	}

	// Resets the dealers drawpile and hand.
	public void reset() {
		this.drawpile.reset();
		this.hand.reset();
	}

	// Returns a string that display only the cards of the dealer.
	public String showHand() {
		return this.hand.getOnlyCards();
	}

	// Returns how many cards a dealer has.
	public int getSizeOfHand() {
		return this.hand.getSize();
	}

	// Returns a very handsome string representation of the dealer.
	public String toString() {
		String builder = "";
		builder += "---------    Press H to hit" + "\n";
		builder += "|*     *|    Press S to stay" + "\n";
		builder += "|   >   |    Press D to double down" + "\n";
		builder += "|  \\_/  |    Press X to surrender" + "\n";
		builder += "---------    Press I to insure" + "\n";
		builder += "   | |       Press A to make your Ace an 11 (if you have one)" + "\n";
		builder += "   | |       Press L to leave to leave in the next round (Current hand will still be evaluated)"
				+ "\n";
		builder += "---------    " + "\n";
		builder += "Dealer ~ " + this.hand + "\n";
		return builder;
	}

	// This class is a dependency in Blackjack.java
}
