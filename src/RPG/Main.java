package RPG;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;

import javax.swing.JFrame;

import RPG.BufferedImageLoader;
import RPG.KeyInput;

public class Main extends Canvas implements Runnable, MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
	private static final int HEIGHT = WIDTH / 12 * 9 + 20;
	private static final int SCALE = 2;
	public final String TITLE = "UNKNOWN RPG";
	
	public static Player p;
	
	private boolean running = false;
	private Thread thread;
	Random rand = new Random();
	
	private int pVel = 2;
	private int frames;
	private int pSize = 8;
	private int levelX = 1, levelY = 1;
	private int painTimer;
	private int tempPID, tempPX, tempPY;
	
	public boolean pain, spiked, paused, beenThere, dragged, inInventory;
	
	private double tempX, tempY, mouseX, mouseY, mouseDX, mouseDY;
	
	private static int blockSize = 20;
	private static int invSize = 40;
	
	private static int row = WIDTH * SCALE / blockSize;
	private static int col = (HEIGHT - 20) * SCALE / blockSize;
	
	private String line = null;
	private String fileName = "RPG_map";
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage tiles = null, items = null, GUI = null, enemies = null;
	BufferedImageLoader loader = new BufferedImageLoader();
	
	private ArrayList<Item> item;
	private ArrayList<Enemy> enemy;
	public static Inventory[] inventory = new Inventory[16];
	public static Block[][] map = new Block[row][col];
	public static int key;
	
	public void init(){
		requestFocus();
		try{
			tiles = loader.loadImage("/res/Sprite_Sheet_RPG_Tiles.png");
			items = loader.loadImage("/res/Sprite_Sheet_RPG_Items.png");
			GUI = loader.loadImage("/res/Sprite_Sheet_RPG_GUI.png");
			enemies = loader.loadImage("/res/Sprite_Sheet_RPG_Enemies.png");
		}catch(IOException e){
			e.printStackTrace();
		}
		addKeyListener(new KeyInput(this));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		p = new Player(26, 26,  pSize, this, null);
		item = new ArrayList<Item>();
		enemy = new ArrayList<Enemy>();

		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				map[x][y] = new Block(x * blockSize,  y * blockSize, blockSize, this);
				
			}
		}
		
		int y = 0;
		int j = 1;
		
		for(int i = 0; i < inventory.length; i++){
			while(j > 4){
				y++;
				j -= 4;
			}
			inventory[i] = new Inventory(200 + j * invSize, 50 + y * invSize, this);
			j++;
		}
		
		for(int i = 0; i < 2; i++){
			inventory[i].setPosition(getWidth() - (1 - i) * invSize - 40, getHeight() - invSize);
		}
		
		loadMap(levelX, levelY);
		item.add(new Item(-20, -20, blockSize,100, this));
		
	}
	
	
	
	public void loadMap(int mapX, int mapY){
		try {
            FileReader fileReader = new FileReader(fileName + mapX + "," + mapY + ".txt");
            int y = 0;
            
            for(int i = 0; i < item.size(); i++){
    			if(item.get(i).mX == mapX && item.get(i).mY == mapY){
    				beenThere = true;
    			}
    		}
        	for(int i = 0; i < inventory.length; i++){
        		if(inventory[i].mX == mapX && inventory[i].mY == mapY){
        			beenThere = true;
        		}
        	}
            
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while((line = bufferedReader.readLine()) != null) {
            	String string = line;
            	String[] parts = string.split("\t");
            	
            	for(int x = 0; x < row; x++){
            		String temp = parts[x];
            		if(temp.contains(";") && temp.contains(":") && !beenThere){
            			String data = temp;
            			String[] meta = data.split(";");
            			String[] last = meta[1].split(":");
            			int itemID = Integer.parseInt(last[0]);
            			item.add(new Item(x * blockSize, y * blockSize, blockSize, itemID, this));
            			item.get(item.size() - 1).setMap(mapX, mapY);
            			int enemyID = Integer.parseInt(last[1]);
            			enemy.add(new Enemy(x * blockSize, y * blockSize, enemyID, this));
            			enemy.get(enemy.size() - 1).setMap(mapX, mapY);
            			int newID = Integer.parseInt(meta[0]);
            			map[x][y].setID(newID);
            		} else if(temp.contains(";") && !beenThere){ 
            			String data = temp;
            			String[] meta = data.split(";");
            			int itemID = Integer.parseInt(meta[1]);
            			item.add(new Item(x * blockSize, y * blockSize, blockSize, itemID, this));
            			item.get(item.size() - 1).setMap(mapX, mapY);
            			int newID = Integer.parseInt(meta[0]);
            			map[x][y].setID(newID);
            		} else if(temp.contains(":") && !beenThere){
            			String data = temp;
            			String[] meta = data.split(":");
            			int enemyID = Integer.parseInt(meta[1]);
            			enemy.add(new Enemy(x * blockSize, y * blockSize, enemyID, this));
            			enemy.get(enemy.size() - 1).setMap(mapX, mapY);
            			int newID = Integer.parseInt(meta[0]);
            			map[x][y].setID(newID);
            		} else if(temp.contains(":") && beenThere){
            			String data = temp;
            			String[] meta = data.split(":");
            			int newID = Integer.parseInt(meta[0]);
            			map[x][y].setID(newID);
            		} else if(temp.contains(";") && beenThere){
            			String data = temp;
            			String[] meta = data.split(";");
            			int newID = Integer.parseInt(meta[0]);
            			map[x][y].setID(newID);
            		} else {
            			int newID = Integer.parseInt(temp);
            			map[x][y].setID(newID);
            		}
            		
            	}
            	y++;
            }   
            beenThere = false;
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
		for(int i = 0; i < inventory.length; i++){
			inventory[i].tick();
		}
		
		//Solid collision
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				if(map[x][y].intersects(p) && map[x][y].isSolid()) {
					p.setPlayer((int)tempX, (int)tempY);
				}
			}
		}
		tempX = p.x;
		tempY = p.y;
		
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
						p.setHealth(p.health - 1);
						pain = false;
						System.out.println(p.health);
					}
					spiked = true;
				}
			}
		}
		
		//Item collision
		for(int i = 0; i < item.size(); i++){
			if(item.get(i).intersects(p) && item.get(i).pickable == true){
				for(int j = 0; j < inventory.length; j++){
					if(!inventory[j].occupied){
						inventory[j].setOccupied(true);
						inventory[j].setID(item.get(i).id);
						inventory[j].setMap(levelX, levelY);
						item.remove(i);
						break;
					}
				}
			}
		}
		
		//Inventory management
		for(int i = 0; i < inventory.length; i++){
			if(inventory[i].contains(mouseX, mouseY) && inInventory){
				inventory[i].setSelected(true);
				if(inventory[i].contains(mouseDX, mouseDY)){
					if(item.get(item.size() - 1).pickedUp && inventory[i].occupied && !dragged){
						tempPID = inventory[i].id;
						tempPX = inventory[i].mX;
						tempPY = inventory[i].mY;
						inventory[i].setID(item.get(item.size() - 1).id);
						inventory[i].setMap(item.get(item.size() - 1).mX, item.get(item.size() - 1).mY);
						item.get(item.size() - 1).setMap(tempPX, tempPY);
						item.get(item.size() - 1).setID(tempPID);
						item.get(item.size() - 1).setPickable(false);
						item.get(item.size() - 1).setPickedUp(true);
						mouseDX = 0;
						mouseDY = 0;
						dragged = true;
						System.out.println("swapped " + i);
					} else if(item.get(item.size() - 1).pickedUp && !inventory[i].occupied && !dragged){
						inventory[i].setOccupied(true);
						inventory[i].setID(item.get(item.size() - 1).id);
						inventory[i].setMap(item.get(item.size() - 1).mX, item.get(item.size() - 1).mY);
						item.remove(item.size() - 1);
						dragged = true;
						mouseDX = 0;
						mouseDY = 0;
						System.out.println("placed " + i);
					} else if(inventory[i].occupied && !dragged){
						item.add(new Item(-1 * blockSize, -1 * blockSize, blockSize, inventory[i].id, this));
						item.get(item.size() - 1).setMap(inventory[i].mX, inventory[i].mY);
						item.get(item.size() - 1).setPickable(false);
						item.get(item.size() - 1).setPickedUp(true);
						inventory[i].setOccupied(false);
						inventory[i].setID(-1);
						inventory[i].setMap(-1, -1);
						mouseDX = 0;
						mouseDY = 0;
						dragged = true;
						System.out.println("picked " + i);
					}
				}
			} else if(inventory[i].selected){
				inventory[i].setSelected(false);
			}
		}
		dragged = false;
		if(item.get(item.size() - 1).pickedUp){
			item.get(item.size() - 1).setItem((int)mouseX, (int)mouseY);
		}
		
		//Next map
		if(p.x >= getWidth() - 4){
			levelX += 1;
			loadMap(levelX, levelY);
			p.setX(5);
			for(int i = 0; i < item.size(); i++){
				if(!(levelX == item.get(i).mX) || !(levelY == item.get(i).mY)){
					item.get(i).setIM(false);
					item.get(i).setPickable(false);
				} else {
					item.get(i).setIM(true);
					item.get(i).setPickable(true);
				}
			}
		}
		
		if(p.x <= 4){
			levelX -= 1;
			loadMap(levelX, levelY);
			p.setX(getWidth() - 5);
			for(int i = 0; i < item.size(); i++){
				if(!(levelX == item.get(i).mX) || !(levelY == item.get(i).mY)){
					item.get(i).setIM(false);
					item.get(i).setPickable(false);
				} else {
					item.get(i).setIM(true);
					item.get(i).setPickable(true);
				}
			}
		}
		
		if(p.y >= getHeight() - 50){
			levelY += 1;
			loadMap(levelX, levelY);
			p.setY(5);
			for(int i = 0; i < item.size(); i++){
				if(!(levelX == item.get(i).mX) || !(levelY == item.get(i).mY)){
					item.get(i).setIM(false);
					item.get(i).setPickable(false);
				} else {
					item.get(i).setIM(true);
					item.get(i).setPickable(true);
				}
			}
		}
		
		if(p.y <= 4){
			levelY -= 1;
			loadMap(levelX, levelY);
			p.setY(getHeight() - 51);
			for(int i = 0; i < item.size(); i++){
				if(!(levelX == item.get(i).mX) || !(levelY == item.get(i).mY)){
					item.get(i).setIM(false);
					item.get(i).setPickable(false);
				} else {
					item.get(i).setIM(true);
					item.get(i).setPickable(true);
				}
			}
		}
		
		if(p.dead){
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
		
		for(int i = 0; i < item.size(); i++){
			item.get(i).render((Graphics2D) g);
//			System.out.println(i + ". Id: " + item.get(i).id);
		}
		
		for(int i = 0; i < enemy.size(); i++){
			enemy.get(i).render((Graphics2D) g);
		}
		
		p.render((Graphics2D) g);
		
		if(inInventory){
			for(int i = 0; i < inventory.length; i++){
				inventory[i].render((Graphics2D) g);
			}
		} else {
			for(int i = 0; i < 2; i++){
				inventory[i].render((Graphics2D) g);
			}
		}
		
		if(item.get(item.size() - 1).pickedUp){
			item.get(item.size() - 1).render((Graphics2D) g);
		}
		//////////////////////////////////
		g.dispose();
		bs.show();
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseClicked(MouseEvent e){
		mouseDX = e.getX();
		mouseDY = e.getY();
	}
	
	public void keyPressed(KeyEvent k){
		key = k.getKeyCode();
		
		if(!inInventory){
			if(key == KeyEvent.VK_W){
				p.setVelY(-pVel);
				p.setVelX(0);
				p.setDirection(0);
			} else if(key == KeyEvent.VK_D){
				p.setVelX(pVel);
				p.setVelY(0);
				p.setDirection(1);
			} else if(key == KeyEvent.VK_S){
				p.setVelY(pVel);
				p.setVelX(0);
				p.setDirection(2);
			} else if(key == KeyEvent.VK_A){
				p.setVelX(-pVel);
				p.setVelY(0);
				p.setDirection(3);
			}
		}
		
		if(key == KeyEvent.VK_E){
			System.out.println(p.direction);
			inInventory = !inInventory;
			if(inInventory){
				for(int i = 0; i < 2; i++){
					inventory[i].setPosition(200 + (i + 1) * invSize, 50);
				}
			} else {
				for(int i = 0; i < 2; i++){
					inventory[i].setPosition(getWidth() - (1 - i) * invSize - 40, getHeight() - invSize);
				}
			}
			p.setVelX(0);
			p.setVelY(0);
		}
	}
	
	public void keyReleased(KeyEvent k){
		int key = k.getKeyCode();
		
		if(key == KeyEvent.VK_W){
			p.setVelY(0);
		} else if(key == KeyEvent.VK_S){
			p.setVelY(0);
		} else if(key == KeyEvent.VK_A){
			p.setVelX(0);
		} else if(key == KeyEvent.VK_D){
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
	
	public BufferedImage getSpriteSheet(String path){
		if(path == "tiles"){
			return tiles;
		} else if(path == "items"){
			return items;
		} else if(path == "GUI"){
			return GUI;
		} else if(path == "Enemies"){
			return enemies;
		}
		return null;
	}
}
