package Bopper;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.ArrayList;
import javax.swing.JFrame;

import Bopper.Player;
import Bopper.KeyInput;
import Bopper.Enemy;
import Bopper.BlueDiamond;
import Bopper.SpeedElement;
import Bopper.EnemyShooter;
import Bopper.Bullet;

public class Main extends Canvas implements Runnable{

	
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
	private boolean menuKeyReleased = true;
	private boolean paused = false;
	private boolean dead = false;
	private boolean enemyFree = false;
	private boolean enemyShooting = false;
	private boolean levelTransfer = false;
	private boolean glitchy = false;
	private boolean speedCounter = false;
	private boolean bullet = false;
	private boolean blueDiamond;
	private boolean speedElement;
	
	private int menuSeperator = 15;
	private int menuChoice = 0;
	private int difficulty = 1;
	private int smallScore = 0;
	private int bigScore = 0;
	private int maxEVel = 3;
	private int level = 1;
	private int nextLevel;
	private int blueTimer = 0;
	private int speedTimer = 0;
	private int bulletCounter = 0;
	private int bulletTimer = 0;
	private int transferTimer;
	
	private int pWIDTH = 8;
	private int pHEIGHT = 8;
	private int dWIDTH = 6;
	private int dHEIGHT = 8;
	
	
	private int pRealX;
	private int pRealY;
	private int eRealX;
	private int eRealY;
	private int dRealX;
	private int dRealY;
	private int bRealX;
	private int bRealY;
	private int sRealX;
	private int sRealY;
	private int esRealX;
	private int esRealY;
	private int buRealX;
	private int buRealY;
	
	private double pERatio;
	private double pESRatio;
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
	private double speedInc = 1;
	private double speedElementCounter;
	private double pVel = 2;
	private double bVel = 12;
	private double eVel = 0;
	private double sVel = 40;
	
	private String difficultyString = "Normal";
	
	Random rand = new Random();
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	private ArrayList<Bullet> bullets;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyShooter> shooters;
	
	public void init(){
		requestFocus();
		addKeyListener(new KeyInput(this));
		
		
		int  diaX = rand.nextInt(getWidth() - 40) + 40;
		int  diaY = rand.nextInt(getHeight() - 40) + 40;
		
		p = new Player(10, 10, this);
		d = new Diamond(diaX, diaY, this);
		b = new BlueDiamond(-20, -20, this);
		enemies = new ArrayList<Enemy>();
		s = new SpeedElement(-20, -20, this);
		shooters = new ArrayList<EnemyShooter>();
		bullets = new ArrayList<Bullet>();
		f = new Font("Arial", Font.PLAIN, 14);

		enemies.add(new Enemy(getWidth() - 20, getHeight() - 50, this));
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
		
	private void tick() {
		
		pRealX = (int)p.getX() + (pWIDTH / 2);
		pRealY = (int)p.getY() + (pHEIGHT / 2);
		dRealX = (int)d.getX() + (dWIDTH / 2);
		dRealY = (int)d.getY() + (dHEIGHT / 2);
		bRealX = (int)b.getX() + (dWIDTH / 2);
		bRealY = (int)b.getY() + (dHEIGHT / 2);
		sRealX = (int)s.getX() + (dWIDTH / 2);
		sRealY = (int)s.getY() + (dHEIGHT / 2);
		
		p.tick();
		
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).tick();
		}
		
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).tick();
		}
		
			
		
		//Enemy to Player difference
		for(int i = 0; i < enemies.size(); i++){
			eRealX = (int)enemies.get(i).getX() + (pWIDTH / 2);
			eRealY = (int)enemies.get(i).getY() + (pHEIGHT / 2);
			xPEDiff = pRealX - eRealX;
			yPEDiff = pRealY - eRealY;
			pEDiff = (xPEDiff * xPEDiff) + (yPEDiff * yPEDiff);
			rootedPEDiff = Math.sqrt(pEDiff);
			if(rootedPEDiff <= (pWIDTH / 2) + (pWIDTH / 2)){
				dead = true;
				p.setX(10);
				p.setY(10);
				p.setVelX(0);
				p.setVelY(0);
				enemies.get(i).setX(getWidth() - 20);
				enemies.get(i).setY(getHeight() - 50);
				enemies.get(i).setVelX(0);
				enemies.get(i).setVelY(0);
				eVel = 0;
				rootedPEDiff = 100;
				enemyFree = false;
				if(bullet){
					shooters.get(i).setX(-20);
					shooters.get(i).setY(-20);
					bullets.get(0).setX(-20);
					bullets.get(0).setY(-20);
					bullet = false;
					enemyShooting = false;
					rootedPBUDiff = 100;
					for(int j = 0; j < bullets.size(); j++){
						bullets.remove(j);
					}
				}
			}
			if(smallScore >= 3 || bigScore >= 1 || rootedPEDiff <= 30){
				if(!(level >= 5)){
					enemyFree = true;
				}
			}
			if(enemyFree){
				if(eVel < maxEVel + difficulty){
					eVel = 2 + ((level - 1) / 3) + ((difficulty - 1) / 2) + (i / 1.75);
				}
				if(glitchy){
					eVel = 2 + ((level - 1) / 3) + ((difficulty - 1) / 2);
				}
			}
			if(!inMenu && !inTutorial && !inDifficultyMenu && !dead && enemyFree && !levelTransfer){
				if(xPEDiff > yPEDiff){
					if(yPEDiff != 0){
						if(xPEDiff > 0){
							pERatio = xPEDiff / yPEDiff;
						} else if(xPEDiff < 0){
							pERatio = -(xPEDiff / yPEDiff);
						}
						if(pERatio < 0){
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1) * pERatio);
						enemies.get(i).setVelY(eVel / (pERatio + 1));
						if(xPEDiff < 0){
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0){
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelX(eVel);
					}
				} else if(xPEDiff < yPEDiff){
					if(xPEDiff != 0){
						if(yPEDiff > 0){
							pERatio = yPEDiff / xPEDiff;
						} else if(yPEDiff < 0){
							pERatio = -(yPEDiff / xPEDiff);
						}
						if(pERatio < 0){
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1));
						enemies.get(i).setVelY(eVel / (pERatio + 1) * pERatio);
						if(xPEDiff < 0){
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0){
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelY(eVel);
					}
				} else if(-xPEDiff > yPEDiff){
					if(yPEDiff != 0){
						if(xPEDiff > 0){
							pERatio = xPEDiff / yPEDiff;
						} else if(xPEDiff < 0){
							pERatio = -(xPEDiff / yPEDiff);
						}
						if(pERatio < 0){
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1) * pERatio);
						enemies.get(i).setVelY(eVel / (pERatio + 1));
						if(xPEDiff < 0){
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0){
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelX(eVel);
					}
				} else if(-xPEDiff < yPEDiff){
					if(xPEDiff != 0){
						if(yPEDiff > 0){
							pERatio = yPEDiff / xPEDiff;
						} else if(yPEDiff < 0){
							pERatio = -(yPEDiff / xPEDiff);
						}
						if(pERatio < 0){
							pERatio = -pERatio;
						}
						enemies.get(i).setVelX(eVel / (pERatio + 1));
						enemies.get(i).setVelY(eVel / (pERatio + 1) * pERatio);
						if(xPEDiff < 0){
							enemies.get(i).setVelX(-(enemies.get(i).getVelX()));
						}
						if(yPEDiff < 0){
							enemies.get(i).setVelY(-(enemies.get(i).getVelY()));
						}
					} else {
						enemies.get(i).setVelY(eVel);
					}
				} 
			}
		}
		
		//Diamond to Player difference
		xPDDiff = pRealX - dRealX;
		yPDDiff = pRealY - dRealY;
		
		//BlueDiamond to Player difference
		xPBDiff = pRealX - bRealX;
		yPBDiff = pRealY - bRealY;
		
		//SpeedElement to Player difference
		xPSDiff = pRealX - sRealX;
		yPSDiff = pRealY - sRealY;
		
		//Bullet to Player difference
		if(bullet){
			for(int i = 0; i < bullets.size(); i++){
				buRealX = (int)bullets.get(0).getX();
				buRealY = (int)bullets.get(0).getY();
				xPBUDiff = pRealX - buRealX;
				yPBUDiff = pRealY - buRealY;
				pBUDiff = (xPBUDiff * xPBUDiff) + (yPBUDiff * yPBUDiff);
				rootedPBUDiff = Math.sqrt(pBUDiff);
				if(rootedPBUDiff <= (pWIDTH / 2)){
					dead = true;
					p.setX(10);
					p.setY(10);
					p.setVelX(0);
					p.setVelY(0);
					for(int j = 0; i < enemies.size(); i++){
						enemies.remove(j);
					}
					shooters.get(i).setX(-20);
					shooters.get(i).setY(-20);
					bullets.get(0).setX(-20);
					bullets.get(0).setY(-20);
					eVel = 0;
					bullet = false;
					enemyShooting = false;
					rootedPEDiff = 100;
					rootedPBUDiff = 100;
					enemyFree = false;
					for(int j = 0; j < bullets.size(); j++){
						bullets.remove(j);
					}
				}
			}
		}
		
		//Distance Player to Diamond
		pDDiff = (xPDDiff * xPDDiff) + (yPDDiff * yPDDiff);
		rootedPDDiff = Math.sqrt(pDDiff);
		if(rootedPDDiff <= (dWIDTH / 2) + (pWIDTH / 2)){
			smallScore += 1;
			d.setX(rand.nextInt(getWidth() - 40) + 40);
			d.setY(rand.nextInt(getHeight() - 40) + 40);
			randomizer = rand.nextInt(10) + 1;
			System.out.println("Spawning: " + randomizer);
		}
		
		//Blue Gem detection
		if(randomizer == 3){
			blueDiamond = true;
			b.setX(rand.nextInt(getWidth() - 40) + 40);
			b.setY(rand.nextInt(getHeight() - 40) + 40);
			blueTimer = 0;
			randomizer = 1;
		}
		
		//BlueDiamond timer
		if(blueDiamond){
			blueTimer++;
		}
		
		//BlueDiamond timer final
		if(blueDiamond && blueTimer >= 180){
			blueDiamond = false;
			b.setX(-20);
			b.setY(-20);
			blueTimer = 0;
		}
		
		//Distance Player to BlueDiamond
		pBDiff = (xPBDiff * xPBDiff) + (yPBDiff * yPBDiff);
		rootedPBDiff = Math.sqrt(pBDiff);
		if(rootedPBDiff <= (dWIDTH / 2) + (pWIDTH / 2)){
			bigScore += 1;
			blueDiamond = false;
			b.setX(-20);
			b.setY(-20);
			blueTimer = 0;
			randomizer = (rand.nextInt(6) + 4);
			System.out.println("Spawning: " + randomizer);
		}
		
		//SpeedElement detection
		if(randomizer == 5 || randomizer == 7){
			speedElement = true;
			s.setX(rand.nextInt(getWidth() - 40) + 40);
			s.setY(rand.nextInt(getHeight() - 40) + 40);
			randomizer = 1;
		}
		
		//Distance Player to SpeedElement
		pSDiff = (xPSDiff * xPSDiff) + (yPSDiff * yPSDiff);
		rootedPSDiff = Math.sqrt(pSDiff);
		if(rootedPSDiff <= (dWIDTH / 2) + (pWIDTH / 2)){
			pVel += speedInc;
			speedElement = false;
			speedCounter = true;
			speedTimer = 0;
			speedElementCounter++;
			if(!glitchy){
				rootedPSDiff = 100;
				s.setX(-20);
				s.setY(-20);
			}
		}
		
		//Speed++
		if(speedCounter){
			speedTimer++;
		}
		
		//Speed Timer
		if(speedTimer >= 300){
			pVel -= speedInc;
			speedElementCounter--;
			if(speedElementCounter == 0){
				speedCounter = false;
			}
			speedTimer = 0;
			System.out.println("Player speed: " + pVel);
		}
		
		//Score count
		if(smallScore >= 10){
			smallScore -= 10;
			bigScore += 1;
		}
		
		//Shooter AI
		if(level == 5){
			enemyShooting = true;
		}
		if(enemyShooting){
			sVel = 40 - (( level - 5) * 5);
		}
		
		//Bullet AI
		if(enemyShooting == true && !dead && !levelTransfer){
			for(int i = 0; i < shooters.size(); i++){
				System.out.println(i);
				esRealX = (int)shooters.get(i).getX() + (pWIDTH / 2);
				esRealY = (int)shooters.get(i).getY() + (pHEIGHT / 2);
				xPESDiff = pRealX - esRealX;
				yPESDiff = pRealY - esRealY;
				if(bulletTimer == sVel - (sVel * (i / 2))){
					if(xPESDiff > yPESDiff && !(xPESDiff > 0)){
						if(yPESDiff != 0){
							if(xPESDiff > 0){
								pESRatio = xPESDiff / yPESDiff;
							} else if(xPESDiff < 0){
								pESRatio = -(xPESDiff / yPESDiff);
							}
							if(pESRatio < 0){
								pESRatio = -pESRatio;
							}
							bullets.add(new Bullet(esRealX, esRealY, this));
							bullets.get(bullets.size() - 1).setVelX(-(bVel / (pESRatio + 1) * pESRatio));
							bullets.get(bullets.size() - 1).setVelY(-(bVel / (pESRatio + 1)));
							bullet = true;
						}
					} else if(xPESDiff < yPESDiff && !(xPESDiff > 0)){
						if(xPESDiff != 0){
							if(yPESDiff > 0){
								pESRatio = yPESDiff / xPESDiff;
							} else if(yPESDiff < 0){
								pESRatio = -(yPESDiff / xPESDiff);
							}
							if(pESRatio < 0){
								pESRatio = -pESRatio;
							}
							bullets.add(new Bullet(esRealX, esRealY, this));
							bullets.get(bullets.size() - 1).setVelY(-(bVel / (pESRatio + 1) * pESRatio));
							bullets.get(bullets.size() - 1).setVelX(-(bVel / (pESRatio + 1)));
							bullet = true;
						}
					} else if(-xPESDiff > yPESDiff){
						if(xPESDiff != 0){
							if(yPESDiff > 0){
								pESRatio = yPESDiff / xPESDiff;
							} else if(yPESDiff < 0){
								pESRatio = -(yPESDiff / xPESDiff);
							}
							if(pESRatio < 0){
								pESRatio = -pESRatio;
							}
							bullets.add(new Bullet(esRealX, esRealY, this));
							bullets.get(bullets.size() - 1).setVelY(-(bVel / (pESRatio + 1) * pESRatio));
							bullets.get(bullets.size() - 1).setVelX((bVel / (pESRatio + 1)));
							bullet = true;
						}
					} else if(-xPESDiff < yPESDiff){
						if(xPESDiff != 0){
							if(yPESDiff > 0){
								pESRatio = yPESDiff / xPESDiff;
							} else if(yPESDiff < 0){
								pESRatio = -(yPESDiff / xPESDiff);
							}
							if(pESRatio < 0){
								pESRatio = -pESRatio;
							}
							bullets.add(new Bullet(esRealX, esRealY, this));
							bullets.get(bullets.size() - 1).setVelY(-(bVel / (pESRatio + 1) * pESRatio));
							bullets.get(bullets.size() - 1).setVelX((bVel / (pESRatio + 1)));
							bullet = true;
						}
					}
				}
				if(bulletTimer >= sVel){
					bulletTimer = 0;
				}
			}
			bulletTimer++;
		}
		
		if(enemyShooting && bullet){
			for(int i = 0; i < bullets.size(); i++){
				if(bullets.get(bulletCounter).getX() < 0 || bullets.get(bulletCounter).getY() < 0){
					bullets.remove(bulletCounter);
				}
			}
		}
		
		//Level system
		if(bigScore >= 3){
			smallScore = 0;
			bigScore = 0;
			levelTransfer = true;
			transferTimer = 0;
			nextLevel = level + 1;
			p.setVelX(0);
			p.setVelY(0);
			for(int i = 0; i < enemies.size(); i++){
				enemies.get(i).setVelX(0);
				enemies.get(i).setVelY(0);
			}
			rootedPEDiff = 100;
		}
		
		if(levelTransfer){
			transferTimer++;
		}
		
		if(transferTimer >= 180){
			levelTransfer = false;
			level++;
			if(level == 5){
				for(int i = 0; i < enemies.size(); i++){
					enemies.remove(i);
				}
				enemies.remove(0);
				shooters.add(new EnemyShooter(getWidth() - 20, getHeight() - 20, this));
			}
			if(level == 3){
				enemies.add(new Enemy(20, 20,this));
			}
			if(level == 7){
				shooters.add(new EnemyShooter(20,getHeight() - 20, this));
			}
			if(!glitchy){
				transferTimer = 0;
			}
		}
	}

	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//////////////////////////////////
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.white);
		
		g.setFont(f);
		
		if(inMenu){
			g.drawString("Play", getWidth() / 2 - 50, getHeight() / 2 - 50 - menuSeperator);
			g.drawString("Tutorial", getWidth() / 2 - 50, getHeight() / 2 - 50 + menuSeperator);
			g.drawString("Difficulty", getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 3));
			g.drawString("Exit", getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 5));
			
			if(menuChoice == 0){
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50 - menuSeperator);
			} else if(menuChoice == 1){
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50 + menuSeperator);
			} else if(menuChoice == 2){
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50 + (menuSeperator * 3));
			} else if(menuChoice == 3){
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50 + (menuSeperator * 5));
			}
		}
		
		if(inTutorial){
			g.drawString("To be added Soon", getWidth() / 2 - 50, getHeight() / 2 - 50 - menuSeperator);
			g.drawString("Press 'Enter' to return", getWidth() / 2 - 50, getHeight() / 2 - 50 + menuSeperator);
			
		}
		
		if(inDifficultyMenu){
			g.drawString("Easy", getWidth() / 2 - 50, getHeight() / 2 - 50 - menuSeperator);
			g.drawString("Normal", getWidth() / 2 - 50, getHeight() / 2 - 50 + menuSeperator);
			g.drawString("Hard", getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 3));
			
			if(menuChoice == 0){
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50 - menuSeperator);
			} else if(menuChoice == 1){
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50 + menuSeperator);
			} else if(menuChoice == 2){
				g.drawString("->", getWidth() / 2 - 75, getHeight() / 2 - 50 + (menuSeperator * 3));
			} else if(menuChoice >= 10){
				g.drawString("->", getWidth() / 2, getHeight() / 2 - 50 + menuSeperator);
			}
		}
		
		if(dead){
			g.setColor(Color.WHITE);
			g.drawString("You Died!", getWidth() / 2 - 50, getHeight() / 2 - 50 - menuSeperator);
			g.drawString("Score:    x" + bigScore + "    x" + smallScore, getWidth() / 2 - 50, getHeight() / 2 - 50 + menuSeperator);
			g.drawString("Difficulty: ", getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 3));
			
			if(difficulty == 0){
				g.setColor(Color.GREEN);
			} else if(difficulty == 1){
				g.setColor(Color.YELLOW);
			} else if(difficulty == 2){
				g.setColor(Color.RED);
			}
			
			g.drawString(difficultyString, getWidth() / 2 - 50 + g.getFontMetrics(f).stringWidth("Difficulty: "), getHeight() / 2 - 50 + (menuSeperator * 3));
			
			g.setColor(Color.WHITE);
			g.drawString("Level: " + level,getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 5));
			g.drawString("Press 'Enter' to return to menu", getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 7));
			
			g.setColor(Color.BLUE);
			g.drawOval(getWidth() / 2 - 50 + g.getFontMetrics(f).stringWidth("Score: "), getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
			g.fillOval(getWidth() / 2 - 50 + g.getFontMetrics(f).stringWidth("Score: "), getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
			
			g.setColor(Color.CYAN);
			g.drawOval(getWidth() / 2 - 50 + g.getFontMetrics(f).stringWidth("Score:    x" + bigScore + " "), getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
			g.fillOval(getWidth() / 2 - 50 + g.getFontMetrics(f).stringWidth("Score:    x" + bigScore + " "), getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
		}
		
		if(levelTransfer){
			g.setColor(Color.WHITE);
			g.drawString("You defeated level: " + level + "!", getWidth() / 2 - 50, getHeight() / 2 - 50 - menuSeperator);
			g.drawString("Now starting level: " + nextLevel, getWidth() / 2 - 50, getHeight() / 2 - 50 + menuSeperator);
		}
		
		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead && !levelTransfer){
			
			//Ingame Rendering
			g.setColor(Color.WHITE);
			g.drawOval((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
			g.fillOval((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
			
			g.setColor(Color.CYAN);
			g.drawOval((int)d.getX(), (int)d.getY(), dWIDTH, dHEIGHT);
			g.fillOval((int)d.getX(), (int)d.getY(), dWIDTH, dHEIGHT);
			
			if(blueDiamond){
				g.setColor(Color.BLUE);
				g.drawOval((int)b.getX(), (int)b.getY(), dWIDTH, dHEIGHT);
				g.fillOval((int)b.getX(), (int)b.getY(), dWIDTH, dHEIGHT);
			}
			
			if(speedElement){
				g.setColor(Color.ORANGE);
				g.drawOval((int)s.getX(), (int)s.getY(), dWIDTH, dHEIGHT);
			}
			
			g.setColor(Color.RED);
			for(int i = 0; i < enemies.size(); i++){
				g.drawOval((int)enemies.get(i).getX(), (int)enemies.get(i).getY(), pWIDTH, pHEIGHT);
				g.fillOval((int)enemies.get(i).getX(), (int)enemies.get(i).getY(), pWIDTH, pHEIGHT);
			}
			
			g.setColor(Color.ORANGE);
			for(int i = 0; i < shooters.size(); i++){
				g.drawOval((int)shooters.get(i).getX(), (int)shooters.get(i).getY(), pWIDTH, pHEIGHT);
				g.fillOval((int)shooters.get(i).getX(), (int)shooters.get(i).getY(), pWIDTH, pHEIGHT);
			}
			
			if(bullet){
				for(int i = 0; i < bullets.size(); i++){
					g.setColor(Color.WHITE);
					g.drawRect((int)bullets.get(i).getX(), (int)bullets.get(i).getY(), 1, 1);
				}
			}
			
			//Cage
			if(!enemyFree){
				g.setColor(Color.WHITE);
				g.drawRect((int)enemies.get(0).getX() - 5, (int)enemies.get(0).getY() - 3, 19, 1);
				g.drawRect((int)enemies.get(0).getX() - 5, (int)enemies.get(0).getY() + 11, 19, 1);
				g.drawRect((int)enemies.get(0).getX() - 3, (int)enemies.get(0).getY() - 3, 1, 14);
				g.drawRect((int)enemies.get(0).getX() + 10, (int)enemies.get(0).getY() - 3, 1, 14);
				g.drawRect((int)enemies.get(0).getX() + 3, (int)enemies.get(0).getY() - 3, 1, 14);
				
			}
			
			//Score Display
			g.setColor(Color.BLACK);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 11, 451);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 9, 449);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 11, 449);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 9, 451);
			g.setColor(Color.WHITE);
			g.drawString("SCORE    x" + bigScore + "    x" + smallScore, 10, 450);
			
			g.setColor(Color.CYAN);
			g.drawOval(10 + g.getFontMetrics(f).stringWidth("Score:    x" + bigScore + "   "), 441, dWIDTH, dHEIGHT);
			g.fillOval(10 + g.getFontMetrics(f).stringWidth("Score:    x" + bigScore + "   "), 441, dWIDTH, dHEIGHT);
			
			g.setColor(Color.BLUE);
			g.drawOval(10 + g.getFontMetrics(f).stringWidth("Score:   "), 441, dWIDTH, dHEIGHT);
			g.fillOval(10 + g.getFontMetrics(f).stringWidth("Score:   "), 441, dWIDTH, dHEIGHT);
		}
		
		if(paused && !levelTransfer){
			g.setColor(Color.WHITE);
			g.drawString("Paused", getWidth() / 2 - 10, getHeight() / 2 - 50 + menuSeperator);
		}
		
		//////////////////////////////////
		g.dispose();
		bs.show();
	}
	
	public void keyPressed(KeyEvent k){
		int key = k.getKeyCode();
		
		if(inMenu){
			if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
				menuChoice+=1;
				if(menuChoice > 3){
					menuChoice = 0;
				}
			}
			if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
				menuChoice-=1;
				if(menuChoice < 0){
					menuChoice = 3;
				}
			}
			if(key == KeyEvent.VK_ENTER && menuKeyReleased){
				if(menuChoice == 0){
					inMenu = false;
				} else if(menuChoice == 1){
					inTutorial = true;
					inMenu = false;
				} else if(menuChoice == 2){
					inDifficultyMenu = true;
					inMenu = false;
					menuChoice = 1;
				} else if(menuChoice == 3){
					System.exit(1);
				}
				menuKeyReleased = false;
			}
		}
		
		if(inTutorial && menuKeyReleased){
			if(key == KeyEvent.VK_ENTER){
				inTutorial = false;
				inMenu = true;
			}
		}
		
		if(inDifficultyMenu && menuKeyReleased){
			if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
				menuChoice+=1;
				if(menuChoice > 2){
					menuChoice = 0;
				}
			}
			if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
				menuChoice-=1;
				if(menuChoice < 0){
					menuChoice = 2;
				}
			}
			if(key == KeyEvent.VK_RIGHT){
				if(menuChoice < 10){
					menuChoice+=10;
				}
			}
			if(key == KeyEvent.VK_LEFT){
				if(menuChoice >= 10){
					menuChoice-= 10;
				}
			}
			
			if(key == KeyEvent.VK_ENTER){
				if(menuChoice == 0){
					difficulty = 0;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Easy";
				} else if(menuChoice == 1){
					difficulty = 1;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Normal";
				} else if(menuChoice == 2){
					difficulty = 2;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Hard";
				} else if(menuChoice >= 10){
					glitchy = true;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Glitch Mode";
				}
				System.out.println("Difficulty: " + difficulty);
			}
		}
		
		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead && !levelTransfer){
			if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
				p.setVelY(-pVel);
			} else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
				p.setVelY(pVel);
			} else if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
				p.setVelX(-pVel);
			} else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
				p.setVelX(pVel);
			}
			
			if(key == KeyEvent.VK_P){
				paused = true;
			}
			
			if(key == KeyEvent.VK_SPACE){
				bigScore = 3;
			} else if(key == KeyEvent.VK_Q){
				randomizer = 5;
			} else if(key == KeyEvent.VK_E){
				randomizer = 3;
			} else if(key == KeyEvent.VK_5){
				level += 3;
				bigScore = 3;
			}
		}
		
		if(dead){
			if(key == KeyEvent.VK_ENTER){
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
				for(int i = 0; i < enemies.size(); i++){
					enemies.remove(i);
				}
				enemies.add(new Enemy(getWidth() - 20, getHeight() - 50, this));
			}
		}
		
	}
	
	public void keyReleased(KeyEvent k){
		int key = k.getKeyCode();
		
		if(key == KeyEvent.VK_ENTER){
			menuKeyReleased = true;
		}
		
		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead){
			if(!inMenu && !inTutorial && !inDifficultyMenu){
				if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
					p.setVelY(0);
				} else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
					p.setVelY(0);
				} else if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
					p.setVelX(0);
				} else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
					p.setVelX(0);
				}
			}
			if(paused){
				if(key == KeyEvent.VK_ENTER){
					paused = false;
				}
			}
		}
	}
	
	public static void main(String args[]){
		Main game = new Main();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		
		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}

}