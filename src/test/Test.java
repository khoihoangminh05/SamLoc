package test;
import model.*;
import samloc.SamLocClient;
public class Test {

	public static void main(String[] args) {
		
		Card c1 = new Card(2,4);
		Card c2 = new Card(3,5);
		Card c3 = new Card(2,6);
		CardList cl1 = new CardList();
		cl1.addCard(c1);
		CardGamePlayer player1 = new CardGamePlayer();
		CardGamePlayer player2 = new CardGamePlayer();
		
		CardList cl2 = new CardList();
		cl2.addCard(c2);
		Single a = new Single(player1,cl1);
		
		Single b = new Single(player2,cl2);
		
		System.out.print(b.beats(a));
		
	}
 

}
