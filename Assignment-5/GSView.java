import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Hashtable;

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

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class does something...
 */

public class GSView extends JFrame {

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menu = new JMenu("Menu");
	private JMenuItem menuStart = new JMenuItem("Start Simulation");
	private JMenuItem menuSave = new JMenuItem("Save Simulation");
	private JMenuItem menuLoad = new JMenuItem("Load Simulation");
	private JMenuItem menuExit = new JMenuItem("Exit");
	
	private JPanel pTopLeft = new JPanel(new GridLayout(4, 1));
	private JPanel pTopCenter = new JPanel(new GridLayout(4, 1));
	private JPanel pTopRight = new JPanel(new GridLayout(4, 1));
	private JPanel pTemperature = new JPanel(new GridLayout(1,2));
	private JPanel pHumidity = new JPanel(new GridLayout(1,2));
	private JPanel pSoilMoisture = new JPanel(new GridLayout(1,2));
	private JPanel pBottomLeft = new JPanel(new GridLayout(4, 1));
	private JPanel pTempControl = new JPanel(new FlowLayout());
	private JPanel pBottomCenter = new JPanel(new GridLayout(4, 1));
	private JPanel pBottomRight = new JPanel(new GridLayout(4, 1));
	private JPanel pWeatherSelect = new JPanel(new FlowLayout());
	private JPanel pEditButton = new JPanel(new FlowLayout());
	
	private JSlider sTemperature = new JSlider(JSlider.VERTICAL, 10, 40, 10);
	private JSlider sTemperatureTarget = new JSlider(JSlider.VERTICAL, 10, 40, 10);
	private JSlider sHumidity = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	private JSlider sHumidityTarget = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	private JSlider sSoilMoisture = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	private JSlider sSoilMoistureTarget = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	
	private JCheckBox cbFurnace = new JCheckBox("Furnace");
	private JCheckBox cbAirConditioner = new JCheckBox("Air Conditioner");
	private JCheckBox cbHumidifier = new JCheckBox("Humidifier");
	private JCheckBox cbSprinkler = new JCheckBox("Sprinkler System");
	
	private JLabel lTitle = new JLabel("Green Thumbs Greenhouses", JLabel.CENTER);
	private JLabel lSubTitle = new JLabel("Automatic Climate Control", JLabel.CENTER);
	private JLabel lTemperature = new JLabel("<html><div style = 'text-align: center;'>"
			+ "Temperature (C)"
			+ "<br><br>"
			+ "Actual: "
			+ "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
			+ "Target:" 
			+ "</html>", JLabel.CENTER);
	private JLabel lHumidity = new JLabel("<html><div style = 'text-align: center;'>"
			+ "Humidity (%)"
			+ "<br><br>"
			+ "Actual: "
			+ "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
			+ "Target:" 
			+ "</html>", JLabel.CENTER);
	private JLabel lSoilMoisture = new JLabel("<html><div style = 'text-align: center;'>"
			+ "Soil Moisture (%)"
			+ "<br><br>"
			+ "Actual: "
			+ "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
			+ "Target:" 
			+ "</html>", JLabel.CENTER);
	private JLabel lWeather = new JLabel("Weather Condition:", JLabel.CENTER);
	private JLabel lInputText01 = new JLabel("Enter initial temperature:");
	private JLabel lInputText02 = new JLabel("Enter desired temperature:");
	private JLabel lInputText03 = new JLabel("Enter humidity:");
	private JLabel lInputText04 = new JLabel("Enter desired humidity:");
	private JLabel lInputText05 = new JLabel("Enter soil moisture:");
	private JLabel lInputText06 = new JLabel("Enter desired soil moisture:");
	private JLabel lInputText07 = new JLabel("Enter furnace efficiency:");
	private JLabel lInputText08 = new JLabel("Enter air conditioner efficiency:");
	private JLabel lInputText09 = new JLabel("Enter humidifier efficiency:");
	private JLabel lInputText10 = new JLabel("Enter sprinkler system efficiency:");
	private JLabel lInputText11 = new JLabel("Enter temperature change on sunny days:");
	private JLabel lInputText12 = new JLabel("Enter temperature change on cloudy days:");
	private JLabel lInputText13 = new JLabel("Enter temperature change on rainy days:");
	private JLabel lInputText14 = new JLabel("Enter temperature change on snowy days:");
	private JLabel lInputText15 = new JLabel("Enter humidity change on sunny days:");
	private JLabel lInputText16 = new JLabel("Enter humidity change on cloudy days:");
	private JLabel lInputText17 = new JLabel("Enter humidity change on rainy days:");
	private JLabel lInputText18 = new JLabel("Enter humidity change on snowy days:");
	private JLabel lInputText19 = new JLabel("Enter soil moisture change on sunny days:");
	private JLabel lInputText20 = new JLabel("Enter soil moisture change on cloudy days:");
	private JLabel lInputText21 = new JLabel("Enter soil moisture change on rainy days:");
	private JLabel lInputText22 = new JLabel("Enter soil moisture change on snowy days:");
	private JLabel lInputText23 = new JLabel("Enter temperature display sample rate:");
	private JLabel lInputText24 = new JLabel("Enter humidity display sample rate:");
	private JLabel lInputText25 = new JLabel("Enter soil moisture display sample rate:");
	private JLabel lInputText26 = new JLabel("Select the initial weather condition:");
	
	private JTextField tfInput01 = new JTextField("20", 4);
	private JTextField tfInput02 = new JTextField("25", 4);
	private JTextField tfInput03 = new JTextField("50", 4);
	private JTextField tfInput04 = new JTextField("50", 4);
	private JTextField tfInput05 = new JTextField("50", 4);
	private JTextField tfInput06 = new JTextField("50", 4);
	private JTextField tfInput07 = new JTextField("2", 4);
	private JTextField tfInput08 = new JTextField("-2", 4);
	private JTextField tfInput09 = new JTextField("2", 4);
	private JTextField tfInput10 = new JTextField("2", 4);
	private JTextField tfInput11 = new JTextField("1", 4);
	private JTextField tfInput12 = new JTextField("0", 4);
	private JTextField tfInput13 = new JTextField("0", 4);
	private JTextField tfInput14 = new JTextField("-1", 4);
	private JTextField tfInput15 = new JTextField("-1", 4);
	private JTextField tfInput16 = new JTextField("0", 4);
	private JTextField tfInput17 = new JTextField("1", 4);
	private JTextField tfInput18 = new JTextField("0", 4);
	private JTextField tfInput19 = new JTextField("0", 4);
	private JTextField tfInput20 = new JTextField("0", 4);
	private JTextField tfInput21 = new JTextField("1", 4);
	private JTextField tfInput22 = new JTextField("0", 4);
	private JTextField tfInput23 = new JTextField("1", 4);
	private JTextField tfInput24 = new JTextField("1", 4);
	private JTextField tfInput25 = new JTextField("1", 4);
	
	private Hashtable<Integer, JLabel> temperatureLabels = new Hashtable<Integer, JLabel>();
	private Hashtable<Integer, JLabel> humidityLabels = new Hashtable<Integer, JLabel>();
	private Hashtable<Integer, JLabel> soilMoistureLabels = new Hashtable<Integer, JLabel>();
	
	private JButton bEditSimulation  = new JButton("Edit current simulation");
	
	private JComboBox<String> comboWeather = new JComboBox<String>();
	private JComboBox<String> comboWeatherSelect = new JComboBox<String>();
	
 	public GSView() {
		super("Greenhouse Simulation");
		setSize(700, 700); 
		setLayout(new GridLayout(3, 3));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initUI (ActionListener menuListener) {
		
		lTitle.setFont(new Font("Georgia", Font.BOLD, 15));
		lSubTitle.setFont(new Font("Georgia", Font.ITALIC, 13));
		lTitle.setVerticalAlignment(JLabel.BOTTOM);
		lSubTitle.setVerticalAlignment(JLabel.TOP);
		
		cbHumidifier.setHorizontalAlignment((int)Button.CENTER_ALIGNMENT);
		cbSprinkler.setHorizontalAlignment((int)Button.CENTER_ALIGNMENT);
		
		for (int i = 10; i < 41; i+= 5) {
			temperatureLabels.put(new Integer(i), new JLabel("  " + Integer.toString(i) + "°"));
		}
		for (int i = 0; i < 101; i += 10) {
			humidityLabels.put(new Integer(i), new JLabel("  " + Integer.toString(i) + "%"));
		}
		for (int i = 0; i < 101; i += 10) {
			soilMoistureLabels.put(new Integer(i), new JLabel("  " + Integer.toString(i) + "%"));
		}
		
		sTemperature.setLabelTable(temperatureLabels);
		sTemperatureTarget.setLabelTable(temperatureLabels);
		sHumidity.setLabelTable(humidityLabels);
		sHumidityTarget.setLabelTable(humidityLabels);
		sSoilMoisture.setLabelTable(soilMoistureLabels);
		sSoilMoistureTarget.setLabelTable(soilMoistureLabels);
		
		sTemperature.setPaintLabels(true);
		sTemperatureTarget.setPaintLabels(true);
		sHumidity.setPaintLabels(true);
		sHumidityTarget.setPaintLabels(true);
		sSoilMoisture.setPaintLabels(true);
		sSoilMoistureTarget.setPaintLabels(true);
		
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
		
		sTemperature.setEnabled(false);
		sTemperatureTarget.setEnabled(false);
		sHumidity.setEnabled(false);
		sHumidityTarget.setEnabled(false);
		sSoilMoisture.setEnabled(false);
		sSoilMoistureTarget.setEnabled(false);
		
		comboWeather.addItem(new String("Sunny"));
		comboWeather.addItem(new String("Cloudy"));
		comboWeather.addItem(new String("Rainy"));
		comboWeather.addItem(new String("Snowy"));
		
		menu.add(menuStart);
		menu.add(menuSave);
		menu.add(menuLoad);
		menu.add(menuExit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		menuStart.addActionListener(menuListener);
		menuSave.addActionListener(menuListener);
		menuLoad.addActionListener(menuListener);
		menuExit.addActionListener(menuListener);
		
		pTopLeft.add(new JLabel(""));
		pTopLeft.add(new JLabel(""));
		pTopLeft.add(new JLabel(""));
		pTopLeft.add(lTemperature);
		
		pTopCenter.add(lTitle);
		pTopCenter.add(lSubTitle);
		pTopCenter.add(new JLabel(""));
		pTopCenter.add(lHumidity);
		
		pTopRight.add(new JLabel(""));
		pTopRight.add(new JLabel(""));
		pTopRight.add(new JLabel(""));
		pTopRight.add(lSoilMoisture);
		
		pTemperature.add(sTemperature);
		pTemperature.add(sTemperatureTarget);
		
		pHumidity.add(sHumidity);
		pHumidity.add(sHumidityTarget);
		
		pSoilMoisture.add(sSoilMoisture);
		pSoilMoisture.add(sSoilMoistureTarget);
		
		pTempControl.add(new JLabel(" "));
		pTempControl.add(cbFurnace);
		pTempControl.add(new JLabel("              "));
		pTempControl.add(cbAirConditioner);
		
		pWeatherSelect.add(comboWeather);
		
		pBottomLeft.add(pTempControl);
		pBottomLeft.add(lWeather);
		pBottomLeft.add(pWeatherSelect);
		pBottomLeft.add(new JLabel(""));
		
		pBottomCenter.add(cbHumidifier);
		pBottomCenter.add(new JLabel(""));
		pBottomCenter.add(new JLabel(""));
		pBottomCenter.add(new JLabel(""));
		
		pEditButton.add(new JLabel(""));
		pEditButton.add(bEditSimulation);
		pEditButton.add(new JLabel(""));
		
		pBottomRight.add(cbSprinkler);
		pBottomRight.add(new JLabel(""));
		pBottomRight.add(new JLabel(""));
		pBottomRight.add(pEditButton);
		
		this.getContentPane().add(pTopLeft);
		this.getContentPane().add(pTopCenter);
		this.getContentPane().add(pTopRight);
		
		this.getContentPane().add(pTemperature);
		this.getContentPane().add(pHumidity);
		this.getContentPane().add(pSoilMoisture);
		
		this.getContentPane().add(pBottomLeft);
		this.getContentPane().add(pBottomCenter);
		this.getContentPane().add(pBottomRight);
		
		this.setVisible(true);
	}
	
	public JPanel[] createInputDialog () {
		
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
		JPanel pInputStep1 = new JPanel(new GridLayout(10, 2));
		JPanel pInputStep2 = new JPanel(new GridLayout(4, 2));
		JPanel pInputStep3 = new JPanel(new GridLayout(4, 2));
		JPanel pInputStep4 = new JPanel(new GridLayout(4, 2));
		JPanel pInputStep5 = new JPanel(new GridLayout(4, 2));
		
		if (comboWeatherSelect.getItemCount() == 0) {
			comboWeatherSelect.addItem(new String("Sunny"));
			comboWeatherSelect.addItem(new String("Cloudy"));
			comboWeatherSelect.addItem(new String("Rainy"));
			comboWeatherSelect.addItem(new String("Snowy"));
		}
		
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
		
		JPanel[] inputPanels = new JPanel[5];
		inputPanels[0] = pInputStep1;
		inputPanels[1] = pInputStep2;
		inputPanels[2] = pInputStep3;
		inputPanels[3] = pInputStep4;
		inputPanels[4] = pInputStep5;
		return inputPanels;
	}
	
	public JTextField[] getInputFields () {
		JTextField[] inputFieldsStep1 = new JTextField[25];
		inputFieldsStep1[0] = tfInput01;
		inputFieldsStep1[1] = tfInput02;
		inputFieldsStep1[2] = tfInput03;
		inputFieldsStep1[3] = tfInput04;
		inputFieldsStep1[4] = tfInput05;
		inputFieldsStep1[5] = tfInput06;
		inputFieldsStep1[6] = tfInput07;
		inputFieldsStep1[7] = tfInput08;
		inputFieldsStep1[8] = tfInput09;
		inputFieldsStep1[9] = tfInput10;
		inputFieldsStep1[10] = tfInput11;
		inputFieldsStep1[11] = tfInput12;
		inputFieldsStep1[12] = tfInput13;
		inputFieldsStep1[13] = tfInput14;
		inputFieldsStep1[14] = tfInput15;
		inputFieldsStep1[15] = tfInput16;
		inputFieldsStep1[16] = tfInput17;
		inputFieldsStep1[17] = tfInput18;
		inputFieldsStep1[18] = tfInput19;
		inputFieldsStep1[19] = tfInput20;
		inputFieldsStep1[20] = tfInput21;
		inputFieldsStep1[21] = tfInput22;
		inputFieldsStep1[22] = tfInput23;
		inputFieldsStep1[23] = tfInput24;
		inputFieldsStep1[24] = tfInput25;
		return inputFieldsStep1;
	}

	public JComboBox<String> getWeatherInput () {
		return comboWeatherSelect;
	}
	
	public JComboBox<String> getWeatherSelection () {
		return comboWeather;
	}
	
	public void setTemperatureDisplay (int value) {
		sTemperature.setValue(value);
	}
	
	public void setTemperatureTargetDisplay (int value) {
		sTemperatureTarget.setValue(value);
	}
	
	public void setHumidityDisplay (int value) {
		sHumidity.setValue(value);
	}
	
	public void setHumidityTargetDisplay (int value) {
		sHumidityTarget.setValue(value);
	}
	
	public void setSoilMoistureDisplay (int value) {
		sSoilMoisture.setValue(value);
	}
	
	public void setSoilMoistureTargetDisplay (int value) {
		sSoilMoistureTarget.setValue(value);
	}
	
	public void setTempDisplayActive (boolean displayOn) {
		sTemperature.setEnabled(displayOn);
	}
	
	public void setTempTargetDisplayActive (boolean displayOn) {
		sTemperatureTarget.setEnabled(displayOn);
	}
	
	public void setHumidDisplayActive (boolean displayOn) {
		sHumidity.setEnabled(displayOn);
	}
	
	public void setHumidTargetDisplayActive (boolean displayOn) {
		sHumidityTarget.setEnabled(displayOn);
	}
	
	public void setSoilMoistDisplayActive (boolean displayOn) {
		sSoilMoisture.setEnabled(displayOn);
	}
	
	public void setSoilMoistTargetDisplayActive (boolean displayOn) {
		sSoilMoistureTarget.setEnabled(displayOn);
	}

	public void setFurnaceChecked (boolean boxChecked) {
		cbFurnace.setSelected(boxChecked);
	}
	
	public void setAirConditionerChecked (boolean boxChecked) {
		cbAirConditioner.setSelected(boxChecked);
	}
	
	public void setHumidifierChecked (boolean boxChecked) {
		cbHumidifier.setSelected(boxChecked);
	}
	
	public void setSprinklerChecked (boolean boxChecked) {
		cbSprinkler.setSelected(boxChecked);
	}
}
