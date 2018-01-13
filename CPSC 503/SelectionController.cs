using UnityEngine;

/*
 * Class controls object selection and highlighting.
 * It communicates with the ObjectMnipulationMenu class.
*/
public class SelectionController : MonoBehaviour {

	#region Variables

	private float highlightingDistance = 25.0f;		// Max distance from which an object will be highlighted 
	private EditableObject selectedObject;			// The currently selected object
	private EditableObject highlightedObject;       // The currently highlighted object
	private ObjectManipulationMenu OMM;             // Object manipulation menu
	private GameObject user;						// User (used by editable objects to get user object reference)

	#endregion

	#region Singleton stuff

	// The selection controller is a singleton
	private static SelectionController _instance;
	public static SelectionController Instance {
		get { return _instance; }
	}

	// Initialize singleton on awake
	public void Awake() {
		if (_instance != null && _instance != this) {
			Destroy(gameObject);
		} else {
			_instance = this;
		}
	}

	#endregion

	#region Object Selection Mgmt

	// Getter for selected object
	public EditableObject getSelected() {
		return selectedObject;
	}

	// Setter for selected object, does a bunch of stuff
	public void setSelected(EditableObject obj, bool wasSelected) {
		if (wasSelected) {				// If some object was selected
			obj.setSelected(true);		// Let obj know its selected
			selectedObject = obj;		// Set selected
			highlightedObject = null;   // Remove highlighted
		} else {						// Else something was unselected
			obj.setSelected(false);     // Let obj know its unselected
			selectedObject = null;      // Unselect object
		}
		OMM.toggleMenu();				// Display OMM
	}

	// Getter & Setter for highlightedObject object
	public EditableObject getHighlighted() {
		return highlightedObject;
	}
	public void setHighlighted(EditableObject obj) {
		highlightedObject = obj;

	}

	// Getter for highlighting distance
	public float getHighlightingDistance() {
		return highlightingDistance;
	}

	#endregion

	#region Util Functions

	// Getter for user
	public GameObject getUser() {
		return user;
	}

	// Setters for user and OMM
	public void setUser(GameObject user) {
		this.user = user;
	}
	public void setOMM (ObjectManipulationMenu OMM) {
		this.OMM = OMM;
	}

	#endregion
}
