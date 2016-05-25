package RPG;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Inventory extends Rectangle{

	private static final long serialVersionUID = 1L;
	public int x,y,id = -1, mX, mY, sc;
	public boolean occupied, selected, use, rotate;
	private BufferedImage GUI, item0, item1;
	AffineTransform at = new AffineTransform();
	
	public Inventory(int x, int y, Main game){
		this.x = x;
		this.y = y;
		setBounds(x, y, 30, 30);
		
		SpriteSheet IF = new SpriteSheet(game.getSpriteSheet("GUI"));
		SpriteSheet item = new SpriteSheet(game.getSpriteSheet("items"));
		GUI = IF.grabImage(0, 0, 30, 30, 30);
		item0 = item.grabImage(0, 0, 20, 20, 20);
		item1 = item.grabImage(1, 0, 20, 20, 20);
		
	}
	
	public void render(Graphics2D g){
		g.drawImage(GUI, x, y, null);
		if(id == 0){
			g.drawImage(item0, x + 5, y + 5, null);
		} else if(id == 1){
			g.drawImage(item1, x + 5, y + 5, null);
		}
		
		if(Main.key == KeyEvent.VK_SPACE && Main.rendered){
			if(Main.inventory[0].id == 0){
				if(Main.p.direction == 0){
					g.drawImage(item0, Main.p.x + 1, Main.p.y - 15, null);
					use = true;
				} else if(Main.p.direction == 1){
					at.setToRotation(Math.PI / 2, Main.p.x + Main.blockSize / 2, Main.p.y + Main.blockSize / 2);
					g.setTransform(at);
					g.drawImage(item0, Main.p.x , Main.p.y - 5, null);
					use = true;
				} else if(Main.p.direction == 2){
					at.setToRotation(Math.PI, Main.p.x + Main.blockSize / 2, Main.p.y + Main.blockSize / 2);
					g.setTransform(at);
					g.drawImage(item0, Main.p.x - 1, Main.p.y + 15, null);
					use = true;
				} else if(Main.p.direction == 3){
					at.setToRotation(Math.PI * 1.5, Main.p.x + Main.blockSize / 2, Main.p.y + Main.blockSize / 2);
					g.setTransform(at);
					g.drawImage(item0, Main.p.x , Main.p.y + 5, null);
					use = true;
				}
				Main.p.setSpeed(0,0);
			}
		}
		
		if(selected){
			
		}
	}
	
	public void tick(){
		if(sc == 20){
			Main.key = KeyEvent.VK_0;
			sc = 0;
			use = false;
		} else if(use){
			sc++;
		}
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
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
