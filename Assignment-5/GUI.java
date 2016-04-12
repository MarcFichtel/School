package GreenhouseSimulator;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edwin Chan 
 * -- Class represents the user interface of the greenhouse simulation
 */

public class GUI extends JFrame {
	
	/**
	 * sTemperature: The slider displaying the temperature
	 */
	private JSlider sTemperature = new JSlider(JSlider.VERTICAL, 10, 40, 10);
	
	/**
	 * sTemperatureTarget: The slider displaying the target temperature
	 */
	private JSlider sTemperatureTarget = new JSlider(JSlider.VERTICAL, 10, 40, 10);
	
	/**
	 * sHumidity: The slider displaying the humidity
	 */
	private JSlider sHumidity = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	
	/**
	 * sHumidityTarget: The slider displaying the target humidity
	 */
	private JSlider sHumidityTarget = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	
	/**
	 * sSoilMoisture: The slider displaying the soil moisture
	 */
	private JSlider sSoilMoisture = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	
	/**
	 * sSoilMoistureTarget: The slider displaying the target soil moisture
	 */
	private JSlider sSoilMoistureTarget = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	
	/**
	 * cbFurnace: The check box displaying whether the furnace is running or not 
	 */
	private JCheckBox cbFurnace = new JCheckBox("Furnace");
	
	/**
	 * cbAirConditioner: The check box displaying whether the air conditioner is running or not
	 */
	private JCheckBox cbAirConditioner = new JCheckBox("Air Conditioner");
	
	/**
	 * cbHumidifier: The check box displaying whether the humidifier is running or not
	 */
	private JCheckBox cbHumidifier = new JCheckBox("Humidifier");
	
	/**
	 * cbSprinkler: The check box displaying whether the sprinkler is running or not
	 */
	private JCheckBox cbSprinkler = new JCheckBox("Sprinkler System");
	
	/**
	 * tfInput01: The 1. user input text field (prompting for temperature)
	 */
	private JTextField tfInput01 = new JTextField("20", 4);
	
	/**
	 * tfInput02: The 2. user input text field (prompting for target temperature)
	 */
	private JTextField tfInput02 = new JTextField("25", 4);
	
	/**
	 * tfInput03: The 3. user input text field (prompting for humidity)
	 */
	private JTextField tfInput03 = new JTextField("50", 4);
	
	/**
	 * tfInput04: The 4. user input text field (prompting for target humidity)
	 */
	private JTextField tfInput04 = new JTextField("50", 4);
	
	/**
	 * tfInput05: The 5. user input text field (prompting for soil moisture)
	 */
	private JTextField tfInput05 = new JTextField("50", 4);
	
	/**
	 * tfInput06: The 6. user input text field (prompting for target soil moisture)
	 */
	private JTextField tfInput06 = new JTextField("50", 4);
	
	/**
	 * tfInput07: The 7. user input text field (prompting for furnace efficiency)
	 */
	private JTextField tfInput07 = new JTextField("2", 4);
	
	/**
	 * tfInput08: The 8. user input text field (prompting for air conditioner efficiency)
	 */
	private JTextField tfInput08 = new JTextField("-2", 4);
	
	/**
	 * tfInput09: The 9. user input text field (prompting for humidifier efficiency)
	 */
	private JTextField tfInput09 = new JTextField("2", 4);
	
	/**
	 * tfInput10: The 10. user input text field (prompting for sprinkler efficiency)
	 */
	private JTextField tfInput10 = new JTextField("2", 4);
	
	/**
	 * tfInput11: The 11. user input text field (prompting for )
	 */
	private JTextField tfInput11 = new JTextField("1", 4);
	
	/**
	 * tfInput12: The 12. user input text field (prompting for )
	 */
	private JTextField tfInput12 = new JTextField("0", 4);
	
	/**
	 * tfInput13: The 13. user input text field (prompting for )
	 */
	private JTextField tfInput13 = new JTextField("0", 4);
	
	/**
	 * tfInput14: The 14. user input text field (prompting for )
	 */
	private JTextField tfInput14 = new JTextField("-1", 4);
	
	/**
	 * tfInput15: The 15. user input text field (prompting for )
	 */
	private JTextField tfInput15 = new JTextField("-1", 4);
	
	/**
	 * tfInput16: The 16. user input text field (prompting for )
	 */
	private JTextField tfInput16 = new JTextField("0", 4);
	
	/**
	 * tfInput17: The 17. user input text field (prompting for )
	 */
	private JTextField tfInput17 = new JTextField("1", 4);
	
	/**
	 * tfInput18: The 18. user input text field (prompting for )
	 */
	private JTextField tfInput18 = new JTextField("0", 4);
	
	/**
	 * tfInput19: The 19. user input text field (prompting for )
	 */
	private JTextField tfInput19 = new JTextField("0", 4);
	
	/**
	 * tfInput20: The 20. user input text field (prompting for )
	 */
	private JTextField tfInput20 = new JTextField("0", 4);
	
	/**
	 * tfInput21: The 21. user input text field (prompting for )
	 */
	private JTextField tfInput21 = new JTextField("1", 4);
	
	/**
	 * tfInput22: The 22. user input text field (prompting for )
	 */
	private JTextField tfInput22 = new JTextField("0", 4);
	
	/**
	 * tfInput23: The 23. user input text field (prompting for )
	 */
	private JTextField tfInput23 = new JTextField("1", 4);
	
	/**
	 * tfInput24: The 24. user input text field (prompting for )
	 */
	private JTextField tfInput24 = new JTextField("1", 4);
	
	/**
	 * tfInput25: The 25. user input text field (prompting for )
	 */
	private JTextField tfInput25 = new JTextField("1", 4);
	
	/**
	 * bEditSimulation: The button for editing the current simulation
	 */
	private JButton bEditSimulation  = new JButton("Edit Sim.");
	
	/**
	 * bPauseSimulation: The button for pausing the current simulation
	 */
	private JButton bPauseSimulation  = new JButton("Pause");
	
	/**
	 * bResumeSimulation: The button for resuming the current simulation
	 */
	private JButton bResumeSimulation  = new JButton("Resume");
	
	/**
	 * bShowLog: The button for showing the simulation's log
	 */
	private JButton bShowLog  = new JButton("Show Log");
	
	/**
	 * comboWeather: The main frame weather drop down
	 */
	private JComboBox<String> comboWeather = new JComboBox<String>();
	
	/**
	 * comboWeatherSelect: The user input weather drop down (user input step 5)
	 */
	private JComboBox<String> comboWeatherSelect = new JComboBox<String>();
	
	/**
	 * iconRed: Icon holding a red dot
	 */
	private ImageIcon iconRed = new ImageIcon("red.gif");
	
	/**
	 * iconGreen: Icon holding a green dot
	 */
	private ImageIcon iconGreen = new ImageIcon("green.jpg");
	
	/**
	 * tempStatus: Label displaying the temperature's status
	 */
	private JLabel tempStatus = new JLabel();
	
	/**
	 * humidStatus: Label displaying the humidity's status
	 */
	private JLabel humidStatus = new JLabel();
	
	/**
	 * soilMoistStatus: Label displaying the soil moisture's status
	 */
	private JLabel soilMoistStatus = new JLabel();
	
	/**
	 * runTime: Label displaying the current simulation's run time
	 */
	private JLabel runTime = new JLabel("00:00:00");
	
	/**
	 * Constructor sets up the JFrame
	 */
 	public GUI() {
		super("Greenhouse Simulation");					// Set frame title
		setSize(750, 900); 								// Set frame size
		setLayout(new GridLayout(3, 3, 10, 10));				// Set 3x3 grid layout
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Set default close operation
	}
	
 	/**
 	 * Sets up the GUI and adds an action listener
 	 * @param menuListener: The menu action listener
 	 */
	public void initUI (ActionListener menuListener) {
		
		// Create menu components
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		JMenuItem menuStart = new JMenuItem("Start simulation");
		JMenuItem menuSave = new JMenuItem("Save current simulation");
		JMenuItem menuLoad = new JMenuItem("Load simulation");
		JMenuItem menuExit = new JMenuItem("Exit program");
		
		// Set menu item short cuts
		menuStart.setAccelerator(KeyStroke.getKeyStroke("F1"));
		menuSave.setAccelerator(KeyStroke.getKeyStroke("F2"));
		menuLoad.setAccelerator(KeyStroke.getKeyStroke("F3"));
		menuExit.setAccelerator(KeyStroke.getKeyStroke("F4"));
		
		// Create grid panels
		JPanel pTopLeft = new JPanel(new GridLayout(4, 1));
		JPanel pTopCenter = new JPanel(new GridLayout(4, 1));
		JPanel pTopRight = new JPanel(new GridLayout(4, 1));
		JPanel pTemperature = new JPanel(new GridLayout(1,2));
		JPanel pHumidity = new JPanel(new GridLayout(1,2));
		JPanel pSoilMoisture = new JPanel(new GridLayout(1,2));
		JPanel pBottomLeft = new JPanel(new GridLayout(4, 1));
		JPanel pTempControl = new JPanel(new FlowLayout());
		JPanel pBottomCenter = new JPanel(new GridLayout(4, 1));
		JPanel pBottomRight = new JPanel(new GridLayout(4, 1));
		JPanel pWeatherSelect = new JPanel(new FlowLayout());
		JPanel pEditButtonsLeft = new JPanel(new GridLayout(2, 2, 10, 10));
		JPanel pEditButtonsRight = new JPanel(new GridLayout(2, 2, 10, 10));
		JPanel pTempStatus = new JPanel(new GridLayout(3,3));
		JPanel pHumidStatus = new JPanel(new GridLayout(3,3));
		JPanel pSoilMoistStatus = new JPanel(new GridLayout(3,3));
		JPanel pRunTime = new JPanel(new GridLayout(1,3));
		
		// Create various labels
		JLabel lTitle = new JLabel("Green Thumbs Greenhouses", JLabel.CENTER);
		JLabel lSubTitle = new JLabel("Automatic Climate Control", JLabel.CENTER);
		JLabel lTemperature = new JLabel("<html><div style = 'text-align: center;'>"
				+ "Temperature (C)"
				+ "<br><br>"
				+ "Actual: "
				+ "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
				+ "Target:" 
				+ "</html>", JLabel.CENTER);
		JLabel lHumidity = new JLabel("<html><div style = 'text-align: center;'>"
				+ "Humidity (%)"
				+ "<br><br>"
				+ "Actual: "
				+ "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
				+ "Target:" 
				+ "</html>", JLabel.CENTER);
		JLabel lSoilMoisture = new JLabel("<html><div style = 'text-align: center;'>"
				+ "Soil Moisture (%)"
				+ "<br><br>"
				+ "Actual: "
				+ "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
				+ "Target:" 
				+ "</html>", JLabel.CENTER);
		JLabel lWeather = new JLabel("Weather Condition:", JLabel.CENTER);
		
		// Create hash tables for slider labeling
		Hashtable<Integer, JLabel> temperatureLabels = new Hashtable<Integer, JLabel>();
		Hashtable<Integer, JLabel> humidityLabels = new Hashtable<Integer, JLabel>();
		Hashtable<Integer, JLabel> soilMoistureLabels = new Hashtable<Integer, JLabel>();
		
		// Adjust title font and placement
		lTitle.setFont(new Font("Georgia", Font.BOLD, 15));
		lSubTitle.setFont(new Font("Georgia", Font.ITALIC, 13));
		lTitle.setVerticalAlignment(JLabel.BOTTOM);
		lSubTitle.setVerticalAlignment(JLabel.TOP);
		
		// Adjust check box placement
		cbHumidifier.setHorizontalAlignment((int)Button.CENTER_ALIGNMENT);
		cbSprinkler.setHorizontalAlignment((int)Button.CENTER_ALIGNMENT);
		
		// Fill slider label hash tables with values to be displayed
		for (int i = 10; i < 41; i+= 5) {
			temperatureLabels.put(new Integer(i), new JLabel("  " + Integer.toString(i) + "°"));
		}
		for (int i = 0; i < 101; i += 10) {
			humidityLabels.put(new Integer(i), new JLabel("  " + Integer.toString(i) + "%"));
		}
		for (int i = 0; i < 101; i += 10) {
			soilMoistureLabels.put(new Integer(i), new JLabel("  " + Integer.toString(i) + "%"));
		}
		
		// Connect label hash tables to sliders
		sTemperature.setLabelTable(temperatureLabels);
		sTemperatureTarget.setLabelTable(temperatureLabels);
		sHumidity.setLabelTable(humidityLabels);
		sHumidityTarget.setLabelTable(humidityLabels);
		sSoilMoisture.setLabelTable(soilMoistureLabels);
		sSoilMoistureTarget.setLabelTable(soilMoistureLabels);
		
		// Display slider labels
		sTemperature.setPaintLabels(true);
		sTemperatureTarget.setPaintLabels(true);
		sHumidity.setPaintLabels(true);
		sHumidityTarget.setPaintLabels(true);
		sSoilMoisture.setPaintLabels(true);
		sSoilMoistureTarget.setPaintLabels(true);
		
		// Define and display slider ticks
		sTemperature.setMajorTickSpacing(1);
		sTemperature.setPaintTicks(true);
		sTemperatureTarget.setMajorTickSpacing(1);
		sTemperatureTarget.setPaintTicks(true);
		sHumidity.setMajorTickSpacing(5);
		sHumidity.setPaintTicks(true);
		sHumidityTarget.setMajorTickSpacing(5);
		sHumidityTarget.setPaintTicks(true);
		sSoilMoisture.setMajorTickSpacing(5);
		sSoilMoisture.setPaintTicks(true);
		sSoilMoistureTarget.setMajorTickSpacing(5);
		sSoilMoistureTarget.setPaintTicks(true);
		
		// Give target sliders name for change listener accessibility
		sTemperatureTarget.setName("TempTarget");
		sHumidityTarget.setName("HumidTarget");
		sSoilMoistureTarget.setName("SoilMoistTarget");
		
		// Create weather drop down in main frame
		comboWeather.addItem(new String("Sunny"));
		comboWeather.addItem(new String("Cloudy"));
		comboWeather.addItem(new String("Rainy"));
		comboWeather.addItem(new String("Snowy"));
		
		// Disable all interactive components when program is started
		sTemperature.setEnabled(false);
		sTemperatureTarget.setEnabled(false);
		sHumidity.setEnabled(false);
		sHumidityTarget.setEnabled(false);
		sSoilMoisture.setEnabled(false);
		sSoilMoistureTarget.setEnabled(false);
		bEditSimulation.setEnabled(false);
		bPauseSimulation.setEnabled(false);
		bResumeSimulation.setEnabled(false);
		bShowLog.setEnabled(false);
		cbFurnace.setEnabled(false);	
		cbAirConditioner.setEnabled(false);	
		cbHumidifier.setEnabled(false);		
		cbSprinkler.setEnabled(false);	
		comboWeather.setEnabled(false);
		
		// Add start menu to main frame
		menu.add(menuStart);
		menu.add(menuSave);
		menu.add(menuLoad);
		menu.add(menuExit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		// Add menu listener to menu items
		menuStart.addActionListener(menuListener);
		menuSave.addActionListener(menuListener);
		menuLoad.addActionListener(menuListener);
		menuExit.addActionListener(menuListener);
		
		// Add components to temperature status panel (done to resize icon)
		pTempStatus.add(new JLabel(""));
		pTempStatus.add(new JLabel(""));
		pTempStatus.add(new JLabel(""));
		pTempStatus.add(new JLabel(""));
		pTempStatus.add(new JLabel(""));
		pTempStatus.add(new JLabel(""));
		pTempStatus.add(new JLabel(""));
		pTempStatus.add(tempStatus);
		pTempStatus.add(new JLabel(""));
		
		// Add components to humidity status panel (done to resize icon)
		pHumidStatus.add(new JLabel(""));
		pHumidStatus.add(new JLabel(""));
		pHumidStatus.add(new JLabel(""));
		pHumidStatus.add(new JLabel(""));
		pHumidStatus.add(new JLabel(""));
		pHumidStatus.add(new JLabel(""));
		pHumidStatus.add(new JLabel(""));
		pHumidStatus.add(humidStatus);
		pHumidStatus.add(new JLabel(""));
		
		// Add components to soil moisture status panel (done to resize icon)
		pSoilMoistStatus.add(new JLabel(""));
		pSoilMoistStatus.add(new JLabel(""));
		pSoilMoistStatus.add(new JLabel(""));
		pSoilMoistStatus.add(new JLabel(""));
		pSoilMoistStatus.add(new JLabel(""));
		pSoilMoistStatus.add(new JLabel(""));
		pSoilMoistStatus.add(new JLabel(""));
		pSoilMoistStatus.add(soilMoistStatus);
		pSoilMoistStatus.add(new JLabel(""));		
		
		// Add components to top left panel
		pTopLeft.add(new JLabel(""));
		pTopLeft.add(new JLabel(""));
		pTopLeft.add(pTempStatus);
		pTopLeft.add(lTemperature);
		
		// Add components to top center panel
		pTopCenter.add(lTitle);
		pTopCenter.add(lSubTitle);
		pTopCenter.add(pHumidStatus);
		pTopCenter.add(lHumidity);
		
		// Add components to run time panel
		pRunTime.add(new JLabel(""));
		pRunTime.add(new JLabel(""));
		pRunTime.add(runTime);
		
		// Add components to top right panel
		pTopRight.add(pRunTime);
		pTopRight.add(new JLabel(""));
		pTopRight.add(pSoilMoistStatus);
		pTopRight.add(lSoilMoisture);
		
		// Add components to temperature panel (center left)
		pTemperature.add(sTemperature);
		pTemperature.add(sTemperatureTarget);
		
		// Add components to humidity panel (center)
		pHumidity.add(sHumidity);
		pHumidity.add(sHumidityTarget);
		
		// Add components to soil moisture panel (center right)
		pSoilMoisture.add(sSoilMoisture);
		pSoilMoisture.add(sSoilMoistureTarget);
		
		// Add components to temperature sub-panel
		pTempControl.add(new JLabel(" "));
		pTempControl.add(cbFurnace);
		pTempControl.add(new JLabel("              "));
		pTempControl.add(cbAirConditioner);
		
		// Add weather drop down to a panel
		pWeatherSelect.add(comboWeather);
		
		// Add components to bottom left panel
		pBottomLeft.add(pTempControl);
		pBottomLeft.add(lWeather);
		pBottomLeft.add(pWeatherSelect);
		pBottomLeft.add(new JLabel(""));
		
		// Add simulation edit buttons to a panel
		pEditButtonsLeft.add(new JLabel(""));
		pEditButtonsLeft.add(bPauseSimulation);
		pEditButtonsLeft.add(new JLabel(""));
		pEditButtonsLeft.add(bEditSimulation);
				
		// Add simulation edit buttons to a panel
		pEditButtonsRight.add(bResumeSimulation);
		pEditButtonsRight.add(new JLabel(""));
		pEditButtonsRight.add(bShowLog);
		pEditButtonsRight.add(new JLabel(""));
		
		// Add components to bottom center panel
		pBottomCenter.add(cbHumidifier);
		pBottomCenter.add(new JLabel(""));
		pBottomCenter.add(pEditButtonsLeft);
		pBottomCenter.add(new JLabel(""));
				
		// Add components to bottom right panel
		pBottomRight.add(cbSprinkler);
		pBottomRight.add(new JLabel(""));
		pBottomRight.add(pEditButtonsRight);
		pBottomRight.add(new JLabel(""));
		
		// Add all grid panels to the frame in order
		this.getContentPane().add(pTopLeft);
		this.getContentPane().add(pTopCenter);
		this.getContentPane().add(pTopRight);	
		this.getContentPane().add(pTemperature);
		this.getContentPane().add(pHumidity);
		this.getContentPane().add(pSoilMoisture);	
		this.getContentPane().add(pBottomLeft);
		this.getContentPane().add(pBottomCenter);
		this.getContentPane().add(pBottomRight);
		
		// Make frame visible
		this.setVisible(true);
	}
	
	/**
	 * Creates an array of panels containing user input components
	 * @return inputPanels: The array of panels containing user input components
	 */
	public JPanel[] createInputPanels () {
		
		// Create user input help text labels
		JLabel lInputText01 = new JLabel("Enter initial temperature:");
		JLabel lInputText02 = new JLabel("Enter desired temperature:");
		JLabel lInputText03 = new JLabel("Enter humidity:");
		JLabel lInputText04 = new JLabel("Enter desired humidity:");
		JLabel lInputText05 = new JLabel("Enter soil moisture:");
		JLabel lInputText06 = new JLabel("Enter desired soil moisture:");
		JLabel lInputText07 = new JLabel("Enter furnace efficiency:");
		JLabel lInputText08 = new JLabel("Enter air conditioner efficiency:");
		JLabel lInputText09 = new JLabel("Enter humidifier efficiency:");
		JLabel lInputText10 = new JLabel("Enter sprinkler system efficiency:");
		JLabel lInputText11 = new JLabel("Enter temperature change on sunny days:");
		JLabel lInputText12 = new JLabel("Enter temperature change on cloudy days:");
		JLabel lInputText13 = new JLabel("Enter temperature change on rainy days:");
		JLabel lInputText14 = new JLabel("Enter temperature change on snowy days:");
		JLabel lInputText15 = new JLabel("Enter humidity change on sunny days:");
		JLabel lInputText16 = new JLabel("Enter humidity change on cloudy days:");
		JLabel lInputText17 = new JLabel("Enter humidity change on rainy days:");
		JLabel lInputText18 = new JLabel("Enter humidity change on snowy days:");
		JLabel lInputText19 = new JLabel("Enter soil moisture change on sunny days:");
		JLabel lInputText20 = new JLabel("Enter soil moisture change on cloudy days:");
		JLabel lInputText21 = new JLabel("Enter soil moisture change on rainy days:");
		JLabel lInputText22 = new JLabel("Enter soil moisture change on snowy days:");
		JLabel lInputText23 = new JLabel("Enter temperature display sample rate:");
		JLabel lInputText24 = new JLabel("Enter humidity display sample rate:");
		JLabel lInputText25 = new JLabel("Enter soil moisture display sample rate:");
		JLabel lInputText26 = new JLabel("Select the initial weather condition:");
		
		// Create user input sub-panels
		JPanel pInput01 = new JPanel(new FlowLayout());
		JPanel pInput02 = new JPanel(new FlowLayout());
		JPanel pInput03 = new JPanel(new FlowLayout());
		JPanel pInput04 = new JPanel(new FlowLayout());
		JPanel pInput05 = new JPanel(new FlowLayout());
		JPanel pInput06 = new JPanel(new FlowLayout());
		JPanel pInput07 = new JPanel(new FlowLayout());
		JPanel pInput08 = new JPanel(new FlowLayout());
		JPanel pInput09 = new JPanel(new FlowLayout());
		JPanel pInput10 = new JPanel(new FlowLayout());
		JPanel pInput11 = new JPanel(new FlowLayout());
		JPanel pInput12 = new JPanel(new FlowLayout());
		JPanel pInput13 = new JPanel(new FlowLayout());
		JPanel pInput14 = new JPanel(new FlowLayout());
		JPanel pInput15 = new JPanel(new FlowLayout());
		JPanel pInput16 = new JPanel(new FlowLayout());
		JPanel pInput17 = new JPanel(new FlowLayout());
		JPanel pInput18 = new JPanel(new FlowLayout());
		JPanel pInput19 = new JPanel(new FlowLayout());
		JPanel pInput20 = new JPanel(new FlowLayout());
		JPanel pInput21 = new JPanel(new FlowLayout());
		JPanel pInput22 = new JPanel(new FlowLayout());
		JPanel pInput23 = new JPanel(new FlowLayout());
		JPanel pInput24 = new JPanel(new FlowLayout());
		JPanel pInput25 = new JPanel(new FlowLayout());
		JPanel pInput26 = new JPanel(new FlowLayout());
		
		// Create user input panels for each input step
		JPanel pInputStep1 = new JPanel(new GridLayout(10, 2));
		JPanel pInputStep2 = new JPanel(new GridLayout(4, 2));
		JPanel pInputStep3 = new JPanel(new GridLayout(4, 2));
		JPanel pInputStep4 = new JPanel(new GridLayout(4, 2));
		JPanel pInputStep5 = new JPanel(new GridLayout(4, 2));
		
		// Add items to user input weather drop down
		if (comboWeatherSelect.getItemCount() == 0) {
			comboWeatherSelect.addItem(new String("Sunny"));
			comboWeatherSelect.addItem(new String("Cloudy"));
			comboWeatherSelect.addItem(new String("Rainy"));
			comboWeatherSelect.addItem(new String("Snowy"));
		}
		
		// Add all components to their respective panels
		pInput01.add(tfInput01);
		pInput01.add(new JLabel("°C           "));
		pInputStep1.add(lInputText01);
		pInputStep1.add(pInput01);
		
		pInput02.add(tfInput02);
		pInput02.add(new JLabel("°C           "));
		pInputStep1.add(lInputText02);
		pInputStep1.add(pInput02);
		
		pInput03.add(tfInput03);
		pInput03.add(new JLabel("%            "));
		pInputStep1.add(lInputText03);
		pInputStep1.add(pInput03);
		
		pInput04.add(tfInput04);
		pInput04.add(new JLabel("%            "));
		pInputStep1.add(lInputText04);
		pInputStep1.add(pInput04);
		
		pInput05.add(tfInput05);
		pInput05.add(new JLabel("%            "));
		pInputStep1.add(lInputText05);
		pInputStep1.add(pInput05);
		
		pInput06.add(tfInput06);
		pInput06.add(new JLabel("%            "));
		pInputStep1.add(lInputText06);
		pInputStep1.add(pInput06);
		
		pInput07.add(tfInput07);
		pInput07.add(new JLabel("°C/min   "));
		pInputStep1.add(lInputText07);
		pInputStep1.add(pInput07);
		
		pInput08.add(tfInput08);
		pInput08.add(new JLabel("°C/min   "));
		pInputStep1.add(lInputText08);
		pInputStep1.add(pInput08);
		
		pInput09.add(tfInput09);
		pInput09.add(new JLabel("%/min    "));
		pInputStep1.add(lInputText09);
		pInputStep1.add(pInput09);
		
		pInput10.add(tfInput10);
		pInput10.add(new JLabel("%/min    "));
		pInputStep1.add(lInputText10);
		pInputStep1.add(pInput10);
		
		pInput11.add(tfInput11);
		pInput11.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText11);
		pInputStep2.add(pInput11);
		
		pInput12.add(tfInput12);
		pInput12.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText12);
		pInputStep2.add(pInput12);
		
		pInput13.add(tfInput13);
		pInput13.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText13);
		pInputStep2.add(pInput13);
		
		pInput14.add(tfInput14);
		pInput14.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText14);
		pInputStep2.add(pInput14);
		
		pInput15.add(tfInput15);
		pInput15.add(new JLabel("%/min    "));
		pInputStep3.add(lInputText15);
		pInputStep3.add(pInput15);
		
		pInput16.add(tfInput16);
		pInput16.add(new JLabel("%/min    "));
		pInputStep3.add(lInputText16);
		pInputStep3.add(pInput16);
		
		pInput17.add(tfInput17);
		pInput17.add(new JLabel("%/min    "));
		pInputStep3.add(lInputText17);
		pInputStep3.add(pInput17);
		
		pInput18.add(tfInput18);
		pInput18.add(new JLabel("%/min    "));
		pInputStep3.add(lInputText18);
		pInputStep3.add(pInput18);
		
		pInput19.add(tfInput19);
		pInput19.add(new JLabel("%/min    "));
		pInputStep4.add(lInputText19);
		pInputStep4.add(pInput19);
		
		pInput20.add(tfInput20);
		pInput20.add(new JLabel("%/min    "));
		pInputStep4.add(lInputText20);
		pInputStep4.add(pInput20);
		
		pInput21.add(tfInput21);
		pInput21.add(new JLabel("%/min    "));
		pInputStep4.add(lInputText21);
		pInputStep4.add(pInput21);
		
		pInput22.add(tfInput22);
		pInput22.add(new JLabel("%/min    "));
		pInputStep4.add(lInputText22);
		pInputStep4.add(pInput22);
		
		pInput23.add(tfInput23);
		pInput23.add(new JLabel("seconds"));
		pInputStep5.add(lInputText23);
		pInputStep5.add(pInput23);
		
		pInput24.add(tfInput24);
		pInput24.add(new JLabel("seconds"));
		pInputStep5.add(lInputText24);
		pInputStep5.add(pInput24);
		
		pInput25.add(tfInput25);
		pInput25.add(new JLabel("seconds"));
		pInputStep5.add(lInputText25);
		pInputStep5.add(pInput25);
		
		pInput26.add(comboWeatherSelect);
		pInputStep5.add(lInputText26);
		pInputStep5.add(pInput26);
		
		// Add sub panels to 5 main input panels
		JPanel[] inputPanels = new JPanel[5];
		inputPanels[0] = pInputStep1;
		inputPanels[1] = pInputStep2;
		inputPanels[2] = pInputStep3;
		inputPanels[3] = pInputStep4;
		inputPanels[4] = pInputStep5;
		
		// Return 5 main input panels
		return inputPanels;
	}
	
	/**
	 * Get an array of all user input text fields
	 * @return inputFieldsStep1: The array of user input text fields
	 */
	public JTextField[] getInputFields () {
		
		// Create array of user input text fields
		JTextField[] inputFields = new JTextField[25];
		
		// Add user input text fields to array
		inputFields[0] = tfInput01;
		inputFields[1] = tfInput02;
		inputFields[2] = tfInput03;
		inputFields[3] = tfInput04;
		inputFields[4] = tfInput05;
		inputFields[5] = tfInput06;
		inputFields[6] = tfInput07;
		inputFields[7] = tfInput08;
		inputFields[8] = tfInput09;
		inputFields[9] = tfInput10;
		inputFields[10] = tfInput11;
		inputFields[11] = tfInput12;
		inputFields[12] = tfInput13;
		inputFields[13] = tfInput14;
		inputFields[14] = tfInput15;
		inputFields[15] = tfInput16;
		inputFields[16] = tfInput17;
		inputFields[17] = tfInput18;
		inputFields[18] = tfInput19;
		inputFields[19] = tfInput20;
		inputFields[20] = tfInput21;
		inputFields[21] = tfInput22;
		inputFields[22] = tfInput23;
		inputFields[23] = tfInput24;
		inputFields[24] = tfInput25;
		
		// Return array of user input text fields
		return inputFields;
	}

	/**
	 * Get the user input weather drop down (user input step 5)
	 * @return comboWeatherSelect: The user input weather drop down
	 */
	public JComboBox<String> getWeatherUserSelection () {
		return comboWeatherSelect;
	}
	
	/**
	 * Get the main frame weather drop down
	 * @return comboWeather: The main frame weather drop down
	 */
	public JComboBox<String> getWeatherDropDown () {
		return comboWeather;
	}
	
	/**
	 * Set the temperature display slider
	 * @param value: The new temperature display slider value
	 */
	public void setTemperatureDisplay (int value) {
		sTemperature.setValue(value);
	}
	
	/**
	 * Set the temperature target display slider
	 * @param value: The new temperature target display slider value
	 */
	public void setTemperatureTargetDisplay (int value) {
		sTemperatureTarget.setValue(value);
	}
	
	/**
	 * Set the humidity display slider
	 * @param value: The new humidity display slider value
	 */
	public void setHumidityDisplay (int value) {
		sHumidity.setValue(value);
	}
	
	/**
	 * Set the humidity target display slider
	 * @param value: The new temperature display slider value
	 */
	public void setHumidityTargetDisplay (int value) {
		sHumidityTarget.setValue(value);
	}
	
	/**
	 * Set the soil moisture display slider
	 * @param value: The new soil moisture display slider value
	 */
	public void setSoilMoistureDisplay (int value) {
		sSoilMoisture.setValue(value);
	}
	
	/**
	 * Set the soil moisture target display slider
	 * @param value: The new soil moisture target display slider value
	 */
	public void setSoilMoistureTargetDisplay (int value) {
		sSoilMoistureTarget.setValue(value);
	}

	/**
	 * Select or deselect the furnace check box
	 * @param boxChecked: Is the box checked or not
	 */
	public void setFurnaceChecked (boolean boxChecked) {
		cbFurnace.setSelected(boxChecked);
	}
	
	/**
	 * Select or deselect the air conditioner check box
	 * @param boxChecked: Is the box checked or not
	 */
	public void setAirConditionerChecked (boolean boxChecked) {
		cbAirConditioner.setSelected(boxChecked);
	}
	
	/**
	 * Select or deselect the humidifier check box
	 * @param boxChecked: Is the box checked or not
	 */
	public void setHumidifierChecked (boolean boxChecked) {
		cbHumidifier.setSelected(boxChecked);
	}
	
	/**
	 * Select or deselect the sprinkler check box
	 * @param boxChecked: Is the box checked or not
	 */
	public void setSprinklerChecked (boolean boxChecked) {
		cbSprinkler.setSelected(boxChecked);
	}
	
	/**
	 * Toggle temperature status icon
	 * @param statusGood: Is the temperature status good or not
	 */
	public void setTempStatus (boolean statusGood) {
		if (statusGood) {
			tempStatus.setIcon(iconGreen);
		} else {
			tempStatus.setIcon(iconRed);
		}
	}
	
	/**
	 * Toggle humidity status icon
	 * @param statusGood: Is the humidity status good or not
	 */
	public void setHumidStatus (boolean statusGood) {
		if (statusGood) {
			humidStatus.setIcon(iconGreen);
		} else {
			humidStatus.setIcon(iconRed);
		}
	}
	
	/**
	 * Toggle soil moisture status icon
	 * @param statusGood: Is the soil moisture status good or not
	 */
	public void setSoilMoistStatus (boolean statusGood) {
		if (statusGood) {
			soilMoistStatus.setIcon(iconGreen);
		} else {
			soilMoistStatus.setIcon(iconRed);
		}
	}
	
	/**
	 * Set the simulation run time display
	 * @param time: The run time
	 */
	public void setRunTime (String time) {
		runTime.setText(time);
	}
	
	/**
	 * Enable all displays except resume button when simulation is not running
	 * @param bool: Enable or disable pause state
	 */
	public void setSimulationRunning (boolean bool) {
		sTemperature.setEnabled(bool);
		sTemperatureTarget.setEnabled(bool);
		sHumidity.setEnabled(bool);
		sHumidityTarget.setEnabled(bool);
		sSoilMoisture.setEnabled(bool);
		sSoilMoistureTarget.setEnabled(bool);
		bEditSimulation.setEnabled(!bool);
		bPauseSimulation.setEnabled(bool);
		bResumeSimulation.setEnabled(!bool);
		bShowLog.setEnabled(!bool);
		cbFurnace.setEnabled(bool);
		cbAirConditioner.setEnabled(bool);
		cbHumidifier.setEnabled(bool);
		cbSprinkler.setEnabled(bool);
		comboWeather.setEnabled(bool);
		tempStatus.setEnabled(bool);
		humidStatus.setEnabled(bool);
		soilMoistStatus.setEnabled(bool);
		runTime.setEnabled(bool);
	}
	
	/**
	 * Add a listener to all interactive gui elements
	 * @param listener: The listener specifying each element's behavior
	 */
	public void addButtonListener (ButtonListener listener) {
		sTemperatureTarget.addChangeListener(listener);
		sHumidityTarget.addChangeListener(listener);
		sSoilMoistureTarget.addChangeListener(listener);
		bEditSimulation.addActionListener(listener);
		bPauseSimulation.addActionListener(listener);
		bResumeSimulation.addActionListener(listener);
		bShowLog.addActionListener(listener);
		comboWeather.addActionListener(listener);
	}
}
