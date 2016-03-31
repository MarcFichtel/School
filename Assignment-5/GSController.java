/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class does something...
 */

public class GSController extends Thread {

	private static GSController controlTemperature;
	private static GSController controlHumidity;
	private static GSController controlSoilMoisture;
	private static GSView ui = new GSView();
	private static GSModel data = new GSModel();
	private static boolean simInProgress = false;
	
	public GSController () {
		super();
	}
	
	@Override
	public void run() {
		
		setSimInProgress(true);

		while (simInProgress) {
			
			try {
				
				if (this.getId() == 16) {
	
					updateTemperature (
							data.getFurnaceOn(),
							data.getAirConditionerOn(),
							data.getWeather());
					
					Thread.sleep(data.getSampleRateTemp() * 600);
						
						
				}
				
				if (this.getId() == 17) {
						
					updateHumidity (
							data.getHumidifierOn(),
							data.getWeather());
					Thread.sleep(data.getSampleRateHumid() * 600);
						
				} 
				
				if (this.getId() == 18) {
						
					updateSoilMoisture (
							data.getSprinklerOn(),
							data.getWeather());
					Thread.sleep(data.getSampleRateSoilMoist() * 600);
						
				}
			
			} catch (Exception ex) {
				// something
			}
		}
		return;
	}
	
	public void updateTemperature (boolean furnaceOn, boolean airConditionerOn, String weather) {
		
		int temperature = data.getTemperature();
		
		if (furnaceOn) {
			temperature += data.getFurnaceEfficiency();
		} else if (airConditionerOn) {
			temperature += data.getAirConditionerEfficiency();
		}

		if (weather == "Sunny") {
			temperature += data.getSunnyDayTempChange();
		} else if (weather == "Cloudy") {
			temperature += data.getCloudyDayTempChange();
		} else if (weather == "Rainy") {
			temperature += data.getRainyDayTempChange();
		} else {
			temperature += data.getSnowyDayTempChange();
		}
		
		data.setTemperature(temperature);
		ui.setFurnaceChecked(data.checkFurnace());
		ui.setAirConditionerChecked(data.checkAirConditioner());
		ui.setTemperatureDisplay(temperature);
	}
	
	public void updateHumidity (boolean humidifierOn, String weather) {
		
		int humidity = data.getHumidity();
		
		if (humidifierOn) {
			humidity += data.getHumidifierEfficiency();
		}

		if (weather == "Sunny") {
			humidity += data.getSunnyDayHumidChange();
		} else if (weather == "Cloudy") {
			humidity += data.getCloudyDayHumidChange();
		} else if (weather == "Rainy") {
			humidity += data.getRainyDayHumidChange();
		} else {
			humidity += data.getSnowyDayHumidChange();
		}
		
		data.setHumidity(humidity);
		ui.setHumidifierChecked(data.checkHumidifier());
		ui.setHumidityDisplay(humidity);
	}
	
	public void updateSoilMoisture (boolean sprinklerOn, String weather) {
		
		int soilMoisture = data.getSoilMoisture();
		
		if (sprinklerOn) {
			soilMoisture += data.getSprinklerEfficiency();
		}

		if (weather == "Sunny") {
			soilMoisture += data.getSunnyDaySoilMoistChange();
		} else if (weather == "Cloudy") {
			soilMoisture += data.getCloudyDaySoilMoistChange();
		} else if (weather == "Rainy") {
			soilMoisture += data.getRainyDaySoilMoistChange();
		} else {
			soilMoisture += data.getSnowyDaySoilMoistChange();
		}
		
		data.setSoilMoisture(soilMoisture);
		ui.setSprinklerChecked(data.checkSprinkler());
		ui.setSoilMoistureDisplay(soilMoisture);
	}
	
	public static void main(String[] args) {
		
		ui.initUI(new GSMenuListener(ui, data));
		
		controlTemperature = new GSController();
		controlHumidity = new GSController();
		controlSoilMoisture = new GSController();
		
		controlTemperature.setName("controlTemperature");
		controlHumidity.setName("controlHumidity");
		controlSoilMoisture.setName("controlSoilMoisture");
		
		
	}
	
	public static void startThreads () {
		controlTemperature.start();
		controlHumidity.start();
		controlSoilMoisture.start();
	}
	
	public static void setSimInProgress (boolean simInProgress) {
		GSController.simInProgress = simInProgress;
	}
	
	public static boolean getSetupComplete () {
		return simInProgress;
	}
	
}
