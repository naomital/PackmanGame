package game_element;

import java.awt.Image;
import java.awt.Toolkit;

import Coords.LatLonAlt;

public class Ghost  extends Packman{
	private Image  ghost;
	public Ghost(Packman packman) {
		super(packman);
		this.ghost=Toolkit.getDefaultToolkit().getImage("icons\\GengarGhost.png");
	}
	public Ghost(LatLonAlt start, double speed) {
		super(start, speed);
		this.ghost=Toolkit.getDefaultToolkit().getImage("icons\\GengarGhost.png");
	}
	public Ghost(String stringGhost) {
		super(stringGhost);
		this.ghost=Toolkit.getDefaultToolkit().getImage("icons\\GengarGhost.png");
	}
	public Image getImage() {
		return this.ghost;
	}
}
