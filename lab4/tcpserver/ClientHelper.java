//Ajevan Nadarajah
package lab4.tcpserver;

import java.io.*;
import java.net.*;

/**
 * ClientHelper class that is going to be used by the server to help handle the incoming messages from clients and to 
 * forward the messages to the appropriate client recipients
 * @author Ajevan
 *
 */
public class ClientHelper extends Thread {
	
	private final String name; //name of the client
	private final Socket connectionSocket; //client's socket
	private BufferedReader inputFromClient;
	private DataOutputStream outputToClient;
	
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

	@Override
	public void run() {
		
		try {
			while(true) {
				
				//read msg from client
				inputFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				String recSentence = inputFromClient.readLine(); 
				
				//if the client disconnects, a final automatic message "_logout_" will be sent
				//so that this client can be removed
				if(recSentence.equals("_logout_")) {
					
					//let everyone connected to the server that this client disconnected
					for(ClientHelper client : MultiClientServer.clients) {
						if(!client.name.equals(this.name)) {
							client.outputToClient.writeBytes(this.name + " is disconnected" + '\n');
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
				
				//split the received message to get the recipient name and message separated
				String[] name_message = recSentence.split("name_message");
				
				//if the name is "send_to_everyone" then we know this message is for everyone
				if(name_message[0].equals("send_to_everyone")) {
					for(ClientHelper client : MultiClientServer.clients) {
						
						//send to everyone except the client that sent the message
						if(!client.name.equals(this.name)) {
							client.outputToClient.writeBytes(this.name + ": " + name_message[1] + '\n');
						}
					}
				}
				else {
					//this means the message is a private message
					for(ClientHelper client : MultiClientServer.clients) {
						if(client.name.equals(name_message[0])) {
							client.outputToClient.writeBytes(this.name + ": " + name_message[1] + '\n');
							break; //already found the recipient, so can stop looking
						}
					}
				}
			}
			
		}
		catch(Exception ex) {
			
		}
		
	}
}
