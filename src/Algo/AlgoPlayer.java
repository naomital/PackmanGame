package Algo;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
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
import graph.Graph_Algo;
import graph.Node;
/** the class AlgoPlayer - gives the next azimut that the player has to walk considering boxes and gost.
 * @author Naomi and Adi */
public class AlgoPlayer {
	private ArrayList<Point3D> boxBorder;
	private ArrayList<Rectangle> pixelRecs;
	private ArrayList<Line2D> starightLines;
	private Game game;
	private MyFrame frame;
	private ConvertFactory cf;


	/**
	 * the algo to the next azimuth.
	 * @return azimut -of the next step
	 */

	public AlgoPlayer(MyFrame frame) {
		this.game= frame.get_game();
		this.frame = frame;
		this.boxBorder = new ArrayList<Point3D>();
		this.cf = frame.getMap().getCf();
		this.pixelRecs = new ArrayList<Rectangle>();
		this.starightLines = new ArrayList<Line2D>();
		//creatSetPointGraph();
		if(pixelRecs.size() == 0) {
			buildPixelRectangles();
		}
		//theNextStep();

	}
	public double theNextStep() {
		double[] flag;
		if(pixelRecs.size() == 0) {
			buildPixelRectangles();
		}
		int i= getClosestFruit(frame.get_game().getPlayer());
		//create path
		if(checkWall(frame.get_game().getPlayer().getLocation(),game.getTarget(i).getLocation())) {
			String p = calculatPath(frame.get_game().getPlayer().getLocation(),game.getTarget(i).getLocation());
			Fruit newTarget = game.getTarget(Integer.parseInt(p));
			flag = new MyCoords().azimuth_elevation_dist( game.getPlayer().getLocation(), newTarget.getLocation());
			return flag[0]; // only the azimuth.
		}
		else {
			flag = new MyCoords().azimuth_elevation_dist(game.getPlayer().getLocation(),game.getTarget(i).getLocation());
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
	private String calculatPath(Point3D player,Point3D fruit){
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
		G.add(new Node(target));

		createStraightLines();
		int index1 = 0, index2 = 1; 
		Node first = G.getNodeByIndex(index1); // source
		Node second = G.getNodeByIndex(index2); // first node on list 
		Point2D _p1 = starightLines.get(0).getP1();//fist node
		Point2D _p2 = starightLines.get(1).getP2();//second node
		for(Line2D line: starightLines) {
			if(!line.getP1().equals(_p1))
				first = G.getNodeByIndex(++index1);
			if(!line.getP1().equals(_p2))
				second = G.getNodeByIndex(++index2);
			for(Rectangle rec: pixelRecs) {
				if(!line.intersects(rec)) {
					G.addEdge(first.get_name(),second.get_name(),line.getP1().distance(line.getP2())); // need to change a and 1 to the right ones. 
				}
			}
		}
		Graph_Algo.dijkstra(G, source);

		Node b = G.getNodeByName(target); 

		System.out.println("***** Graph Demo for OOP_Ex4 *****");
		System.out.println(b);
		System.out.println("Dist: "+b.getDist());
		ArrayList<String> shortestPath = b.getPath();
		for(int i=0;i<shortestPath.size();i++) {
			System.out.print(","+shortestPath.get(i));
		}
		//System.out.println(G.toString());
		return shortestPath.get(0);

	}

	public boolean checkWall(Point3D p1, Point3D p2) {
		boolean isWall = false;
		p1 = new Point3D(cf.GpsToPicsel(p1, frame.getWidth(), frame.getHeight()));
		p2 = new Point3D(cf.GpsToPicsel(p2, frame.getWidth(), frame.getHeight()));
		Line2D newLine = new Line2D.Double(p1.x(), p1.y(),p2.x(), p2.y());
		for(Rectangle rec: pixelRecs) {
			if(newLine.intersects(rec)) {
				isWall = true;
			}
		}
		System.out.println("Check Wall: "+isWall);
		return isWall;
	}

	private void createStraightLines() {
		int runner = 1;
		for(Point3D p: boxBorder) {
			for(int i=runner; runner<boxBorder.size(); runner++) {
				Line2D newLine = new Line2D.Double(p.x(), p.y(),boxBorder.get(i).x(), boxBorder.get(i).y());
				starightLines.add(newLine);
			}
			runner++;
		}
	}

	/**
	 * This function build the graph that needed for the algorythm. 
	 * for every box in the game the function checks all it's edges and adds to arrayList the points that
	 * are not inside another box and that are between the game's frame border. 
	 * In addition, this function increase every "fit" point with 1 pixel. (to allowed the player to walk in safe
	 * without losing points).
	 */
	private void creatSetPointGraph() {
		for(int i=0; i<game.sizeB();i++) {
			//A:
			Point3D upperLeft = cf.GpsToPicsel((new Point3D(game.getBox(i).getMin().lat(),game.getBox(i).getMax().lon(),0)), frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(upperLeft))) {
				upperLeft.add(-1,-1);
				if(isIn(upperLeft)) {
					//whatISee(upperLeft, i);
					boxBorder.add(cf.PicselToGps(upperLeft, frame.getWidth(), frame.getHeight()));
				}
			}
			//B:
			Point3D upperRight = cf.GpsToPicsel(new Point3D(game.getBox(i).getMax()), frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(upperRight))) {
				upperRight.add(1,-1);
				if(isIn(upperRight))
					boxBorder.add(cf.PicselToGps(upperRight, frame.getWidth(), frame.getHeight()));
			}
			//C:
			Point3D lowerRight =cf.GpsToPicsel((new Point3D(game.getBox(i).getMax().lat(),game.getBox(i).getMin().lon(),0)),frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(lowerRight))) {
				lowerRight.add(1,1);
				if(isIn(lowerRight))
					boxBorder.add(cf.PicselToGps(lowerRight, frame.getWidth(), frame.getHeight()));
			}
			//D:
			Point3D lowerLeft = cf.GpsToPicsel(new Point3D(game.getBox(i).getMin()), frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(lowerLeft))) {
				lowerLeft.add(-1, 1);
				if(isIn(lowerLeft))
					boxBorder.add(cf.PicselToGps(lowerLeft, frame.getWidth(), frame.getHeight()));
			}
		}		
	}

	/**ifPointIsInRectangle - check if point is in rectangle. */
	/**
	 * Check if the box's edge point is inside another box.
	 * @param p - the given point in GPS coordinate.
	 * @return TRUE - if the point is inside another box or FALSE otherwise.
	 */
	private boolean ifPointIsInRectangle(Point3D p) {
		boolean answer=false;
		Point3D pointPixel = cf.GpsToPicsel(p, frame.getWidth(), frame.getHeight());
		for(Rectangle rec: pixelRecs) {
			//			Rectangle rec = createPixelRec(i);
			//	System.out.println(rec.toString()+"\n");
			if(rec.contains((int)pointPixel.x(), (int)pointPixel.y())) {
				answer=true;
			}
		}
		return answer;
	}

	/**
	 * Builds arrayList of boxes in pixels.
	 */
	private void buildPixelRectangles() {
		for(int i=0;i< game.sizeB();i++) {
			pixelRecs.add(createPixelRec(i));
		}
	}

	/**
	 * Check if the Edge point is inside the game's frame borders.
	 * @param pixelPoint - the given edge point in pixels.
	 * @return true - if the given point is inside the frame (not include the borders).
	 */
	public boolean isIn(Point3D pixelPoint) {
		if((pixelPoint.x() < 1)||(pixelPoint.x() > 1432))
			return false;
		if((pixelPoint.y() < 1)||(pixelPoint.y() > 641))
			return false;
		return true;
	}

	/**
	 * This Function takes GeoBox and create rectangle in pixels. 
	 * @param index - the index of the box in the array of boxes.
	 * @return rec - Rectangle that represent the box in Pixels.
	 */
	private Rectangle createPixelRec(int index) {
		Point3D upperLeft,lowerRight,lowerRightP,upperLeftP, upperRightP, lowerLeftP;
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
	//	private boolean isWall(Point3D gps0,Point3D gps1) {
	//		return false;
	//
	//	}

//	public static void main(String[] args) {
//		MyFrame frame = new MyFrame();
//		AlgoPlayer ap = new AlgoPlayer(frame);
//		Point3D p1 = new Point3D(526.0, 127.0, 0);
//		Point3D p2 = new Point3D(151.0, 354.0, 0.0);
//		Rectangle rec = new Rectangle(263, 186,317,217);
//		ap.getPixelRecs().add(rec);
//		System.out.println(ap.checkWall(p1, p2));
//		
//	}
	public ArrayList<Rectangle> getPixelRecs() {
		return pixelRecs;
	}
	public void setPixelRecs(ArrayList<Rectangle> pixelRecs) {
		this.pixelRecs = pixelRecs;
	}
}
