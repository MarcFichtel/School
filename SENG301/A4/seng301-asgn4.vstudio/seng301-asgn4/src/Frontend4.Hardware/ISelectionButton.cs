using System;

namespace Frontend4.Hardware {
    /**
    * Events emanating from a selection button.
    */
    public interface ISelectionButton : IHardware {
        /**
        * An event that is announced to the listener when the indicated button has
        * been pressed.
        * 
        */
        event EventHandler Pressed;
    }
}