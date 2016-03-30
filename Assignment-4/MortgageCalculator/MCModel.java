package MortgageCalculator;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class calculates and stores data used for Mortgage Calculatoions
 */
public class MCModel {

	/** 
	 * amortizationInMonths: Number of Monthly payments
	 */
	private double amortizationInMonths;
	
	/**
	 * principal: The total amount loaned
	 */
	private double principal;
	
	/**
	 * annualInterestRate: Annual interest rate
	 */
	private double annualInterestRate;
	
	/**
	 * annualInterestRate: Annual compounding frequency 
	 */
	private int compoundFrequency;

	/**
	 * paymentsPerYear: The number of payments per year
	 */
	private int paymentsPerYear;
	
	/**
	 * paymentsPerMonth: The number of payments per month
	 */
	private int paymentsPerMonth;
	
	/**
	 * Set the amortization (number of monthly payments)
	 * @param newAmortization: New number of monthly payments
	 */
	public void setAmortization(int newAmortization) {
		amortizationInMonths = newAmortization;
	}
	
	/**
	 * Get the amortization (number of monthly payments)
	 * @return amortizationInMonths: The current amortization
	 */
	public double getAmortization() {
		return amortizationInMonths;
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
	public void setAnnualCompoundFrequency (int newCompoundFrequency) {
		compoundFrequency = newCompoundFrequency;
	}
	
	/**
	 * Get the compounding frequency
	 * @return compoundFrequency: The current compounding frequency
	 */
	public double getAnnualCompoundFrequency() {
		return compoundFrequency;
	}
	
	/**
	 * Set the annual payment frequency
	 * @param newAnnualPaymentFrequency: The new number of payments per year
	 */
	public void setPaymentsPerYear (int newPaymentsPerYear) {
		paymentsPerYear = newPaymentsPerYear;
	}
	
	/**
	 * Get the payment frequency
	 * @return paymentFrequency: The frequency of payments (monthly, bi-weekly, weekly)
	 */
	public double getPaymentsPerYear() {
		return paymentsPerYear;
	}
	
	/**
	 * Set the monthly payment frequency
	 * @param newMonthlyPaymentFrequency: The new number of payments per month
	 */
	public void setPaymentsPerMonth (int newPaymentsPerMonth) {
		paymentsPerMonth = newPaymentsPerMonth;
	}
	
	/**
	 * Get the payment frequency
	 * @return paymentFrequency: The frequency of payments (monthly, bi-weekly, weekly)
	 */
	public double getPaymentsPerMonth() {
		return paymentsPerMonth;
	}
	
	/**
	 * Calculate the interest factor for the given interest
	 * @return interestFactor: The interest factor
	 */
	public double computeInterestFactor () {
		
		// Compute the interest factor with the given formula
		double interestFactor = (Math.pow(
			(annualInterestRate / compoundFrequency + 1), 
			(compoundFrequency / getPaymentsPerYear())) - 1);
		
		return interestFactor;
	}

	/**
	 * Calculate the blended monthly payment (principal + interest)
	 * @return blendedMonthlyPayment: The blended monthly payment
	 */
	public double computeBlendedMonthlyPayment () {
		
		// Given formula for blended monthly payment
		double blendedMonthlyPayment = (principal * computeInterestFactor ()) /
				(1 - Math.pow(computeInterestFactor () + 1, -amortizationInMonths));
		
		return blendedMonthlyPayment;	
	}
	
	/**
	 * Calculate the total interest to be paid over the length of the mortgage
	 * @return The total interest
	 */
	public double computeTotalInterest () {
		
		// Round and return interest with formula Principle * Rate * Time
		return Math.round((principal * annualInterestRate * computeAmortizationInYears()) / 100) * 100;
	}

	/**
	 * Calculate the Interest-to-Principal ratio
	 * @return The Interest-to-Principal ratio
	 */
	public double computeInterestPrincipalRatio () {
		return computeTotalInterest() / principal;
	}
	
	/**
	 * Calculate the average interest per year
	 * @return The average annual interest
	 */
	public double computeAverageAnnualInterest () {
		return computeTotalInterest () / (amortizationInMonths / 12);
	}
	
	/**
	 * Calculate the average interest per month
	 * @return The average monthly interest
	 */
	public double computeAverageMonthlyInterest () {
		return computeTotalInterest () / amortizationInMonths;
	}
	
	/**
	 * Calculate the final investment value (principal + interest)
	 * @return The final investment value
	 */
	public double computeFinalInvestValue () {
		return principal + computeTotalInterest();
	}
	
	/**
	 * Calculate the amortization expressed in years
	 * @return The amortization expressed in years
	 */
	public double computeAmortizationInYears () {
		return amortizationInMonths / 12;
	}
}