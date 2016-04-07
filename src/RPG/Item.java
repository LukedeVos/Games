package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Item extends Rectangle{

	private static final long serialVersionUID = 1L;
	public int x,y,id;

	private BufferedImage item0, itemNull;
		
	public Item(int x, int y, int size, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		setBounds(x, y, size, size);
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("items"));
		item0 = ss.grabImage(0, 0, size, size);
	}
		
	public void render(Graphics2D g){
		if(id == 0){
			g.drawImage(item0, x, y, null);
		} else if(id == 100){
			
		}
	}
		
	public void setID(int newID){
		id = newID;
	}
		
	public int getID(){
		return id;
	}
		
}