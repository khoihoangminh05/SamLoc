package model;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import java.awt.*;
//import CardGameServer.ClearMenuItemListener;
//import CardGameServer.QuitMenuItemListener;

public class Server {
	// Tên server
	private String serverName;
	// Số người chơi tối đa
	private int maxNumOfPlayers ;
	// lưu trữ các socket của client
	private Socket[] clientSockets;
	//lưu trữ ObjectOutputStreams của clients
	private ObjectOutputStream[] clientOutputStreams;
	// tên của clients
	private String[] clientNames;
	// trạng thái sẵn sàng của mỗi clients
	private boolean[] clientReadyStates;
	// số người chơi hiện tại
	private int numOfPlayers = 0;
	// main frame
	private JFrame frame = null;
	// Text hiện thị trạng thái của server
	private JTextArea textArea = null;
	
	public Server(String serverName, int maxNumOfPlayers) {
		this.serverName = serverName;
		this.maxNumOfPlayers = maxNumOfPlayers;

		clientSockets = new Socket[maxNumOfPlayers];
		clientOutputStreams = new ObjectOutputStream[maxNumOfPlayers];
		clientNames = new String[maxNumOfPlayers];
		clientReadyStates = new boolean[maxNumOfPlayers];

		buildGUI();
	}
	
	
	 
		private void buildGUI() {
			
			frame = new JFrame(serverName);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(420, 500);
			
			textArea = new JTextArea(420, 500);
			textArea.setEditable(false);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setBackground(Color.BLACK); // Nền đen
	        textArea.setForeground(Color.GREEN); // Chữ xanh lá cây
	        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Font chữ Monospaced
	        textArea.setCaretColor(Color.GREEN); 
			JScrollPane scroller = new JScrollPane(textArea);
			scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			frame.add(scroller);
			
			frame.setVisible(true);
			
		} 
		
		public void start(int port) {
			
			try {
		      ServerSocket serverSocket = new ServerSocket(port);
		      println("Server is running on IP: " + InetAddress.getLocalHost().getHostAddress() + ", Port: " + port);
		      while(true) {
		    	  Socket clientSocket = serverSocket.accept();
		    	  addConnection(clientSocket); 
		      }
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
		
		public void addConnection(Socket clientSocket) {
			if(numOfPlayers < maxNumOfPlayers) {
			 for(int i = 0; i < 4; i++) 
			   if(clientSockets[i]==null) { 
				 try {
				    ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
				    numOfPlayers++;
				    clientSockets[i] = clientSocket;
				    clientNames[i] = "";
				    clientOutputStreams[i] = oos;
				    clientReadyStates[i] = false;
				    println("Co thang ket noi"); 
				    oos.writeObject(new GameMessage( GameMessage.PLAYER_LIST, i, clientNames));
				     } 
				 catch(Exception ex) {
				 	ex.printStackTrace();
				 }
				
				 	
				 Thread t = new Thread(new clientHandler(clientSocket)); 	
				 t.start();
				 break;
			}
		 }
		}
		
		private void removeConnection(Socket clientSocket) {
			if (numOfPlayers > 0) {
				// locates the client socket in the array
				for (int i = 0; i < maxNumOfPlayers; i++) {
					if (clientSockets[i] == clientSocket) {
						String name = clientNames[i];

						clientSockets[i] = null;
						clientOutputStreams[i] = null;
						clientNames[i] = null;
						clientReadyStates[i] = false;
						numOfPlayers--;

						println(name + " (" + clientSocket.getRemoteSocketAddress()
								+ ") leaves the game.");

						String remoteAddress = clientSocket
								.getRemoteSocketAddress().toString();

						// broadcasts a message about the leaving of this player
						sendMessageToAll(new GameMessage(GameMessage.QUIT, i, remoteAddress));
						break;
					}
				}
			}
		} // removeConnection
		
		private synchronized void setReadyState(Socket clientSocket) {
			
			if (numOfPlayers > 0) {
				// locates the client socket in the array
				for (int i = 0; i < maxNumOfPlayers; i++) {
					if (clientSockets[i] == clientSocket) {
						clientReadyStates[i] = true;
						println(clientNames[i] + " ("
								+ clientSocket.getRemoteSocketAddress()
								+ " ) is ready for the next game.");
						sendMessageToAll(new GameMessage(GameMessage.READY, i, null));
						break;
					}
				}
			}

			// checks if all players are ready
			if (numOfPlayers == maxNumOfPlayers) {
				for (int i = 0; i < maxNumOfPlayers; i++) {
					if (clientReadyStates[i] == false) {
						// returns if any of the players is not ready
						return;
					}
				}

				// resets the ready states of all the players for the next game
				for (int i = 0; i < maxNumOfPlayers; i++) {
					clientReadyStates[i] = false;
				}

				// creates a new deck, shuffles the deck, and starts a new game
				Deck deck = createDeck();
				deck.shuffle();
				println("All players are ready. Game starts.");
				sendMessageToAll(new GameMessage(GameMessage.START, -1, deck));
			}
		}

		public Deck createDeck() {
			return new Deck();
		}
		
		public void parseMessage(Socket clientSocket, GameMessage message) {
			
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] == clientSocket) {
					message.setPlayerID(i);
					break;
				}
			}
			if(message.getType() == GameMessage.MSG) {
				recieveMessage(clientSocket,(String)message.getData());
			}
			else if(message.getType() == GameMessage.JOIN) {
				addPlayer(clientSocket, (String) message.getData());
			}
			else if(message.getType() == GameMessage.READY) {
				setReadyState(clientSocket);
			} 
			else if(message.getType() == GameMessage.MOVE) {
				sendMessageToAll(message);
			}
		}
		
		private  void addPlayer(Socket clientSocket, String name) {
			if (numOfPlayers > 0) {
				// locates the client socket in the array
				for (int i = 0; i < maxNumOfPlayers; i++) {
					if (clientSockets[i] == clientSocket) {
						// updates the name of the new player
						clientNames[i] = name;

						println(name + " (" + clientSocket.getRemoteSocketAddress()
								+ ") joins the game.");

						// broadcasts a message about this player joining the game
						sendMessageToAll(new GameMessage(GameMessage.JOIN, i, name));
						break;
					}
				}
			}
		} // addPlayer
		
		
		public void sendMessageToAll(GameMessage message) {
			for (int i = 0; i < maxNumOfPlayers; i++) {
				if (clientSockets[i] != null && clientOutputStreams[i] != null) {
					try {
						clientOutputStreams[i].writeObject(message);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		public void recieveMessage(Socket clientSocket, String msg) {
			//System.out.print(123);
			if (numOfPlayers > 0) {
				
				for (int i = 0; i < maxNumOfPlayers; i++) {
					if (clientSockets[i] == clientSocket) {
						String longMsg = clientNames[i] + ":" + msg;
						sendMessageToAll(new GameMessage(GameMessage.MSG, i, longMsg));
						break;
					}
				}
			}
		}
		
		private void println(String msg) {
			textArea.append(msg + "\n");
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		
		class clientHandler implements Runnable {

			Socket clientSocket = null;
			ObjectInputStream ois = null;
			
			public clientHandler(Socket clientSocket) {
				this.clientSocket = clientSocket;
				try {
				 ois = new ObjectInputStream(clientSocket.getInputStream());
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			@Override
			public void run() {
				// TODO Auto-generated method stub
				GameMessage message;
				try {
					while((message = (GameMessage) ois.readObject()) != null) {

						parseMessage(clientSocket, message);
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					removeConnection(clientSocket);
				}
			}
			
		}
		


}
