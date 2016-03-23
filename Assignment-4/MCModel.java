/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * Class stores data used for Mortgage Calculator
 */
public class MCModel {

	/** 
	 * Amortization: Monthly payments
	 */
	private double monthlyAmortization;
	
	/**
	 * Principal: The total amount loaned
	 */
	private double principal;
	
	/**
	 * Annual interest rate
	 */
	private double annualInterestRate;
	
	/**
	 * Annual compounding frequency 
	 */
	private int compoundFrequency;
	
	/**
	 * Monthly payments per year = 12 (one per month)
	 */
	private final int MONTHLY_PAYMENTS_PER_YEAR = 12;

	/**
	 * Set the amortization (number of monthly payments)
	 * @param newAmortization: New number of monthly payments
	 */
	public void setMonthlyAmortization(double newAmortization) {
		monthlyAmortization = newAmortization;
	}
	
	/**
	 * Get the amortization (number of monthly payments)
	 * @return amortization: The current amortization
	 */
	public double getMonthlyAmortization() {
		return monthlyAmortization;
	}
	
	/**
	 * Set the principal (the total amount loaned)
	 * @param newPrincipal: New principal
	 */
	public void setPrincipal(double newPrincipal) {
		principal = newPrincipal;
	}
	
	/**
	 * Get the principal (the total amount loaned)
	 * @return principal: The current principal
	 */
	public double getPrincipal() {
		return principal;
	}
	
	/**
	 * Set the annual interest rate
	 * @param newAnnualInterestRate: The new annual interest rate
	 */
	public void setAnnualInterestRate(double newAnnualInterestRate) {
		annualInterestRate = newAnnualInterestRate;
	}
	
	/**
	 * Get the annual interest rate
	 * @return annualInterestRate: The current annual interest rate
	 */
	public double getAnnualInterestRate() {
		return annualInterestRate;
	}
	
	/**
	 * Set the compounding frequency
	 * @param newCompoundFrequency: The new compounding frequency
	 */
	public void setCompoundFrequency (int newCompoundFrequency) {
		compoundFrequency = newCompoundFrequency;
	}
	
	/**
	 * Get the compounding frequency
	 * @return compoundFrequency: The current compounding frequency
	 */
	public int getAnnualCompoundFrequency() {
		return compoundFrequency;
	}
	
	/**
	 * Get the number of monthly payments per year
	 * @return MONTHLY_PAYMENTS_PER_YEAR: 12
	 */
	public int getMonthlyPaymentsPerYear() {
		return MONTHLY_PAYMENTS_PER_YEAR;
	}
}