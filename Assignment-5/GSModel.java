/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class does something...
 */

public class GSModel {

	private int temperature;
	private int temperatureTarget;
	private int humidity;
	private int humidityTarget;
	private int soilMoisture;
	private int soilMoistureTarget;
	private int furnaceEfficiency;
	private int conditionerEfficiency;
	private int humidifierEfficiency;
	private int sprinklerEfficiency;
	private int sunnyDayTempChange;
	private int cloudyDayTempChange;
	private int rainyDayTempChange;
	private int snowyDayTempChange;
	private int sunnyDayHumidChange;
	private int cloudyDayHumidChange;
	private int rainyDayHumidChange;
	private int snowyDayHumidChange;
	private int sunnyDaySoilMoistChange;
	private int cloudyDaySoilMoistChange;
	private int rainyDaySoilMoistChange;
	private int snowyDaySoilMoistChange;
	private int sampleRateTemp;
	private int sampleRateHumid;
	private int sampleRateSoilMoist;
	private boolean furnaceOn = false;
	private boolean airConditionerOn = false;
	private boolean humidifierOn = false;
	private boolean sprinklerOn = false;
	private String weather;
	
	
	public boolean checkFurnace () {
		
		boolean furnaceOn = false;
		
		if (temperature < temperatureTarget - 3) {
			setFurnaceOn(true);
			furnaceOn = true;
		} 
		
		if (temperature >= temperatureTarget) {
			setFurnaceOn(false);
			furnaceOn = false;
		}
		
		return furnaceOn;
	}
	
	public boolean checkAirConditioner () {
		
		boolean airCondiOn = false;
		
		if (temperature > temperatureTarget + 3) {
			setAirConditionerOn(true);
			airCondiOn = true;
		}
		
		if (temperature <= temperatureTarget) {
			setAirConditionerOn(false);
			airCondiOn = false;
		}
		
		return airCondiOn;
	}
	
	public boolean checkHumidifier () {
		
		boolean humidOn = false;
		
		if (humidity < humidityTarget - 3) {
			setHumidifierOn(true);
			humidOn = true;
		} 
		
		if (humidity >= humidityTarget) {
			setHumidifierOn(false);
			humidOn = false;
		}
		
		return humidOn;
	}

	public boolean checkSprinkler () {
		
		boolean sprinklerOn = false;
		
		if (soilMoisture < soilMoistureTarget - 3) {
			setSprinklerOn(true);
			sprinklerOn = true;
		} 
		
		if (soilMoisture >= soilMoistureTarget) {
			setSprinklerOn(false);
			sprinklerOn = false;
		}
		
		return sprinklerOn;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	public void setTemperature (int newValue) {
		temperature = newValue;
	}
	
	public void setTargetTemperature (int newValue) {
		temperatureTarget = newValue;
	}
	
	public void setHumidity (int newValue) {
		humidity = newValue;
	}
	
	public void setHumidityTarget (int newValue) {
		humidityTarget = newValue;
	}
	
	public void setSoilMoisture (int newValue) {
		soilMoisture = newValue;
	}
	
	public void setSoilMoistureTarget (int newValue) {
		soilMoistureTarget = newValue;
	}
	
	public void setFurnaceEfficiency (int newValue) {
		furnaceEfficiency = newValue;
	}
	
	public void setAirConditionerEfficiency (int newValue) {
		conditionerEfficiency = newValue;
	}
	
	public void setHumidifierEfficiency (int newValue) {
		humidifierEfficiency = newValue;
	}
	
	public void setSprinklerEfficiency (int newValue) {
		sprinklerEfficiency = newValue;
	}
	
	public void setSunnyDayTempChange (int newValue) {
		sunnyDayTempChange = newValue;
	}
	
	public void setCloudyDayTempChange (int newValue) {
		cloudyDayTempChange = newValue;
	}
	
	public void setRainyDayTempChange (int newValue) {
		rainyDayTempChange = newValue;
	}
	
	public void setSnowyDayTempChange (int newValue) {
		snowyDayTempChange = newValue;
	}
	
	public void setSunnyDayHumidChange (int newValue) {
		sunnyDayHumidChange = newValue;
	}
	
	public void setCloudyDayHumidChange (int newValue) {
		cloudyDayHumidChange = newValue;
	}
	
	public void setRainyDayHumidChange (int newValue) {
		rainyDayHumidChange = newValue;
	}
	
	public void setSnowyDayHumidChange (int newValue) {
		snowyDayHumidChange = newValue;
	}
	
	public void setSunnyDaySoilMoistChange (int newValue) {
		sunnyDaySoilMoistChange = newValue;
	}
	
	public void setCloudyDaySoilMoistChange (int newValue) {
		cloudyDaySoilMoistChange = newValue;
	}
	
	public void setRainyDaySoilMoistChange (int newValue) {
		rainyDaySoilMoistChange = newValue;
	}
	
	public void setSnowyDaySoilMoistChange (int newValue) {
		snowyDaySoilMoistChange = newValue;
	}
	
	public void setSampleRateTemp (int newValue) {
		sampleRateTemp = newValue;
	}
	
	public void setSampleRateHumid (int newValue) {
		sampleRateHumid = newValue;
	}
	
	public void setSampleRateSoilMoist (int newValue) {
		sampleRateSoilMoist = newValue;
	}
	
	public void setFurnaceOn (boolean bool) {
		furnaceOn = bool;
	}
	
	public void setAirConditionerOn (boolean bool) {
		airConditionerOn = bool;
	}
	
	public void setHumidifierOn (boolean bool) {
		humidifierOn = bool;
	}
	
	public void setSprinklerOn (boolean bool) {
		sprinklerOn = bool;
	}
	
	public void setWeather (String newWeather) {
		weather = newWeather;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getTemperature () {
		return temperature;
	}
	
	public int getTargetTemperature () {
		return temperatureTarget;
	}
	
	public int getHumidity () {
		return humidity;
	}
	
	public int getHumidityTarget () {
		return humidityTarget;
	}
	
	public int getSoilMoisture () {
		return soilMoisture;
	}
	
	public int getSoilMoistureTarget () {
		return soilMoistureTarget;
	}
	
	public int getFurnaceEfficiency () {
		return furnaceEfficiency;
	}
	
	public int getAirConditionerEfficiency () {
		return conditionerEfficiency;
	}
	
	public int getHumidifierEfficiency () {
		return humidifierEfficiency;
	}
	
	public int getSprinklerEfficiency () {
		return sprinklerEfficiency;
	}
	
	public int getSunnyDayTempChange () {
		return sunnyDayTempChange;
	}
	
	public int getCloudyDayTempChange () {
		return cloudyDayTempChange;
	}
	
	public int getRainyDayTempChange () {
		return rainyDayTempChange;
	}
	
	public int getSnowyDayTempChange () {
		return snowyDayTempChange;
	}
	
	public int getSunnyDayHumidChange () {
		return sunnyDayHumidChange;
	}
	
	public int getCloudyDayHumidChange () {
		return cloudyDayHumidChange;
	}
	
	public int getRainyDayHumidChange () {
		return rainyDayHumidChange;
	}
	
	public int getSnowyDayHumidChange () {
		return snowyDayHumidChange;
	}
	
	public int getSunnyDaySoilMoistChange () {
		return sunnyDaySoilMoistChange;
	}
	
	public int getCloudyDaySoilMoistChange () {
		return cloudyDaySoilMoistChange;
	}
	
	public int getRainyDaySoilMoistChange () {
		return rainyDaySoilMoistChange;
	}
	
	public int getSnowyDaySoilMoistChange () {
		return snowyDaySoilMoistChange;
	}
	
	public int getSampleRateTemp () {
		return sampleRateTemp;
	}
	
	public int getSampleRateHumid () {
		return sampleRateHumid;
	}
	
	public int getSampleRateSoilMoist () {
		return sampleRateSoilMoist;
	}
	
	public boolean getFurnaceOn () {
		return furnaceOn;
	}
	
	public boolean getAirConditionerOn () {
		return airConditionerOn;
	}
	
	public boolean getHumidifierOn () {
		return humidifierOn;
	}
	
	public boolean getSprinklerOn () {
		return sprinklerOn;
	}
	
	public String getWeather () {
		return weather;
	}
}