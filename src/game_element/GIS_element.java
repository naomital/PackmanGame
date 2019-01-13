package game_element;

import Geom.Geom_element;
import Geom.Point3D;
/**
 * This interface represent element in the game.
 * there are different types of elements: Packman, Fruit, Ghost, etc...
 * @author Naomi and Adi.
 *
 */
public abstract interface GIS_element
{
  public abstract Geom_element getGeom();
  
  public abstract Meta_data getData();
  
  /** this function takes GPS point and translate it to X,Y,Z */
  public abstract void translate(Point3D paramPoint3D);
}

