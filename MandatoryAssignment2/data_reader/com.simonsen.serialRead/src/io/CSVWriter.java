package io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import model.Data;

public class CSVWriter {

	public synchronized void generateCsvFile(ArrayList<Data> dataList) {
		try	{
			
			int count = 30;
			
//			String header = "TimeStamp, AccXAxis, AccYAxis, AccZAxis, GyroXAcis, GyroYAxis, GyroZAxis\n";
			
			FileWriter writer = new FileWriter("/home/bs/Desktop/output.csv");
			
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
