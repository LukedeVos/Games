package Pong;

public class Player {

	private double x;
	private double y;

	private double velY = 0;

	public Player(double x, double y, Main game) {
		this.x = x;
		this.y = y;
	}

	public void tick() {
		y += velY;

		if(y <= 0) {
			y = 0;
		} else if(y >= 390) {
			y = 390;
		}

	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getVelY() {
		return velY;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}

}
