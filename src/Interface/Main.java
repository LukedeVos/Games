package Interface;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Main extends Canvas implements Runnable {

	public static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "Interface";
	
	private int select = 2;
	private int keys = 0;
	private int profileselect = 0;
	private boolean unpressed = true, inMenu = true, settings = false, profile = false;
	private static Main game = new Main();
	private static JFrame frame = new JFrame(game.TITLE);
	private static BufferedImageLoader loader = new BufferedImageLoader();
	
	private boolean running, inserted;
	private Thread thread;
	
	private BufferedImage inter, loaded, s0, s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16, s17, s18, s19;
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
			s7 = loader.loadImage("/res/Interface/Create_Profile.png");
			s8 = loader.loadImage("/res/Interface/Create_Profile_select.png");
			s9 = loader.loadImage("/res/Interface/Profile_1.png");
			s10 = loader.loadImage("/res/Interface/Profile_2.png");
			s11 = loader.loadImage("/res/Interface/Profile_3.png");
			s12 = loader.loadImage("/res/Interface/Profile_4.png");
			s13 = loader.loadImage("/res/Interface/Profile_5.png");
			s14 = loader.loadImage("/res/Interface/Profile_1_select.png");
			s15 = loader.loadImage("/res/Interface/Profile_2_select.png");
			s16 = loader.loadImage("/res/Interface/Profile_3_select.png");
			s17 = loader.loadImage("/res/Interface/Profile_4_select.png");
			s18 = loader.loadImage("/res/Interface/Profile_5_select.png");
			s19 = loader.loadImage("/res/Interface/CurrSelect.png");
			
			
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
		final double amountOfTicks = 30.0;
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
			if(keys == 0 && !new File("/home/luke/Desktop/Profile1").exists() && !new File("/home/luke/Desktop/Profile2").exists() && !new File("/home/luke/Desktop/Profile3").exists() && !new File("/home/luke/Desktop/Profile4").exists() && !new File("/home/luke/Desktop/Profile5").exists()){
				g.drawImage(s8, 325, 50, 382, 105, null);
			}else if(keys == 0 && new File("/home/luke/Desktop/Profile1").exists()){
				g.drawImage(s8, 125, 50, 382, 105, null);
				g.drawImage(s9, 125, 150, 382, 105, null);
			}else if(keys == 0 && new File ("home/luke/Desktop/Profile2").exists()){
				g.drawImage(s8, 125, 50, 382, 105, null);
				g.drawImage(s9, 125, 150, 382, 105, null);
				g.drawImage(s10, 125, 350, 382, 105, null);
			}else if(keys == 0 && new File ("home/luke/Desktop/Profile3").exists()){
				g.drawImage(s8, 125, 50, 382, 105, null);
				g.drawImage(s9, 125, 150, 382, 105, null);
				g.drawImage(s10, 125, 350, 382, 105, null);
				g.drawImage(s11, 125, 500, 382, 105, null);
			}else if(keys == 0 && new File ("home/luke/Desktop/Profile4").exists()){
				g.drawImage(s8, 125, 50, 382, 105, null);
				g.drawImage(s9, 125, 150, 382, 105, null);
				g.drawImage(s10, 125, 350, 382, 105, null);
				g.drawImage(s11, 125, 500, 382, 105, null);
				g.drawImage(s12, 125, 650, 382, 105, null);
			}else if(keys == 0 && new File ("home/luke/Desktop/Profile5").exists()){
				g.drawImage(s8, 125, 50, 382, 105, null);
				g.drawImage(s9, 125, 150, 382, 105, null);
				g.drawImage(s10, 125, 350, 382, 105, null);
				g.drawImage(s11, 125, 500, 382, 105, null);
				g.drawImage(s12, 125, 650, 382, 105, null);
				g.drawImage(s13, 125, 800, 382, 105, null);
			}else if(keys == 1){
				g.drawImage(s7, 325, 50, 382, 105, null);
				g.drawImage(s14, 325, 200,382,105, null);
				g.drawImage(s10, 325, 350, 382, 105, null);
				g.drawImage(s11, 325, 500, 382, 105, null);
				g.drawImage(s12, 325, 650, 382, 105, null);
				g.drawImage(s13, 325, 800, 382, 105, null);
			}else if(keys == 2){
				g.drawImage(s7, 25, 50, 382, 105, null);
				g.drawImage(s9, 25, 200,382,105, null);
				g.drawImage(s15, 25, 350, 382, 105, null);
				g.drawImage(s11, 25, 500, 382, 105, null);
				g.drawImage(s12, 25, 650, 382, 105, null);
				g.drawImage(s13, 25, 800, 382, 105, null);
			}else if(keys == 3){
				g.drawImage(s7, 25, 50, 382, 105, null);
				g.drawImage(s9, 25, 200,382,105, null);
				g.drawImage(s10, 25, 350, 382, 105, null);
				g.drawImage(s16, 25, 500, 382, 105, null);
				g.drawImage(s12, 25, 650, 382, 105, null);
				g.drawImage(s13, 25, 800, 382, 105, null);
			}else if(keys == 4){
				g.drawImage(s7, 25, 50, 382, 105, null);
				g.drawImage(s9, 25, 200,382,105, null);
				g.drawImage(s10, 25, 350, 382, 105, null);
				g.drawImage(s11, 25, 500, 382, 105, null);
				g.drawImage(s17, 25, 650, 382, 105, null);
				g.drawImage(s13, 25, 800, 382, 105, null);
			}else if(keys == 5){
				g.drawImage(s7, 25, 50, 382, 105, null);
				g.drawImage(s9, 25, 200,382,105, null);
				g.drawImage(s10, 25, 350, 382, 105, null);
				g.drawImage(s11, 25, 500, 382, 105, null);
				g.drawImage(s12, 25, 650, 382, 105, null);
				g.drawImage(s18, 25, 800, 382, 105, null);
			}
			if(profileselect == 1){
				g.drawImage(s19, 1225, 200, 382, 105, null);
			}else if(profileselect == 2){
				g.drawImage(s19, 1225, 350, 382, 105, null);
			}else if(profileselect == 3){
				g.drawImage(s19, 1225, 500, 382, 105, null);
			}else if(profileselect == 4){
				g.drawImage(s19, 1225, 650, 382, 105, null);
			}else if(profileselect == 5){
				g.drawImage(s19, 1225, 800, 382, 105, null);
			}			
							
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
				if(inMenu && !profile && !settings){
				if(select == 1 || select == 2 || select == 3){
					select = 0;
					unpressed = false;
				}else if(select == 4){
					select = 2;
					unpressed = false;
				}
			}else if(profile && !inMenu && !settings){
				if(keys >= 0){
				keys --;
				}else if(keys >= 0 && !new File("/home/luke/Desktop/Profile1").exists()){
					keys = 0;
				}else if(keys >= 0 && !new File("/home/luke/Desktop/Profile2").exists()){
					keys = 1;
				}else if(keys >= 0 && !new File("/home/luke/Desktop/Profile3").exists()){
					keys = 2;
				}else if(keys >= 0 && !new File("/home/luke/Desktop/Profile4").exists()){
					keys = 3;
				}else if(keys >= 0 && !new File("/home/luke/Desktop/Profile5").exists()){
					keys = 4;				
				}else{
					keys = 5;
				}
			}
			} else if(key == KeyEvent.VK_A){
				
				if(inMenu && !profile && !settings){
					if(select == 0 || select == 2 || select == 4){
						select = 1;
						unpressed = false;
					} else if(select == 3){
						select = 2;
						unpressed = false;
					}
				}else if(profile && !inMenu && !settings){
					
				}
			} else if(key == KeyEvent.VK_S){
				if(inMenu = true && !profile && !settings){	
					if(select == 1 || select == 2 || select == 3){
						select = 4;
						unpressed = false;
					} else if(select == 0){
						select = 2;
						unpressed = false;
					}
				}else if(profile && !inMenu && !settings){
					if(keys <= 5 && new File("/home/luke/Desktop/Profile1").exists() && new File("/home/luke/Desktop/Profile2").exists() && new File("/home/luke/Desktop/Profile3").exists() && new File("/home/luke/Desktop/Profile4").exists() && new File("/home/luke/Desktop/Profile5").exists()){
					keys ++;
					}else if(keys <= 5 && !new File("/home/luke/Desktop/Profile1").exists()){
						keys = 0;
					}else if(keys <= 5 && !new File("/home/luke/Desktop/Profile2").exists()){
						keys = 1;
					}else if(keys <= 5 && !new File("/home/luke/Desktop/Profile3").exists()){
						keys = 2;
					}else if(keys <= 5 && !new File("/home/luke/Desktop/Profile4").exists()){
						keys = 3;
					}else if(keys <= 5 && !new File("/home/luke/Desktop/Profile5").exists()){
						keys = 4;				
					}else{
						keys = 0;
					}
				}
			} else if(key == KeyEvent.VK_D){
				
			if(inMenu = true && !profile && !settings){	
					if(select == 0 || select == 2 || select == 4){
						select = 3;
						unpressed = false;
					} else if(select == 1){
						select = 2;
						unpressed = false;
					}
			}else if(profile && !inMenu && !settings){
				
			}
			}
		}
		
		if(key == KeyEvent.VK_ENTER && unpressed){
			if(inMenu && !profile && !settings){
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
			    			inserted = !inserted;
						} catch (IOException e1){
							e1.printStackTrace();
						}
			    	} else {
				    	try{
				    		Runtime rt = Runtime.getRuntime();
				    		System.out.println(rt.exec("mcopy ../../../media/luke/LUKESTICK/Image.png"));
				    		File sourceimage = new File("Image.png");
				    		image = ImageIO.read(sourceimage);
				    		inserted = !inserted;
				    	} catch(IOException e2){
				    		e2.printStackTrace();
				    	}
			    	}
				} else {
					inserted = !inserted;
				}	
			} else if(select == 2){
				//run game
				String drive = (new DetectDrive()).USBDetect();
		    	if(drive != null && !drive.isEmpty()){
		    		FileSearch fileSearch = new FileSearch();
		    		String jarPath = fileSearch.find(new File(drive), ".jar");
		    		System.out.println(jarPath);
		    		Runtime rt = Runtime.getRuntime();
					try {
						rt.exec("java -jar " + jarPath);
						System.out.println("java -jar " + jarPath);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		    	} else {
		    		Runtime rt = Runtime.getRuntime();
		    		try {
						System.out.println(rt.exec("java -jar ../../../media/luke/LUKESTICK/game.jar"));
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
		}else if(profile && !inMenu && !settings && keys == 0){
				File file1 = new File("/home/luke/Desktop/Profile1");
				file1.mkdir();
			 if(new File("/home/luke/Desktop/Profile1").exists()){
				File file2 = new File("/home/luke/Desktop/Profile2");
				file2.mkdir();
			}else if(new File("/home/luke/Desktop/Profile2").exists()){
				File file3 = new File("/home/luke/Desktop/Profile3");
				file3.mkdir();
			}else if(new File("/home/luke/Desktop/Profile3").exists()){
				File file4 = new File("/home/luke/Desktop/Profile4");
				file4.mkdir();
			}else if(new File("/home/luke/Desktop/Profile4").exists()){
				File file5 = new File("/home/luke/Desktop/Profile5");
				file5.mkdir();
			}
		}else if(profile && !inMenu && !settings && keys != 0){
			if(keys == 1 && new File("/home/luke/Desktop/Profile1").exists()){
				profileselect = 1;
			}else if(keys == 2 && new File("/home/luke/Desktop/Profile2").exists()){
				profileselect = 2;
			}else if(keys == 3 && new File("/home/luke/Desktop/Profile3").exists()){
				profileselect =3;
			}else if(keys == 4 && new File("/home/luke/Desktop/Profile4").exists()){
				profileselect = 4;
			}else if(keys == 5 && new File("/home/luke/Desktop/Profile5").exists()){
				profileselect = 5;
			}
		}
	}

	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_A || key == KeyEvent.VK_S || key == KeyEvent.VK_D || key == KeyEvent.VK_ENTER){
			unpressed = true;
		}
		
	}
	public void selection() {
		PrintWriter writer;
		if(profileselect == 1){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Prof1select", "UTF-8");
				writer.close();
				System.out.println("Selected");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof2select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof3select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof4select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
	
				File file = new File("/home/luke/Desktop/Prof5select", "UTF-8");
				file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(profileselect == 2){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Prof2select", "UTF-8");
				writer.close();
				System.out.println("Selected");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
try {
				
				File file = new File("/home/luke/Desktop/Prof1select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof3select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof4select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
	
				File file = new File("/home/luke/Desktop/Prof5select", "UTF-8");
				file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(profileselect == 3){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Prof3select", "UTF-8");
				writer.close();
				System.out.println("Selected");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
try {
				
				File file = new File("/home/luke/Desktop/Prof2select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof1select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof4select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
	
				File file = new File("/home/luke/Desktop/Prof5select", "UTF-8");
				file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(profileselect == 4){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Prof4select", "UTF-8");
				writer.close();
				System.out.println("Selected");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof2select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof3select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof1select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
	
				File file = new File("/home/luke/Desktop/Prof5select", "UTF-8");
				file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if(profileselect == 5){
			try {
				writer = new PrintWriter("/home/luke/Desktop/Prof5select", "UTF-8");
				writer.close();
				System.out.println("Selected");
			} catch(FileNotFoundException e) {
				e.printStackTrace();
			} catch(UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof2select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof3select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				
				File file = new File("/home/luke/Desktop/Prof4select", "UTF-8");
				 file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
	
				File file = new File("/home/luke/Desktop/Prof1select", "UTF-8");
				file.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
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
