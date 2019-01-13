package game_element;


	import Coords.GeoBox;
	import Coords.LatLonAlt;
	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.PrintStream;
	import java.io.Serializable;
	import java.util.ArrayList;



	public class Game
	  implements Serializable
	{
	  private Player _player;
	  private ArrayList<Enemy> _robots;
	  private ArrayList<Ghost> _ghosts;
	  private ArrayList<Fruit> _targets;
	  private ArrayList<GeoBox> _boxes;
	  
	  public Game()
	  {
	    _robots = new ArrayList();
	    _ghosts = new ArrayList();
	    _targets = new ArrayList();
	    _boxes = new ArrayList();
	    _player = new Player(new LatLonAlt(0.0D, 0.0D, 0.0D), 2.0D);
	  }
	  
	  public Game(String f) { this();
	    try {
	      BufferedReader br = new BufferedReader(new FileReader(f));
	      String header = br.readLine();
	      


	      String line = br.readLine();
	      while (line != null) {
	        if (line.startsWith("M")) {
	          _player = new Player(line);
	        }
	        if (line.startsWith("P")) {
	          Packman p = new Enemy(line);
	          _robots.add(( Enemy)p);
	        }
	        if (line.startsWith("G")) {
	          Packman p = new Ghost(line);
	          _ghosts.add((Ghost)p);
	        }
	        if (line.startsWith("F")) {
	          Fruit ff = new Fruit(line);
	          _targets.add(ff);
	        }
	        if (line.startsWith("B")) {
	          GeoBox ff = new GeoBox(line);
	          _boxes.add(ff);
	        }
	        
	        line = br.readLine();
	      }
	      br.close();
	    }
	    catch (IOException e)
	    {
	      e.printStackTrace();
	      System.out.println("Exception ");
	    }
	  }
	  
	  public Game(Game g) { this();
	    for (int i = 0; i < _robots.size(); i++)
	      _robots.add(new Enemy((Enemy)_robots.get(i)));
	    for (int i = 0; i < _ghosts.size(); i++)
	      _ghosts.add(new Ghost(_ghosts.get(i)));
	    for (int i = 0; i < _targets.size(); i++)
	      _targets.add(new Fruit((Fruit)_targets.get(i)));
	    for (int i = 0; i < _boxes.size(); i++)
	      _boxes.add(new GeoBox((GeoBox)_boxes.get(i))); }
	  
	  public ArrayList<Enemy> getRobots() { return _robots; }
	  public ArrayList<Ghost> getGhosts() { return _ghosts; }
	  public Packman getGhosts(int i) { return (Packman)_ghosts.get(i); }
	  public ArrayList<Fruit> getTargets() { return _targets; }
	  public void add(Packman r) { _robots.add((Enemy)r); }
	  public void add(Fruit f) { _targets.add(f); }
	  public void add(GeoBox b) { _boxes.add(b); }
	  public int sizeR() { return _robots.size(); }
	  public int sizeG() { return _ghosts.size(); }
	  public int sizeT() { return _targets.size(); }
	  public int sizeB() { return _boxes.size(); }
	  public Packman getPackman(int i) { return (Packman)_robots.get(i); }
	  public Player getPlayer() { return _player; }
	  public Fruit getTarget(int i) { return (Fruit)_targets.get(i); }
	  public void removeTarget(int i) { _targets.remove(i); }
	  public void removeRobot(int i) { _robots.remove(i); }
	  public GeoBox getBox(int i) { return (GeoBox)_boxes.get(i); }
	  
	  public void clear() { _robots.clear();
	    _targets.clear();
	    _boxes.clear();
	    _ghosts.clear();
	    _player = null;
	  }
	  
	  public ArrayList<String> getGame() { ArrayList<String> ans = new ArrayList();
	    if (_player != null) ans.add(_player.toString1());
	    for (int i = 0; i < _robots.size(); i++) {
	      ans.add(((Packman)_robots.get(i)).toString());
	    }
	    for (int i = 0; i < _ghosts.size(); i++) {
	      ans.add(((Packman)_ghosts.get(i)).toString2());
	    }
	    for (int i = 0; i < _targets.size(); i++) {
	      ans.add(((Fruit)_targets.get(i)).toString());
	    }
	    for (int i = 0; i < _boxes.size(); i++) {
	      ans.add(((GeoBox)_boxes.get(i)).toString());
	    }
	    return ans;
	  }
	  public String getStringGame() {
		  String answer = "";
		ArrayList<String> gameString=   this.getGame();
		  for(int i=0 ;i<gameString.size();i++) {
			  answer+= gameString.get(i)+",";
		  }
		  return answer;
	  }
	  public void setPlayer(Player p) { _player = p; }
	  
	  public void addStringGame(ArrayList<String> Stringame) {	 
		  _robots = new ArrayList();
		    _ghosts = new ArrayList();
		    _targets = new ArrayList();
		//    _boxes = new ArrayList();
		    _player = new Player(new LatLonAlt(0.0D, 0.0D, 0.0D), 2.0D);
		  for(int i=0;i<Stringame.size();i++) {
			   if (Stringame.get(i).startsWith("M")) {
				   System.out.println(Stringame.get(i));
			          _player = new Player(Stringame.get(i));
			        }
			        if (Stringame.get(i).startsWith("P")) {
			        	Enemy p = new Enemy(Stringame.get(i));
			          _robots.add(p);
			        }
			        if (Stringame.get(i).startsWith("G")) {
			          Packman p = new Ghost(Stringame.get(i));
			          _ghosts.add((Ghost)p);
			        }
			        if (Stringame.get(i).startsWith("F")) {
			          Fruit ff = new Fruit(Stringame.get(i));
			          _targets.add(ff);
			        }
//			        if (Stringame.get(i).startsWith("B")) {
//			          GeoBox ff = new GeoBox(Stringame.get(i));
//			          _boxes.add(ff);
//			        }
			       
		  }
	  }
	  public void addGhost(Packman r) { _ghosts.add((Ghost)r); }
	}


