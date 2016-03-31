package RPG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player extends Rectangle {
	
	private static final long serialVersionUID = 1L;

	public int x,y, size;
	public int health = 100;
	public int velX = 0;
	public int velY = 0;
	public int oldH = 100, newH = 100;
	
	public boolean dead, damaged;
	
	public Player(int x, int y, int size, Main game){
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void tick(){
		x+=velX;
		y+=velY;
		setBounds(x, y, size, size);
		if(health <= 0){
			dead = true;
		}
		newH = health;
		if(newH < oldH){
			damaged = true;
		} else {
			damaged = false;
		}
		oldH = newH;
	}
	
	public void render(Graphics2D g) {
		if(damaged){
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.WHITE);
		}
		g.fillRect(x, y, size, size);
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
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setVelX(int velX){
		this.velX = velX;
	}
	public void setVelY(int velY){
		this.velY = velY;
	}
	public int getHealth(){
		return health;
	}
	public void setHealth(int health){
		this.health = health;
	}
	public boolean getDead(){
		return dead;
	}
}
