package io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DataServer {
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	
	public DataServer(int port) {
		initSockets(port);
	}
	
	public boolean sendMsg(String msg) {
		if(clientSocket != null) {
			DataOutputStream dOut;
			try {
				dOut = new DataOutputStream(clientSocket.getOutputStream());
				// Send first message
				dOut.writeByte(1);
				dOut.writeUTF(msg);
				dOut.flush(); 
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	private void initSockets(int port) {
		try {
			serverSocket =  new ServerSocket(port);
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}

//public static void main(String[] args) throws IOException {
//    ServerSocket listener = new ServerSocket(9090);
//    try {
//        while (true) {
//            Socket socket = listener.accept();
//            try {
//                PrintWriter out =
//                    new PrintWriter(socket.getOutputStream(), true);
//                out.println(new Date().toString());
//            } finally {
//                socket.close();
//            }
//        }
//    }
//    finally {
//        listener.close();
//    }
//}
