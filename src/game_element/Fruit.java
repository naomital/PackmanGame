package game_element;


import Coords.LatLonAlt;
import GIS.GIS_element;
import GIS.Meta_data;
import Geom.Geom_element;
import Geom.Point3D;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.Date;

/**
 * This class represent a fruit. 
 * @author Adi and Naomi.
 *
 */
public class Fruit implements GIS_element, Serializable
{
	private LatLonAlt _pos;
	private Point3D _ori;
	private Meta_data _meta_data;
	private long _start_time;
	private static int _count = 0;
	private double _weight;
	private int _id;
	public static final double STANDARD_WEIGHT = 1.0D;
	private Image  dountIcon;

	/**
	 * constructor
	 * @param start - start location of the player. 
	 */
	public Fruit(LatLonAlt start) {
		_pos = new LatLonAlt(start);
		initTime();
		_id = (_count++);
		_weight = 1.0D;
		this.dountIcon = Toolkit.getDefaultToolkit().getImage("icons\\dount.png");
	}

	/** constructor from CSV line*/
	public Fruit(String line) {
		String[] arr = line.split(",");
		_id = Integer.parseInt(arr[1]);
		String p = arr[2] + "," + arr[3] + "," + arr[4];
		_pos = new LatLonAlt(p);
		_weight = Double.parseDouble(arr[5]);
		_start_time = 0L;
		this.dountIcon = Toolkit.getDefaultToolkit().getImage("icons\\dount.png");
	}

	 /** Copy constructor */
	public Fruit(Fruit fruit) { this(fruit.toString()); }

	public String toString() {
		String ans = "F," + _id + "," + getLocation() + "," + _weight;
		return ans; }

	public double getWeight() { return _weight; }
	public LatLonAlt getLocation() { return _pos; }

	/**
	 * gets the current time. 
	 */
	public void initTime() { _start_time = new Date().getTime(); }

	/**
	 * @return  The time since the start of the game until now.
	 */
	public long delta_time() { long dt_ms = new Date().getTime() - _start_time;
	return dt_ms; }

	public Geom_element getGeom() { return _pos; }
	public Meta_data getData() { return _meta_data; }

	public void translate(Point3D vec) { _pos.add(vec); }

	public Point3D getOrientation() { return _ori; }

	public Image getImage() {
		return this.dountIcon;
	}

}
