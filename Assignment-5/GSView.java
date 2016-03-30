import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
	private JPanel pBottomLeft = new JPanel(new GridLayout(4, 1));
	private JPanel pTempControl = new JPanel(new FlowLayout());
	private JPanel pBottomCenter = new JPanel(new GridLayout(4, 1));
	private JPanel pBottomRight = new JPanel(new GridLayout(4, 1));
	private JPanel pInput01 = new JPanel(new FlowLayout());
	private JPanel pInput02 = new JPanel(new FlowLayout());
	private JPanel pInput03 = new JPanel(new FlowLayout());
	private JPanel pInput04 = new JPanel(new FlowLayout());
	private JPanel pInput05 = new JPanel(new FlowLayout());
	private JPanel pInput06 = new JPanel(new FlowLayout());
	private JPanel pInput07 = new JPanel(new FlowLayout());
	private JPanel pInput08 = new JPanel(new FlowLayout());
	private JPanel pInput09 = new JPanel(new FlowLayout());
	private JPanel pInput10 = new JPanel(new FlowLayout());
	private JPanel pInput11 = new JPanel(new FlowLayout());
	private JPanel pInput12 = new JPanel(new FlowLayout());
	private JPanel pInput13 = new JPanel(new FlowLayout());
	private JPanel pInput14 = new JPanel(new FlowLayout());
	private JPanel pInput15 = new JPanel(new FlowLayout());
	private JPanel pInput16 = new JPanel(new FlowLayout());
	private JPanel pInput17 = new JPanel(new FlowLayout());
	private JPanel pInput18 = new JPanel(new FlowLayout());
	private JPanel pInput19 = new JPanel(new FlowLayout());
	private JPanel pInput20 = new JPanel(new FlowLayout());
	private JPanel pInput21 = new JPanel(new FlowLayout());
	private JPanel pInput22 = new JPanel(new FlowLayout());
	private JPanel pInput23 = new JPanel(new FlowLayout());
	private JPanel pInputStep1 = new JPanel(new GridLayout(8, 2));
	private JPanel pInputStep2 = new JPanel(new GridLayout(4, 2));
	private JPanel pInputStep3 = new JPanel(new GridLayout(4, 2));
	private JPanel pInputStep4 = new JPanel(new GridLayout(4, 2));
	private JPanel pInputStep5 = new JPanel(new GridLayout(3, 2));
	
	private JSlider sTemperatureActual = new JSlider(JSlider.VERTICAL, 10, 40, 10);
	private JSlider sTemperatureDesired = new JSlider(JSlider.VERTICAL, 10, 40, 10);
	private JSlider sHumidity = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	private JSlider sSoilMoisture = new JSlider(JSlider.VERTICAL, 0, 100, 0);
	
	private JCheckBox cbFurnace = new JCheckBox("Furnace");
	private JCheckBox cbAirConditioner = new JCheckBox("Air Conditioner");
	private JCheckBox cbHumidifier = new JCheckBox("Humidifier");
	private JCheckBox cbSprinkler = new JCheckBox("Sprinkler System");
	
	private JLabel lTitle = new JLabel("Green Thumbs Greenhouses", JLabel.CENTER);
	private JLabel lSubTitle = new JLabel("Automatic Climate Control", JLabel.CENTER);
	private JLabel lTemperature = new JLabel("<html><div style = 'text-align: center;'>"
			+ "Temperature (Celsius)"
			+ "<br><br>"
			+ "Actual: "
			+ "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp "
			+ "Target:" 
			+ "</html>", JLabel.CENTER);
	private JLabel lWeather = new JLabel("Weather Condition: Sunny", JLabel.CENTER);
	private JLabel lHumidity = new JLabel("Humidity", JLabel.CENTER);
	private JLabel lSoilMoisture = new JLabel("Soil Moisture", JLabel.CENTER);
	private JLabel lInputText01 = new JLabel("Enter initial temperature:");
	private JLabel lInputText02 = new JLabel("Enter desired temperature:");
	private JLabel lInputText03 = new JLabel("Enter humidity:");
	private JLabel lInputText04 = new JLabel("Enter soil moisture:");
	private JLabel lInputText05 = new JLabel("Enter furnace efficiency:");
	private JLabel lInputText06 = new JLabel("Enter air conditioner efficiency:");
	private JLabel lInputText07 = new JLabel("Enter humidifier efficiency:");
	private JLabel lInputText08 = new JLabel("Enter sprinkler system efficiency:");
	private JLabel lInputText09 = new JLabel("Enter temperature change on sunny days:");
	private JLabel lInputText10 = new JLabel("Enter temperature change on cloudy days:");
	private JLabel lInputText11 = new JLabel("Enter temperature change on rainy days:");
	private JLabel lInputText12 = new JLabel("Enter temperature change on snowy days:");
	private JLabel lInputText13 = new JLabel("Enter humidity change on sunny days:");
	private JLabel lInputText14 = new JLabel("Enter humidity change on cloudy days:");
	private JLabel lInputText15 = new JLabel("Enter humidity change on rainy days:");
	private JLabel lInputText16 = new JLabel("Enter humidity change on snowy days:");
	private JLabel lInputText17 = new JLabel("Enter soil moisture change on sunny days:");
	private JLabel lInputText18 = new JLabel("Enter soil moisture change on cloudy days:");
	private JLabel lInputText19 = new JLabel("Enter soil moisture change on rainy days:");
	private JLabel lInputText20 = new JLabel("Enter soil moisture change on snowy days:");
	private JLabel lInputText21 = new JLabel("Enter temperature display sample rate:");
	private JLabel lInputText22 = new JLabel("Enter humidity display sample rate:");
	private JLabel lInputText23 = new JLabel("Enter soil moisture display sample rate:");
	
	private JTextField tfInput01 = new JTextField(4);
	private JTextField tfInput02 = new JTextField(4);
	private JTextField tfInput03 = new JTextField(4);
	private JTextField tfInput04 = new JTextField(4);
	private JTextField tfInput05 = new JTextField(4);
	private JTextField tfInput06 = new JTextField(4);
	private JTextField tfInput07 = new JTextField(4);
	private JTextField tfInput08 = new JTextField(4);
	private JTextField tfInput09 = new JTextField(4);
	private JTextField tfInput10 = new JTextField(4);
	private JTextField tfInput11 = new JTextField(4);
	private JTextField tfInput12 = new JTextField(4);
	private JTextField tfInput13 = new JTextField(4);
	private JTextField tfInput14 = new JTextField(4);
	private JTextField tfInput15 = new JTextField(4);
	private JTextField tfInput16 = new JTextField(4);
	private JTextField tfInput17 = new JTextField(4);
	private JTextField tfInput18 = new JTextField(4);
	private JTextField tfInput19 = new JTextField(4);
	private JTextField tfInput20 = new JTextField(4);
	private JTextField tfInput21 = new JTextField(4);
	private JTextField tfInput22 = new JTextField(4);
	private JTextField tfInput23 = new JTextField(4);
	
	private Hashtable<Integer, JLabel> temperatureLabels = new Hashtable<Integer, JLabel>();
	private Hashtable<Integer, JLabel> humidityLabels = new Hashtable<Integer, JLabel>();
	private Hashtable<Integer, JLabel> soilMoistureLabels = new Hashtable<Integer, JLabel>();
	
	//private JDialog inputDialog;
	
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
		
		sTemperatureActual.setLabelTable(temperatureLabels);
		sTemperatureDesired.setLabelTable(temperatureLabels);
		sHumidity.setLabelTable(humidityLabels);
		sSoilMoisture.setLabelTable(soilMoistureLabels);
		sTemperatureActual.setPaintLabels(true);
		sTemperatureDesired.setPaintLabels(true);
		sHumidity.setPaintLabels(true);
		sSoilMoisture.setPaintLabels(true);
		
		sTemperatureActual.setMajorTickSpacing(1);
		sTemperatureActual.setPaintTicks(true);
		sTemperatureDesired.setMajorTickSpacing(1);
		sTemperatureDesired.setPaintTicks(true);
		sHumidity.setMajorTickSpacing(5);
		sHumidity.setPaintTicks(true);
		sSoilMoisture.setMajorTickSpacing(5);
		sSoilMoisture.setPaintTicks(true);
		
		sTemperatureActual.setEnabled(false);
		sTemperatureDesired.setEnabled(false);
		sHumidity.setEnabled(false);
		sSoilMoisture.setEnabled(false);
		
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
		
		pTemperature.add(sTemperatureActual);
		pTemperature.add(sTemperatureDesired);
		
		pTempControl.add(new JLabel(" "));
		pTempControl.add(cbFurnace);
		pTempControl.add(new JLabel("              "));
		pTempControl.add(cbAirConditioner);
		
		pBottomLeft.add(pTempControl);
		pBottomLeft.add(lWeather);
		pBottomLeft.add(new JLabel(""));
		pBottomLeft.add(new JLabel(""));
		
		pBottomCenter.add(cbHumidifier);
		pBottomCenter.add(new JLabel(""));
		pBottomCenter.add(new JLabel(""));
		pBottomCenter.add(new JLabel(""));
		
		pBottomRight.add(cbSprinkler);
		pBottomRight.add(new JLabel(""));
		pBottomRight.add(new JLabel(""));
		pBottomRight.add(new JLabel(""));
		
		this.getContentPane().add(pTopLeft);
		this.getContentPane().add(pTopCenter);
		this.getContentPane().add(pTopRight);
		
		this.getContentPane().add(pTemperature);
		this.getContentPane().add(sHumidity);
		this.getContentPane().add(sSoilMoisture);
		
		this.getContentPane().add(pBottomLeft);
		this.getContentPane().add(pBottomCenter);
		this.getContentPane().add(pBottomRight);
		
		this.setVisible(true);
	}
	
	public void showInputDialog () {
		
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
		pInput05.add(new JLabel("°C/min   "));
		pInputStep1.add(lInputText05);
		pInputStep1.add(pInput05);
		
		pInput06.add(tfInput06);
		pInput06.add(new JLabel("°C/min   "));
		pInputStep1.add(lInputText06);
		pInputStep1.add(pInput06);
		
		pInput07.add(tfInput07);
		pInput07.add(new JLabel("%/min    "));
		pInputStep1.add(lInputText07);
		pInputStep1.add(pInput07);
		
		pInput08.add(tfInput08);
		pInput08.add(new JLabel("%/min    "));
		pInputStep1.add(lInputText08);
		pInputStep1.add(pInput08);
		
		pInput09.add(tfInput09);
		pInput09.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText09);
		pInputStep2.add(pInput09);
		
		pInput10.add(tfInput10);
		pInput10.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText10);
		pInputStep2.add(pInput10);
		
		pInput11.add(tfInput11);
		pInput11.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText11);
		pInputStep2.add(pInput11);
		
		pInput12.add(tfInput12);
		pInput12.add(new JLabel("°C/min   "));
		pInputStep2.add(lInputText12);
		pInputStep2.add(pInput12);
		
		pInput13.add(tfInput13);
		pInput13.add(new JLabel("%/min    "));
		pInputStep3.add(lInputText13);
		pInputStep3.add(pInput13);
		
		pInput14.add(tfInput14);
		pInput14.add(new JLabel("%/min    "));
		pInputStep3.add(lInputText14);
		pInputStep3.add(pInput14);
		
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
		pInputStep4.add(lInputText17);
		pInputStep4.add(pInput17);
		
		pInput18.add(tfInput18);
		pInput18.add(new JLabel("%/min    "));
		pInputStep4.add(lInputText18);
		pInputStep4.add(pInput18);
		
		pInput19.add(tfInput19);
		pInput19.add(new JLabel("%/min    "));
		pInputStep4.add(lInputText19);
		pInputStep4.add(pInput19);
		
		pInput20.add(tfInput20);
		pInput20.add(new JLabel("%/min    "));
		pInputStep4.add(lInputText20);
		pInputStep4.add(pInput20);
		
		pInput21.add(tfInput21);
		pInput21.add(new JLabel("seconds"));
		pInputStep5.add(lInputText21);
		pInputStep5.add(pInput21);
		
		pInput22.add(tfInput22);
		pInput22.add(new JLabel("seconds"));
		pInputStep5.add(lInputText22);
		pInputStep5.add(pInput22);
		
		pInput23.add(tfInput23);
		pInput23.add(new JLabel("seconds"));
		pInputStep5.add(lInputText23);
		pInputStep5.add(pInput23);
		
		JOptionPane.showMessageDialog(this, pInputStep1, 
				"Step 1 of 5: Please enter simulation parameters", JOptionPane.QUESTION_MESSAGE);
		JOptionPane.showMessageDialog(this, pInputStep2, 
				"Step 2 of 5: Please enter simulation parameters", JOptionPane.QUESTION_MESSAGE);
		JOptionPane.showMessageDialog(this, pInputStep3, 
				"Step 3 of 5: Please enter simulation parameters", JOptionPane.QUESTION_MESSAGE);
		JOptionPane.showMessageDialog(this, pInputStep4, 
				"Step 4 of 5: Please enter simulation parameters", JOptionPane.QUESTION_MESSAGE);
		JOptionPane.showMessageDialog(this, pInputStep5, 
				"Step 5 of 5: Please enter simulation parameters", JOptionPane.QUESTION_MESSAGE);
	}
}
