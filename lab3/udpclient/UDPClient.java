//Ajevan Nadarajah
package lab3.udpclient;

import java.net.*;
import java.util.Scanner;

public class UDPClient {

	public static void main(String[] args) {
		
		try {
			System.out.println("How many numbers do you want to send: ");
			Scanner intScanner = new Scanner(System.in);
			int size = intScanner.nextInt();
			String numbers = Integer.toString(size) + ",";
			
			System.out.println("Please enter " + size + " numbers");
			
			//get the numbers the user wants to send
			for(int i = 0; i < size; i++) {
				numbers += intScanner.nextInt() + ",";
			}
			numbers += "E";
			intScanner.close();
			
			DatagramSocket clientSocket = new DatagramSocket(); 
			
			byte[] data = new byte[1024];
			data = numbers.getBytes();
			
			int socketPortNumber = 65000;
			InetAddress IPAddress = InetAddress.getByName("localhost");
			
			DatagramPacket packet = new DatagramPacket(data, data.length, IPAddress, socketPortNumber);
			clientSocket.send(packet);
			
			
			//receive the server's reply
			byte[] recData = new byte[1024]; 
			DatagramPacket recPacket = new DatagramPacket(recData, recData.length);
			
			clientSocket.receive(recPacket);
			String recResults = new String(recPacket.getData());
			String[] results = recResults.split(","); //split on ","
			
			System.out.println("Sum = " + results[1]);
			System.out.println("Average = " + results[2]);
			System.out.println("Max = " + results[3]);
			System.out.println("Min = " + results[4]);
			
			clientSocket.close(); //close the socket
		}
		catch(Exception ex) {
			System.out.println("Invalid!");
		}

	}

}
