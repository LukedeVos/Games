package Bopper;

import Bopper.Main;

public class Player {
	
	public double x;
	public double y;
	
	public double velX = 0;
	public double velY = 0;
	
	public Player(double x, double y, Main game){
		this.x = x;
		this.y = y;
	}
	
	public void tick(){
		x+=velX;
		y+=velY;
		
		if(x <= -11){
			x = 649;
		} else if(x >= 650){
			x = -10;
		}
		
		if(y <= -11){
			y = 479;
		} else if(y >= 480){
			y = -10;
		}
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
	public void setVelX(double velX){
		this.velX = velX;
	}
	public void setVelY(double velY){
		this.velY = velY;
	}
}
