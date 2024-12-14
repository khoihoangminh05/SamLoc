package model;

public class Hand extends CardList {

	private static final long serialVersionUID = 1L;
	
	private CardGamePlayer player;
	
	public Hand(CardGamePlayer player, CardList cards)
	{
		this.player = player;
		
		for(int i = 0; i < cards.size();i++)
		{
			this.addCard(cards.getCard(i));
		}
	}
	
	public CardGamePlayer getPlayer()
	{
		return this.player;
		
	}
	
	public Card getTopCard()
	{
		return null;
		
	}
	
	public boolean beats(Hand hand)
	{
		System.out.println(this.getType());
		System.out.println(hand.getType());
		
		if(this.getType().equals(hand.getType()) && this.size() == hand.size()) {
			if(this.getTopCard().compareTo(hand.getTopCard()) == 1) {
				return true;
			}
		}
		
		return false;
	}
	
	
	
	
	
	
	
	public boolean isValid()
	{
		return false;
	}
	
	
	public String getType()
	{
		return null;
	}

}
