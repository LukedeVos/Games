package RPG;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	private BufferedImage image;
	private int size = 20;
		
	public SpriteSheet(BufferedImage image){
		this.image = image;
	}
	
	public BufferedImage grabImage(int col, int row, int width, int height){
		
		BufferedImage img = image.getSubimage((col * size), (row * size), width, height);
		
		return img;
	}
	
}
