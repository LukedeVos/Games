package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Block extends Rectangle {
	private static final long serialVersionUID = 1L;
	public int x, y, xSize, ySize;
	private int id;
	private boolean solid, pain;
	private ArrayList<BufferedImage> img;


	public Block(int x, int y, int xSize, int ySize, Main game){
		this.x = x;
		this.y = y;
		this.xSize = xSize;
		this.ySize = ySize;
		setBounds(x, y, xSize, ySize);
		img = new ArrayList<BufferedImage>();
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("tiles"));
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				img.add(ss.grabImage(j, i, 20, 20, 20));
			}
		}
	}

	public void render(Graphics2D g){
		g.drawImage(img.get(id), x, y, (int)(20 * Main.xMod), (int)(20 * Main.yMod), null);
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
