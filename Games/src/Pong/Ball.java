package Pong;

public class Ball {
	
	private double ballX;
	private double ballY;
	
	private double ballVelX = -2.5;
	private double ballVelY = 0;
	
	public Ball(double ballX, double ballY, Main game){
		this.ballX = ballX;
		this.ballY = ballY;
	}
	
	public void tick(){
		ballX += ballVelX;
		ballY += ballVelY;
	}
	
	public double getBallX(){
		return ballX;
	}
	public double getBallY(){
		return ballY;
	}
	public double getBallVelX(){
		return ballVelX;
	}
	public double getBallVelY(){
		return ballVelY;
	}
	public void setBallX(double ballX){
		this.ballX = ballX;
	}
	public void setBallY(double ballY){
		this.ballY = ballY;
	}
	public void setBallVelX(double ballVelX){
		this.ballVelX = ballVelX;
	}
	public void setBallVelY(double ballVelY){
		this.ballVelY = ballVelY;
	}
}
