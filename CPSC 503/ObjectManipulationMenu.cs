using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
using TMPro;

// Class represents the Object Manipulation Menu.
public class ObjectManipulationMenu {

	#region Variables

	// Object manipulation menus
	private Transform objectManipulationMenu;

	// Is the menu being diaplyed?
	private bool isShowing = false;

	// Object transform before edit
	private Vector3 originalPosition;
	private Quaternion originalRotation;
	private Vector3 originalScale;
	private float originalIntensity;

	// Buttons
	private Button sizeIncrButton;
	private Button sizeDecrButton;
	private Button rotXIncrButton;
	private Button rotXDecrButton;
	private Button rotYIncrButton;
	private Button rotYDecrButton;
	private Button rotZIncrButton;
	private Button rotZDecrButton;
	private Button locXIncrButton;
	private Button locXDecrButton;
	private Button locYIncrButton;
	private Button locYDecrButton;
	private Button locZIncrButton;
	private Button locZDecrButton;
	private Button intyIncrButton;
	private Button intyDecrButton;
	private Button confirmButton;
	private Button cancelButton;
	private Button deleteButton;

	// Object name text
	private TextMeshProUGUI objectName;

	// Change amounts
	private float changeSizeAmount = 0.1f;
	private float changeRotationAmount = 0.1f;
	private float changeLocationAmount = 0.5f;
	private float changeIntensityAmount = 0.5f;

	// Intensity section (only used for light objects)
	private GameObject intensitySection;

	#endregion

	#region Constructor

	// Constructor
	public ObjectManipulationMenu (Canvas objectManipulationMenuCanvas) {

		// Get menu transforms
		objectManipulationMenu = objectManipulationMenuCanvas.transform.GetChild(0);

		// Get object name text
		objectName = objectManipulationMenu.transform.GetChild(1).GetComponent<TextMeshProUGUI>();

		// Get intensity section
		intensitySection = objectManipulationMenu.transform.GetChild(5).gameObject;

		// Get buttons
		sizeIncrButton = objectManipulationMenu.transform.GetChild(2).transform.GetChild(1).GetComponent<Button>();
		sizeDecrButton = objectManipulationMenu.transform.GetChild(2).transform.GetChild(2).GetComponent<Button>();
		rotXIncrButton = objectManipulationMenu.transform.GetChild(3).transform.GetChild(1).transform.GetChild(0).GetComponent<Button>();
		rotXDecrButton = objectManipulationMenu.transform.GetChild(3).transform.GetChild(1).transform.GetChild(1).GetComponent<Button>();
		rotYIncrButton = objectManipulationMenu.transform.GetChild(3).transform.GetChild(2).transform.GetChild(1).GetComponent<Button>();
		rotYDecrButton = objectManipulationMenu.transform.GetChild(3).transform.GetChild(2).transform.GetChild(2).GetComponent<Button>();
		rotZIncrButton = objectManipulationMenu.transform.GetChild(3).transform.GetChild(3).transform.GetChild(1).GetComponent<Button>();
		rotZDecrButton = objectManipulationMenu.transform.GetChild(3).transform.GetChild(3).transform.GetChild(2).GetComponent<Button>();
		locXIncrButton = objectManipulationMenu.transform.GetChild(4).transform.GetChild(1).transform.GetChild(0).GetComponent<Button>();
		locXDecrButton = objectManipulationMenu.transform.GetChild(4).transform.GetChild(1).transform.GetChild(1).GetComponent<Button>();
		locYIncrButton = objectManipulationMenu.transform.GetChild(4).transform.GetChild(2).transform.GetChild(1).GetComponent<Button>();
		locYDecrButton = objectManipulationMenu.transform.GetChild(4).transform.GetChild(2).transform.GetChild(2).GetComponent<Button>();
		locZIncrButton = objectManipulationMenu.transform.GetChild(4).transform.GetChild(3).transform.GetChild(1).GetComponent<Button>();
		locZDecrButton = objectManipulationMenu.transform.GetChild(4).transform.GetChild(3).transform.GetChild(2).GetComponent<Button>();
		intyIncrButton = intensitySection.transform.GetChild(1).GetComponent<Button>();
		intyDecrButton = intensitySection.transform.GetChild(2).GetComponent<Button>();
		confirmButton = objectManipulationMenu.transform.GetChild(6).transform.GetChild(0).GetComponent<Button>();
		cancelButton = objectManipulationMenu.transform.GetChild(6).transform.GetChild(1).GetComponent<Button>();
		deleteButton = objectManipulationMenu.transform.GetChild(6).transform.GetChild(2).GetComponent<Button>();

		// Assign functions to buttons
		sizeIncrButton.onClick.AddListener(incrSize);
		sizeDecrButton.onClick.AddListener(decrSize);
		rotXIncrButton.onClick.AddListener(incrRotX);
		rotXDecrButton.onClick.AddListener(decrRotX);
		rotYIncrButton.onClick.AddListener(incrRotY);
		rotYDecrButton.onClick.AddListener(decrRotY);
		rotZIncrButton.onClick.AddListener(incrRotZ);
		rotZDecrButton.onClick.AddListener(decrRotZ);
		locXIncrButton.onClick.AddListener(incrLocX);
		locXDecrButton.onClick.AddListener(decrLocX);
		locYIncrButton.onClick.AddListener(incrLocY);
		locYDecrButton.onClick.AddListener(decrLocY);
		locZIncrButton.onClick.AddListener(incrLocZ);
		locZDecrButton.onClick.AddListener(decrLocZ);
		intyIncrButton.onClick.AddListener(incrInty);
		intyDecrButton.onClick.AddListener(decrInty);
		confirmButton.onClick.AddListener(closeMenu);
		cancelButton.onClick.AddListener(cancelManip);
		deleteButton.onClick.AddListener(deleteObj);
	}

	#endregion

	#region Toggle Object Manipulation Menu

	// Toggle object manipulation menu on or off 
	public void toggleMenu () {
		Animator animator = objectManipulationMenu.GetComponent<Animator>();
		if (!isShowing) {
			animator.SetBool("slideOn", true);
			isShowing = true;
			objectName.text = SelectionController.Instance.getSelected().gameObject.name;

			// Get object's transform
			originalPosition = SelectionController.Instance.getSelected().gameObject.transform.localPosition;
			originalRotation = SelectionController.Instance.getSelected().gameObject.transform.localRotation;
			originalScale = SelectionController.Instance.getSelected().gameObject.transform.localScale;

			// Show intensity part, if selected object is a light source
			if (SelectionController.Instance.getSelected() is LightEditableObject &&
				!intensitySection.activeSelf) {
				intensitySection.SetActive(true);
				originalIntensity = SelectionController.Instance.getSelected().gameObject.GetComponent<Light>().intensity;

			// Hide intensity part, if selected object is not a light source
			} else if (intensitySection.activeSelf) {
				intensitySection.SetActive(false);
			}

		} else {
			animator.SetBool("slideOn", false);
			isShowing = false;
		}
	}

	#endregion

	#region Button Handlers

	// Close the OMM (can just use toggleMenu since button can't be pressed offscreen)
	public void closeMenu() {

		// If an object is selected, unselect it
		if (SelectionController.Instance.getSelected() != null) {
			SelectionController.Instance.setSelected(SelectionController.Instance.getSelected(), false);
		} 
	}

	// Increment object size
	public void incrSize() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;	// Get obj transform
		Vector3 size = obj.localScale;														// Get obj scale
		size += new Vector3(changeSizeAmount, changeSizeAmount, changeSizeAmount);			// Add amount
		obj.localScale = size;                                                              // Apply new scale
	}

	// Decrement object size
	public void decrSize() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Vector3 size = obj.localScale;                                                      // Get obj scale
		size -= new Vector3(changeSizeAmount, changeSizeAmount, changeSizeAmount);          // Subtract amount
		obj.localScale = size;                                                              // Apply new scale
	}

	// Increment object rotation X
	public void incrRotX() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Quaternion rot = obj.rotation;                                                      // Get obj rotation
		rot.x += changeRotationAmount;														// Add X amount
		obj.rotation = rot;                                                                 // Apply new rotation
	}

	// Decrement object rotation X
	public void decrRotX() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Quaternion rot = obj.rotation;                                                      // Get obj rotation
		rot.x -= changeRotationAmount;                                                      // Subtract X amount
		obj.rotation = rot;                                                                 // Apply new rotation
	}

	// Increment object rotation Y
	public void incrRotY() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Quaternion rot = obj.rotation;                                                      // Get obj rotation
		rot.y += changeRotationAmount;                                                      // Add Y amount
		obj.rotation = rot;                                                                 // Apply new rotation
	}

	// Decrement object rotation Y
	public void decrRotY() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Quaternion rot = obj.rotation;                                                      // Get obj rotation
		rot.y -= changeRotationAmount;                                                      // Subtract Y amount
		obj.rotation = rot;                                                                 // Apply new rotation
	}

	// Increment object rotation Z
	public void incrRotZ() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Quaternion rot = obj.rotation;                                                      // Get obj rotation
		rot.z += changeRotationAmount;                                                      // Add Z amount
		obj.rotation = rot;                                                                 // Apply new rotation
	}

	// Decrement object rotation Z
	public void decrRotZ() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Quaternion rot = obj.rotation;                                                      // Get obj rotation
		rot.z -= changeRotationAmount;                                                      // Subtract Z amount
		obj.rotation = rot;                                                                 // Apply new rotation
	}

	// Increment object location X
	public void incrLocX() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Vector3 loc = obj.position;															// Get obj location
		loc.x += changeLocationAmount;                                                      // Add X amount
		obj.position = loc;                                                                 // Apply new location
	}

	// Decrement object location X
	public void decrLocX() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Vector3 loc = obj.position;                                                         // Get obj location
		loc.x -= changeLocationAmount;                                                      // Subtract X amount
		obj.position = loc;                                                                 // Apply new location
	}

	// Increment object location Y
	public void incrLocY() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Vector3 loc = obj.position;                                                         // Get obj location
		loc.y += changeLocationAmount;                                                      // Add Y amount
		obj.position = loc;                                                                 // Apply new location
	}

	// Decrement object location Y
	public void decrLocY() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Vector3 loc = obj.position;                                                         // Get obj location
		loc.y -= changeLocationAmount;                                                      // Subtract Y amount
		obj.position = loc;                                                                 // Apply new location
	}
	
	// Increment object location Z
	public void incrLocZ() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Vector3 loc = obj.position;                                                         // Get obj location
		loc.z += changeLocationAmount;                                                      // Add Z amount
		obj.position = loc;                                                                 // Apply new location
	}

	// Decrement object location Z
	public void decrLocZ() {
		Transform obj = SelectionController.Instance.getSelected().gameObject.transform;    // Get obj transform
		Vector3 loc = obj.position;                                                         // Get obj location
		loc.z -= changeLocationAmount;                                                      // Subtract Z amount
		obj.position = loc;                                                                 // Apply new location
	}

	// Increment light intensity
	public void incrInty() {
		Light light = SelectionController.Instance.getSelected().GetComponent<Light>();		// Get light source
		light.intensity += changeIntensityAmount;											// Add change amount
	}

	// Decrement light intensity
	public void decrInty() {
		Light light = SelectionController.Instance.getSelected().GetComponent<Light>();		// Get light source
		light.intensity -= changeIntensityAmount;											// Subtract change amount
	}

	// Cancel object manipulation by retracing every action in reverse
	public void cancelManip() {

		// Restore object's original transform
		SelectionController.Instance.getSelected().gameObject.transform.localPosition = originalPosition;
		SelectionController.Instance.getSelected().gameObject.transform.localRotation = originalRotation;
		SelectionController.Instance.getSelected().gameObject.transform.localScale = originalScale;
		if (SelectionController.Instance.getSelected() is LightEditableObject) {
			SelectionController.Instance.getSelected().GetComponent<Light>().intensity = originalIntensity;
		}

		// Unselect object
		SelectionController.Instance.setSelected(SelectionController.Instance.getSelected(), false);
	}

	// Delete object manipulation
	public void deleteObj() {
		Object.Destroy(SelectionController.Instance.getSelected().gameObject);							// Destroy the selected object
		SelectionController.Instance.setSelected(SelectionController.Instance.getSelected(), false);	// Set selected to null
	}

	#endregion

	#region Util Methods

	// Getter for isShowing
	public bool GetShowing() {
		return isShowing;
	}

	#endregion
}
