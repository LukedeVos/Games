package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Enemy extends Rectangle{

	private static final long serialVersionUID = 1L;
	public int x, y, id, size, mX, mY, health;
	public boolean inMap = true;
	private BufferedImage en0, en1;
		
	public Enemy(int x, int y, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		
		if(id == 0){
			size = 20;
			health = 50;
		}
		
		setBounds(x, y, size, size);
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("Enemies"));
		en0 = ss.grabImage(0, 0, 20, 20, 20);
		en1 = ss.grabImage(1, 0, 20, 20, 20);
	}
		
	public void render(Graphics2D g){
		if(inMap){
			if(id == 0){
				g.drawImage(en0, x, y, null);
			} else if(id == 1){
				g.drawImage(en1, x, y, null);
			} else if(id == 100){
				
			}
		}
	}
		
	public void setID(int newID){
		id = newID;
	}	
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setItem(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}
	public void setIM(boolean inMap){
		this.inMap = inMap;
	}
	public void setHealth(int health){
		this.health = health;
	}
}

