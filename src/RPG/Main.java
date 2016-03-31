package RPG;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

import RPG.BufferedImageLoader;
import RPG.KeyInput;

public class Main extends Canvas implements Runnable{

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
	private static final int HEIGHT = WIDTH / 12 * 9;
	private static final int SCALE = 2;
	public final String TITLE = "UNKNOWN RPG";
	
	private Player p;
	
	private boolean running = false;
	private Thread thread;
	Random rand = new Random();
	
	private int pVel = 2;
	private int frames;
	private int pSize = 8;
	private int levelX = 1, levelY = 1;
	private int painTimer;
	
	private boolean pain;
	private boolean spiked;
	private boolean paused;
	
	private double tempX, tempY;
	
	private static int blockSize = 20;
	
	private static int row = WIDTH * SCALE / blockSize;
	private static int col = HEIGHT * SCALE / blockSize;
	
	private String line = null;
	private String fileName = "RPG_map";
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	BufferedImageLoader loader = new BufferedImageLoader();
	
	public static Block[][] map = new Block[row][col];
	
	public void init(){
		requestFocus();
		try{
			spriteSheet = loader.loadImage("/res/Sprite_Sheet_RPG.png");
		}catch(IOException e){
			e.printStackTrace();
		}
		addKeyListener(new KeyInput(this));
		
		p = new Player(26, 26,  pSize, this);
		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				map[x][y] = new Block(x * blockSize,  y * blockSize, blockSize, this);
			}
		}
		loadMap(levelX, levelY);
	}
	
	public void loadMap(int mapX, int mapY){
		try {
            FileReader fileReader = new FileReader(fileName + mapX + "," + mapY + ".txt");
            int y = 0;
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
            	String string = line;
            	String[] parts = string.split(" ");
            	for(int x = 0; x < row; x++){
            		String temp = parts[x];
            		int newID = Integer.parseInt(temp);
            		map[x][y].setID(newID);
            	}
            	y++;
            }   
            bufferedReader.close();    
            System.out.println("Loaded: '" + fileName + mapX + "," + mapY + ".txt'");
        } catch(FileNotFoundException e) {
            e.printStackTrace();                
        } catch(IOException e) {
            e.printStackTrace();                 
        }
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
		long timer = System.currentTimeMillis();
		
		//Game loop starts here
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1){
				if(!paused){
					tick();
					render();
				}
				updates++;
				delta--; 
			}
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
		p.tick();
		
		//Solid collision
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				if(map[x][y].intersects(p) && map[x][y].isSolid()) {
					p.setX((int)tempX);
					p.setY((int)tempY);
				}
			}
		}
		tempX = p.getX();
		tempY = p.getY();
		
		//Spike collision
		if(spiked){
			if(!pain){
				painTimer += 1;
			}
			if(painTimer == 12){
				pain = true;
				painTimer = 0;
			}
		}
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				if(map[x][y].intersects(p) && map[x][y].isPain()) {
					if(pain){
						p.setHealth(p.getHealth() - 1);
						pain = false;
						System.out.println(p.getHealth());
					}
					spiked = true;
				}
			}
		}
		
		//Next map
		if(p.getX() >= getWidth() - 4){
			levelX += 1;
			loadMap(levelX, levelY);
			p.setX(5);
		}
		if(p.getX() <= 4){
			levelX -= 1;
			loadMap(levelX, levelY);
			p.setX(getWidth() - 5);
		}
		
		if(p.getY() >= getHeight() - 4){
			levelY += 1;
			loadMap(levelX, levelY);
			p.setY(5);
		}
		if(p.getY() <= 4){
			levelY -= 1;
			loadMap(levelX, levelY);
			p.setY(getHeight() - 5);
		}
		
		if(p.getDead()){
			stop();
		}
		
	}
	
	public void render(){
		frames++;

		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//////////////////////////////////
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		
		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				map[x][y].render((Graphics2D) g);
			}
		}
		
		p.render((Graphics2D) g);
		//////////////////////////////////
		g.dispose();
		bs.show();
	}
	
	public void keyPressed(KeyEvent k){
		int key = k.getKeyCode();
		
		if(key == KeyEvent.VK_UP){
			p.setVelY(-pVel);
		} else if(key == KeyEvent.VK_DOWN){
			p.setVelY(pVel);
		} else if(key == KeyEvent.VK_LEFT){
			p.setVelX(-pVel);
		} else if(key == KeyEvent.VK_RIGHT){
			p.setVelX(pVel);
		}
	}
	
	public void keyReleased(KeyEvent k){
		int key = k.getKeyCode();
		
		if(key == KeyEvent.VK_UP){
			p.setVelY(0);
		} else if(key == KeyEvent.VK_DOWN){
			p.setVelY(0);
		} else if(key == KeyEvent.VK_LEFT){
			p.setVelX(0);
		} else if(key == KeyEvent.VK_RIGHT){
			p.setVelX(0);
		}
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
	
	public BufferedImage getSpriteSheet(){
		return spriteSheet;
	}
}
