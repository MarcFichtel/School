import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFileChooser;
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
	
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "Start Simulation") {
			
			promptAndValidateInput();
			
			ui.setTempDisplayActive(true);
			ui.setTempTargetDisplayActive(true);
			ui.setHumidDisplayActive(true);
			ui.setHumidTargetDisplayActive(true);
			ui.setSoilMoistDisplayActive(true);
			ui.setSoilMoistTargetDisplayActive(true);
			
			ui.setTemperatureTargetDisplay(data.getTargetTemperature());
			ui.setHumidityTargetDisplay(data.getTargetHumidity());
			ui.setSoilMoistureTargetDisplay(data.getTargetSoilMoisture());
			
			GSController.startThreads();
			
		} else if (e.getActionCommand() == "Save Simulation") {
			
			String saveFileName = JOptionPane.showInputDialog(ui, "Enter save file name:", 
					null, JOptionPane.QUESTION_MESSAGE);
			
			try {
				PrintWriter saveFile =
						new PrintWriter(new FileOutputStream(saveFileName + ".txt"));
			
				saveFile.println(data.getTemperature());
				saveFile.println(data.getTargetTemperature());
				saveFile.println(data.getHumidity());
				saveFile.println(data.getTargetHumidity());
				saveFile.println(data.getSoilMoisture());
				saveFile.println(data.getTargetSoilMoisture());
				saveFile.println(data.getFurnaceEfficiency());
				saveFile.println(data.getAirConditionerEfficiency());
				saveFile.println(data.getHumidifierEfficiency());
				saveFile.println(data.getSprinklerEfficiency());
				saveFile.println(data.getSunnyDayTempChange());
				saveFile.println(data.getCloudyDayTempChange());
				saveFile.println(data.getRainyDayTempChange());
				saveFile.println(data.getSnowyDayTempChange());
				saveFile.println(data.getSunnyDayHumidChange());
				saveFile.println(data.getCloudyDayHumidChange());
				saveFile.println(data.getRainyDayHumidChange());
				saveFile.println(data.getSnowyDayHumidChange());
				saveFile.println(data.getSunnyDaySoilMoistChange());
				saveFile.println(data.getCloudyDaySoilMoistChange());
				saveFile.println(data.getRainyDaySoilMoistChange());
				saveFile.println(data.getSnowyDaySoilMoistChange());
				saveFile.println(data.getSampleRateTemp());
				saveFile.println(data.getSampleRateHumid());
				saveFile.println(data.getSampleRateSoilMoist());
				saveFile.println(data.getFurnaceOn());
				saveFile.println(data.getAirConditionerOn());
				saveFile.println(data.getHumidifierOn());
				saveFile.println(data.getSprinklerOn());
				saveFile.println(data.getWeather());

				JOptionPane.showMessageDialog(ui, "File was saved successfully!", 
						null, JOptionPane.INFORMATION_MESSAGE);
				
				saveFile.close();
			
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(ui, "Error saving file.", 
						null, JOptionPane.ERROR_MESSAGE);
				
			}
			
		} else if (e.getActionCommand() == "Load Simulation") {
			
			final JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(ui);
			File file = fc.getSelectedFile();
			Scanner inputStream;
			
			try {
				inputStream = new Scanner(new FileInputStream(file));
				
				while (inputStream.hasNextLine()) {
					System.out.println(inputStream.nextLine());
				}
				
				// TODO Set given values, start simulation
				
			} catch (FileNotFoundException e1) {
				// TODO something
			}
			
		} else {
			// TODO something
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
				data.setTargetHumidity(Integer.parseInt(inputFields[3].getText()));
				data.setSoilMoisture(Integer.parseInt(inputFields[4].getText()));
				data.setTargetSoilMoisture(Integer.parseInt(inputFields[5].getText()));
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
