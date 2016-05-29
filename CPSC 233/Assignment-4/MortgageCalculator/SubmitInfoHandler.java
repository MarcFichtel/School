package MortgageCalculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JOptionPane;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class handles user information submission
 */
public class SubmitInfoHandler implements ActionListener {
	
	/**
	 * data: The mortgage calculation model
	 */
	private MCModel data;
	
	/**
	 * ui: The mortgage calculator GUI
	 */
	private MCView ui;
	
	/**
	 * Constructor assigns the given values
	 * @param data: The mortgage calculation model
	 * @param ui: The mortgage calculator GUI
	 */
	public SubmitInfoHandler (MCModel data, MCView ui) {
		this.data = data;
		this.ui = ui;
	}
	
	/**
	 * The mortgage calculator computes and displays relevant information
	 * when the according button is pressed
	 * -- Validate given data
	 * -- Reset text fields
	 * -- Calculate and display statistics depending on user choices
	 * -- Show error messages for invalid data
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			
			// Check (& convert), if user input can be cast to required data types
			Double convertedPrincipal = Double.parseDouble(ui.getPrincipalTextField().getText());
			Double convertedInterestRate = Double.parseDouble(ui.getInterestRateTextField().getText()) / 100;
			int convertedAmortization = Integer.parseInt(ui.getAmortizationTextField().getText());
			int convertedCompoundFrequency = Integer.parseInt(ui.getCompoundFrequencyTextField().getText());
			
			// Multiply amortization depending on chosen frequency
			String paymentFrequencyChoice;
			int paymentsPerYear;
			int paymentsPerMonth;
			if (ui.getAmortizationDropDown().getSelectedItem() == "Bi-Weekly") {
				convertedAmortization /= 2;
				paymentFrequencyChoice = "Bi-Weekly";
				paymentsPerYear = 28;
				paymentsPerMonth = 2;
			} else if (ui.getAmortizationDropDown().getSelectedItem() == "Weekly") {
				convertedAmortization /= 4;
				paymentFrequencyChoice = "Weekly";
				paymentsPerYear = 56;
				paymentsPerMonth = 4;
			} else {
				paymentFrequencyChoice = "Monthly";
				paymentsPerYear = 12;
				paymentsPerMonth = 1;
			}
			
			// If Year(s) was chosen, no need to adjust the value
			int annualCompoundFrequency;
			if (ui.getCompoundDropDown().getSelectedItem() == "Year(s)") {
				annualCompoundFrequency = convertedCompoundFrequency;
			} 
			
			// If Month(s) was chosen, multiply by 12
			else if ((ui.getCompoundDropDown().getSelectedItem() == "Month(s)")) {
				annualCompoundFrequency = convertedCompoundFrequency * 12;
			} 
						
			// If Week(s) was chosen, multiply by 56
			else if (ui.getCompoundDropDown().getSelectedItem() == "Week(s)") {
				annualCompoundFrequency = convertedCompoundFrequency * 56;
			} 
						
			// If Week(s) was chosen, multiply by 365 or 366 (leap year or not?)
			else {
				// Instantiate a calendar to get access to time fields and methods
				GregorianCalendar time = new GregorianCalendar();
							
				// If it is a leap year, multiply value by 366, else by 365
				if (time.isLeapYear(time.get(Calendar.YEAR))) {
					annualCompoundFrequency = convertedCompoundFrequency * 366;
				} else {
					annualCompoundFrequency = convertedCompoundFrequency * 365;
				}
			}
						
			// Set the indicated information to the given value
			data.setPrincipal(convertedPrincipal);
			data.setAnnualInterestRate(convertedInterestRate);
			data.setAmortization(convertedAmortization);
			data.setPaymentsPerYear(paymentsPerYear);
			data.setPaymentsPerMonth(paymentsPerMonth);
			data.setAnnualCompoundFrequency(annualCompoundFrequency);
			
			// Calculate and display statistics, rounded to 2 decimals where appropriate
			ui.getStatsTextField().setText(
				("<html>$" + ui.getPrincipalTextField().getText() +
				"<br>"  + ui.getInterestRateTextField().getText() + "%" +
				"<br>" + ui.getAmortizationTextField().getText() + " " + paymentFrequencyChoice + " Payments" + 
				"<br>$" + (double) Math.round(data.computeBlendedMonthlyPayment() * 100) / 100 +
				"<br>$" + (double) Math.round(data.computeTotalInterest() * 100) / 100 + 
				"<br>$" + (double) Math.round(data.computeFinalInvestValue() * 100) / 100 + 
				"<br>"  + (double) Math.round(data.computeInterestPrincipalRatio() * 100) / 100 + "%" +
				"<br>$" + (double) Math.round(data.computeAverageAnnualInterest() * 100) / 100 +
				"<br>$" + (double) Math.round(data.computeAverageMonthlyInterest() * 100) / 100 + "</html>"));	
			
			// Clear the text fields
			ui.setPrincipalTextField("");
			ui.setInterestRateTextField("");
			ui.setAmortizationTextField("");
			
			// Enable payment schedule button
			ui.getScheduleButton().setEnabled(true);
		}
		// If input is invalid, show error dialog
		catch (Exception ex) {
			JOptionPane.showMessageDialog(ui.getFrame(),
				    "Please enter valid information.",
				    "Input Error",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
}
