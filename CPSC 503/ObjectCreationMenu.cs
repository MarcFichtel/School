using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

// Class represents the object creation menu
public class ObjectCreationMenu {

	#region Variables

	private Transform top;				// Top part of meu	
	private Transform mid;				// Middle part of menu
	private Transform bot;              // Bottom part of menu
	private Transform staticParent;		// Object to parent all static objects
	private Transform lightParent;      // Object to parent all light objects
	private Transform livingParent;		// Object to parent all living objects

	private Button closeMenuButton;     // Button for closing the OCM
	private Button createStaticButton;	// Button for creating static objects
	private Button createLightButton;	// Button for creating light objects
	private Button createLivingButton;  // Button for creating alive objects
	private Button createObjectButton;  // Button for creating a chosen object
	private Button objectButtonPrefab;  // Prefab of button to show available objects with

	private GameObject content;         // Content of scrollview for available objects
	private GameObject selectedObject;  // Seleted object to be created
	private GameObject preview;			// Preview of the object to be created

	private Vector3 buttonStartPos =
		new Vector3(162, -10, 0);       // Start position of these buttons within content of scrollview

	private bool showingTop = false;	// Is top being displayed?
	private bool showingMid = false;	// Is mid being displayed?
	private bool showingBot = false;    // Is bot being displayed?

	private int contentWidth = 320;     // Width of content

	private float camDistance = 10.0f;  // Distance at which object will be created in front of main camera

	private string selectedCategory;    // Selected object category
	
	#endregion

	#region Constructor

	// Constructor
	public ObjectCreationMenu(Canvas createObjectMenuCanvas) {

		// Get menu parts
		top = createObjectMenuCanvas.transform.GetChild(0);						// Get top
		mid = createObjectMenuCanvas.transform.GetChild(1);						// Get mid
		bot = createObjectMenuCanvas.transform.GetChild(2);                     // Get bot

		// Get & set up close menu button
		closeMenuButton = top.GetChild(2).GetComponent<Button>();               // Get button
		closeMenuButton.onClick.AddListener(closeMenu);                         // Assign handler

		// Get & set up button for creating static objects
		createStaticButton = top.GetChild(3).GetChild(0).GetComponent<Button>();// Get button
		createStaticButton.onClick.AddListener(showMid);                        // Assign handler

		// Get & set up button for creating light objects
		createLightButton = top.GetChild(3).GetChild(1).GetComponent<Button>(); // Get button
		createLightButton.onClick.AddListener(showMid);                         // Assign handler

		// Get & set up button for creating living objects
		createLivingButton = top.GetChild(3).GetChild(2).GetComponent<Button>(); // Get button
		createLivingButton.onClick.AddListener(showMid);                         // Assign handler

		// Get the scroll view content object
		content = mid.GetChild(1).GetChild(0).GetChild(0).gameObject;			// Scrollview content

		// Get & set up button for creating selected objects
		createObjectButton = bot.GetChild(1).GetComponent<Button>();            // Get button
		createObjectButton.onClick.AddListener(createObject);                   // Assign handler

		// Get the prefab for objectButtonPrefab
		GameObject temp = Resources.Load("Create Object Button") as GameObject; // Get game object
		objectButtonPrefab = temp.GetComponent<Button>();                       // Get button component

		// Get object category parents
		GameObject objects = GameObject.Find("Objects");						// Find object containing parent objects
		staticParent = objects.transform.GetChild(0);							// Get static parent object
		lightParent = objects.transform.GetChild(1);							// Get light parent object
		livingParent = objects.transform.GetChild(2);                           // Get living parent object
	}
	
	#endregion

	#region Toggle OCM

	// Toggle top of OBJ creation menu with N key
	public void toggleOBJCreationMenu() {
		if (!showingTop) {										// If top is hidden
			GameController.Instance.setCreatingObject(true);	// Inform game controller
			shiftMenuPart(top, showingTop);						// Display it
			showingTop = true;									// Set showingTop to true
			clearContent();										// Clear content
		} else {												// Else top is displayed
			closeMenu();										// So hide it and any other part of OCM that is displayed
			GameController.Instance.setCreatingObject(false);   // Inform game controller
		}
	}
	
	#endregion

	#region Shift menu part

	// Shift a menu part on or off the screen
	public void shiftMenuPart(Transform part, bool showingPart) {
		Animator animator = part.gameObject.GetComponent<Animator>();	// Get part's animator component
		if (!showingPart) {												// If part is hidden
			animator.SetBool("slideOn", true);							// Display it
		} else {														// Else part is displayed so
			animator.SetBool("slideOn", false);							// Hide it
		}
	}
	
	#endregion

	#region Button handlers

	// Button handler - Close OCM
	public void closeMenu() {
		shiftMenuPart(top, showingTop);					// Hide top
		showingTop = false;								// Set showingTop to false
		if (showingMid) {								// If mid is displayed
			shiftMenuPart(mid, showingMid);				// Hide it
			showingMid = false;							// Set showingMid to false
		}
		if (showingBot) {								// If bot is displayed
			shiftMenuPart(bot, showingBot);				// Hide it
			showingBot = false;							// Set showingBot to false
		}
		selectedCategory = null;						// Clear selected category
		EventSystem.current.SetSelectedGameObject(null);// Unselect any button that may be selected

		// Delete any preview that may currently be shown
		if (preview != null) {
			Object.DestroyObject(preview);
		}
	}

	// Button handler - Show Mid
	public void showMid() {

		// Get clicked button
		Button selected = EventSystem.current.currentSelectedGameObject.GetComponent<Button>();
		if (!showingMid) {                          // If mid is currently hidden
			shiftMenuPart(mid, showingMid);         // Display it
			showingMid = true;                      // Set showingMid to true
		}

		// Fill scrollview with objects from chosen category
		if (selected == createStaticButton &&       // If user selected static objects...
			selectedCategory != "Static") {         // and the button wasn't pressed before
			getAndButtonizeObjects("Static");       // Get objects for selected category
		} else if (selected == createLightButton && // Else if user selected light objects...
			selectedCategory != "Light") {          // and the button wasn't pressed before
			getAndButtonizeObjects("Light");        // Get objects for selected category
		} else if (selected == createLivingButton &&// Else if user selected living objects...
			selectedCategory != "Living") {         // and the button wasn't pressed before
			getAndButtonizeObjects("Living");       // Get objects for selected category
		}
	}

	// Button handler - Set selected game object
	public void setSelectedObject() {

		// Delete any preview that may currently be shown
		if (preview != null) {
			Object.DestroyObject(preview);
		}

		// Get name of selected object prefab
		string obj = EventSystem.current.currentSelectedGameObject.GetComponentInChildren<Text>().text;

		// Get selected object prefab
		selectedObject = Resources.Load(selectedCategory + "/" + obj) as GameObject;   

		// Show bottom part of OCM
		if (!showingBot) {                                                              // If bot is hidden
			shiftMenuPart(bot, showingBot);                                             // Display bot
			showingBot = true;                                                          // Set showingBot to true
		}

		// Show preview of selected object
		// TEMP. WORKAROUND FOR FIRE OBJECT: If its a light object, adjust rotation
		if (selectedCategory == "Light") {
			preview = Object.Instantiate(selectedObject,
			Camera.main.ViewportToWorldPoint(new Vector3(0.5f, 0.5f, camDistance)),
			Quaternion.Euler(270f, 0f, 0f)) as GameObject;

			// Else its a static object
		} else {
			preview = Object.Instantiate(selectedObject,
			Camera.main.ViewportToWorldPoint(new Vector3(0.5f, 0.5f, camDistance)),
			Quaternion.identity) as GameObject;
		}

		// Remove "(Clone)" from end of new object name
		preview.name = preview.name.Substring(0, preview.name.Length - 7);

		// Make object a child of the appropriate category object, and select it
		if (selectedCategory == "Static") {
			preview.transform.SetParent(staticParent);
		} else if (selectedCategory == "Light") {
			preview.transform.SetParent(lightParent);
		} else if (selectedCategory == "Living") {
			preview.transform.SetParent(livingParent);
		}
	}

	// Button handler - Instantiate selected object
	public void createObject() {

		// Reset preview, thus not destroying the instantiated object
		preview = null;

		// Close OCM
		closeMenu();
	}

	#endregion

	#region Scrollview stuff

	// Clear content in scrollview
	public void clearContent() {
		foreach(Transform child in content.transform) {	// For every child in content
			Object.Destroy(child.gameObject);			// Destroy that game object
		}
		content.GetComponent<RectTransform>().sizeDelta =
			new Vector2(0, 0);							// Reset content size
	}

	// Populate scrollview for chosen selected object category
	public void getAndButtonizeObjects(string path) {
		clearContent();                                                 // Clear previous contents
		selectedCategory = path;                                        // Set selected category
		Object[] objects = Resources.LoadAll(path);                     // Get objects for chosen category
		content.GetComponent<RectTransform>().sizeDelta = 
			new Vector2(contentWidth, 20 * objects.Length);				// Adjust size of content based on number of objects
		for (int i = 0; i < objects.Length; i++) {                      // Iterate over these objects
			Button b = Object.Instantiate(objectButtonPrefab,           // Instantiate button from prefab
							   Vector3.zero,							// At some location
							   Quaternion.identity);                    // With no rotation
			b.transform.SetParent(content.transform);                   // And make it a child of the scrollview's content
			b.transform.localPosition = new Vector3(                    // Place object at proper position
											buttonStartPos.x,			// X
											buttonStartPos.y - 20 * i,	// Y (adjusted for each button)
											buttonStartPos.z);			// Z
			b.onClick.AddListener(setSelectedObject);					// Add preview object handler to button
			b.GetComponentInChildren<Text>().text = objects[i].name;	// Change button text to current object's name
		}
	}

	#endregion

	#region Util Functions

	// Getter for showingTop
	public bool isShowingTop() {
		return showingTop;
	}

	#endregion
}
