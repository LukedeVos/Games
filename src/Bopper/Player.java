package Bopper;

import game.SpriteSheet;
import Bopper.Main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {
	
	public double x;
	public double y;
	
	public int col = 1;
	public int row = 1;
	
	public double velX = 0;
	public double velY = 0;
	
	private BufferedImage player;
	
	public Player(double x, double y, Main game){
		this.x = x;
		this.y = y;
	}
	
	public void tick(Main game){
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
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		player = ss.grabImage(col, row, 16, 16);
	}
	
	public void render(Graphics g){
		g.drawImage(player, (int)x, (int)y, null);
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
	public void setCol(int col){
		this.col = col;
	}
	public void setRow(int row){
		this.row = row;
	}
}
