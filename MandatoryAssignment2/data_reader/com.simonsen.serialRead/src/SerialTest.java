import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import io.CSVWriter;
import io.DataServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.TooManyListenersException;

import model.AccelerometerData;
import model.Data;
import model.GyroData;


public class SerialTest implements SerialPortEventListener {
	private static long timeStamp = 0;
	private static ArrayList<Data> dataList;
	SerialPort serialPort;
	private static final String PORT_NAMES[] = {"/dev/tty.usbserial-A9007UX1", "/dev/ttyACM0", "/dev/ttyUSB0", "COM3", "/dev/rfcomm0"};
	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	
	private static String TMP_DATA_LOCATION = "/home/bs/Desktop/tmp_data/";
	private static String TMP_FILE = TMP_DATA_LOCATION + "outFile.csv";
	
	private DataServer dataServer;
	public static final int SERVER_PORT = 6666;
	
	public BufferedReader getInput() { return input; };

	public void initialize() {
		dataList = new ArrayList<Data>();

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine = input.readLine();
				String[] d = inputLine.split(" ");
				if(d.length == 11) {
					AccelerometerData acc = new AccelerometerData();
					acc.setxAxis(Double.parseDouble(d[0]));
					acc.setyAxis(Double.parseDouble(d[2]));
					acc.setzAxis(Double.parseDouble(d[4]));
					
					GyroData gyro = new GyroData();
					gyro.setxAxis(Double.parseDouble(d[6]));
					gyro.setyAxis(Double.parseDouble(d[8]));
					gyro.setzAxis(Double.parseDouble(d[10]));
					
					Data data = new Data();
					data.setAcc(acc);
					data.setGyro(gyro);
					data.setTimeStamp(timeStamp);
//					String tmp = data.writeValues(false);
					timeStamp++;
					
					dataList.add(data);
					
//					System.out.println("line: " + inputLine);
					
//					System.out.println(dataList.size());
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	public static void readData() {
		SerialTest main = new SerialTest();
		main.initialize();
		
		boolean started = false;
		
		Thread t = null;
		Thread countThread = initCounterThread();
		countThread.start();
		
		while(true) {
			Scanner reader = new Scanner(System.in);
			System.out.println("S for start, Q for stop");
			// get user input for a
			String cmd = reader.next();
			
			if(cmd.equals("s") && !started) {
				started = true;
				System.out.println("cmd.START");
				t = new Thread() {
					public void run() {
						//the following line will keep this app alive for 1000 seconds,
						//waiting for events to occur and responding to them (printing incoming messages to console).
						try {Thread.sleep(500000000);} catch (InterruptedException ie) { System.out.println("inter" + dataList.size());}
						System.out.println("FINISH, LENGTH = " + dataList.size());
					}
				};
				t.start();
			} else if(cmd.equals("q")) {
				
				System.out.println("cmd.STOP");
				ArrayList<Data> finalList = copyList(dataList);
				System.out.println("fl: " + finalList.size());
				System.out.println("dl: " + dataList.size());
				CSVWriter w = new CSVWriter();
				w.generateCsvFile(finalList);
				if(t != null)
					t.interrupt();
				reader.close();
				System.exit(0);
			}
		}
	}
	
	private static boolean reading = false;
	
	public static void main(String[] args) throws Exception {
//		readData();
		SerialTest st = new SerialTest();
		st.dataServer = new DataServer(SERVER_PORT);
		st.initialize();
		Classifier c = new Classifier();
		CSVWriter w = new CSVWriter();
//		c.classify(st.getInput());
		
		
		Thread countThread = initCounterThread();
		countThread.start();
		
		while(true) {
			if(getDataListSize() > 29) {
				System.out.println("----------------" + dataList.size());
				ArrayList<Data> tmp = copyList(dataList);
				dataList.clear();
				System.out.println("----------------" + dataList.size());
				System.out.println("----------------" + tmp.size());
				
				
				boolean success = w.writeCsvFile(tmp, TMP_FILE, "LBL");
				
//				System.out.println("TEST");
				
				if(success) {
					c.classify2(TMP_FILE);
					w.deleteCsvFile(TMP_FILE);
//					c.classify(TEST_DATA);
					success = false;
				}
			}
		}
	}
	
	private final static String DATA_LOCATION = "/home/bs/Desktop/csv_data/";
	private final static String TEST_DATA = DATA_LOCATION + "test_data/test_data.csv";
	
//	private static final String DATA_LOCATION = "/home/bs/itu/pervasive_computing_SPCT/PervasiveComputing_2015/MandatoryAssignment2/motion_data/use_this/";
//	private static final String TEST_DATA = DATA_LOCATION + "test_data.csv";
//	private static final String TRAINING_DATA = DATA_LOCATION + "training_data.csv";
	
	private static Thread initCounterThread() {
		return new Thread() {
			public void run() {
				int count = 0;
//				Classifier c = new Classifier();
//				CSVWriter w = new CSVWriter();
				while(true) {
//					System.out.println("DataList: " + dataList.size());
//					ArrayList<Data> tmp = copyList(dataList);
//					clearList();
//					System.out.println("L: " + tmp.size());
//					System.out.println("DataList: " + dataList.size());
//					
//					boolean success = w.writeCsvFile(tmp, TMP_FILE, "LBL");
//					
////					System.out.println("TEST");
//					
//					if(success) {
//						c.classify(TMP_FILE);
//						w.deleteCsvFile(TMP_FILE);
////						c.classify(TRAINING_DATA);
//						success = false;
//					}
					System.out.println("START! " + count);
					count++;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
	}
	
	private static synchronized int getDataListSize() {
		return dataList.size();
	}
	
//	private static synchronized void clearList() {
//		dataList.clear();
//	}
	
	private static synchronized ArrayList<Data> copyList(ArrayList<Data> list) {
		ArrayList<Data> retval = new ArrayList<Data>(list);
		return retval;
	}
	
//	private synchronized void closeEventListener() {
//	serialPort.removeEventListener();
//}
//
//private synchronized void setEventListener() {
//	try {
//		serialPort.addEventListener(SerialTest.this);
//	} catch (TooManyListenersException e) {
//		e.printStackTrace();
//	}
//}	
}