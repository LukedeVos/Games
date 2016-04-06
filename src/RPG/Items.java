package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Items extends Rectangle{

	private static final long serialVersionUID = 1L;
	public int x,y;
	private int id;

	private BufferedImage item0;
		
	public Items(int x, int y, int size, Main game){
		this.x = x;
		this.y = y;
		setBounds(x, y, size, size);
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("items"));
		item0 = ss.grabImage(0, 0, size, size);
	}
		
	public void render(Graphics2D g){
		if(id == 0){
			g.drawImage(item0, x, y, null);
		} else if(id == 1){
			g.drawImage(item0, x, y, null);
		} else if (id == 2){
			g.drawImage(item0, x, y, null);
		}
	}
		
	public void setID(int newID){
		id = newID;
		if(id == 0){
			
		} else if(id == 1){
				
		} else if(id == 2){

		}
	}
		
	public int getID(){
		return id;
	}
		
}