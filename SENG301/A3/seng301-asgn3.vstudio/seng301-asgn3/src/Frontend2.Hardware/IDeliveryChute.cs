using System;

namespace Frontend2.Hardware {
    /**
    * Events emanating from a delivery chute.
    */
    public interface IDeliveryChute : IHardware {
        /**
        * Indicates that an item has been delivered to the indicated delivery
        * chute.
        * 

        */
        event EventHandler<DeliverableEventArgs> ItemDelivered;

        /**
        * Indicates that the door of the indicated delivery chute has been opened.
        * 

        */
        event EventHandler ChuteDoorOpened;

        /**
        * Indicates that the door of the indicated delivery chute has been closed.
        * 
        */
        event EventHandler ChuteDoorClosed;

        /**
        * Indicates that the delivery chute will not be able to hold any more
        * items.
        * 
        */
        event EventHandler ChuteFull;
    }
}