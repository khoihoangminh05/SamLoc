package samloc;

import javax.swing.JOptionPane;

import model.*;
public class SamLocServer extends Server{
	
	public SamLocServer(String serverName, int maxNumOfPlayers) {
		super(serverName, maxNumOfPlayers);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Creates and returns an instance of the BigTwoServer class.
	 */
	public SamLocServer() {
		super("Sam Loc Server", 4);
	}
	
	/**
	 * Creates and returns an instance of the BigTwoDeck class.
	 * @return an instance of the BigTwoDeck class
	 */
	public Deck createDeck() {
		return new SamLocDeck(); 
	}
	
	/**
	 * main() method for starting the server.
	 * 
	 * @param args
	 *            the port to be used by the server. The default port 5000 will
	 *            be used if no arguments has been supplied
	 */
	public static void main(String[] args) {
		int number;
		while(true) {
			String input = JOptionPane.showInputDialog("Nhập vào số người chơi:");
			 number = Integer.parseInt(input);
			 if(0 < number && number < 5) break;
		}
		
		SamLocServer server = new SamLocServer("Sâm Lốc", number);
		if (args.length > 0) {
			server.start(Integer.parseInt(args[0]));
		} else {
			server.start(1234);
		}
	} // main
}
