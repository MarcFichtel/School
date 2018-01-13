using System.IO;
using TMPro;
using UnityEngine;
using UnityEngine.EventSystems;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

#if UNITY_EDITOR
using UnityEditor;
#endif

// Class controls the main menu
public class MenuController : MonoBehaviour {

	#region Variables

	private Canvas main;				// Main menu canvas
	private Canvas settings;			// Settings menu canvas
	private Canvas about;               // About menu canvas
	private Canvas load;				// Load menu canvas

	private Button buttonNew;			// Start new simulation button
	private Button buttonLoad;			// Open load menu button
	private Button buttonSettings;		// Open seetings menu button
	private Button buttonAbout;			// Open about menu button
	private Button loadButtonPrefab;    // Prefab of button to show available objects with
	private Button loadFileButton;		// Load a saved file
	private Button deleteFileButton;    // Delete a saved file button
	private Button selected;            // Which load button was selected

	public Button buttonBack;           // Go back to main menu button

	private RawImage previewImage;		// Preview image of saved simulation

	private GameObject content;         // Content of scrollview for available savefiles
	private int contentWidth = 0;		// Width of content
	private Vector3 buttonStartPos =
		new Vector3(170, -18, 0);       // Start position of these buttons within content of scrollview

	#endregion

	#region Start (Initialization)

	// Use this for initialization
	void Start () {

		// Get canvases
		Transform menu = GameObject.Find("Menu").GetComponent<Transform>();	// Get canvas parent
		main = menu.GetChild(0).GetComponent<Canvas>();						// Get main menu canvas
		settings = menu.GetChild(1).GetComponent<Canvas>();					// Get settings canvas
		about = menu.GetChild(2).GetComponent<Canvas>();                    // Get about canvas
		load = menu.GetChild(3).GetComponent<Canvas>();						// Get load canvas

		// Get buttons
		Transform buttons = GameObject.Find("Buttons").GetComponent<Transform>();	// Get parent of main buttons
		buttonNew = buttons.GetChild(0).GetComponent<Button>();						// Get 'New' button
		buttonLoad = buttons.GetChild(1).GetComponent<Button>();					// Get 'Load' button
		buttonSettings = buttons.GetChild(2).GetComponent<Button>();				// Get 'Settings' button
		buttonAbout = buttons.GetChild(3).GetComponent<Button>();                   // Get 'About' button
		loadFileButton = load.transform.GetChild(2).GetComponent<Button>();			// Get 'Load File' button
		deleteFileButton = load.transform.GetChild(3).GetComponent<Button>();       // Get 'Delete File' button

		// Get image
		previewImage = load.transform.GetChild(4).GetComponent<RawImage>();

		// Get the prefab for objectButtonPrefab
		GameObject temp = Resources.Load("Load File Button") as GameObject; // Get game object
		loadButtonPrefab = temp.GetComponent<Button>();                     // Get button component

		// Get the scroll view content object
		content = menu.GetChild(3).GetChild(1).GetChild(0).GetChild(0).gameObject;	// Scrollview content

		// Assign button handlers
		buttonNew.onClick.AddListener(New);					// Add listener for 'New' button
		buttonLoad.onClick.AddListener(Load);				// Add listener for 'Load' button
		buttonSettings.onClick.AddListener(Settings);		// Add listener for 'Settings' button
		buttonAbout.onClick.AddListener(About);				// Add listener for 'About' button
		buttonBack.onClick.AddListener(Back);				// Add listener for 'Back' button
		loadFileButton.onClick.AddListener(loadSimulation);	// Add listener for 'Load File' button
		deleteFileButton.onClick.AddListener(deleteFile);	// Add listener for 'Delete File' button
	}

	#endregion

	#region Button Handlers

	// Button handler: Start new simulation
	public void New() {
		SceneManager.LoadSceneAsync(1);
	}

	// Button handler: Show load menu
	public void Load() {
		getAndDisplaySavedFiles();
		main.gameObject.SetActive(false);
		load.gameObject.SetActive(true);
		moveBackButton(load);
	}

	// Button handler: Show settings menu
	public void Settings() {
		main.gameObject.SetActive(false);
		settings.gameObject.SetActive(true);
		moveBackButton(settings);
	}

	// Button handler: Show about menu
	public void About() {
		main.gameObject.SetActive(false);
		about.gameObject.SetActive(true);
		moveBackButton(about);
	}

	// Button handler: Return to main menu
	public void Back() {
		settings.gameObject.SetActive(false);
		about.gameObject.SetActive(false);
		load.gameObject.SetActive(false);
		main.gameObject.SetActive(true);

		// If returning from save menu and buttons are on, turn them off
		if (loadFileButton.IsInteractable() || deleteFileButton.IsInteractable()) {
			loadFileButton.interactable = false;
			deleteFileButton.interactable = false;
		}

		// Reset preview image
		if (previewImage.texture != null) {
			previewImage.texture = null;
		}
	}

	// Button handler: Select a file
	public void selectFile() {
		selected = EventSystem.current.currentSelectedGameObject.GetComponentInChildren<Button>();
		loadFileButton.interactable = true;
		deleteFileButton.interactable = true;

		// Load save file preview image
		byte[] bytes = File.ReadAllBytes("Assets/Resources/Savefiles/" + selected.GetComponentInChildren<TextMeshProUGUI>().text + ".png");
		Texture2D tex = new Texture2D(453, 340);
		tex.LoadImage(bytes);
		previewImage.texture = tex;
	}

	// Button handler: Load a saved simulation
	public void loadSimulation() {
		string file = selected.GetComponentInChildren<TextMeshProUGUI>().text + ".txt";
		GameController.Instance.SetLoadFile(file);
		SceneManager.LoadScene(2);
	}

	// Button handler: Delete a saved simulation
	public void deleteFile() {
		string filename = selected.GetComponentInChildren<TextMeshProUGUI>().text;

		#if UNITY_EDITOR
		AssetDatabase.DeleteAsset("Assets/Resources/Savefiles/" + filename + ".txt");
		AssetDatabase.DeleteAsset("Assets/Resources/Savefiles/" + filename + ".png");
		#endif

		selected = null;
		previewImage.texture = null;
		loadFileButton.interactable = false;
		deleteFileButton.interactable = false;
		getAndDisplaySavedFiles();
	}

	#endregion

	#region Util Functions

	// Move back button to active canvas
	public void moveBackButton(Canvas newParent) {
		buttonBack.transform.SetParent(newParent.transform);
		buttonBack.transform.localScale = new Vector3(1,1,1);
	}

	// Get and display loadable simulation files
	public void getAndDisplaySavedFiles() {

		// Clear content
		Button[] buttons = content.GetComponentsInChildren<Button>();
		foreach (Button b in buttons) {
			DestroyObject(b.gameObject);
		}

		// Populate content
		Object[] objects = Resources.LoadAll("Savefiles");              // Load savefile objects from resources folder
		content.GetComponent<RectTransform>().sizeDelta =
			new Vector2(contentWidth, 29 * objects.Length);             // Adjust size of content based on number of objects

		// Iterate over these objects
		for (int i = 0; i < objects.Length; i++) {

			// Skip every second file (every second file is an image)
			if ((i+1) % 2 == 0) { continue; }	

			// Create buttons
			Button b = Instantiate(loadButtonPrefab,						// Instantiate button from prefab
								   Vector3.zero,							// At some location
								   Quaternion.identity);					// With no rotation
			b.transform.SetParent(content.transform);						// And make it a child of the scrollview's content
			b.transform.localPosition = new Vector3(						// Place button at proper position
											buttonStartPos.x,				// X
											buttonStartPos.y - 29 * (i/2),  // Y (adjusted for each button)
											buttonStartPos.z);				// Z
			b.transform.localScale = new Vector3(1,1,1);					// Set button scale to 1 (not sure why its not instantiated as such...)
			b.onClick.AddListener(selectFile);								// Add preview object handler to button
			b.GetComponentInChildren<TextMeshProUGUI>().text	
											= objects[i].name;				// Change button text to current object's name
		}

	}

	#endregion
}
