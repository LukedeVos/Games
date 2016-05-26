package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Entity extends Rectangle{

	private static final long serialVersionUID = 1L;
	public int x, y, id, mX, mY, sc;
	private BufferedImage en0, en1;
	AffineTransform at = new AffineTransform();
		
	public Entity(int x, int y, int size, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		if(id == 0){
			setBounds((int)(x + Main.blockSize * 0.35), y, size / 3, size);
		} else {
			setBounds(x, y, size, size);
		}
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("items"));
		en0 = ss.grabImage(0, 0, 20, size, size);
		en1 = ss.grabImage(1, 0, 20, size, size);
	}
		
	public void render(Graphics2D g){
			if(id == 0){
				g.drawImage(en0, x, y, null);
			} else if(id == 1){
				g.drawImage(en1, x, y, null);
			} else if(id == 100){
				
			}
	}
		
	public void useItem(Graphics2D g){
		if(Main.inventory[0].id == 0){
			if(Main.p.direction == 0){
				g.drawImage(en0, Main.p.x - 6, Main.p.y - 13, null);
			} else if(Main.p.direction == 1){
				at.setToRotation(Math.PI / 2, Main.p.x + Main.blockSize / 2, Main.p.y + Main.blockSize / 2);
				g.setTransform(at);
				g.drawImage(en0, Main.p.x - 6, Main.p.y - 3, null);
			} else if(Main.p.direction == 2){
				at.setToRotation(Math.PI, Main.p.x + Main.blockSize / 2, Main.p.y + Main.blockSize / 2);
				g.setTransform(at);
				g.drawImage(en0, Main.p.x + 6, Main.p.y - 3, null);
			} else if(Main.p.direction == 3){
				at.setToRotation(-Math.PI / 2, Main.p.x + Main.blockSize / 2, Main.p.y + Main.blockSize / 2);
				g.setTransform(at);
				g.drawImage(en0, Main.p.x + 6, Main.p.y - 13, null);
			}
			Main.p.setSpeed(0,0);
			g.dispose();
		}
	}
	
	public void tick(){
		if(sc == 20){
			Main.disableMovement = false;
			sc = 0;
			Main.use = false;
		} else if(Main.use){
			sc++;
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
	public void setEntity(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}
}