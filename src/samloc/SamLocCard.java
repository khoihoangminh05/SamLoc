package samloc;
import model.*;

public class SamLocCard extends Card {


	private static final long serialVersionUID = 1L;


	public SamLocCard(int suit, int rank) {
		super(suit, rank);
		
	}


	public int compareTo(Card card)
	{
		
		int rank = this.rank;
		int card_rank = card.getRank();
		
		if(rank == 0)
		{
			rank = 13;
		}
		
		if(rank == 1)
		{
			rank = 14;
		}
		
		if(card_rank == 0)
		{
			card_rank = 13;
		}
		
		if(card_rank == 1)
		{
			card_rank = 14;
		}		
		
		if (rank > card_rank) {
			return 1;
		} 
		else if (rank < card_rank) {
			return -1;
		} 
		 
		
		
		else {
			return 0;
		}
	}
	
}