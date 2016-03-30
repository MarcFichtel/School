/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 5
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class does something...
 */

public class GSController {

	private static GSView ui = new GSView();
	//private static GSModel data = new GSModel();
	
	public static void main(String[] args) {
		ui.initUI(new GSMenuListener(ui));
	}
	
	
}
