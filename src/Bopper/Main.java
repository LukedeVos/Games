package Bopper;

import Bopper.BufferedImageLoader;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import Bopper.Player;
import Bopper.KeyInput;
import Bopper.Enemy;
import Bopper.BlueDiamond;
import Bopper.SpeedElement;
import Bopper.EnemyShooter;
import Bopper.Bullet;
import Bopper.SpriteSheet;

public class Main extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
	private static final int HEIGHT = WIDTH / 12 * 9;
	private static final int SCALE = 2;
	public final String TITLE = "Bopper";

	private boolean running = false;
	private Thread thread;

	private Player p;
	private Diamond d;
	private BlueDiamond b;
	private SpeedElement s;
	private Font f;

	private boolean inMenu = true;
	private boolean inTutorial = false;
	private boolean inDifficultyMenu = false;
	private boolean inSettings = false;
	private boolean menuKeyReleased = true;
	private boolean paused = false;
	private boolean dead = false;
	private boolean enemyFree = false;
	private boolean enemyShooting = false;
	private boolean levelTransfer = false;
	private boolean glitchy = false;
	private boolean speedCounter = false;
	private boolean bullet = false;
	private boolean titleMusicPlaying = false;
	private boolean blueDiamond;
	private boolean speedElement;
	private boolean glitchyTextures = false;
	private boolean volume = true;
	private boolean eating = false;
	private boolean diamondEaten;

	private int menuSeperator = 15;
	private int menuChoice = 0;
	private int difficulty = 1;
	private int smallScore = 0;
	private int bigScore = 0;
	private int maxEVel = 3;
	private int level = 1;
	private int nextLevel;
	private int blueTimer = 0;
	private int blueFinal = 180;
	private int diaTimer = 0;
	private int speedTimer = 0;
	private int bulletCounter = 0;
	private int bulletTimer = 0;
	private int shooterShooting = 0;
	private int transferTimer;
	private int frames = 0;

	private int pWIDTH = 16;
	private int dWIDTH = 6;
	private int dHEIGHT = 8;

	private static Clip clip;

	private int esRealX;
	private int esRealY;
	private int buRealX;
	private int buRealY;

	private double pERatio;
	private double pESRatio;
	private double eDRatio;
	private double randomizer;
	private double xPDDiff;
	private double yPDDiff;
	private double pDDiff;
	private double rootedPDDiff;
	private double xPBDiff;
	private double yPBDiff;
	private double pBDiff;
	private double rootedPBDiff;
	private double xPEDiff;
	private double yPEDiff;
	private double pEDiff;
	private double rootedPEDiff;
	private double xPSDiff;
	private double yPSDiff;
	private double pSDiff;
	private double rootedPSDiff;
	private double xPESDiff;
	private double yPESDiff;
	private double xPBUDiff;
	private double yPBUDiff;
	private double pBUDiff;
	private double rootedPBUDiff = 100;
	private double xEDDiff;
	private double yEDDiff;
	private double eDDiff;
	private double rootedEDDiff;
	private double speedInc = 1;
	private double speedElementCounter;
	private double pVel = 2;
	private double bVel = 12;
	private double eVel = 0;
	private double sVel = 40;

	private String difficultyString = "Normal";
	private String fileName = "Bopper_sav.txt";
	private String line = null;

	Random rand = new Random();
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	private BufferedImage diamond;
	private BufferedImage blue;
	BufferedImageLoader loader = new BufferedImageLoader();

	private ArrayList<Bullet> bullets;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyShooter> shooters;
	private ArrayList<EnemyEater> eaters;

	public void init() {
		requestFocus();
		try {
			spriteSheet = loader.loadImage("/res/Sprite_Sheet_Bopper.png");
		} catch(IOException e) {
			e.printStackTrace();
		}
		addKeyListener(new KeyInput(this));

		int diaX = rand.nextInt(getWidth() - 40) + 40;
		int diaY = rand.nextInt(getHeight() - 40) + 40;

		p = new Player(10, 10, this);
		d = new Diamond(diaX, diaY, this);
		b = new BlueDiamond(-20, -20, this);
		enemies = new ArrayList<Enemy>();
		eaters = new ArrayList<EnemyEater>();
		s = new SpeedElement(-20, -20, this);
		shooters = new ArrayList<EnemyShooter>();
		bullets = new ArrayList<Bullet>();
		f = new Font("Arial", Font.PLAIN, 14);

		enemies.add(new Enemy(getWidth() - 30, getHeight() - 30, this));

		diamond = new SpriteSheet(this.getSpriteSheet())
				.grabImage(1, 2, 16, 16);
		blue = new SpriteSheet(this.getSpriteSheet()).grabImage(2, 2, 16, 16);
	}

	public void save() {
		PrintWriter writer;
		if(new File("/home/luke/Desktop/Prof1select", "UTF-8").exists()){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Profile1/Bopper_sav.txt", "UTF-8");
				writer.println(level);
				writer.println(smallScore);
				writer.println(bigScore);
				writer.close();
				System.out.println("file saved");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if(new File("/home/luke/Desktop/Prof2select", "UTF-8").exists()){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Profile2/Bopper_sav.txt", "UTF-8");
				writer.println(level);
				writer.println(smallScore);
				writer.println(bigScore);
				writer.close();
				System.out.println("file saved");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if(new File("/home/luke/Desktop/Prof2select", "UTF-8").exists()){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Profile3/Bopper_sav.txt", "UTF-8");
				writer.println(level);
				writer.println(smallScore);
				writer.println(bigScore);
				writer.close();
				System.out.println("file saved");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if(new File("/home/luke/Desktop/Prof2select", "UTF-8").exists()){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Profile4/Bopper_sav.txt", "UTF-8");
				writer.println(level);
				writer.println(smallScore);
				writer.println(bigScore);
				writer.close();
				System.out.println("file saved");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if(new File("/home/luke/Desktop/Prof2select", "UTF-8").exists()){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Profile5/Bopper_sav.txt", "UTF-8");
				writer.println(level);
				writer.println(smallScore);
				writer.println(bigScore);
				writer.close();
				System.out.println("file saved");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			try {
				writer = new PrintWriter("/home/luke/Desktop/Bopper_sav.txt", "UTF-8");
				writer.println(level);
				writer.println(smallScore);
				writer.println(bigScore);
				writer.close();
				System.out.println("file saved");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public void load() {
		int reading = 0;

		try {
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null) {
				int temp = Integer.parseInt(line);
				if(reading == 0) {
					level = temp;
				} else if(reading == 1) {
					smallScore = temp;
				} else if(reading == 2) {
					bigScore = temp;
				}
				reading++;
			}
			bufferedReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch(IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	public static synchronized void playSound(final String url) {
		new Thread(new Runnable() {
			public void run() {
				try {
					clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(Main.class
									.getResourceAsStream("/res/" + url));
					clip.open(inputStream);
					clip.start();
				} catch(Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
	}

	public void start() {
		if(running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		if(!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
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

		// Game loop starts here
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				if(!paused) {
					tick();
				} else {
					render();
					frames++;
				}
				updates++;
				delta--;
			}
			// music();
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ticks, Fps " + frames);
				updates = 0;
				frames = 0;
			}
		}
		stop();
	}

	private void tick() {
		render();
		frames++;

		if(glitchy && !glitchyTextures) {
			try {
				spriteSheet = loader.loadImage("/res/Sprite_Sheet_Bopper2.png");
			} catch(IOException e) {
				e.printStackTrace();
			}
			glitchyTextures = true;
		}

		p.tick(this);

		if(p.getX() <= -11) {
			p.setX(getWidth() + 9);
		} else if(p.getX() >= getWidth() + 10) {
			p.setX(-10);
		}

		if(p.getY() <= -11) {
			p.setY(getHeight() + 9);
		} else if(p.getY() >= getHeight() + 10) {
			p.setY(-10);
		}

		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).tick();
		}

		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).tick();
		}

		for(int i = 0; i < eaters.size(); i++) {
			eaters.get(i).tick();
		}

		// Enemy to Player difference
		for(int i = 0; i < enemies.size(); i++) {
			xPEDiff = p.getX() - enemies.get(i).getX();
			yPEDiff = p.getY() - enemies.get(i).getY();
			pEDiff = (xPEDiff * xPEDiff) + (yPEDiff * yPEDiff);
			rootedPEDiff = Math.sqrt(pEDiff);
			if(rootedPEDiff <= (pWIDTH / 2) + (pWIDTH / 2) && !levelTransfer) {
				dead = true;
				randomizer = rand.nextInt(2);
				if(randomizer == 0 && volume) {
					playSound("Bopper_Death.wav");
				} else if(randomizer == 1 && volume) {
					playSound("Bopper_Death2.wav");
				}
				p.setX(10);
				p.setY(10);
				p.setVelX(0);
				p.setVelY(0);
				for(int j = 0; j < enemies.size(); j++) {
					enemies.remove(j);
				}
				for(int j = 0; j < eaters.size(); j++) {
					eaters.remove(j);
				}
				eVel = 0;
				rootedPEDiff = 100;
				enemyFree = false;
				enemyShooting = false;
				eating = false;
				if(bullet) {
					shooters.get(i).setX(-20);
					shooters.get(i).setY(-20);
					bullets.get(0).setX(-20);
					bullets.get(0).setY(-20);
					bullet = false;
					enemyShooting = false;
					rootedPBUDiff = 100;
					for(int j = 0; j < bullets.size(); j++) {
						bullets.remove(j);
					}
				}
			}
			if(smallScore >= 3 || bigScore >= 1 || rootedPEDiff <= 30) {
				if(!(level >= 5)) {
					enemyFree = true;
				}
			}
			if(enemyFree) {
				if(eVel < maxEVel + difficulty && level <= 5) {
					eVel = 2 + ((level - 1) / 4) + ((difficulty - 1) / 2)
							+ (i / 1.75);
				} else if(eVel < maxEVel + difficulty && level >= 10) {
					eVel = 2 + ((level - 6) / 4) + ((difficulty - 1) / 2)
							+ (i / 1.75);
				}
				if(glitchy) {
					eVel = 2 + ((level - 1) / 3) + ((difficulty - 1) / 2);
				}
			}
			if(!inMenu && !inTutorial && !inDifficultyMenu && !dead
					&& enemyFree && !levelTransfer) {
				if(xPEDiff > yPEDiff) {
					if(yPEDiff != 0) {
						if(xPEDiff > 0) {
							pERatio = xPEDiff / yPEDiff;
						} else if(xPEDiff < 0) {
							pERatio = -(xPEDiff / yPEDiff);
						}
						if(pERatio < 0) {
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1) * pERatio);
						enemies.get(i).setVelY(eVel / (pERatio + 1));
						if(xPEDiff < 0) {
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0) {
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelX(eVel);
					}
				} else if(xPEDiff < yPEDiff) {
					if(xPEDiff != 0) {
						if(yPEDiff > 0) {
							pERatio = yPEDiff / xPEDiff;
						} else if(yPEDiff < 0) {
							pERatio = -(yPEDiff / xPEDiff);
						}
						if(pERatio < 0) {
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1));
						enemies.get(i).setVelY(eVel / (pERatio + 1) * pERatio);
						if(xPEDiff < 0) {
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0) {
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelY(eVel);
					}
				} else if(-xPEDiff > yPEDiff) {
					if(yPEDiff != 0) {
						if(xPEDiff > 0) {
							pERatio = xPEDiff / yPEDiff;
						} else if(xPEDiff < 0) {
							pERatio = -(xPEDiff / yPEDiff);
						}
						if(pERatio < 0) {
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1) * pERatio);
						enemies.get(i).setVelY(eVel / (pERatio + 1));
						if(xPEDiff < 0) {
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0) {
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelX(eVel);
					}
				} else if(-xPEDiff < yPEDiff) {
					if(xPEDiff != 0) {
						if(yPEDiff > 0) {
							pERatio = yPEDiff / xPEDiff;
						} else if(yPEDiff < 0) {
							pERatio = -(yPEDiff / xPEDiff);
						}
						if(pERatio < 0) {
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1));
						enemies.get(i).setVelY(eVel / (pERatio + 1) * pERatio);
						if(xPEDiff < 0) {
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0) {
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelY(eVel);
					}
				}
			}
		}

		// Diamond to Player difference
		xPDDiff = p.getX() - d.getX();
		yPDDiff = p.getY() - d.getY();

		// BlueDiamond to Player difference
		xPBDiff = p.getX() - b.getX();
		yPBDiff = p.getY() - b.getY();

		// SpeedElement to Player difference
		xPSDiff = p.getX() - s.getX();
		yPSDiff = p.getY() - s.getY();

		// Bullet to Player difference
		if(bullet && !levelTransfer) {
			for(int i = 0; i < bullets.size(); i++) {
				buRealX = (int) bullets.get(i).getX();
				buRealY = (int) bullets.get(i).getY();
				xPBUDiff = p.getX() - buRealX;
				yPBUDiff = p.getY() - buRealY;
				pBUDiff = (xPBUDiff * xPBUDiff) + (yPBUDiff * yPBUDiff);
				rootedPBUDiff = Math.sqrt(pBUDiff);
				if(rootedPBUDiff <= (pWIDTH / 2)) {
					dead = true;
					p.setX(10);
					p.setY(10);
					p.setVelX(0);
					p.setVelY(0);
					for(int j = 0; j < enemies.size(); j++) {
						enemies.remove(j);
					}
					for(int j = 0; j < shooters.size(); j++) {
						shooters.remove(j);
					}
					for(int j = 0; j < eaters.size(); j++) {
						eaters.remove(j);
					}
					eVel = 0;
					bullet = false;
					enemyShooting = false;
					eating = false;
					rootedPEDiff = 100;
					rootedPBUDiff = 100;
					enemyFree = false;
					bullets.remove(i);
				}
			}
		}

		// Distance Player to Diamond
		pDDiff = (xPDDiff * xPDDiff) + (yPDDiff * yPDDiff);
		rootedPDDiff = Math.sqrt(pDDiff);
		if(rootedPDDiff <= (dWIDTH / 2) + (pWIDTH / 2)) {
			smallScore += 1;
			d.setX(rand.nextInt(getWidth() - 80) + 40);
			d.setY(rand.nextInt(getHeight() - 80) + 40);
			randomizer = rand.nextInt(10) + 1;
			System.out.println("Spawning: " + randomizer);
		}

		// Blue Gem detection
		if(randomizer == 3) {
			blueDiamond = true;
			b.setX(rand.nextInt(getWidth() - 80) + 40);
			b.setY(rand.nextInt(getHeight() - 80) + 40);
			blueTimer = 0;
			randomizer = 1;
		}

		// BlueDiamond timer
		if(blueDiamond) {
			blueTimer++;
		}
		if(getHeight() > 480) {
			blueFinal = 600;
		} else if(getHeight() < 480) {
			blueFinal = 180;
		}

		// BlueDiamond timer final
		if(blueDiamond && blueTimer >= blueFinal) {
			blueDiamond = false;
			b.setX(-20);
			b.setY(-20);
			blueTimer = 0;
		}

		// Distance Player to BlueDiamond
		pBDiff = (xPBDiff * xPBDiff) + (yPBDiff * yPBDiff);
		rootedPBDiff = Math.sqrt(pBDiff);
		if(rootedPBDiff <= (dWIDTH / 2) + (pWIDTH / 2)) {
			bigScore += 1;
			blueDiamond = false;
			b.setX(-20);
			b.setY(-20);
			blueTimer = 0;
			randomizer = (rand.nextInt(6) + 4);
			System.out.println("Spawning: " + randomizer);
		}

		// SpeedElement detection
		if(randomizer == 5 || randomizer == 7) {
			speedElement = true;
			s.setX(rand.nextInt(getWidth() - 40) + 40);
			s.setY(rand.nextInt(getHeight() - 40) + 40);
			randomizer = 1;
		}

		// Distance Player to SpeedElement
		pSDiff = (xPSDiff * xPSDiff) + (yPSDiff * yPSDiff);
		rootedPSDiff = Math.sqrt(pSDiff);
		if(rootedPSDiff <= (dWIDTH / 2) + (pWIDTH / 2)) {
			pVel += speedInc;
			speedElement = false;
			speedCounter = true;
			speedTimer = 0;
			speedElementCounter++;
			if(!glitchy) {
				rootedPSDiff = 100;
				s.setX(-20);
				s.setY(-20);
			}
		}

		// Speed++
		if(speedCounter) {
			speedTimer++;
		}

		// Speed Timer
		if(speedTimer >= 300) {
			pVel -= speedInc;
			speedElementCounter--;
			if(speedElementCounter == 0) {
				speedCounter = false;
			}
			speedTimer = 0;
			System.out.println("Player speed: " + pVel);
		}

		// Score count
		if(smallScore >= 10) {
			smallScore -= 10;
			bigScore += 1;
		}

		// Shooter AI
		if(level == 5) {
			enemyShooting = true;
		}
		if(enemyShooting) {
			if(level < 7) {
				sVel = 40 - ((level - 5) * 5) - ((difficulty - 1) * 3);
			}
		}

		// Bullet AI
		if(enemyShooting && !dead && !levelTransfer) {
			esRealX = (int) shooters.get(shooterShooting).getX();
			esRealY = (int) shooters.get(shooterShooting).getY();
			xPESDiff = p.getX() - esRealX;
			yPESDiff = p.getY() - esRealY;
			if(bulletTimer == sVel) {
				if(xPESDiff > yPESDiff && !(xPESDiff > 0)) {
					if(yPESDiff != 0) {
						if(xPESDiff > 0) {
							pESRatio = xPESDiff / yPESDiff;
						} else if(xPESDiff < 0) {
							pESRatio = -(xPESDiff / yPESDiff);
						}
						if(pESRatio < 0) {
							pESRatio = -pESRatio;
						}
						bullets.add(new Bullet(esRealX, esRealY, this));
						bullets.get(bullets.size() - 1).setVelX(
								-(bVel / (pESRatio + 1) * pESRatio));
						bullets.get(bullets.size() - 1).setVelY(
								-(bVel / (pESRatio + 1)));
						bullet = true;
					}
				} else if(xPESDiff < yPESDiff && !(xPESDiff > 0)) {
					if(xPESDiff != 0) {
						if(yPESDiff > 0) {
							pESRatio = yPESDiff / xPESDiff;
						} else if(yPESDiff < 0) {
							pESRatio = -(yPESDiff / xPESDiff);
						}
						if(pESRatio < 0) {
							pESRatio = -pESRatio;
						}
						bullets.add(new Bullet(esRealX, esRealY, this));
						bullets.get(bullets.size() - 1).setVelY(
								-(bVel / (pESRatio + 1) * pESRatio));
						bullets.get(bullets.size() - 1).setVelX(
								-(bVel / (pESRatio + 1)));
						bullet = true;
					}
				} else if(-xPESDiff > yPESDiff) {
					if(xPESDiff != 0) {
						if(yPESDiff > 0) {
							pESRatio = yPESDiff / xPESDiff;
						} else if(yPESDiff < 0) {
							pESRatio = -(yPESDiff / xPESDiff);
						}
						if(pESRatio < 0) {
							pESRatio = -pESRatio;
						}
						bullets.add(new Bullet(esRealX, esRealY, this));
						bullets.get(bullets.size() - 1).setVelY(
								-(bVel / (pESRatio + 1) * pESRatio));
						bullets.get(bullets.size() - 1).setVelX(
								(bVel / (pESRatio + 1)));
						bullet = true;
					}
				} else if(-xPESDiff < yPESDiff) {
					if(xPESDiff != 0) {
						if(yPESDiff > 0) {
							pESRatio = yPESDiff / xPESDiff;
						} else if(yPESDiff < 0) {
							pESRatio = -(yPESDiff / xPESDiff);
						}
						if(pESRatio < 0) {
							pESRatio = -pESRatio;
						}
						bullets.add(new Bullet(esRealX, esRealY, this));
						bullets.get(bullets.size() - 1).setVelY(
								-(bVel / (pESRatio + 1) * pESRatio));
						bullets.get(bullets.size() - 1).setVelX(
								(bVel / (pESRatio + 1)));
						bullet = true;
					}
				}
				bulletTimer = 0;
				if(shooters.size() != 1) {
					shooterShooting++;
					if(shooterShooting == shooters.size()) {
						shooterShooting = 0;
					}
				}
			}
			bulletTimer++;
		}

		if(enemyShooting && bullet) {
			for(int i = 0; i < bullets.size(); i++) {
				if(bullets.get(bulletCounter).getX() < 0
						|| bullets.get(bulletCounter).getY() < 0) {
					bullets.remove(bulletCounter);
				}
			}
		}

		// Eater AI
		if(eating && !diamondEaten) {
			for(int i = 0; i < eaters.size(); i++) {
				xEDDiff = d.getX() - eaters.get(i).getX();
				yEDDiff = d.getY() - eaters.get(i).getY();
				eDDiff = (xEDDiff * xEDDiff) + (yEDDiff * yEDDiff);
				rootedEDDiff = Math.sqrt(eDDiff);
				if(rootedEDDiff <= (pWIDTH / 2) + (dWIDTH / 2)
						&& !levelTransfer) {
					d.setX(-20);
					d.setY(-20);
					diamondEaten = true;
				}
				if(eVel < maxEVel + difficulty && level <= 5) {
					eVel = 2.3;
				}
				if(!inMenu && !inTutorial && !inDifficultyMenu && !dead
						&& !levelTransfer) {
					if(xEDDiff > yEDDiff) {
						if(yEDDiff != 0) {
							if(xEDDiff > 0) {
								eDRatio = xEDDiff / yEDDiff;
							} else if(xEDDiff < 0) {
								eDRatio = -(xEDDiff / yEDDiff);
							}
							if(eDRatio < 0) {
								eDRatio = -eDRatio;
							}
							eaters.get(i).setVelX(
									eVel / (eDRatio + 1) * eDRatio);
							eaters.get(i).setVelY(eVel / (eDRatio + 1));
							if(xEDDiff < 0) {
								eaters.get(i).setVelX(
										-(eaters.get(i).getVelX()));
							}
							if(yEDDiff < 0) {
								eaters.get(i).setVelY(
										-(eaters.get(i).getVelY()));
							}
						} else {
							eaters.get(i).setVelX(eVel);
						}
					} else if(xEDDiff < yEDDiff) {
						if(xEDDiff != 0) {
							if(yEDDiff > 0) {
								eDRatio = yEDDiff / xEDDiff;
							} else if(yEDDiff < 0) {
								eDRatio = -(yEDDiff / xEDDiff);
							}
							if(eDRatio < 0) {
								eDRatio = -eDRatio;
							}
							eaters.get(i).setVelX(eVel / (eDRatio + 1));
							eaters.get(i).setVelY(
									eVel / (eDRatio + 1) * eDRatio);
							if(xEDDiff < 0) {
								eaters.get(i).setVelX(
										-(eaters.get(i).getVelX()));
							}
							if(yEDDiff < 0) {
								eaters.get(i).setVelY(
										-(eaters.get(i).getVelY()));
							}
						} else {
							eaters.get(i).setVelY(eVel);
						}
					} else if(-xEDDiff > yEDDiff) {
						if(yEDDiff != 0) {
							if(xEDDiff > 0) {
								eDRatio = xEDDiff / yEDDiff;
							} else if(xEDDiff < 0) {
								eDRatio = -(xEDDiff / yEDDiff);
							}
							if(eDRatio < 0) {
								eDRatio = -eDRatio;
							}
							eaters.get(i).setVelX(
									eVel / (eDRatio + 1) * eDRatio);
							eaters.get(i).setVelY(eVel / (eDRatio + 1));
							if(xEDDiff < 0) {
								eaters.get(i).setVelX(
										-(eaters.get(i).getVelX()));
							}
							if(yEDDiff < 0) {
								eaters.get(i).setVelY(
										-(eaters.get(i).getVelY()));
							}
						} else {
							eaters.get(i).setVelX(eVel);
						}
					} else if(-xEDDiff < yEDDiff) {
						if(xEDDiff != 0) {
							if(yEDDiff > 0) {
								eDRatio = yEDDiff / xEDDiff;
							} else if(yEDDiff < 0) {
								eDRatio = -(yEDDiff / xEDDiff);
							}
							if(eDRatio < 0) {
								eDRatio = -eDRatio;
							}
							eaters.get(i).setVelX(eVel / (eDRatio + 1));
							eaters.get(i).setVelY(
									eVel / (eDRatio + 1) * eDRatio);
							if(xEDDiff < 0) {
								eaters.get(i).setVelX(
										-(eaters.get(i).getVelX()));
							}
							if(yEDDiff < 0) {
								eaters.get(i).setVelY(
										-(eaters.get(i).getVelY()));
							}
						} else {
							eaters.get(i).setVelY(eVel);
						}
					}
				}
			}
		}

		if(diamondEaten) {
			diaTimer++;
			if(!glitchy) {
				for(int i = 0; i < eaters.size(); i++) {
					eaters.get(i).setVelX(0);
					eaters.get(i).setVelY(0);
				}
			}
		}

		if(diaTimer >= 300) {
			d.setX(rand.nextInt(getWidth() - 80) + 40);
			d.setY(rand.nextInt(getHeight() - 80) + 40);
			diamondEaten = false;
			diaTimer = 0;
		}

		// Level system
		if((bigScore == 1 && smallScore >= 5) || (bigScore > 1)) {
			smallScore = 0;
			bigScore = 0;
			levelTransfer = true;
			transferTimer = 0;
			nextLevel = level + 1;
			p.setVelX(0);
			p.setVelY(0);
			for(int i = 0; i < enemies.size(); i++) {
				enemies.get(i).setVelX(0);
				enemies.get(i).setVelY(0);
			}
			if(eating) {
				for(int i = 0; i < eaters.size(); i++) {
					eaters.get(i).setVelX(0);
					eaters.get(i).setVelY(0);
				}
			}
			rootedPEDiff = 100;
			bulletTimer = 0;
		}

		if(levelTransfer) {
			transferTimer++;
		}

		if(transferTimer >= 180) {
			levelTransfer = false;
			level++;
			if(!glitchy) {
				if(level == 5) {
					for(int i = 0; i < enemies.size(); i++) {
						enemies.remove(i);
						System.out.println("Removed enemy #" + i);
					}
					enemies.remove(0);
					System.out.println("Removed enemy #1");
					shooters.add(new EnemyShooter(getWidth() - 20,
							getHeight() - 20, this));
				}
				if(level == 3 || level == 10 || level == 13) {
					enemies.add(new Enemy(getWidth() - 20, getHeight() - 20,
							this));
				}
				if(level == 7) {
					shooters.add(new EnemyShooter(20, getHeight() - 20, this));
				}
				if(level == 2) {
					eaters.add(new EnemyEater(getWidth() - 30,
							getHeight() - 30, this));
					eating = true;
				}
			}
			if(!glitchy) {
				transferTimer = 0;
			} else if(glitchy) {
				shooters.add(new EnemyShooter(getWidth() - 30,
						getHeight() - 30, this));
				eaters.add(new EnemyEater(getWidth() - 30, getHeight() - 30,
						this));
			}
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		// ////////////////////////////////
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.white);

		g.setFont(f);

		if(inMenu) {
			g.drawString("Play", getWidth() / 2 - 50, getHeight() / 2 - 50
					- menuSeperator);
			g.drawString("Tutorial", getWidth() / 2 - 50, getHeight() / 2 - 50
					+ menuSeperator);
			g.drawString("Difficulty", getWidth() / 2 - 50, getHeight() / 2
					- 50 + (menuSeperator * 3));
			g.drawString("Settings", getWidth() / 2 - 50, getHeight() / 2 - 50
					+ (menuSeperator * 5));
			g.drawString("Exit", getWidth() / 2 - 50, getHeight() / 2 - 50
					+ (menuSeperator * 7));

			if(menuChoice == 0) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						- menuSeperator);
			} else if(menuChoice == 1) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						+ menuSeperator);
			} else if(menuChoice == 2) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						+ (menuSeperator * 3));
			} else if(menuChoice == 3) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						+ (menuSeperator * 5));
			} else if(menuChoice == 4) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						+ (menuSeperator * 7));
			}
		}

		if(inTutorial) {
			g.drawString("To be added Soon", getWidth() / 2 - 50, getHeight()
					/ 2 - 50 - menuSeperator);
			g.drawString("Press 'Enter' to return", getWidth() / 2 - 50,
					getHeight() / 2 - 50 + menuSeperator);

		}

		if(inDifficultyMenu) {
			g.drawString("Easy", getWidth() / 2 - 50, getHeight() / 2 - 50
					- menuSeperator);
			g.drawString("Normal", getWidth() / 2 - 50, getHeight() / 2 - 50
					+ menuSeperator);
			g.drawString("Hard", getWidth() / 2 - 50, getHeight() / 2 - 50
					+ (menuSeperator * 3));

			if(menuChoice == 0) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						- menuSeperator);
			} else if(menuChoice == 1) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						+ menuSeperator);
			} else if(menuChoice == 2) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						+ (menuSeperator * 3));
			} else if(menuChoice >= 10) {
				g.drawString("->", getWidth() / 2, getHeight() / 2 - 50
						+ menuSeperator);
			}
		}

		if(inSettings) {
			g.drawString("Sound: ", getWidth() / 2 - 50, getHeight() / 2 - 50
					- menuSeperator);
			g.drawString("Accept", getWidth() / 2 - 50, getHeight() / 2 - 50
					+ menuSeperator);

			if(menuChoice == 0) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						- menuSeperator);
			} else if(menuChoice == 1) {
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50
						+ menuSeperator);
			}

			if(volume) {
				g.drawString("on", getWidth() / 2 - 50
						+ g.getFontMetrics(f).stringWidth("Sound: "),
						getHeight() / 2 - 50 - menuSeperator);
			} else if(!volume) {
				g.drawString("off", getWidth() / 2 - 50
						+ g.getFontMetrics(f).stringWidth("Sound: "),
						getHeight() / 2 - 50 - menuSeperator);
			}
		}

		if(dead) {
			g.setColor(Color.WHITE);
			g.drawString("You Died!", getWidth() / 2 - 50, getHeight() / 2 - 50
					- menuSeperator);
			g.drawString("Score:    x" + bigScore + "    x" + smallScore,
					getWidth() / 2 - 50, getHeight() / 2 - 50 + menuSeperator);
			g.drawString("Difficulty: ", getWidth() / 2 - 50, getHeight() / 2
					- 50 + (menuSeperator * 3));

			if(difficulty == 0) {
				g.setColor(Color.GREEN);
			} else if(difficulty == 1) {
				g.setColor(Color.YELLOW);
			} else if(difficulty == 2) {
				g.setColor(Color.RED);
			}

			g.drawString(difficultyString, getWidth() / 2 - 50
					+ g.getFontMetrics(f).stringWidth("Difficulty: "),
					getHeight() / 2 - 50 + (menuSeperator * 3));

			g.setColor(Color.WHITE);
			g.drawString("Level: " + level, getWidth() / 2 - 50, getHeight()
					/ 2 - 50 + (menuSeperator * 5));
			g.drawString("Press 'Enter' to return to menu",
					getWidth() / 2 - 50, getHeight() / 2 - 50
							+ (menuSeperator * 7));

			g.drawImage(blue, getWidth() / 2 - 50
					+ g.getFontMetrics(f).stringWidth("Score: ") - dWIDTH,
					getHeight() / 2 - 59 + menuSeperator - (dHEIGHT / 2), null);
			g.drawImage(
					diamond,
					getWidth()
							/ 2
							- 50
							+ g.getFontMetrics(f).stringWidth(
									"Score:    x" + bigScore + " ") - dWIDTH,
					getHeight() / 2 - 59 + menuSeperator - (dHEIGHT / 2), null);

		}

		if(levelTransfer) {
			g.setColor(Color.WHITE);
			g.drawString("You defeated level: " + level + "!",
					getWidth() / 2 - 50, getHeight() / 2 - 50 - menuSeperator);
			g.drawString("Now starting level: " + nextLevel,
					getWidth() / 2 - 50, getHeight() / 2 - 50 + menuSeperator);
		}

		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead
				&& !levelTransfer && !inSettings) {
			if(titleMusicPlaying) {
				clip.stop();
				titleMusicPlaying = false;
			}

			// Ingame Rendering
			p.render(g);
			d.render(g);

			if(blueDiamond) {
				b.render(g);
			}

			if(speedElement) {
				s.render(g);
			}

			for(int i = 0; i < enemies.size(); i++) {
				enemies.get(i).render(g);
			}
			if(!enemyFree) {
				enemies.get(0).setX(getWidth() - 30);
				enemies.get(0).setY(getHeight() - 30);
			}

			for(int i = 0; i < shooters.size(); i++) {
				shooters.get(i).render(g);
			}

			if(bullet) {
				for(int i = 0; i < bullets.size(); i++) {
					g.setColor(Color.WHITE);
					g.drawRect((int) bullets.get(i).getX(), (int) bullets
							.get(i).getY(), 1, 1);
				}
			}

			if(eating) {
				for(int i = 0; i < eaters.size(); i++) {
					eaters.get(i).render(g);
				}
			}

			// Cage
			if(!enemyFree) {
				g.setColor(Color.WHITE);
				g.drawRect((int) enemies.get(0).getX() - 2, (int) enemies
						.get(0).getY() - 2, 19, 1);
				g.drawRect((int) enemies.get(0).getX() - 2, (int) enemies
						.get(0).getY() + 12, 19, 1);
				g.drawRect((int) enemies.get(0).getX(), (int) enemies.get(0)
						.getY() - 2, 1, 14);
				g.drawRect((int) enemies.get(0).getX() + 13,
						(int) enemies.get(0).getY() - 2, 1, 14);
				g.drawRect((int) enemies.get(0).getX() + 6, (int) enemies
						.get(0).getY() - 2, 1, 14);
			}

			// Score Display
			g.setColor(Color.BLACK);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 11,
					getHeight() - 17);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 9,
					getHeight() - 19);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 11,
					getHeight() - 19);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 9,
					getHeight() - 17);
			g.setColor(Color.WHITE);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 10,
					getHeight() - 18);

			g.drawImage(
					diamond,
					10
							+ g.getFontMetrics(f).stringWidth(
									"Score:    x" + bigScore + "   ") - dWIDTH,
					(getHeight() - 27) - (dHEIGHT / 2), null);
			g.drawImage(blue, 10 + g.getFontMetrics(f).stringWidth("Score:   ")
					- dWIDTH, (getHeight() - 27) - (dHEIGHT / 2), null);
		}

		if(paused && !levelTransfer) {
			g.setColor(Color.WHITE);
			g.drawString("Paused", getWidth() / 2
					- (g.getFontMetrics(f).stringWidth("Paused") / 2),
					getHeight() / 2 - 3);
		}

		// ////////////////////////////////
		g.dispose();
		bs.show();
	}

	public void music() {
		if(!titleMusicPlaying && volume) {
			playSound("Bopper_Title_Music.wav");
			titleMusicPlaying = true;
		}
		if(!volume) {
			clip.stop();
		}
	}

	public void keyPressed(KeyEvent k) {
		int key = k.getKeyCode();
		System.out.println(key);

		if(inMenu) {
			if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
				menuChoice += 1;
				if(menuChoice > 4) {
					menuChoice = 0;
				}
			}
			if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
				menuChoice -= 1;
				if(menuChoice < 0) {
					menuChoice = 3;
				}
			}
			if(key == KeyEvent.VK_ENTER && menuKeyReleased) {
				if(menuChoice == 0) {
					inMenu = false;
				} else if(menuChoice == 1) {
					inTutorial = true;
					inMenu = false;
				} else if(menuChoice == 2) {
					inDifficultyMenu = true;
					inMenu = false;
					menuChoice = 1;
				} else if(menuChoice == 3) {
					inSettings = true;
					inMenu = false;
					menuChoice = 0;
				} else if(menuChoice == 4) {
					System.exit(1);
				}
				menuKeyReleased = false;
			}
		}

		if(inTutorial && menuKeyReleased) {
			if(key == KeyEvent.VK_ENTER) {
				inTutorial = false;
				inMenu = true;
			}
		}

		if(inDifficultyMenu && menuKeyReleased) {
			if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
				menuChoice += 1;
				if(menuChoice > 2) {
					menuChoice = 0;
				}
			}
			if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
				menuChoice -= 1;
				if(menuChoice < 0) {
					menuChoice = 2;
				}
			}
			if(key == KeyEvent.VK_RIGHT) {
				if(menuChoice < 10) {
					menuChoice += 10;
				}
			}
			if(key == KeyEvent.VK_LEFT) {
				if(menuChoice >= 10) {
					menuChoice -= 10;
				}
			}

			if(key == KeyEvent.VK_ENTER) {
				if(menuChoice == 0) {
					difficulty = 0;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Easy";
				} else if(menuChoice == 1) {
					difficulty = 1;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Normal";
				} else if(menuChoice == 2) {
					difficulty = 2;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Hard";
				} else if(menuChoice >= 10) {
					glitchy = true;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Glitch Mode";
				}
				System.out.println("Difficulty: " + difficulty);
			}
		}

		if(inSettings && menuKeyReleased) {
			if(key == KeyEvent.VK_UP) {
				menuChoice -= 1;
				if(menuChoice < 0) {
					menuChoice = 1;
				}
			} else if(key == KeyEvent.VK_DOWN) {
				menuChoice += 1;
				if(menuChoice > 1) {
					menuChoice = 0;
				}
			}

			if(key == KeyEvent.VK_ENTER) {
				if(menuChoice == 0) {
					volume = !volume;
				}
				if(menuChoice == 1) {
					inMenu = true;
					inSettings = false;
					menuChoice = 3;
				}
			}
		}

		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead
				&& !levelTransfer) {
			if((key == KeyEvent.VK_UP || key == KeyEvent.VK_W)) {
				p.setVelY(-pVel);
			} else if((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S)) {
				p.setVelY(pVel);
			} else if((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A)) {
				p.setVelX(-pVel);
			} else if((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D)) {
				p.setVelX(pVel);
			}

			if(key == KeyEvent.VK_P) {
				paused = true;
				render();
			}

			if(key == KeyEvent.VK_O) {
				save();
			} else if(key == KeyEvent.VK_L) {
				load();
				if(level >= 5) {
					shooters.add(new EnemyShooter(getWidth() - 20,
							getHeight() - 20, this));
				}
				if((level >= 1 && level < 5) || (level >= 10)) {
					enemies.add(new Enemy(getWidth() - 20, getHeight() - 20,
							this));
					if((level >= 3 && level < 5) || level >= 13) {
						enemies.add(new Enemy(getWidth() - 20,
								getHeight() - 20, this));
					}
				}
				if(level >= 7) {
					shooters.add(new EnemyShooter(20, getHeight() - 20, this));
				}
				if(level >= 2) {
					eaters.add(new EnemyEater(getWidth() - 30,
							getHeight() - 30, this));
					eating = true;
				}
			}

			if(key == KeyEvent.VK_SPACE) {
				bigScore = 1;
				smallScore = 5;
			} else if(key == KeyEvent.VK_Q) {
				randomizer = 5;
			} else if(key == KeyEvent.VK_E) {
				randomizer = 3;
			} else if(key == KeyEvent.VK_5) {
				if(level == 1) {
					level += 3;
					if(enemies.size() < 2) {
						enemies.add(new Enemy(20, 20, this));
					}
					levelTransfer = true;
				} else {
					level += 4;
				}
				bigScore = 3;

			}
		}

		if(dead) {
			if(key == KeyEvent.VK_ENTER) {
				inMenu = true;
				dead = false;
				menuKeyReleased = false;
				enemyFree = false;
				enemyShooting = false;
				bullet = false;
				difficulty = 1;
				smallScore = 0;
				bigScore = 0;
				level = 1;
				if(enemies.size() >= 2) {
					for(int i = 0; i < enemies.size(); i++) {
						enemies.remove(i);
					}
					enemies.remove(0);
				} else {
					for(int i = 0; i < enemies.size(); i++) {
						enemies.remove(i);
					}
				}
				if(shooters.size() >= 2) {
					for(int i = 0; i < shooters.size(); i++) {
						shooters.remove(i);
					}
					shooters.remove(0);
				} else {
					for(int i = 0; i < shooters.size(); i++) {
						shooters.remove(i);
					}
				}
				enemies.add(new Enemy(getWidth() - 20, getHeight() - 50, this));
			}
		}

	}

	public void keyReleased(KeyEvent k) {
		int key = k.getKeyCode();

		if(key == KeyEvent.VK_ENTER) {
			menuKeyReleased = true;
		}

		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead) {
			if(!inMenu && !inTutorial && !inDifficultyMenu) {
				if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
					p.setVelY(0);
				} else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
					p.setVelY(0);
				} else if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
					p.setVelX(0);
				} else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
					p.setVelX(0);
				}
			}
			if(paused) {
				if(key == KeyEvent.VK_ENTER) {
					paused = false;
				}
			}
		}

	}

	public static void main(String args[]) {
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

	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}

}