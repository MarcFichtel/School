package MortgageCalculator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class handles payment schedule generation
 * -- TODO There appear to be small rounding errors throughout the balance owing.
 * Identify the source of these and fix them. Note that they are not caused
 * by the uses of Math.round() in this class (the rounding errors persist
 * even when instances of Math.round() are removed from this class).
 */
public class GeneratePaymentSchedule implements ActionListener {
	
	/**
	 * data: The mortgage calculation model
	 */
	private MCModel data;
	
	/**
	 * Constructor assigns the given values
	 * @param data: The mortgage calculation mode
	 */
	public GeneratePaymentSchedule (MCModel data) {
		this.data = data;
	}
	
	/**
	 * A payment schedule is generated when the according button is pressed
	 * -- Create a new JFrame to hold the payment schedule
	 * -- Create payment schedule and display it
	 */
	public void actionPerformed(ActionEvent e) {

		// Create new frame for displaying payment schedule
		JFrame frame = new JFrame ("Payment Schedule");
		
		// Create and initialize variables for payment schedule display
		double principalComponent = 0.0;
		double interestComponent = 0.0;
		int blendedAmount = (int)Math.round(data.computeBlendedMonthlyPayment());
		int numberOfPayments = (int)data.getAmortization();
		
		// Create table column names
		String[] columns = {"Payment #", "Blended Payment", "Interest", "Principal", "Amount Owed"};
		
		// Create payment schedule in 2D-Object Array to pass to table
		Object[][] scheduleData = new Object[numberOfPayments][5]; 
		for (int i = 0; i < numberOfPayments; i++) { 	// Iterate through every row (payments)
			for (int j = 0; j < 5; j++) { 				// Iterate through every column (title values)
					
				// The first cell of each row holds the payment number
				if (j == 0) {
					scheduleData[i][j] = i + 1;
				} 
				
				// The second cell of each row holds the blended payment (principal + interest)
				else if (j == 1) {
					scheduleData[i][j] = "$" + blendedAmount;
				} 
				
				// The third cell of each row holds the interest component
				else if (j == 2) {
					interestComponent = data.computeTotalInterest() / data.getAmortization();
					scheduleData[i][j] = ("$" + (int)(Math.round(interestComponent)));
				} 
				
				// The fourth cell of each row holds the principal component
				else if (j == 3) {
					principalComponent = blendedAmount -
							(data.computeTotalInterest() / data.getAmortization());
					scheduleData[i][j] = ("$" + (int)Math.round(principalComponent));
					
					// Deduct principal component from remaining principal owed
					data.setPrincipal(data.getPrincipal() - principalComponent);
				} 
				
				// The fifth cell of each row holds the amount owed
				else if (j == 4) {
					scheduleData[i][j] = "$" + (int)Math.round(data.getPrincipal());
				}
			}
		}
		
		// Create table in a scroll panel, add scroll panel to frame
		JTable table = new JTable(scheduleData, columns);
		JScrollPane scrollPane = new JScrollPane(table);
		frame.getContentPane().add(scrollPane); 
		
		frame.setSize(600, 900);								// Set frame size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// Set default close operation
		frame.setLocation(535, 0);								// Locate frame next to main frame
		frame.setVisible(true); 								// Make frame visible
	}

}
