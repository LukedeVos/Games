package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Item extends Rectangle{

	private static final long serialVersionUID = 1L;
	public int x, y, id, mX, mY;
	public boolean pickable = true, inMap = true, pickedUp, useable, consumeable;
	private BufferedImage item0, item1;
		
	public Item(int x, int y, int size, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		if(id == 0){
			setBounds((int)(x + Main.blockSize * 0.35), y, size / 3, size);
			useable = true;
			consumeable = false;
		} else {
			setBounds(x, y, size, size);
			useable = false;
			consumeable = true;
		}
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("items"));
		item0 = ss.grabImage(0, 0, 20, size, size);
		item1 = ss.grabImage(1, 0, 20, size, size);
	}
		
	public void render(Graphics2D g){
		if(inMap){
			if(id == 0){
				g.drawImage(item0, x, y, null);
			} else if(id == 1){
				g.drawImage(item1, x, y, null);
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
	public void setPickedUp(boolean pickedUp){
		this.pickedUp = pickedUp;
	}
	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}
	public void setIM(boolean inMap){
		this.inMap = inMap;
	}
	public void setPickable(boolean pickable){
		this.pickable = pickable;
	}
	public void setUseable(boolean useable){
		this.useable = useable;
	}
}