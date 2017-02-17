using System;

namespace Frontend2.Hardware {
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

        //void messageChange(Display display, String oldMessage, String newMessage);
    }
}