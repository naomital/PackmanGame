package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Algo.AlgoPlayer;
import Coords.LatLonAlt;
import Coords1.ConvertFactory;
import Coords1.Map;
import Coords1.MyCoords;

import Geom.Point3D;
import Robot.Play;
import game_element.Enemy;
import game_element.Fruit;
import game_element.Game;
import game_element.Ghost;
import game_element.Packman;
import game_element.Player;

/**
 * This class responsible for the graphic user interface.
 * @author Adi and Naomi
 */

public class MyFrame extends JFrame implements MouseListener{

	private Map map;
	private LatLonAlt latlonalt;
	private BufferedImage image;
	private Play play;
	private JFileChooser openFileChosser;
	private MenuItem openFile,menuPlayer, startGame, startAlgo;
	private int x ,y;
	private int option;
	private Game _game;
	private ConvertFactory conv;
	private double ang = 0;
	private double azimut;
	private String info="" ;
	private Font font= new Font("Ariel",Font.BOLD,18);
	private AlgoPlayer algoPlayer;
	private int numOfGame;
	private String points;
	private Image gameOver;
	
	/**
	 * constructor for MyFrame.
	 */
	public  MyFrame(){
		super("Game");
		this.setLayout(new FlowLayout());
		this.setBounds(0, 0, 1433, 642);
		Dimension screenSize = new Dimension();
		screenSize.setSize(1433, 642);
		this.setPreferredSize(screenSize);
		Point3D center = new Point3D(this.getWidth()/2, this.getHeight()/2, 0);
		System.out.println("CENTER1: "+center);
		LatLonAlt latlonalt = new LatLonAlt(center.ix(), center.iy(), 0);
		System.out.println("CENTER2: "+latlonalt.toString());
		conv= new ConvertFactory();
		map = new Map();
		
		//set icons for the game: 
		this.image = map.getImg();
		this.gameOver=Toolkit.getDefaultToolkit().getImage("icons\\gameover.jpeg");
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		openFileChosser= new JFileChooser();
		openFileChosser.setCurrentDirectory(new File("C:\\Users\\eclipse-workspace\\Ex4_OOP"));
		pack();
	}
	
	/**
	 * This function creates the menu.
	 */
	public void initGui() {
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		menuBar.add(menu); 

		//open-game #1: This button opens a new CSV game file and repaint it to the game board.  
		openFile = new MenuItem("open Game");
		menu.add(openFile); // the menu adds this item
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
				repaint();
			}
		});
		
		//add player #2: This button selects a position for our player.
		menuPlayer = new MenuItem("add Player");
		menu.add(menuPlayer); // the menu adds this item
		menuPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				option=1;
			}

		});

		//start game #3: This button opens a game that moves the player by mouse clickes.
		startGame = new MenuItem("Start!");
		menu.add(startGame); // the menu adds this item
		this.addKeyListener(new keyListener()); // start listening for ENTER key
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play.setIDs(207642638, 313245268);
				numOfGame=play.getHash1();
				System.out.println("game name:"+numOfGame);
				play.start(); // Defualt time is 100,000 milliseconds, default speed of player is 20
				Thread thread =new Thread() {
					public void run() {
						while(play.isRuning()) {
							play.rotate(azimut);
							_game.addStringGame(play.getBoard()); //update _game objs
							try {
								Thread.sleep(50);
								repaint();
							}catch(Exception e){

							}
						}
						System.out.println("* Done Game (user stop) *");
						info = play.getStatistics() ;
						System.out.println(info);
					}
				};
				thread.start();
				
			}
		});
		
		//start game #3: This button activates a game that uses the algorithm we have built for the player

		startAlgo = new MenuItem("Start player's algorithm!");
		menu.add(startAlgo); // the menu adds this item
		this.addKeyListener(new keyListener()); // start listening for ENTER key
		startAlgo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				ArrayList<Point3D> path=new ArrayList<Point3D> ();
				path.addAll( algoPlayer.theNextStep());
				play.setIDs(207642638, 313245268);
				numOfGame=play.getHash1();
				play.start(); // Default time is 100,000 milliseconds, default speed of player is 20
				Thread thread =new Thread() {
					public void run() {
						while(play.isRuning()) {
							if(wasEaten(path)) {
								path.remove(0);
							}
							if(path.size()==0) {
								path.addAll( algoPlayer.theNextStep());
							}
							azimut=theNextAzimut(path);
							play.rotate(azimut);
							_game.addStringGame(play.getBoard());
							info = play.getStatistics();
							System.out.println(info);
							try {
								Thread.sleep(50);
								repaint();
							}catch(Exception e){

							}
						}
						 points=new Comparisons().Points(numOfGame);
						 option =2;
					}
				};
				thread.start();
			}
		});
		setMenuBar(menuBar);  // "this" JFrame sets its menu-bar	
		this.addMouseListener(this);
	}

	/**
	 * This function checks if the player reach the first destination of the path. 
	 * @param path - the current walk path for the player. 
	 * @return True - if the player reached the first destination of the path. False - if not. 
	 */
	private boolean wasEaten(ArrayList<Point3D> path) {
		boolean isEat = false; 
		if(_game.getPlayer().getLocation().lat() > path.get(0).x()- (0.00001) &&_game.getPlayer().getLocation().lat() < path.get(0).x() + (0.00001)
				&&_game.getPlayer().getLocation().lon() > path.get(0).y()- (0.00001) && _game.getPlayer().getLocation().lon() < path.get(0).y() + (0.00001)) {
			isEat = true;
		}
		return isEat;
	}
	
	/**
	 * This function calculate and return the next azimut to the player's target. 
	 * @param path - the player's current walking path.
	 * @return
	 */
	private double theNextAzimut(ArrayList<Point3D> path) {

		double [] angle= new MyCoords().azimuth_elevation_dist( _game.getPlayer().getLocation(),path.get(0));
		return (int) angle[0];
	}
	
	/**
	 * This function open file from CSV game and set new game base of it.
	 */
	private void openFile() {
		int returnValue = openFileChosser.showOpenDialog(this);
		if(returnValue==JFileChooser.APPROVE_OPTION) {
			try {
				_game =  new Game(openFileChosser.getSelectedFile().getPath());
				
				play = new Play(openFileChosser.getSelectedFile().getPath());

			}catch(Exception e) {}		
		}
		algoPlayer= new AlgoPlayer(this);
	}
	
	/**
	 * This function paints the game on the map with all it's elements.
	 */
	public synchronized void paint(Graphics g)
	{
		Image img = createImage(5000,5000);
		Graphics g1 = img.getGraphics();
		g1.drawImage(image, 0, 0,getWidth(),getHeight(), this);
		
		if(_game!= null) {
			if(_game.sizeB()>0) {
				double upperY, lowerY, leftX, rightX;

				//for testing the size: 
				//	System.out.println("Size of boxes: "+_game.sizeB());

				for(int i=0; i<_game.sizeB();i++) {

					//	System.out.println("BOX: "+_game.getBox(i).toString());

					//check size of the rectangle: 
					if(_game.getBox(i).getMax().y() > _game.getBox(i).getMin().y()) {
						upperY =  _game.getBox(i).getMin().y();
						lowerY =  _game.getBox(i).getMax().y();
					}
					else {
						upperY =  _game.getBox(i).getMax().y();
						lowerY =  _game.getBox(i).getMin().y();
					}
					if(_game.getBox(i).getMax().x() > _game.getBox(i).getMin().x()) {
						leftX = _game.getBox(i).getMin().x();
						rightX = _game.getBox(i).getMax().x();
					}
					else {
						rightX = _game.getBox(i).getMin().x();
						leftX = _game.getBox(i).getMax().x();
					}

					Point3D p1 = new Point3D(leftX, upperY, 0);
					Point3D p2 = new Point3D(rightX, upperY, 0);
					Point3D p3 = new Point3D(leftX, lowerY, 0);
					Point3D p4 = new Point3D(rightX, lowerY, 0);

					Point3D p5 = conv.GpsToPicsel(p1, this.getWidth(), this.getHeight());
					Point3D p6 = conv.GpsToPicsel(p2, this.getWidth(), this.getHeight());
					Point3D p7 = conv.GpsToPicsel(p3, this.getWidth(), this.getHeight());
					Point3D p8 = conv.GpsToPicsel(p4, this.getWidth(), this.getHeight());
					g1.setColor(Color.BLACK);
					g1.fillRect((int)p6.x(), (int)p6.y(), (int)p7.x() - (int)p6.ix(), (int)p7.y() - (int)p6.y());
				}
			}
			if(_game!= null) {
				if(_game.getPlayer()!=null) {
					Point3D p = new Point3D(_game.getPlayer().getLocation().lat(), _game.getPlayer().getLocation().lon(), _game.getPlayer().getLocation().alt());
					Point3D p1= conv.GpsToPicsel(p, this.getWidth(),this.getHeight());
					//	System.out.println(p.toString());
					g1.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);
					g1.drawImage(_game.getPlayer().getImage(),(int)p1.x()-20,(int)p1.y()-20, this);
				}
			}
			if(_game.getTargets().size()>0) {
				//draw all the dounts to the screen: 
			for (Fruit fruit : _game.getTargets() ) {
					Point3D p = conv.GpsToPicsel(fruit.getLocation(), this.getWidth(),this.getHeight() );
					g1.drawImage(fruit.getImage(), (int)p.x(),(int) p.y(), this);
				}
				
			}
			if(_game.sizeR()>0) {
				//draw all the pacman to the screen:
				System.out.println("**");
				
				for(Enemy packman :_game.getRobots()) 
				{
					if(packman!=null) {
					System.out.println("test1: "+packman.getLocation());
					LatLonAlt lla = new LatLonAlt(packman.getLocation().lat(), packman.getLocation().lon(),packman.getLocation().alt());
					Point3D p = conv.GpsToPicsel(lla, this.getWidth(),this.getHeight() );
					g1.drawImage(packman.getImage(), (int)p.ix(),(int) p.iy(), this);
				}
				}
				System.out.println("**");
			}
			if(_game.sizeG()>0) {
				g1.setColor(Color.green);
				for(Ghost ghost:_game.getGhosts()) {
					if(ghost!=null) {
					Point3D p = conv.GpsToPicsel(ghost.getLocation(), this.getWidth(),this.getHeight() );
					System.out.println(p);
					g1.drawImage(ghost.getImage(), (int)p.x(),(int) p.y(), this);
					}
				}
			}
		}
		if(option ==2) {
			g1.setFont(font);
			g1.setColor(Color.white);
			g1.drawImage(this.gameOver, 10, 10, 1433, 642,this);
			g1.drawString(points,270,180);
			System.out.println(points);
		}

		g1.setFont(font);
		g1.setColor(Color.white);
		g1.drawString(info,60,620);

		g.drawImage(img,0,0,this);
	}

	/**
	 * This function responds to mouse clicks and set the player's location based to the clickes. 
	 */
	@Override
	public void mouseClicked(MouseEvent event) {
		System.out.println("mouse clicked!");
		x = event.getX();
		y = event.getY();
		Point3D p = new Point3D(x, y,0);
		Point3D p1 =  conv.PicselToGps(p, this.getWidth(), this.getHeight());
		LatLonAlt latlonalt= new LatLonAlt(p1.x(),p1.y(),p1.z());
		if(option==1 && _game != null) {
			_game.setPlayer( new Player(latlonalt, 100.0D));
			play.setInitLocation(_game.getPlayer().getLocation().lat(),_game.getPlayer().getLocation().lon());
			option=0;
		}
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent event) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent event) {}
	/**
	 * This function calculate and update the current azimut as the mouse been pressed.
	 */
	@Override
	public void mouseReleased(MouseEvent event) {
		x = event.getX();
		y = event.getY();
		Point3D p = new Point3D(x, y,0);
		Point3D p1 =  conv.PicselToGps(p, this.getWidth(), this.getHeight());
		double [] flag= new MyCoords().azimuth_elevation_dist( _game.getPlayer().getLocation(),p1);
		this.azimut=(int) flag[0];
	}

	// /---Key Listener---///
	private class keyListener implements KeyListener{
		public void keyPressed(KeyEvent ke)
		{
			if (ke.getKeyCode() == KeyEvent.VK_ENTER)
			{
				ang = (ang + 1) % 10; // circle until 10. 
				System.out.println("* "+ang+"**");
			}
		}
		public void keyReleased(KeyEvent ke){}
		public void keyTyped(KeyEvent ke){}
	}
	public Map getMap() {	return map;}
	public void setMap(Map map) {this.map = map;}
	public Game get_game() {return _game;}
	public void set_game(Game _game) {this._game = _game;}
	public static void main(String[] args) {
		MyFrame f = new MyFrame();
		f.initGui();
		f.setVisible(true);
	}
	public void setGame(String g) {
		this._game= new Game(g);
	}
}