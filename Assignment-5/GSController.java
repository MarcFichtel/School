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
	
					updateTemperature ();
					Thread.sleep(data.getSampleRateTemp() * 600);
						
						
				}
				
				if (this.getId() == 17) {
						
					updateHumidity ();
					Thread.sleep(data.getSampleRateHumid() * 600);
						
				} 
				
				if (this.getId() == 18) {
						
					updateSoilMoisture ();
					Thread.sleep(data.getSampleRateSoilMoist() * 600);
						
				}
			
			} catch (Exception ex) {
				// TODO something
			}
		}
		return;
	}
	
	public void updateTemperature () {
		
		int temp = data.getTemperature();
		int tempTarget = data.getTargetTemperature();
		boolean furnaceOn = data.getFurnaceOn();
		boolean airConditionerOn = data.getAirConditionerOn();
		String weather = data.getWeather();
		
		if (weather == "Sunny") {
			temp += data.getSunnyDayTempChange();
		} else if (weather == "Cloudy") {
			temp += data.getCloudyDayTempChange();
		} else if (weather == "Rainy") {
			temp += data.getRainyDayTempChange();
		} else {
			temp += data.getSnowyDayTempChange();
		}
		
		if (furnaceOn) {
			temp += data.getFurnaceEfficiency();
		} else if (airConditionerOn) {
			temp += data.getAirConditionerEfficiency();
		}
		
		if (temp < tempTarget - 3) {
			data.setFurnaceOn(true);
			ui.setFurnaceChecked(true);
		} else if (temp >= tempTarget) {
			data.setFurnaceOn(false);
			ui.setFurnaceChecked(false);
		}
		
		if (temp > tempTarget + 3) {
			data.setAirConditionerOn(true);
			ui.setAirConditionerChecked(true);
		} else if (temp <= tempTarget) {
			data.setAirConditionerOn(false);
			ui.setAirConditionerChecked(false);
		}
		
		data.setTemperature(temp);
		ui.setTemperatureDisplay(temp);
	}
	
	public void updateHumidity () {
		
		int humidity = data.getHumidity();
		int humidityTarget = data.getTargetHumidity();
		boolean humidifierOn = data.getHumidifierOn();
		String weather = data.getWeather();
		
		if (weather == "Sunny") {
			humidity += data.getSunnyDayHumidChange();
		} else if (weather == "Cloudy") {
			humidity += data.getCloudyDayHumidChange();
		} else if (weather == "Rainy") {
			humidity += data.getRainyDayHumidChange();
		} else {
			humidity += data.getSnowyDayHumidChange();
		}
		
		if (humidifierOn) {
			humidity += data.getHumidifierEfficiency();
		}
		
		if (humidity < humidityTarget - 3) {
			data.setHumidifierOn(true);
			ui.setHumidifierChecked(true);
		}  else if (humidity >= humidityTarget) {
			data.setHumidifierOn(false);
			ui.setHumidifierChecked(false);
		}
		
		data.setHumidity(humidity);
		ui.setHumidityDisplay(humidity);
	}
	
	public void updateSoilMoisture () {
		
		int soilMoisture = data.getSoilMoisture();
		int soilMoistureTarget = data.getTargetSoilMoisture();
		boolean sprinklerOn = data.getSprinklerOn();
		String weather = data.getWeather();
		
		if (weather == "Sunny") {
			soilMoisture += data.getSunnyDaySoilMoistChange();
		} else if (weather == "Cloudy") {
			soilMoisture += data.getCloudyDaySoilMoistChange();
		} else if (weather == "Rainy") {
			soilMoisture += data.getRainyDaySoilMoistChange();
		} else {
			soilMoisture += data.getSnowyDaySoilMoistChange();
		}
		
		if (sprinklerOn) {
			soilMoisture += data.getSprinklerEfficiency();
		}

		if (soilMoisture < soilMoistureTarget - 3) {
			data.setSprinklerOn(true);
			ui.setSprinklerChecked(true);
		}  else if (soilMoisture >= soilMoistureTarget) {
			data.setSprinklerOn(false);
			ui.setSprinklerChecked(false);
		}
		
		data.setSoilMoisture(soilMoisture);
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
		if (!controlTemperature.isAlive() &&
			!controlHumidity.isAlive() &&
			!controlSoilMoisture.isAlive()) {
			controlTemperature.start();
			controlHumidity.start();
			controlSoilMoisture.start();
		}
	}
	
	public static void setSimInProgress (boolean simInProgress) {
		GSController.simInProgress = simInProgress;
	}
	
	public static boolean getSimInProgress () {
		return simInProgress;
	}
	
}
