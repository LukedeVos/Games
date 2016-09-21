package RPG;

import java.awt.Canvas;
import java.awt.Color;
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

public class Main extends Canvas implements Runnable, MouseListener,
		MouseMotionListener {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 960, HEIGHT = WIDTH / 12 * 9 + 20, SCALE = 2;
	public final String TITLE = "UNKNOWN RPG";

	public static Player p;

	private boolean running = false;
	private Thread thread;
	Random rand = new Random();

	// TODO Get into Player class
	public String hS;
	public int invisC;

	private int frames, pSize = 8, painTimer, tempPID, tempPX, tempPY, entityCounter, eInv;
	public static int levelX = 1, levelY = 1, used;

	public boolean pain, spiked, paused, beenThere, dragged, inInventory;
	public static boolean disableMovement, use, remove, showBounds;

	private double tempX, tempY, mouseDX, mouseDY, lvl;
	public static double mouseX, mouseY,pVel = 2;
	
	static int xB = 64, yB = 46, blockWidth = 20, blockHeight = 20, invSize = 175;
	public static double xMod, yMod, inX, inY;
	
	private String line = null, fileName = "RPG_map", tempN;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage tiles = null, items = null, GUI = null, enemies = null;
	BufferedImageLoader loader = new BufferedImageLoader();

	private ArrayList<Item> item;
	private ArrayList<Enemy> enemy;
	private ArrayList<Entity> entity;
	private ArrayList<Double> maps;
	public static Inventory[] inventory = new Inventory[16];
	public static Block[][] map = new Block[xB][yB];
	public static int key;

	public void init(){
		requestFocus();
		try {
			tiles = loader.loadImage("/res/Sprite_Sheet_RPG_Tiles.png");
			items = loader.loadImage("/res/Sprite_Sheet_RPG_Items.png");
			GUI = loader.loadImage("/res/Sprite_Sheet_RPG_GUI.png");
			enemies = loader.loadImage("/res/Sprite_Sheet_RPG_Enemies.png");
		} catch(IOException e){
			e.printStackTrace();
		}
		addKeyListener(new KeyInput(this));

		addMouseListener(this);
		addMouseMotionListener(this);
		
		inX = getWidth() / xB;
		inY = (getHeight() - 60) / yB;
		xMod = inX / blockWidth;
		yMod = inY / blockHeight;
		
		System.out.println(inX + " " + inY + " " + xMod + " " + yMod);
		System.out.println(getWidth());
		
		p = new Player((int)(26 * xMod), (int)(26 * yMod), (int)(pSize * yMod), this, null);
		item = new ArrayList<Item>();
		enemy = new ArrayList<Enemy>();
		entity = new ArrayList<Entity>();
		maps = new ArrayList<Double>();
		
		for(int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				map[x][y] = new Block((int)(x * inX), (int)(y * inY), (int)(blockWidth * xMod), (int)(blockHeight * yMod), this);
			}
		}

		int y = 0;
		int j = 1;

		for(int i = 0; i < inventory.length; i++){
			while(j > 4){
				y++;
				j -= 4;
			}
			inventory[i] = new Inventory((int)((200 + j * invSize) * xMod), (int)((50 + y * invSize) * yMod), this);
			j++;
		}

		for(int i = 0; i < 2; i++){
			inventory[i].setPosition(getWidth() - (1 - i) * invSize - 40, getHeight() - 40);
		}

		System.out.println("xBs: " + xB + " yBs: " + yB);
		loadMap(levelX, levelY);
		lvl = levelX + levelY * 0.1;
		maps.add(lvl);
		item.add(new Item(-20, -20, blockWidth, 100, this));
		pVel *= yMod;
	}

	public void loadMap(int mapX, int mapY){
		try {
			FileReader fileReader = new FileReader(fileName + mapX + "," + mapY + ".txt");
			int y = 0;

			if(!maps.isEmpty()){
				for(int i = 0; i < maps.size(); i++){
					double temp = maps.get(i);
					int tempX = (int) temp;
					int tempY = (int) (temp * 10 - tempX * 10);
					if(mapX == tempX && mapY == tempY){
						beenThere = true;
					}
				}
			}

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null){
				String string = line;
				String[] parts = string.split("\t");

				for(int x = 0; x < xB; x++){
					String temp = parts[x];
					if(temp.contains(";") && temp.contains(":") && !beenThere){
						String data = temp;
						String[] meta = data.split(";");
						String[] last = meta[1].split(":");
						int itemID = Integer.parseInt(last[0]);
						item.add(new Item((int)(x * blockWidth * xMod), (int)(y * blockWidth * yMod), (int)(blockWidth * yMod), itemID, this));
						item.get(item.size() - 1).setMap(mapX, mapY);
						int enemyID = Integer.parseInt(last[1]);
						enemy.add(new Enemy((int)(x * blockWidth * xMod), (int)(y * blockWidth * yMod), enemyID, this));
						enemy.get(enemy.size() - 1).setMap(mapX, mapY);
						int newID = Integer.parseInt(meta[0]);
						map[x][y].setID(newID);
					} else if(temp.contains(";") && !beenThere){
						String data = temp;
						String[] meta = data.split(";");
						int itemID = Integer.parseInt(meta[1]);
						item.add(new Item((int)(x * blockWidth * xMod), (int)(y * blockWidth * yMod), (int)(blockWidth * yMod), itemID, this));
						item.get(item.size() - 1).setMap(mapX, mapY);
						int newID = Integer.parseInt(meta[0]);
						map[x][y].setID(newID);
					} else if(temp.contains(":") && !beenThere){
						String data = temp;
						String[] meta = data.split(":");
						int enemyID = Integer.parseInt(meta[1]);
						enemy.add(new Enemy((int)(x * blockWidth * xMod), (int)(y * blockWidth * yMod), enemyID, this));
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
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
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
		int updates = 0;
		long timer = System.currentTimeMillis();

		// Game loop starts here
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

		for(int i = 0; i < enemy.size(); i++){
			enemy.get(i).tick();
		}

		for(int i = 0; i < entity.size(); i++){
			entity.get(i).tick();
		}
		
		// Solid collision
		for(int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				if(map[x][y].intersects(p) && map[x][y].isSolid()){
					p.setPlayer((int) tempX, (int) tempY);
				}
			}
		}
		tempX = p.x;
		tempY = p.y;
		
		// Enemy solid collision
		for(int i = 0; i < enemy.size(); i++){
			for(int x = 0; x < map.length; x++){
				for(int y = 0; y < map[0].length; y++){
					if(map[x][y].intersects(enemy.get(i)) && map[x][y].isSolid()){
						enemy.get(i).setEnemy((int) enemy.get(i).tempX,
								(int) enemy.get(i).tempY);
						enemy.get(i).collide = true;
					}
				}
			}
			enemy.get(i).tempX = (int) enemy.get(i).x;
			enemy.get(i).tempY = (int) enemy.get(i).y;
		}
		
		// Spike collision
		if(spiked){
			if(!pain){
				painTimer += 1;
			}
			if(painTimer == 12){
				pain = true;
				painTimer = 0;
			}
		}
		for(int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				if(map[x][y].intersects(p) && map[x][y].isPain()){
					if(pain){
						p.setHealth(p.health - 1);
						pain = false;
					}
					spiked = true;
				}
			}
		}

		// Item collision
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

		// Enemy collision
		for(int i = 0; i < enemy.size(); i++){
			if(enemy.get(i).intersects(p) && !p.invincible
					&& enemy.get(i).inMap){
				p.setHealth(p.health - enemy.get(i).damage);
				p.setInvincible(true);
				eInv = (int) (enemy.get(i).time * 0.01 * 60);

			}
		}
		if(p.invincible){
			invisC++;
		}
		if(invisC >= eInv){
			invisC = 0;
			p.setInvincible(false);
		}

		// Inventory management
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
						item.get(item.size() - 1).setName(tempN);
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
						item.add(new Item(-1 * blockWidth, -1 * blockWidth, blockWidth, inventory[i].id, this));
						item.get(item.size() - 1).setMap(inventory[i].mX, inventory[i].mY);
						item.get(item.size() - 1).setPickable(false);
						item.get(item.size() - 1).setPickedUp(true);
						clearInventory(i);
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
			item.get(item.size() - 1).setItem((int) mouseX, (int) mouseY);
		}
		
		// Enemy Entity collision
		for(int i = 0; i < entity.size(); i++){
			for(int j = 0; j < enemy.size(); j++){
				if(enemy.get(j).getBounds().intersects(entity.get(i).getBounds()) && enemy.get(j).inMap && (entity.get(i).type == 0 || entity.get(i).type == 4 || entity.get(i).type == 2)){
					enemy.get(j).setHealth(enemy.get(j).health - entity.get(i).effect);
					if(enemy.get(j).health <= 0){
						enemy.remove(j);
					}
				}
			}
		}
		
		if(remove){
			entity.remove(entity.size() - 1 - entityCounter);
			remove = false;
			entityCounter = 0;
		}

		// Next map
		if(p.x >= getWidth() - 4){
			levelX += 1;
			loadMap(levelX, levelY);
			lvl = levelX + levelY * 0.1;
			boolean add = true;
			if(!maps.isEmpty()){
				for(int i = 0; i < maps.size(); i++){
					if(maps.get(i) == lvl){
						add = false;
					}
				}
			}
			if(add){
				maps.add(lvl);
			}
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
			lvl = levelX + levelY * 0.1;
			boolean add = true;
			if(!maps.isEmpty()){
				for(int i = 0; i < maps.size(); i++){
					if(maps.get(i) == lvl){
						add = false;
					}
				}
			}
			if(add){
				maps.add(lvl);
			}
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
			lvl = levelX + levelY * 0.1;
			boolean add = true;
			if(!maps.isEmpty()){
				for(int i = 0; i < maps.size(); i++){
					if(maps.get(i) == lvl){
						add = false;
					}
				}
			}
			if(add){
				maps.add(lvl);
			}
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
			lvl = levelX + levelY * 0.1;
			boolean add = true;
			if(!maps.isEmpty()){
				for(int i = 0; i < maps.size(); i++){
					if(maps.get(i) == lvl){
						add = false;
					}
				}
			}
			if(add){
				maps.add(lvl);
			}
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
			//TODO Add death feature
			running = false;
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

		for(int x = 0; x < map.length; x++){
			for(int y = 0; y < map[0].length; y++){
				map[x][y].render((Graphics2D) g);
			}
		}

		for(int i = 0; i < item.size(); i++){
			item.get(i).render((Graphics2D) g);
		}

		for(int i = 0; i < enemy.size(); i++){
			enemy.get(i).render((Graphics2D) g);
		}

		p.render((Graphics2D) g);

		// TODO Find a way to put this in the Player class
		g.setColor(Color.RED);
		hS = Integer.toString(p.health);
		g.drawString(hS, 40, (int) getHeight() - 20);
		//

		if(inInventory){
			for(int i = 0; i < inventory.length; i++){
				if(!inventory[i].selected){
					inventory[i].render((Graphics2D) g);
				}
			}
			for(int i = 0; i < inventory.length; i++){
				if(inventory[i].selected){
					inventory[i].render((Graphics2D) g);
				}
			}
		} else {
			for(int i = 0; i < 2; i++){
				inventory[i].render((Graphics2D) g);
			}
		}

		for(int i = 0; i < entity.size(); i++){
			entity.get(i).useItem((Graphics2D) g);
		}

		if(item.get(item.size() - 1).pickedUp){
			item.get(item.size() - 1).render((Graphics2D) g);
		}
		//////////////////////////////////
		g.dispose();
		bs.show();
	}

	public static void clearInventory(int i){
		inventory[i].setOccupied(false);
		inventory[i].setID(-1);
		inventory[i].setMap(-1, -1);
	}

	public void mousePressed(MouseEvent e){

	}

	public void mouseReleased(MouseEvent e){

	}

	public void mouseEntered(MouseEvent e){

	}

	public void mouseExited(MouseEvent e){

	}

	public void mouseMoved(MouseEvent e){
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseDragged(MouseEvent e){

	}

	public void mouseClicked(MouseEvent e){
		mouseDX = e.getX();
		mouseDY = e.getY();
	}

	public void keyPressed(KeyEvent k){
		key = k.getKeyCode();

		if(!inInventory && !disableMovement){
			if(key == KeyEvent.VK_W){
				p.setVelY((int)-(pVel));
				p.setVelX(0);
				p.setDirection(0);
			} else if(key == KeyEvent.VK_D){
				p.setVelX((int)(pVel));
				p.setVelY(0);
				p.setDirection(1);
			} else if(key == KeyEvent.VK_S){
				p.setVelY((int)(pVel));
				p.setVelX(0);
				p.setDirection(2);
			} else if(key == KeyEvent.VK_A){
				p.setVelX((int)-(pVel));
				p.setVelY(0);
				p.setDirection(3);
			}
		}

		if(key == KeyEvent.VK_E){
			inInventory = !inInventory;
			if(inInventory){
				for(int i = 0; i < 2; i++){
					inventory[i].setPosition((int)((200 + (i + 1) * invSize) * xMod), (int)((50 * yMod)));
				}
			} else {
				for(int i = 0; i < 2; i++){
					inventory[i].setPosition(getWidth() - (1 - i) * invSize - 40, getHeight() - 40);
				}
			}
			p.setVelX(0);
			p.setVelY(0);
		}

		if(key == KeyEvent.VK_SPACE && !use){
			if(inventory[0].usable){
				disableMovement = true;
				use = true;
				used = 0;
				entity.add(new Entity(p.x, p.y, blockWidth, inventory[0].id,
						this));
				p.setVelX(0);
				p.setVelY(0);
			} else if(inventory[0].consumable){
				disableMovement = true;
				use = true;
				used = 0;
				entity.add(new Entity(p.x, p.y, blockWidth, inventory[0].id,
						this));
				p.setVelX(0);
				p.setVelY(0);
			}
		}

		if(key == KeyEvent.VK_SHIFT && !use){
			System.out.println(inventory[0].id + " " + inventory[1].id);
			if(inventory[1].usable){
				disableMovement = true;
				use = true;
				used = 1;
				entity.add(new Entity(p.x, p.y, blockWidth, inventory[1].id,
						this));
				p.setVelX(0);
				p.setVelY(0);
			} else if(inventory[1].consumable){
				disableMovement = true;
				use = true;
				used = 1;
				entity.add(new Entity(p.x, p.y, blockWidth, inventory[1].id,
						this));
				p.setVelX(0);
				p.setVelY(0);
			}
		}

		if(key == KeyEvent.VK_B){
			showBounds = !showBounds;
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

		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH * SCALE, HEIGHT * SCALE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setUndecorated(true);
		frame.pack();
		frame.setVisible(true);
		game.start();
		
//		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
//
//		JFrame frame = new JFrame(game.TITLE);
//		frame.add(game);
//		frame.pack();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(true);
//		frame.setLocationRelativeTo(null);
//		frame.setVisible(true);
//
//		game.start();
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
