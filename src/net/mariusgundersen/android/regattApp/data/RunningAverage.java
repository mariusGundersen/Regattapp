package net.mariusgundersen.android.regattApp.data;

import net.mariusgundersen.useful.Lambda;

public class RunningAverage<T> {

	private List<T> history;
	private double sumOfHistory;
	private final Lambda<T, Double> select;

	public RunningAverage(int windowSize, Lambda<T, Double> select) {
		this.select = select;
		this.history = new List<T>(windowSize);
	}
	
	public double eventOccured(T value){
		if(history.isFull()){
			sumOfHistory -= select._(history.get(0));
		}
		sumOfHistory += select._(value);
		history.add(value);
		
		return sumOfHistory/history.size();
	}
	
	public double average(){
		return sumOfHistory/history.size();
	}
	
	public int size(){
		return history.size();
	}
}
