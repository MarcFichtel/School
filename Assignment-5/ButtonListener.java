import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class handles button behavior (Pause, Resume, Edit, Change weather, Change target value)
 */

// TODO Bug: The step time is incorrect when any sample rate is edited throughout a simulation
// TODO Missing feature: Log is not included in save file, so loaded simulations cannot show log from before
// TODO Nice-To-Have: Get column header (run time) from Time Controller to avoid code duplication
public class ButtonListener implements ActionListener, ChangeListener {

	/**
	 * ui: The simulation's graphical user interface
	 */
	private GUI ui;
	
	/**
	 * env: The simulation greenhouse
	 */
	private Greenhouse env;

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
	 * menuListener: Listener used to handle menu events 
	 * Used for prompting and validating edited simulation parameters here
	 */
	private MenuListener menuListener;
	
	/**
	 * logControl: The log controller
	 */
	private LogController logControl;
	/**
	 * log: The current simulation's log
	 */
	private String[] log;
	
	/**
	 *  updateFrequency: The log update frequency
	 */
	private int updateFrequency;
	
	/**
	 * Constructor assigns the given values
	 * @param ui: The program's GUI
	 * @param env: The greenhouse environment
	 * @param tempControl: The temperature controller
	 * @param humidControl: The humidity controller
	 * @param soilMoistControl: The soil moisture controller
	 * @param menuListener: The menu event handler
	 */
	public ButtonListener (
			GUI ui, 
			Greenhouse env, 
			Controller tempControl, 
			Controller humidControl, 
			Controller soilMoistControl,
			MenuListener menuListener,
			LogController logControl) {
		
		this.ui = ui;
		this.env = env;
		this.tempControl = tempControl;
		this.humidControl = humidControl;
		this.soilMoistControl = soilMoistControl;
		this.menuListener = menuListener;
		this.logControl = logControl;
		this.log = logControl.getLog();
	}
	
	/**
	 * Handles button behavior
	 * Pause: Pauses the running simulation
	 * Resume: Resumes the paused simulation
	 * Edit: Edit simulation parameters
	 * Weather: Update simulation, if weather drop down selection is changed
	 */
	public void actionPerformed(ActionEvent e) {
		
		// Pause simulation
		if (e.getActionCommand() == "Pause") {
			Controller.setSimPaused(true);
			ui.setSimulationRunning(false);
		
		// Resume paused simulation
		} else if (e.getActionCommand() == "Resume") {
			Controller.setSimPaused(false);
			ui.setSimulationRunning(true);
		
		// Edit simulation parameters
		} else if (e.getActionCommand() == "Edit Sim.") {
			
			// Pause simulation as before editing parameters
			Controller.setSimPaused(true);
			ui.setSimulationRunning(false);
			
			// Prompt user for new parameters and validate them
			menuListener.promptAndValidateInput();
			
			
			// Resume the simulation
			Controller.setSimPaused(false);
			ui.setSimulationRunning(true);
		
		// Show log
		} else if (e.getActionCommand() == "Show Log") {
			
			// Get how many columns (i.e. log entries) the table needs
			int cols = 0;
			String[] logLine = log[0].split(",");
			for (String s : logLine) {
				if (s != ",") {
					cols += 1;
				}
			}
			
			// Make header column names (simulation duration - 1 simulation second = 1 actual minute)
			updateFrequency = logControl.getUpdateFrequency();
			String[] columnHeader = new String[cols];
			int hours = 0;
			int minutes = 0;
			
			for (int i = 0; i < cols; i++) {
				
				// Enter some text in first cell
				if (i == 0) {
					columnHeader[i] = "Initial Values at 00:00:00";
				
				// Show time log step in other column headers
				} else {
					hours = (i/60 - i/60/60) * updateFrequency;
					minutes = (i%60) * updateFrequency;

					// Adjust time display to show two digit zeros
					if (minutes < 10 && hours > 10) {
						columnHeader[i] = hours + ":0" + minutes + ":00";
					
					} else if (minutes > 10 && hours < 10) {
						columnHeader[i] = "0" + hours + ":" + minutes + ":00";
					
					} else if (minutes > 10 && hours > 10) {
						columnHeader[i] = hours + ":" + minutes + ":00";
					
					} else {
						columnHeader[i] = "0" + hours + ":0" + minutes + ":00";
					}
				}
			}
			
			// Create Object array holding data to display in table
			int rows = log.length;
			Object[][] data = new Object[rows][cols];
			
			// Populate object array with log data
			for (int i = 0; i < rows; i++) {		// Iterate through rows
				String[] line = log[i].split(","); 	// Split each line into separate values
				for (int j = 0; j < cols; j++) { 	// Iterate through columns
					
					// Show weather condition instead of weather index
					if (i == rows - 1 && j != 0) {
						if (Integer.parseInt(line[j]) == 0) {
							data[i][j] = "Sunny";
						} else if (Integer.parseInt(line[j]) == 1) {
							data[i][j] = "Cloudy";
						} else if (Integer.parseInt(line[j]) == 2) {
							data[i][j] = "Rainy";
						} else if (Integer.parseInt(line[j]) == 3) {
							data[i][j] = "Snowy";
						}
					
					// Enter the corresponding value into each cell
					} else {
						data[i][j] = line[j];
					}
				}
			}
			
			// Create new JTable
			JTable logTable = new JTable(data, columnHeader);
			
			// Disable auto resizing
			logTable.setAutoResizeMode(0);

			// Resize first column
			TableColumnModel tcm = logTable.getColumnModel();
			tcm.getColumn(0).setPreferredWidth(300);
			
			// Insert table in scroll pane
			JScrollPane scrollPane = new JScrollPane(logTable);
			
			// Open new window, add scroll pane
			JFrame logFrame = new JFrame("Log");
			logFrame.setSize(1000, 600);
			logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			logFrame.getContentPane().add(scrollPane);
			logFrame.setVisible(true);
			
		// Update weather
		} else {
			JComboBox<String> weather = (JComboBox<String>) e.getSource();
			env.setWeatherIndex(weather.getSelectedIndex());
		}
		
	}

	/**
	 * Handles target value slider adjustment
	 */
	public void stateChanged(ChangeEvent e) {
		
		// Get the slider that is being adjusted
		JSlider slider = (JSlider) e.getSource();
		
		// Update target temperature
		if (slider.getName() == "TempTarget") {
			tempControl.setTarget(slider.getValue());
		
		// Update target humidity
		} else if (slider.getName() == "HumidTarget") {
			humidControl.setTarget(slider.getValue());
		
		// Update target soil moisture
		} else {
			soilMoistControl.setTarget(slider.getValue());
		}
	}
}
