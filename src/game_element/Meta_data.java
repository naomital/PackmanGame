package game_element;

import Geom.Point3D;
/**
 * This interface responsible on the information of each element on the map. 
 * @author Adi and Naomi.
 *
 */
public abstract interface Meta_data
{
  public abstract long getUTC();
  public abstract String toString();
  public abstract Point3D get_Orientation();
}

