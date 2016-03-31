import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GSMenuListener implements ActionListener {

	private GSView ui;
	private GSModel data;
	private JPanel[] inputPanels;
	
	public GSMenuListener (GSView ui, GSModel data) {
		this.ui = ui;
		this.data = data;
		this.inputPanels = ui.createInputDialog();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "Start Simulation") {
			promptAndValidateInput();
			data.setWeather("Sunny");
			
			ui.setTempDisplayActive(true);
			ui.setTempTargetDisplayActive(true);
			ui.setHumidDisplayActive(true);
			ui.setSoilMoistDisplayActive(true);
			
			GSController.startThreads();
			
		} else if (e.getActionCommand() == "Save Simulation") {
			// something
			
		} else if (e.getActionCommand() == "Load Simulation") {
			// something
			
		} else {
			// something
			System.exit(0);
		}
		
	}
	
	public void promptAndValidateInput () {
		
		boolean step1Done = false, step2Done = false, step3Done = false, 
				step4Done = false, step5Done = false;
		
		inputPanels = ui.createInputDialog();
		JTextField[] inputFields = ui.getInputFields();
		
		
		while (!step1Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[0], 
					"Step 1 of 5: Please enter simulation parameters", JOptionPane.QUESTION_MESSAGE);
			
			try {
				data.setTemperature(Integer.parseInt(inputFields[0].getText()));
				data.setTargetTemperature(Integer.parseInt(inputFields[1].getText()));
				data.setHumidity(Integer.parseInt(inputFields[2].getText()));
				data.setHumidityTarget(Integer.parseInt(inputFields[3].getText()));
				data.setSoilMoisture(Integer.parseInt(inputFields[4].getText()));
				data.setSoilMoistureTarget(Integer.parseInt(inputFields[5].getText()));
				data.setFurnaceEfficiency(Integer.parseInt(inputFields[6].getText()));
				data.setAirConditionerEfficiency(Integer.parseInt(inputFields[7].getText()));
				data.setHumidifierEfficiency(Integer.parseInt(inputFields[8].getText()));
				data.setSprinklerEfficiency(Integer.parseInt(inputFields[9].getText()));
				step1Done = true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		while (!step2Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[1], 
					"Step 2 of 5: Please enter weather effect on temperature", JOptionPane.QUESTION_MESSAGE);
			
			try {
				data.setSunnyDayTempChange(Integer.parseInt(inputFields[10].getText()));
				data.setCloudyDayTempChange(Integer.parseInt(inputFields[11].getText()));
				data.setRainyDayTempChange(Integer.parseInt(inputFields[12].getText()));
				data.setSnowyDayTempChange(Integer.parseInt(inputFields[13].getText()));
				step2Done = true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		while (!step3Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[2], 
					"Step 3 of 5: Please enter sweather effect on humidity", JOptionPane.QUESTION_MESSAGE);
			
			try {
				data.setSunnyDayHumidChange(Integer.parseInt(inputFields[14].getText()));
				data.setCloudyDayHumidChange(Integer.parseInt(inputFields[15].getText()));
				data.setRainyDayHumidChange(Integer.parseInt(inputFields[16].getText()));
				data.setSnowyDayHumidChange(Integer.parseInt(inputFields[17].getText()));
				step3Done = true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		while (!step4Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[3], 
					"Step 4 of 5: Please enter weather effect on soil moisture", JOptionPane.QUESTION_MESSAGE);
			
			try {
				data.setSunnyDaySoilMoistChange(Integer.parseInt(inputFields[18].getText()));
				data.setCloudyDaySoilMoistChange(Integer.parseInt(inputFields[19].getText()));
				data.setRainyDaySoilMoistChange(Integer.parseInt(inputFields[20].getText()));
				data.setSnowyDaySoilMoistChange(Integer.parseInt(inputFields[21].getText()));
				step4Done = true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		while (!step5Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[4], 
					"Step 5 of 5: Please enter display update frequency", JOptionPane.QUESTION_MESSAGE);
			
			try {
				data.setSampleRateTemp(Integer.parseInt(inputFields[22].getText()));
				data.setSampleRateHumid(Integer.parseInt(inputFields[23].getText()));
				data.setSampleRateSoilMoist(Integer.parseInt(inputFields[24].getText()));
				
				int weatherSelection = ui.getWeatherInput().getSelectedIndex();
				if (weatherSelection == 0) {
					data.setWeather("Sunny");
					ui.getWeatherSelection().setSelectedIndex(0);
				} else if (weatherSelection == 1) {
					data.setWeather("Cloudy");
					ui.getWeatherSelection().setSelectedIndex(1);
				} else if (weatherSelection == 2) {
					data.setWeather("Rainy");
					ui.getWeatherSelection().setSelectedIndex(2);
				} else {
					data.setWeather("Snowy");
					ui.getWeatherSelection().setSelectedIndex(3);
				}
				
				step5Done = true;
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
