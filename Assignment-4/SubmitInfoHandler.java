import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * Class handles user information submission
 */
public class SubmitInfoHandler implements ActionListener {
	
	/**
	 * The main frame created in MCView
	 */
	private JFrame frame;
	
	/**
	 * The principal input text field
	 */
	private JTextField principalInput;
	
	/**
	 * The interest rate input text field
	 */
	private JTextField interestRateInput;
	
	/**
	 * The amortization input text field
	 */
	private JTextField amortizationInput;
	
	/**
	 * The payment frequency (monthly, biweekly, or weekly)
	 */
	private String paymentFrequency;
	
	/**
	 * The second of three payment frequency radio buttons
	 */
	private JRadioButton paymentFrequencyBiweekly;
	
	/**
	 * The third of three payment frequency radio buttons
	 */
	private JRadioButton paymentFrequencyWeekly;
	
	/**
	 * The compound frequency text field
	 */
	private JTextField compoundFrequencyInput;
	
	/**
	 * The compound frequency drop down selection
	 */
	private JComboBox<String> compoundFrequencyInterval;
	
	/**
	 * The payment schedule generation button
	 */
	private JButton scheduleButton;
	
	/**
	 * The text holding the calculated statistics
	 */
	private JLabel calculationsText;
	
	/**
	 * The controller that was instantiated in MCView
	 */
	private MCController control;
	
	/**
	 * Constructor assigns the given values
	 * @param frame: The frame
	 * @param input1: The text field containing the principal
	 * @param input2: The text field containing the interest rate
	 * @param input3: The text field containing the amortization
	 * @param paymentFrequencyBiweekly: Radio button choice 2
	 * @param paymentFrequencyWeekly: Radio button choice 3
	 * @param compoundFrequencyInput: Text field containing a number
	 * @param compoundFrequencyInterval: Drop down containing an interval
	 * @param scheduleButton: Button used for generating payment schedules
	 * @param calculationsText: The statistics display text label
	 * @param control: MCController
	 */
	public SubmitInfoHandler (
			JFrame frame, 
			JTextField input1, 
			JTextField input2, 
			JTextField input3,
			JRadioButton paymentFrequencyBiweekly,
			JRadioButton paymentFrequencyWeekly,
			JTextField compoundFrequencyInput,
			JComboBox<String> compoundFrequencyInterval,
			JButton scheduleButton,
			JLabel calculationsText ,
			MCController control) {
		
		this.frame = frame;
		this.principalInput = input1;
		this.interestRateInput = input2;
		this.amortizationInput = input3;
		this.paymentFrequencyBiweekly = paymentFrequencyBiweekly;
		this.paymentFrequencyWeekly = paymentFrequencyWeekly;
		this.compoundFrequencyInput = compoundFrequencyInput;
		this.compoundFrequencyInterval = compoundFrequencyInterval;
		this.scheduleButton = scheduleButton;
		this.calculationsText = calculationsText;
		this.control = control;
	}
	
	/**
	 * Validate given data
	 * Reset text fields
	 * Calculate & display statistics depending on user choices
	 * Show error messages for invalid data
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			// Check, if input can be cast to required data types
			Double.parseDouble(principalInput.getText());
			Double.parseDouble(interestRateInput.getText());
			Double.parseDouble(amortizationInput.getText());
			Integer.parseInt(compoundFrequencyInput.getText());
			
			// Convert interest rate from a percentage to a fraction
			Double convertedInterestRate = Double.parseDouble(interestRateInput.getText()) / 100;

			// Multiply amortization depending on chosen payment frequency
			Double amortizationValue = Double.parseDouble(amortizationInput.getText());
			if (paymentFrequencyBiweekly.isSelected()) {
				amortizationValue *= 2;
				paymentFrequency = " Bi-Weekly";
			} else if (paymentFrequencyWeekly.isSelected()) {
				amortizationValue *= 4;
				paymentFrequency = " Weekly";
			} else {
				paymentFrequency = " Monthly";
			}
			
			// Set the indicated information to the given value
			control.setPrincipal(principalInput.getText());
			control.setInterestRate(convertedInterestRate.toString());
			control.setAmortization(amortizationValue.toString());
			
			// Set compounding frequency depending on chosen time interval
			int compoundFrequencyValue = Integer.parseInt(compoundFrequencyInput.getText());
			int actualAnnualCompoundFrequency;
			
			// If Year(s) was chosen, no need to adjust the value
			if ((String)compoundFrequencyInterval.getSelectedItem() == "Year(s)") {
				actualAnnualCompoundFrequency = 1 / compoundFrequencyValue;
				control.setCompoundFrequency(String.valueOf(actualAnnualCompoundFrequency));
			} 
			// If Month(s) was chosen, multiply by 12
			else if ((String)compoundFrequencyInterval.getSelectedItem() == "Month(s)") {
				actualAnnualCompoundFrequency = 12 / compoundFrequencyValue;
				control.setCompoundFrequency(String.valueOf(actualAnnualCompoundFrequency));
			} 
			// If Week(s) was chosen, multiply by 52
			else if ((String)compoundFrequencyInterval.getSelectedItem() == "Week(s)") {
				actualAnnualCompoundFrequency = 52 / compoundFrequencyValue;
				control.setCompoundFrequency(String.valueOf(actualAnnualCompoundFrequency));
			} 
			// If Week(s) was chosen, multiply by 365 or 366 (leap year or not?)
			else {
				// Instantiate a calendar to get access to time fields and methods
				GregorianCalendar time = new GregorianCalendar();
				
				// If it is a leap year, multiply value by 366, else by 365
				if (time.isLeapYear(time.get(Calendar.YEAR))) {
					actualAnnualCompoundFrequency = 366 / compoundFrequencyValue;
				} else {
					actualAnnualCompoundFrequency = 365 / compoundFrequencyValue;
				}
				control.setCompoundFrequency(String.valueOf(actualAnnualCompoundFrequency));
			}
			
			// Calculate and display statistics rounded to 2 decimals
			calculationsText.setText(
				("<html>$" + principalInput.getText() +
				"<br>"  + interestRateInput.getText() + "%" +
				"<br>$" + amortizationInput.getText() + paymentFrequency +
				"<br>$" + (double) Math.round(control.computeBlendedMonthlyPayment() * 100) / 100 +
				"<br>$" + (double) Math.round(control.computeTotalInterest() * 100) / 100 + 
				"<br>$" + (double) Math.round(control.computeFinalInvestmentValue() * 100) / 100 + 
				"<br>"  + (double) Math.round(control.computeInterestPrincipalRatio() * 100) / 100 + "%" +
				"<br>$" + (double) Math.round(control.computeAverageAnnualInterest() * 100) / 100 +
				"<br>$" + (double) Math.round(control.computeAverageMonthlyInterest() * 100) / 100 + 
				"<br>$" + (double) Math.round(control.computeAnnualAmortization() * 100) / 100) + "</html>");	
			
			// Clear the text field
			principalInput.setText("");
			interestRateInput.setText("");
			amortizationInput.setText("");
			
			// Enable payment schedule button
			scheduleButton.setEnabled(true);
		}
		  catch (Exception ex) {
			
			  // If input is invalid, show error dialog
			JOptionPane.showMessageDialog(frame,
				    "Please enter valid information.",
				    "Input Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
}
