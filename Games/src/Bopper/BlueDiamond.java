package Bopper;

public class BlueDiamond {
	
	public double x;
	public double y;
	
	public BlueDiamond(double diaX, double diaY, Main game){
		this.x = diaX;
		this.y = diaY;
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(double diaX){
		this.x = diaX;
	}
	public void setY(double diaY){
		this.y = diaY;
	}
}