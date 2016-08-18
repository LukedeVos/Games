package Bopper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Bopper.SpriteSheet;

public class Diamond {

	public double x;
	public double y;

	private BufferedImage diamond;

	public Diamond(double diaX, double diaY, Main game) {
		this.x = diaX;
		this.y = diaY;

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		diamond = ss.grabImage(1, 2, 16, 16);
	}

	public void render(Graphics g) {
		g.drawImage(diamond, (int) x, (int) y, null);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double diaX) {
		this.x = diaX;
	}

	public void setY(double diaY) {
		this.y = diaY;
	}
}
