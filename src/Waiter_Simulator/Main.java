package Waiter_Simulator;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
	private static final int HEIGHT = WIDTH / 12 * 9;
	private static final int SCALE = 2;
	public final String TITLE = "Waiter Simulator";
	
	private boolean running = false;
	private Thread thread;
	
	private boolean paused = false;
	
	public void init(){
//		requestFocus();
//		try{
//			spriteSheet = loader.loadImage("/res/Sprite_Sheet_Bopper.png");
//		}catch(IOException e){
//			e.printStackTrace();
//		}
		addKeyListener(new KeyInput(this));
	}
	
	public void start(){
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public void stop(){
		if(!running){
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(1);
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		//Game loop starts here
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1){
				if(!paused){
					tick();
				}
				updates++;
				delta--; 
			}
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println(updates + " ticks, Fps " + frames);
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}
	
	public void tick(){
		
	}
	
	public void render(){
		
	}
	
	public void keyPressed(KeyEvent k){
		
	}
	
	public void keyReleased(KeyEvent k){
		
	}
	
	public static void main(String args[]){
		Main game = new Main();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		
		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
}
