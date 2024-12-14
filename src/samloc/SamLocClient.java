package samloc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.*;

public class SamLocClient {
	
	private int numberOfPlayer;
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int playerID; 
	private String playerName; 
	private int currentIdx; 
	private String serverIP;  
	private int serverPort;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private SamLocTable table;
	private Card minCard;
	private boolean isBot;
	
     public SamLocClient() {
    	 
    	playerList = new ArrayList<CardGamePlayer>();
 		
 		for (int i = 0; i < 4; i++)
 		{
 			playerList.add(new CardGamePlayer());
 		}		
 		handsOnTable = new ArrayList<Hand>();
    	table = new SamLocTable(this);
    	 makeConnect();
     }
     
     public void makeConnect() {
    	 try {
    		 InetAddress ip = InetAddress.getLocalHost();
             String serverIP = ip.getHostAddress(); 
             System.out.println(serverIP);
         } catch (UnknownHostException e) {
             e.printStackTrace();
         }
    	 
    	 
    	 serverPort = 1234;
    	 
    	 try {
    		 socket = new Socket(serverIP, serverPort);
  
    	 } catch(Exception ex) {
    		 ex.printStackTrace();
    	 }
    	 
    	 try
 		{
 			oos = new ObjectOutputStream(socket.getOutputStream());
 			ois = new ObjectInputStream(socket.getInputStream());
 		} catch (IOException exception)
 		{
 			exception.printStackTrace();
 		}
    	 
    	 Thread t = new Thread(new ServerHandler());
    	 t.start();
    	
     }
    public Card getMinCard()
  	{
  		return minCard;
  	}

  	public void setMinCard(Card minCard)
  	{
  		this.minCard = minCard;
  	}
     public int getPlayerID()
 	{
 		return playerID;
 	}

 	public void setPlayerID(int playerID)
 	{
 		this.playerID = playerID;
 	}

 	public String getPlayerName()
 	{
 		return playerName;
 	}

 	public void setPlayerName(String playerName)
 	{
 		playerList.get(playerID).setName(playerName);
 		this.playerName = playerName;
 	}

 	public String getServerIP()
 	{
 		return serverIP;
 	}

 	public void setServerIP(String serverIP)
 	{
 		this.serverIP = serverIP;
 	}

 	public int getServerPort()
 	{
 		return serverPort;
 	}
 	
 	public void setServerPort(int serverPort)
 	{
 		this.serverPort = serverPort;
 	}
 	public int getNumberOfPlayer(){
 		return numberOfPlayer;
 	}
 	public void setNumberOfPlayer(int numberofplayer) {
 		this.numberOfPlayer = numberofplayer;
 	}
 	public ArrayList<CardGamePlayer> getPlayerList() 
	{
		return playerList;
	}

	public ArrayList<Hand> getHandsOnTable() 
	{
		return handsOnTable;
	}

	public int getCurrentIdx()
	{
		return currentIdx;
	}
	
	public void setIsBot(boolean isBot) {
		this.isBot = isBot;
	}
	
	public boolean checkBot() {
		return isBot;
	}
 	

     public void parseMessage(GameMessage message) throws InterruptedException {
    	if(message.getType() == GameMessage.PLAYER_LIST) {
 			playerID = message.getPlayerID();
 			table.setActivePlayer(playerID);
 			this.setNumberOfPlayer(((String[]) message.getData()).length);
 			table.setNumberOfPlayer(numberOfPlayer);
 			for (int i = 0; i < numberOfPlayer; i++)
 			{
 				if (((String[])message.getData())[i] != null)
 				{
 					this.playerList.get(i).setName(((String[])message.getData())[i]);
 					table.setExistence(i);
 				}
 			}
 			
 		} 
    	else if(message.getType() == GameMessage.MSG) {
    		 table.printChatMSG((String)message.getData());
    	 }
    	else if(message.getType() == GameMessage.JOIN) {
			playerList.get(message.getPlayerID()).setName((String)message.getData());
			table.setExistence(message.getPlayerID());
		}
    	else if(message.getType() == GameMessage.READY){
			table.printChatMSG("Player " + message.getPlayerID() + " is ready now!\n");
			handsOnTable = new ArrayList<Hand>();
			//table.repaint();
		}
    	else if(message.getType() == GameMessage.START){
			start((SamLocDeck)message.getData());
			table.printChatMSG("Game has started!\n\n");
		}
    	else if(message.getType() == GameMessage.QUIT) {
    		table.setNotExistence(message.getPlayerID());
    	}
    	else if(message.getType() == GameMessage.MOVE)
		{
			checkMove(message.getPlayerID(), (int[])message.getData());
			table.repaint();
		}
     }
     
     public void start(Deck deck) {
 		this.deck = deck;
 		
 		for (int i = 0; i < numberOfPlayer; i++)
 		{
 			playerList.get(i).removeAllCards();
 		}
 		
 		for (int i = 0; i < numberOfPlayer; i++)
 		{
 			for (int j = 0; j < 10; j++)
 			{
 				getPlayerList().get(i).addCard(this.deck.getCard(i*10+j));
 			}
 		}
 		
 		
 		for (int i = 0; i < numberOfPlayer; i++)
 		{
 			getPlayerList().get(i).getCardsInHand().sort();
 		}
 		
 		minCard = null;
 		
 		for (int i = 0; i < numberOfPlayer; i++)
 		{
 			for(int j = 0; j < playerList.get(i).getCardsInHand().size(); j++) {
 				Card card = playerList.get(i).getCardsInHand().getCard(j);
 				if(minCard == null) {
 					setMinCard(card);
 					currentIdx = i;
 				} 
 				else if(card.compareTo(getMinCard()) == -1) {
 					currentIdx = i;
 					setMinCard(card);
 				}
 			}
 			
 		}
 		System.out.println(playerList.get(playerID).getCardsInHand());
 		
 		System.out.println(currentIdx  + " " + minCard );
 		table.repaint();
 		table.setActivePlayer(playerID);
 		table.enable();
 		
 		if(isBot) {
 			if(playerID == currentIdx) {
 				makeMoveAutomatically();
 			}
 		}
 	}
     
     public void sendMessage(GameMessage message){
    	 
    	 try {
    	   oos.writeObject(message);
    	  } catch (Exception ex) {
    		 ex.printStackTrace();
    	 }
     }
     
     class ServerHandler implements Runnable{
    	 
    	public ServerHandler() {
    		
    	}
    	 
		@Override
		public void run() {
			// TODO Auto-generated method stub
            GameMessage message = null;
			try
			{
				while ((message = (GameMessage) ois.readObject()) != null)
				{
					parseMessage(message);
					System.out.println("Accepting messages Now");
				}
			} 
			
			catch (Exception exception) 
			{
				exception.printStackTrace();
			}
			
		}
    	 
     }
   
    public void makeMoveAutomatically() {
    	
    	int numOfHandsPlayed=handsOnTable.size();
    	if(numOfHandsPlayed == 0) {
    		 int[] cardIdx = {0};
    		 makeMove(playerID, cardIdx);
    	} 
    	 else {
    		 
    		 CardList playerSelectedCards =new CardList();
			 Hand playerHand;
			 
    		 if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName()==playerList.get(currentIdx).getName()) {
  				
    			int size = playerList.get(currentIdx).getCardsInHand().size(); 	
    			for(int i=0; i<size ; i++) {
    				
    				// straight
    				if(i + 2 < size) {
    					playerSelectedCards.removeAllCards();
    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+2) );
    					
    					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
    					if(playerHand != null) {
    						int[] cardIdx = {i,i+1,i+2};
    						makeMove(playerID, cardIdx );
    						return;
    					}
    				}
    				
    				// pair 
    				
    				if(i + 1 < size) {
    					playerSelectedCards.removeAllCards();
    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
    					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
    					
    					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
    					if(playerHand != null) {
    						int[] cardIdx = {i,i+1};
    						makeMove(playerID, cardIdx );
    						return;
    					}
    				}
    			}
    			
    			playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(0) );
  				playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
  				int[] cardIdx = {0};
				makeMove(playerID, cardIdx );
				return;
    		 } else {
    			 
    			int size = playerList.get(currentIdx).getCardsInHand().size(); 	
     			for(int i=0; i<size ; i++) {
     				
     				// straight
     				if(i + 2 < size) {
     					playerSelectedCards.removeAllCards();
     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+2) );
     					
     					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
     					if(playerHand != null ) {
     						if (playerHand.beats(handsOnTable.get(handsOnTable.size()-1))==true) {
     						int[] cardIdx = {i,i+1,i+2};
     						makeMove(playerID, cardIdx );
     						return;
     						}
     					}
     				}
     				
     				// pair 
     				
     				if(i + 1 < size) {
     					playerSelectedCards.removeAllCards();
     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i+1) );
     					
     					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
     					if(playerHand != null ) {
     						if (playerHand.beats(handsOnTable.get(handsOnTable.size()-1))==true) {
     						int[] cardIdx = {i,i+1};
     						makeMove(playerID, cardIdx );
     						return;
     						}
     					}
     				}
     				if(i < size) {
     					playerSelectedCards.removeAllCards();
     					playerSelectedCards.addCard( playerList.get(currentIdx).getCardsInHand().getCard(i) );
     					
     					playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
     					if(playerHand != null ) {
     						if (playerHand.beats(handsOnTable.get(handsOnTable.size()-1))==true) {
     						int[] cardIdx = {i};
     						makeMove(playerID, cardIdx );
     						return;
     						}
     					}
     				}
     				
     			}
     			
     			makeMove(playerID, null);
    		 }
    	}
    }
     
    public void makeMove(int playerID, int[] cardIdx) 
 	{  
 		GameMessage message = new GameMessage(6, playerID, cardIdx);
 		sendMessage(message);
 	}
     
 	public void checkMove(int playerID, int[] cardIdx) throws InterruptedException
 	{
        System.out.println(cardIdx);
 		int numOfHandsPlayed=handsOnTable.size();
 		
 		if(cardIdx==null)
 		{
 			if(numOfHandsPlayed==0)
 			{
 				table.printChatMSG("Not a legal move!\n");
 			}
 			
 			else if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName()==playerList.get(currentIdx).getName())
 			{
 				table.printChatMSG("Not a legal move!\n");
 			}
 			
 			else
 			{
 				table.printChatMSG(playerList.get(currentIdx).getName()+": "+"{Pass}\n");
 				
 				if (currentIdx!=numberOfPlayer-1)
 				{
 					++currentIdx;
 				}
 				
 				else
 				{
 					currentIdx=0;
 				}
 				table.printChatMSG(this.getPlayerList().get(currentIdx).getName()+" Please make a move! \n");
 			}
 		}
 		
 		else 
 		{
 			if(numOfHandsPlayed==0)
 			{
 				CardList playerSelectedCards =new CardList();
 				Hand playerHand;
 				
 				for(int i=0; i<cardIdx.length; ++i)
 				{
 					playerSelectedCards.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));	
 				}
 				
 				playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
 				
 				if(playerHand==null)
 				{
 					table.printChatMSG("Not a legal move!\n");
 				}
 				
 				else
 				{
 					playerHand.sort();
 					
 					if(minCard.compareTo(playerHand.getCard(0)) != 0)
 					{
 						table.printChatMSG("Not a legal move!\n");
 					}
 					
 					else
 					{
 						
 						table.printChatMSG(playerList.get(currentIdx).getName()+": "+"{"+playerHand.getType()+"}");
 						
 						for (int j=0; j<playerHand.size(); ++j)
 						{
 							table.printChatMSG(" ["+playerHand.getCard(j).toString()+"]");
 						}
 						
 						table.printChatMSG("\n");
 						playerList.get(currentIdx).removeCards(playerHand);
 						
 						if (currentIdx!=numberOfPlayer-1)
 						{
 							++currentIdx;
 						}
 						
 						else
 						{
 							currentIdx=0;
 						}
 						
 						handsOnTable.add(playerHand);
 						table.printChatMSG(this.getPlayerList().get(currentIdx).getName()+" Please make a move! \n");
 					}
 				}
 			}
 			
 			else
 			{
 				CardList playerSelectedCards =new CardList();
 				Hand playerHand;
 				
 				for(int i=0; i<cardIdx.length; ++i)
 				{
 					playerSelectedCards.addCard(playerList.get(currentIdx).getCardsInHand().getCard(cardIdx[i]));
 					
 				}
 				
 				playerHand=composeHand(playerList.get(currentIdx), playerSelectedCards);
 				
 				if(handsOnTable.get(numOfHandsPlayed-1).getPlayer().getName()==playerList.get(currentIdx).getName())
 				{
 					if (playerHand==null)
 					{
 						table.printChatMSG("Not a legal move!\n");
 					}
 					
 					else
 					{
 						playerHand.sort();
 						table.printChatMSG(playerList.get(currentIdx).getName()+": "+"{"+playerHand.getType()+"}");
 						
 						for (int j=0; j<playerHand.size(); ++j)
 						{
 							table.printChatMSG(" ["+playerHand.getCard(j).toString()+"]");
 						}
 						
 						table.printChatMSG("\n");
 						playerList.get(currentIdx).removeCards(playerHand);
 						
 						if (currentIdx!=numberOfPlayer-1)
 						{
 							++currentIdx;
 						}
 						
 						else
 						{
 							currentIdx=0;
 						}
 						
 						handsOnTable.add(playerHand);
 						table.printChatMSG(this.getPlayerList().get(currentIdx).getName()+" Please make a move! \n");
 					}
 				}
 				
 				else
 				{
 					if(playerHand!=null)
 					{
 						if (playerHand.size()==handsOnTable.get(handsOnTable.size()-1).size())
 						{
 							if (handsOnTable.get(handsOnTable.size()-1).beats(playerHand)==true)
 							{
 								table.printChatMSG("Not a legal move!\n");
 							}
 							
 							else if(playerHand!=null)
 							{
 								playerHand.sort();
 								table.printChatMSG(playerList.get(currentIdx).getName()+": "+"{"+playerHand.getType()+"}");
 								
 								for (int j=0; j<playerHand.size(); ++j)
 								{
 									table.printChatMSG(" ["+playerHand.getCard(j).toString()+"]");
 								}
 								
 								table.printChatMSG("\n");
 								playerList.get(currentIdx).removeCards(playerHand);
 								
 								if (currentIdx!=numberOfPlayer-1)
 								{
 									++currentIdx;
 								}
 								
 								else
 								{
 									currentIdx=0;
 								}
 								
 								handsOnTable.add(playerHand);
 								table.printChatMSG(this.getPlayerList().get(currentIdx).getName()+" Please make a move!\n");
 							}
 						} else
 						{
 							if(playerHand.getType() == "Quad" && handsOnTable.get(handsOnTable.size()-1).size() == 1) {
 								if(handsOnTable.get(handsOnTable.size()-1).getTopCard().getRank()== 1) {
 									
 									playerList.get(currentIdx).removeCards(playerHand);
 	 								
 	 								if (currentIdx!=numberOfPlayer-1)
 	 								{
 	 									++currentIdx;
 	 								}
 	 								
 	 								else
 	 								{
 	 									currentIdx=0;
 	 								}
 	 								
 	 								handsOnTable.add(playerHand);
 	 								table.printChatMSG(this.getPlayerList().get(currentIdx).getName()+" Please make a move!\n");
 								}
 							}
 							
 						}
 					}
 					
 					else
 					{
 						table.printChatMSG("Not a legal move!\n");
 					}
 				}
 			}
 		} 
 		
 		if(!endOfGame())
 		{
 			playerList.get(playerID).getCardsInHand().sort();
 			table.resetSelected();
 			
 			if(this.playerID==currentIdx)
 			{
 				if(isBot) {
 					Thread.sleep(1000);// + new java.util.Random().nextInt(1001));
 					makeMoveAutomatically();
 				}
 				table.enable();
 			}
 			
 			else
 			{
 				table.disable();
 			}
 			
 			table.repaint();
 			
 		}
 		
 		else
 		{
 			
 			table.repaint();
 			table.printEndGameMsg();
 			handsOnTable.clear();
 			
 			for (int i=0; i<numberOfPlayer; ++i)
 			{
 				playerList.get(i).removeAllCards();
 			}
 			
 			sendMessage(new GameMessage(GameMessage.READY, -1, null));
 		} 
 		
 		
 	}
     
     public boolean endOfGame() 
 	{
 		for (int i = 0; i < numberOfPlayer; i++)
 		{
 			if (this.getPlayerList().get(i).getNumOfCards() == 0)
 			{
 				return true;
 			}
 				
 		}
 			
 		return false;
 	}
     
     
     public static Hand composeHand(CardGamePlayer player, CardList cards)
 	{
 		Hand test;
 		test = new Single(player, cards);
 		
 		if (test.isValid())
 		{
 			return test;
 		}
 			
 		test = new Pair(player, cards);
 		
 		if (test.isValid())
 		{
 			return test;
 		}
 		
 		test = new Triple(player, cards);
 		
 		if (test.isValid())
 		{
 			return test;
 		}
 		
 		test = new Quad(player, cards);
 		
 		if (test.isValid())
 		{
 			return test;
 		}
 		
 		test = new Straight(player, cards);
 		
 		if (test.isValid())
 		{
 			return test;
 		}
 			
 		return null;
 	}
     
     
}
