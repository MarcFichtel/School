import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * Class handles payment schedule generation
 */
public class GeneratePaymentSchedule implements ActionListener {

	/**
	 * The second of three payment frequency radio buttons
	 */
	private JRadioButton paymentFrequencyBiweekly;
	
	/**
	 * The third of three payment frequency radio buttons
	 */
	private JRadioButton paymentFrequencyWeekly;
	
	/**
	 * The controller that was instantiated in MCView
	 */
	private MCController control;
	
	/**
	 * Constructor assigns the given values
	 * @param paymentFrequencyBiweekly
	 * @param paymentFrequencyWeekly
	 * @param control
	 */
	public GeneratePaymentSchedule (
			JRadioButton paymentFrequencyBiweekly,
			JRadioButton paymentFrequencyWeekly,
			MCController control) {
		
		this.paymentFrequencyBiweekly = paymentFrequencyBiweekly;
		this.paymentFrequencyWeekly = paymentFrequencyWeekly;
		this.control = control;
	}
	
	/**
	 * Generates a JTable payment schedule in a new frame
	 */
	public void actionPerformed(ActionEvent e) {

		// Create new frame for displaying payment schedule
		JFrame frame = new JFrame ("Payment Schedule");
		
		// Get loan period (in months)
		double loanPeriodInMonths = control.computeLoanPeriodInMonths();
		int numberOfPayments;
		
		// Calculate number of payments based on payment frequency
		// Round number up to include last smaller payment
		if (paymentFrequencyBiweekly.isSelected()) {
			
			// For biweekly payments, multiply by 2
			numberOfPayments = (int)(loanPeriodInMonths + 1) * 2;
		} 
		else if (paymentFrequencyWeekly.isSelected()) {
			
			// For weekly payments, multiply by 4
			numberOfPayments = (int)(loanPeriodInMonths + 1) * 4;
		} else {
			
			// For monthly payments, simply round the loan period up			
			numberOfPayments = (int)(loanPeriodInMonths + 1);
		}
		
		// Create table column names
		String[] columns = {"Payment #", "Blended Payment", "Interest", "Principal", "Amount Owed"};
		
		// Create payment schedule in Object Array to pass to table
		Object[][] data = new Object[numberOfPayments][5]; 
		for (int i = 0; i < numberOfPayments; i++) { 	// Iterate through every row (payments)
			for (int j = 0; j < 5; j++) { 				// Iterate through every column (title values)
					
				// The first cell of each row holds the payment number
				if (j == 0) {
					data[i][j] = i + 1;
				} 
				
				// The second cell of each row holds the blended payment amount
				else if (j == 1) {
					data[i][j] = "$" + (double) Math.round(control.computeBlendedMonthlyPayment() * 100) / 100;
				} 
				
				// The third cell of each row holds the interest
				else if (j == 2) {
					data[i][j] = (double) Math.round(control.getInterestRate() * 100) / 100 + "%";
				} 
				
				// The fourth cell of each row holds the principal
				else if (j == 3) {
					data[i][j] = "$" + (double) Math.round(control.getPrincipal() * 100) / 100;
				} 
				
				// The fifth cell of each row holds the amount owed
				else if (j == 4) {
					data[i][j] = "$" + ((double) Math.round(control.computeFinalInvestmentValue() 
							* 100) / 100 - i * (double) Math.round(control.getAmortization()));
				}
			}
		}
		
		// Create table in a scroll panel, add scroll panel to frame
		JTable table = new JTable(data, columns);
		JScrollPane scrollPane = new JScrollPane(table);
		frame.getContentPane().add(scrollPane); 
		
		frame.setSize(600, 900);						// Set frame size
		frame.addWindowListener(new WindowHandler());	// Add window event handler	
		frame.setLocation(535, 0);						// Locate frame next to main frame
		frame.setVisible(true); 						// Make frame visible
	}

}
