package GreenhouseSimulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
	 * env: The simulation greenhouse
	 */
	private Greenhouse env;
	
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
	 * logControl: The log controller
	 */
	private LogController logControl;
	
	/**
	 * Constructor assigns given values
	 * @param ui: The simulation's gui
	 * @param env: The simulation greenhouse
	 * @param furnace: The furnace device
	 * @param airConditioner: The air conditioner device
	 * @param humidifier: The humidifier device
	 * @param sprinklerSystem: The sprinkler device
	 * @param tempControl: The temperature controller
	 * @param humidControl: The humidity controller
	 * @param soilMoistControl: The soil moisture controller
	 * @param logControl: The log controller
	 */
	public MenuListener (
			GUI ui, 
			Greenhouse env,
			Device furnace,
			Device airConditioner,
			Device humidifier,
			Device sprinklerSystem,
			Controller tempControl,
			Controller humidControl,
			Controller soilMoistControl,
			LogController logControl) {
		
		// Assign given values
		this.ui = ui;
		this.env = env;
		this.furnace = furnace;
		this.airConditioner = airConditioner;
		this.humidifier = humidifier;
		this.sprinklerSystem = sprinklerSystem;
		this.tempControl = tempControl;
		this.humidControl = humidControl;
		this.soilMoistControl = soilMoistControl;
		this.logControl = logControl;
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
		if (e.getActionCommand() == "Start simulation") {
			promptAndValidateInput(); 	// Get and validate user input
			
		// Menu Item: Save Simulation
		} else if (e.getActionCommand() == "Save current simulation") {
			
			// Prompt user for the save file's name
			String saveFileName = JOptionPane.showInputDialog(ui, "Enter save file name:", 
					"Save File", JOptionPane.QUESTION_MESSAGE);
			
			// Use try/catch to cover any possible error exceptions
			try {
				// Check if cancel or ok was clicked
				if (saveFileName != null) {
					
					// Create a new text file with the given name
					PrintWriter saveFile =
							new PrintWriter(new FileWriter(saveFileName + ".txt"));
				
					// Get log, then write the current simulation's latest state into file
					String[] log = logControl.getLog();
					for (String s : log) {
						saveFile.println(s.substring(s.lastIndexOf(" ")+1));
					}

					// Inform user that the simulation was successfully saved
					JOptionPane.showMessageDialog(ui, "File was saved successfully!", 
							"Save Successful", JOptionPane.INFORMATION_MESSAGE);
					
					// Close the newly created save file
					saveFile.close();
				}
				
			// In case of errors, let user know something went wrong
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Error saving file.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
		// Menu Item: Load Simulation
		} else if (e.getActionCommand() == "Load simulation") {
			
			// Create and display new file picker
			final JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(ui);
			
			// Get selected file
			File file = fc.getSelectedFile();
			
			// Create new Scanner for reading the file
			Scanner inputStream;
			
			// Use try/catch to cover any possible error exceptions
			try {
				// Check if file was chosen or cancel was clicked
				if (file != null) {
					
					// Initialize scanner with FileInputStream to read the file
					inputStream = new Scanner(new FileInputStream(file));
					
					// Update simulation with values from the save file
					// For each line in the save file, update the last recorded value in that line
					// TODO Update simulation log with all recorded values to be able to view saved log in the application
					String line = inputStream.nextLine();
					env.setTemperature(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					tempControl.setTarget(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					env.setHumidity(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidControl.setTarget(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					env.setSoilMoisture(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					soilMoistControl.setTarget(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					furnace.setEfficiency(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					airConditioner.setEfficiency(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidifier.setEfficiency(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					sprinklerSystem.setEfficiency(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					tempControl.setSunnyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					tempControl.setCloudyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					tempControl.setRainyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					tempControl.setSnowyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidControl.setSunnyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidControl.setCloudyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidControl.setRainyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidControl.setSnowyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					soilMoistControl.setSunnyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					soilMoistControl.setCloudyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					soilMoistControl.setRainyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					soilMoistControl.setSnowyDayChange(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					tempControl.setSampleRate(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidControl.setSampleRate(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					soilMoistControl.setSampleRate(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					furnace.setDeviceActive(Boolean.parseBoolean(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					airConditioner.setDeviceActive(Boolean.parseBoolean(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					humidifier.setDeviceActive(Boolean.parseBoolean(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					sprinklerSystem.setDeviceActive(Boolean.parseBoolean(line.substring(line.lastIndexOf(",")+1)));
					line = inputStream.nextLine();
					env.setWeatherIndex(Integer.parseInt(line.substring(line.lastIndexOf(",")+1)));
					
					// Update ui with loaded values
					ui.setTemperatureDisplay(env.getTemperature());
					ui.setTemperatureTargetDisplay(tempControl.getTarget());
					ui.setHumidityDisplay(env.getHumidity());
					ui.setHumidityTargetDisplay(humidControl.getTarget());
					ui.setSoilMoistureDisplay(env.getSoilMoisture());
					ui.setSoilMoistureTargetDisplay(soilMoistControl.getTarget());
					ui.setFurnaceChecked(furnace.getDeviceActive());
					ui.setAirConditionerChecked(airConditioner.getDeviceActive());
					ui.setHumidifierChecked(humidifier.getDeviceActive());
					ui.setSprinklerChecked(sprinklerSystem.getDeviceActive());
					ui.getWeatherDropDown().setSelectedIndex(env.getWeatherIndex());
					
					// Start simulation, if none was in progress before loading
					if (!Controller.getSimInProgress()) {
						Controller.setSimInProgress(true);
						Controller.startThreads();
					}
					
					// Pause loaded simulation to allow user to view its final state (can be resumed)
					Controller.setSimPaused(true);
					ui.setSimulationRunning(false);
				}
				
			// In case of errors, let user know something went wrong
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Error loading file.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			
		// Menu Item: Exit Program
		} else {
			
			// Prompt user to confirm exiting the program
			int exitProgramChoice = JOptionPane.showConfirmDialog(ui, "Exit Program?", 
					"Exit Program?", JOptionPane.OK_CANCEL_OPTION);
			
			// Exit program if user gives confirmation
			if (exitProgramChoice == 0) {
				System.exit(0);
			}
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
				env.setTemperature(Integer.parseInt(inputFields[0].getText()));
				tempControl.setTarget(Integer.parseInt(inputFields[1].getText()));
				env.setHumidity(Integer.parseInt(inputFields[2].getText()));
				humidControl.setTarget(Integer.parseInt(inputFields[3].getText()));
				env.setSoilMoisture(Integer.parseInt(inputFields[4].getText()));
				soilMoistControl.setTarget(Integer.parseInt(inputFields[5].getText()));
				furnace.setEfficiency(Integer.parseInt(inputFields[6].getText()));
				airConditioner.setEfficiency(Integer.parseInt(inputFields[7].getText()));
				humidifier.setEfficiency(Integer.parseInt(inputFields[8].getText()));
				sprinklerSystem.setEfficiency(Integer.parseInt(inputFields[9].getText()));
				
				// Mark step 1 as done
				step1Done = true;
			
			// In case of errors, let user know what went wrong
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
			
			// In case of errors, let user know something went wrong
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
			
			// In case of errors, let user know something went wrong
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
			
			// In case of errors, let user know something went wrong
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
					env.setWeatherIndex(0);
					ui.getWeatherDropDown().setSelectedIndex(0);
				} else if (weatherSelection == 1) {
					env.setWeatherIndex(1);
					ui.getWeatherDropDown().setSelectedIndex(1);
				} else if (weatherSelection == 2) {
					env.setWeatherIndex(2);
					ui.getWeatherDropDown().setSelectedIndex(2);
				} else {
					env.setWeatherIndex(3);
					ui.getWeatherDropDown().setSelectedIndex(3);
				}
				
				// Mark step 5 as done
				step5Done = true;
				
			// In case of errors, let user know something went wrong
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ui, "Please enter valid information.", 
						null, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		// If input is valid, start simulation and controllers, enable displays
		if (step1Done && step2Done && step3Done && step4Done && step5Done) {
			Controller.setSimInProgress(true);
			Controller.startThreads();
			ui.setSimulationRunning(true);
			
			// Set sliders' target values given by user
			ui.setTemperatureTargetDisplay(tempControl.getTarget());
			ui.setHumidityTargetDisplay(humidControl.getTarget());
			ui.setSoilMoistureTargetDisplay(soilMoistControl.getTarget());
		}
	}

}
