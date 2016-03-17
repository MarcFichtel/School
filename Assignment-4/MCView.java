/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */

// import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MCView {

	public static void main (String[] args) {
		initUI();
	}
	
	private static void initUI () {

		final MCController control = new MCController ();
		
		final JFrame frame = new JFrame("Mortgage Calculator");
		final JPanel mainPanel = new JPanel();
		
		JLabel title = new JLabel("Welcome! \nEnter the following to calculate your Mortgage Statistics.");
		JLabel principleHelpText = new JLabel("Enter Principle:");
		JLabel interestRateHelpText = new JLabel("Enter Annual Interest Rate:");
		JLabel amortizationHelpText = new JLabel("Enter Monthly Amortization ($):");
		
		final JTextField principleTextField = new JTextField(10);
		final JTextField interestRateTextField = new JTextField(10);
		final JTextField amortizationTextField = new JTextField(10);
		
		JButton principleSubmit = new JButton("Submit");
		JButton rateSubmit = new JButton("Submit");
		JButton amortizationSubmit = new JButton("Submit");
		JButton startCalculationsButton = new JButton("Submit All");
		 
		principleSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Double.parseDouble(principleTextField.getText());
					control.setPrincipal(principleTextField.getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame,
						    "Please enter a number.",
						    "Input Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		rateSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Double.parseDouble(interestRateTextField.getText());
					control.setInterestRate(interestRateTextField.getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame,
						    "Please enter a number.",
						    "Input Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		amortizationSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Double.parseDouble(amortizationTextField.getText());
					control.setAmortization(amortizationTextField.getText());
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame,
						    "Please enter a number.",
						    "Input Error",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		startCalculationsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				boolean statsShowing = false;
				
				if (control.getPrincipal() != 0.0 &&
					control.getInterestRate() != 0.0 &&
					control.getAmortization() != 0.0 &&
					!statsShowing) {
					
					statsShowing = true;
					
					JLabel showStatsText = new JLabel(
						"Principal: " + control.getPrincipal() + "\n"
						+ "Annual Interest Rate: " + (double) Math.round(control.getInterestRate() * 100) / 100 + "<html><br></html>"
						+ "Monthly Amortization: " + (double) Math.round(control.getAmortization() * 100) / 100 + "\n"
						+ "Blended Monthly Payment: " + (double) Math.round(control.computeBlendedMonthlyPayment() * 100) / 100 + "\n"
						+ "Total Interest: " + (double) Math.round(control.computeTotalInterest() * 100) / 100 + "\n"
						+ "Final Investment Value: " + (double) Math.round(control.computeFinalInvestmentValue() * 100) / 100 + "\n"
						+ "Interest/Principle Ratio: " + (double) Math.round(control.computeInterestPrincipalRatio() * 100) / 100 + "\n"
						+ "Average Annual Interest: " + (double) Math.round(control.computeAverageAnnualInterest() * 100) / 100 + "\n"
						+ "Average Monthly Interest: " + (double) Math.round(control.computeAverageMonthlyInterest() * 100) / 100 + "\n"
						+ "Annual Amortization: " + (double) Math.round(control.computeAnnualAmortization() * 100) / 100);	
		
					mainPanel.add(showStatsText);
				} else {
					JOptionPane.showMessageDialog(frame,
						"Please enter all required information.",
						"Input Incomplete",
						JOptionPane.ERROR_MESSAGE);
					System.out.println("Principal: " + control.getPrincipal() + "\n"
							+ "Annual Interest Rate: " + (double) Math.round(control.getInterestRate() * 100) / 100 + "\n"
							+ "Monthly Amortization: " + (double) Math.round(control.getAmortization() * 100) / 100);
				}
			}
		});
		
		mainPanel.add(title);
		mainPanel.add(principleHelpText);
		mainPanel.add(principleTextField);
		mainPanel.add(principleSubmit);
		mainPanel.add(interestRateHelpText);
		mainPanel.add(interestRateTextField);
		mainPanel.add(rateSubmit);
		mainPanel.add(amortizationHelpText);
		mainPanel.add(amortizationTextField);
		mainPanel.add(amortizationSubmit);
		mainPanel.add(startCalculationsButton);
		 
		WindowHandler wh = new WindowHandler();
		frame.addWindowListener(wh);
		frame.getContentPane().add(mainPanel);
		frame.setSize(400, 300);
		frame.setVisible(true);
		


	}
	
	
}
