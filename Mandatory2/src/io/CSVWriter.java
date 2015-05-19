package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Data;

public class CSVWriter {
	
	private static final int C = 30;
	
	public synchronized boolean deleteCsvFile(String fileName) {
		try {
    		File file = new File(fileName);
    		if(file.delete()) return true;
    		else return false;
    	} catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
	
	private void stripList(ArrayList<Data> list) {
		while(list.size() < 30) {
			list.remove(0);
		}
	}
	
	public synchronized boolean writeCsvFile(ArrayList<Data> dataList, String filename, String label) {
		FileWriter writer = null;
		try {
			int count = C;
			writer = new FileWriter(filename);
			
			stripList(dataList);
			
			// Write header
//			writer.append("TimeStamp");
			for(int i = 0; i < count; i++) {
//				writer.append(',');
				writer.append("AccX_" + i);
				writer.append(',');
				writer.append("AccY_" + i);
				writer.append(',');
				writer.append("AccZ_" + i);
				writer.append(',');
				writer.append("GyrX_" + i);
				writer.append(',');
				writer.append("GyrY_" + i);
				writer.append(',');
				writer.append("GyrZ_" + i);
				writer.append(',');
			}
//			writer.append(",");
			writer.append("Label");
			writer.append('\n');
			
			// write value
			int j = 0;
			for(Data d : dataList) {
				writer.append(d.writeValues(false));
				if(j == count - 1) {
					writer.append(label);
					writer.append('\n');
					j = 0;
				} else {
					j++;
				}
			}

			// Flush and close writer
			writer.flush();
			writer.close();
			System.out.println("Finished writing csv file.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			if(writer != null)
				try {
					writer.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			return false;
		}
	}

	public synchronized void generateCsvFile(ArrayList<Data> dataList) {
		try	{
			
			int count = 30;
			
			FileWriter writer = new FileWriter("/home/bs/Desktop/csv_data/output.csv");
			
			// Write header
			writer.append("TimeStamp");
			for(int i = 0; i < count; i++) {
				writer.append(',');
				writer.append("AccX_" + i);
				writer.append(',');
				writer.append("AccY_" + i);
				writer.append(',');
				writer.append("AccZ_" + i);
				writer.append(',');
				writer.append("GyrX_" + i);
				writer.append(',');
				writer.append("GyrY_" + i);
				writer.append(',');
				writer.append("GyrZ_" + i);
			}
			writer.append(",");
			writer.append("Label");
			writer.append('\n');
			
			// write value
			int j = 0;
			for(Data d : dataList) {
				writer.append(d.writeValues((j == 0)));
//				System.out.println(d.writeValues(false));
				if(j == count - 1) {
					writer.append("TILT_L,");
					writer.append('\n');
					j = 0;
				} else {
					j++;
				}
			}
			
			// Flush and close writer
			writer.flush();
			writer.close();
			System.out.println("Finished writing csv file.");
		}
		catch(IOException e) {
			e.printStackTrace();
		} 
	}
}
