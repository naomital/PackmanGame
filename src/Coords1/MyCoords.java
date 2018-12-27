package Coords1;

import java.util.Vector;

import Coords.coords_converter;
import Geom.Point3D;
/**
 * class that does calculations and conversions on points.
 * @author Naomi and Adi
 *
 */
public class MyCoords implements coords_converter  {
	private final int EarthRadius=6371000;
	private final double LonNorm=0.847091;
/** function add: receives a  gps point and meter point,
 *  converts the gps point to meter and add the point together.
 * @param gps - gps point
 * @param local_vector_in_meter - vector in meter*/
	@Override
	public Point3D add(Point3D gps, Point3D local_vector_in_meter) {
		Point3D ans=trans_gpsToMeter(gps);
		ans.add(local_vector_in_meter);
		return ans;
	}
/** function vector3D: receives two  gps point, calculates their distance and returns the distance in meters
 * @param gps0 - gps point number  1
 * @param gps1 - gps point number 2
 * @return double that are the distance in meters.*/
	@Override
	public double distance3d(Point3D gps0, Point3D gps1) {
		Point3D p_meter=vector3D(gps0,gps1) ;
		double ans= Math.sqrt(Math.pow(p_meter.x(),2)+Math.pow(p_meter.y(),2));
		return ans;
	}
/**function vector3D: receives two  gps point, calculates their distance and returns it as a vector in meters.
 * @param gps0 - gps point number  1
 * @param gps1 - gps point number 2
 * @return Point3D vector in meters that are the distance.*/
	@Override
	public Point3D vector3D(Point3D gps0, Point3D gps1) {
	    double lonNorm= Math.cos(gps0.x()*Math.PI/180);
		Point3D p1= new Point3D(gps1.x() - gps0.x(),gps1.y() -gps0.y());
		double radian=(p1.x()*Math.PI)/180;
		double meterX= Math.sin(radian)*EarthRadius;
		 radian=(p1.y()*Math.PI)/180;
		 double meterY= Math.sin(radian)*EarthRadius*lonNorm;
		return new Point3D(meterX,meterY,p1.z());
	}
/**function azimuth_elevation_dist: Gets points and converts the distance between them to the cordenta
 * @param gps0 - gps point number  1
 * @param gps1 - gps point number 2
 * @returnzimuth  double[]:[0]:azimuth,[1]: elevation (pitch),[2]: distance. */
	@Override
	public  double[] azimuth_elevation_dist(Point3D gps0, Point3D gps1) {
		double [] ans = new double [3];
		ans[0]=bearing(gps0.x(),gps0.y(),gps1.x(),gps1.y());//azimuth
		double z= (gps1.z()-gps0.z());
		ans[2]=distance3d(gps0,gps1);//dist
		ans[1]=(180/Math.PI)*((z/ans[2])-(ans[2]/(2*EarthRadius)));//elevation
		return ans;
	}
/** function isValid_GPS_Point: Gets a point and checks whether it is a  gps point
 * @param p - A point we want to check
 * @return true if its right and false if its wrong.*/
	@Override
	public boolean isValid_GPS_Point(Point3D p) {
		boolean flg=true;
		if(!(-180<p.x()&&p.x()<180))flg=false;
		if(!(-90<p.y()&&p.y()<90))flg=false;
		if(!(-450<p.z()&&p.z()>450))flg=false;
		return flg;
	}
	//private function that converts a gps point to a point in meters.
	private  Point3D trans_gpsToMeter(Point3D gps) {
		double x,y,z=gps.z();
		x=Math.sin((gps.x()*Math.PI)/180)*EarthRadius;
		y=Math.sin((gps.y()*Math.PI)/180)*EarthRadius*LonNorm;
		Point3D ans=new Point3D (x,y,z);
		return ans;
	}
	//protected function that calculates the azimuth, is taken from the following link-https://stackoverflow.com/questions/9457988/bearing-from-one-coordinate-to-another/29471137
	protected static double bearing(double lat1, double lon1, double lat2, double lon2){
		  double longitude1 = lon1;
		  double longitude2 = lon2;
		  double latitude1 = Math.toRadians(lat1);
		  double latitude2 = Math.toRadians(lat2);
		  double longDiff= Math.toRadians(longitude2-longitude1);
		  double y= Math.sin(longDiff)*Math.cos(latitude2);
		  double x=Math.cos(latitude1)*Math.sin(latitude2)-Math.sin(latitude1)*Math.cos(latitude2)*Math.cos(longDiff);

		  return (Math.toDegrees(Math.atan2(y, x))+360)%360;
		}
	
	
	

}
