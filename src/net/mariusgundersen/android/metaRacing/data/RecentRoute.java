package net.mariusgundersen.android.metaRacing.data;

public class RecentRoute {

	private List<Point> points = new List<Point>(10);
	
	public void addPoint(Point point){
		points.add(point);
	}
	
	public double speed(){
		Point old = points.get(0);
		Point now = points.get(-1);
		return old.speedTo(now);
	}
	
	public double bearing(){
		Point old = points.get(0);
		Point now = points.get(-1);
		return old.bearingTo(now);
	}
	
}
