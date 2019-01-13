package game_element;

import java.awt.Image;
import java.awt.Toolkit;

import Coords.LatLonAlt;

/**
 * This function represent a ghost in the game.
 * @author Adi and Naomi
 *
 */
public class Ghost  extends Packman{
	private Image  ghost;
	
	/**Copy constructor*/
	public Ghost(Packman packman) {
		super(packman);
		this.ghost=Toolkit.getDefaultToolkit().getImage("icons\\GengarGhost.png");
	}
	/**constructor*/
	public Ghost(LatLonAlt start, double speed) {
		super(start, speed);
		this.ghost=Toolkit.getDefaultToolkit().getImage("icons\\GengarGhost.png");
	}
	/**string constructor*/
	public Ghost(String stringGhost) {
		super(stringGhost);
		this.ghost=Toolkit.getDefaultToolkit().getImage("icons\\GengarGhost.png");
	}
	public Image getImage() {
		return this.ghost;
	}
}
