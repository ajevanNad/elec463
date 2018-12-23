//Ajevan Nadarajah
package lab5.tcpserver;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Server that is able to accept multiple clients.
 * @author Ajevan
 *
 */
public class MultiClientServer {

	//vector with each client that is connected to the server
	public static Vector<ClientHelper> clients = new Vector<ClientHelper>();

	public static void main(String[] args) throws Exception {
		
		//listens for request from client
		ServerSocket welcomeSocket = new ServerSocket(6789);
		
		JFrame frame = new JFrame();
		frame.setBounds(600, 200, 600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Chatting Server");
		frame.setLayout(null);
		frame.setVisible(true);
		
		//number of clients label
		JLabel numClientsLabel = new JLabel();
		numClientsLabel.setBounds(200, 40, 500, 20);
		numClientsLabel.setFont(new Font("Times", Font.BOLD, 14));
		frame.getContentPane().add(numClientsLabel);
		
		//thread that counts and displays the number of clients connected
		Thread countClients = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					
					if(clients.size() == 0) {
						numClientsLabel.setText("No Clients Connected");
						numClientsLabel.setForeground(Color.red);
					}
					else {
						numClientsLabel.setText(clients.size() + " Clients Connected");
						numClientsLabel.setForeground(Color.blue);
					}
				}
			}
		});
		countClients.start(); //start this thread
				
		while (true) {
			
			//accept the request
			Socket connectionSocket = welcomeSocket.accept();
			
			BufferedReader inputFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outputToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			//get the client name, since by default the first message the client sends when it connects is its name
			String name = inputFromClient.readLine();
			
			//let the client know that he is connected
			outputToClient.writeBytes("You are connected\n");
			
			Thread sendAvailablePlayers = new Thread(new Runnable() {
				@Override
				public void run() {
					
					//store the last list of available players sent
					String previousAvailablePlayers = "";
					
					while(true) {
						try {
							
							String availablePlayers = "available_players";
							
							//get the list of players that are available
							for(ClientHelper client : MultiClientServer.clients) {
								if(client.getPlayingWith() == null && !client.getClientName().equals(name)) {
									availablePlayers += "," + client.getClientName();
								}
							}
							
							//only if list of available players changed then send updated list
							if(!availablePlayers.equals(previousAvailablePlayers)) {
								
								previousAvailablePlayers = availablePlayers;
								outputToClient.writeBytes(availablePlayers + "\n");
								
							}
							
						}
						catch(Exception ex) {
							
						}
					}	
				}
			});
			sendAvailablePlayers.start();
			
			//let everyone who is connected to the server that a new client has connected
			for(ClientHelper client : MultiClientServer.clients) {
				if(!client.getClientName().equals(name)) {
					client.getOutputToClient().writeBytes(name + " is connected" + '\n');
				}
			}
			
			ClientHelper newClient = new ClientHelper(name, connectionSocket, inputFromClient, outputToClient);
			
			Thread t = new Thread(newClient); //new thread with ClientHelper object
			clients.add(newClient); //add to list of clients connected
			t.start(); //start the thread
		}

	}

}
