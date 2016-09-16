package RPG;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Inventory extends Rectangle {

	private static final long serialVersionUID = 1L;
	public int x, y, id = -1, mX, mY, lL = 11, duration, type;
	public double effect;
	public boolean occupied, selected, usable, consumable;
	private ArrayList<BufferedImage> itemImg, GUIImg;
	public String name, line;
	private Font f;

	public Inventory(int x, int y, Main game){
		this.x = x;
		this.y = y;

		setBounds(x - 10, y - 10, 40 * 3, 40 * 3);
		f = new Font("Arial", Font.PLAIN, 14);
		itemImg = new ArrayList<BufferedImage>();
		GUIImg = new ArrayList<BufferedImage>();

		SpriteSheet IF = new SpriteSheet(game.getSpriteSheet("GUI"));
		SpriteSheet item = new SpriteSheet(game.getSpriteSheet("items"));

		for(int ys = 0; ys < 8; ys++){
			for(int xs = 0; xs < 8; xs++){
				itemImg.add(item.grabImage(xs, ys, 20, 20, 20));
			}
		}
		for(int ys = 0; ys < 8; ys++){
			for(int xs = 0; xs < 8; xs++){
				GUIImg.add(IF.grabImage(xs, ys, 32, 32, 32));
			}
		}
	}
	
	public void loadItem(int iID){
		try {
			FileReader fileReader = new FileReader("item.txt");
			int y = 0;
			boolean found = false;
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null){
				String string = line;
				String[] data = string.split(" = ");
				if(y == iID * lL && iID != 100){
					if(Integer.parseInt(data[1]) == iID && !found){
						found = true;
					} else if(Integer.parseInt(data[1]) == iID + 1 && found){
						found = false;
					}
				}
				if(found){
					if(y == iID * lL + 1){
						name = data[1];
					} else if(y == iID * lL + 2){
						effect = Integer.parseInt(data[1]) / 100;
					} else if(y == iID * lL + 3){
						type = Integer.parseInt(data[1]);
					} else if(y == iID * lL + 4){
						duration = Integer.parseInt(data[1]);
					} else if(y == iID * lL + 9){
						usable = Boolean.valueOf(data[1]);
					} else if(y == iID * lL + 10){
						consumable = Boolean.valueOf(data[1]);
					}
				}
				y++;
			}
			bufferedReader.close();
		} catch(FileNotFoundException e){
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void render(Graphics2D g){
		g.setFont(f);
		g.drawImage(GUIImg.get(0), x, y, (int)(32 * Main.yMod) * 3, (int)(32 * Main.yMod) * 3, null);
		if(occupied){
			g.drawImage(itemImg.get(id), x + 5, y + 5, null);
		}

		if(selected){
			g.drawImage(GUIImg.get(1), x, y, null);
			if(occupied){
				g.setColor(Color.BLACK);
				g.drawImage(GUIImg.get(2), (int)Main.mouseX + 10, (int)Main.mouseY + 1, g.getFontMetrics(f).stringWidth(name) + 10, 20, null);
				g.drawString(name, (int)Main.mouseX + 15, (int)Main.mouseY + 15);
			}
		}
		if(Main.showBounds){
			g.setColor(Color.RED);
			g.drawRect(x - 10, y - 10, 40 * 3, 40 * 3);
		}
	}

	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setOccupied(boolean occupied){
		this.occupied = occupied;
	}

	public void setID(int id){
		this.id = id;
		if(id != -1){
			loadItem(id);
		}
	}

	public void setSelected(boolean selected){
		this.selected = selected;
	}

	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}
}
