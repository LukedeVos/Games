package RPG;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Inventory {
	
	public int x,y,id = -1;
	
	public boolean occupied;
	
	private BufferedImage GUI, item0;
	
	public Inventory(int x, int y, Main game){
		this.x = x;
		this.y = y;
		
		SpriteSheet IF = new SpriteSheet(game.getSpriteSheet("GUI"));
		SpriteSheet item = new SpriteSheet(game.getSpriteSheet("items"));
		GUI = IF.grabImage(0, 0, 30, 30, 30);
		item0 = item.grabImage(0, 0, 20, 20, 20);
	}
	
	public void render(Graphics2D g){
		g.drawImage(GUI, x, y, null);
		if(id == 0){
			g.drawImage(item0, x + 5, y + 5, null);
		}
	}
	
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
	public void setID(int id){
		this.id = id;
	}
	
}
