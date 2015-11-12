package Bopper;

public class Bullet {
	
	public double x;
	public double y;
	
	public double velX;
	public double velY;
	
	public Bullet(double x, double y, Main game){
		this.x = x;
		this.y = y;
	}
	
	public void tick(){
		x += velX;
		y += velY;
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getVelX(){
		return velX;
	}
	public double getVelY(){
		return velY;
	}
	public void setX(double x){
		this.x = x;
	}
	public void setY(double y){
		this.y = y;
	}
	public void setVelX(double velX){
		this.velX = velX;
	}
	public void setVelY(double velY){
		this.velY = velY;
	}
}
