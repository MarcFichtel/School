import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * Class represents the GUI of the mortgage calculator program
 */
public class MCView extends JFrame {

	/**
	 * Starts program by calling initUI()
	 * @param args
	 */
	public static void main (String[] args) {
		initUI();
	}
	
	/**
	 * Sets up the GUI
	 */
	private static void initUI () {

		// Initialize Controller
		MCController control = new MCController ();
		
		// Create frame with 3 rows and 2 columns
		JFrame frame = new MCView (3, 2);
		
		// Create panels for title, input, and statistics
		JPanel titlePanel = new JPanel(new GridLayout(1, 2));		// Title panel spans over top row
		JPanel inputPanel = new JPanel(new GridLayout(11, 2, 0, 5));// Small vertical gap between components
		JPanel statsPanel = new JPanel(new GridLayout(1, 2));		// Stats panel spans over bottom row
		JPanel compoundPanel = new JPanel(new GridLayout(1,3));		// Compound panel has 3 sections
		
		// Set border values to create padding around input & statistics
		inputPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
		statsPanel.setBorder(new EmptyBorder(0, 40, 40, 0));
		
		// Create and center-align welcome text
		JLabel titleText = new JLabel("<html><div style = 'text-align: center;'>" +
				"Welcome to the <i>Mortgage Calculator!</i><br>" +
				"<br>Assignment 4 submission for CPSC 233 by Marc-Andre Fichtel<br>" +
				"<br><br><u>Enter the following to calculate your Mortgage Statistics</u><br>" +
				"</html>");
		titleText.setHorizontalAlignment(JLabel.CENTER);
		
		// Create text labels for input instructions and statistics
		JLabel principleHelpText = new JLabel("Enter Principle ($):");
		JLabel interestRateHelpText = new JLabel("Enter Annual Interest Rate (%):");
		JLabel amortizationHelpText = new JLabel("Enter Amortization ($):");
		JLabel compoundFrequencyText = new JLabel("Enter Compounding Frequency:");
		JLabel statsDescriptionText = new JLabel("<html>Principal: " +
												"<br>Annual Interest Rate: " + 
												"<br>Amortization: " + 
												"<br>Blended Monthly Payment: " + 
												"<br>Total Interest: " + 
												"<br>Final Investment Value: " + 
												"<br>Interest/Principle Ratio: " + 
												"<br>Average Annual Interest: " + 
												"<br>Average Monthly Interest: " + 
												"<br>Annual Amortization: " +
												"<br></html>");
		
		// This label will be updated to display statistics later
		JLabel statsValueText = new JLabel("");
		
		// Create text fields for user input
		JTextField principalTextField = new JTextField();
		JTextField interestRateTextField = new JTextField();
		JTextField amortizationTextField = new JTextField();
		JTextField compoundFrequencyTextField = new JTextField();
		
		// Create drop down for compound frequency choice
		JComboBox<String> compoundFrequencyDropdown = new JComboBox<String>();
		compoundFrequencyDropdown.addItem("Day(s)");
		compoundFrequencyDropdown.addItem("Week(s)");
		compoundFrequencyDropdown.addItem("Month(s)");
		compoundFrequencyDropdown.addItem("Year(s)");
		
		// Set default values for compound frequency input elements (twice a year)
		compoundFrequencyTextField.setText("6");
		compoundFrequencyDropdown.setSelectedIndex(2);
		
		// Create buttons for submitting entered values and generating a payment schedule
		JButton submitButton = new JButton("Submit");
		JButton scheduleButton = new JButton("Generate Payment Schedule");
		
		// Create radio buttons & button group for payment frequency
		ButtonGroup paymentFrequencyOptions = new ButtonGroup();
		JRadioButton optionMonthly = new JRadioButton("Monthly Payments");
		JRadioButton optionBiweekly = new JRadioButton("Bi-Weekly Payments");
		JRadioButton optionWeekly = new JRadioButton("Weekly Payments");
		
		// Activate monthly payments by default
		optionMonthly.setSelected(true);
		
		// Add radio buttons to button group
		paymentFrequencyOptions.add(optionMonthly);
		paymentFrequencyOptions.add(optionBiweekly);
		paymentFrequencyOptions.add(optionWeekly);
		
		// Add action listeners to buttons
		submitButton.addActionListener(new SubmitInfoHandler(
			frame, 
			principalTextField, 
			interestRateTextField, 
			amortizationTextField,
			optionBiweekly,
			optionWeekly,
			compoundFrequencyTextField,
			compoundFrequencyDropdown,
			scheduleButton,
			statsValueText, 
			control));
		scheduleButton.addActionListener(new GeneratePaymentSchedule(
				optionBiweekly,
				optionWeekly,
				control));
				
		// Disable payment schedule button until some data has been submitted
		scheduleButton.setEnabled(false);
		
		// Add title to title panel
		titlePanel.add(titleText);
		
		// Add texts and elements to compound frequency panel
		compoundPanel.add(new JLabel("            Every"));
		compoundPanel.add(compoundFrequencyTextField);
		compoundPanel.add(compoundFrequencyDropdown);
		
		// Add texts and input elements to input panel
		inputPanel.add(principleHelpText);
		inputPanel.add(principalTextField);
		inputPanel.add(interestRateHelpText);
		inputPanel.add(interestRateTextField);
		inputPanel.add(amortizationHelpText);
		inputPanel.add(optionMonthly);
		inputPanel.add(new JLabel());
		inputPanel.add(optionBiweekly);
		inputPanel.add(new JLabel());
		inputPanel.add(optionWeekly);
		inputPanel.add(new JLabel());
		inputPanel.add(amortizationTextField);
		inputPanel.add(compoundFrequencyText);
		inputPanel.add(compoundPanel);
		
		// Add empty text and submit button to input panel (place button in right cell)
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(submitButton);
		inputPanel.add(new JLabel());
		inputPanel.add(scheduleButton);
		
		// Add descriptions and empty value texts to statistics panel
		statsPanel.add(statsDescriptionText);
		statsPanel.add(statsValueText);
		
		// Create new window listener, add panels to frame
		WindowHandler wh = new WindowHandler();
		frame.addWindowListener(wh);
		frame.getContentPane().add(titlePanel);
		frame.getContentPane().add(inputPanel);
		frame.getContentPane().add(statsPanel);
		
		// Make everything visible
		frame.setVisible(true);
	}
	
	/**
	 *  Set up the frame
	 * @param rows: Number of rows of new frame
	 * @param columns: Number of columns of new frame
	 */
	public MCView (int rows, int columns) {
		
		// Invoke default frame constructor
		super("Mortgage Calculator");	
		
		// Set frame size
		final int MC_WIDTH = 550;
		final int MC_HEIGHT = 900;
		setSize(MC_WIDTH, MC_HEIGHT); 				
		
		// Create new grid with given dimensions
		GridLayout gl = new GridLayout(rows, columns);
		setLayout(gl);									
	}
}