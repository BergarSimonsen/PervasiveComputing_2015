import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class CsvReader {

    private static ArrayList<String> AccX = new ArrayList<String>();
    private static ArrayList<String> AccY = new ArrayList<String>();
    private static ArrayList<String> AccZ = new ArrayList<String>();
    private static ArrayList<String> GyroX = new ArrayList<String>();
    private static ArrayList<String> GyroY = new ArrayList<String>();
    private static ArrayList<String> GyroZ = new ArrayList<String>();

    private static int COMPLEX_LENGTH = 182;
    private static int SIMPLE_LENGTH = 6;

    private static String OUT_FILE = "/home/bs/Desktop/csv_data/filereader/out_file.csv";
    //    private static String OUT_FOLDER = "/home/bs/Desktop/pervasiveData/";
    //    private static String OUT_FILE = OUT_FOLDER;

    private static boolean complex;
    private static String fName;
    private static String label;
    private static int len;
    
    public static void main(String[] args) {
	if(args.length < 2) {
	    System.out.println("Usage: <prog> simple/complex (true/false) filename label");
	    System.exit(1);
	}
	    
	int counter = 0;
	complex = Boolean.parseBoolean(args[0]);
	fName = args[1];
	label = args[2];
	len = 0;
	OUT_FILE += processOutFile(label);

	if (complex)
	    len = COMPLEX_LENGTH;
	else
	    len = SIMPLE_LENGTH;

	try {
	    //	    File file = new File("/home/bs/itu/pervasive_computing_SPCT/PervasiveComputing_2015/motion_data/new/Up.csv");
	    File file = new File(fName);
	    FileReader fileReader = new FileReader(file);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    StringBuffer stringBuffer = new StringBuffer();
	    String line;

	    System.out.println("Complex: " + complex);
	    System.out.println("Length: " + len);
	    System.out.println("Filename: " + fName);

	    while ((line = bufferedReader.readLine()) != null) {
		String tmp[] = line.split(",");
		System.out.println("!!!" + tmp.length);
		if(tmp.length == len) {
		    System.out.println("Pre process data");
		    System.out.println("Length: " + tmp.length);
		    processData(tmp, complex);
		}
		counter++;
	    }
	    fileReader.close();
	    write(complex);
	    System.out.println("Contents of file:");
	    //	    System.out.println(stringBuffer.toString());
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static String processOutFile(String input) {
	return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase() + ".csv";
    }

    public static void processData(String[] arr, boolean complex) {
	int ic = 1;

	int i = 0;
	System.out.println("PD: " + arr.length);
	while (i < arr.length) {
	    if(complex && i == 0) {
		i++;
		continue;
	    }

	    System.out.println("i: " + i);
	    System.out.println("arr.length: " + arr.length);

	    if(i + 5 <= arr.length) {
		AccX.add(arr[i]);
		AccY.add(arr[i+1]);
		AccZ.add(arr[i+2]);
		GyroX.add(arr[i+3]);
		GyroY.add(arr[i+4]);
		GyroZ.add(arr[i+5]);
	    }

	    if (!complex || (complex && i > 0))
		i += 6;
	}
    }

    public static void write(boolean complex) {
	if(!complex)
	    writeComplexData();
	else
	    writeSimpleData();
    }

    public static void writeComplexData() {
	BufferedWriter bWriter = null;
	try {
	    File file = new File(OUT_FILE);
	    FileWriter writer = new FileWriter(file);
	    bWriter = new BufferedWriter(writer);

	    String header = "";
	    for(int i = 0; i < 30; i++) {
		header += "AccX_" + i + ",";
		header += "AccY_" + i + ",";
		header += "AccZ_" + i + ",";
		header += "GyroX_" + i + ",";
		header += "GyroY_" + i + ",";
		header += "GyroZ_" + i + ",";
	    }
	    header += "Label \n";

	    int count = 0;

	    String s = header;

	    for (int i = 0; i < AccX.size(); i++) {
		s += AccX.get(i) + ",";
		s += AccY.get(i) + ",";
		s += AccZ.get(i) + ",";
		s += GyroX.get(i) + ",";
		s += GyroY.get(i) + ",";
		s += GyroZ.get(i);
		if(count == 29) {
		    s += "," + label;
		    s += "\n";
		    count = 0;
		} else {
		    s += ",";
		    count++;
		}
		
	    }

	    bWriter.write(s);
	    bWriter.flush();

	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
                // Close the writer regardless of what happens...
		if(bWriter != null)
		    bWriter.close();
            } catch (Exception e) {
            }
	}
    }

    public static void writeSimpleData() {
	BufferedWriter bWriter = null;
	try {
	    File file = new File(OUT_FILE);
	    FileWriter writer = new FileWriter(file);
	    bWriter = new BufferedWriter(writer);

	    String s = "";

	    for (int i = 0; i < AccX.size(); i++) {
		s += AccX.get(i) + ",";
		s += AccY.get(i) + ",";
		s += AccZ.get(i) + ",";
		s += GyroX.get(i) + ",";
		s += GyroY.get(i) + ",";
		s += GyroZ.get(i) + "\n";
	    }

	    bWriter.write(s);
	    bWriter.flush();

	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
                // Close the writer regardless of what happens...
		if(bWriter != null)
		    bWriter.close();
            } catch (Exception e) {
            }
	}
    }



    public static String printArray(String[] arr, int limit) {
	String s = "";
	for (int i = 0; i < limit; i++)
	    s += arr[i] + " | ";

	return s;
    }
}
