package net.mariusgundersen.android.metaRacing.application;

import net.mariusgundersen.android.metaRacing.data.Delta;
import net.mariusgundersen.android.metaRacing.data.RunningAverage;
import net.mariusgundersen.useful.Lambda;

public class RunningAverageBearing{
	
	private static final double TO_RAD = Math.PI / 180;
	private int windowSize;

	RunningAverage<Delta> sinBearing = new RunningAverage<Delta>(windowSize, new Lambda<Delta, Double>() {public Double _(Delta argument) {
			return Math.sin(argument.bearing()*TO_RAD);
		}});
	RunningAverage<Delta> cosBearing = new RunningAverage<Delta>(windowSize, new Lambda<Delta, Double>() {public Double _(Delta argument) {
		return Math.cos(argument.bearing()*TO_RAD);
	}});

	public RunningAverageBearing(int windowSize) {
		this.windowSize = windowSize;
	}
	
	public double eventOccured(Delta event){
		double sinSum = sinBearing.eventOccured(event)*sinBearing.size();
		double cosSum = cosBearing.eventOccured(event)*cosBearing.size();
		
		return Math.atan2(sinSum, cosSum)/TO_RAD;
	}
	
	public double average(){

		double sinSum = sinBearing.average()*sinBearing.size();
		double cosSum = cosBearing.average()*cosBearing.size();
		
		return Math.atan2(sinSum, cosSum)/TO_RAD;
	}

}
