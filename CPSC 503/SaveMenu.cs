using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.UI;

// Class represents the Save Menu
public class SaveMenu {

	#region Variables

	// Is the menu being diaplyed?
	private bool isShowing = false;

	// Menu parts
	private Transform saveMenuTransform;	// The object containing the menu
	private InputField inputField;			// The input field
	private Button saveButton;				// The save button
	private Button closeMenuButton;         // The close button
	private Toggle saveUserToggle;          // The save user position toggle

	#endregion

	#region Constructor

	// Constructor
	public SaveMenu (Canvas saveMenuCanvas) {

		// Get menu parts
		saveMenuTransform = saveMenuCanvas.transform.GetChild(0);
		saveUserToggle = saveMenuTransform.transform.GetChild(2).GetComponent<Toggle>();
		saveButton = saveMenuTransform.GetChild(3).GetComponent<Button>();
		inputField = saveMenuTransform.GetChild(4).GetComponent<InputField>();
		closeMenuButton = saveMenuTransform.GetChild(5).GetComponent<Button>();

		// Assign button handlers
		saveButton.onClick.AddListener(saveFile);
		closeMenuButton.onClick.AddListener(closeMenu);
	}

	#endregion

	#region Button Handlers

	// Save button handler
	public void saveFile() {
		if (inputField.text != null) {
			GameController.Instance.saveToFile(inputField.text, saveUserToggle.isOn);
			closeMenu();
		}
	}

	// Close menu button handler
	public void closeMenu() {
		toggleMenu();
		inputField.text = "";
	}

	#endregion

	#region Util Functions

	// Toggle object manipulation menu on or off 
	public void toggleMenu() {
		Animator animator = saveMenuTransform.GetComponent<Animator>();
		if (!isShowing) {
			inputField.Select();
			animator.SetBool("slideOn", true);
			isShowing = true;
		} else {
			inputField.OnDeselect (new BaseEventData(EventSystem.current));
			animator.SetBool("slideOn", false);
			isShowing = false;
		}
	}

	// Is menu currently open?
	public bool isOpen() {
		return isShowing;
	}

	#endregion
}
