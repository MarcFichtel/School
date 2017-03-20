using System;

namespace Frontend4.Hardware {
    /**
    * Events emanating from a coin rack.
    */
    public interface ICoinRack : IHardware {
        /**
        * Announces that the indicated coin rack is full of coins.
        *
        */
        event EventHandler CoinRackFull;

        /**
        * Announces that the indicated coin rack is empty of coins.
        * 
        */
        event EventHandler CoinRackEmpty;

        /**
        * Announces that the indicated coin has been added to the indicated coin
        * rack.
        * 
        */
        event EventHandler<CoinEventArgs> CoinAdded;

        /**
        * Announces that the indicated coin has been added to the indicated coin
        * rack.
        * 
        */
        event EventHandler<CoinEventArgs> CoinRemoved;

        /**
        * Announces that the indicated sequence of coins has been added to the
        * indicated coin rack. Used to simulate direct, physical loading of the
        * rack.
        * 
        */
        event EventHandler<CoinEventArgs> CoinsLoaded;

        /**
        * Announces that the indicated sequence of coins has been removed to the
        * indicated coin rack. Used to simulate direct, physical unloading of the
        * rack.
        * 
        */
        event EventHandler<CoinEventArgs> CoinsUnloaded;
    }
}