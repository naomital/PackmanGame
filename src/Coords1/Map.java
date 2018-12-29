package Coords1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import Geom.Point3D;

/**
 * This class represents a map that contains image and 2 GPS points that helps to adjust the image of the map to the world map.
 * This class calls: 
 * [*] ImageFactory in order to set the map's image and handle it's functionality.
 * [*] ConvertFactory - in order to handle convert pixels to GPS points, GPS to pixels and to calculate distance between 2 coords.
 * @author Adi && Naomi
 */
public class Map{
	private BufferedImage img;
	private ConvertFactory cf;

	//private String path;  
	/**
	 * constructor for map that try to read an Image and calls ImageFactory to handle the image functionality.
	 */
	public Map() { // constructor
		try {
			String path = "Ariel.jpg";
			if(!path.endsWith("jpg") && !path.endsWith("png")) {
				throw new IOException("Can't read input file!");
			}
			this.img =ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		this.cf = new ConvertFactory();
	}

	/**
	 * This function returns the image of the map.
	 * @return img - the image of the map.
	 */
	public BufferedImage getImg() {
		return img;
	}

	/**
	 * This function sets the image of the map. 
	 * @param img -  the image of the map. 
	 */
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	/**
	 * This function returns a ConvertFactory object.
	 * @return cf - object that helps this class to handle coords Conversions.
	 */
	public ConvertFactory getCf() {
		return cf;
	}

	/**
	 * This function sets a ConvertFactory object.
	 * @param cf - object that helps this class to handle coords Conversions.
	 */
	public void setCf(ConvertFactory cf) {
		this.cf = cf;
	}

}