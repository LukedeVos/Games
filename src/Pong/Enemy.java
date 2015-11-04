package Pong;

public class Enemy {
	
	public double eX;
	public double eY;
	
	public double eVelY;
	
	public Enemy(double eX, double eY, Main game){
		this.eX = eX;
		this.eY = eY;
	}
	
	public void tick(){
		eY += eVelY;
		
		if(eY <= 0){
			eY = 0;
		} else if(eY >= 390){
			eY = 390;
		}
	}
	
	public double getEX(){
		return eX;
	}
	public double getEY(){
		return eY;
	}
	public double getEVelY(){
		return eVelY;
	}
	public void setEX(double eX){
		this.eX = eX;
	}
	public void setEY(double eY){
		this.eY = eY;
	}
	public void setEVelY(double eVelY){
		this.eVelY = eVelY;
	}
	
}
