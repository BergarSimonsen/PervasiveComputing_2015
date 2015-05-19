package model;

public class Data {
	private AccelerometerData acc;
	private GyroData gyro;
	private long timeStamp;
	
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String writeValues(boolean timestamp) {
		String s = ""; 
		if(timestamp)
			s += timeStamp + ","; 
		s += acc.getxAxis() + ",";
		s += acc.getyAxis() + ",";
		s += acc.getzAxis() + ",";
		s += gyro.getxAxis() + ",";
		s += gyro.getyAxis() + ",";
		s += gyro.getzAxis() + ",";

		return s;
	}

	public AccelerometerData getAcc() {
		return acc;
	}

	public void setAcc(AccelerometerData acc) {
		this.acc = acc;
	}

	public GyroData getGyro() {
		return gyro;
	}

	public void setGyro(GyroData gyro) {
		this.gyro = gyro;
	}

}
