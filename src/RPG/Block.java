package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Block extends Rectangle{
	private static final long serialVersionUID = 1L;
	public int x, y, xSize, ySize;
	private int id;
	private boolean solid, pain;
	
	
	private BufferedImage tile0, tile1, tile2;
	
	public Block(int x, int y, int xSize, int ySize, Main game){
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
		setBounds(x, y, xSize, ySize);
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("tiles"));
		tile0 = ss.grabImage(0, 0, 20, xSize, xSize);
		tile1 = ss.grabImage(1, 0, 20, xSize, xSize);
		tile2 = ss.grabImage(2, 0, 20, xSize, xSize);
	}
	
	public void render(Graphics2D g){
		if(id == 0){
			g.drawImage(tile0, x, y, null);
		} else if(id == 1){
			g.drawImage(tile1, x, y, null);
		} else if (id == 2){
			g.drawImage(tile2, x, y, null);
		}
	}
	
	public void setID(int newID){
		id = newID;
		if(id == 0){
			solid = false;
			pain = false;
		} else if(id == 1){
			solid = true;
			pain = false;
		} else if(id == 2){
			solid = false;
			pain = true;
		}
	}
	
	public int getID(){
		return id;
	}
	
	public boolean isSolid(){
		return solid;
	}
	
	public boolean isPain(){
		return pain;
	}
}
