using System.Collections;
using System.Collections.Generic;
using UnityEngine;

// Class represents light source objects.
// TODO Display object manipulation menu for light sources when one of these was selected
public class LightEditableObject : EditableObject {

	#region Variables

	private Light lightsource;                  // The obect's light component
	private float changeIntensitySpeed = 0.1f;  // Speed with which light intensity is changed
	private float changeRangeSpeed = 0.1f;      // Speed with which light range is changed

	#endregion

	#region Start (Initialization)

	// Use this for initialization
	void Start () {
		lightsource = GetComponent<Light>();
	}

	#endregion

	#region Light Intensity

	// Public methods for changing light intensity
	public void increaseIntensity() {
		changeIntensity(true);
	}
	public void decreaseIntensity() {
		changeIntensity(false);
	}

	// Either increase or decrease a light source's intensity
	private void changeIntensity(bool inc) {
		float intsty = lightsource.intensity;	// Get light's intensity
		if (inc) {								// If intensity if increased
			intsty += changeIntensitySpeed;		// Increase intensity
		} else {								// Else intensity is decreased
			intsty -= changeIntensitySpeed;		// Decrease intensity
		}
		lightsource.intensity = intsty;         // Apply new intensity
	}

	#endregion

	#region Light Range

	// Public methods for changing light range
	public void increaseRange() {
		changeRange(true);
	}
	public void decreaseRange() {
		changeRange(false);
	}

	// Either increase or decrease a light source's range
	private void changeRange(bool inc) {
		float rng = lightsource.range;        // Get light's range
		if (inc) {                      // If range if increased
			rng += changeRangeSpeed;	// Increase range
		} else {                        // Else range is decreased
			rng -= changeRangeSpeed;	// Decrease range
		}
		lightsource.intensity = rng;          // Apply new range
	}

	#endregion
}
