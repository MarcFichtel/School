using System;

namespace Frontend2.Hardware {

    /**
    * Events emanating from a coin receptacle.
    */
    public interface ICoinReceptacle : IHardware {
        /**
        * An event that announces that the indicated coin has been added to the
        * indicated receptacle.
        * 
        */
        event EventHandler<CoinEventArgs> CoinAdded;

        /**
        * An event that announces that all coins have been removed from the
        * indicated receptacle.
        * 
        */
        event EventHandler CoinsRemoved;

        /**
        * An event that announces that the indicated receptacle is now full.
        * 
        */
        event EventHandler ReceptacleFull;

        /**
        * Announces that the indicated sequence of coins has been added to the
        * indicated coin receptacle. Used to simulate direct, physical loading of
        * the receptacle.
        * 
        */
        event EventHandler<CoinEventArgs> CoinsLoaded;

        /**
        * Announces that the indicated sequence of coins has been removed to the
        * indicated coin receptacle. Used to simulate direct, physical unloading of
        * the receptacle.
        * 
        */
        event EventHandler<CoinEventArgs> CoinsUnloaded;
    }
}