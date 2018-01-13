using UnityEngine;
using TMPro;

// Reference: https://www.assetstore.unity3d.com/en/#!/content/27024

[System.Serializable]
public class DayNightController : MonoBehaviour {
	public float daySpeedMultiplier = 0.1f;					// Speed of the cycle (if you set this to 1 the one hour in the cycle will pass in 1 real life second)
	public Light sunLight;									// Main directional light
	public bool controlIntensity = true;					// Control intensity of sun?
	public float startTime = 12.0f;							// What time this cycle should start
	float currentTime = 0.0f;								// What's the current time
	public string timeString = "00:00 AM"; 
	private float xValueOfSun = 90.0f;						// X rotation value of the light
	[SerializeField]	public Transform[] cloudSpheres;	// Clouds
	public float cloudRotationSpeed = 1.0f;					// Rotation speed of clouds
	[SerializeField]	public Transform[] starSpheres;		// Rotation speed of spheres
	public float twinkleFrequency = 5.0f;					// Twinkle frequency of the stars
	private float twinkleCounter = 0.0f;					// Background counter for twikle effect
	public float starRotationSpeed = 0.15f;					// Star's rotation speed
	public Camera cameraToFollow;                           // Camera to follow

	public Animator SkyColorAnimator;                       // Animator controlling the sky background color
	public Animator CloudAlphaAnimator;                     // Animator controlling the cloud's alpha value
	private TextMeshProUGUI TimeText;						// Daytime text

	// Use this for initialization
	void Start () {
		TimeText = GameObject.Find("Text: Time").GetComponent<TextMeshProUGUI>();
		currentTime = startTime;                            // Set the start time
		SkyColorAnimator =									// Get sky color animator
			Camera.main.transform.GetChild(0).GetComponent<Animator>();
		CloudAlphaAnimator = 
			transform.GetChild(0).GetComponent<Animator>(); // Get cloud alpha animator
	}
	
	// Update is called once per frame
	void Update () {
		currentTime += Time.deltaTime*daySpeedMultiplier;	// Increment time
		if (currentTime >= 24.0f) {                         // Reset time
			currentTime %= 24.0f;
		}
		if (sunLight) {                                     // Check for sunlight
			ControlLight();
		}
		if (cloudSpheres.Length > 0) {                      // Check for cloudsphere
			ControlClouds();
		}
		if (starSpheres.Length > 0) {                       // Check for starsphere
			StarSphere();
		}
		ControlCamera ();                                   // Camera control
		CalculateTime ();                                   // Gets The timeString;
	}

	void ControlLight() {
		// Rotate light
		xValueOfSun = -(90.0f+currentTime*15.0f);
		sunLight.transform.eulerAngles = sunLight.transform.right*xValueOfSun;
		// Reset angle
		if (xValueOfSun >= 360.0f) {
			xValueOfSun = 0.0f;
		}
		// This basically turn on and off the sun light based on day / night
		if (controlIntensity && sunLight && (currentTime >= 18.0f || currentTime <= 5.5f)) {
			sunLight.intensity = Mathf.MoveTowards(sunLight.intensity,0.0f,Time.deltaTime*daySpeedMultiplier*10.0f);
		} else if (controlIntensity && sunLight) {
			sunLight.intensity = Mathf.MoveTowards(sunLight.intensity,1.0f,Time.deltaTime*daySpeedMultiplier*10.0f);
		}

	}

	void ControlClouds (){
		// Rotate clouds
		foreach (Transform cloud in cloudSpheres) {
			if (cloud){
				cloud.transform.Rotate(Vector3.forward*cloudRotationSpeed*daySpeedMultiplier*Time.deltaTime);
			}
		}
	}

	void StarSphere() {
		// Get the color of the stars
		Color currentColor;
		// Rotate and eneble and disable stars
		foreach (Transform stars in starSpheres) {
			if (stars){
				stars.transform.Rotate(Vector3.forward*starRotationSpeed*daySpeedMultiplier*Time.deltaTime);
				if (currentTime > 5.5f && currentTime < 18.0f && stars.GetComponent<Renderer>()){
					currentColor = stars.GetComponent<Renderer>().material.color;
					stars.GetComponent<Renderer>().material.color = new Color (currentColor.r,currentColor.g,currentColor.b,Mathf.Lerp(currentColor.a , 0.0f,Time.deltaTime*50.0f*daySpeedMultiplier));
				}
			
			}

		}
		// Choose in between range
		int chosenOne = Random.Range (0, starSpheres.Length);

		// Twinkle effect
		if (starSpheres [chosenOne] && twinkleCounter <= 0.0f && (currentTime >= 18.0f || currentTime <= 5.5f) && starSpheres [chosenOne].GetComponent<Renderer>()) {
			twinkleCounter = 1.0f;
			currentColor = starSpheres [chosenOne].GetComponent<Renderer>().material.color;
			starSpheres [chosenOne].GetComponent<Renderer>().material.color = new Color (currentColor.r,currentColor.g,currentColor.b,Random.Range(0.01f,0.5f));
		}
		// Reset counter
		if (twinkleCounter > 0.0f) {
			twinkleCounter -= Time.deltaTime*daySpeedMultiplier*twinkleFrequency;
		}
	}

	void ControlCamera () {
		// Get camera
		if (!cameraToFollow) {
			cameraToFollow = Camera.main;
			return;
		}
		// Set position to the camera
		if (cameraToFollow) {
			transform.position = cameraToFollow.transform.position;
		}
	}
	void CalculateTime (){
		// Is it am of pm?
		string AMPM = "";
		float minutes = ((currentTime) - (Mathf.Floor(currentTime)))*60.0f;
		Camera cam = cameraToFollow.transform.GetChild(0).GetComponent<Camera>();
		Color skyColor = cam.backgroundColor;
		if (currentTime <= 12.0f) {
			AMPM = "AM";
		} else {
			AMPM = "PM";

		}
		// Change sky color & cloud alpha when sun rises or sets
		SkyColorAnimator.speed = daySpeedMultiplier;                // Adjust animation speed to daySpeedMultiplier
		CloudAlphaAnimator.speed = daySpeedMultiplier;              // Adjust animation speed to daySpeedMultiplier
		if (currentTime >= 6.0f && currentTime < 7.0f) {			// At sunrise
			SkyColorAnimator.SetBool("isDay", true);                // Trigger animation from night to day sky color
			CloudAlphaAnimator.SetBool("isDay", true);              // Trigger animation from night to day cloud alpha
		} else if (currentTime >= 17.0f && currentTime < 18.0f) {	// At sunset
			SkyColorAnimator.SetBool("isDay", false);               // Trigger animation from day to night sky color
			CloudAlphaAnimator.SetBool("isDay", false);             // Trigger animation from day to night cloud alpha
		}
		// Make the final string
		string hoursString, minutesString;
		if (Mathf.Floor(currentTime) < 10) {
			hoursString = "0" + Mathf.Floor(currentTime).ToString();
		} else {
			hoursString = Mathf.Floor(currentTime).ToString();
		}
		if (minutes < 10) {
			minutesString = "0" + minutes.ToString("F0");
		} else {
			minutesString = minutes.ToString("F0");
		}
		timeString = hoursString + " : " + minutesString + " " + AMPM ;
		TimeText.text = timeString;
	}

	// Button handlers for time control buttons
	public void Stop() {
		daySpeedMultiplier = 0;
	}
	public void Slow() {
		daySpeedMultiplier = 0.1f;
	}
	public void Medium() {
		daySpeedMultiplier = 0.5f;
	}
	public void Fast() {
		daySpeedMultiplier = 1.0f;
	}
}
