package model;

import java.io.Serializable;

public class Card implements Comparable<Card>,Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final char[] SUITS= {'\u2666', '\u2663', '\u2665', '\u2660' };
	private static final char[] RANKS= { 'A', '2', '3', '4', '5', '6', '7',
		'8', '9', '0', 'J', 'Q', 'K' };
	
	protected int suit;
	protected int rank;
	
	public int getSuit() {
		return suit;
	}
	
	public void setSuit(int suit) {
		this.suit = suit;
	}

	public int getRank() {
		return rank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public Card(int suit, int rank) {
		this.suit = suit;
		this.rank = rank;
	}
	
	public String toString() {
		if ((this.suit % 2 == 0)) {
			return "\u001B[31m" + SUITS[this.suit] + RANKS[this.rank] + "\u001B[0m";
		} else {
			return "" + SUITS[this.suit] + RANKS[this.rank];
		}
	}

	@Override
	public int compareTo(Card card) {
		if (this.rank > card.rank) {
			return 1;
		} else if (this.rank < card.rank) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public boolean equals(Object card) {
		return (this.rank == ((Card) card).getRank() && suit == ((Card) card)
				.getSuit());
	}
	
	public int hashCode() {
		return rank;
	}

}
