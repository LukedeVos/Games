package Bopper;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

import Bopper.Player;
import Bopper.KeyInput;
import Bopper.Enemy;

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
	private Enemy e;
	private BlueDiamond b;
	
	private boolean inMenu = true;
	private boolean inTutorial = false;
	private boolean inDifficultyMenu = false;
	private boolean menuKeyReleased = true;
	private boolean dead = false;
	private boolean enemyFree = false;
	private boolean enemyMoved = false;
	private boolean blueDiamond;
	
	private int menuSeperator = 15;
	private int menuChoice = 0;
	private int difficulty = 1;
	private int smallScore = 0;
	private int bigScore = 0;
	private int pVel = 2;
	private int eVel = 0;
	private int level = 1;
	private double pERatio;
	private double randomizer; 
	
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
	
	private String difficultyString = "Normal";
	
	Random rand = new Random();
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	public void init(){
		requestFocus();
		addKeyListener(new KeyInput(this));
		
		
		int  diaX = rand.nextInt(getWidth() - 40) + 40;
		int  diaY = rand.nextInt(getHeight() - 40) + 40;
		
		p = new Player(10, 10, this);
		d = new Diamond(diaX, diaY, this);
		b = new BlueDiamond(-20, -20, this);
		e = new Enemy(getWidth() - 20, getHeight() - 50, this);
		
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
				tick();
				updates++;
				delta--; 
			}
			
			pRealX = (int)p.getX() + (pWIDTH / 2);
			pRealY = (int)p.getY() + (pHEIGHT / 2);
			eRealX = (int)e.getX() + (pWIDTH / 2);
			eRealY = (int)e.getY() + (pHEIGHT / 2);
			dRealX = (int)d.getX() + (dWIDTH / 2);
			dRealY = (int)d.getY() + (dHEIGHT / 2);
			bRealX = (int)b.getX() + (dWIDTH / 2);
			bRealY = (int)b.getY() + (dHEIGHT / 2);
					
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
		p.tick();
		e.tick();
		
		//Enemy to Player difference
		xPEDiff = pRealX - eRealX;
		yPEDiff = pRealY - eRealY;
		
		//Diamond to Player difference
		xPDDiff = pRealX - dRealX;
		yPDDiff = pRealY - dRealY;
		
		//BlueDiamond to Player difference
		xPBDiff = pRealX - bRealX;
		yPBDiff = pRealY - bRealY;
		
		//Distance Player to Enemy
		pEDiff = (xPEDiff * xPEDiff) + (yPEDiff * yPEDiff);
		rootedPEDiff = Math.sqrt(pEDiff);
		if(rootedPEDiff <= (pWIDTH / 2) + (pWIDTH / 2)){
			dead = true;
			p.setX(10);
			p.setY(10);
			p.setVelX(0);
			p.setVelY(0);
			e.setX(getWidth() - 20);
			e.setY(getHeight() - 50);
			e.setVelX(0);
			e.setVelY(0);
			eVel = 0;
			rootedPEDiff = 100;
			enemyFree = false;
		}
		
		//Distance Player to Diamond
		pDDiff = (xPDDiff * xPDDiff) + (yPDDiff * yPDDiff);
		rootedPDDiff = Math.sqrt(pDDiff);
		if(rootedPDDiff <= (dWIDTH / 2) + (pWIDTH / 2)){
			smallScore += 1;
			d.setX(rand.nextInt(getWidth() - 40) + 40);
			d.setY(rand.nextInt(getHeight() - 40) + 40);
			randomizer = rand.nextInt(5) + 1;
			System.out.println(randomizer);
		}
		
		//Blue Gem detection
		if(randomizer == 3){
			blueDiamond = true;
			b.setX(rand.nextInt(getWidth() - 40) + 40);
			b.setY(rand.nextInt(getHeight() - 40) + 40);
			randomizer = 1;
		}
		
		//Distance Player to BlueDiamond
		pBDiff = (xPBDiff * xPBDiff) + (yPBDiff * yPBDiff);
		rootedPBDiff = Math.sqrt(pBDiff);
		if(rootedPDDiff <= (dWIDTH / 2) + (pWIDTH / 2)){
			bigScore += 1;
			blueDiamond = false;
			System.out.println(randomizer);
		}
		
		//Score count
		if(smallScore >= 10){
			smallScore -= 10;
			bigScore += 1;
		}
		
		
		//Enemy AI
		if(smallScore >= 3 || bigScore >= 1 || rootedPEDiff <= 30){
			enemyFree = true;
		}
		if(enemyFree){
			eVel = 2 + (level - 1) + (difficulty - 1);
		}
		
		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead && enemyFree){
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
					e.setVelX(eVel / (pERatio + 1) * pERatio);
					e.setVelY(eVel / (pERatio + 1));
					if(xPEDiff < 0){
						e.setVelX(-(e.getVelX()));
					}
					if(yPEDiff < 0){
						e.setVelY(-(e.getVelY()));
					}
				} else {
					e.setVelX(eVel);
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
					e.setVelX(eVel / (pERatio + 1));
					e.setVelY(eVel / (pERatio + 1) * pERatio);
					if(xPEDiff < 0){
						e.setVelX(-(e.getVelX()));
					}
					if(yPEDiff < 0){
						e.setVelY(-(e.getVelY()));
					}
				} else {
					e.setVelY(eVel);
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
					e.setVelX(eVel / (pERatio + 1) * pERatio);
					e.setVelY(eVel / (pERatio + 1));
					if(xPEDiff < 0){
						e.setVelX(-(e.getVelX()));
					}
					if(yPEDiff < 0){
						e.setVelY(-(e.getVelY()));
					}
				} else {
					e.setVelX(eVel);
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
					e.setVelX(eVel / (pERatio + 1));
					e.setVelY(eVel / (pERatio + 1) * pERatio);
					if(xPEDiff < 0){
						e.setVelX(-(e.getVelX()));
					}
					if(yPEDiff < 0){
						e.setVelY(-(e.getVelY()));
					}
				} else {
					e.setVelY(eVel);
				}
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
			
			g.drawString(difficultyString, getWidth() / 2, getHeight() / 2 - 50 + (menuSeperator * 3));
			
			g.setColor(Color.WHITE);
			g.drawString("Level: " + level,getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 5));
			g.drawString("Press 'Enter' to return to menu", getWidth() / 2 - 50, getHeight() / 2 - 50 + (menuSeperator * 7));
			
			g.setColor(Color.BLUE);
			g.drawOval(getWidth() / 2 - 12, getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
			g.fillOval(getWidth() / 2 - 12, getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
			
			g.setColor(Color.CYAN);
			g.drawOval(getWidth() / 2 + 12, getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
			g.fillOval(getWidth() / 2 + 12, getHeight() / 2 - 59 + menuSeperator, dWIDTH, dHEIGHT);
		}
		
		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead){
			
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
			
			g.setColor(Color.RED);
			g.drawOval((int)e.getX(), (int)e.getY(), pWIDTH, pHEIGHT);
			g.fillOval((int)e.getX(), (int)e.getY(), pWIDTH, pHEIGHT);
			
			//Cage
			if(!enemyFree){
				g.setColor(Color.WHITE);
				g.drawRect((int)e.getX() - 5, (int)e.getY() - 3, 19, 1);
				g.drawRect((int)e.getX() - 5, (int)e.getY() + 11, 19, 1);
				g.drawRect((int)e.getX() - 3, (int)e.getY() - 3, 1, 14);
				g.drawRect((int)e.getX() + 10, (int)e.getY() - 3, 1, 14);
				g.drawRect((int)e.getX() + 3, (int)e.getY() - 3, 1, 14);
				
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
			g.drawOval(79, 441, dWIDTH, dHEIGHT);
			g.fillOval(79, 441, dWIDTH, dHEIGHT);
			
			g.setColor(Color.BLUE);
			g.drawOval(55, 441, dWIDTH, dHEIGHT);
			g.fillOval(55, 441, dWIDTH, dHEIGHT);
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
					menuChoice = 3;
				}
			}
			
			if(key == KeyEvent.VK_ENTER){
				if(menuChoice == 0){
					difficulty = 0;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Easy";
					System.out.println(difficulty);
				} else if(menuChoice == 1){
					difficulty = 1;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Normal";
					System.out.println(difficulty);
				} else if(menuChoice == 2){
					difficulty = 2;
					inDifficultyMenu = false;
					inMenu = true;
					menuChoice = 2;
					difficultyString = "Hard";
					System.out.println(difficulty);
				}
			}
		}
		
		if(!inMenu && !inTutorial && !inDifficultyMenu && !dead){
			if(key == KeyEvent.VK_UP || key == KeyEvent.VK_W){
				p.setVelY(-pVel);
			} else if(key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S){
				p.setVelY(pVel);
			} else if(key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A){
				p.setVelX(-pVel);
			} else if(key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D){
				p.setVelX(pVel);
			}
		}
		
		if(dead){
			if(key == KeyEvent.VK_ENTER){
				inMenu = true;
				dead = false;
				menuKeyReleased = false;
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