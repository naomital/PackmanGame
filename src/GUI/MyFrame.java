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
import GIS.GIS_element;

import Geom.Point3D;
import Robot.Fruit;
import Robot.Game;
import Robot.Packman;
import Robot.Play;

public class MyFrame extends JFrame implements MouseListener{

	private Map map;
	private LatLonAlt latlonalt;
	private BufferedImage image;
	private Play play;
	private JFileChooser openFileChosser;
	private MenuItem openFile,menuPlayer, startGame, startAlgo;
	private int x ,y;
	private int option;
	private Packman player;
	private Game _game;
	private Image pacmanIcon, dountIcon, playerGui, ghost;
	private ConvertFactory conv;
	private double ang = 0;
	private double azimut;
	private String info="" ;
	private Font font= new Font("Ariel",Font.BOLD,18);
	private AlgoPlayer algoPlayer;
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
		this.image = map.getImg();
		this.setLayout(new FlowLayout());
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");
		this.dountIcon = Toolkit.getDefaultToolkit().getImage("icons\\dount.png");
		this.playerGui=Toolkit.getDefaultToolkit().getImage("icons\\EeveePlayer.png");
		this.ghost=Toolkit.getDefaultToolkit().getImage("icons\\GengarGhost.png");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		openFileChosser= new JFileChooser();
		openFileChosser.setCurrentDirectory(new File("C:\\Users\\עדי\\eclipse-workspace\\Ex4_OOP"));
		pack();
	}
	public void initGui() {
		MenuBar menuBar = new MenuBar();
		Menu menu = new Menu("Menu");
		menuBar.add(menu); 

		//***open-game #1: 
		openFile = new MenuItem("open Game");
		menu.add(openFile); // the menu adds this item
		openFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openFile();
				repaint();

			}
		});
		//***add player #2: 
		menuPlayer = new MenuItem("add Player");
		menu.add(menuPlayer); // the menu adds this item
		menuPlayer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				option=1;
			}

		});

		//***start game #3: 
		startGame = new MenuItem("Start!");
		menu.add(startGame); // the menu adds this item
		this.addKeyListener(new keyListener()); // start listening for ENTER key
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play = new Play(_game);
				play.setIDs(207642638, 313245268);
				play.start(); // Defualt time is 100,000 milliseconds, default speed of player is 20
				Thread thread =new Thread() {
					public void run() {
						while(play.isRuning()) {
							play.rotate(azimut);
							try {
								Thread.sleep(50);
								repaint();
							}catch(Exception e){

							}
						}
						//play.stop();
						System.out.println("** Done Game (user stop) **");
						info = play.getStatistics();
						System.out.println(info);
					}
				};
				thread.start();



			}

		});
		//***start game #3: 
		startAlgo = new MenuItem("Start player's algorithm!");
		menu.add(startAlgo); // the menu adds this item
		this.addKeyListener(new keyListener()); // start listening for ENTER key
		startAlgo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play = new Play(_game);

				play.setIDs(207642638, 313245268);
				play.start(); // Default time is 100,000 milliseconds, default speed of player is 20
				Thread thread =new Thread() {
					public void run() {
						while(play.isRuning()) {
							azimut = algoPlayer.theNextStep();
							play.rotate(azimut);
							info = play.getStatistics();
							System.out.println(info);
							try {
								Thread.sleep(50);
								repaint();
							}catch(Exception e){

							}

						}
						//play.stop();
						System.out.println("** Done Game (user stop) **");



					}
				};
				thread.start();



			}

		});




		setMenuBar(menuBar);  // "this" JFrame sets its menu-bar	
		this.addMouseListener(this);

	}

	private void openFile() {
		int returnValue = openFileChosser.showOpenDialog(this);
		if(returnValue==JFileChooser.APPROVE_OPTION) {
			try {
				_game =  new Game(openFileChosser.getSelectedFile().getPath());

			}catch(Exception e) {}		
		}
		algoPlayer= new AlgoPlayer(this);
	}
	public synchronized void paint(Graphics g)
	{
		Image img = createImage(5000,5000);
		Graphics g1 = img.getGraphics();
		g1.drawImage(image, 0, 0,getWidth(),getHeight(), this);
		g1.setFont(font);
		g1.setColor(Color.white);
		g1.drawString(info,60,620);

		if(player != null) {
			Point3D p = new Point3D(player.getLocation().lat(), player.getLocation().lon(), player.getLocation().alt());
			Point3D p1= conv.GpsToPicsel(p, this.getWidth(),this.getHeight());
			System.out.println(p.toString());
			g1.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);
			g1.drawImage(playerGui,(int)p1.x(),(int)p1.y(), this);
			System.out.println("test3: "+player.getLocation());

			//	g.drawOval(player.getLocation().ix(), player.getLocation().iy(), width, height);

		}
		if(_game!= null) {
			if(_game.sizeB()>0) {
				double upperY, lowerY, leftX, rightX;

				//for testing the size: 
				System.out.println("Size of boxes: "+_game.sizeB());

				for(int i=0; i<_game.sizeB();i++) {

					System.out.println("BOX: "+_game.getBox(i).toString());

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

					//for testing with painter the pixels:
					System.out.println("P1: "+p5);
					System.out.println("P2 "+ p6);
					System.out.println("P3 "+p7);
					System.out.println("P4 "+p8);

					g1.setColor(Color.BLACK);
					g1.fillRect((int)p6.x(), (int)p6.y(), (int)p7.x() - (int)p6.ix(), (int)p7.y() - (int)p6.y());
				}
			}
			if(_game.getTargets().size()>0) {
				System.out.println("Size of Fruits: "+_game.getTargets().size());
				Iterator<Fruit> itr0= _game.getTargets().iterator();

				//draw all the dounts to the screen: 
				while(itr0.hasNext()) {
					Fruit fruit = (Fruit)itr0.next();
					Point3D p = conv.GpsToPicsel(fruit.getLocation(), this.getWidth(),this.getHeight() );
					g1.drawImage(dountIcon, (int)p.x(),(int) p.y(), this);
				}

				//draw all the pacman to the screen:

				System.out.println("**");
			}
			if(_game.sizeR()>0) {
				System.out.println("Size of Packmans: "+_game.sizeR());
				Iterator<Packman> itr1 = _game.getRobots().iterator();
				while(itr1.hasNext()) 
				{
					Packman packman = (Packman)itr1.next();
					//	LatLonAlt lla = new LatLonAlt(packman.getLocation().lat(), packman.getLocation().lon(), 0);
					//System.out.println(lla);
					System.out.println("test1: "+packman.getLocation());
					LatLonAlt lla = new LatLonAlt(packman.getLocation().lat(), packman.getLocation().lon(),packman.getLocation().alt());
					Point3D p = conv.GpsToPicsel(lla, this.getWidth(),this.getHeight() );
					System.out.println(p);
					g1.drawImage(pacmanIcon, (int)p.ix(),(int) p.iy(), this);
				}

				System.out.println("**");
			}
			if(_game.sizeG()>0) {
				System.out.println("Size of Ghosts: "+_game.getGhosts().size());
				g1.setColor(Color.green);

				Iterator<Packman> itr2 = _game.getGhosts().iterator();
				while(itr2.hasNext()) {
					Packman packman = (Packman)itr2.next();
					System.out.println("test2: "+packman.getLocation());
					Point3D p = conv.GpsToPicsel(packman.getLocation(), this.getWidth(),this.getHeight() );
					System.out.println(p);
					g1.drawImage(ghost, (int)p.x(),(int) p.y(), this);
				}
			}
		}

		g.drawImage(img,0,0,this);
	}

	@Override
	public void mouseClicked(MouseEvent event) {

		System.out.println("mouse clicked!");
		x = event.getX();
		y = event.getY();
		System.out.println("X: "+x+" , "+"Y: "+y);
		Point3D p = new Point3D(x, y,0);
		Point3D p1 =  conv.PicselToGps(p, this.getWidth(), this.getHeight());
		LatLonAlt latlonalt= new LatLonAlt(p1.x(),p1.y(),p1.z());
		if(option==1 && _game != null) {
			System.out.println("I am in");
			this.player = new Packman(latlonalt, 100.0D);
			_game.setPlayer(player);
			option=0;

		}

		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent event) {

	}



	@Override
	public void mouseExited(MouseEvent arg0) {}



	@Override
	public void mousePressed(MouseEvent event) {


	}



	@Override
	public void mouseReleased(MouseEvent event) {
		x = event.getX();
		y = event.getY();
		System.out.println("X: "+x+" , "+"Y: "+y);
		Point3D p = new Point3D(x, y,0);
		Point3D p1 =  conv.PicselToGps(p, this.getWidth(), this.getHeight());
		double [] flag= new MyCoords().azimuth_elevation_dist( _game.getPlayer().getLocation(),p1);
		this.azimut=(int) flag[0];


	}

	//	public void mousemove(MouseEvent event) {
	//		
	//	}

	public static void main(String[] args) {
		MyFrame f = new MyFrame();
		f.initGui();
		f.setVisible(true);
	}



	// /---Key Listener---///
	private class keyListener implements KeyListener
	{
		public void keyPressed(KeyEvent ke)
		{
			if (ke.getKeyCode() == KeyEvent.VK_ENTER)
			{
				ang = (ang + 1) % 10; // circle until 10. 
				System.out.println("** "+ang+"***");
			}
		}

		public void keyReleased(KeyEvent ke){}

		public void keyTyped(KeyEvent ke){}

	}

	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public Game get_game() {
		return _game;
	}
	public void set_game(Game _game) {
		this._game = _game;
	}




}