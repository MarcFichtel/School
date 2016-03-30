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
	private int temperatureInit;
	private int temperatureTarget;
	private int humidity;
	private int soilMoisture;
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
	
	public void setTemperature (int newValue) {
		temperature = newValue;
	}
	
	public void setInitialTemperature (int newValue) {
		temperatureInit = newValue;
	}
	
	public void setTargetTemperature (int newValue) {
		temperatureTarget = newValue;
	}
	
	public void setHumidity (int newValue) {
		humidity = newValue;
	}
	
	public void setSoilMoisture (int newValue) {
		soilMoisture = newValue;
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
	
	public int getTemperature () {
		return temperature;
	}
	
	public int getInitialTemperature () {
		return temperatureInit;
	}
	
	public int getTargetTemperature () {
		return temperatureTarget;
	}
	
	public int getHumidity () {
		return humidity;
	}
	
	public int getSoilMoisture () {
		return soilMoisture;
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
}