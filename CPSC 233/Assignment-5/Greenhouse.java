package GreenhouseSimulator;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class represents the greenhouse environment
 */

public class Greenhouse {

	/**
	 * The greenhouse's temperature
	 */
	private int temperature;
	
	/**
	 * The greenhouse's humidity
	 */
	private int humidity;
	
	/**
	 * The greenhouse's soil moisture
	 */
	private int soilMoisture;
	
	/**
	 * The weather
	 */
	private int weatherIndex;
	
	/**
	 * Get the greenhouse's temperature
	 * @return temperature: The current temperature
	 */
	public int getTemperature () {
		return temperature;
	}
	
	/**
	 * Set the greenhouse's temperature
	 * @param newValue: The new temperature
	 */
	public void setTemperature (int newValue) {
		temperature = newValue;
	}
	
	/**
	 * Get the greenhouse's humidity
	 * @return humidity: The current humidity
	 */
	public int getHumidity () {
		return humidity;
	}
	
	/**
	 * Set the greenhouse's humidity
	 * @param newValue: The new humidity
	 */
	public void setHumidity (int newValue) {
		humidity = newValue;
	}
	
	/**
	 * Get the greenhouse's soil moisture
	 * @return soilMoisture: The current soil moisture
	 */
	public int getSoilMoisture () {
		return soilMoisture;
	}
	
	/**
	 * Set the greenhouse's soil moisture
	 * @param newValue: The new soil moisture
	 */
	public void setSoilMoisture (int newValue) {
		soilMoisture = newValue;
	}
	
	/**
	 * Get the weather index
	 * @return weatherIndex: The current weather index
	 */
	public int getWeatherIndex () {
		return weatherIndex;
	}
	
	/**
	 * Set the weather index
	 * @param newWeatherIndex: The new weather index
	 */
	public void setWeatherIndex (int newWeatherIndex) {
		weatherIndex = newWeatherIndex;
	}
}
