package Interface;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

public class Main extends Canvas implements Runnable {

	public static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Interface";
	
	private int select = 2;
	private boolean unpressed = true, inMenu = true, settings, profile;
	private static Main game = new Main();
	private static JFrame frame = new JFrame(game.TITLE);
	private static BufferedImageLoader loader = new BufferedImageLoader();
	
	private boolean running, inserted;
	private Thread thread;
	
	private BufferedImage inter, loaded, s0, s1, s2, s3, s4, s5, s6;
	private Image image;

	public void init(){
		requestFocus();
		try{
			inter = loader.loadImage("/res/Interface/Interface_Normal.png");
			loaded = loader.loadImage("/res/Interface/Interface_Loaded.png");
			s0 = loader.loadImage("/res/Interface/Interface_0.png");
			s1 = loader.loadImage("/res/Interface/Interface_1.png");
			s2 = loader.loadImage("/res/Interface/Interface_2.png");
			s3 = loader.loadImage("/res/Interface/Interface_3.png");
			s4 = loader.loadImage("/res/Interface/Interface_4.png");
			s5 = loader.loadImage("/res/Interface/Interface_5.png");
			s6 = loader.loadImage("/res/Interface/Interface_6.png");
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
		if(!inserted){
			g.drawImage(inter, 0, 0, getWidth(), getHeight(), null);
		} else {
			g.drawImage(image, 20, 20, getWidth(), getHeight(), null);
			g.drawImage(loaded, 0, 0, getWidth(), getHeight(), null);
		}
		
		if(inMenu && !profile && !settings){
			if(select == 0){
				g.drawImage(s0, 0, 0, getWidth(), getHeight(), null);
			} else if(select == 1){
				g.drawImage(s1, 0, 0, getWidth(), getHeight(), null);
			} else if(select == 2){
				g.drawImage(s2, 0, 0, getWidth(), getHeight(), null);
			} else if(select == 3){
				g.drawImage(s3, 0, 0, getWidth(), getHeight(), null);
			} else if(select == 4){
				g.drawImage(s4, 0, 0, getWidth(), getHeight(), null);
			}
		}else if(!inMenu && profile && !settings){
			g.drawImage(s5, 0, 0, getWidth(), getHeight(), null);
		}else if(!inMenu && !profile && settings){
			g.drawImage(s6, 0, 0, getWidth(), getHeight(), null);
		}
		//////////////////////////////////
		g.dispose();
		bs.show();
	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if(profile || settings){
			if(key == KeyEvent.VK_ESCAPE){
				profile = false;
				settings = false;
				inMenu = true;
			}
		//simpel nu ff failsafe ofc ga ik verbeteren
		}
		
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
		
		if(key == KeyEvent.VK_ENTER && unpressed){
			if(select == 0){
				//profiles
				inMenu = false;
				profile = true;
				settings = false;
				
			} else if(select == 1){
				//load/unload game
				if(!inserted){
					String drive = (new DetectDrive()).USBDetect();
			    	if(drive != null && !drive.isEmpty()){
			    		FileSearch fileSearch = new FileSearch();
			    		String imagePath = fileSearch.find(new File(drive), ".png");
			    		System.out.println(imagePath);
			    		try{
			    			File sourceimage = new File(imagePath);
			    			image = ImageIO.read(sourceimage);
						} catch (IOException e1){
							e1.printStackTrace();
						}
			    	}
				}
				inserted = !inserted;
			} else if(select == 2){
				//run game
				String drive = (new DetectDrive()).USBDetect();
		    	if(drive != null && !drive.isEmpty()){
		    		FileSearch fileSearch = new FileSearch();
		    		String jarPath = fileSearch.find(new File(drive), ".jar");
		    		System.out.println(jarPath);
		    		Runtime rt = Runtime.getRuntime();
					try {
						Process pr = rt.exec("java -jar " + jarPath);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		    	}
			} else if(select == 3){
				//turn off console
				frame.setVisible(false);
				System.exit(0);
				frame.dispose();
			} else if(select == 4){
				//settings
				inMenu = false;
				profile = false;
				settings = true;
			}
			unpressed = false;
		}
		
	}

	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D || key == KeyEvent.VK_ENTER){
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
