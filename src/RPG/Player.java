package RPG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Player extends Rectangle {
	
	private static final long serialVersionUID = 1L;

	public int x,y, size;
	
	public int velX = 0;
	public int velY = 0;
	
	public Player(int x, int y, int size, Main game){
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public void tick(){
		x+=velX;
		y+=velY;
		setBounds(x, y, size, size);
	}
	
	public void render(Graphics2D g) {
		g.setColor(Color.WHITE);
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
}
