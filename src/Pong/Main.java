package Pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Main extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH  = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Pong";
	
	private boolean running = false;
	private Thread thread;
	
	public final int pWIDTH = 10;
	public final int pHEIGHT = 91;
	
	public final int bWIDTH = 7;
	public final int bHEIGHT = 7;
	
	public int pScore = 0;
	public int eScore = 0;
	
	public double ballHit;
	private int verticalAccelerator = 20;
	private double horizontalAccelerator = 0.75;
	
	private double maxYVel = 5;
	private double maxXVel = 30;
	private double enemyVel = 2.5;
	
	private int menuChoice = 0;
	
	private boolean inMenu = true;
	private boolean twoPlayer;
	public boolean paused = false;
	
	private Player p;
	private Ball b;
	private Enemy e;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	private void init(){
		requestFocus();
		addKeyListener(new KeyInput(this));

		while(inMenu){
			render();
		}
		
		if(menuChoice == 0){
			twoPlayer = false;
		} else if(menuChoice == 1){
			twoPlayer = true;
		}
		
		
			p = new Player(10, (getHeight() / 2) - 45, this);
			e = new Enemy(getWidth() - 20, (getHeight() / 2) - 45, this);
			b = new Ball((getWidth() / 2) - 3, (getHeight() / 2) - 3, this);
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
	
	public void run(){
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
		p.tick();
		b.tick();
		e.tick();
		
		
		//Player collision detection
		if((b.getBallX() <= p.getX() + pWIDTH) && !(b.getBallX() <= 0)){
			if(!(b.getBallY() + bHEIGHT < p.getY()) && !(b.getBallY() > p.getY() + pHEIGHT)){
				ballHit = (b.getBallY() + 4) - (p.getY() + (pHEIGHT / 2) + 0.5);
				if(ballHit == 0){
					b.setBallVelX(-(b.getBallVelX() - horizontalAccelerator));
				} else if(ballHit > 0){
					if(b.getBallVelY() < 0){
						b.setBallVelY(-b.getBallVelY());
					}
					if(b.getBallVelY() < maxYVel){
						b.setBallVelY(b.getBallVelY() + (ballHit / verticalAccelerator));
					}
					if(b.getBallVelX() > -maxXVel){
						b.setBallVelX(-(b.getBallVelX() - horizontalAccelerator));
					} else {
						b.setBallVelX(-b.getBallVelX());
					}
				} else if(ballHit < 0){
					if(b.getBallVelY() > 0){
						b.setBallVelY(-b.getBallVelY());
					}
					if(b.getBallVelY() > -maxYVel){
						b.setBallVelY(b.getBallVelY() + (ballHit / verticalAccelerator));
					}
					if(b.getBallVelX() > -maxXVel){
						b.setBallVelX(-(b.getBallVelX() - horizontalAccelerator));
					} else {
						b.setBallVelX(-b.getBallVelX());
					}
				}
			}
		}
	
		//Enemy Collision Detection
		if((b.getBallX() + bWIDTH >= e.getEX()) && !(b.getBallX() + bWIDTH >= getWidth())){
			if(!(b.getBallY() + bHEIGHT < e.getEY()) && !(b.getBallY() > e.getEY() + pHEIGHT)){
				ballHit = (b.getBallY() + 4) - (e.getEY() + (pHEIGHT / 2) + 0.5);
				if(ballHit == 0){
					b.setBallVelX(-(b.getBallVelX() + horizontalAccelerator));
				} else if(ballHit > 0){
					if(b.getBallVelY() < 0){
						b.setBallVelY(-b.getBallVelY());
					}
					if(b.getBallVelY() < maxYVel){
						b.setBallVelY(b.getBallVelY() + (ballHit / verticalAccelerator));
					}
					if(b.getBallVelX() < maxXVel){
						b.setBallVelX(-(b.getBallVelX() + horizontalAccelerator));
					} else {
						b.setBallVelX(-b.getBallVelX());
					}
				} else if(ballHit < 0){
					if(b.getBallVelY() > 0){
						b.setBallVelY(-b.getBallVelY());
					}
					if(b.getBallVelY() < maxYVel){
						b.setBallVelY(b.getBallVelY() + (ballHit / verticalAccelerator));
					}
					if(b.getBallVelX() < maxXVel){
						b.setBallVelX(-(b.getBallVelX() + horizontalAccelerator));
					} else {
						b.setBallVelX(-b.getBallVelX());
					}
				}
			}
		}
	
		//Enemy Movement
		if(!(twoPlayer)){
			if(b.getBallY() + 4 == e.getEY() + 46){
				e.setEVelY(0);
			} else if(b.getBallY() + 4 > e.getEY() + 46){
				e.setEVelY(enemyVel);
			} else if(b.getBallY() + 4 < e.getEY() + 46){
				e.setEVelY(-enemyVel);
			}
		}
		
		//Enemy AI
		if(enemyVel < 4.5){
			enemyVel = 2.5 + 0.5 * pScore;
		}
		
		//Score count
		if(b.getBallX() <= 0){
			eScore+=1;
			b.setBallX(getWidth() / 2);
			b.setBallY(getHeight() / 2);
			b.setBallVelX(-2.5);
			b.setBallVelY(0);
		} else if(b.getBallX() >= getWidth()){
			pScore+=1;
			b.setBallX(getWidth() / 2);
			b.setBallY(getHeight() / 2);
			b.setBallVelX(2.5);
			b.setBallVelY(0);
		}
	
		//Ball bounce
		if(b.getBallY() <= 0){
			b.setBallY(0);
			b.setBallVelY(-b.getBallVelY());
		} else if(b.getBallY() + bHEIGHT >= getHeight()){
			b.setBallY(getHeight() - bHEIGHT);
			b.setBallVelY(-b.getBallVelY());
		}
	
		
	}
	
	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		//////////////////////////////////
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.white);
		
		if(paused){
			g.drawString("PAUSED", getWidth() / 2 - 10, getHeight() / 2);
		}
		
		if(inMenu){
			g.drawString("SinglePlayer", getWidth() / 2 - 20, getHeight() / 2 - 15);
			g.drawString("MultiPlayer", getWidth() / 2 - 20, getHeight() / 2 + 15);
			
			if(menuChoice == 0){
				g.drawString("->", getWidth() / 2 - 45, getHeight() / 2 - 15);
			} else if(menuChoice == 1){
				g.drawString("->", getWidth() / 2 - 45, getHeight() / 2 + 15);
			}
		}
		
		if(!inMenu){
			//Rendering Player
			g.drawRect((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
			g.fillRect((int)p.getX(), (int)p.getY(), pWIDTH, pHEIGHT);
		
			//Rendering Enemy
			g.drawRect((int)e.getEX(), (int)e.getEY(), pWIDTH, pHEIGHT);
			g.fillRect((int)e.getEX(), (int)e.getEY(), pWIDTH, pHEIGHT);
		
			//Rendering Ball
			g.drawRect((int)b.getBallX(), (int)b.getBallY(), bWIDTH, bHEIGHT);
			g.fillRect((int)b.getBallX(), (int)b.getBallY(), bWIDTH, bHEIGHT);
		
			//Score Display
			g.setFont(new Font("default", Font.BOLD, 16));
			String s = pScore + "   |   " + eScore;
			g.drawString(s, (getWidth() / 2) - (s.length() * g.getFont().getSize()) / 4, 20);
		}
		//////////////////////////////////
		g.dispose();
		bs.show();
	}
	
	public void pause(){
		while(paused){
			render();
		}
	}
	
	public void keyPressed(KeyEvent k){
		if(!inMenu && !paused){
			int key = k.getKeyCode();
		
			if(!twoPlayer){
				if(key == KeyEvent.VK_UP){
					p.setVelY(-4);
				} else if(key == KeyEvent.VK_DOWN){
					p.setVelY(4);
				}
			}
		
			if(twoPlayer){
				if(key == KeyEvent.VK_W){
					p.setVelY(-4);
				} else if(key == KeyEvent.VK_S){
					p.setVelY(4);
				}
				if(key == KeyEvent.VK_UP){
					e.setEVelY(-4);
				} else if(key == KeyEvent.VK_DOWN){
					e.setEVelY(4);
				}
			}
			if(key == KeyEvent.VK_P){
				paused = true;
				//running = false;
			}
		} if(inMenu){
			int key = k.getKeyCode();
		
			if(key == KeyEvent.VK_DOWN){
				menuChoice+=1;
				if(menuChoice > 1){
					menuChoice = 0;
				}
			} else if(key == KeyEvent.VK_UP){
				menuChoice-=1;
				if(menuChoice < 0){
					menuChoice = 1;
				}
			} else if(key == KeyEvent.VK_ENTER){
				inMenu = false;
			}
		}
		if(paused){
			int key = k.getKeyCode();
			
			if(key == KeyEvent.VK_ENTER){
				paused = false;
			}
		}
	}
	
	public void keyReleased(KeyEvent k){
		if(!inMenu){
			int key = k.getKeyCode();
		
			if(!twoPlayer){
				if(key == KeyEvent.VK_UP){
					p.setVelY(0);
				} else if(key == KeyEvent.VK_DOWN){
					p.setVelY(0);
				}
			}
		
			if(twoPlayer){
				if(key == KeyEvent.VK_W){
					p.setVelY(0);
				} else if(key == KeyEvent.VK_S){
					p.setVelY(0);
				}
				if(key == KeyEvent.VK_UP){
					e.setEVelY(0);
				} else if(key == KeyEvent.VK_DOWN){
					e.setEVelY(0);
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