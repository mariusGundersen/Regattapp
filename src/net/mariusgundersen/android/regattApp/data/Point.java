package net.mariusgundersen.android.regattApp.data;

import android.location.Location;

public class Point extends Location{

	public Point(Location l) {
		super(l);
	}
	
	public double speedTo(Point to){
		if(this == to)
			return 0.0;
		double distance = this.distanceTo(to);
		long time = Math.abs(to.getTime() - this.getTime())/1000;
		
		return distance/time;
	}
}
