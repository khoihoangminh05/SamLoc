package samloc;



import javax.swing.*;

import model.*;
import javax.swing.text.DefaultCaret;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class SamLocTable {
	
	private JFrame frame;
	private SamLocClient game;
	private int activePlayer;
	private JPanel messageBox;
	private boolean status_msb;
	private JTextArea msgArea;
	private JPanel mainPanel;
	private JPanel introPanel;
	private boolean[] selected;
	private Image[] avatars;
	private Image[][] cardImages;
	private Image cardBackImage;
	private boolean[] presence;
	private int numberOfPlayer;
	private JButton[] cardButtons;
	private PassButton passbutton;
	private PlayButton playbutton;
	
	public SamLocTable(SamLocClient game) {
		
		this.game = game;
		buildGUI();
	}
	

	public void buildGUI() {
		
		setActivePlayer(game.getPlayerID()); 
			selected = new boolean[13];
			resetSelected();

		
			avatars = new Image[4];
			cardImages = new Image [4][13];
			cardButtons = new JButton [11];
			cardBackImage = new ImageIcon(getClass().getResource("/cards/BACK.png.png")).getImage();
			
			avatars[0] = new ImageIcon(getClass().getResource("/avatars/avatar1.png")).getImage();
			avatars[1] = new ImageIcon(getClass().getResource("/avatars/avatar2.png")).getImage();
			avatars[2] = new ImageIcon(getClass().getResource("/avatars/avatar3.png")).getImage();
			avatars[3] = new ImageIcon(getClass().getResource("/avatars/avatar4.png")).getImage();
			
			char[] suit = {'D','C','H','S'};
			char[] rank = {'A', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'J', 'Q', 'K'};
			
			String fileLocation = new String();
			for (int i = 0; i < 4; i++)
			    {
					for(int j = 0 ; j < 13;j++)
					{
						fileLocation = "/cards/" + rank[j] + "-" + suit[i] + ".png.png";
				        cardImages[i][j] = new ImageIcon(getClass().getResource(fileLocation)).getImage();
					}
			        
			    }
			
			presence = new boolean[4];
			for (int i = 0; i < numberOfPlayer; i++)
				presence[i] = false;
		
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setTitle("Sâm Lốc");
		frame.setLocation(100,40);
		frame.setSize(1240, 740);
		
		mainPanel = new SamLocPanel();
		introPanel = new IntroPanel();
		frame.setContentPane(introPanel);
		
		frame.setVisible(true);
		
	}
	
      class IntroPanel extends JPanel {
    	Image introImage = new ImageIcon(getClass().getResource("/background/intro.jpg")).getImage();
  		public IntroPanel() {
  			this.setLayout(null);
  			this.setUp();
  		}
  		
  		public void paintComponent(Graphics g) {
            g.drawImage(introImage, 0, 0,getWidth() ,getHeight(), this);
		}
  		
  		public void setUp() {
  			
  			
  			JPanel panel = new JPanel(new BorderLayout());
  			panel.setBounds(400, 250,350, 270);
  			NameTextField nameText = new NameTextField(30);
  			panel.add(nameText,BorderLayout.NORTH);
  			panel.setOpaque(false);
  		
  			StartButton startbutton = new StartButton("");
  			startbutton.setBounds(520, 300, 100, 90);
  			this.add(startbutton);
  			
  			BotButton botbutton = new BotButton("");
  			botbutton.setBounds(400, 295, 100, 90);
  			this.add(botbutton);
  			
  			this.add(panel);
  		}
  		
  		
      }
	  class SamLocPanel extends JPanel implements MouseListener  {
		
		Image backgroundImage = new ImageIcon(getClass().getResource("/background/background.jpg")).getImage();
		public SamLocPanel() {
			this.setLayout(null);
			this.setUp();
		}
		@Override
		public void paintComponent(Graphics g) {
            g.drawImage(backgroundImage, 0, 0,getWidth() ,getHeight(), this);
          
    		this.setOpaque(true);
    		Graphics2D g2 = (Graphics2D) g;
    			
    		ArrayList<Integer> view = new ArrayList<Integer>();
    		view.add(activePlayer);
    		for(int i= 0; i < numberOfPlayer; i++) {
    			if(i!=activePlayer) view.add(i);
    		}
    		
    		int ID = view.get(0);
    		if(presence[ID]) {
    			g2.drawImage(avatars[ID], 560, 610, this);
    			g2.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 24));
    			g2.setColor(Color.BLUE);
    			g2.drawString("You", 580, 700);
    		
    			
    			for (int i = 0; i < game.getPlayerList().get(ID).getNumOfCards(); i++) 
	        	{
    				ImageIcon icon = new ImageIcon(cardImages[game.getPlayerList().get(ID).getCardsInHand().getCard(i).getSuit()][game.getPlayerList().get(ID).getCardsInHand().getCard(i).getRank()]);
    				cardButtons[i].setIcon(icon);
    				cardButtons[i].setActionCommand(String.valueOf(i));
    				cardButtons[i].addActionListener(new CardButtonListener());
    				if(!selected[i])
    				{
    					cardButtons[i].setBounds(300 + i*40, 480, icon.getIconWidth(), icon.getIconHeight());
    				} 
    				else
    				{
    					cardButtons[i].setBounds(300 + i*40, 480-20, icon.getIconWidth(), icon.getIconHeight());
    				}
    				cardButtons[i].setVisible(true);
    				
		    	}
    			
    			for (int i = game.getPlayerList().get(ID).getNumOfCards(); i < 10; i++) 
    			{
    				cardButtons[i].setVisible(false);
    			}
    			
    			

    		
    			
    			
    			
    		}
   

    		if(numberOfPlayer>1 && presence[view.get(1)]) {
    			
    			ID = view.get(1);
    			g2.drawImage(avatars[ID], 1120, 300, this);
    			g2.setColor(Color.GREEN);
    			g2.drawString(game.getPlayerList().get(ID).getName(),1140, 390);
    			
    			if(game.getPlayerList().get(ID).getNumOfCards()>0) {
    			 g2.drawImage(cardBackImage,1030, 270,this);
    			 g2.setColor(Color.BLACK);
    			 g2.drawString(String.valueOf(game.getPlayerList().get(ID).getNumOfCards()),1054,330);
    			 g2.drawOval(1052, 308, 30, 30);
    			 
    			}
    			
    		}
        
    		if(numberOfPlayer>2 && presence[view.get(2)]) {
    			ID = view.get(2);
    			g2.drawImage(avatars[ID], 560, 30, this);
    			g2.setColor(Color.GREEN);
    			g2.drawString(game.getPlayerList().get(ID).getName(),580, 118);
    			
    			if(game.getPlayerList().get(ID).getNumOfCards()>0) {
       			 g2.drawImage(cardBackImage,640, 20,this);
       			 g2.setColor(Color.BLACK);
       			 g2.drawString(String.valueOf(game.getPlayerList().get(ID).getNumOfCards()),664,80);
       			 g2.drawOval(662, 58, 30, 30);
       			 
       			}
    		}
    		
    		if(numberOfPlayer>3 && presence[view.get(3)]) {
    			ID = view.get(3);
    			g2.drawImage(avatars[ID], 30, 300, this);
    			g2.setColor(Color.GREEN);
    			g2.drawString(game.getPlayerList().get(ID).getName(),50, 390);
    			
    			if(game.getPlayerList().get(ID).getNumOfCards()>0) {
          			 g2.drawImage(cardBackImage,120, 270,this);
          			 g2.setColor(Color.BLACK);
          			 g2.drawString(String.valueOf(game.getPlayerList().get(ID).getNumOfCards()),144,330);
          			 g2.drawOval(142, 308, 30, 30);
          			 
          			}
    		}
    		
    		if(game.getHandsOnTable().size() > 0) {
    			Hand  hand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
    			for (int i = 0; i <hand.size(); i++) 
	        	{
    	
    				g2.drawImage(cardImages[hand.getCard(i).getSuit()][hand.getCard(i).getRank()], 600 + i*44, 270, this);
	        	}
    				
    		}
    		
    		
		}
		
		public void setUp() {
			
			messageBox = new JPanel();
			messageBox.setLayout(new BorderLayout());
			msgArea = new JTextArea(100,30) {
				@Override
	            protected void paintComponent(Graphics g) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	                // Vẽ nền mờ
	                g2.setColor(new Color(255, 255, 255, 100)); 
	                g2.fillRoundRect(0, 0, getWidth(), getHeight(),0,0);

	                g2.dispose();
	                super.paintComponent(g);
	            }
	        };
	        msgArea.setOpaque(false); // Làm nền trong suốt
	        msgArea.setForeground(Color.BLACK); // Màu chữ
	        msgArea.setFont(new Font("Arial", Font.PLAIN, 16)); // Font chữ
	        msgArea.setLineWrap(true); // Tự xuống dòng
	        msgArea.setWrapStyleWord(true); // Xuống dòng theo từ
	        msgArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			DefaultCaret caret = (DefaultCaret)msgArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			msgArea.append("This is the chat area!\n\n");
			msgArea.setEditable(false);
			messageBox.setOpaque(false);
			messageBox.add(msgArea,BorderLayout.CENTER);
			
			MyTextField text = new MyTextField(30);
			messageBox.add(text,BorderLayout.SOUTH);
			messageBox.setBounds(0, 50, 300, 400);
			messageBox.setVisible(status_msb);
			this.add(messageBox);
			
			HomeButton homebutton = new HomeButton("");
			homebutton.setBounds(1150, 30, 50, 50);
			this.add(homebutton);
			
			MessageButton messagebutton = new MessageButton("");
			messagebutton.setBounds(1095, 30, 50, 50);
			this.add(messagebutton);
			
			ReadyButton readybutton = new ReadyButton("");
			readybutton.setBounds(1000, 500, 50, 50);
			this.add(readybutton);
			
			passbutton = new PassButton("");
			passbutton.setBounds(680,410,50,50);
			passbutton.setVisible(false);
			this.add(passbutton);
			
			playbutton = new PlayButton("");
			playbutton.setBounds(560,410,120,50);
			playbutton.setVisible(false);
			this.add(playbutton);
			
	        JLayeredPane layeredPane = new JLayeredPane();
	        layeredPane.setBounds(0, 0, 800, 800); 
	        this.add(layeredPane);

	        for (int i = 0; i < 10; i++) {
	            cardButtons[i] = new JButton();
	            cardButtons[i].setVisible(false);
	            cardButtons[i].setBounds(300 + i * 20, 480, 100, 100);
	            layeredPane.add(cardButtons[i], Integer.valueOf(i));
	        }
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	  
	    public void printEndGameMsg()
	  	{
	  		JOptionPane.showMessageDialog(null, "GameOver!!\n");
	  	}
	  
	  public void printChatMSG(String message) {
		  msgArea.append(message + "\n");
	  }
	  
	  class MyTextField extends JTextField implements ActionListener
		{

			private static final long serialVersionUID = 1L;

			public MyTextField(int i)
			{
				super(i);
				addActionListener(this);
				this.setFont(new Font("Arial", Font.PLAIN, 16));
				this.setOpaque(false);
			}
			
			 protected void paintComponent(Graphics g) {
	                Graphics2D g2 = (Graphics2D) g.create();
	                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	                // Vẽ nền mờ
	                g2.setColor(new Color(255, 255, 255, 100)); 
	                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

	                g2.dispose();
	                super.paintComponent(g);
	            }

			public void actionPerformed(ActionEvent event)
			{
				String input = getText(); 
				
				if (input != null && input.trim().isEmpty() == false) 
				{  
					GameMessage message = new GameMessage(7, activePlayer, input);
					game.sendMessage(message);
				}
				
				this.setText("");
			}
		}
	  
	  class NameTextField extends MyTextField implements ActionListener{
		  
		private static final long serialVersionUID = 1L;
		public NameTextField(int i) {
			super(i);
			this.setFont(new Font("Arial", Font.PLAIN, 30));
			this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		}
		
		public void actionPerformed(ActionEvent event)
		{
			game.setPlayerName(this.getText());
			//this.setText("");
		}
		  
	  }
	  
	  public void setActivePlayer(int activePlayer)
		{
			this.activePlayer = activePlayer;
		}
	  
	  class StartButton extends JButton implements ActionListener{

		public StartButton(String s) {
			super(s);
			ImageIcon i = new ImageIcon(getClass().getResource("/buttons/start.png"));
			
	        Image resizedImg = i.getImage().getScaledInstance(100, 90, Image.SCALE_SMOOTH); // Resize về 50x50
	        ImageIcon resizedIcon = new ImageIcon(resizedImg); 
	        
			this.setIcon(resizedIcon);
			this.setPreferredSize(new Dimension(40, 40));
			this.setBorderPainted(false);
		    this.setFocusPainted(false);  
			this.setContentAreaFilled(false); 
			
			this.addActionListener(this);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub           
			frame.setContentPane(mainPanel);
			frame.revalidate();          // Cập nhật giao diện
            frame.repaint();                    
            GameMessage message = new GameMessage(1, -1 , game.getPlayerName());
			game.sendMessage(message);
		}
	  }
		class HomeButton extends JButton implements ActionListener{

				public HomeButton(String s) {
					super(s);
					ImageIcon i = new ImageIcon(getClass().getResource("/buttons/home.png"));
					
			        Image resizedImg = i.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); 
			        ImageIcon resizedIcon = new ImageIcon(resizedImg); 
			        
					this.setIcon(resizedIcon);
					this.setPreferredSize(new Dimension(40, 40));
					this.setBorderPainted(false);
				    this.setFocusPainted(false);  
					this.setContentAreaFilled(false); 
					
					this.addActionListener(this);
				}
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					frame.setContentPane(mainPanel);
					frame.revalidate();          // Cập nhật giao diện
		            frame.repaint();   
		            game.setIsBot(false);
		            GameMessage message = new GameMessage(1, -1 , game.getPlayerName());
					game.sendMessage(message); 
				}
		  
	  
	  }
		
		class BotButton extends JButton implements ActionListener{

			public BotButton(String s) {
				super(s);
				ImageIcon i = new ImageIcon(getClass().getResource("/buttons/bot.png"));
				
		        Image resizedImg = i.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); 
		        ImageIcon resizedIcon = new ImageIcon(resizedImg); 
		        
				this.setIcon(resizedIcon);
				this.setPreferredSize(new Dimension(40, 40));
				this.setBorderPainted(false);
			    this.setFocusPainted(false);  
				this.setContentAreaFilled(false); 
				
				this.addActionListener(this);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setContentPane(mainPanel);
				frame.revalidate();          // Cập nhật giao diện
	            frame.repaint();   
	            game.setIsBot(true);
	            GameMessage message = new GameMessage(1, -1 , game.getPlayerName());
				game.sendMessage(message);
			}
	  
  
  }
		
		class CardButtonListener implements ActionListener
		{
			
			@Override
			public void actionPerformed(ActionEvent event)
			{
				 String s = event.getActionCommand();
				 System.out.println(s);
				 selected[Integer.parseInt(s)] = !selected[Integer.parseInt(s)] ;
				 repaint();
				 
			}
		}
		
		
		class MessageButton extends JButton implements ActionListener{

			public MessageButton(String s) {
				super(s);
				ImageIcon i = new ImageIcon(getClass().getResource("/buttons/message.png"));
				
		        Image resizedImg = i.getImage().getScaledInstance(47, 47, Image.SCALE_SMOOTH); 
		        ImageIcon resizedIcon = new ImageIcon(resizedImg); 
		        
				this.setIcon(resizedIcon);
				this.setPreferredSize(new Dimension(40, 40));
				this.setBorderPainted(false);
			    this.setFocusPainted(false);  
				this.setContentAreaFilled(false); 
				
				this.addActionListener(this);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				status_msb = !status_msb;
				messageBox.setVisible(status_msb);
			}
	      }
		
		class ReadyButton extends JButton implements ActionListener{

			public ReadyButton(String s) {
				super(s);
				ImageIcon i = new ImageIcon(getClass().getResource("/buttons/ready.png"));
				
		        Image resizedImg = i.getImage().getScaledInstance(47, 47, Image.SCALE_SMOOTH); 
		        ImageIcon resizedIcon = new ImageIcon(resizedImg); 
		        
				this.setIcon(resizedIcon);
				this.setPreferredSize(new Dimension(40, 40));
				this.setBorderPainted(false);
			    this.setFocusPainted(false);  
				this.setContentAreaFilled(false); 
				
				this.addActionListener(this);
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				game.sendMessage(new GameMessage(4, -1, null));
				this.setVisible(false);
				frame.setState(Frame.ICONIFIED);

			}
	      }
		class PlayButton extends JButton implements ActionListener
		{
			
			public PlayButton(String s) {
				super(s);
				ImageIcon i = new ImageIcon(getClass().getResource("/buttons/play.png"));
				
		        Image resizedImg = i.getImage().getScaledInstance(120, 47, Image.SCALE_SMOOTH); 
		        ImageIcon resizedIcon = new ImageIcon(resizedImg); 
		        
				this.setIcon(resizedIcon);
				this.setPreferredSize(new Dimension(40, 40));
				this.setBorderPainted(false);
			    this.setFocusPainted(false);  
				this.setContentAreaFilled(false); 
				
				this.addActionListener(this);
			}
			
			@Override
			public void actionPerformed(ActionEvent event)
			{
				
				
				if (game.getCurrentIdx() == activePlayer)
				{ 
					if (getSelected().length == 0) 
					{	
						int [] cardIdx = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
						game.makeMove(activePlayer, cardIdx);
					}
					
					else
					{
						game.makeMove(activePlayer, getSelected());
					}
						
					resetSelected();
					repaint();
				}
				
				else 
				{ 
					printChatMSG("It is not your turn\n");
					resetSelected();
					repaint();
				}
			}
		}
		
		class PassButton extends JButton implements ActionListener
	 	{
			
			public PassButton(String s) {
				super(s);
				ImageIcon i = new ImageIcon(getClass().getResource("/buttons/pass.png"));
				
		        Image resizedImg = i.getImage().getScaledInstance(47, 44, Image.SCALE_SMOOTH); 
		        ImageIcon resizedIcon = new ImageIcon(resizedImg); 
		        
				this.setIcon(resizedIcon);
				this.setPreferredSize(new Dimension(40, 40));
				this.setBorderPainted(false);
			    this.setFocusPainted(false);  
				this.setContentAreaFilled(false); 
				
				this.addActionListener(this);
			}	
		
		 public void actionPerformed(ActionEvent e) {
			if (game.getCurrentIdx() == activePlayer)
			{ 
				int[] cardIdx = null;
				game.makeMove(activePlayer, cardIdx);
				resetSelected();
				repaint();
			} 
			
			else 
			{
				printChatMSG("Not your turn!\n");
				resetSelected();
				repaint();
			}
		}
	}
		
		public int[] getSelected()
		{
			int ct = 0;
			
			for (int i = 0; i < 13; i++)
			{
				if (selected[i])
				{
					ct++;
				}
			}
			
			int[] user_input = new int[ct];
			int counter = 0;
			
			for (int i = 0; i < 13; i++)
			{
				if (selected[i])
				{
					user_input[counter] = i;
					counter++;
				}
			}
			
			return user_input; 
		}
		
		public void enable()
		{
			playbutton.setVisible(true);
			passbutton.setVisible(true);
		}

		
		public void disable()
		{
			playbutton.setVisible(false);
			passbutton.setVisible(false);
		}
		
		public void repaint() {
			frame.repaint();
		}
		public void resetSelected()
		{
			for (int i = 0; i < 13; i++)
				selected[i] = false;
		}
		
		public void setExistence(int playerID)
		{
			presence[playerID] = true;
		}
		public void setNotExistence(int playerID)
		{
			presence[playerID] = false;
		}
		
		public int getNumberOfPlayer()
	 	{
	 		return numberOfPlayer;
	 	}
	 	
	 	public void setNumberOfPlayer(int numberOfPlayer)
	 	{
	 		this.numberOfPlayer = numberOfPlayer;
	 	}
		
}
