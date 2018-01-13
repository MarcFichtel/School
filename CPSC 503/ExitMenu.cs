using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

// Class represents the Exit Menu.
public class ExitMenu {

	#region Variables

	// Is the menu being diaplyed?
	private bool isShowing = false;
	
	// Menu parts
	private Transform exitMenuTransform;    // The object containing the menu
	private Button exitButton;              // The save button
	private Button closeMenuButton;         // The close button

	#endregion

	#region Constructor

	// Constructor
	public ExitMenu(Canvas exitMenuCanvas) {

		// Get menu parts
		exitMenuTransform = exitMenuCanvas.transform.GetChild(0);
		exitButton = exitMenuTransform.GetChild(2).GetComponent<Button>();
		closeMenuButton = exitMenuTransform.GetChild(3).GetComponent<Button>();

		// Assign button handlers
		exitButton.onClick.AddListener(exitSimulation);
		closeMenuButton.onClick.AddListener(closeMenu);
	}

	#endregion

	#region Button handlers

	// Close menu button handler
	public void closeMenu() {
		toggleMenu();
	}

	#endregion

	#region Util Functions

	// Toggle object manipulation menu on or off 
	public void toggleMenu() {
		Animator animator = exitMenuTransform.GetComponent<Animator>();
		if (!isShowing) {
			animator.SetBool("slideOn", true);
			isShowing = true;
		} else {
			animator.SetBool("slideOn", false);
			isShowing = false;
		}
	}

	// Exit simulation (go to main menu)
	public void exitSimulation() {
		SceneManager.LoadScene(0);
	}

	// Is menu currently open?
	public bool isOpen() {
		return isShowing;
	}

	#endregion
}
