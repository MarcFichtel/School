import java.util.TimerTask;

// Class handles timeouts from Router.java
public class TimeoutHandler extends TimerTask {

	Router main;

	// Constructor
	public TimeoutHandler(Router main) {
		this.main = main;
	}

	// On timeout, update neighbors
	public void run() {
		System.out.println("TIMEOUT!");
		main.updateNeighbors();
	}
}
