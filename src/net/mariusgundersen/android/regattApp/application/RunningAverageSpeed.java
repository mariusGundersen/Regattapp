package net.mariusgundersen.android.regattApp.application;

import net.mariusgundersen.android.regattApp.data.Delta;
import net.mariusgundersen.android.regattApp.data.RunningAverage;
import net.mariusgundersen.useful.Lambda;

public class RunningAverageSpeed extends RunningAverage<Delta> {

	public RunningAverageSpeed(int windowSize) {
		super(windowSize, 
				new Lambda<Delta, Double>() {public Double _(Delta argument) {
					return argument.speed();
				}
			});
	}

}
