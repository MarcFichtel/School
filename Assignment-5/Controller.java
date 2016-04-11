import javax.swing.JOptionPane;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class runs the program, and is the superclass for controllers coordinating the simulation
 */

public class Controller extends Thread {

	/**
	 * simInProgress: Is the simulation currently in progress or not
	 */
	private static boolean simInProgress = false;
	
	/**
	 * simPause: Is the simulation paused or not
	 */
	private static boolean simPaused = false;
	
	/**
	 * ui: The simulation's user interface
	 */
	private GUI ui;
	
	/**
	 * greenhouse: The simulation's greenhouse environment
	 */
	private Greenhouse greenhouse;
	
	/**
	 * device1: The first device the controller needs to maintain the environment
	 */
	private Device device1;
	
	/**
	 * device2: The second device the controller may need to maintain the environment
	 */
	private Device device2;
	
	/**
	 * temperatureControl: Regulates temperature
	 */
	private static Controller temperatureControl;
	
	/**
	 * humidityControl: Regulates humidity
	 */
	private static Controller humidityControl;
	
	/**
	 * soilMoistureControl: Regulates soil moisture
	 */
	private static Controller soilMoistureControl;
	
	/**
	 * logControl: Updates the simulation log
	 */
	private static LogController logControl;
	
	/**
	 * tempControlThreadID: The temperature controller's thread id
	 */
	private static long tempControlThreadID;
	
	/**
	 * humidControlThreadID: The humidity controller's thread id
	 */
	private static long humidControlThreadID;
	
	/**
	 * targetValue: The desired value the controller uses to coordinate devices
	 */
	private int targetValue;
	
	/**
	 * sunnyDayChange: The value's change rate on a sunny day
	 */
	private int sunnyDayChange;
	
	/**
	 * cloudyDayChange: The value's change rate on a cloudy day
	 */
	private int cloudyDayChange;
	
	/**
	 * rainyDayChange: The value's change rate on a rainy day
	 */
	private int rainyDayChange;
	
	/**
	 * snowyDayChange: The value's change rate on a snowy day
	 */
	private int snowyDayChange;
	
	/**
	 * sampleRate: The frequency at which the controller updates it's display
	 */
	private int sampleRate;
	
	/**
	 * Constructor assigns given values
	 * @param ui: The user interface
	 * @param greenhouse: The greenhouse environment
	 * @param device1: The first environment maintenance device
	 * @param device2: The second environment maintenance device
	 * @param name: The controller's name (TODO check if needed)
	 */
	public Controller (
			GUI ui,
			Greenhouse greenhouse,
			Device device1,
			Device device2,
			String name) {
		
		// Invoke Thread superclass constructor
		super();
		
		// Assign given values
		this.ui = ui;
		this.greenhouse = greenhouse;
		this.device1 = device1;
		this.device2 = device2;
		this.setName(name); 		// TODO check if needed
	}
	
	/**
	 * Defines controller behavior
	 * Calculates value depending on environment conditions
	 * Updates display with specified frequency
	 * Displays an error, if one occurs
	 */
	@Override
	public void run () {
		
		// Use try/catch to cover any possible error exceptions
		try {
			
			// Controller runs only while simulation is in progress
			while (Controller.simInProgress) {
				
				// Get relevant data
				int value = 0; 
				boolean device1On = device1.getDeviceActive();
				boolean device2On = false;
				int weather = greenhouse.getWeatherIndex();
								
				// Temperature controller gets temperature value (°C) & second device
				if (this.getId() == tempControlThreadID) {
					value = greenhouse.getTemperature();
					device2On = device2.getDeviceActive();

					// Humidity controller gets humidity value (%)
				} else if (this.getId() == humidControlThreadID) {
					value = greenhouse.getHumidity();
				
				// Soil Moisture controller gets soil moisture value (%)
				} else {
					value = greenhouse.getSoilMoisture();
				}
				
				// Change value depending on weather condition change rate
				if (weather == 0) {
					value += sunnyDayChange;
				} else if (weather == 1) {
					value += cloudyDayChange;
				} else if (weather == 2) {
					value += rainyDayChange;
				} else {
					value += snowyDayChange;
				}
				
				// Change value depending on active device(s)
				if (device1On) {
					value += device1.getEfficiency();
				} else if (device2On) {
					value += device2.getEfficiency();
				}
				
				// Activate first device depending on proximity of value to the target
				if (value < targetValue - 3) {
					device1.setDeviceActive(true);
					
					// Activate device's check box in ui
					if (this.getId() == tempControlThreadID) {
						ui.setFurnaceChecked(true);
					} else if (this.getId() == humidControlThreadID) {
						ui.setHumidifierChecked(true);
					} else {
						ui.setSprinklerChecked(true);
					}
				
				// Activate first device depending on proximity of value to the target
				} else if (value >= targetValue) {
					device1.setDeviceActive(false);
					
					// Deactivate device's check box in ui
					if (this.getId() == tempControlThreadID) {
						ui.setFurnaceChecked(false);
					} else if (this.getId() == humidControlThreadID) {
						ui.setHumidifierChecked(false);
					} else {
						ui.setSprinklerChecked(false);
					}
				}
				
				// Change a second device's (Air Conditioner only) state depending on proximity of value to the target
				if (this.getId() == tempControlThreadID && value > targetValue + 3) {
					device2.setDeviceActive(true);
					ui.setAirConditionerChecked(true);
				} else if (this.getId() == tempControlThreadID && value <= targetValue) {
					device2.setDeviceActive(false);
					ui.setAirConditionerChecked(false);
				}
				
				// Update temperature after all calculations, update display
				if (this.getId() == tempControlThreadID) {			
					
					// Keep value within range
					if (value > 40) {
						value = 40;
					} else if (value < 10) {
						value = 10;
					}
					
					// Update new temperature value
					greenhouse.setTemperature(value);
					ui.setTemperatureDisplay(value);
					
					// Update status icon
					if (value >= targetValue - 4 &&
						value <= targetValue + 4) {
						ui.setTempStatus(true);
					} else {
						ui.setTempStatus(false);
					}
					
				// Update humidity after all calculations, update display
				} else if (this.getId() == humidControlThreadID) {
					
					// Keep value within range
					if (value > 100) {
						value = 100;
					} else if (value < 0) {
						value = 0;
					}
					
					// Update new humidity value
					greenhouse.setHumidity(value);
					ui.setHumidityDisplay(value);
					
					// Update status icon
					if (value >= targetValue - 4 &&
						value <= targetValue + 4) {
						ui.setHumidStatus(true);
					} else {
						ui.setHumidStatus(false);
					}
				
				// Update soil moisture after all calculations, update display
				} else {					
					
					// Keep value within range
					if (value > 100) {
						value = 100;
					} else if (value < 0) {
						value = 0;
					}
					
					// Update new air moisture value
					greenhouse.setSoilMoisture(value);
					ui.setSoilMoistureDisplay(value);
					
					// Update status icon
					if (value >= targetValue - 4 &&
						value <= targetValue + 4) {
						ui.setSoilMoistStatus(true);
					} else {
						ui.setSoilMoistStatus(false);
					}
				}
				
				// Wait for specified sample rate until next update
				Thread.sleep(sampleRate * 1000);
				
				// If simulation is paused, keep threads looping until it is resumed
				// TODO check if there's a better way to do this
				for (;;) {
					if (!simPaused) {
						break;
					}
					Thread.sleep(sampleRate * 1000);
				}
			}
		
		// In case of errors, let user know something went wrong
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(ui, "Error running threads: " + ex.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Start controller threads, if they're not already running
	 * @param temperatureControl: The temperature control thread
	 * @param humidityControl: The humidity control thread
	 * @param soilMoistureControl: The soil moisture control thread
	 */
	public static void startThreads () {
		
		// Check, if control threads are already running
		if (!temperatureControl.isAlive() &&
			!humidityControl.isAlive() &&
			!soilMoistureControl.isAlive() &&
			!logControl.isAlive()) {
			
			// If not, start them
			temperatureControl.start();
			humidityControl.start();
			soilMoistureControl.start();
			logControl.start();
		}
	}
	
	/**
	 * Starts the greenhouse simulation program
	 * @param args: Command line arguments
	 */
	public static void main(String[] args) {
		
		// Instantiate a new graphical user interface
		GUI ui = new GUI();
				
		// Create a new greenhouse environment
		Greenhouse env = new Greenhouse();
		
		// Create environment maintenance devices
		Device furnace = new Device();
		Device ac = new Device();
		Device humidifier = new Device();
		Device sprinkler = new Device();
		
		// Create environment controllers
		temperatureControl = new Controller(ui, env, furnace, ac, "temperatureControl");
		humidityControl = new Controller(ui, env, humidifier, null, "humidityControl");
		soilMoistureControl = new Controller(ui, env, sprinkler, null, "soilMoistureControl");
		
		// Get the newly created controller's thread ids (because names don't seem to work)
		setTempControlThreadID(temperatureControl.getId());
		setHumidControlThreadID(humidityControl.getId());
		
		// Create a log controller
		logControl = new LogController (ui, env,
				furnace, ac, humidifier, sprinkler,
				temperatureControl, humidityControl, soilMoistureControl);
		
		// Create a new menu listener
		MenuListener menuListener = new MenuListener (
				ui, env, 
				furnace, ac, humidifier, sprinkler,
				temperatureControl, humidityControl, 
				soilMoistureControl, logControl);
		
		// Set up the gui and add the menu listener
		ui.initUI(menuListener);
		
		// Add a listener to all interactive ui elements
		ui.addButtonListener(new ButtonListener(
				ui, env,
				temperatureControl, humidityControl,
				soilMoistureControl, menuListener, logControl));
	}

	/**
	 * Get whether there is a simulation in progress or not
	 * @return simInProgress: The current simulation status
	 */
	public static boolean getSimInProgress () {
		return simInProgress;
	}
	
	/**
	 * Set whether there is a simulation in progress or not
	 * @param bool: The new simulation status
	 */
	public static void setSimInProgress (boolean bool) {
		simInProgress = bool;
	}
	
	/**
	 * Get whether the simulation is paused or not
	 * @return simPaused: The current simulation status
	 */
	public static boolean getSimPaused () {
		return simPaused;
	}
	
	/**
	 * Set whether the current simulation is paused or not
	 * @param bool: Is simulation paused or not
	 */
	public static void setSimPaused (boolean bool) {
		simPaused = bool;
	}
	
	/**
	 * Set the temperature controller's thread id (since names don't seem to work)
	 * @param id: The controller's thread id
	 */
	public static void setTempControlThreadID (long id) {
		tempControlThreadID = id;
	}
	
	/**
	 * Set the humidity controller's thread id (since names don't seem to work)
	 * @param id: The controller's thread id
	 */
	public static void setHumidControlThreadID (long id) {
		humidControlThreadID = id;
	}
	
	/**
	 * Get the controller's target/desired value
	 * @return targetValue: The controller's current target/desired value
	 */
	public int getTarget () {
		return targetValue;
	}
	
	/**
	 * Set the controller's target/desired value
	 * @param newValue: The controller's new target/desired value
	 */
	public void setTarget (int newValue) {
		targetValue = newValue;
	}
	
	/**
	 * Get the value change rate under sunny conditions
	 * @return sunnyDayChange: The current value change rate under sunny conditions
	 */
	public int getSunnyDayChange () {
		return sunnyDayChange;
	}
	
	/**
	 * Set the value change rate under sunny conditions
	 * @param newValue: The new value change rate under sunny conditions
	 */
	public void setSunnyDayChange (int newValue) {
		sunnyDayChange = newValue;
	}
	
	/**
	 * Get the value change rate under cloudy conditions
	 * @return cloudyDayChange: The current value change rate under cloudy conditions
	 */
	public int getCloudyDayChange () {
		return cloudyDayChange;
	}
	
	/**
	 * Set the value change rate under cloudy conditions
	 * @param newValue: The new value change rate under cloudy conditions
	 */
	public void setCloudyDayChange (int newValue) {
		cloudyDayChange = newValue;
	}
	
	/**
	 * Get the value change rate under rainy conditions
	 * @return rainyDayChange: The current value change rate under rainy conditions
	 */
	public int getRainyDayChange () {
		return rainyDayChange;
	}
	
	/**
	 * Set the value change rate under rainy conditions
	 * @param newValue: The new value change rate under rainy conditions
	 */
	public void setRainyDayChange (int newValue) {
		rainyDayChange = newValue;
	}
	
	/**
	 * Get the value change rate under snowy conditions
	 * @return snowyDayChange: The current value change rate under snowy conditions
	 */
	public int getSnowyDayChange () {
		return snowyDayChange;
	}
	
	/**
	 * Set the value change rate under snowy conditions
	 * @param newValue: The new value change rate under snowy conditions
	 */
	public void setSnowyDayChange (int newValue) {
		snowyDayChange = newValue;
	}
	
	/**
	 * Get the controller's sample rate (update frequency) in seconds
	 * @return sampleRate: The controller's current sample rate (update frequency)
	 */
	public int getSampleRate () {
		return sampleRate;
	}
	
	/**
	 * Set the controller's sample rate (update frequency) in seconds
	 * @param newValue: The controller's new sample rate (update frequency)
	 */
	public void setSampleRate (int newValue) {
		sampleRate = newValue;
	}
}
