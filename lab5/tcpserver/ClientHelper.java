//Ajevan Nadarajah
package lab5.tcpserver;

import java.io.*;
import java.net.*;


/**
 * ClientHelper class that is going to be used by the server to help handle the incoming messages from clients and to 
 * forward the messages to the appropriate client recipients.
 * @author Ajevan
 *
 */
public class ClientHelper extends Thread {

	private final String name; //name of the client
	private final Socket connectionSocket; //client's socket
	private BufferedReader inputFromClient;
	private DataOutputStream outputToClient;
	private String playingWith = null; //who this player is playing with
	private String gameChoice = null; //this player's choice ex:rock
	
	/**
	 * ClientHelper constructor to get all the required info about the client
	 * @param name
	 * @param connectionSocket
	 * @param inputFromClient
	 * @param outputTpClient
	 */
	public ClientHelper(String name, Socket connectionSocket, BufferedReader inputFromClient, 
			DataOutputStream outputTpClient) {
		
		this.name = name;
		this.connectionSocket = connectionSocket;
		this.inputFromClient = inputFromClient;
		this.outputToClient = outputTpClient;
	}
	
	public void setplayingWith( String playingWith) {
		this.playingWith = playingWith;
	}
	
	public String getPlayingWith() {
		return playingWith;
	}
	
	/**
	 * get the client name
	 * @return name
	 */
	public String getClientName() {
		return name;
	}
	
	/**
	 * get the client's output stream
	 * @return outputToClient
	 */
	public DataOutputStream getOutputToClient() {
		return outputToClient;
	}
	
	public BufferedReader getInputFromClient() {
		return inputFromClient;
	}
	
	/**
	 * decide who the winner is
	 * @param p1 player 1's choice ex:rock
	 * @param p2 player 2's choice ex:paper
	 * @return the winning choice ex:paper
	 */
	public String decideWinner(String p1, String p2) {
		if((p1.equals("paper") && p2.equals("rock")) || (p1.equals("rock") && p2.equals("paper"))) {
			return "paper";
		}
		else if((p1.equals("paper") && p2.equals("scissors")) || (p1.equals("scissors") && p2.equals("paper"))) {
			return "scissors";
		}
		else if((p1.equals("rock") && p2.equals("scissors")) || (p1.equals("scissors") && p2.equals("rock"))) {
			return "rock";
		}
		else {
			return "tie";
		}
	}

	@Override
	public void run() {
		
		try {
			while(true) {
				
				//read msg from client
				inputFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				String recSentence = inputFromClient.readLine(); 
				
				System.out.println("recSentence="+recSentence);
				
				//if the client disconnects, a final automatic message "_logout_" will be sent
				//so that this client can be removed
				if(recSentence.equals("_logout_")) {
					
					System.out.println("reached logout");
					
					//let everyone connected to the server that this client disconnected
					for(ClientHelper client : MultiClientServer.clients) {
						if(!client.name.equals(this.name)) {
							client.outputToClient.writeBytes(this.name + " is disconnected" + '\n');
						}
					}
					
					for(ClientHelper client : MultiClientServer.clients) {
						if(client.name.equals(this.playingWith)) {
							client.gameChoice = null;
							client.playingWith = null;
							client.outputToClient.writeBytes("_playerLeft_" + '\n');
							break;
						}
						
					}
					
					//remove this client from the list of clients connected to the server 
					for(ClientHelper client : MultiClientServer.clients) {
						if(client.name.equals(this.name)) {
							MultiClientServer.clients.removeElement(client);
							break; //found client to remove, so can stop looking
						}
					}
					break; //get out of the infinite while loop
				}
				
				else if(recSentence.equals("_stop_")) {
					for(ClientHelper client : MultiClientServer.clients) {
						if(client.name.equals(this.playingWith)) {
							
							this.playingWith = null; //me not playing with anyone anymore
							
							client.gameChoice = null; //other player not playing with anyone anymore
							client.playingWith = null; 
							client.outputToClient.writeBytes("_playerLeft_" + '\n');
							break;
						}
						
					}
				}
				
				else if(recSentence.startsWith("_response_")) {
					recSentence = recSentence.replaceFirst("_response_", "");
					this.gameChoice = recSentence;
					
					for(ClientHelper client : MultiClientServer.clients) {
						if(client.name.equals(this.playingWith) && client.gameChoice != null) {
							String p1choice = client.gameChoice;
							String p2choice = this.gameChoice;
							
							String winner = decideWinner(p1choice, p2choice);
							System.out.println("p1="+p1choice);
							System.out.println("p2="+p2choice);
							System.out.println("winner="+winner);
							
							if(winner.equals("tie")) {
								client.outputToClient.writeBytes("_winner_" + p1choice + " x " + p2choice + "...Draw" + '\n');
								this.outputToClient.writeBytes("_winner_" + p1choice + " x " + p2choice + "...Draw" + '\n');
							}
							else if (winner.equals(p1choice)) {
								client.outputToClient.writeBytes("_winner_" + p1choice + " x " + p2choice + "...You win" + '\n');
								this.outputToClient.writeBytes("_winner_" + p1choice + " x " + p2choice + "..." + this.playingWith + " win" + '\n');
							}
							else if(winner.equals(p2choice)) {
								
								this.outputToClient.writeBytes("_winner_" + p1choice + " x " + p2choice + "...You win" + '\n');
								client.outputToClient.writeBytes("_winner_" + p1choice + " x " + p2choice + "..." + client.playingWith + " win" + '\n');
							}
							
							client.gameChoice = null;
							this.gameChoice = null;
							
							client.outputToClient.writeBytes("_waitingForResponse_" + '\n');
							this.outputToClient.writeBytes("_waitingForResponse_" + '\n');
							
							break;
						}
					}
				}
				
				
				else if(recSentence.startsWith("_playWith_")) {
					recSentence = recSentence.replaceFirst("_playWith_", "");
					
					//player 1 and player 2
					String[] p1_p2 = recSentence.split(",");
					this.playingWith = p1_p2[0];
					
					
					System.out.println("p1="+p1_p2[0]);
					System.out.println("p2="+p1_p2[1]);
					
					
					//get outputs for players
					DataOutputStream p1outputToClient = null; //other player
					DataOutputStream p2outputToClient = this.outputToClient; //me
					
					for(ClientHelper client : MultiClientServer.clients) {
						if(client.name.equals(p1_p2[0])) {
							
							client.setplayingWith(p1_p2[1]);
							
							p1outputToClient = client.getOutputToClient();
							
							client.outputToClient.writeBytes("_chosenToPlay_" + p1_p2[1] + '\n');
							break; //found person, so get out of for loop
						}
					}
					
					p1outputToClient.writeBytes("_waitingForResponse_" + '\n');
					p2outputToClient.writeBytes("_waitingForResponse_" + '\n');
					
				}
				
			}
			
		}
		catch(Exception ex) {
			
		}
		
	}
	
}
