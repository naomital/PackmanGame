package ex4_example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import Coords.LatLonAlt;
import Coords.Map;
import GIS.GIS_element;

import Geom.Point3D;
import Robot.Game;
import Robot.Packman;
import Robot.Play;

public class MyFrame extends JFrame implements MouseListener{

	private Map map;
	private LatLonAlt latlonalt;
	private BufferedImage image;
	private Play play;
	private JFileChooser openFileChosser;
	private MenuItem openFile,menuPlayer, startGame;
	private int x ,y;
	private int option;
	private Packman player;
	private Game _game;

	public  MyFrame(){
		super("unicord");
		double mapLongitudeStart =  35.202574, mapLatitudeStart = 32.106046;
		// length of map in long/lat
		double mapLongitude = 35.212405 -mapLongitudeStart, 
				// invert because it decreases as you go down
				mapLatitude = mapLatitudeStart - 32.101858  ;

		this.setLayout(new FlowLayout());
		Dimension screenSize = new Dimension();
		screenSize.setSize(mapLatitude, mapLongitude);
		this.setPreferredSize(screenSize);
		latlonalt = new	LatLonAlt(screenSize.width/2, screenSize.height/2, 0);
		map= new Map(latlonalt,mapLatitude, mapLongitude, "Ariel.png");
		this.image = map.getImage();
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		openFileChosser= new JFileChooser();
		openFileChosser.setCurrentDirectory(new File("C:\\Users\\n&e\\eclipse-workspace\\Ex4_OOP"));
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
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				play.start();
			}

		});



		setMenuBar(menuBar);  // "this" JFrame sets its menu-bar	
		this.addMouseListener(this);

	}

	private void openFile() {
		int returnValue = openFileChosser.showOpenDialog(this);
		if(returnValue==openFileChosser.APPROVE_OPTION) {
			try {
				_game =  new Game(openFileChosser.getSelectedFile().getPath());
				play = new Play(_game);
			}catch(Exception e) {}		
		}
		play.setIDs(207642638, 313245268);


	}
	public synchronized void paint(Graphics g)
	{
		g.drawImage(image, 0, 0,this.getWidth(),this.getHeight(), this);
		if(player != null) {
			g.setColor(Color.PINK);
			g.fillOval(player.getLocation().ix(), player.getLocation().iy(), 15, 15);
		//	g.drawOval(player.getLocation().ix(), player.getLocation().iy(), width, height);
			
		}


		//		Iterator<GIS_element> itr0= play.    game.getFruits().iterator();
		//		Iterator<GIS_element> itr1 = game.getPacmans().iterator();
		//
		//
		//		//draw all the dounts to the screen: 
		//		while(itr0.hasNext()) {
		//			Fruit fruit = (Fruit)itr0.next();
		//			Point3D p = this.map.getCf().GpsToPicsel(fruit.getPoint(), this.getWidth(), this.getHeight());
		//			g.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);
		//			g.drawImage(dountIcon, (int)p.x(),(int) p.y(), this);
		//		}
		//
		//		//draw all the pacman to the screen:
		//
		//		System.out.println("****");
		//
		//		while(itr1.hasNext()) 
		//		{
		//			Pacman pacman = (Pacman)itr1.next();
		//			Point3D p = this.map.getCf().GpsToPicsel(new Point3D(pacman.getPoint()), this.getWidth(), this.getHeight());
		//			System.out.println(p);
		//			g.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);
		//			g.drawImage(pacmanIcon, (int)p.x(),(int) p.y(), this);
		//		}
		//
		//		System.out.println("****");


	}

	@Override
	public void mouseClicked(MouseEvent event) {
		System.out.println("mouse clicked!");
		x = event.getX();
		y = event.getY();
		System.out.println("X: "+x+" , "+"Y: "+y);
		Point3D p = new Point3D(x, y,0);
		LatLonAlt latlonalt = map.frame2world(p);
		
		if(option==1 && player == null) {
			System.out.println("I am in");
			player = new Packman(latlonalt, 100.0D);
			_game.setPlayer(player);
			option=0;
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mousePressed(MouseEvent arg0) {

	}



	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		MyFrame f = new MyFrame();
		f.setBounds(0, 0, 1433, 642);
		f.initGui();
		f.setVisible(true);
	}


}
