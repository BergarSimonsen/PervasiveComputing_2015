package io;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class DataServer {
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	
	PrintWriter out = null;
	BufferedReader in = null;
	
	private boolean readFromClient = true;
	private final long THREAD_DELAY = 2000; 
	
	private boolean serverStarted = false;
	
	public DataServer(final int port) {
		initServerSocket(port);
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!serverStarted) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Done sleeping: " + serverStarted);
				initClientSocket();
				readFromClient();
			}
		});
		t.start();
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				initSockets(port);
//			}
//		});
//		t.start();
//		
//		Thread t2 = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				printFromClient();
//				
//			}
//		});
//		t2.start();
	}
	
	private void initServerSocket(int port) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server listening on port: " + port);
			serverStarted = true;
		} catch (IOException e) {
			System.out.println("Could not listen on port: " + port);
			e.printStackTrace();
		}
	}
	
	public boolean sendMsg(String msg) {
		if(clientSocket != null) {
			DataOutputStream dOut;
			try {
				dOut = new DataOutputStream(clientSocket.getOutputStream());
				
				// Send first message
//				dOut.writeByte(1);
//				dOut.writeUTF(msg);
				dOut.write(msg.getBytes(Charset.forName("UTF-8")));
				dOut.flush(); 
				System.out.println("Message sent to: " + clientSocket.getInetAddress().toString());
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		System.out.println("Send message: clientSocket == null");
		return false;
	}
	

	
	public void closeSockets() {
		try {
			if(clientSocket != null)
				clientSocket.close();
			if(serverSocket != null)
				serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initClientSocket() {
		try {
//			 System.out.println("Waiting for connection...");
//             Socket client = server.accept();
//             System.out.println("Incoming connection - Attempting to establish connection...");
//             ConnectionManager manager = new ConnectionManager(client, dispatcher, this);
//             manager.start();
//         }
			if(serverSocket != null) {
				System.out.println("waiting for client");
				while(true) {
					clientSocket = serverSocket.accept();
					System.out.println("client: " + clientSocket.isConnected());
					if(clientSocket.isConnected())
						break;
				}
				
				System.out.println("Client connected: " + clientSocket.getInetAddress().toString());
			} else {
				System.out.println("Server socket == null!");
			}
		} catch (IOException e) {
			System.out.println("Could not initialize client socket!");
			e.printStackTrace();
		}
	}
	
	public void readFromClient() {
		try {
			if(clientSocket != null) {
				
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				
				String inputLine = "";
				
				while(readFromClient) {
					if((inputLine = in.readLine()) != null) {
						System.out.println("From client: " + inputLine);
					} else {
						try {
							Thread.sleep(THREAD_DELAY);
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
					}
				}
			} else {
				System.out.println("Client socket == null!");
			}
		} catch (IOException e) {
			System.out.println("Error reading data from client!");
			e.printStackTrace();
		}
	}
}

//private void printFromClient() {
//try {
//	if(clientSocket != null) {
//		System.out.println("listening to client");
////		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//		String textFromClient =null;
//		String textToClient =null;
//		textFromClient = in.readLine(); // read the text from client
//		System.out.println("client: " + textFromClient);
////		if( textFromClient.equals("A")){
////			textToClient = "1111";
////		}else if ( textFromClient.equals("B")){
////			textToClient = "2222\r\n3333";
////		}
//	} else {
//		System.out.println("client socket is null :/");
//	}
//	
//} catch (Exception e) {
//	e.printStackTrace();
//}
//}