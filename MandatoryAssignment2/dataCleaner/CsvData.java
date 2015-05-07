import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

public class CsvData {
    private ArrayList<String> AccX;
    private ArrayList<String> AccY;
    private ArrayList<String> AccZ;
    private ArrayList<String> GyroX;
    private ArrayList<String> GyroY;
    private ArrayList<String> GyroZ;

    private String label;

    public CsvData(String label) {
	AccX = new ArrayList<String>();
	AccY = new ArrayList<String>();
	AccZ = new ArrayList<String>();
	GyroX = new ArrayList<String>();
	GyroY = new ArrayList<String>();
	GyroZ = new ArrayList<String>();
	this.label = label;
    }

    public ArrayList<String> getAccX() { return AccX; }
    public ArrayList<String> getAccY() { return AccY; }
    public ArrayList<String> getAccZ() { return AccZ; }
    public ArrayList<String> getGyroX() { return GyroX; }
    public ArrayList<String> getGyroY() { return GyroY; }
    public ArrayList<String> getGyroZ() { return GyroZ; }
    public String getLabel() { return label; }

}
