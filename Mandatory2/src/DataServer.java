import io.CSVWriter;

import java.io.IOException;
import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import model.Data;
 
/** 
 * @ServerEndpoint gives the relative name for the end point
 * This will be accessed via ws://localhost:8080/EchoChamber/echo
 * Where "localhost" is the address of the host,
 * "EchoChamber" is the name of the package
 * and "echo" is the address to access this class from the server
 */
@ServerEndpoint("/echo") 
public class DataServer {
	
	private static String TMP_DATA_LOCATION = "/home/bs/Desktop/tmp_data/";
	private static String TMP_FILE = TMP_DATA_LOCATION + "outFile.csv";
    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was 
     * successful.
     */
    @OnOpen
    public void onOpen(final Session session){
        System.out.println(session.getId() + " has opened a connection"); 
//        Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					main(session);
//				} catch (IOException e) {
//					System.out.println("ERROR STARTING MAIN THREAD!");
//					e.printStackTrace();
//				}
//			}
//		});
//        t.start();
//        try {
//			Thread.sleep(999999999);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        
        try {
            session.getBasicRemote().sendText("Connection Established");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
			main(session);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void main(Session session) throws IOException {
//    	readData();
		SerialTest st = new SerialTest();
//		st.dataServer = new DataServer(SERVER_PORT);
		st.initialize();
		Classifier c = new Classifier();
		CSVWriter w = new CSVWriter();
//		c.classify(st.getInput());
		
		
		Thread countThread = st.initCounterThread();
		countThread.start();
		
		while(true) {
			if(st.getDataListSize() > 29) {
				System.out.println("----------------" + st.dataList.size());
				ArrayList<Data> tmp = st.copyList(st.dataList);
				st.dataList.clear();
//				System.out.println("----------------" + dataList.size());
//				System.out.println("----------------" + tmp.size());
				
				
				boolean success = w.writeCsvFile(tmp, TMP_FILE, "LBL");
				
//				System.out.println("TEST");
				System.out.println("1");
				if(success) {
					System.out.println("2");
					String g = c.classify2(TMP_FILE);
					System.out.println("3");
					if(g != null && g.length() > 0) {
						System.out.println("4");
//						if(session.isOpen())
//							session.getBasicRemote().sendText("TILT_R");
//						session.getAsyncRemote().sendText("UP");
						session.getBasicRemote().sendText(g);
						System.out.println("5");
					}
					System.out.println("6");
//						st.sendGesture(g);
					w.deleteCsvFile(TMP_FILE);
					System.out.println("7");
//					c.classify(TEST_DATA);
					success = false;
					System.out.println("8");
				}
			}
		}
    }
 
    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(String message, Session session){
        System.out.println("Message from " + session.getId() + ": " + message);
//        try {
//            session.getBasicRemote().sendText(message);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
 
    /**
     * The user closes the connection.
     * 
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
    }
}