package Algo;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

import Coords.LatLonAlt;
import Coords1.ConvertFactory;
import Coords1.Map;
import Coords1.MyCoords;
import GUI.MyFrame;
import Geom.Point3D;
import Robot.Fruit;
import Robot.Game;
import Robot.Packman;
import graph.Graph;
import graph.Node;
/** the class AlgoPlayer - gives the next azimut that the player has to walk considering boxes and gost.
 * @author Naomi and Adi */
public class AlgoPlayer {
	private ArrayList<Point3D> boxBorder;
	private Game game;
	private MyFrame frame;

	/**
	 * the algo to the next azimuth.
	 * @return azimut -of the next step
	 */
	
	public AlgoPlayer(MyFrame frame) {
		this.game= frame.get_game();
		this.frame = frame;
		this.boxBorder = new ArrayList<Point3D>();
		creatSetPointGraph();

	}
	
	public double theNextStep() {
		double[] flag;
		int i= getClosestFruit(frame.get_game().getPlayer());
		//create path
		if(isWall(frame.get_game().getPlayer().getLocation(),game.getTarget(i).getLocation())) {
			Point3D p=calculatPath(frame.get_game().getPlayer().getLocation(),game.getTarget(i).getLocation());
			flag= new MyCoords().azimuth_elevation_dist( game.getPlayer().getLocation(),p);
			return flag[0]; // only the azimuth.
		}
		else {
			flag= new MyCoords().azimuth_elevation_dist( game.getPlayer().getLocation(),game.getTarget(i).getLocation());
			return flag[0];
		}
	}
	/**
	 * getClosestFruit - passes on the array of he fruits and return the closest fruit to the player.
	 * @param p - player
	 * @param game - the game
	 * @return - the index of the closes fruit.
	 */
	private int getClosestFruit(Packman p) {
		int ans = -1;
		ArrayList<Fruit> ff = game.getTargets();
		if (ff.size() > 0) {
			ans = 0;
			double min_d = p.distance3D((Fruit)ff.get(ans));
			for (int i = 1; i < ff.size(); i++) {
				double d = p.distance3D((Fruit)ff.get(i));
				if (d < min_d) {
					min_d = d;
					ans = i;
				}
			}
		}
		return ans;
	}
	/**
	 * nextStepGost- check if is gost on the way.
	 * @return boolean- if not-false if is a gost in the way- true.
	 */
	private boolean nextStepGost() {
		return true;
	}
	/**calculatPath- if in the path from the player to the fruit we have  a box this function will 
	 * Calculate a way to go to the fruit without going through the boxes.
	 * @param player - the point of the player
	 * @param fruit - the point of the fruit
	 * @return - the next point to go for reach the fruit.
	 */
	private Point3D calculatPath(Point3D player,Point3D fruit){
		Graph G = new Graph(); 
		creatSetPointGraph(); // create array of points
		//create graph:
		String source = "a";
		String target = "b";
		G.add(new Node(source));
		for(int i=1; i<boxBorder.size()-1; i++) {
			Node d = new Node(""+i);
			G.add(d);
		}
		G.add(new Node(source));
		
		
		return null;

	}
	
	
	private ArrayList<Integer> whatISee(Point3D me) {
		ArrayList<Integer> seeing = new ArrayList<Integer>();
		
		
		
		
		return seeing;
	}
	
	
	/**creatSetPointGraph - 
	 * 
	 */
	private void creatSetPointGraph() {
		double meterInCoords=0.000008983;
		for(int i=0; i<game.sizeB();i++) {
			//B:
			Point3D lowerLeftAdd = game.getBox(i).getMin();
			lowerLeftAdd.add(meterInCoords, -meterInCoords);
			if(!(ifPointIsInRectangle(game.getBox(i).getMax()))) {
				boxBorder.add(game.getBox(i).getMax());
			}
			//C:
			LatLonAlt lowerRight =new LatLonAlt(game.getBox(i).getMax().lat(),game.getBox(i).getMin().lon(),0);
			if(!(ifPointIsInRectangle(lowerRight))) {
				Point3D lowerRightAdd = game.getBox(i).getMin();
				lowerRightAdd.add(meterInCoords, meterInCoords);
				boxBorder.add(lowerRightAdd);
			}
			//D:
			if(!(ifPointIsInRectangle(game.getBox(i).getMin()))) {
				Point3D lowerLeft = game.getBox(i).getMin();
				lowerLeft.add(-meterInCoords, meterInCoords);
				boxBorder.add(lowerLeft);
			}
			//A:
			LatLonAlt upperLeft =new LatLonAlt(game.getBox(i).getMin().lat(),game.getBox(i).getMax().lon(),0);
			if(!(ifPointIsInRectangle(upperLeft))) {
				Point3D upperLeftAdd = game.getBox(i).getMin();
				upperLeftAdd.add(-meterInCoords,-meterInCoords);
				boxBorder.add(upperLeftAdd);
			}

			
		}
		System.out.println("All Edges for Algo:");
		for(Point3D p: boxBorder) {
			System.out.println(p.toString()+"/n");
		}

	}
	
	/**ifPointIsInRectangle - check if point is in rectangle. */
	private boolean ifPointIsInRectangle(Point3D p) {
		boolean answer=false;
		ConvertFactory cf = frame.getMap().getCf();
		Point3D pPixel = cf.GpsToPicsel(p, frame.getWidth(), frame.getHeight());
		for(int i=0;i< game.sizeB();i++) {
			Rectangle rec = createPixelRec(i);
			if(rec.contains((int)pPixel.x(), (int)pPixel.y())) {
				answer=true;
			}
		}
		return answer;
	}
	
	/**
	 * This Function takes GeoBox and create rectangle in pixels. 
	 * @param index - the index of the box in the array of boxes.
	 * @return rec - Rectangle that represent the box in Pixels.
	 */
	private Rectangle createPixelRec(int index) {
		Point3D upperLeft,lowerRight,lowerRightP,upperLeftP, upperRightP, lowerLeftP;
		ConvertFactory cf = frame.getMap().getCf();
		upperLeft = new Point3D(game.getBox(index).getMin().lat(), game.getBox(index).getMax().lon(), 0); //A
		lowerRight = new Point3D(game.getBox(index).getMax().lat(), game.getBox(index).getMin().lon(),0); //C
		
		upperLeftP = cf.GpsToPicsel(upperLeft, frame.getWidth(), frame.getHeight()); //A
		lowerRightP = cf.GpsToPicsel(lowerRight, frame.getWidth(), frame.getHeight());//C
		upperRightP = cf.GpsToPicsel(game.getBox(index).getMax(), frame.getWidth(), frame.getHeight());//B
		lowerLeftP = cf.GpsToPicsel(game.getBox(index).getMin(), frame.getWidth(), frame.getHeight());//D
		
		Rectangle rec = new Rectangle((int)upperLeftP.x(), (int)upperLeftP.y(), (int)lowerRightP.x() - (int)lowerLeftP.x(),(int)upperLeftP.y() - (int)lowerLeftP.y());
		return rec;
	}



	/**isWall - check if the path passes into a box.
	 * @param gps0 - the player point.
	 * @param gps1 - the fruit point.
	 * @return - boolean if it passes or not.
	 */
	private boolean isWall(Point3D gps0,Point3D gps1) {
		return false;

	}

}
