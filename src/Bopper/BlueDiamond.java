package Bopper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Bopper.SpriteSheet;

public class BlueDiamond {
	
	public double x;
	public double y;
	
	private BufferedImage blueDiamond;
	
	public BlueDiamond(double diaX, double diaY, Main game){
		this.x = diaX;
		this.y = diaY;
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		blueDiamond = ss.grabImage(2, 2, 16, 16);
	}
	
	public void render(Graphics g){
		g.drawImage(blueDiamond, (int)x, (int)y, null);
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