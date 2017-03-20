using System;

namespace Frontend4.Hardware {
    /**
    * Represents a simple push button on the vending machine. It ignores the
    * enabled/disabled state.
    */
    public class SelectionButton : AbstractHardware, ISelectionButton {

        public event EventHandler Pressed;

        /**
        * Simulates the pressing of the button. Notifies its listeners of a
        * "pressed" event.
        */
        public void Press() {
            if (this.Pressed != null) {
                this.Pressed(this, new EventArgs());
            }
        }
    }
}