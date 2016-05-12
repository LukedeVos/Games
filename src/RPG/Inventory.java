package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Inventory extends Rectangle{

	private static final long serialVersionUID = 1L;

	public int x,y,id = -1, mX, mY;
	
	public boolean occupied, selected;
	
	private BufferedImage GUI, item0;
	
	public Inventory(int x, int y, Main game){
		this.x = x;
		this.y = y;
		setBounds(x, y, 30, 30);
		
		
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
		
		if(selected){
			
		}
	}
	
	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}
	public void setID(int id){
		this.id = id;
	}
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}
}
