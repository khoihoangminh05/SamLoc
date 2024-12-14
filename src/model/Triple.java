package model;

public class Triple extends Hand{

	private static final long serialVersionUID = 1L;

	public Triple(CardGamePlayer player, CardList card)
	{
		super(player,card);
	}
	
	public Card getTopCard()
	{
		this.sort();
		return this.getCard(2);
		
	}
	

	
	public boolean isValid()
	{
		if(this.size()  != 3) 
		{
			return false;
		}
		
		if(this.getCard(0).rank == this.getCard(1).rank && this.getCard(0).rank == this.getCard(2).rank) 
		{
			return true;
		}
		
		return false;
	}
	
	
	
	
	

	
	public String getType()
	{
		return new String("Triple"); 
	}

}
