using UnityEngine;

/* 
 * Abstract class for any editable object
 * Extended by static, light, and living editable object classes
*/ 
public abstract class EditableObject : MonoBehaviour {

	#region Variables

	private float changeSizeSpeed = 0.1f;               // Speed with which object size is changed
	private float changeRotationSpeed = 0.1f;           // Speed with which object rotation is changed
	private float changeLocationSpeed = 0.1f;           // Speed with which object location is changed
	private bool highlighted = false;                   // Is an object currently highlighted?
	private bool selected = false;                      // Is an object currently selected?
	private Material[] mats;                            // The object's current materials
	public Material outlinedMat;                        // The object outlined material
	private Color highlightedColorStatic = Color.yellow;// Outline color of highlighted static objects
	private Color highlightedColorLight = Color.blue;   // Outline color of highlighted light objects
	private Color highlightedColorLiving = Color.green; // Outline color of highlighted living objects
	private Color selectedColor = Color.red;            // Outline color of selected objects

	#endregion

	#region Size stuff

	// Public methods for changing object size
	public void increaseSize() {
		changeSize(true);
	}
	public void decreaseSize() {
		changeSize(false);
	}

	// Either increase or decrease an object's size
	private void changeSize(bool inc) {
		Vector3 size = transform.localScale;    // Get object's current size
		if (inc) {                              // If size if increased
			size.x += changeSizeSpeed;              // Increase size on x-axis 
			size.y += changeSizeSpeed;              // Increase size on y-axis
			size.z += changeSizeSpeed;              // Increase size on z-axis
		} else {                                // Else size is decreased
			size.x -= changeSizeSpeed;              // Decrease size on x-axis
			size.y -= changeSizeSpeed;              // Decrease size on y-axis
			size.z -= changeSizeSpeed;              // Decrease size on z-axis
		}
		transform.localScale = size;            // Apply new size
	}

	#endregion

	#region Rotation stuff

	// Public methods for changing object rotation
	public void increaseRotationX() {
		changeRotation(0, true);
	}
	public void decreaseRotationX() {
		changeRotation(0, false);
	}
	public void increaseRotationY() {
		changeRotation(1, true);
	}
	public void decreaseRotationY() {
		changeRotation(1, false);
	}
	public void increaseRotationZ() {
		changeRotation(2, true);
	}
	public void decreaseRotationZ() {
		changeRotation(2, false);
	}

	// Either increase or decrease an object's rotation on some axis
	private void changeRotation(int axis, bool inc) {
		Quaternion rotation = transform.localRotation;              // Get object's current rotation
		if (inc) {                                                  // If rotation if increased
			if (axis == 0) rotation.x += changeRotationSpeed;  // Increase rotation on x-axis, or 
			else if (axis == 1) rotation.y += changeRotationSpeed;  // Increase rotation on y-axis, or
			else rotation.z += changeRotationSpeed;  // Increase rotation on z-axis
		} else {                                                    // Else rotation is decreased
			if (axis == 0) rotation.x -= changeRotationSpeed;  // Decrease rotation on x-axis
			else if (axis == 1) rotation.y -= changeRotationSpeed;  // Decrease rotation on y-axis
			else rotation.z -= changeRotationSpeed;  // Decrease rotation on z-axis
		}
		transform.localRotation = rotation;                         // Apply new rotation
	}

	#endregion

	#region Location stuff

	// Public methods for changing object location
	public void increaseLocationX() {
		changeLocation(0, true);
	}
	public void decreaseLocationX() {
		changeLocation(0, false);
	}
	public void increaseLocationY() {
		changeLocation(1, true);
	}
	public void decreaseLocationY() {
		changeLocation(1, false);
	}
	public void increaseLocationZ() {
		changeLocation(2, true);
	}
	public void decreaseLocationZ() {
		changeLocation(2, false);
	}

	// Either increase or decrease an object's location on some axis
	private void changeLocation(int axis, bool inc) {
		Vector3 location = transform.localPosition;                 // Get object's current location
		if (inc) {                                                  // If location if increased
			if (axis == 0) location.x += changeLocationSpeed;  // Increase location on x-axis, or 
			else if (axis == 1) location.y += changeLocationSpeed;  // Increase location on y-axis, or
			else location.z += changeLocationSpeed;  // Increase location on z-axis
		} else {                                                    // Else location is decreased
			if (axis == 0) location.x -= changeLocationSpeed;  // Decrease location on x-axis
			else if (axis == 1) location.y -= changeLocationSpeed;  // Decrease location on y-axis
			else location.z -= changeLocationSpeed;  // Decrease location on z-axis
		}
		transform.localPosition = location;                         // Apply new location
	}

	#endregion

	#region Highlighting stuff

	// Getter & Setter for selection mgmt
	public bool isHighlighted() {
		return highlighted;
	}
	public void setHighlighted(bool value) {
		highlighted = value;		// Update highlighted bool
		if (value) {				// If obj was highlighted
			highlightObject();		// Update its material
		} else {                    // Else
			unhighlightObject();    // Unhighlight the object
		}
	}
	public bool isSelected() {
		return selected;
	}
	public void setSelected(bool value) {
		selected = value;			// Update selected
		if (value) {				// If object was selected, update highlighted object's outline color
			GetComponent<Renderer>().material.SetColor("_OutlineColor", selectedColor);
		} else {                    // Else restore original materials to object
			unselectObject();      
		}
	}

	// Highlight editable object on hover
	public void OnMouseEnter() {    // When hovering over an object 

		// If OCM is not open, and object isn't already selected, and the distance between the user and this object isn't to great, and user is in edit mode
		if (!GameController.Instance.isCreatingObject() && 
			!selected &&            
				Vector3.Distance(SelectionController.Instance.getUser().transform.position, transform.position) <= SelectionController.Instance.getHighlightingDistance() &&
				GameController.Instance.isEditModeOn()) {

			// Highlight it
			setHighlighted(true);	
		}
	}

	// Stop highlighting an editable object after hover
	public void OnMouseExit() {					// When not hovering over an object anymore
		if (!selected &&						// The object isn't selected, and
			Vector3.Distance(					// The distance between the user and this object isn't to great 
				SelectionController.Instance.getUser().transform.position, transform.position) <= SelectionController.Instance.getHighlightingDistance()) {
			setHighlighted(false);				// Unhighlight the object
		}
	}

	// Get outline color depending of object type
	public Color getColor(string type) {
		if (GetType().ToString() == "StaticEditableObject") {       // If object type is static
			return highlightedColorStatic;                          // Return static object outline color
		} else if (GetType().ToString() == "LightEditableObject") { // Else if object type is light source
			return highlightedColorLight;                           // Return light object outline color
		} else {                                                    // Else object type is living
			return highlightedColorLiving;                          // Return living object outline color
		}
	}

	// Highlight this object by applying the outlinedMat material to it
	public void highlightObject() {

		// OnMouseEnter often fires multiple times, so make sure to not overwrite original mats
		if (GetComponent<Renderer>().materials[0].name != "Outlined_Material (Instance)") {
			mats = GetComponent<Renderer>().materials;          // Get its current materials
		}

		// Only do work, if original materials were read
		if (mats != null) {
			Material[] newMats = new Material[mats.Length];     // Make a new array of materials
			for (int i = 0; i < mats.Length; i++) {             // Iterate over materials arrays
				newMats[i] = new Material(outlinedMat);         // Add an outline material for each material in mats
				newMats[i].SetColor("_OutlineColor",
					getColor(GetType().ToString()));            // Set outline color depending on object type
				if (newMats[i].HasProperty("color"))			// If material has a color
					newMats[i].color = mats[i].color;           // Preserve it
			}
			GetComponent<Renderer>().materials = newMats;       // Apply new materials to object
			SelectionController.Instance.setHighlighted(this);  // Set highlighted object in selection controller
		}
	}

	// Unhighlight this object by reapplying original materials to it
	public void unhighlightObject() {
		if (mats != null) {                                 // TODO mats is occasionally null, not sure why. Guessing its when the user moves the mouse over the object too fast
			GetComponent<Renderer>().materials = mats;      // Apply original materials to object
		}
		SelectionController.Instance.setHighlighted(null);  // Remove highlighted object from SC
	}

	// Unselect this object by reapplying original materials to it
	public void unselectObject() {
		GetComponent<Renderer>().materials = mats; 
	}

	#endregion
}
