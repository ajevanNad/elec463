//Ajevan Nadarajah
package lab4.tcpclient;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * client code
 * @author Ajevan
 *
 */
public class Client {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		frame.setBounds(600, 200, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Client");
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
		
		//received messages from other clients
		JTextArea recMessagesTextArea = new JTextArea();
		recMessagesTextArea.setBounds(20, 60, 540, 330);
		frame.getContentPane().add(recMessagesTextArea);
		recMessagesTextArea.setEditable(false);
		
		//send to label
		JLabel sendLabel = new JLabel("Send to:");
		sendLabel.setBounds(20, 400, 100, 30);
		sendLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(sendLabel);
		
		//name of client to send to
		JTextField sendTextfield = new JTextField();
		sendTextfield.setBounds(130, 400, 200, 30);
		sendTextfield.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(sendTextfield);
		sendTextfield.setEnabled(false);
		
		//textarea to write message that is going to be sent
		JTextArea sendMessageTextArea = new JTextArea();
		sendMessageTextArea.setBounds(20, 440, 450, 90);
		frame.getContentPane().add(sendMessageTextArea);
		sendMessageTextArea.setEnabled(false);
		
		//button to send
		JButton sendButton = new JButton("Send");
		sendButton.setBounds(480, 460, 90, 30);
		sendButton.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(sendButton);
		sendButton.setEnabled(false);
		
		connectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//if the person clicked the connectButton and provided a valid client name
				if(clientTextfield.getText().matches("\\w+(\\s)?(\\w+)?")) {
					
					String name = clientTextfield.getText();
					clientTextfield.setEnabled(false);
					connectButton.setVisible(false);
					disconnectButton.setVisible(true);
					
					connect(name, sendButton, sendTextfield, sendMessageTextArea, recMessagesTextArea
							,connectButton, clientTextfield, disconnectButton);
					
					//after connecting, now the user can see the send area
					sendTextfield.setEnabled(true);
					sendMessageTextArea.setEnabled(true);
					sendButton.setEnabled(true);
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
	public static void connect(String name, JButton sendButton, JTextField sendTextfield, JTextArea sendMessageTextArea
			,JTextArea recMessagesTextArea, JButton connectButton, JTextField clientTextfield
			,JButton disconnectButton) {
		
		try {
			
			//ip address 127.0.0.1 or local address can be used //or on mac/linux loopbackip address
			Socket clientSocket = new Socket("localhost", 6789);
			
			//send initial msg to server with the client name
			DataOutputStream outputToServer = new DataOutputStream(clientSocket.getOutputStream());
			String sentence = name + '\n'; //have to add \n since using readLine in recSentence
			outputToServer.writeBytes(sentence);
			
			//read intial msg from server
			BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String recSentence = inputFromServer.readLine();
			recMessagesTextArea.append(recSentence + '\n');
			
			//read message thread to read incomming messages to this client
			Thread readMessage = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true) {
						
						try {
							String recSentence = inputFromServer.readLine();
							recMessagesTextArea.append(recSentence + '\n');
						}
						catch(Exception ex) {
							
						}
					}
				}
			});
			readMessage.start(); //start this thread
			
			sendButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					try {
						//send to only the client with the name provided 
						if(sendTextfield.getText().matches("\\w+(\\s)?(\\w+)?")) {
							
							outputToServer.writeBytes(sendTextfield.getText() + "name_message" 
									+ sendMessageTextArea.getText() + '\n');
							
							recMessagesTextArea.append("You to " + sendTextfield.getText() + ": " 
									+ sendMessageTextArea.getText() + '\n');
							
							//clear the textarea as soon as the user clicks send
							sendMessageTextArea.setText("");
						}
						
						//if no name provided, then send to everyone
						else {
							
							outputToServer.writeBytes("send_to_everyone" + "name_message" 
						+ sendMessageTextArea.getText() + '\n');
														
							recMessagesTextArea.append("You: " + sendMessageTextArea.getText() + '\n');
							
							//clear the textarea as soon as the user clicks send
							sendMessageTextArea.setText("");
						}
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
						
						sendTextfield.setEnabled(false);
						sendMessageTextArea.setEnabled(false);
						sendButton.setEnabled(false);
						
						//send a final msg to server letting it know this client is logging out
						outputToServer.writeBytes("_logout_" + '\n');
						clientSocket.close(); //close the client socket
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
