package game_element;

import java.awt.Image;
import java.awt.Toolkit;

import Coords.LatLonAlt;

/**
 * This function represent the packman's (The player's Enemy). 
 * @author Naomi and Adi.
 */
public class Enemy extends Packman{
	private Image pacmanIcon;
	
	/** constructor */
	public Enemy(LatLonAlt start, double speed) {
		super(start, speed);
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");

	}
	
	/** copy constructor */
	public Enemy(Packman packman) {
		super(packman);
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");
	}
	
	/** string constructor */
	public Enemy(String Enemy) {
		super(Enemy);
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");

	}
	
	/** 
	 * @return the image of the packman.
	 */
	public Image getImage() {
		return this.pacmanIcon;
	}
}
