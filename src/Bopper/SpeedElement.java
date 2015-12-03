package Bopper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SpeedElement {
	
	private double x;
	private double y;
	
	private BufferedImage speedElement;
	
	public SpeedElement(double x, double y, Main game){
		this.x = x;
		this.y = x;
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		speedElement = ss.grabImage(3, 2, 16, 16);
	}
	
	public void render(Graphics g){
		g.drawImage(speedElement, (int)x, (int)y, null);
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
