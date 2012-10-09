package net.mariusgundersen.android.metaRacing.data;

public class Delta {
	private double _speed;
	private double _bearing;

	public Delta(Point from, Point to) {
		this._speed = from.speedTo(to);
		this._bearing = from.bearingTo(to);
		while(this._bearing < 0){
			this._bearing += 360;
		}
	}
	
	
	public double speed(){
		return _speed;
	}
	
	public double bearing(){
		return _bearing;
	}
}
