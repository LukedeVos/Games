package RPG;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Inventory extends Rectangle {

	private static final long serialVersionUID = 1L;
	public int x, y, id = -1, mX, mY;
	public boolean occupied, selected, usable, consumable;
	private ArrayList<BufferedImage> itemImg, GUIImg;
	public String name;
	private Font f;

	public Inventory(int x, int y, Main game){
		this.x = x;
		this.y = y;

		setBounds(x - (Main.invSize - 30), y - (Main.invSize - 30), 40, 40);
		f = new Font("Arial", Font.PLAIN, 14);
		itemImg = new ArrayList<BufferedImage>();
		GUIImg = new ArrayList<BufferedImage>();

		SpriteSheet IF = new SpriteSheet(game.getSpriteSheet("GUI"));
		SpriteSheet item = new SpriteSheet(game.getSpriteSheet("items"));

		for(int ys = 0; ys < 8; ys++){
			for(int xs = 0; xs < 8; xs++){
				itemImg.add(item.grabImage(xs, ys, 20, 20, 20));
			}
		}
		for(int ys = 0; ys < 8; ys++){
			for(int xs = 0; xs < 8; xs++){
				GUIImg.add(IF.grabImage(xs, ys, 32, 32, 32));
			}
		}
	}

	public void render(Graphics2D g){
		g.setFont(f);
		g.drawImage(GUIImg.get(0), x, y, null);
		if(occupied){
			g.drawImage(itemImg.get(id), x + 5, y + 5, null);
		}

		if(selected){
			g.drawImage(GUIImg.get(1), x, y, null);
			if(occupied){
				g.setColor(Color.BLACK);
				g.drawImage(GUIImg.get(2), (int)Main.mouseX + 10, (int)Main.mouseY + 1, g.getFontMetrics(f).stringWidth(name) + 10, 20, null);
				g.drawString(name, (int)Main.mouseX + 15, (int)Main.mouseY + 15);
			}
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

	public void setusable(boolean usable){
		this.usable = usable;
	}

	public void setConsumeabel(boolean consumable){
		this.consumable = consumable;
	}
	public void setName(String name){
		this.name = name;
	}
}
