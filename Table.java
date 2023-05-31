// This class represents a table full of players. They can leave, or get kicked out.
public class Table {
    // When the array is created, this is the default size.
    private final int ARRAY_DEFAULT = 1000;

    // When the array will expand, it will multiply it's length by this amount.
    private final int EXPANSION = 2;

    // A table has a max capacity of this amount.
    private final int MAXIMUM_CAPACITY = 4;

    private Player[] players;
    private int next;

    // Constructor
    public Table() {
        this.players = new Player[this.ARRAY_DEFAULT];
        this.next = 0;
    }

    // Takes as input a number of players, and adds them to the table.
    public void addBulk(int numberOfPlayers) {
        if (numberOfPlayers <= 0 || numberOfPlayers > this.MAXIMUM_CAPACITY) {
            throw new IllegalArgumentException(
                    "Sorry, but the number of players must be between 1 and " + this.MAXIMUM_CAPACITY + ".");
        }
        if (this.next == this.players.length - 1) {
            expand();
        }
        addAllPlayersOneByOne(numberOfPlayers);
    }

    // Doubles the length of the array in case the array is full during our
    // addBulk() method.
    private void expand() {
        Player[] expanded = new Player[this.players.length * this.EXPANSION];
        for (int i = 0; i < expanded.length; i++) {
            expanded[i] = this.players[i];
        }
        this.players = expanded;
    }

    // Takes as input a number of players, and adds that many players to the table.
    private void addAllPlayersOneByOne(int numberOfPlayers) {
        for (int i = 0; i < numberOfPlayers; i++) {
            this.players[i] = new Player(i + 1);
        }
        this.next += numberOfPlayers;
    }

    // Takes as input an index. Throws an exception if the index is out of bounds.
    // Removes the player at that index.
    public void remove(int index) {
        if (index >= this.next) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        for (int i = index; i < this.next; i++) {
            this.players[i] = this.players[i + 1];
        }
        this.next--;
    }

    // Returns how many players are on the table.
    public int length() {
        return this.next;
    }

    // Takes as input an index. Throws an exception of the index is out of bounds.
    // Returns the player at that index.
    public Player get(int index) {
        if (index >= this.next) {
            throw new ArrayIndexOutOfBoundsException("Index " + index + " is out of bounds.");
        }
        return this.players[index];
    }

    // Returns the maximum capacity of the table.
    public int getMaxSize() {
        return this.MAXIMUM_CAPACITY;
    }

    // Returns true if there is atleast 1 player on the table.
    public boolean isNotEmpty() {
        return this.next > 0;
    }

    // Returns a string representation of all the players.
    public String toString() {
        String builder = "";
        for (int i = 0; i < this.next - 1; i++) {
            builder += this.players[i] + "\n" + "\n";
        }
        return builder += this.players[this.next - 1];
    }

    // This class is a dependency in Blackjack.java
}
