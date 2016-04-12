package GreenhouseSimulator;

import javax.swing.JOptionPane;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class logs simulation values over time
 */

public class LogController extends Thread {

	/**
	 * ui: The simulation's graphical user interface
	 */
	private GUI ui;
	
	/**
	 * env: The simulation greenhouse
	 */
	private Greenhouse env;
	
	/**
	 * furnace: The furnace device used to increase temperature
	 */
	private Device furnace;
	
	/**
	 * ac: The air conditioner device used to decrease temperature
	 */
	private Device ac;
	
	/**
	 * humidifier: The humidifier device used to increase humidity
	 */
	private Device humidifier;
	
	/**
	 * sprinkler: The sprinkler device used to increase soil moisture
	 */
	private Device sprinkler;
	
	/**
	 * tempControl: The temperature controller
	 */
	private Controller temperatureControl;
	
	/**
	 * humidControl: The humidity controller
	 */
	private Controller humidityControl;
	
	/**
	 * soilMoistControl: The soil moisture controller
	 */
	private Controller soilMoistureControl;
	
	/**
	 * log: String array holding save file log information
	 */
	private String[] log = new String[30];
	
	/**
	 * updateInterval: The log update frequency in seconds
	 */
	private int updateInterval;
	
	/**
	 * Constructor assigns given values
	 * @param ui: The GUI
	 * @param env: The greenhouse
	 * @param furnace: The furnace device
	 * @param ac: The air conditioner device
	 * @param humidifier: The humidifier device
	 * @param sprinkler: The sprinkler device
	 * @param temperatureControl: The temperature controller
	 * @param humidityControl: The humidity controller
	 * @param soilMoistureControl: The soil moisture controller
	 */
	public LogController(
			GUI ui,
			Greenhouse env, 
			Device furnace, 
			Device ac, 
			Device humidifier, 
			Device sprinkler,
			Controller temperatureControl,
			Controller humidityControl,
			Controller soilMoistureControl) {
		
		this.ui = ui;
		this.env = env;
		this.furnace = furnace;
		this.ac = ac;
		this.humidifier = humidifier;
		this.sprinkler = sprinkler;
		this.temperatureControl = temperatureControl;
		this.humidityControl = humidityControl;
		this.soilMoistureControl = soilMoistureControl;
	}
	
	/**
	 * Initialize and update log
	 */
	@Override
	public void run() {
		
		// Use try/catch to cover any possible error exceptions
		try {
			
			// Initialize log
			initLog(env.getTemperature(),
				temperatureControl.getTarget(),
				env.getHumidity(),
				humidityControl.getTarget(),
				env.getSoilMoisture(),
				soilMoistureControl.getTarget(),
				furnace.getEfficiency(),
				ac.getEfficiency(),
				humidifier.getEfficiency(),
				sprinkler.getEfficiency(),
				temperatureControl.getSunnyDayChange(),
				temperatureControl.getCloudyDayChange(),
				temperatureControl.getRainyDayChange(),
				temperatureControl.getSnowyDayChange(),
				humidityControl.getSunnyDayChange(),
				humidityControl.getCloudyDayChange(),
				humidityControl.getRainyDayChange(),
				humidityControl.getSnowyDayChange(),
				soilMoistureControl.getSunnyDayChange(),
				soilMoistureControl.getCloudyDayChange(),
				soilMoistureControl.getRainyDayChange(),
				soilMoistureControl.getSnowyDayChange(),
				temperatureControl.getSampleRate(),
				humidityControl.getSampleRate(),
				soilMoistureControl.getSampleRate(),
				furnace.getDeviceActive(),
				ac.getDeviceActive(),
				humidifier.getDeviceActive(),
				sprinkler.getDeviceActive(),
				env.getWeatherIndex());
		
			// Update log while simulation is in progress and running
			while (Controller.getSimInProgress() &&
					!Controller.getSimPaused()) {
				updateLog(env.getTemperature(),
					temperatureControl.getTarget(),
					env.getHumidity(),
					humidityControl.getTarget(),
					env.getSoilMoisture(),
					soilMoistureControl.getTarget(),
					furnace.getEfficiency(),
					ac.getEfficiency(),
					humidifier.getEfficiency(),
					sprinkler.getEfficiency(),
					temperatureControl.getSunnyDayChange(),
					temperatureControl.getCloudyDayChange(),
					temperatureControl.getRainyDayChange(),
					temperatureControl.getSnowyDayChange(),
					humidityControl.getSunnyDayChange(),
					humidityControl.getCloudyDayChange(),
					humidityControl.getRainyDayChange(),
					humidityControl.getSnowyDayChange(),
					soilMoistureControl.getSunnyDayChange(),
					soilMoistureControl.getCloudyDayChange(),
					soilMoistureControl.getRainyDayChange(),
					soilMoistureControl.getSnowyDayChange(),
					temperatureControl.getSampleRate(),
					humidityControl.getSampleRate(),
					soilMoistureControl.getSampleRate(),
					furnace.getDeviceActive(),
					ac.getDeviceActive(),
					humidifier.getDeviceActive(),
					sprinkler.getDeviceActive(),
					env.getWeatherIndex());
				
				// Wait for longest of 3 sample rates before updating log again
				updateInterval = getUpdateFrequency();
				Thread.sleep(updateInterval * 1000);
			}
			
		// In case of errors, let user know something went wrong
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(ui, "Error updating log", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}	
	}
	
	/**
	 * Initialized the log
	 * @param temperature: The simulation's temperature
	 * @param temperatureTarget: The temperature target
	 * @param humidity: The simulation's humidity
	 * @param humidityTarget: The humidity target
	 * @param soilMoisture: The simulation's soil moisture
	 * @param soilMoistureTarget: The soil moisture target
	 * @param furnaceEfficiency: Furnace device efficiency
	 * @param airCondiEfficiency: Air conditioner device efficiency
	 * @param humidifierEfficiency: Humidifier device efficiency
	 * @param sprinklerEfficiency: Sprinkler device efficiency
	 * @param sunnyTempChange: Temperature change rate on sunny days
	 * @param cloudyTempChange: Temperature change rate on cloudy days
	 * @param rainyTempChange: Temperature change rate on rainy days
	 * @param snowyTempChange: Temperature change rate on snowy days
	 * @param sunnyHumidChange: Humidity change rate on sunny days
	 * @param cloudyHumidChange: Humidity change rate on cloudy days
	 * @param rainyHumidChange: Humidity change rate on rainy days
	 * @param snowyHumidChange: Humidity change rate on snowy days
	 * @param sunnySoilMoistChange: Soil moisture change rate on sunny days
	 * @param cloudySoilMoistChange: Soil moisture change rate on cloudy days
	 * @param rainySoilMoistChange: Soil moisture change rate on rainy days
	 * @param snowySoilMoistChange: Soil moisture change rate on snowy days
	 * @param tempSampleRate: Temperature display sample rate
	 * @param humidSampleRate: Humidity display sample rate
	 * @param soilMoistSampleRate: Soil moisture display sample rate
	 * @param furnaceOn: Is furnace active or not
	 * @param airCondiOn: Is air conditioner active or not
	 * @param humidifierOn: Is humidifier active or not
	 * @param sprinklerOn: Is sprinkler active or not
	 * @param weatherIndex: The index of the current weather selection
	 */
	public void initLog (
			int temperature,
			int temperatureTarget,
			int humidity,
			int humidityTarget,
			int soilMoisture,
			int soilMoistureTarget,
			int furnaceEfficiency,
			int airCondiEfficiency,
			int humidifierEfficiency,
			int sprinklerEfficiency,
			int sunnyTempChange,
			int cloudyTempChange,
			int rainyTempChange,
			int snowyTempChange,
			int sunnyHumidChange,
			int cloudyHumidChange,
			int rainyHumidChange,
			int snowyHumidChange,
			int sunnySoilMoistChange,
			int cloudySoilMoistChange,
			int rainySoilMoistChange,
			int snowySoilMoistChange,
			int tempSampleRate,
			int humidSampleRate,
			int soilMoistSampleRate,
			boolean furnaceOn,
			boolean airCondiOn,
			boolean humidifierOn,
			boolean sprinklerOn,
			int weatherIndex) {
		
		log[0] = "Temperature: " + String.valueOf(temperature);
		log[1] = "Temperature Target: " + String.valueOf(temperatureTarget);
		log[2] = "Humidity: " + String.valueOf(humidity);
		log[3] = "Humidity Target: " + String.valueOf(humidityTarget);
		log[4] = "Soil Moisture: " + String.valueOf(soilMoisture);
		log[5] = "Soil Moisture Target: " + String.valueOf(soilMoistureTarget);
		log[6] = "Furnace Efficiency: " + String.valueOf(furnaceEfficiency);
		log[7] = "Air Conditioner Efficiency: " + String.valueOf(airCondiEfficiency);
		log[8] = "Humidifier Efficiency: " + String.valueOf(humidifierEfficiency);
		log[9] = "Sprinkler Efficiency: " + String.valueOf(sprinklerEfficiency);
		log[10] = "Temperature Change Rate on Sunny days: " + String.valueOf(sunnyTempChange);
		log[11] = "Temperature Change Rate on Cloudy days: " + String.valueOf(cloudyTempChange);
		log[12] = "Temperature Change Rate on Rainy days: " + String.valueOf(rainyTempChange);
		log[13] = "Temperature Change Rate on Snowy days: " + String.valueOf(snowyTempChange);
		log[14] = "Humidity Change Rate on Sunny days: " + String.valueOf(sunnyHumidChange);
		log[15] = "Humidity Change Rate on Cloudy days: " + String.valueOf(cloudyHumidChange);
		log[16] = "Humidity Change Rate on Rainy days: " + String.valueOf(rainyHumidChange);
		log[17] = "Humidity Change Rate on Snowy days: " + String.valueOf(snowyHumidChange);
		log[18] = "Soil Moisture Change Rate on Sunny days: " + String.valueOf(sunnySoilMoistChange);
		log[19] = "Soil Moisture Change Rate on Cloudy days: " + String.valueOf(cloudySoilMoistChange);
		log[20] = "Soil Moisture Change Rate on Rainy days: " + String.valueOf(rainySoilMoistChange);
		log[21] = "Soil Moisture Change Rate on Snowy days: " + String.valueOf(snowySoilMoistChange);
		log[22] = "Temperature Sample Rate: " + String.valueOf(tempSampleRate);
		log[23] = "Humidity Sample Rate: " + String.valueOf(humidSampleRate);
		log[24] = "Soil Moisture Sample Rate: " + String.valueOf(soilMoistSampleRate);
		log[25] = "Furnace On: " + String.valueOf(furnaceOn);
		log[26] = "Air Conditioner On: " + String.valueOf(airCondiOn);
		log[27] = "Humidifier On: " + String.valueOf(humidifierOn);
		log[28] = "Sprinkler On: " + String.valueOf(sprinklerOn);
		log[29] = "Weather: " + weatherIndex;
	}
	
	/**
	 * Updates the log
	 * @param temperature: The simulation's temperature
	 * @param temperatureTarget: The temperature target
	 * @param humidity: The simulation's humidity
	 * @param humidityTarget: The humidity target
	 * @param soilMoisture: The simulation's soil moisture
	 * @param soilMoistureTarget: The soil moisture target
	 * @param furnaceEfficiency: Furnace device efficiency
	 * @param airCondiEfficiency: Air conditioner device efficiency
	 * @param humidifierEfficiency: Humidifier device efficiency
	 * @param sprinklerEfficiency: Sprinkler device efficiency
	 * @param sunnyTempChange: Temperature change rate on sunny days
	 * @param cloudyTempChange: Temperature change rate on cloudy days
	 * @param rainyTempChange: Temperature change rate on rainy days
	 * @param snowyTempChange: Temperature change rate on snowy days
	 * @param sunnyHumidChange: Humidity change rate on sunny days
	 * @param cloudyHumidChange: Humidity change rate on cloudy days
	 * @param rainyHumidChange: Humidity change rate on rainy days
	 * @param snowyHumidChange: Humidity change rate on snowy days
	 * @param sunnySoilMoistChange: Soil moisture change rate on sunny days
	 * @param cloudySoilMoistChange: Soil moisture change rate on cloudy days
	 * @param rainySoilMoistChange: Soil moisture change rate on rainy days
	 * @param snowySoilMoistChange: Soil moisture change rate on snowy days
	 * @param tempSampleRate: Temperature display sample rate
	 * @param humidSampleRate: Humidity display sample rate
	 * @param soilMoistSampleRate: Soil moisture display sample rate
	 * @param furnaceOn: Is furnace active or not
	 * @param airCondiOn: Is air conditioner active or not
	 * @param humidifierOn: Is humidifier active or not
	 * @param sprinklerOn: Is sprinkler active or not
	 * @param weatherIndex: The index of the current weather selection
	 */
	public void updateLog (
			int temperature,
			int temperatureTarget,
			int humidity,
			int humidityTarget,
			int soilMoisture,
			int soilMoistureTarget,
			int furnaceEfficiency,
			int airCondiEfficiency,
			int humidifierEfficiency,
			int sprinklerEfficiency,
			int sunnyTempChange,
			int cloudyTempChange,
			int rainyTempChange,
			int snowyTempChange,
			int sunnyHumidChange,
			int cloudyHumidChange,
			int rainyHumidChange,
			int snowyHumidChange,
			int sunnySoilMoistChange,
			int cloudySoilMoistChange,
			int rainySoilMoistChange,
			int snowySoilMoistChange,
			int tempSampleRate,
			int humidSampleRate,
			int soilMoistSampleRate,
			boolean furnaceOn,
			boolean airCondiOn,
			boolean humidifierOn,
			boolean sprinklerOn,
			int weatherIndex) {
		
		log[0] += "," + String.valueOf(temperature);
		log[1] += "," + String.valueOf(temperatureTarget);
		log[2] += "," + String.valueOf(humidity);
		log[3] += "," + String.valueOf(humidityTarget);
		log[4] += "," + String.valueOf(soilMoisture);
		log[5] += "," + String.valueOf(soilMoistureTarget);
		log[6] += "," + String.valueOf(furnaceEfficiency);
		log[7] += "," + String.valueOf(airCondiEfficiency);
		log[8] += "," + String.valueOf(humidifierEfficiency);
		log[9] += "," + String.valueOf(sprinklerEfficiency);
		log[10] += "," + String.valueOf(sunnyTempChange);
		log[11] += "," + String.valueOf(cloudyTempChange);
		log[12] += "," + String.valueOf(rainyTempChange);
		log[13] += "," + String.valueOf(snowyTempChange);
		log[14] += "," + String.valueOf(sunnyHumidChange);
		log[15] += "," + String.valueOf(cloudyHumidChange);
		log[16] += "," + String.valueOf(rainyHumidChange);
		log[17] += "," + String.valueOf(snowyHumidChange);
		log[18] += "," + String.valueOf(sunnySoilMoistChange);
		log[19] += "," + String.valueOf(cloudySoilMoistChange);
		log[20] += "," + String.valueOf(rainySoilMoistChange);
		log[21] += "," + String.valueOf(snowySoilMoistChange);
		log[22] += "," + String.valueOf(tempSampleRate);
		log[23] += "," + String.valueOf(humidSampleRate);
		log[24] += "," + String.valueOf(soilMoistSampleRate);
		log[25] += "," + String.valueOf(furnaceOn);
		log[26] += "," + String.valueOf(airCondiOn);
		log[27] += "," + String.valueOf(humidifierOn);
		log[28] += "," + String.valueOf(sprinklerOn);
		log[29] += "," + weatherIndex;
	}

	/**
	 * Compute frequency at which log is updated
	 * @return updateInterval: The highest sample rate of the three environment controllers
	 */
	public int getUpdateFrequency () {
		
		// Wait for longest of 3 sample rates before updating log again
		updateInterval = Integer.max(
				temperatureControl.getSampleRate(), humidityControl.getSampleRate());
		updateInterval = Integer.max(
				updateInterval, soilMoistureControl.getSampleRate());
		return updateInterval;
	}
	
	/**
	 * Get the log
	 * @return log: The log String array
	 */
	public String[] getLog () {
		return log;
	}
}
