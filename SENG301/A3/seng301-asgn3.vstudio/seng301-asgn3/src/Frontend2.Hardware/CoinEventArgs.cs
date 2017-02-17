using System;
using System.Collections.Generic;

namespace Frontend2.Hardware {

    /// <summary>
    /// A set of coins. Some events fire with the Coin property set; others fire with the
    /// Coins property set.
    /// </summary>
    public class CoinEventArgs : EventArgs {
        public Coin Coin { get; set; }
        public List<Coin> Coins { get; set; }
    }
}