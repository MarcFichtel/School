using TMPro;
using UnityEngine;

// Class enables user to control the simulation
public class UserControl {

	#region Variables

	// The user
	private GameObject user;

	// Joystick Variables
	private KeyCode AButton = KeyCode.Joystick1Button1;
//	private KeyCode BButton = KeyCode.Joystick1Button2;
//	private KeyCode XButton = KeyCode.Joystick1Button0;
	private KeyCode YButton = KeyCode.Joystick1Button3;

	private KeyCode RBButton = KeyCode.Joystick1Button5;
	private KeyCode LBButton = KeyCode.Joystick1Button4;
//	private KeyCode RTButton = KeyCode.Joystick1Button7;
//	private KeyCode LTButton = KeyCode.Joystick1Button6;

	private KeyCode BackButton = KeyCode.Joystick1Button8;
	private KeyCode StartButton = KeyCode.Joystick1Button9;
//	private KeyCode LSButton = KeyCode.Joystick1Button10;
	private KeyCode RSButton = KeyCode.Joystick1Button11;

	// Movement & Rotation Variables
	private float speedMovement = 0.2f;				// Camera movement speed
	private float speedHorizontalRotation = 2.0f;	// Camera horizontal rotation speed
	private float speedVerticalRotation = 2.0f;		// Camera vertical rotation speed
	private float yaw = 0;							// Used for rotating camera
	private float pitch = 0;                        // Used for rotating camera
	private float roll = 0;							// Used for rotating camera

	// Menu Variables
	private ObjectCreationMenu OCM;                 // Object creation menu
	private ObjectManipulationMenu OMM;				// Object manipulation menu
	private SaveMenu SM;                            // Save menu
	private ExitMenu EM;							// Exit menu
	private SelectionController SC;                 // Object selection controller
	private bool saveMenuOpen = false;              // Is the save menu currently open?
	private bool exitMenuOpen = false;              // Is the exit menu currently open?

	// Presentation Mode Variables
	private Rigidbody rb;                           // The user's rigidbody
	private TextMeshProUGUI modeText;               // The text indicating which mode the user is in
	private bool editModeOn = true;					// Is user currently in edit mode?

	// Control Keys
	private KeyCode moveForward = KeyCode.W;
	private KeyCode moveBackward = KeyCode.S;
	private KeyCode moveLeft = KeyCode.A;
	private KeyCode moveRight = KeyCode.D;
	private KeyCode moveUp = KeyCode.Q;
	private KeyCode moveDown = KeyCode.E;
	private KeyCode createObject = KeyCode.F1;
	private KeyCode save = KeyCode.F2;
	private KeyCode quit = KeyCode.F3;
	private KeyCode switchMode = KeyCode.F4;
	private int select = 0;
	private int rotate = 1;

	#endregion

	#region Constructor

	// Constructor
	public UserControl(GameObject user, ObjectCreationMenu OCM, ObjectManipulationMenu OMM, SaveMenu SM, ExitMenu EM, SelectionController SC, GameObject modeCanvas) {
		this.user = user;
		this.OCM = OCM;
		this.OMM = OMM;
		this.SM = SM;
		this.EM = EM;
		this.SC = SC;
		modeText = modeCanvas.GetComponentInChildren<TextMeshProUGUI>();
		rb = user.GetComponent<Rigidbody>();
	}

	#endregion

	#region Handle control

	// Handle user control
	public void handleUserControl() {

		// These controls are allowed in edit mode
		if (editModeOn) {
			if (!saveMenuOpen && !exitMenuOpen) {
				handleMovement();       // Handle Movement control
				handleOpenOCM();        // Handle OCM control
				handleObjectSelect();   // Handle object selection
				handleExitMenu();       // Handle Exit menu control
				handleSaveMenu();       // Handle Save menu control
				handleMode();           // Handle Mode switching
			} else if (!saveMenuOpen) {
				handleExitMenu();       // Handle Exit menu control
			} else if (!exitMenuOpen) {
				handleSaveMenu();       // Handle Save menu control
			}
			handleMouseRotation();      // Handle Rotation control

		// These controls are allowed in presentation mode
		} else {
			if (!saveMenuOpen && !exitMenuOpen) {
				handleMovement();       // Handle Movement control
				handleExitMenu();       // Handle Exit menu control
				handleSaveMenu();       // Handle Save menu control
				handleMode();           // Handle Mode switching
			} else if (!saveMenuOpen) {
				handleExitMenu();       // Handle Exit menu control
			} else if (!exitMenuOpen) {
				handleSaveMenu();       // Handle Save menu control
			}
			handleMouseRotation();      // Handle Rotation control
		}
	}

	#endregion

	#region Handle Movement

	// Handle camera movement by updating its position via WASDQE keys
	// Reference: https://forum.unity3d.com/threads/how-can-i-change-the-transform-localposition-values.436255/
	public void handleMovement() {
		Vector3 pos = user.transform.localPosition;				// Get user's current position
		Vector3 fwd = user.transform.forward * speedMovement;	// Compute motion foward
		Vector3 rgt = user.transform.right * speedMovement;		// Compute motion to the right
		Vector3 up = user.transform.up * speedMovement;         // Compute motion upward


		// Handle input
		if (Input.GetKey(moveForward) || Input.GetAxis("Mouse ScrollWheel") > 0) {	// Move forward or backward
			pos += fwd;
		} else if (Input.GetKey(moveBackward) || Input.GetAxis("Mouse ScrollWheel") < 0) {
			pos -= fwd;							
		}
		if (Input.GetKey(moveLeft)) {												// Move left or right
			pos -= rgt;							
		} else if (Input.GetKey(moveRight)) {
			pos += rgt;							
		}
		if (Input.GetKey(moveUp) || Input.GetKey(RBButton)) {	// Move up or down (note: in presentation mode, up functions as jump, and down is disabled)
			pos += up;							
		} else if ((Input.GetKey(moveDown) || Input.GetKey(LBButton)) && editModeOn) {
			pos -= up;					
		}

		// Handle joystick input
//		pos += rgt * Input.GetAxis("LeftJoyHorizontal");
//		pos -= fwd * Input.GetAxis("LeftJoyVertical");

		user.transform.localPosition = pos;                     // Apply new position
	}

	#endregion

	#region Handle OCM

	// Handle Object Creation Menu opening
	public void handleOpenOCM() {

		// Toggle OCM
		if (Input.GetKeyDown(createObject) || Input.GetKeyDown(YButton)) {
			OCM.toggleOBJCreationMenu();

			// Toggle OMM if necessary
			if (OMM.GetShowing()) {
				OMM.cancelManip();
			}

			// Unhighlight any object when OCM is opened
			if (SC.getHighlighted() != null) {
				SC.getHighlighted().unhighlightObject();
				SC.setHighlighted(null);
			}
		}

		// Update game controller, if OCM was closed via Close Button
		if (!OCM.isShowingTop() && GameController.Instance.isCreatingObject()) {
			GameController.Instance.setCreatingObject(false);
		}
	}

	#endregion

	#region Handle Save Menu

	// Handle Save Menu opening
	public void handleSaveMenu() {

		// Update Save Menu status (because there's more than one way to close the menu)
		if (saveMenuOpen != SM.isOpen()) {
			saveMenuOpen = SM.isOpen();
		}

		// Toggle Save Menu
		if (Input.GetKeyDown(save) || Input.GetKeyDown(StartButton)) {
			SM.toggleMenu();

			// Close other menus
			if (OCM.isShowingTop()) OCM.closeMenu();
			if (OMM.GetShowing()) OMM.closeMenu();
		}
	}

	#endregion

	#region Handle Exit Menu

	// Handle Exit Menu opening
	public void handleExitMenu() {

		// Update Exit Menu status (because there's more than one way to close the menu)
		if (exitMenuOpen != EM.isOpen()) {
			exitMenuOpen = EM.isOpen();
		}

		// Toggle Exit Menu
		if (Input.GetKeyDown(quit) || Input.GetKeyDown(BackButton)) {
			EM.toggleMenu();

			// Close other menus
			if (OCM.isShowingTop()) OCM.closeMenu();
			if (OMM.GetShowing()) OMM.closeMenu();
		}
	}

	#endregion

	#region Handle Object Selection

	// Select an Object
	public void handleObjectSelect() {

		// On left click or A Button press, if...
		if ((Input.GetMouseButtonDown(select) || Input.GetKeyDown(AButton)) &&			
			SC.getHighlighted() != null &&				// An object is being highlighted, and
			SC.getSelected() == null &&					// No object is already selected, and
			Vector3.Distance(							// The distance between the user and the object isn't to great 
				user.transform.position, SC.getHighlighted().transform.position) <= SC.getHighlightingDistance()) {
			SC.setSelected(SC.getHighlighted(), true);  // Select the object
		}
	}

	#endregion

	#region Handle Rotation

	// Update camera rotation via mouse, if right mouse button is clicked
	// Reference: https://gamedev.stackexchange.com/questions/104693/how-to-use-input-getaxismouse-x-y-to-rotate-the-camera
	public void handleMouseRotation() {
		if (Input.GetMouseButton(rotate)) {                             // While right mouse button is pressed
			yaw += speedHorizontalRotation * Input.GetAxis("Mouse X");  // Compute new horizontal rotation (yaw)
			pitch -= speedVerticalRotation * Input.GetAxis("Mouse Y");  // Compute new vertical rotation (pitch)
		}

		// Handle joystick input
//		yaw += Input.GetAxis("RightJoyHorizontal");
//		pitch += Input.GetAxis("RightJoyVertical");

		user.transform.eulerAngles = new Vector3(pitch, yaw, roll);		// Apply new rotation
	}

	// Set Yaw and Pitch
	public void SetYawAndPitch(Quaternion quat) {
		yaw = quat.eulerAngles.y;
		pitch = quat.eulerAngles.x;
	}

	#endregion

	#region Handle Mode

	// Handle mode switching
	public void handleMode() {

		// Toggle mode by modifying the user's rigidbody to obey gravity or not
		if (Input.GetKeyDown(switchMode) || Input.GetKeyDown(RSButton)) {
			rb.isKinematic = !rb.isKinematic;
			editModeOn = !editModeOn;

			// Pass change on to GameController
			if (editModeOn != (GameController.Instance.isEditModeOn()))
				GameController.Instance.setEditModeOn(editModeOn);

			// Toggle text
			if (modeText.text == "Edit Mode\nF1: Create Object - F2: Save - F3: Exit - F4: Switch Mode") {
				modeText.text = "Presentation Mode\nF1: Create Object - F2: Save - F3: Exit - F4: Switch Mode";
			} else {
				modeText.text = "Edit Mode\nF1: Create Object - F2: Save - F3: Exit - F4: Switch Mode";
			}

			// Hide object creation menu, if open
			if (!editModeOn) {
				if (OCM.isShowingTop()) OCM.closeMenu(); 
				
				/* 
				 * Note: Chose not to do the same for an open object manipulation menu.
				 * ~ What should happen to changes made to an object when the user switches into presentation mode? Save them, or cancel them?
				 * ~ How should this behavior be communicated to the user? 
				 * Dealing with these questions made things more complicated than necessary, so instead the user may finish their object manipulation in presentation mode.
				*/

			}
		}
	}

	#endregion
}
