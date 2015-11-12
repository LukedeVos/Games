package Bopper;

public class EnemyShooter {
	
	private double x;
	private double y;
	
	
	public EnemyShooter(double x, double y, Main game){
		this.x = x;
		this.y = y;
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
