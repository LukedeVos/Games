package RPG;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Inventory {
	
	public int x,y;
	
	public boolean occupied;
	
	private BufferedImage GUI;
	
	public Inventory(int x, int y, Main game){
		this.x = x;
		this.y = y;
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("GUI"));
		GUI = ss.grabImage(0, 0, 300, 300, 300);
	}
	
	public void render(Graphics2D g){
		g.drawImage(GUI, x, y, null);
	}
	
}
