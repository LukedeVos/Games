package RPG;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Entity extends Rectangle {

	private static final long serialVersionUID = 1L;
	public int x, y, id, mX, mY, sc, size;
	private BufferedImage en0, en1, en2, en3, en4;
	AffineTransform at = new AffineTransform();

	public Entity(int x, int y, int size, int id, Main game){
		this.x = x;
		this.y = y;
		this.id = id;
		this.size = size;

		if(id == 0){
			if(Main.p.direction == 0){
				setBounds((int) (x + Main.blockSize * 0.35) - 6, y - 13,
						size / 3, size);
			} else if(Main.p.direction == 1){
				setBounds(x + 3, (int) (y + Main.blockSize * 0.35) - 6, size,
						size / 3);
			} else if(Main.p.direction == 2){
				setBounds((int) (x + Main.blockSize * 0.35) - 6, y + 3,
						size / 3, size);
			} else if(Main.p.direction == 3){
				setBounds(x - 14, (int) (y + Main.blockSize * 0.35) - 6, size,
						size / 3);
			}
		}

		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet("items"));
		en0 = ss.grabImage(0, 0, 20, size, size);
		en1 = ss.grabImage(1, 0, 20, size, size);
		en2 = ss.grabImage(2, 0, 20, size, size);
		en3 = ss.grabImage(3, 0, 20, size, size);
		en4 = ss.grabImage(4, 0, 20, size, size);
	}

	public void render(Graphics2D g){
		if(id == 0){
			g.drawImage(en0, x, y, null);
		} else if(id == 1){
			g.drawImage(en1, x, y, null);
		} else if(id == 2){
			g.drawImage(en2, x, y, null);
		} else if(id == 3){
			g.drawImage(en3, x, y, null);
		} else if(id == 4){
			g.drawImage(en4, x, y, null);
		}
	}

	public void useItem(Graphics2D g){
		g.setColor(Color.RED);
		if(Main.inventory[Main.used].id == 0){
			if(Main.p.direction == 0){
				g.drawImage(en0, Main.p.x - 6, Main.p.y - 13, null);
				if(Main.showBounds){
					g.drawRect((int) (Main.p.x + Main.blockSize * 0.35) - 6,
							Main.p.y - 13, 20 / 3, 20);
				}
			} else if(Main.p.direction == 1){
				at.setToRotation(Math.PI / 2, Main.p.x + Main.blockSize / 2,
						Main.p.y + Main.blockSize / 2);
				if(Main.showBounds){
					g.drawRect(x + 3, (int) (y + Main.blockSize * 0.35) - 6,
							20, 20 / 3);
				}
				g.setTransform(at);
				g.drawImage(en0, Main.p.x - 6, Main.p.y - 3, null);
			} else if(Main.p.direction == 2){
				at.setToRotation(Math.PI, Main.p.x + Main.blockSize / 2,
						Main.p.y + Main.blockSize / 2);
				if(Main.showBounds){
					g.drawRect((int) (x + Main.blockSize * 0.35) - 6, y + 3,
							20 / 3, 20);
				}
				g.setTransform(at);
				g.drawImage(en0, Main.p.x + 6, Main.p.y - 3, null);
			} else if(Main.p.direction == 3){
				at.setToRotation(-Math.PI / 2, Main.p.x + Main.blockSize / 2,
						Main.p.y + Main.blockSize / 2);
				if(Main.showBounds){
					g.drawRect(x - 14, (int) (y + Main.blockSize * 0.35) - 6,
							20, 20 / 3);
				}
				g.setTransform(at);
				g.drawImage(en0, Main.p.x + 6, Main.p.y - 13, null);
			}
			Main.p.setSpeed(0, 0);
			g.dispose();
		} else if(Main.inventory[Main.used].id == 1){
			Main.p.setDirection(2);
			g.drawImage(en1, Main.p.x - 2, Main.p.y, 15, 15, null);
			g.dispose();
			if(Main.p.health < 100 && sc % 2 == 0){
				Main.p.setHealth(Main.p.health + 1);
			}
		} else if(Main.inventory[Main.used].id == 2){
			g.drawImage(en2, x, y, null);
			g.dispose();
		} else if(Main.inventory[Main.used].id == 3){
			g.drawImage(en3, x, y, null);
			g.dispose();
		} else if(Main.inventory[Main.used].id == 4){
			g.drawImage(en4, x, y, null);
			g.dispose();
		}

	}

	public void tick(){
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
		} else if(sc == 50 && Main.inventory[Main.used].id == 2){
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

	public void setMap(int mX, int mY){
		this.mX = mX;
		this.mY = mY;
	}
}