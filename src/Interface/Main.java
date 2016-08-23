package Interface;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {

	public static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Interface";
	
	private int select;
	private boolean unpressed = true;
	private static Main game = new Main();
	private static JFrame frame = new JFrame(game.TITLE);
	
	private boolean running = false;
	private Thread thread;
	private BufferedImage inter = null, loaded;


	public void init(){
		requestFocus();
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			inter = loader.loadImage("/res/Interface_Normal.png");
			loaded = loader.loadImage("/res/Interface_Loaded.png");
		} catch(IOException e){
			e.printStackTrace();
		}
		addKeyListener(new KeyInput(this));
	}

	private synchronized void start(){
		if(running){
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private synchronized void stop(){
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		System.exit(1);
	}

	public void run(){
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1){
				render();
				frames++;
				delta--;
			}
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("Fps " + frames);
				frames = 0;
			}

		}
		stop();

	}

	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//////////////////////////////////
		
		g.drawImage(inter, 0, 0, null);
		g.setColor(Color.WHITE);
		g.drawRect(select * 100, 40, 40, 40);
		
		//////////////////////////////////
		g.dispose();
		bs.show();
	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		
		if(unpressed){
			if(key == KeyEvent.VK_W){
				if(select == 1 || select == 2 || select == 3){
					select = 0;
					unpressed = false;
				} else if(select == 4){
					select = 2;
					unpressed = false;
				}
			} else if(key == KeyEvent.VK_A){
				if(select == 0 || select == 2 || select == 4){
					select = 1;
					unpressed = false;
				} else if(select == 3){
					select = 2;
					unpressed = false;
				}
			} else if(key == KeyEvent.VK_S){
				if(select == 1 || select == 2 || select == 3){
					select = 4;
					unpressed = false;
				} else if(select == 0){
					select = 2;
					unpressed = false;
				}
			} else if(key == KeyEvent.VK_D){
				if(select == 0 || select == 2 || select == 4){
					select = 3;
					unpressed = false;
				} else if(select == 1){
					select = 2;
					unpressed = false;
				}
			}
		}
		
		if(key == KeyEvent.VK_ENTER){
			if(select == 0){
				
			} else if(select == 1){
			
			} else if(select == 2){
				
			} else if(select == 3){
				frame.setVisible(false);
				System.exit(0);
				frame.dispose();
			} else if(select == 4){
				
			}
		}
		
	}

	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D){
			unpressed = true;
		}
		
	}

	public static void main(String args[]){
		
		frame.add(game);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.pack();
		frame.setVisible(true);
		game.start();
	}

	public BufferedImage getSpriteSheet(String path){
		if(path == "normal"){
			return inter;
		} else if(path == "loaded"){
			return loaded;
		} else {
			return null;
		}
	}

}
