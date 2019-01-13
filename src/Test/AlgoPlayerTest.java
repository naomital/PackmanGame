package Test;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Rectangle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Algo.AlgoPlayer;
import Coords.LatLonAlt;
import GUI.MyFrame;
import Geom.Point3D;
import Robot.Packman;
import game_element.Player;

class AlgoPlayerTest {
	MyFrame frame;

	AlgoPlayer ap;

	public AlgoPlayerTest () {
		frame = new MyFrame();
		frame.setGame("data\\Ex4_OOP_example8.csv");
		ap = new AlgoPlayer(frame);
	}



	@Test
	public void Test_getClosestFruit() {
		Point3D p1 = new Point3D(478,335, 0);
		p1=	ap.getCf().PicselToGps(p1, frame.getWidth(), frame.getHeight());
		LatLonAlt l1= new LatLonAlt(p1.x(),p1.y(),0);
		Player p= new Player(l1,10);
		 int i  =ap.getClosestFruit(p);
		 LatLonAlt l2=  frame.get_game().getTarget(i).getLocation();
		 assertTrue(32.1037921397027>=l2.lat());
		 assertTrue(32.1027921397027<=l2.lat());
		 assertTrue(35.20645864973751 >=l2.lon());
		 assertTrue(35.20545864973751 <=l2.lon());
	}
	
	@Test
	public void Test_getClosestPackman() {
		Point3D p1 = new Point3D(478,335, 0);
		p1=	ap.getCf().PicselToGps(p1, frame.getWidth(), frame.getHeight());
		LatLonAlt l1= new LatLonAlt(p1.x(),p1.y(),0);
		Player p= new Player(l1,10);
		 int i  =ap.getClosestPackman(p);
		 LatLonAlt l2=  frame.get_game().getTarget(i).getLocation();
		 assertTrue(32.10538273737223>=l2.lat());
		 assertTrue(32.10438273737223<=l2.lat());
		 assertTrue(35.20820765024855 >=l2.lon());
		 assertTrue(35.20720765024855 <=l2.lon());
	}
	@Test
	public void Test_getClosestTarget() {
		Point3D p1 = new Point3D(478,335, 0);
		p1=	ap.getCf().PicselToGps(p1, frame.getWidth(), frame.getHeight());
		LatLonAlt l1= new LatLonAlt(p1.x(),p1.y(),0);
		Player p= new Player(l1,10);
		 int i  =ap.getClosestTarget(p);
		 LatLonAlt l2=  frame.get_game().getTarget(i).getLocation();
		 assertTrue(32.1037921397027>=l2.lat());
		 assertTrue(32.1027921397027<=l2.lat());
		 assertTrue(35.20645864973751 >=l2.lon());
		 assertTrue(35.20545864973751 <=l2.lon());
	}
	@Test
	public void TestCreatPixelRec() {
		ap.createPixelRec(1);
		assertTrue( 64.0>=ap.getPixelRecs().get(0).getWidth());
		assertTrue( 63.0<=ap.getPixelRecs().get(0).getWidth());
		assertTrue(501.0>=ap.getPixelRecs().get(0).getHeight());
		assertTrue(500.0<=ap.getPixelRecs().get(0).getHeight());
	}
	@Test
	public void TestCheckWall() {
		Point3D p1 = new Point3D(526.0, 127.0, 0);
		Point3D P1= new Point3D(ap.getCf().PicselToGps(p1, frame.getWidth(), frame.getHeight()));
		Point3D p2 = new Point3D(151.0, 354.0, 0.0);
		Point3D P2= new Point3D(ap.getCf().PicselToGps(p2, frame.getWidth(), frame.getHeight()));
		Rectangle rec = new Rectangle(263, 186,317,217);
		ap.getPixelRecs().add(rec);
		boolean answer=	ap.checkWall(P1, P2);
		assertTrue(answer);
	}
	@Test
	public void TestIfPointIsInRectangles() {
		assertFalse(ap.ifPointIsInRectangle(new Point3D(ap.getCf().PicselToGps(new Point3D(20,30,0), frame.getWidth(), frame.getHeight()))));
	}
	public void TestIsIn() {
	assertFalse(ap.isIn(new Point3D(1432,641)));
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
}
