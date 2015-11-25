package Bopper;

public class SpeedElement {
	
	private double x;
	private double y;
	
	public SpeedElement(double x, double y, Main game){
		this.x = x;
		this.y = x;
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
}
