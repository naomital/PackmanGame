package game_element;

import java.awt.Image;
import java.awt.Toolkit;

import Coords.LatLonAlt;

public class Enemy extends Packman{
	private Image pacmanIcon;
	
	/**
	 * 
	 */
	public Enemy(LatLonAlt start, double speed) {
		super(start, speed);
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");

	}
	public Enemy(Packman packman) {
		super(packman);
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");
	}
	public Enemy(String Enemy) {
		super(Enemy);
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");

	}
	public Image getImage() {
		return this.pacmanIcon;
	}
}
