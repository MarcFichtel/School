using UnityEngine;

// Top level class for everything user-related (mostly sets up the controls)
public class User : MonoBehaviour {

	private UserControl UC;             // User control
	private ObjectCreationMenu OCM;     // Object creation menu
	private ObjectManipulationMenu OMM; // Object manipulation menu
	private SelectionController SC;     // Object selection controller
	private SaveMenu SM;                // Save menu
	private ExitMenu EM;				// Exit menu

	// Use this for initialization
	void Start () {

		// Init object creation menu
		OCM = new ObjectCreationMenu(GameObject.Find("Canvas: Create New Object").GetComponent<Canvas>());

		// Init object manipulation menu
		OMM = new ObjectManipulationMenu(GameObject.Find("Canvas: Object Manipulation").GetComponent<Canvas>());

		// Init selection controller
		SC = SelectionController.Instance;
		SC.setUser(gameObject);
		SC.setOMM(OMM);

		// Init save menu
		SM = new SaveMenu(GameObject.Find("Canvas: Saving").GetComponent<Canvas>());

		// Init exit menu
		EM = new ExitMenu(GameObject.Find("Canvas: Exit").GetComponent<Canvas>());

		// Init user control
		UC = new UserControl(gameObject, OCM, OMM, SM, EM, SC, GameObject.Find("Canvas: Mode"));
		UC.SetYawAndPitch(gameObject.transform.rotation);
	}
	
	// Update is called once per frame
	void Update () {

		// Handle user control
		UC.handleUserControl();
	}
}
