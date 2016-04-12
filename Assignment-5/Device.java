package GreenhouseSimulator;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class represents devices used to maintain the greenhouse environment
 */

public class Device {

	/**
	 * efficiency: The efficiency of the device (% or °C)
	 */
	private int efficiency;
	
	/**
	 * isDeviceActive: The device's state (idle or active)
	 */
	private boolean isDeviceActive;
	
	/**
	 * Get the device's efficiency
	 * @return efficiency: The device's efficiency
	 */
	public int getEfficiency () {
		return efficiency;
	}
	
	/**
	 * Set the device's efficiency
	 * @param newValue: The device's new efficiency
	 */
	public void setEfficiency (int newValue) {
		efficiency = newValue;
	}
	
	/**
	 * Get the device's state
	 * @return isDeviceActive: The device's state
	 */
	public boolean getDeviceActive () {
		return isDeviceActive;
	}
	
	/**
	 * Set the device's state
	 * @param bool: The device's new state
	 */
	public void setDeviceActive (boolean bool) {
		isDeviceActive = bool;
	}
}
