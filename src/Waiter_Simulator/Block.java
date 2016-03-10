package Waiter_Simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle{
	public int x,y,size;
	private Color color;
	private int id;
	private boolean solid;
	
	public Block(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		setBounds(x, y, size, size);
	}
	
	public void render(Graphics2D g) {
		g.setColor(color);
		g.fillRect(x + 1, y + 1, size - 1, size - 1);
	}
	
	public void setID(int newID) {
		id = newID;
		if(id == 1) {
			color = Color.RED;
			solid = true;
		} else if(id == 2) {
			color = Color.GREEN;
			solid = false;
		}
	}
	
	public int getID() {
		return id;
	}
	
	public boolean isSolid() {
		return solid;
	}
}
