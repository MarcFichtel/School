import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GSMenuListener implements ActionListener{

	GSView ui;
	
	public GSMenuListener (GSView ui) {
		this.ui = ui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand() == "Start Simulation") {
			System.out.println("Clicked Start");
			ui.showInputDialog();
			
		} else if (e.getActionCommand() == "Save Simulation") {
			System.out.println("Clicked Save");
			
		} else if (e.getActionCommand() == "Load Simulation") {
			System.out.println("Clicked Load");
			
		} else {
			System.out.println("Clicked Exit");
			System.exit(0);
		}
		
	}

}
