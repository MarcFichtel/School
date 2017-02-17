using System;

namespace Frontend2.Hardware {

    /**
    * Events emanating from a coin slot.
    */
    public interface ICoinSlot : IHardware {
        /**
        * An event announcing that the indicated, valid coin has been inserted and
        * successfully delivered to the storage device connected to the indicated
        * coin slot.
        * 
        */
        event EventHandler<CoinEventArgs> CoinAccepted;

        /**
        * An event announcing that the indicated coin has been rejected and, hence,
        * returned.
        * 
        */
        event EventHandler<CoinEventArgs> CoinRejected;
    }
}