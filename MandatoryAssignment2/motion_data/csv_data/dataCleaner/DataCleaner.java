import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class DataCleaner {


    private static String OUT_FOLDER = "/home/bs/Desktop/csv_data/";
    private static String IN_FOLDER = "/home/bs/Desktop/csv_data/test_data/";
    private static String OUT_FILE = OUT_FOLDER + "out.csv";

    private static boolean transform = false;
    private static boolean smooth = false;

    // Input files
    private static final String FILE_UP     = IN_FOLDER + "Up.csv";
    private static final String FILE_DOWN   = IN_FOLDER + "Down.csv";
    private static final String FILE_RIGHT  = IN_FOLDER + "Right.csv";
    private static final String FILE_LEFT   = IN_FOLDER + "Left.csv";
    private static final String FILE_TILT_R = IN_FOLDER + "TiltR.csv";
    private static final String FILE_TILT_L = IN_FOLDER + "TiltL.csv";
    private static final String FILE_IDLE   = IN_FOLDER + "Idle.csv";

    private static String[] labels = {"UP", "DOWN", "RIGHT", "LEFT", "TILT_R", "TILT_L", "IDLE"};

    // Output files
    private static final String OUT_FILE_UP     = OUT_FOLDER + "Out_Up.csv";
    private static final String OUT_FILE_DOWN   = OUT_FOLDER + "Out_Down.csv";
    private static final String OUT_FILE_RIGHT  = OUT_FOLDER + "Out_Right.csv";
    private static final String OUT_FILE_LEFT   = OUT_FOLDER + "Out_Left.csv";
    private static final String OUT_FILE_TILT_R = OUT_FOLDER + "Out_TiltR.csv";
    private static final String OUT_FILE_TILT_L = OUT_FOLDER + "Out_TiltL.csv";
    private static final String OUT_FILE_IDLE   = OUT_FOLDER + "Out_Idle.csv";

    private static String[] inFiles;
    private static String[] outFiles;
    private static CsvData[] data;

    public static void main(String[] args) {
	inFiles = new String[7];
	inFiles[0] = FILE_UP;
	inFiles[1] = FILE_DOWN;
	inFiles[2] = FILE_RIGHT;
	inFiles[3] = FILE_LEFT;
	inFiles[4] = FILE_TILT_R;
	inFiles[5] = FILE_TILT_L;
	inFiles[6] = FILE_IDLE;

	outFiles = new String[7];
	outFiles[0] = OUT_FILE_UP;
	outFiles[1] = OUT_FILE_DOWN;
	outFiles[2] = OUT_FILE_RIGHT;
	outFiles[3] = OUT_FILE_LEFT;
	outFiles[4] = OUT_FILE_TILT_R;
	outFiles[5] = OUT_FILE_TILT_L;
	outFiles[6] = OUT_FILE_IDLE;

	data = new CsvData[7];

	for (int k = 0; k < labels.length; k++)
	    data[k] = new CsvData(labels[k]);

	try {
	    for (int i = 0; i < inFiles.length; i++) {
		System.out.println("Reading file: " + inFiles[i]);
		File file = new File(inFiles[i]);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = "";

		while ((line = bufferedReader.readLine()) != null) {
		    String tmp[] = line.split(",");
		    processData(tmp, i);
		}

		bufferedReader.close();
		fileReader.close();
		bufferedReader = null;
		fileReader = null;
		file = null;
	    }

	    System.out.println("Finished Reading");

	    smoothData();

	    System.out.println("Finished smooth data");

	    write();
	    
	    System.out.println("Finished writing");

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void smoothData() {
	System.out.println("Inside smoothData");
	if(smooth) {
	    for(CsvData d : data) {
		System.out.println("Processing " + d.getLabel());
		ArrayList<String> ax = new ArrayList<String>(d.getAccX());
		ArrayList<String> ay = new ArrayList<String>(d.getAccY());
		ArrayList<String> az = new ArrayList<String>(d.getAccZ());
		ArrayList<String> gx = new ArrayList<String>(d.getGyroX());
		ArrayList<String> gy = new ArrayList<String>(d.getGyroY());
		ArrayList<String> gz = new ArrayList<String>(d.getGyroZ());
		
		//		System.out.println(ax.size());

		d.getAccX().clear();
		d.getAccY().clear();
		d.getAccZ().clear();
		d.getGyroX().clear();
		d.getGyroY().clear();
		d.getGyroZ().clear();

		for(int i = 0; i < ax.size(); i++) {
		    //		    System.out.println("valid: " + (i + 4 < ax.size()));
		    if(i + 4 < ax.size()) {
			Double[] tmp = new Double[5];
			tmp[0] = Double.parseDouble(ax.get(i));
			tmp[1] = Double.parseDouble(ax.get(i+1));
			tmp[2] = Double.parseDouble(ax.get(i+2));
			tmp[3] = Double.parseDouble(ax.get(i+3));
			tmp[4] = Double.parseDouble(ax.get(i+4));
			d.getAccX().add(calcAverage(tmp));
		    }
		}

		for(int i = 0; i < ay.size(); i++) {
		    if(i + 4 < ay.size()) {
			Double[] tmp = new Double[5];
			tmp[0] = Double.parseDouble(ay.get(i));
			tmp[1] = Double.parseDouble(ay.get(i+1));
			tmp[2] = Double.parseDouble(ay.get(i+2));
			tmp[3] = Double.parseDouble(ay.get(i+3));
			tmp[4] = Double.parseDouble(ay.get(i+4));
			d.getAccY().add(calcAverage(tmp));
		    }
		}

		for(int i = 0; i < az.size(); i++) {
		    if(i + 4 < az.size()) {
			Double[] tmp = new Double[5];
			tmp[0] = Double.parseDouble(az.get(i));
			tmp[1] = Double.parseDouble(az.get(i+1));
			tmp[2] = Double.parseDouble(az.get(i+2));
			tmp[3] = Double.parseDouble(az.get(i+3));
			tmp[4] = Double.parseDouble(az.get(i+4));
			d.getAccZ().add(calcAverage(tmp));
		    }
		}

		for(int i = 0; i < gx.size(); i++) {
		    if(i + 4 < gx.size()) {
			Double[] tmp = new Double[5];
			tmp[0] = Double.parseDouble(gx.get(i));
			tmp[1] = Double.parseDouble(gx.get(i+1));
			tmp[2] = Double.parseDouble(gx.get(i+2));
			tmp[3] = Double.parseDouble(gx.get(i+3));
			tmp[4] = Double.parseDouble(gx.get(i+4));
			d.getGyroX().add(calcAverage(tmp));
		    }
		}

		for(int i = 0; i < gy.size(); i++) {
		    if(i + 4 < gy.size()) {
			Double[] tmp = new Double[5];
			tmp[0] = Double.parseDouble(gy.get(i));
			tmp[1] = Double.parseDouble(gy.get(i+1));
			tmp[2] = Double.parseDouble(gy.get(i+2));
			tmp[3] = Double.parseDouble(gy.get(i+3));
			tmp[4] = Double.parseDouble(gy.get(i+4));
			d.getGyroY().add(calcAverage(tmp));
		    }
		}

		for(int i = 0; i < gz.size(); i++) {
		    if(i + 4 < gz.size()) {
			Double[] tmp = new Double[5];
			tmp[0] = Double.parseDouble(gz.get(i));
			tmp[1] = Double.parseDouble(gz.get(i+1));
			tmp[2] = Double.parseDouble(gz.get(i+2));
			tmp[3] = Double.parseDouble(gz.get(i+3));
			tmp[4] = Double.parseDouble(gz.get(i+4));
			d.getGyroZ().add(calcAverage(tmp));
		    }
		}
		printDebug(d);
	    }
	}
    }

    private static void printDebug(CsvData d) {
	String s = d.getLabel();
	s += "AccX: " + d.getAccX().size();
	s += "AccY: " + d.getAccY().size();
	s += "AccZ: " + d.getAccZ().size();
	s += "GyroX: " + d.getGyroX().size();
	s += "GyroY: " + d.getGyroY().size();
	s += "GyroZ: " + d.getGyroZ().size();
	System.out.println(s);
    }

    private static String calcAverage(Double[] vals) {
	double sum = 0.0;
	for(int i = 0; i < vals.length; i++) {
	    sum += sum + vals[i];
	}
	return String.valueOf(sum / vals.length);
    }

    public static void processData(String[] arr, int index) {
	int ic = 1;
	int i = 0;
	while (i < arr.length) {
	    /*	    if(complex && i == 0) {
		i++;
		continue;
		} */


	    if(i + 5 <= arr.length) {
		data[index].getAccX().add(arr[i]);
		data[index].getAccY().add(arr[i+1]);
		data[index].getAccZ().add(arr[i+2]);
		data[index].getGyroX().add(arr[i+3]);
		data[index].getGyroY().add(arr[i+4]);
		data[index].getGyroZ().add(arr[i+5]);
	    }

	    i += 6;
	}
    }

    private static void cleanupList(ArrayList<String> list) {
	if(list.size() % 30 != 0) {
	    list.remove(list.size() - 1);
	    cleanupList(list);
	}
    }

    public static void write() {
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


	    for (CsvData d : data) {
		System.out.println("Writing file: " + d.getLabel());

		// Make sure all lists have length % 30 == 0
		
		cleanupList(d.getAccX());
		cleanupList(d.getAccY());
		cleanupList(d.getAccZ());
		cleanupList(d.getGyroX());
		cleanupList(d.getGyroY());
		cleanupList(d.getGyroZ());

		int len = d.getAccX().size();
		if(d.getAccY().size() < len)
		    len = d.getAccY().size();
		if(d.getAccZ().size() < len)
		    len = d.getAccZ().size();
		if(d.getGyroX().size() < len)
		    len = d.getGyroX().size();
		if(d.getGyroY().size() < len)
		    len = d.getGyroY().size();
		if(d.getGyroZ().size() < len)
		    len = d.getGyroZ().size();
		
		System.out.println("len: " + len);
		

		for (int i = 0; i < len; i++) {
		    s += d.getAccX().get(i) + ",";
		    s += d.getAccY().get(i) + ",";
		    s += d.getAccZ().get(i) + ",";
		    s += d.getGyroX().get(i) + ",";
		    s += d.getGyroY().get(i) + ",";
		    s += d.getGyroZ().get(i);
		    if(count == 29) {
			s += "," + d.getLabel();
			s += "\n";
			count = 0;
		    } else {
			s += ",";
			count++;
		    }
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
}
