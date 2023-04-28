// This enum defines all the default suits of a playing card.
public enum Suit {
	HEART("♥"),
	DIAMOND("♦"),
	SPADE("♠"),
	CLUB("♣");

	// Each suit has a string representation for terminal display.
	private final String suit;

	// Constructor
	private Suit(String suit) {
		this.suit = suit;
	}

	// Returns the string representation of a suit.
	public String getSuit() {
		return this.suit;
	}

	// This enum is contained in Card.java.
}
