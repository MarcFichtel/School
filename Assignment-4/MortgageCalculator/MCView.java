package MortgageCalculator;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class represents the GUI of the mortgage calculator program
 */
public class MCView extends JFrame {

	/**
	 * titlePanel: The JPanel holding the welcome title
	 */
	private JPanel titlePanel;
	
	/**
	 * inputPanel: The JPanel holding the user input elements
	 */
	private JPanel inputPanel;
	
	/**
	 * amortizationPanel: The JPanel holding the amortization sub-input fields
	 */
	private JPanel amortizationPanel;
	
	/**
	 * compoundPanel: The JPanel holding the compounding sub-input fields
	 */
	private JPanel compoundPanel;
	
	/**
	 * statsPanel: The JPanel holding the statistics display elements
	 */
	private JPanel statsPanel;
	
	/**
	 * titleText: The JLabel welcome message
	 */
	private JLabel titleText;
	
	/**
	 * principleHelpText: The JLabel help text for the principal input
	 */
	private JLabel principleHelpText;
	
	/**
	 * interestRateHelpText: The JLabel help text for the interest rate input
	 */
	private JLabel interestRateHelpText;
	
	/**
	 * amortizationHelpText: The JLabel help text for the amortization input
	 */
	private JLabel amortizationHelpText;
	
	/**
	 * compoundFrequencyText: The JLabel help text for the compounding input
	 */
	private JLabel compoundFrequencyText;
	
	/**
	 * statsDescriptionText: The JLabel describing the statistics to be displayed
	 */
	private JLabel statsDescriptionText;
	
	/**
	 * statsValueText: The JLabel displaying the mortgage statistics
	 */
	private JLabel statsValueText;
	
	/**
	 * principalTextField: The JTextField for the principal input
	 */
	private JTextField principalTextField;
	
	/**
	 * interestRateTextField: The JTextField for the interest rate input
	 */
	private JTextField interestRateTextField;
	
	/**
	 * amortizationTextField: The JTextField for the amortization input
	 */
	private JTextField amortizationTextField;
	
	/**
	 * amortizationDropdown: The JComboBox for the amortization input frequency
	 */
	private JComboBox<String> amortizationDropdown;
	
	/**
	 * compoundFrequencyTextField: The JTextField for the compounding input
	 */
	private JTextField compoundFrequencyTextField;
	
	/**
	 * compoundFrequencyDropdown: The JComboBox for the compounding input frequency
	 */
	private JComboBox<String> compoundFrequencyDropdown;
	
	/**
	 * submitButton: The JButton for user input submission and statistics display
	 */
	private JButton submitButton;
	
	/**
	 * scheduleButton: The JButton for payment schedule generation and display
	 */
	private JButton scheduleButton;
	
	/**
	 * Sets up the GUI
	 * Creates components (panels, text fields, buttons, labels, drop downs)
	 * Adds action listeners to buttons, window listener to frame
	 */
	public void initUI () {
		
		// Create panels for title, input, and statistics
		titlePanel = new JPanel(new GridLayout(1, 2));
		inputPanel = new JPanel(new GridLayout(8, 2, 0, 5));
		amortizationPanel = new JPanel(new GridLayout(1, 3, 5, 0));
		compoundPanel = new JPanel(new GridLayout(1, 3, 5, 0));
		statsPanel = new JPanel(new GridLayout(1, 2));
		
		// Set border values to create padding around input & statistics
		inputPanel.setBorder(new EmptyBorder(0, 40, 0, 40));
		statsPanel.setBorder(new EmptyBorder(0, 40, 40, 0));
		
		// Create and center-align welcome text
		titleText = new JLabel("<html><div style = 'text-align: center;'>" +
				"Welcome to the <i>Mortgage Calculator!</i><br>" +
				"<br>Assignment 4 submission for CPSC 233 by Marc-Andre Fichtel<br>" +
				"<br><br><u>Enter the following to calculate your Mortgage Statistics</u><br>" +
				"</html>");
		titleText.setHorizontalAlignment(JLabel.CENTER);
		
		// Create text labels for input instructions and statistics
		principleHelpText = new JLabel("Enter Principle ($):");
		interestRateHelpText = new JLabel("Enter Annual Interest Rate (%):");
		amortizationHelpText = new JLabel("Enter Amortization:");
		compoundFrequencyText = new JLabel("Enter Compounding Frequency:");
		statsValueText = new JLabel("");
		statsDescriptionText = new JLabel("<html>Principal: " +
												"<br>Annual Interest Rate: " + 
												"<br>Amortization: " + 
												"<br>Blended Payment Amount: " + 
												"<br>Total Interest: " + 
												"<br>Final Investment Value: " + 
												"<br>Interest/Principle Ratio: " + 
												"<br>Average Annual Interest: " + 
												"<br>Average Monthly Interest: " + 
												"<br></html>");
		

		// Create text fields for user input
		principalTextField = new JTextField();
		interestRateTextField = new JTextField();
		amortizationTextField = new JTextField();
		compoundFrequencyTextField = new JTextField();
		
		
		// Create drop down for amortization choices
		amortizationDropdown = new JComboBox<String>();
		amortizationDropdown.addItem("Monthly");
		amortizationDropdown.addItem("Bi-Weekly");
		amortizationDropdown.addItem("Weekly");
		
		// Create drop down for compound frequency choice
		compoundFrequencyDropdown = new JComboBox<String>();
		compoundFrequencyDropdown.addItem("Day(s)");
		compoundFrequencyDropdown.addItem("Week(s)");
		compoundFrequencyDropdown.addItem("Month(s)");
		compoundFrequencyDropdown.addItem("Year(s)");
		
		// Set default values for compound frequency input elements (twice a year)
		compoundFrequencyTextField.setText("6");
		compoundFrequencyDropdown.setSelectedIndex(2);
		
		// Create buttons for submitting entered values and generating a payment schedule
		submitButton = new JButton("Submit");
		scheduleButton = new JButton("Generate Payment Schedule");
				
		// Disable payment schedule button until some data has been submitted
		scheduleButton.setEnabled(false);
		
		// TODO Add explanatory tool tips to various components 
		
		// Add title to title panel
		titlePanel.add(titleText);
		
		amortizationPanel.add(amortizationTextField);
		amortizationPanel.add(amortizationDropdown);
		amortizationPanel.add(new JLabel("Payments"));
		
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
		inputPanel.add(amortizationPanel);
		inputPanel.add(compoundFrequencyText);
		inputPanel.add(compoundPanel);
		
		// Add empty text and submit button to input panel (place button in right cell)
		inputPanel.add(new JLabel());
		inputPanel.add(submitButton);
		inputPanel.add(new JLabel());
		inputPanel.add(scheduleButton);
		
		// Add descriptions and empty value texts to statistics panel
		statsPanel.add(statsDescriptionText);
		statsPanel.add(statsValueText);
		
		// Create new window listener, add panels to frame, make frame visible
		WindowHandler wh = new WindowHandler();
		this.addWindowListener(wh);
		this.getContentPane().add(titlePanel);
		this.getContentPane().add(inputPanel);
		this.getContentPane().add(statsPanel);
		this.setVisible(true);
	}
	
	/**
	 *  Set up the main frame
	 * @param rows: Number of rows of new frame
	 * @param columns: Number of columns of new frame
	 */
	public MCView (int rows, int columns) {
		
		// Invoke default frame constructor
		super("Mortgage Calculator");	
		
		// Set frame size
		setSize(600, 900); 				
		
		// Create new grid with given dimensions
		GridLayout gl = new GridLayout(rows, columns);
		setLayout(gl);
	}
	
	/**
	 * Get the main frame
	 * @return this: The main frame (MCView)
	 */
	public JFrame getFrame () {
		return this;
	}
	
	/**
	 * Get the principal input text field
	 * @return principalTextField: The principal input text field
	 */
	public JTextField getPrincipalTextField () {
		return principalTextField;
	}
	
	/**
	 * Set the principal input text field's text
	 * @param text: The new text field's text
	 */
	public void setPrincipalTextField (String text) {
		principalTextField.setText(text);
	}
	
	/**
	 * Get the interest rate input text field
	 * @return interestRateTextField: The interest rate input text field
	 */
	public JTextField getInterestRateTextField () {
		return interestRateTextField;
	}
	
	/**
	 * Set the interest rate input text field's text
	 * @param text: The new text field's text
	 */
	public void setInterestRateTextField (String text) {
		interestRateTextField.setText(text);
	}
	
	/**
	 * Get the amortization input text field
	 * @return amortizationTextField: The amortization input text field
	 */
	public JTextField getAmortizationTextField () {
		return amortizationTextField;
	}
	
	/**
	 * Set the amortization input text field's text
	 * @param text: The new text field's text
	 */
	public void setAmortizationTextField (String text) {
		amortizationTextField.setText(text);
	}
	
	/**
	 * Get the amortization drop down 
	 * @return amortizationDropdown: The amortization drop down 
	 */
	public JComboBox<String> getAmortizationDropDown () {
		return amortizationDropdown;
	}
	
	/**
	 * Get the compounding input text field
	 * @return compoundFrequencyTextField: The compounding input text field
	 */
	public JTextField getCompoundFrequencyTextField () {
		return compoundFrequencyTextField;
	}
	
	/**
	 * Set the compound input text field's text
	 * @param text: The new text field's text
	 */
	public void setCompoundFrequencyTextField (String text) {
		compoundFrequencyTextField.setText(text);
	}
	
	/**
	 * Get the compounding drop down 
	 * @return compoundFrequencyDropdown: The compounding drop down 
	 */
	public JComboBox<String> getCompoundDropDown () {
		return compoundFrequencyDropdown;
	}
	
	/**
	 * Get the user input submit button
	 * @return submitButton: The user input submit button
	 */
	public JButton getSubmitButton () {
		return submitButton;
	}
	
	/**
	 * Get the schedule generation button
	 * @return scheduleButton: The schedule generation button
	 */
	public JButton getScheduleButton () {
		return scheduleButton;
	}
	
	/**
	 * Get the statistics display text field
	 * @return statsValueText: The statistics display text field
	 */
	public JLabel getStatsTextField () {
		return statsValueText;
	}
	
	/**
	 * Add an action listener to the user input submit button
	 * @param submitListener: The listener to be added
	 */
	public void addSubmitInfoListener (ActionListener submitListener) {
		submitButton.addActionListener(submitListener);
	}
	
	/**
	 * Add an action listener to the schedule generation button
	 * @param scheduleListener: The listener to be added
	 */
	public void addScheduleListener (ActionListener scheduleListener) {
		scheduleButton.addActionListener(scheduleListener);
	}
}