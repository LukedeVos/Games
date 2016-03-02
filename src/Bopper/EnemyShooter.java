package Bopper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EnemyShooter {
	
	private double x;
	private double y;
	
	private BufferedImage shooter;
	
	public EnemyShooter(double x, double y, Main game){
		this.x = x;
		this.y = y;
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		shooter = ss.grabImage(3, 1, 16, 16);
	}
	
	public void render(Graphics g){
		g.drawImage(shooter, (int)x, (int)y, null);
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
