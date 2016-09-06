package RPG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
//import java.awt.image.BufferedImage;
//import java.util.ArrayList;

public class Player extends Rectangle {

	private static final long serialVersionUID = 1L;

	public int x, y, size, health = 100, velX = 0, velY = 0, oldH = 100, newH = 100, direction = 2;
	public boolean dead, damaged, invincible;
//	private ArrayList<BufferedImage> img;
	public String hS;

	public Player(int x, int y, int size, Main game, Inventory inventory){
		this.x = x;
		this.y = y;
		this.size = size;
	}

	public void tick(){
		x += velX;
		y += velY;
		setBounds(x, y, size, size);
		if(health <= 0){
			health = 0;
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

	public void render(Graphics2D g){
		if(damaged){
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.WHITE);
		}
		g.fillRect(x, y, size, size);
		if(Main.showBounds){
			g.setColor(Color.RED);
			g.drawRect(x, y, size, size);
		}
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setPlayer(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setVelX(int velX){
		this.velX = velX;
	}

	public void setVelY(int velY){
		this.velY = velY;
	}

	public void setSpeed(int velX, int velY){
		this.velX = velX;
		this.velY = velY;
	}

	public void setHealth(int health){
		this.health = health;
	}

	public void setDirection(int direction){
		this.direction = direction;
	}

	public void setInvincible(boolean invincible){
		this.invincible = invincible;
	}
}
