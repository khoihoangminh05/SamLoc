package model;

public class Straight extends Hand {


	private static final long serialVersionUID = 1L;
	
	public Straight(CardGamePlayer player, CardList card)
	{
		super(player,card);
	}
	
	public Card getTopCard()
	{
		this.sort();
		
		return this.getCard(this.size()-1);
	}

	
	public boolean isValid()
	{
		if(this.size() < 3) 
			return false;

		for(int i = 1; i < this.size() - 1; i++) {
			if(this.getCard(i-1).getRank()+1 != this.getCard(i).getRank()) return false;
		}
		
		if(this.getCard(this.size()-2).getRank() == 12 && this.getCard(this.size()-1).getRank() == 0) return true;
		
		if(this.getCard(this.size()-2).getRank() + 1 ==this.getCard(this.size()-1).getRank() ) return true;
		
		return false;
	}
	
	public String getType()
	{
		return "Straight"; 
	}
}