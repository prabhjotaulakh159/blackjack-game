// This class represents a pile of cards that can change in length when we draw or add cards.
public class DynamicCardArray {
	// When the array is created, this is the default size.
	private final int ARRAY_DEFAULT_SIZE = 1000;

	// When the array will expand, it will multiply it's length by this amount.
	private final int EXPANSION_TIMES = 2;

	private Card[] cards;
	private int next;

	// Constructor.
	public DynamicCardArray() {
		this.cards = new Card[this.ARRAY_DEFAULT_SIZE];
		this.next = 0;
	}

	// Returns the length of the dynamic array (not the Card[]).
	public int length() {
		return this.next;
	}

	// Takes as input an index. Throws an exceptions if the index is outside next.
	// Returns the card at that index.
	public Card get(int index) {
		if (index >= this.next) {
			throw new ArrayIndexOutOfBoundsException(
					"Index " + index + " is out of bounds for length " + this.next);
		}
		return this.cards[index];
	}

	// Takes as input a card object. Appends the card object in the array at the
	// next available space.
	public void add(Card card) {
		if (this.next == this.cards.length - 1) {
			this.expand();
		}
		this.cards[this.next] = card;
		this.next++;
	}

	// Doubles the length of the array in case the array is full during our add()
	// method.
	private void expand() {
		Card[] expanded = new Card[this.cards.length * this.EXPANSION_TIMES];
		for (int i = 0; i < expanded.length; i++) {
			expanded[i] = this.cards[i];
		}
		this.cards = expanded;
	}

	// Returns the last card in the array. In theory, it would be the card on top of
	// a pile.
	public Card remove() {
		Card topCard = this.cards[this.next - 1];
		this.cards[this.next - 1] = null;
		this.next--;
		return topCard;
	}

	// Takes as input an index and a card object. Throws an exception if the index
	// is out of range. Sets the card object to be at that index.
	public void set(int index, Card card) {
		if (index >= this.next) {
			throw new ArrayIndexOutOfBoundsException(
					"Index " + index + " is out of bounds for length " + this.next);
		}
		this.cards[index] = card;
	}

	// Takes as input a default value to look for. Returns true or false whether
	// that a card of that value is in our array.
	public boolean contains(Value value) {
		// Using binary search, so we must sort. We do not want to sort the actual array
		// of cards itself.
		Card[] tempArray = new Card[this.next];
		fillTemporaryCardArray(tempArray);
		selectionSort(tempArray);
		return binarySearch(tempArray, value);
	}

	// Takes as input our temporary card array. Copies over the value of our current
	// array to the temporary array.
	private void fillTemporaryCardArray(Card[] tempArray) {
		for (int i = 0; i < this.next; i++) {
			tempArray[i] = this.cards[i];
		}
	}

	// Takes as input our temporary card array. Sorts the temporary array using
	// selection sort.
	private void selectionSort(Card[] tempArray) {
		int smallest = 0;
		for (int i = 0; i < tempArray.length; i++) {
			smallest = findSmallest(tempArray, i);
			swap(tempArray, smallest, i);
		}
	}

	// Takes as input our temporary card array and an index from where the seach
	// will start. Finds the index of the next smallest value within a certain
	// range.
	private int findSmallest(Card[] tempArray, int start) {
		int smallestIndex = start;
		for (int i = start; i < tempArray.length; i++) {
			if (tempArray[i].getValueInt() < tempArray[smallestIndex].getValueInt()) {
				smallestIndex = i;
			}
		}
		return smallestIndex;
	}

	// Takes as input our temporary card array, the index of the smallest value, and
	// our current index. Swaps the smallest valued card with the card at our
	// current index.
	private void swap(Card[] tempArray, int smallest, int index) {
		Card tempCard = tempArray[smallest];
		tempArray[smallest] = tempArray[index];
		tempArray[index] = tempCard;
	}

	// Takes as input our temporary array, and a default value. Returns true a card
	// with the same value is within our array.
	private boolean binarySearch(Card[] temporaryCardArray, Value value) {
		int left = 0;
		int right = temporaryCardArray.length - 1;
		int middle = (left + right) / 2;
		return cutAndSearch(left, right, middle, temporaryCardArray, value);
	}

	// Takes as input a left pointer, right pointer, middle point, a temporary card
	// array (sorted), and a default value.
	// Returns true if a card with that value is within our array. Implements actual
	// binary search algorith.
	private boolean cutAndSearch(int left, int right, int middle, Card[] temporaryCardArray, Value value) {
		while (left <= right) {
			if (isExactlyTheSame(value, temporaryCardArray[middle])) {
				return true;
			} else if (temporaryCardArray[middle].getValueInt() < value.getValueInt()) {
				left = middle + 1;
			} else {
				right = middle - 1;
			}
			middle = (left + right) / 2;
		}
		return false;
	}

	// Takes as input a default value and a card object. Returns true if the cards
	// integer and string representation matches the default values.
	private boolean isExactlyTheSame(Value value, Card card) {
		return card.getValueStr() == value.getValueStr() && card.getValueInt() == value.getValueInt();
	}

	// Empties out our dynamic card array.
	public void clear() {
		for (int i = this.next - 1; i >= 0; i--) {
			this.cards[next] = null;
		}
		this.next = 0;
	}

	// Returns a string representation of our dynamic card array.
	public String toString() {
		String builder = "";
		for (int i = 0; i < this.next; i++) {
			builder += this.cards[i] + " ";
		}
		return builder;
	}

	// This class is contained in Drawpile.java and Hand.java
}
