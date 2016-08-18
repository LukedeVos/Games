package Bopper;

import Bopper.SpriteSheet;
import Bopper.Main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Player {

	public double x;
	public double y;

	public double velX = 0;
	public double velY = 0;

	private BufferedImage player;

	public Player(double x, double y, Main game) {
		this.x = x;
		this.y = y;
	}

	public void tick(Main game) {
		x += velX;
		y += velY;

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		player = ss.grabImage(1, 1, 16, 16);
	}

	public void render(Graphics g) {
		g.drawImage(player, (int) x, (int) y, null);
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
