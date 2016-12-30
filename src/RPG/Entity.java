package RPG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Entity extends Rectangle {

	private static final long serialVersionUID = 1L;
	public int x, y, id, mX, mY, sc, size, lL = 12, xMod, yMod, type, direction;
	public double effect, xSize, ySize, velX, velY;
	public boolean beingUsed;
	private ArrayList<BufferedImage> img;
	AffineTransform at = new AffineTransform();
	private String line = null;

	public Entity(int x, int y, int size, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		this.size = size;
		img = new ArrayList<BufferedImage>();
		
		if(Main.use){
			Main.entityCounter++;
		}
		
		loadItem(id);
		
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("items"));
		for(int ys = 0; ys < 8; ys++){
			for(int xs = 0; xs < 8; xs++){
				img.add(ss.grabImage(xs, ys, 20, size, size));
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
					if(y == iID * lL + 2){
						effect = Integer.parseInt(data[1]) / 100;
					} else if(y == iID * lL + 3){
						type = Integer.parseInt(data[1]);
					} else if(y == iID * lL + 5){
						xSize = (Integer.parseInt(data[1]) * 0.01) * Main.xMod;
					} else if(y == iID * lL + 6){
						ySize = (Integer.parseInt(data[1]) * 0.01) * Main.yMod;
					} else if(y == iID * lL + 7){
						xMod = Integer.parseInt(data[1]);
					} else if(y == iID * lL + 8){
						yMod = Integer.parseInt(data[1]);
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
		g.setColor(Color.RED);
		if(Main.inventory[Main.used].type == 0){
			if(Main.p.direction == 0){
				g.drawImage(img.get(id), Main.p.x - 6, Main.p.y - 13, null);
				if(Main.showBounds){
					g.drawRect((int) (Main.p.x + Main.blockWidth * 0.35) - 6, Main.p.y - 13, 20 / 3, 20);
				}
			} else if(Main.p.direction == 1){
				at.setToRotation(Math.PI / 2, Main.p.x + Main.blockWidth / 2, Main.p.y + Main.blockWidth / 2);
				if(Main.showBounds){
					g.drawRect(x + 3, (int) (y + Main.blockWidth * 0.35) - 6, 20, 20 / 3);
				}
				g.setTransform(at);
				g.drawImage(img.get(id), Main.p.x - 6, Main.p.y - 3, null);
			} else if(Main.p.direction == 2){
				at.setToRotation(Math.PI, Main.p.x + Main.blockWidth / 2, Main.p.y + Main.blockWidth / 2);
				if(Main.showBounds){
					g.drawRect((int) (x + Main.blockWidth * 0.35) - 6, y + 3, 20 / 3, 20);
				}
				g.setTransform(at);
				g.drawImage(img.get(id), Main.p.x + 6, Main.p.y - 3, null);
			} else if(Main.p.direction == 3){
				at.setToRotation(-Math.PI / 2, Main.p.x + Main.blockWidth / 2, Main.p.y + Main.blockWidth / 2);
				if(Main.showBounds){
					g.drawRect(x - 14, (int) (y + Main.blockWidth * 0.35) - 6, 20, 20 / 3);
				}
				g.setTransform(at);
				g.drawImage(img.get(id), Main.p.x + 6, Main.p.y - 13, null);
			}
			Main.p.setSpeed(0, 0);
		} else if(Main.inventory[Main.used].type == 1){
			g.drawImage(img.get(id), x, y, null);
		} else if(Main.inventory[Main.used].type == 2){
			g.drawImage(img.get(id), x, y, null);
		} else if(Main.inventory[Main.used].consumable){
			Main.p.setDirection(2);
			g.drawImage(img.get(1), Main.p.x - 2, Main.p.y, 15, 15, null);
			if(Main.p.health < 100 && sc % 2 == 0){
				Main.p.setHealth(Main.p.health + 1);
			}
		} else {
			System.out.println(direction);
			if(direction == 1){
				at.setToRotation(Math.PI / 2, x, y);
				g.setTransform(at);
			} else if(direction == 2){
				at.setToRotation(Math.PI, x, y);
				g.setTransform(at);
			} else if(direction == 3){
				at.setToRotation(-Math.PI / 2, x, y);
				g.setTransform(at);
			}
			g.drawImage(img.get(id), x, y, null);
		}
		g.dispose();
	}

	public void useItem(){
		if(Main.inventory[Main.used].type == 0){
			if(Main.p.direction == 0){
				setBounds((int) (x + Main.blockWidth * 0.35) - 6, y - 13, size / 3, size);
			} else if(Main.p.direction == 1){
				setBounds(x + 3, (int) (y + Main.blockWidth * 0.35) - 6, size, size / 3);
			} else if(Main.p.direction == 2){
				setBounds((int) (x + Main.blockWidth * 0.35) - 6, y + 3, size / 3, size);
			} else if(Main.p.direction == 3){
				setBounds(x - 14, (int) (y + Main.blockWidth * 0.35) - 6, size, size / 3);
			}
		} else if(Main.inventory[Main.used].type == 3){
			
		} else if(Main.inventory[Main.used].type == 1){
			System.out.println("yep");
			boolean arrow = false;
			for(int i = 0; i < Main.inventory.length; i++){
				if(Main.inventory[i].type == 2){
					arrow = true;
				}
			}
			if(arrow){
				Main.shootArrow = true;
			} else {
				
			}
		} else if(Main.inventory[Main.used].type == 2){

		}
		beingUsed = false;

	}

	public void tick(){
		x+=velX;
		y+=velY;
		
		if(sc == 20 && Main.inventory[Main.used].id == 0){
			Main.disableMovement = false;
			sc = 0;
			Main.use = false;
			Main.remove = true;
		} else if(sc == 50 && Main.inventory[Main.used].id == 1){
			Main.disableMovement = false;
			sc = 0;
			Main.use = false;
			Main.remove = true;
			Main.clearInventory(Main.used);
		} else if(sc == 10 && Main.inventory[Main.used].id == 2){
			Main.disableMovement = false;
			sc = 0;
			Main.use = false;
			Main.remove = true;
		} else if(sc == 50 && Main.inventory[Main.used].id == 3){
			Main.disableMovement = false;
			sc = 0;
			Main.use = false;
			Main.remove = true;
			Main.clearInventory(Main.used);
		} else if(sc == 50 && Main.inventory[Main.used].id == 4){
			Main.disableMovement = false;
			sc = 0;
			Main.use = false;
			Main.remove = true;
		} else if(Main.use){
			sc++;
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
	public void setEntity(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setVelX(int velX){
		this.velX = velX;
	}
	public void setVelY(int velY){
		this.velY = velY;
	}
	public void setSpeed(int velX, int velY){
		this.velX = velX;
		this.velY = velY;
	}
	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}
	public void setBeingUsed(boolean beingUsed){
		this.beingUsed = beingUsed;
	}
	public void setDirection(int direction){
		this.direction = direction;
	}
}