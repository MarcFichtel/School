package MortgageCalculator;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class creates a link between MCModel and MCView, 
 * runs the program, and adds user interaction events
 */
public class MCController {
	
	/**
	 * data: Instantiation of the mortgage calculation model
	 */
	private static MCModel data = new MCModel();
	
	/**
	 * ui: Instantiation of the mortgage calculator ui (JFrame has 3 rows, 2 columns)
	 */
	private static MCView ui = new MCView(3, 2);
	
	/**
	 *  Start program by setting up the UI, add user interaction listeners
	 */
	public static void main (String[] args) {
		ui.initUI();
		ui.addSubmitInfoListener(new SubmitInfoHandler(data, ui));
		ui.addScheduleListener(new GeneratePaymentSchedule(data));
	}
}