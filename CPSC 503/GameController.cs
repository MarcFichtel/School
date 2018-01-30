using System.Collections;
using System.IO;
using UnityEngine;

#if UNITY_EDITOR
using UnityEditor;
#endif

// Convenience class used for saving, loading, and a bunch of stuff
public class GameController : MonoBehaviour {

	#region Variables

	private static string path = "";
	private Transform staticObjects;
	private Transform lightObjects;
	private Transform livingObjects;
	private bool editModeOn = true;
	private bool creatingObject = false;

	#endregion

	#region Singleton stuff

	// The GameController is a singleton
	private static GameController _instance;
	public static GameController Instance {
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

	#region Load File

	// After empty simulation scene was loaded, load objects
	public void OnLevelWasLoaded(int level) {
		if (level == 2) {

			// Get object category parents
			Transform objects = GameObject.Find("Objects").GetComponent<Transform>();
			staticObjects = objects.GetChild(0).GetComponent<Transform>();
			lightObjects = objects.GetChild(1).GetComponent<Transform>();
			livingObjects = objects.GetChild(2).GetComponent<Transform>();

			// Read file
			StreamReader reader = new StreamReader(path, true);

			while (!reader.EndOfStream) {
				string line = reader.ReadLine();

				// Parse and instantiate objects
				if (line.Contains("Object")) {

					// Get object name
					string[] parts = line.Split(':');
					string objectName = parts[1].Trim();
					string objectCategory = parts[2].Trim();

					// Load object from resources
					Object loadObject = Resources.Load(objectCategory + "/" + objectName);

					// Parse object position
					line = reader.ReadLine();
					parts = line.Split(' ');
					float posX = float.Parse(parts[parts.Length - 3]);
					float posY = float.Parse(parts[parts.Length - 2]);
					float posZ = float.Parse(parts[parts.Length - 1]);
					Vector3 pos = new Vector3(posX, posY, posZ);

					// Parse object rotation
					line = reader.ReadLine();
					parts = line.Split(' ');
					float rotX = float.Parse(parts[parts.Length - 3]);
					float rotY = float.Parse(parts[parts.Length - 2]);
					float rotZ = float.Parse(parts[parts.Length - 1]);
					Quaternion rot = new Quaternion(rotX, rotY, rotZ, 0);

					// Parse object scale
					line = reader.ReadLine();
					parts = line.Split(' ');
					float sclX = float.Parse(parts[parts.Length - 3]);
					float sclY = float.Parse(parts[parts.Length - 2]);
					float sclZ = float.Parse(parts[parts.Length - 1]);
					Vector3 scl = new Vector3(sclX, sclY, sclZ);

					// Get parent depending on category
					Transform parent;
					if (objectCategory == "Static") {
						parent = staticObjects;
					} else if (objectCategory == "Light") {
						parent = lightObjects;
					} else {
						parent = livingObjects;
					}

					// Instantiate object & update scale
					GameObject obj = Instantiate(loadObject, pos, rot, parent) as GameObject;
					obj.transform.localScale = scl;

					// Remove "(clone)" from object name
					obj.name = obj.name.Substring(0, obj.name.Length - 7);
				}

				// Parse and update user position
				else if (line.Contains("User position")) {
					string[] parts = line.Split(' ');
					float posX = float.Parse(parts[parts.Length - 3]);
					float posY = float.Parse(parts[parts.Length - 2]);
					float posZ = float.Parse(parts[parts.Length - 1]);
					Vector3 pos = new Vector3(posX, posY, posZ);
					GameObject.Find("User").GetComponent<Transform>().position = pos;
				}

				// Parse and update user rotation
				else if (line.Contains("User rotation")) {
					string[] parts = line.Split(' ');
					float rotX = float.Parse(parts[parts.Length - 3]);
					float rotY = float.Parse(parts[parts.Length - 2]);
					float rotZ = float.Parse(parts[parts.Length - 1]);
					GameObject.Find("User").GetComponent<Transform>().eulerAngles = new Vector3(rotX, rotY, rotZ);
				}
			}

			// Close reader
			reader.Close();
		}
	}

	#endregion

	#region Save File

	// Save the current simulation to a textfile
	// Reference: https://support.unity3d.com/hc/en-us/articles/115000341143-How-do-I-read-and-write-data-from-a-text-file-
	public void saveToFile(string filename, bool saveUser) {

		// If filename is empty, do nothing
		if (filename == "") {
			return;
		}

		// Set path
		SetLoadFile(filename + ".txt");

		// (Over)Write the file
		StreamWriter writer = new StreamWriter(path, false);

		// Save user position & rotation if indicated
		if (saveUser) {
			string userPosition =
				Camera.main.transform.position.x.ToString() + " " +
				Camera.main.transform.position.y.ToString() + " " +
				Camera.main.transform.position.z.ToString();
			string userRotation =
				Camera.main.transform.rotation.eulerAngles.x + " " +
				Camera.main.transform.rotation.eulerAngles.y + " " +
				Camera.main.transform.rotation.eulerAngles.z;
			writer.WriteLine("User position: " + userPosition);
			writer.WriteLine("User rotation: " + userRotation + "\n");
		}

		// Save static objects
		GameObject parent = GameObject.Find("Static Objects");
		Transform[] objs = parent.transform.GetComponentsInChildren<Transform>();
		for (int i = 1; i < objs.Length; i++) {
			Transform obj = objs[i];
			writer.WriteLine("Object: " + obj.name + ": Static");	// Save object name and category
			writer.WriteLine(										// Save object position
				obj.position.x + " " +
				obj.position.y + " " +
				obj.position.z);
			writer.WriteLine(										// Save object rotation
				obj.rotation.x + " " +
				obj.rotation.y + " " + 
				obj.rotation.z);
			writer.WriteLine(										// Save object scale
				obj.localScale.x + " " +
				obj.localScale.y + " " +
				obj.localScale.z + "\n");
		}

		// Save light objects
		parent = GameObject.Find("Light Objects");
		objs = parent.transform.GetComponentsInChildren<Transform>();
		for (int i = 1; i < objs.Length; i++) {
			Transform obj = objs[i];
			writer.WriteLine("Object: " + obj.name + ": Light");	// Save object name and category
			writer.WriteLine(                                       // Save object position
				obj.position.x + " " +
				obj.position.y + " " +
				obj.position.z);
			writer.WriteLine(                                       // Save object rotation
				obj.rotation.x + " " +
				obj.rotation.y + " " +
				obj.rotation.z);
			writer.WriteLine(                                       // Save object scale
				obj.localScale.x + " " +
				obj.localScale.y + " " +
				obj.localScale.z + "\n");
		}

		// Save living objects
		parent = GameObject.Find("Living Objects");
		objs = parent.transform.GetComponentsInChildren<Transform>();
		for (int i = 1; i < objs.Length; i++) {
			Transform obj = objs[i];
			writer.WriteLine("Object: " + obj.name + ": Living");	// Save object name and category
			writer.WriteLine(                                       // Save object position
				obj.position.x + " " +
				obj.position.y + " " +
				obj.position.z);
			writer.WriteLine(                                       // Save object rotation
				obj.rotation.x + " " +
				obj.rotation.y + " " +
				obj.rotation.z);
			writer.WriteLine(                                       // Save object scale
				obj.localScale.x + " " +
				obj.localScale.y + " " +
				obj.localScale.z + "\n");
		}

		// Done writing to file
		writer.Close();

		// Take a screenshot to display in the load menu if user saved position
		if (saveUser) StartCoroutine(TakeScreenshot(filename));

		// Import save file so load menu displays it
		#if UNITY_EDITOR
		AssetDatabase.ImportAsset("Assets/Resources/Savefiles/" + filename + ".txt");
		#endif
	}

	#endregion

	#region Util Functions

	// Set load file
	public void SetLoadFile (string file) {
		path = "Assets/Resources/Savefiles/" + file;
	}

	// Take a screenshot
	public IEnumerator TakeScreenshot(string filename) {

		// Wait a bit, then take a scrrenshot
		yield return new WaitForSeconds(1);
		ScreenCapture.CaptureScreenshot("Assets/Resources/Savefiles/" + filename + ".png");

		// Wait a bit, then import taken scrrenshot (so it can be used as preview in load menu)
		#if UNITY_EDITOR
		yield return new WaitForSeconds(1);
		AssetDatabase.ImportAsset("Assets/Resources/Savefiles/" + filename + ".png");
		#endif
	}

	// editModeOn Getter & Setter
	public bool isEditModeOn() { return editModeOn;	}
	public void setEditModeOn(bool val) { editModeOn = val; }

	// creatingObject Getter & Setter
	public bool isCreatingObject() { return creatingObject; }
	public void setCreatingObject(bool val) { creatingObject = val; }

	#endregion
}
