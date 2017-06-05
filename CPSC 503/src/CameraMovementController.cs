using System.Collections;
using System.Collections.Generic;
using UnityEngine;

/// <summary>
/// Class represents camera movement controller by the user.
/// </summary>
public class CameraMovementController : MonoBehaviour {

    private float speedMovement = 0.2f;
    private float speedHorizontalRotation = 2.0f;
    private float speedVerticalRotation = 2.0f;
    private float yaw = 0;
    private float pitch = 0;

    // Used for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {

		// Handle camera movement by updating its position via WASDQE keys
		// Reference: https://forum.unity3d.com/threads/how-can-i-change-the-transform-localposition-values.436255/
		if (Input.GetKey(KeyCode.W)) {
			this.transform.localPosition += transform.forward * speedMovement;	// Move forward on W
		} else if (Input.GetKey(KeyCode.S)) {
			this.transform.localPosition -= transform.forward * speedMovement;	// Move backward on S
		}
		if (Input.GetKey(KeyCode.A)) {
			this.transform.localPosition -= transform.right * speedMovement;	// Pan left on A
		} else if (Input.GetKey(KeyCode.D)) {
			this.transform.localPosition += transform.right * speedMovement;	// Pan right on D
		}
		if (Input.GetKey(KeyCode.Q)) {
			this.transform.localPosition += transform.up * speedMovement;		// Pan up on Q
		} else if (Input.GetKey(KeyCode.E)) {
			this.transform.localPosition -= transform.up * speedMovement;		// Pan down on E
		}

		// Update camera rotation via mouse rotation
		// Reference: https://gamedev.stackexchange.com/questions/104693/how-to-use-input-getaxismouse-x-y-to-rotate-the-camera
		yaw += speedHorizontalRotation * Input.GetAxis("Mouse X");
        pitch -= speedVerticalRotation * Input.GetAxis("Mouse Y");
        transform.eulerAngles = new Vector3(pitch, yaw, 0.0f);
    }
}
