package model;

public class Quad extends Hand {
	
	private static final long serialVersionUID = 1L;
	
	public Quad(CardGamePlayer player, CardList card)
	{
		super(player,card);
	}
	
	public Card getTopCard()
	{
		this.sort(); 
		return this.getCard(3);
	}
	
	public boolean isValid()
	{
		if(this.size() != 4)
		{
			return false;
		}
		
		this.sort();
		
		if(this.getCard(0).getRank() == this.getCard(3).getRank()) return true;
		
		return false;
	}
	public String getType()
	{
		return "Quad"; 
	}
}