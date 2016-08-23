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

public class Enemy extends Rectangle {

	private static final long serialVersionUID = 1L;
	public int tempX, tempY, id, size, mX, mY, health, damage, time, lL = 8, direction = 2, defence, type;
	public double x, y, velX, velY, pDist, eVel;
	public boolean inMap = true, collide;
	private ArrayList<BufferedImage> img;
	private String line = null;

	public Enemy(int x, int y, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		img = new ArrayList<BufferedImage>();
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("Enemies"));
		
		for(int ys = 0; ys < 10; ys++){
			for(int xs = 0; xs < 10; xs++){
				img.add(ss.grabImage(xs, ys, 20, 20, 20));
			}
		}

		loadEnemy(id);
	}

	public void loadEnemy(int eID){
		try {
			FileReader fileReader = new FileReader("enemy.txt");
			int y = 0;
			boolean found = false;
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while((line = bufferedReader.readLine()) != null){
				String string = line;
				String[] data = string.split(" = ");
				if(y == eID * lL)
					if(Integer.parseInt(data[1]) == eID && !found){
						found = true;
					} else if(data[1] == String.valueOf(eID + 1) && found){
						found = false;
					}
				if(found){
					if(y == eID * lL + 1){
						size = Integer.parseInt(data[1]);
					} else if(y == eID * lL + 2){
						health = Integer.parseInt(data[1]);
					} else if(y == eID * lL + 3){
						defence = Integer.parseInt(data[1]);
					} else if(y == eID * lL + 4){
						damage = Integer.parseInt(data[1]);
					} else if(y == eID * lL + 5){
						eVel = Integer.parseInt(data[1]) * 0.01;
					} else if(y == eID * lL + 6){
						time = Integer.parseInt(data[1]);
					} else if(y == eID * lL + 7){
						type = Integer.parseInt(data[1]);
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

	public void tick(){
		if(inMap){
			x += velX;
			y += velY;
			setBounds((int) x, (int) y, size, size);

			pDist = Math.sqrt(Math.pow(Main.p.x - x, 2)
					+ Math.pow(Main.p.y - y, 2));

			if(pDist <= 200){
				if(Main.p.x < (int) x + size / 2 - Main.p.size / 2){
					velX = -eVel;
					velY = 0;
				} else if(Main.p.x > (int) x + size / 2 - Main.p.size / 2){
					velX = eVel;
					velY = 0;
				} else if(Main.p.y < (int) y + size / 2 - Main.p.size / 2){
					velY = -eVel;
					velX = 0;
				} else if(Main.p.y > (int) y + size / 2 - Main.p.size / 2){
					velY = eVel;
					velX = 0;
				}
				if(Main.p.x < (int) x + 2 + size / 2 - Main.p.size / 2
						&& Main.p.x > (int) x - 2 + size / 2 - Main.p.size / 2){
					if(Main.p.y < (int) y + size / 2 - Main.p.size / 2){
						velY = -eVel;
						velX = 0;
					} else if(Main.p.x > (int) y + size / 2 - Main.p.size / 2){
						velY = eVel;
						velX = 0;
					}
				} else if(Main.p.y < (int) y + 2 + size / 2 - Main.p.size / 2
						&& Main.p.y > (int) y - 2 + size / 2 - Main.p.size / 2){
					if(Main.p.x < (int) x + size / 2 - Main.p.size / 2){
						velX = -eVel;
						velY = 0;
					} else if(Main.p.x > (int) x + size / 2 - Main.p.size / 2){
						velX = eVel;
						velY = 0;
					}
				}
			} else {
				velX = 0;
				velY = 0;
			}
		} else {
			velX = 0;
			velY = 0;
		}
		if(Main.levelX != mX || Main.levelY != mY){
			inMap = false;
			collide = false;
		} else {
			inMap = true;
		}
	}

	public void render(Graphics2D g){
		if(inMap){
			if(id != 100){
				g.drawImage(img.get(id), (int) x, (int) y, null);
			}
		}
		if(Main.showBounds && inMap){
			g.setColor(Color.RED);
			g.drawRect((int) x, (int) y, size, size);
		}
	}

	public void setID(int newID){
		id = newID;
	}

	public void setEnemy(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setItem(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}

	public void setIM(boolean inMap){
		this.inMap = inMap;
	}

	public void setHealth(int health){
		this.health = health;
	}
}
