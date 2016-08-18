package Bopper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class EnemyEater {
	public double x;
	public double y;

	public double velX;
	public double velY;

	private BufferedImage eater;

	public EnemyEater(double x, double y, Main game) {
		this.x = x;
		this.y = y;

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		eater = ss.grabImage(4, 1, 16, 16);
	}

	public void tick() {
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {
		g.drawImage(eater, (int) x, (int) y, null);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getVelX() {
		return velX;
	}

	public double getVelY() {
		return velY;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}
}
