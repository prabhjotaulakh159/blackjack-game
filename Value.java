// This enum defines all the default values of a playing card.
public enum Value {
	ACE("A", 1),
	TWO("2", 2),
	THREE("3", 3),
	FOUR("4", 4),
	FIVE("5", 5),
	SIX("6", 6),
	SEVEN("7", 7),
	EIGHT("8", 8),
	NINE("9", 9),
	TEN("10", 10),
	JACK("J", 10),
	QUEEN("Q", 10),
	KING("K", 10);

	// A value has a string representation for terminal display, and an integer value for calculating hands.
	private final String valueString;
	private final int valueInt;

	// Constructor
	private Value(String valueString, int valueInt) {
		this.valueString = valueString;
		this.valueInt = valueInt;
	}

	// Returns the string representation of a default value.
	public String getValueStr() {
		return this.valueString;
	}

	// Returns the integer value of a value.
	public int getValueInt() {
		return this.valueInt;
	}

	// This enum is contained in Card.java.
}
