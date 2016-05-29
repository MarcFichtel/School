package MortgageCalculator;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** 
 * @author Marc-Andre Fichtel
 * -- Assignment 4
 * -- Course: CPSC 233 
 * -- University of Calgary
 * -- Tutorial 05
 * -- Instructor: Edward Chan 
 * -- Class handles window closing events
 */
public class WindowHandler extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent e) {
		System.exit(0);		// Exit program when window is closed
	}
}
