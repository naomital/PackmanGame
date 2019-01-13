package Algo;

import java.awt.Color;
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
import game_element.Enemy;
import game_element.Fruit;
import game_element.Game;
import game_element.Ghost;
import game_element.Packman;
import game_element.Player;
import graph.Graph;
import graph.Graph_Algo;
import graph.Node;
/** the class AlgoPlayer - gives the next azimut that the player has to walk considering boxes and gost.
 * @author Naomi and Adi */
public class AlgoPlayer {
	
	private ArrayList<Point3D> boxBorder;
	private ArrayList<Rectangle> pixelRecs;
	private Game game;
	private MyFrame frame;
	private ConvertFactory cf;
	private char nextType = 'X';
	private int indexTraget = -1;

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
		if(pixelRecs.size() == 0) {
			buildPixelRectangles();
		}

	}
	public ArrayList<Point3D> theNextStep() {
		Point3D target = null;
		ArrayList<Point3D> path=new ArrayList<Point3D> ();
		int i = getClosestTarget(frame.get_game().getPlayer());
		if(nextType == 'F') {
			target = game.getTarget(i).getLocation();
		}
		else if(nextType == 'P'){
			target = game.getRobots().get(i).getLocation();
		}
		//create path
		if(checkWall(frame.get_game().getPlayer().getLocation(),target)) {
			path= calculatPath(frame.get_game().getPlayer().getLocation(),target);
			return path;
		}
		else {
			path.add(target);
			return path;
		}
	}
	
	public int getClosestTarget(Packman p) {
		double dFruit=Integer.MAX_VALUE,dPackman=Integer.MAX_VALUE;
		int fruitIndex = getClosestFruit(p);
		int packmanIndex = getClosestPackman(p);
		if(game.sizeT()!=0) { dFruit = p.distance3D(game.getTargets().get(fruitIndex));}
		if(game.sizeR()!=0) { dPackman = p.distance3D(game.getRobots().get(packmanIndex));}
		
		if(dPackman > dFruit) {
			if(this.checkWall(game.getTarget(fruitIndex).getLocation(), game.getTarget(fruitIndex).getLocation())) {
				nextType = 'P';
				indexTraget = packmanIndex;
				return packmanIndex;
			}
			nextType = 'F';
			indexTraget = fruitIndex;
			return fruitIndex;
		}
		else {
			if(this.checkWall(game.getPackman(packmanIndex).getLocation(), game.getPackman(packmanIndex).getLocation())) {
				nextType = 'F';
				indexTraget = fruitIndex;
				return fruitIndex;
			}
			nextType = 'P';
			indexTraget = packmanIndex;
			return packmanIndex;
		}
	}

	public int getIndexTraget() {
		return indexTraget;
	}
	public void setIndexTraget(int indexTraget) {
		this.indexTraget = indexTraget;
	}
	/**
	 * getClosestFruit - passes on the array of he fruits and return the closest fruit to the player.
	 * @param p - player
	 * @param game - the game
	 * @return - the index of the closes fruit.
	 */
	public int getClosestFruit(Packman p) {
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
	 * This function gets the player location and returns the nearest packman.
	 * @param p
	 * @return
	 */
	public int getClosestPackman(Packman p) {
		int ans = -1;
		ArrayList<Enemy> pp = game.getRobots();
		if (pp.size() > 0) {
			ans = 0;
			double min_d = p.distance3D((Packman)pp.get(ans));
			for (int i = 1; i < pp.size(); i++) {
				double d = p.distance3D((Packman)pp.get(i));
				if (d < min_d) {
					min_d = d;
					ans = i;

				}
			}
		}
		return ans;
	}

	/**calculatPath- if in the path from the player to the fruit we have  a box this function will 
	 * Calculate a way to go to the fruit without going through the boxes.
	 * @param player - the point of the player
	 * @param fruit - the point of the fruit
	 * @return - the next point to go for reach the fruit.
	 */
	public ArrayList<Point3D>  calculatPath(Point3D player,Point3D target){
		System.out.println("start calc");
		ArrayList<Point3D> path=new ArrayList<Point3D> (); // from current place to next target
		boolean inRec = false;
		Graph G = new Graph(); 
		creatSetPointGraph(); // create array of points
		//create graph:
		String source = "a";
		String dest = "b";
		G.add(new Node(source));
		for(int i=0; i<boxBorder.size(); i++) {
			Node d = new Node(""+i);
			G.add(d);
		}
		G.add(new Node(dest));

		//player to boxBorder that see each other
		for(int i=0;i<boxBorder.size();i++) {
			Point3D p1 = cf.GpsToPicsel(player, frame.getWidth(), frame.getHeight());
			Point3D p2= cf.GpsToPicsel(boxBorder.get(i), frame.getWidth(), frame.getHeight());
			Line2D line = new Line2D.Double(p1.x(), p1.y(),p2.x(), p2.y());
			for(Rectangle rec: pixelRecs) {
				if(line.intersects(rec)) {				
					inRec = true;
					break;
				}
			}
			if(!inRec) {	
				System.out.println("player:"+player+"boxBorder:"+boxBorder.get(i).toString());
				G.addEdge("a",""+i,player.distance3D(boxBorder.get(i))); 
			}
			inRec = false;
		}
		
		//	boxBorder to boxBorder 	 that see each other
		for(int i=0;i<boxBorder.size(); i++) {
			for(int j=i+1;j<boxBorder.size(); j++) {
				Point3D p1 = cf.GpsToPicsel(boxBorder.get(i), frame.getWidth(), frame.getHeight());
				Point3D p2= cf.GpsToPicsel(boxBorder.get(j), frame.getWidth(), frame.getHeight());
				Line2D line = new Line2D.Double(p1.x(), p1.y(),p2.x(), p2.y());
				for(Rectangle rec: pixelRecs) {
					if(line.intersects(rec)) {				
						inRec = true;
						break;
					}
				}
				if(!inRec) {		
					System.out.println("boxborder1:"+boxBorder.get(i).toString()+"boxborder2:"+boxBorder.get(j).toString());
					G.addEdge(""+i,""+j,boxBorder.get(i).distance3D(boxBorder.get(j))); 
				}
				inRec = false;
			}
		}
		
		//target to boxBorder that see each other
		for(int i=0;i<boxBorder.size();i++) {
			for(Rectangle rec: pixelRecs) {
				Point3D p1 = cf.GpsToPicsel(target, frame.getWidth(), frame.getHeight());
				Point3D p2= cf.GpsToPicsel(boxBorder.get(i), frame.getWidth(), frame.getHeight());
				Line2D line = new Line2D.Double(p1.x(), p1.y(),p2.x(), p2.y());
				if(line.intersects(rec)) {	
					inRec = true;
					break;
				}
			}
			if(!inRec) {
				System.out.println("target:"+target.toString()+"boxBorder:"+boxBorder.get(i).toString());
				G.addEdge(""+i,"b",target.distance3D(boxBorder.get(i))); 
			}
			inRec = false;
		}

		Graph_Algo graph_algo =new Graph_Algo();
		
		try
		{
		graph_algo.dijkstra(G, source);
		}
		catch (Exception e)
		{
			System.out.println();
		}

		Node b = G.getNodeByName(dest); 

		System.out.println("* Graph Demo for OOP_Ex4 *");
		System.out.println(b);
		System.out.println("Dist: "+b.getDist());
		ArrayList<String> shortestPath = b.getPath();

		for(int i=1;i<shortestPath.size();i++) {
			Point3D p1= new Point3D(boxBorder.get(Integer.parseInt(shortestPath.get(i))));
			path.add(p1);
		}
		path.add(target);
		return path;

	}
	/**checkWall - check if the path passes into a box.
	 * @param gps0 - the player point.
	 * @param gps1 - the target point.
	 * @return - boolean if it passes or not.
	 */
	public boolean checkWall(Point3D p1, Point3D p2) {
		p1 = new Point3D(cf.GpsToPicsel(p1, frame.getWidth(), frame.getHeight()));
		p2 = new Point3D(cf.GpsToPicsel(p2, frame.getWidth(), frame.getHeight()));
		Line2D newLine = new Line2D.Double(p1.x(), p1.y(),p2.x(), p2.y());
		for(Rectangle rec: pixelRecs) {
			if(newLine.intersects(rec)) {
				System.out.println("Check Wall: True");
				return true;
			}
		}
		System.out.println("Check Wall: False");
		return false;
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
			System.out.println("Rec number "+i+": ");
			//A:
			Point3D upperLeft = cf.GpsToPicsel((new Point3D(game.getBox(i).getMax().lat(),game.getBox(i).getMin().lon(),0)),frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(upperLeft))&&isIn(upperLeft)) {
				upperLeft.add(-1,-1);
					boxBorder.add(cf.PicselToGps(upperLeft, frame.getWidth(), frame.getHeight()));
				
			}
			//B:
			Point3D upperRight = cf.GpsToPicsel(new Point3D(game.getBox(i).getMax()), frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(upperRight)&&isIn(upperRight))) {
				upperRight.add(1,-1);
			//	if(isIn(upperRight))
					boxBorder.add(cf.PicselToGps(upperRight, frame.getWidth(), frame.getHeight()));
			}
			//C:
			Point3D lowerRight =cf.GpsToPicsel((new Point3D(game.getBox(i).getMin().lat(),game.getBox(i).getMax().lon(),0)), frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(lowerRight))&&isIn(lowerRight)) {
				lowerRight.add(1,1);
				//if(isIn(lowerRight))
					boxBorder.add(cf.PicselToGps(lowerRight, frame.getWidth(), frame.getHeight()));
			}
			//D:
			Point3D lowerLeft = cf.GpsToPicsel(new Point3D(game.getBox(i).getMin()), frame.getWidth(), frame.getHeight());
			if(!(ifPointIsInRectangle(lowerLeft))&&isIn(lowerLeft)) {
				lowerLeft.add(-1, 1);
				//if(isIn(lowerLeft))
					boxBorder.add(cf.PicselToGps(lowerLeft, frame.getWidth(), frame.getHeight()));
			//}
		}		
	}
	}

	/**ifPointIsInRectangle - check if point is in rectangle. */
	/**
	 * Check if the box's edge point is inside another box.
	 * @param p - the given point in GPS coordinate.
	 * @return TRUE - if the point is inside another box or FALSE otherwise.
	 */
	public boolean ifPointIsInRectangle(Point3D p) {
		Point3D pointPixel = cf.GpsToPicsel(p, frame.getWidth(), frame.getHeight());
		for(Rectangle rec: pixelRecs) {
			//			Rectangle rec = createPixelRec(i);
			//	System.out.println(rec.toString()+"\n");
			if(rec.contains((int)pointPixel.x(), (int)pointPixel.y())) {
				return true;
			}
		}
		return false;
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
	public Rectangle createPixelRec(int index) {
		double upperY, lowerY, leftX, rightX;
		//	if(game.getBox(index).getMax().y() > game.getBox(index).getMin().y()) {
		lowerY =  game.getBox(index).getMin().y();
		upperY =  game.getBox(index).getMax().y();
	
		rightX = game.getBox(index).getMin().x();
		leftX = game.getBox(index).getMax().x();
		
		Point3D C = cf.GpsToPicsel(new Point3D(rightX, upperY),frame.getWidth(), frame.getHeight()) ;
		Point3D A = cf.GpsToPicsel(new Point3D(leftX, lowerY), frame.getWidth(), frame.getHeight());
		
		Rectangle rec = new Rectangle((int)A.x(), (int)A.y(), Math.abs((int)C.x() - (int)A.x()), Math.abs((int)A.y() - (int)C.y()));
		return rec;
	}




	
	
		public static void main(String[] args) {
			MyFrame frame = new MyFrame();
			frame.setGame("data\\Ex4_OOP_example8.csv");

			AlgoPlayer ap = new AlgoPlayer(frame);
			Point3D p1 = new Point3D(526.0, 127.0, 0);
			Point3D P1= new Point3D(ap.cf.PicselToGps(p1, frame.getWidth(), frame.getHeight()));
			Point3D p2 = new Point3D(151.0, 354.0, 0.0);
			Point3D P2= new Point3D(ap.cf.PicselToGps(p2, frame.getWidth(), frame.getHeight()));
	
			Rectangle rec = new Rectangle(263, 186,317,217);
			ap.getPixelRecs().add(rec);
			System.out.println(ap.checkWall(P1, P2));
			Point3D p0 = new Point3D(478,335, 0);
			p1=	ap.getCf().PicselToGps(p0, frame.getWidth(), frame.getHeight());
			LatLonAlt l1= new LatLonAlt(p1.x(),p1.y(),0);
			Player p= new Player(l1,10);
			 int i  =ap.getClosestTarget(p);
			 LatLonAlt l2=  frame.get_game().getTarget(i).getLocation();
			 System.out.println( frame.get_game().getTarget(i).getLocation());
			 ap.createPixelRec(1);
			System.out.println(ap.getPixelRecs().get(0).getWidth());	
				System.out.println(ap.getPixelRecs().get(0).getHeight());
				System.out.println(ap.getPixelRecs().get(0).toString());
			System.out.println(ap.ifPointIsInRectangle(new Point3D(ap.cf.PicselToGps(new Point3D(20,30,0), frame.getWidth(), frame.getHeight()))));
	
		}
	public ConvertFactory getCf() {
		return cf;
	}
	public void setCf(ConvertFactory cf) {
		this.cf = cf;
	}
	public ArrayList<Rectangle> getPixelRecs() {
		return pixelRecs;
	}
	public void setPixelRecs(ArrayList<Rectangle> pixelRecs) {
		this.pixelRecs = pixelRecs;
	}
	public char getNextType() {
		return nextType;
	}
	public void setNextType(char nextType) {
		this.nextType = nextType;
	}
}