package model;

import java.io.Serializable;
import java.util.ArrayList;

public class CardList implements Serializable{
    
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Card> cards = new ArrayList<Card>();
	
	public void addCard(Card card) {
		if (card != null) {
			cards.add(card);
		}
	}
	
	public Card getCard(int i) {
		if (i >= 0 && i < cards.size()) {
			return cards.get(i);
		} else {
			return null;
		}
	}
	
	public Card removeCard(int i) {
		if (i >= 0 && i < cards.size()) {
			return cards.remove(i);
		} else {
			return null;
		}
	}
	
	public boolean removeCard(Card card) {
		return cards.remove(card);
	}
	
	public void removeAllCards() {
		cards = new ArrayList<Card>();
	}
	
	public Card setCard(int i, Card card) {
		if (i >= 0 && i < cards.size()) {
			return cards.set(i, card);
		} else {
			return null;
		}
	}
	
	public boolean contains(Card card) {
		return cards.contains(card);
	}
	
	public boolean isEmpty() {
		return cards.isEmpty();
	}
	
	public void sort() {
		cards.sort(null);
	}
	
	public int size() {
		return cards.size();
	}
	
	public String toString() {
		String string = "";
		if (cards.size() > 0) {
			for (int i = 0; i < cards.size(); i++) {
				string = string + "[" + cards.get(i) + "]";
				if (i != cards.size() - 1) {
					string = string + " ";
				}
			}
		} else {
			string = "[Empty]";
		}

		return string;
	}
}
