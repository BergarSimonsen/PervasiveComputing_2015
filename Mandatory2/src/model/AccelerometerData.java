package model;

public class AccelerometerData {
	private double xAxis;
	private double yAxis;
	private double zAxis;
	
	public double getxAxis() {
		return xAxis;
	}
	
	public void setxAxis(double xAxis) {
		this.xAxis = xAxis;
	}
	
	public double getyAxis() {
		return yAxis;
	}
	
	public void setyAxis(double yAxis) {
		this.yAxis = yAxis;
	}
	
	public double getzAxis() {
		return zAxis;
	}
	
	public void setzAxis(double zAxis) {
		this.zAxis = zAxis;
	}
	
	@Override
	public String toString() {
		String s = String.format("x: %f y: %f x: %f", xAxis, yAxis, zAxis);
		return s;
	}
}
