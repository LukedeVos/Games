package Interface;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

public class Main extends Canvas implements Runnable {

	public static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Interface";
	
	private int inMenu = 1;
	private int settings = 0;
	private int profile = 0;
	private int select = 2;
	private boolean unpressed = true;
	private static Main game = new Main();
	private static JFrame frame = new JFrame(game.TITLE);
	private static BufferedImageLoader loader = new BufferedImageLoader();
	
	private boolean running, inserted;
	private Thread thread;
	
	private BufferedImage inter, loaded, image, s0, s1, s2, s3, s4, s5, s6;

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
	
	private String checkUSB(){
		System.out.println("File system roots returned by   FileSystemView.getFileSystemView():");
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File[] roots = fsv.getRoots();
		String driveLetter = "";
		for (int i = 0; i < roots.length; i++){
			System.out.println("Root: " + roots[i]);
		}
		System.out.println("Home directory: " + fsv.getHomeDirectory());
		System.out.println("File system roots returned by File.listRoots():");
		
		File[] f = File.listRoots();
		for (int i = 0; i < f.length; i++){
			String drive = f[i].getPath();
			String displayName = fsv.getSystemDisplayName(f[i]);
			String type = fsv.getSystemTypeDescription(f[i]);
			boolean isDrive = fsv.isDrive(f[i]);
			boolean isFloppy = fsv.isFloppyDrive(f[i]);
			boolean canRead = f[i].canRead();
			boolean canWrite = f[i].canWrite();
			
			System.out.println(type.toLowerCase());
			
			if (canRead && canWrite && !isFloppy && isDrive && (type.toLowerCase().contains("verwisselbare"))){
				System.out.println("Detected PEN Drive: " + drive + " - "+ displayName); 
				driveLetter = drive;
				break;
			}
		}
		return driveLetter;
	}
	
	public String findUSB(File dir){
		String pattern = ".png";
		
		File listFile[] = dir.listFiles();
		if (listFile != null){
			for (int i = 0; i < listFile.length; i++){
				System.out.println(listFile[i]);
				System.out.println("lol");
				if (listFile[i].isDirectory()){
					findUSB(listFile[i]);
				} else { 
					if (listFile[i].getName().endsWith(pattern)){
						System.out.println(listFile[i].getPath());
					}
				}
			}
		}
		return pattern;
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
			g.drawImage(loaded, 0, 0, getWidth(), getHeight(), null);
			g.drawImage(image, 20, 20, getWidth(), getHeight(), null);
		}
		
		if(inMenu == 1 && profile == 0 && settings == 0){
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
		}else if(inMenu == 0 && profile == 1 && settings == 0){
			g.drawImage(s5, 0, 0, getWidth(), getHeight(), null);
		}else if(inMenu == 0 && profile == 0 && settings == 1){
			g.drawImage(s6, 0, 0, getWidth(), getHeight(), null);
		}
		//////////////////////////////////
		g.dispose();
		bs.show();
	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		if(profile == 1 || settings == 1){
			if(key == KeyEvent.VK_ESCAPE){
				profile = 0;
				settings = 0;
				inMenu = 1;
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
				inMenu = 0;
				profile = 1;
				settings = 0;
				
			} else if(select == 1){
				if(!inserted){
					String drive = checkUSB();
					if(drive != null && !drive.isEmpty()) {
						System.out.println(findUSB(new File(drive + ":")));
//						try {
//							image = loader.loadImage(findUSB(new File("Image.png")));
//							inserted = true;
//						} catch(IOException e2) {
//							e2.printStackTrace();
//						}
					}
				}
			} else if(select == 2){
				
			} else if(select == 3){
				frame.setVisible(false);
				System.exit(0);
				frame.dispose();
			} else if(select == 4){
				inMenu = 0;
				profile = 0;
				settings = 1;
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
		if(path == "normal"){
			return inter;
		} else if(path == "loaded"){
			return loaded;
		} else {
			return null;
		}
	}

}
