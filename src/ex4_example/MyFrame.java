package ex4_example;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
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
import Coords1.ConvertFactory;
import Coords1.Map;
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
	private MenuItem openFile,menuPlayer, startGame;
	private int x ,y;
	private int option;
	private Packman player;
	private Game _game;
	private Image pacmanIcon, dountIcon, playerGui, goust;
    private ConvertFactory conv;

	public  MyFrame(){
		super("Game");
		double mapLongitudeStart =  35.202574, mapLatitudeStart = 32.106046;
		// length of map in long/lat
		double mapLongitude = 35.212405 -mapLongitudeStart, 
				// invert because it decreases as you go down
				mapLatitude = mapLatitudeStart - 32.101858  ;

		this.setLayout(new FlowLayout());
		this.setBounds(0, 0, 1300, 642);
		Dimension screenSize = new Dimension();
		screenSize.setSize(this.getWidth(), this.getHeight());
		this.setPreferredSize(screenSize);
		Point3D center = new Point3D(this.getWidth()/2, this.getHeight()/2, 0);
		System.out.println("CENTER1: "+center);
		LatLonAlt latlonalt = new LatLonAlt(center.ix(), center.iy(), 0);
		
		System.out.println("CENTER2: "+latlonalt.toString());
		conv= new ConvertFactory();
		map= new Map();
		this.image = map.getImg();
		this.setLayout(new FlowLayout());
		this.pacmanIcon = Toolkit.getDefaultToolkit().getImage("icons\\pacman.png");
		this.dountIcon = Toolkit.getDefaultToolkit().getImage("icons\\dount.png");
		this.playerGui=Toolkit.getDefaultToolkit().getImage("icons\\250px-133Eevee.png");
		this.goust=Toolkit.getDefaultToolkit().getImage("icons\\Gengar.png");
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
		startGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//play.start();
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
				//play = new Play(_game);
			}catch(Exception e) {}		
		}
		//play.setIDs(207642638, 313245268);


	}
	public synchronized void paint(Graphics g)
	{
		g.drawImage(image, 0, 0,this.getWidth(),this.getHeight(), this);
		
		
		if(player != null) {
			g.setColor(Color.PINK);
			Point3D p= conv.GpsToPicsel(player.getLocation(), this.getWidth(),this.getHeight() );
			g.drawImage(playerGui, (int)p.x(),(int) p.y(), this);
		//	g.fillOval((int)p.x(),(int)p.y(), 15, 15);
			System.out.println("test3: "+player.getLocation());

			System.out.println("x:"+player.getLocation().lat());
			System.out.println("y:"+player.getLocation().lon());

		//	g.drawOval(player.getLocation().ix(), player.getLocation().iy(), width, height);
			
		}
//                for(int i=0; i<_game.sizeB();i++) {
//                	_game.getBox(i).getMax().;
//                	g.fillRect(i, i, width, height);
//                }
if(_game!= null) {
//    if(_game.getTargets().size()>0) {
//		Iterator<Fruit> itr0= _game.getTargets().iterator();
//
//				//draw all the dounts to the screen: 
//				while(itr0.hasNext()) {
//					Fruit fruit = (Fruit)itr0.next();
//					Point3D p = map.world2frame(fruit.getLocation());  
//					g.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);
//					g.drawImage(goust, (int)p.x(),(int) p.y(), this);
//				}
//		
//				//draw all the pacman to the screen:
//		
//				System.out.println("****");
//    }
    if(_game.sizeR()>0) {
		Iterator<Packman> itr1 = _game.getRobots().iterator();

    
				while(itr1.hasNext()) 
				{
					Packman packman = (Packman)itr1.next();
				//	LatLonAlt lla = new LatLonAlt(packman.getLocation().lat(), packman.getLocation().lon(), 0);
					//System.out.println(lla);
					System.out.println("test1: "+packman.getLocation());

					Point3D p = conv.GpsToPicsel(packman.getLocation(), this.getWidth(),this.getHeight() );
					System.out.println(p);
					g.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);
					g.drawImage(pacmanIcon, (int)p.x(),(int) p.y(), this);
				}
		
				System.out.println("****");
    }
    if(_game.sizeG()>0) {
		Iterator<Packman> itr2 = _game.getGhosts().iterator();
		while(itr2.hasNext()) {
		Packman packman = (Packman)itr2.next();
		System.out.println("test2: "+packman.getLocation());
		Point3D p = conv.GpsToPicsel(packman.getLocation(), this.getWidth(),this.getHeight() );
		System.out.println(p);
		g.drawString("("+Integer.toString(x)+", "+Integer.toString(y)+")",x,y-10);
		g.drawImage(pacmanIcon, (int)p.x(),(int) p.y(), this);
    }
    }
}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		System.out.println("mouse clicked!");
		x = event.getX();
		y = event.getY();
		System.out.println("X: "+x+" , "+"Y: "+y);
		Point3D p = new Point3D(x, y,0);
		Point3D p1 =  conv.PicselToGps(p, this.getWidth(), this.getHeight());
		LatLonAlt latlonalt= new LatLonAlt(p1.x(),p.y(),p.z());
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
		f.initGui();
		f.setVisible(true);
	}


}
