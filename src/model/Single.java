package model;

public class Single extends Hand{

	private static final long serialVersionUID = 1L;

	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	public Card getTopCard()
	{
		return this.getCard(0); 
	}
	
	public boolean isValid()
	{
		if(this.size() == 1) { 
			return true; 
		}
		
		return false;
	}
	

	
	public String getType()
	{
		return new String("Single"); 
	}

}
