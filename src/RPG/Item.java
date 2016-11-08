package RPG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Item extends Rectangle {

	private static final long serialVersionUID = 1L;
	public int x, y, id, mX, mY, size, xMod, yMod, lL = 12, duration, type;
	public double xSize, ySize, effect;
	public boolean pickable = true, inMap = true, pickedUp, usable, consumable, stackAble;
	private ArrayList<BufferedImage> itemImg;
	public String line = null, name = null;

	public Item(int x, int y, int size, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		this.size = size;

		itemImg = new ArrayList<BufferedImage>();

		loadItem(id);

		if(id != 100){
			setBounds((int) (x + Main.blockWidth * xSize), (int) (y + Main.blockHeight * ySize), size / xMod, size / yMod);
		}

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("items"));
		for(int ys = 0; ys < 8; ys++){
			for(int xs = 0; xs < 8; xs++){
				itemImg.add(ss.grabImage(xs, ys, 20, 20, 20));
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
					} else if(y == iID * lL + 5){
						xSize = (Integer.parseInt(data[1]) * 0.01) * Main.xMod;
					} else if(y == iID * lL + 6){
						ySize = (Integer.parseInt(data[1]) * 0.01) * Main.yMod;
					} else if(y == iID * lL + 7){
						xMod = Integer.parseInt(data[1]);
					} else if(y == iID * lL + 8){
						yMod = Integer.parseInt(data[1]);
					} else if(y == iID * lL + 9){
						usable = Boolean.valueOf(data[1]);
					} else if(y == iID * lL + 10){
						consumable = Boolean.valueOf(data[1]);
					} else if(y == iID * lL + 11){
						stackAble = Boolean.valueOf(data[1]);
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
		if(inMap && id != 100){
			g.drawImage(itemImg.get(id), x, y, null);
		}
		if(Main.showBounds && id != 100 && inMap){
			g.setColor(Color.RED);
			g.drawRect((int)(x + Main.blockWidth * xSize), (int)(y + Main.blockHeight * ySize), size / xMod, size / yMod);
		}
	}

	public void setID(int newID){
		id = newID;
	}

	public void setX(int x){
		this.x = x;
	}

	public void setY(int y){
		this.y = y;
	}

	public void setItem(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setPickedUp(boolean pickedUp){
		this.pickedUp = pickedUp;
	}

	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}

	public void setIM(boolean inMap){
		this.inMap = inMap;
	}

	public void setPickable(boolean pickable){
		this.pickable = pickable;
	}

	public void setUsable(boolean usable){
		this.usable = usable;
	}
	public void setName(String name){
		this.name = name;
	}
}