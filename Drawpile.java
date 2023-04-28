import java.util.Random;

// This class represents a drawpile from which a dealer will draw and deal cards.
public class Drawpile {
	// A drawpile is a pile of cards.
	private DynamicCardArray dynamicCardArray;

	// For shuffling.
	private Random random;

	// Constructor that generates 52 cards, and shuffles them.
	public Drawpile() {
		this.dynamicCardArray = new DynamicCardArray();
		this.random = new Random();
		this.generate52Cards();
		this.shuffle();
	}

	// Generates 13 unique value cards for each of the 4 suits.
	private void generate52Cards() {
		for (Suit suit : Suit.values()) {
			for (Value value : Value.values()) {
				this.dynamicCardArray.add(new Card(suit, value));
			}
		}
	}

	// Shuffles the 52 cards by using a swapping algorithm.
	private void shuffle() {
		for (int i = 0; i < this.dynamicCardArray.length(); i++) {
			swap(i);
		}
	}

	// Takes as input a current index. Swaps the card at the current index with a
	// card at a random index.
	private void swap(int index) {
		int randomIndex = random.nextInt(this.dynamicCardArray.length());
		Card temp = this.dynamicCardArray.get(index);
		this.dynamicCardArray.set(index, this.dynamicCardArray.get(randomIndex));
		this.dynamicCardArray.set(randomIndex, temp);
	}

	// Empties the drawpile, and re-generates 52 cards and shuffles them.
	public void reset() {
		this.dynamicCardArray.clear();
		this.generate52Cards();
		this.shuffle();
	}

	// Returns the card on top of the drawpile.
	public Card drawTop() {
		return this.dynamicCardArray.remove();
	}

	// This class is contained in Dealer.java
}
