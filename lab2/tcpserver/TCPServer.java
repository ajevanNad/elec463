//Ajevan Nadarajah
package lab2.tcpserver;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class TCPServer {

	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame();
		frame.setBounds(600, 200, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("TCP Server");
		frame.setLayout(null);
		frame.setVisible(true);
		
		//connection status label
		JLabel statusLabel = new JLabel("Connection Status: Not Connected");
		statusLabel.setForeground(Color.red);
		statusLabel.setBounds(20, 20, 500, 20);
		statusLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(statusLabel);
		
		//received messages from client in textarea
		JTextArea recMessages = new JTextArea();
		recMessages.setBounds(20, 100, 540, 430);
		frame.getContentPane().add(recMessages);
		recMessages.setText("Messages received from Client:\n");
		
		
		try {
		
			//listens for request from client
			ServerSocket welcomeSocket = new ServerSocket(6789);
			
			while(true) {
				
				//accept the request
				Socket connectionSocket = welcomeSocket.accept();
				
				statusLabel.setText("Connection Status: Connected");
				statusLabel.setForeground(Color.blue);
				
				//read msg from client
				BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				
				String recSentence = inputFromClient.readLine(); 
				System.out.println("From Client: " + recSentence);
				recMessages.append(recSentence + '\n');
				
				//send msg to client
				DataOutputStream outputToClient = new DataOutputStream(connectionSocket.getOutputStream());
				String sentence = "Connection Accepted.\n"; //have to add \n since using readline() in recSentence which reads until endline reached
				outputToClient.writeBytes(sentence);
			}
		}
		catch(Exception ex) {
			
		}

	}

}
