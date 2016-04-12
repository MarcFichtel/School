package GreenhouseSimulator;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class keeps track of simulation time and updates display thereof
 */

// TODO Nice-to-have: Make a getTime() method which LogController can use
public class TimeController extends Thread {

	/**
	 * ui: The application's GUI
	 */
	private GUI ui;
	
	/**
	 * runTime: The simulation's run time
	 */
	private int runTime = 0;
	
	/**
	 * Constructor assigns given values
	 * @param ui: The application's GUI
	 */
	public TimeController (GUI ui) {
		this.ui = ui;
	}
	
	/**
	 * Update run time every second while simulation is running
	 */
	@Override
	public void run() {
		
		// Only update time when simulation is not paused
		while (Controller.getSimInProgress()) {
			
			// Use try/catch to cover any possible error exceptions
			try {
				
				// Increment runTime every second
				runTime++;
				
				// Convert runTime integer to a string
				int hours = 0;
				int minutes = 0;
				String runTimeString = "";
				
				hours = (runTime/60 - runTime/60/60);
				minutes = (runTime%60);

				// Adjust time display to show two digit zeros
				if (minutes < 10 && hours > 10) {
					runTimeString = hours + ":0" + minutes + ":00";
				
				} else if (minutes > 10 && hours < 10) {
					runTimeString = "0" + hours + ":" + minutes + ":00";
				
				} else if (minutes > 10 && hours > 10) {
					runTimeString = hours + ":" + minutes + ":00";
				
				} else {
					runTimeString = "0" + hours + ":0" + minutes + ":00";
				}
				
				// Display new run time in ui
				ui.setRunTime(runTimeString);
				
				// Wait a second before updating time again
				Thread.sleep(1000);
				
				// If simulation is paused, keep threads looping until it is resumed
				// TODO check if there's a better way to do this
				for (;;) {
					if (!Controller.getSimPaused()) {
						break;
					}
					Thread.sleep(1000);
				}
			
			// In case of errors, let user know something went wrong
			} catch (Exception ex) {
				
			}
		}
	}
}
