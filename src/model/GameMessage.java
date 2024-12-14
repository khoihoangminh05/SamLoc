package model;
import java.io.Serializable;

public class GameMessage implements Serializable {
	
	/**
     * Gửi từ server tới client khi kết nốt thành công danh sách tên các người chơi hiện tại.
	 */
	public static final int PLAYER_LIST = 0;
	/**
	 * Được gửi từ client tới server khi kết nối thành công ( playerID được quan tâm, data = NULL )
	 */
	public static final int JOIN = 1;
	/**
	 * Sent by the server to a client after a connection is established but the
	 * server is not able to serve this client because it is full. In this
	 * message, playerID is -1 (not being used) and data is simply null (not
	 * being used).
	 */
	public static final int FULL = 2;
	/**
	 * Broadcast by a server when a client loses connection to the server. In this message,
	 * playerID specifies the player who loses the connection to the server, and data is a string
	 * representation of the IP address and TCP port of this player.
	 */
	public static final int QUIT = 3;
	/**
	 * Sent by a client to the server to indicate it is ready for a new game. The server will also
	 * broadcast this message upon receiving it. In this message, playerID specifies the player who
	 * becomes ready for a new game (for the message broadcast by the server) or -1 (for the message
	 * sent by a client), and data is simply null (not being used).
	 */
	public static final int READY = 4;
	/**
	 * Broadcast by the server when all clients are ready for a new game. In this message, playerID is 
	 * -1 (no being used), and data is a reference to a Deck object (a shuffled deck for the new game).
	 */
	public static final int START = 5;
	/**
	 * Sent by a client when the local player makes a move. The server will broadcast this message upon
	 * receiving it. In this message, playerID specifies the player who makes the move, and data is a
	 * reference to an array of int specifying the indices of the cards being played.
	 */
	public static final int MOVE = 6;
	/**
	 * Sent by a client to the server when the local player press [ENTER] in the chat input field.
	 * The server will first add the name, IP address and TCP port of the player into the chat
	 * message, and then broadcast the message. In this message, playerID specifies the player who
	 * sent this chat message, and data is a reference to a string containing a formated chat message.
	 */
	public static final int MSG = 7;

	/**
	 * Creates and returns an instance of CardGameMessage.
	 * 
	 * @param type
	 *            the message type of this message
	 * @param playerID
	 *            the playerID of this message
	 * @param data
	 *            the data of this message
	 */
	private static final long serialVersionUID = -9138385504565085818L;
	private int type;
	private int playerID;
	private Object data;
	
	public GameMessage(int type, int playerID, Object data) {
		this.type = type;
		this.playerID = playerID;
		this.data = data;
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getPlayerID() {
		return this.playerID;
	}
	
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	public Object getData() {
		return this.data;
	}
	
	public void setData(Object data) {
		this.data = data;
	}
}

