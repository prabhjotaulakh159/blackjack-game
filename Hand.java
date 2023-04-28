// This card represents both a dealers and players hand, which has points.
public class Hand {
	// If the player chooses to make an ace to be 11 points, we add this amount to
	// the hand.
	private final int AMOUNT_IF_ACE_ELEVEN = 10;

	// A hand value over this amount is considered a bust.
	private final int BUST_AMOUNT = 21;

	// A hand is a pile of cards.
	private DynamicCardArray dynamicCardArray;

	// It has a value assigned to it.
	private int value;

	// Constructor.
	public Hand() {
		this.dynamicCardArray = new DynamicCardArray();
		this.value = 0;
	}

	// Returns the value of the hand.
	public int getValue() {
		return this.value;
	}

	// Returns how many cards there are in the hand.
	public int getSize() {
		return this.dynamicCardArray.length();
	}

	// Takes as input a card object. Appends it to the hand and updates the value.
	public void updateHand(Card card) {
		this.dynamicCardArray.add(card);
		this.value += card.getValueInt();
	}

	// Returns true if the current hand has an ace.
	public boolean hasAce() {
		return this.dynamicCardArray.contains(Value.ACE);
	}

	// Returns true if the current hand has a jack.
	private boolean hasJack() {
		return this.dynamicCardArray.contains(Value.JACK);
	}

	// Adds 10 points to the current hand in case a player chooses an 11-point-ace.
	public void incrementAceValue() {
		this.value += this.AMOUNT_IF_ACE_ELEVEN;
	}

	// Returns true if the current hand is a blackjack.
	public boolean checkBlackjack() {
		if (hasValidBJConditions()) {
			this.incrementAceValue();
			return true;
		}
		return false;
	}

	// Returns true if the current hand meets the requirements for a blackjack.
	private boolean hasValidBJConditions() {
		final int ONLY_2_CARDS = 2;
		return this.hasAce() && this.hasJack() && this.dynamicCardArray.length() == ONLY_2_CARDS;
	}

	// Returns true if the current hand is considered a bust.
	public boolean checkBust() {
		return this.value > this.BUST_AMOUNT;
	}

	// Empties the hand and resets the value.
	public void reset() {
		this.dynamicCardArray.clear();
		this.value = 0;
	}

	// Returns as a string only the cards.
	public String getOnlyCards() {
		return this.dynamicCardArray.toString();
	}

	// Returns as a string both the cards and the amount of points of the hand.
	public String toString() {
		return this.dynamicCardArray + "Value is " + this.value;
	}

	// This class is contained in Dealer.java and Player.java
}
