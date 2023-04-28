// This class defines a simple playing card in blackjack.
public class Card {
	// A card has a default suit.
	private Suit suit;

	// A card has a default value.
	private Value value;

	// Constructor
	public Card(Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
	}

	// Returns the suit of the card for terminal display.
	public String getSuit() {
		return this.suit.getSuit();
	}

	// Returns the integer value of the card to calculate hands.
	public int getValueInt() {
		return this.value.getValueInt();
	}

	// Returns the string representation of a cards value for terminal display.
	public String getValueStr() {
		return this.value.getValueStr();
	}

	// Returns a string representation of a card.
	public String toString() {
		return "|" + this.suit.getSuit() + " " + this.value.getValueStr() + "|";
	}

	// This class is contained in DynamicCardArray.java
}
