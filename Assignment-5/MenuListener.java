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

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class handles menu item behavior (Start, Save, Load, Quit)
 */

public class MenuListener implements ActionListener {

	/**
	 * ui: The simulation's graphical user interface
	 */
	private GUI ui;
	
	/**
	 * inputPanels: The JPanels containing user input components
	 */
	private JPanel[] inputPanels;
	
	/**
	 * greenhouse: The simulation greenhouse
	 */
	private Greenhouse greenhouse;
	
	/**
	 * tempControl: The temperature controller
	 */
	private Controller tempControl;
	
	/**
	 * humidControl: The humidity controller
	 */
	private Controller humidControl;
	
	/**
	 * soilMoistControl: The soil moisture controller
	 */
	private Controller soilMoistControl;
	
	/**
	 * furnace: The furnace device used to increase temperature
	 */
	private Device furnace;
	
	/**
	 * airConditioner: The air conditioner device used to decrease temperature
	 */
	private Device airConditioner;
	
	/**
	 * humidifier: The humidifier device used to increase humidity
	 */
	private Device humidifier;
	
	/**
	 * sprinklerSystem: The sprinkler device used to increase soil moisture
	 */
	private Device sprinklerSystem;
	
	/**
	 * Constructor assigns given values
	 * @param ui: The simulation's gui
	 * @param greenhouse: The simulation greenhouse
	 * @param tempControl: The temperature controller
	 * @param humidControl: The humidity controller
	 * @param soilMoistControl: The soil moisture controller
	 * @param furnace: The furnace device
	 * @param airConditioner: The air conditioner device
	 * @param humidifier: The humidifier device
	 * @param sprinklerSystem: The sprinkler device
	 */
	public MenuListener (
			GUI ui, 
			Greenhouse greenhouse,
			Controller tempControl,
			Controller humidControl,
			Controller soilMoistControl,
			Device furnace,
			Device airConditioner,
			Device humidifier,
			Device sprinklerSystem) {
		
		// Assign given values
		this.ui = ui;
		this.greenhouse = greenhouse;
		this.tempControl = tempControl;
		this.humidControl = humidControl;
		this.soilMoistControl = soilMoistControl;
		this.furnace = furnace;
		this.airConditioner = airConditioner;
		this.humidifier = humidifier;
		this.sprinklerSystem = sprinklerSystem;
		
	}
	
	/**
	 * Handles menu item behavior
	 * Start: Prompts user for input and starts the simulation
	 * Save: Saves the current simulation state to a text file
	 * Load: Prompts user to choose a simulation save file and loads it
	 * Exit: Exits the program
	 */
	public void actionPerformed(ActionEvent e) {
		
		// Menu Item: Start Simulation
		if (e.getActionCommand() == "Start Simulation") {
			
			// Get user input
			promptAndValidateInput();
			
			// Activate display sliders
			ui.setTempDisplayActive(true);
			ui.setTempTargetDisplayActive(true);
			ui.setHumidDisplayActive(true);
			ui.setHumidTargetDisplayActive(true);
			ui.setSoilMoistDisplayActive(true);
			ui.setSoilMoistTargetDisplayActive(true);
			
			// Set sliders' target values given by user
			ui.setTemperatureTargetDisplay(tempControl.getTarget());
			ui.setHumidityTargetDisplay(humidControl.getTarget());
			ui.setSoilMoistureTargetDisplay(soilMoistControl.getTarget());
			
		// Menu Item: Save Simulation
		} else if (e.getActionCommand() == "Save Simulation") {
			
			// Prompt user for the save file's name
			String saveFileName = JOptionPane.showInputDialog(ui, "Enter save file name:", 
					null, JOptionPane.QUESTION_MESSAGE);
			
			// Use try/catch to cover any possible error exceptions
			try {
				// Create a new text file with the given name
				PrintWriter saveFile =
						new PrintWriter(new FileOutputStream(saveFileName + ".txt"));
			
				// Write the current simulation's state into the text file
				saveFile.println(greenhouse.getTemperature());
				saveFile.println(tempControl.getTarget());
				saveFile.println(greenhouse.getHumidity());
				saveFile.println(humidControl.getTarget());
				saveFile.println(greenhouse.getSoilMoisture());
				saveFile.println(soilMoistControl.getTarget());
				saveFile.println(furnace.getEfficiency());
				saveFile.println(airConditioner.getEfficiency());
				saveFile.println(humidifier.getEfficiency());
				saveFile.println(sprinklerSystem);
				saveFile.println(tempControl.getSunnyDayChange());
				saveFile.println(tempControl.getCloudyDayChange());
				saveFile.println(tempControl.getRainyDayChange());
				saveFile.println(tempControl.getSnowyDayChange());
				saveFile.println(humidControl.getSunnyDayChange());
				saveFile.println(humidControl.getCloudyDayChange());
				saveFile.println(humidControl.getRainyDayChange());
				saveFile.println(humidControl.getSnowyDayChange());
				saveFile.println(soilMoistControl.getSunnyDayChange());
				saveFile.println(soilMoistControl.getCloudyDayChange());
				saveFile.println(soilMoistControl.getRainyDayChange());
				saveFile.println(soilMoistControl.getSnowyDayChange());
				saveFile.println(tempControl.getSampleRate());
				saveFile.println(humidControl.getSampleRate());
				saveFile.println(soilMoistControl.getSampleRate());
				saveFile.println(furnace.getDeviceActive());
				saveFile.println(airConditioner.getDeviceActive());
				saveFile.println(humidifier.getDeviceActive());
				saveFile.println(sprinklerSystem.getDeviceActive());
				saveFile.println(greenhouse.getWeather());

				// Inform user that the simulation was successfully saved
				// TODO only do this if cancel was not clicked
				JOptionPane.showMessageDialog(ui, "File was saved successfully!", 
						null, JOptionPane.INFORMATION_MESSAGE);
				
				// Close the newly created save file
				saveFile.close();
			
			// In case of errors, let user know what went wrong
			// TODO add various exceptions to cover all error cases
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(ui, "Error saving file.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
			
		// Menu Item: Load Simulation
		} else if (e.getActionCommand() == "Load Simulation") {
			
			// Create and display new file picker
			final JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(ui);
			
			// Get selected file
			File file = fc.getSelectedFile();
			
			// Create new Scanner for reading the file
			Scanner inputStream;
			
			// Use try/catch to cover any possible error exceptions
			try {
				// Initialize scanner with FileInputStream to read the file
				inputStream = new Scanner(new FileInputStream(file));
				
				// Iterate through all lines in the text file
				while (inputStream.hasNextLine()) {
					System.out.println(inputStream.nextLine());
					// TODO Set given values, start simulation
				}
				
			// In case of errors, let user know what went wrong
			// TODO add various exceptions to cover all error cases
			} catch (FileNotFoundException e1) {
				// TODO something
			}
			
		// Menu Item: Exit Program
		} else {
			// TODO something
			System.exit(0);
		}
		
	}
	
	/**
	 * Creates user input dialogs
	 * Prompts user for input
	 * Validates user input, starts simulation if validation was successful
	 */
	public void promptAndValidateInput () {
		
		// Create validation booleans for each input step
		boolean step1Done = false, step2Done = false, step3Done = false, 
				step4Done = false, step5Done = false;
		
		// Create input panels
		inputPanels = ui.createInputPanels();
		
		// Get all text fields from the input panels
		JTextField[] inputFields = ui.getInputFields();
		
		// Step 1: Prompt user for first set of inputs
		while (!step1Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[0], 
					"Step 1 of 5: Please enter simulation parameters", JOptionPane.QUESTION_MESSAGE);
			
			// Check if given values are of the correct format (integers)
			try {
				// If input is valid, assign it to its respective storage location
				greenhouse.setTemperature(Integer.parseInt(inputFields[0].getText()));
				tempControl.setTarget(Integer.parseInt(inputFields[1].getText()));
				greenhouse.setHumidity(Integer.parseInt(inputFields[2].getText()));
				humidControl.setTarget(Integer.parseInt(inputFields[3].getText()));
				greenhouse.setSoilMoisture(Integer.parseInt(inputFields[4].getText()));
				soilMoistControl.setTarget(Integer.parseInt(inputFields[5].getText()));
				furnace.setEfficiency(Integer.parseInt(inputFields[6].getText()));
				airConditioner.setEfficiency(Integer.parseInt(inputFields[7].getText()));
				humidifier.setEfficiency(Integer.parseInt(inputFields[8].getText()));
				sprinklerSystem.setEfficiency(Integer.parseInt(inputFields[9].getText()));
				
				// Mark step 1 as done
				step1Done = true;
			
			// In case of errors, let user know what went wrong
			// TODO add various exceptions to cover all error cases
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// Step 2: Prompt user for second set of inputs
		while (!step2Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[1], 
					"Step 2 of 5: Please enter weather effect on temperature", JOptionPane.QUESTION_MESSAGE);
			
			// Check if given values are of the correct format (integers)
			try {
				// If input is valid, assign it to its respective storage location
				tempControl.setSunnyDayChange(Integer.parseInt(inputFields[10].getText()));
				tempControl.setCloudyDayChange(Integer.parseInt(inputFields[11].getText()));
				tempControl.setRainyDayChange(Integer.parseInt(inputFields[12].getText()));
				tempControl.setSnowyDayChange(Integer.parseInt(inputFields[13].getText()));
				
				// Mark step 2 as done
				step2Done = true;
			
			// In case of errors, let user know what went wrong
			// TODO add various exceptions to cover all error cases
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// Step 3: Prompt user for third set of inputs
		while (!step3Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[2], 
					"Step 3 of 5: Please enter sweather effect on humidity", JOptionPane.QUESTION_MESSAGE);
			
			// Check if given values are of the correct format (integers)
			try {
				// If input is valid, assign it to its respective storage location
				humidControl.setSunnyDayChange(Integer.parseInt(inputFields[14].getText()));
				humidControl.setCloudyDayChange(Integer.parseInt(inputFields[15].getText()));
				humidControl.setRainyDayChange(Integer.parseInt(inputFields[16].getText()));
				humidControl.setSnowyDayChange(Integer.parseInt(inputFields[17].getText()));
				
				// Mark step 3 as done
				step3Done = true;
			
			// In case of errors, let user know what went wrong
			// TODO add various exceptions to cover all error cases
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// Step 4: Prompt user for fourth set of inputs
		while (!step4Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[3], 
					"Step 4 of 5: Please enter weather effect on soil moisture", JOptionPane.QUESTION_MESSAGE);
			
			// Check if given values are of the correct format (integers)
			try {
				// If input is valid, assign it to its respective storage location
				soilMoistControl.setSunnyDayChange(Integer.parseInt(inputFields[18].getText()));
				soilMoistControl.setCloudyDayChange(Integer.parseInt(inputFields[19].getText()));
				soilMoistControl.setRainyDayChange(Integer.parseInt(inputFields[20].getText()));
				soilMoistControl.setSnowyDayChange(Integer.parseInt(inputFields[21].getText()));
				
				// Mark step 4 as done
				step4Done = true;
			
			// In case of errors, let user know what went wrong
			// TODO add various exceptions to cover all error cases
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// Step 5: Prompt user for fifth and last set of inputs
		while (!step5Done) {
			JOptionPane.showMessageDialog(ui, inputPanels[4], 
					"Step 5 of 5: Please enter display update frequency", JOptionPane.QUESTION_MESSAGE);
			
			// Check if given values are of the correct format (integers)
			try {
				// If input is valid, assign it to its respective storage location
				tempControl.setSampleRate(Integer.parseInt(inputFields[22].getText()));
				humidControl.setSampleRate(Integer.parseInt(inputFields[23].getText()));
				soilMoistControl.setSampleRate(Integer.parseInt(inputFields[24].getText()));
				
				// Get the selected weather drop down item 
				int weatherSelection = ui.getWeatherUserSelection().getSelectedIndex();
				
				// Set the weather condition depending on the chosen drop down item
				if (weatherSelection == 0) {
					greenhouse.setWeather("Sunny");
					ui.getWeatherDropDown().setSelectedIndex(0);
				} else if (weatherSelection == 1) {
					greenhouse.setWeather("Cloudy");
					ui.getWeatherDropDown().setSelectedIndex(1);
				} else if (weatherSelection == 2) {
					greenhouse.setWeather("Rainy");
					ui.getWeatherDropDown().setSelectedIndex(2);
				} else {
					greenhouse.setWeather("Snowy");
					ui.getWeatherDropDown().setSelectedIndex(3);
				}
				
				// Mark step 5 as done
				step5Done = true;
				
			// In case of errors, let user know what went wrong
			// TODO add various exceptions to cover all error cases
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// If all steps were passed successfully, start the simulation and controllers
		if (step1Done && step2Done && step3Done && step4Done && step5Done) {
			Controller.setSimInProgress(true);
			Controller.startThreads(tempControl, humidControl, soilMoistControl);
		}
	}

}
