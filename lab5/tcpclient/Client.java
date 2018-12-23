//Ajevan Nadarajah
package lab5.tcpclient;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * client code.
 * @author Ajevan
 *
 */
public class Client {
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setBounds(600, 200, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("RPS Game Client");
		frame.setLayout(null);
		frame.setVisible(true);
		
		//client name label
		JLabel clientLabel = new JLabel("Client Name:");
		clientLabel.setBounds(20, 20, 200, 30);
		clientLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(clientLabel);
		
		//client name textfield
		JTextField clientTextfield = new JTextField();
		clientTextfield.setBounds(150, 20, 200, 30);
		clientTextfield.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(clientTextfield);
		
		//button to connect
		JButton connectButton = new JButton("Connect");
		connectButton.setBounds(380, 20, 150, 30);
		connectButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(connectButton);
		
		//button to disconnect
		JButton disconnectButton = new JButton("Disconnect");
		disconnectButton.setBounds(380, 20, 150, 30);
		disconnectButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(disconnectButton);
		disconnectButton.setVisible(false);
		
		//play with label
		JLabel playWithLabel = new JLabel("Play with:");
		playWithLabel.setBounds(20, 60, 200, 30);
		playWithLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(playWithLabel);
		
		//play with combo box
		JComboBox<String> playWithCombo = new JComboBox<String>();
		playWithCombo.setBounds(150, 60, 200, 30);
		playWithCombo.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(playWithCombo);
		playWithCombo.setEnabled(false);
		
		//button to play
		JButton playButton = new JButton("Play");
		playButton.setBounds(380, 60, 150, 30);
		playButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(playButton);
		playButton.setEnabled(false);
		
		
		//button to stop playing
		JButton stopPlayButton = new JButton("Stop");
		stopPlayButton.setBounds(380, 60, 150, 30);
		stopPlayButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(stopPlayButton);
		stopPlayButton.setVisible(false);
		
		//button to choose rock
		JButton rockButton = new JButton("Rock");
		rockButton.setBounds(200, 150, 150, 30);
		rockButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(rockButton);
		rockButton.setEnabled(false);
		
		//button to choose paper
		JButton paperButton = new JButton("Paper");
		paperButton.setBounds(200, 190, 150, 30);
		paperButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(paperButton);
		paperButton.setEnabled(false);
		
		//button to choose scissors
		JButton scissorsButton = new JButton("Scissors");
		scissorsButton.setBounds(200, 230, 150, 30);
		scissorsButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(scissorsButton);
		scissorsButton.setEnabled(false);
		
		//winner or player status label
		JLabel winnerLabel = new JLabel();
		winnerLabel.setBounds(20, 500, 500, 30);
		winnerLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(winnerLabel);
		winnerLabel.setForeground(Color.blue);
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//if the person clicked the connectButton and provided a valid client name
				if(clientTextfield.getText().matches("\\w+(\\s)?(\\w+)?")) {
					
					String name = clientTextfield.getText();
					clientTextfield.setEnabled(false);
					connectButton.setVisible(false);
					disconnectButton.setVisible(true);
					
					connect(name,connectButton, clientTextfield, disconnectButton, playWithCombo, playButton, 
							stopPlayButton, rockButton, paperButton, scissorsButton, winnerLabel);
					
					playWithCombo.setEnabled(true);
					playButton.setEnabled(true);
					
				}
				
				//if not provided with a valid client name, then do nothing
			}
		});
		
	}
	
	/**
	 * method that will connect to the server
	 * @param name
	 * @param sendButton
	 * @param sendTextfield
	 * @param sendMessageTextArea
	 * @param recMessagesTextArea
	 * @param connectButton
	 * @param clientTextfield
	 * @param disconnectButton
	 */
	public static void connect(String name, JButton connectButton, JTextField clientTextfield
			,JButton disconnectButton, JComboBox<String> playWithCombo, JButton playButton, JButton stopPlayButton
			,JButton rockButton, JButton paperButton, JButton scissorsButton, JLabel winnerLabel) {
		
		try {
			
			//ip address 127.0.0.1 or local address can be used //or on mac/linux loopbackip address
			Socket clientSocket = new Socket("localhost", 6789);
			
			//send initial msg to server with the client name
			DataOutputStream outputToServer = new DataOutputStream(clientSocket.getOutputStream());
			String sentence = name + '\n'; //have to add \n since using readLine in recSentence
			outputToServer.writeBytes(sentence);
			
			//read intial msg from server
			BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			String recSentence = inputFromServer.readLine();**********************************************************
			
			//read message thread to read incomming messages to this client
			Thread readMessage = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					while(true) {
						
						try {
							String recSentence = inputFromServer.readLine();
							
							System.out.println("client received ="+recSentence);
							
							//if starts with 'availablePlayers' then know this has list of available players
							if(recSentence.startsWith("available_players")) {
								if(recSentence.length() == 17) {
									recSentence += ",";
								}
								
								recSentence = recSentence.replaceFirst("available_players,", "");
								String[] availablePlayers = recSentence.split(",");
								
								playWithCombo.removeAllItems();
								
								for(String ap : availablePlayers) {
									playWithCombo.addItem(ap);
								}
							}
							
							
							else if(recSentence.startsWith("_chosenToPlay_")) {
								recSentence = recSentence.replaceFirst("_chosenToPlay_", "");
								playWithCombo.setSelectedItem(recSentence);
								
								playWithCombo.setEnabled(false);
								playButton.setVisible(false);
								stopPlayButton.setVisible(true);
								
							}
							
							else if(recSentence.startsWith("_waitingForResponse_")) {
								rockButton.setEnabled(true);
								paperButton.setEnabled(true);
								scissorsButton.setEnabled(true);
							}
							
							else if(recSentence.startsWith("_winner_")) {
								recSentence = recSentence.replaceFirst("_winner_", "");
								winnerLabel.setText(recSentence);
								winnerLabel.setForeground(Color.blue);
							}
							
							else if(recSentence.equals("_playerLeft_")) {
								String leftmsg = "You can not play with this player. Please choose another player";
								winnerLabel.setText(leftmsg);
								winnerLabel.setForeground(Color.red);
								
								stopPlayButton.setVisible(false);
								playButton.setVisible(true);
								playWithCombo.setEnabled(true);
								
								rockButton.setEnabled(false);
								paperButton.setEnabled(false);
								scissorsButton.setEnabled(false);
							}
							
						}
						catch(Exception ex) {
							
						}
					}
				}
			});
			readMessage.start(); //start this thread
			
			playButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					//the player that the user wants to play with
					String chosenPlayer = playWithCombo.getSelectedItem().toString();
					
					try {
						if(!chosenPlayer.equals("") || chosenPlayer != null) {
							outputToServer.writeBytes("_playWith_" + chosenPlayer + "," + name + '\n');
							
							playWithCombo.setEnabled(false);
							playButton.setVisible(false);
							stopPlayButton.setVisible(true);
							
						}
						
					}
					catch(Exception ex) {
						
					}
				}
			});
			
			rockButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						
						
						outputToServer.writeBytes("_response_rock\n");
						
						System.out.println("sent _response_rock");
						
						rockButton.setEnabled(false);
						paperButton.setEnabled(false);
						scissorsButton.setEnabled(false);
						
					}
					catch(Exception ex) {
						
					}
					
					
				}
			});
			
			paperButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						
						outputToServer.writeBytes("_response_paper\n");
						System.out.println("sent _response_paper");
						
						rockButton.setEnabled(false);
						paperButton.setEnabled(false);
						scissorsButton.setEnabled(false);
					}
					catch(Exception ex) {
						
					}
					
				}
			});
			
			scissorsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						
						
						outputToServer.writeBytes("_response_scissors\n");
						
						System.out.println("sent _response_scissors");
						
						rockButton.setEnabled(false);
						paperButton.setEnabled(false);
						scissorsButton.setEnabled(false);
					}
					catch(Exception ex) {
						
					}
					
					
				}
			});
			
			disconnectButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						clientTextfield.setEnabled(true);
						disconnectButton.setVisible(false);
						connectButton.setVisible(true);
						
						rockButton.setEnabled(false);
						paperButton.setEnabled(false);
						scissorsButton.setEnabled(false);
						
						stopPlayButton.setVisible(false);
						playButton.setVisible(true);
						playWithCombo.setEnabled(false);
						
						//send a final msg to server letting it know this client is logging out
						outputToServer.writeBytes("_logout_" + '\n');
						clientSocket.close(); //close the client socket
					}
					catch(Exception ex) {
						
					}
				}
			});	
						
			stopPlayButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
//						
						stopPlayButton.setVisible(false);
						playButton.setVisible(true);
						playWithCombo.setEnabled(true);
						
						rockButton.setEnabled(false);
						paperButton.setEnabled(false);
						scissorsButton.setEnabled(false);
						
						//send a final msg to server letting it know this client is logging out
						outputToServer.writeBytes("_stop_" + '\n');
					}
					catch(Exception ex) {
						
					}
				}
			});	
			
		}
		catch(Exception ex) {
			
		}
		
	}

}
