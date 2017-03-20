using System;

namespace Frontend4.Hardware {
    /**
    * Events emanating from a display device.
    */
    public interface IDisplay: IHardware {

        /**
        * Event that announces that the message on the indicated display has
        * changed.
        * 
        */
        event EventHandler<MessageEventArgs> MessageChanged;
    }
}