package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Block extends Rectangle{
	private static final long serialVersionUID = 1L;
	public int x,y,size;
	private int id;
	private boolean solid;
	
	
	private BufferedImage tile0, tile1;
	
	public Block(int x, int y, int size, Main game) {
		this.x = x;
		this.y = y;
		this.size = size;
		setBounds(x, y, size, size);
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		tile0 = ss.grabImage(1, 1, 16, 16);
		tile1 = ss.grabImage(1, 2, 16, 16);
	}
	
	public void render(Graphics2D g) {
		if(id == 0){
			g.drawImage(tile0, x, y, null);
		} else if(id == 1){
			g.drawImage(tile1, x, y, null);
		}
	}
	
	public void setID(int newID) {
		id = newID;
		if(id == 0) {
			solid = false;
		} else if(id == 1) {
			solid = true;
		}
	}
	
	public int getID() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}
}
