/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 */

// Class performs calculations and transfers data between MCModel and MCView
public class MCController {
	
	// Instantiate MCModel for access to relevant data
	private MCModel data = new MCModel();
	
	/**
	 * MCController Constructor provides the following for MCModel
	 * @param principle: The principal
	 * @param interestRate: The annual interest rate 
	 * @param amortization: The monthly amortization
	 */
	public MCController (double principle, double interestRate, double amortization) {
		data.setPrincipal(principle);
		data.setAnnualInterestRate(interestRate);
		data.setMonthlyAmortization(amortization);
	}
	
	/**
	 * Default constructor does not initialize any values
	 */
	public MCController () {}
	
	/**
	 * Calculate the interest factor for the given interest
	 * @return interestFactor: The interest factor
	 */
	public double computeInterestFactor () {
		
		// Get the semi-annual compound frequency
		double semiAnnualCompundFrequency = (data.getAnnualCompoundFrequency() / 2);
		
		// Compute the interest factor with the given formula
		double interestFactor = (Math.pow(
			(data.getAnnualInterestRate() / semiAnnualCompundFrequency) + 1, 
			(semiAnnualCompundFrequency / data.getMonthlyPaymentsPerYear())) - 1);
		
		// Return the interest factor
		return interestFactor;
	}
	
	/**
	 * Calculate the loan period for the given data
	 * @return loanPeriod: The loan period in months
	 */
	public double computeLoanPeriodInMonths () {
		
		// Get the required values
		double amortization = data.getMonthlyAmortization();
		double interest = data.getAnnualInterestRate();
		double principal = data.getPrincipal();
		
		// Calculate loan period
		double loanPeriod = (Math.log(amortization) - 
			Math.log(amortization - principal * interest / 12)) / 
			Math.log(1 + interest / 12);
		
		// Return loan period as an integer (number of months)
		return loanPeriod;
	}
	
	/**
	 * Calculate the annual amortization (annual payments)
	 * @return Amortization expressed in years
	 */
	public double computeAnnualAmortization () {
	
		// Get monthly amortization, multiply by 12
		return data.getMonthlyAmortization() * 12;
		
	}
	
	/**
	 * Calculate the total amortization
	 * @return Total amortization
	 */
	public double computeTotalAmortization () {
		
		// Get loan period in months
		double loanPeriod = computeLoanPeriodInMonths ();
		
		// Multiply monthly amortization by loan period
		return data.getMonthlyAmortization() * loanPeriod;
	}
	
	/**
	 * Calculate the blended monthly payment (principal + interest)
	 * @return blendedMonthlyPayment: The blended monthly payment
	 */
	public double computeBlendedMonthlyPayment () {
		
		// Get relevant data
		double principle = data.getPrincipal();
		double interestRate = data.getAnnualInterestRate();
		double totalAmortization = computeTotalAmortization();
		
		// Given formula for blended monthly payment
		double blendedMonthlyPayment = (principle * interestRate) /
				(1 - Math.pow(interestRate + 1, -totalAmortization));
		
		return blendedMonthlyPayment;	
	}
	
	/**
	 * Calculate the total interest paid over the length of the mortgage
	 * @return The total interest
	 */
	public double computeTotalInterest () {
		double principle = data.getPrincipal();
		double annualInterestRate = data.getAnnualInterestRate();
		double loanPeriodInYears =  computeLoanPeriodInMonths() / 12;
		
		// Return interest with formula Principle * Rate * Time
		return principle * annualInterestRate * loanPeriodInYears;
	}

	/**
	 * Calculate the final investment value (principle + interest)
	 * @return The final investment value
	 */
	public double computeFinalInvestmentValue () {
		return data.getPrincipal() + computeTotalInterest();
	}

	/**
	 * Calculate the Interest-to-Principal ratio
	 * @return The Interest-to-Principal ratio
	 */
	public double computeInterestPrincipalRatio () {
		return computeTotalInterest() / data.getPrincipal();
	}
	
	/**
	 * Calculate the average interest per year
	 * @return The average annual interest
	 */
	public double computeAverageAnnualInterest () {
		return computeTotalInterest () / (computeLoanPeriodInMonths () / 12);
	}
	
	/**
	 * Calculate the average interest per month
	 * @return The average monthly interest
	 */
	public double computeAverageMonthlyInterest () {
		return computeTotalInterest () / computeLoanPeriodInMonths ();
	}

	/**
	 * Set Principal in MCModel with input given in MCView
	 * @param principal: The entered principle
	 */
	public void setPrincipal (String principal) {
		data.setPrincipal(Double.parseDouble(principal));
	}
	
	/**
	 * Get Principal
	 * @return The Principal
	 */
	public double getPrincipal() {
		return data.getPrincipal();
	}
	
	/**
	 * Set Interest Rate in MCModel with input given in MCView
	 * @param rate: The entered interest rate
	 */
	public void setInterestRate (String rate) {
		data.setAnnualInterestRate(Double.parseDouble(rate));
	}
	
	/**
	 * Get Annual Interest Rate
	 * @return The annual interest rate
	 */
	public double getInterestRate() {
		return data.getAnnualInterestRate();
	}
	
	/**
	 * Set Amortization in MCModel with input given in MCView
	 * @param amortization: The entered amortization
	 */
	public void setAmortization (String amortization) {
		data.setMonthlyAmortization(Double.parseDouble(amortization));
	}
	
	/**
	 * Get Monthly Amortization
	 * @return The monthly amortization
	 */
	public double getAmortization() {
		return data.getMonthlyAmortization();
	}
}
