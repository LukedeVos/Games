package RPG;

import java.awt.image.BufferedImage;

public class SpriteSheet {
	
	private BufferedImage image;
		
	public SpriteSheet(BufferedImage image){
		this.image = image;
	}
	
	public BufferedImage grabImage(int col, int row, int size, int width, int height){
		
		BufferedImage img = image.getSubimage((col * size), (row * size), width, height);
		
		return img;
	}
	
}
