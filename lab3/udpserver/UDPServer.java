//Ajevan Nadarajah
package lab3.udpserver;

import java.net.*;

public class UDPServer { 

	public static void main(String[] args) {
		try {
			DatagramSocket serverSocket = new DatagramSocket(65000);
			
			//read a message
			DatagramPacket packet;
			String recString;
			byte[] data; //buffer
			
			while(true) {
				
				data = new byte[1024];
				packet = new DatagramPacket(data, data.length);
				
				//to read msg from client
				serverSocket.receive(packet); //receive packet
				
				//convert data to string
				recString = new String(packet.getData());
				
				String[] stringNumbers = recString.split(",");
				int size = Integer.parseInt(stringNumbers[0]); //how many numbers the user sent
				int[] numbers = new int[size]; //the numbers the user sent
				
				for(int i = 1; i <= size; i++) {
					numbers[i - 1] = Integer.parseInt(stringNumbers[i]);
				}
				
				
				//send reply
				int clientPortNumber = packet.getPort();
				InetAddress IPAddress = packet.getAddress();
				
				String result = "4,";
				result += sum(numbers) + ",";
				result += average(numbers) + ",";
				result += max(numbers) + ",";
				result += min(numbers) + ",";
				result += "E";
				
				data = result.getBytes();
				
				packet = new DatagramPacket(data, data.length, IPAddress, clientPortNumber);
				serverSocket.send(packet);
			}
		}
		catch(Exception ex) {
			
		}

	}
	
	public static int sum(int[] a) {
		int total = 0;
		
		for(int i =0; i < a.length; i++) {
			total += a[i];
		}
		return total;
	}
	
	public static double average(int[] a) {
		return sum(a) / (double)a.length;
	}
	
	public static int max(int[] a) {
		int max = 0;
		
		for(int i = 1; i < a.length; i++) {
			if(a[i] > a[max]) {
				max = i;
			}
		}
		return a[max];
	}
	
	public static int min(int[] a) {
		int min = 0;
		
		for(int i = 1; i < a.length; i++) {
			if(a[i] < a[min]) {
				min = i;
			}
		}
		return a[min];
	}

}
