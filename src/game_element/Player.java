package game_element;

import java.awt.Image;
import java.awt.Toolkit;

import Coords.LatLonAlt;

public class Player extends Packman{
	private Image   playerGui;
	public Player(Packman packman) {
		super(packman);
		this.playerGui=Toolkit.getDefaultToolkit().getImage("icons\\EeveePlayer.png");
	}
	public Player(LatLonAlt start, double speed) {
		super(start, speed);
		this.playerGui=Toolkit.getDefaultToolkit().getImage("icons\\EeveePlayer.png");
	}
	public Player(String player) {
		super(player);
		this.playerGui=Toolkit.getDefaultToolkit().getImage("icons\\EeveePlayer.png");
	}
	public Image getImage() {
		return this.playerGui;
	}
}
