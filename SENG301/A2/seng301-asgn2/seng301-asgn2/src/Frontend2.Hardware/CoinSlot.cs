using System;

namespace Frontend2.Hardware {

    /**
    * Represents a simple coin slot device that has two output channels, one to a
    * storage container and one to a coin return device. A coin slot detects the
    * presence of invalid coins and returns them. A coin slot can be disabled to
    * prevent the insertion of coins (temporarily).
    */
    public class CoinSlot : AbstractHardware, ICoinSlot {

        private int[] validValues;
        private CoinChannel valid, invalid;

        public event EventHandler<CoinEventArgs> CoinAccepted;
        public event EventHandler<CoinEventArgs> CoinRejected;

        /**
        * Creates a coin slot that recognizes coins of the specified values.
        * 
        * @param validValues
        *            An array of the valid coin values to accept.
        */
        public CoinSlot(int[] validValues) {
            this.validValues = validValues;
        }

        /**
        * Connects output channels to the coin slot. Causes no events.
        * 
        * @param valid
        *            Where valid coins to be stored should be passed.
        * @param invalid
        *            Where invalid coins (or coins that exceed capacity) should be
        *            passed.
        */
        public void Connect(CoinChannel valid, CoinChannel invalid) {
            this.valid = valid;
            this.invalid = invalid;
        }

        private bool isValid(Coin coin) {
            foreach (var validValue in this.validValues) {
                if (coin.Value == validValue) {
                    return true;
                }
            }
            return false;
        }

        /**
        * Tells the coin slot that the indicated coin is being inserted. If the
        * coin is valid and there is space in the machine to store it, a
        * "CoinAccepted" event is announced to its listeners and the coin is
        * delivered to the storage device. If there is no space in the machine to
        * store it or the coin is invalid, a "CoinRejected" event is announced to
        * its listeners and the coin is returned.
        * 
        * @param coin
        *            The coin to be added. Cannot be null.
        */
        public void AddCoin(Coin coin) {
            if (!this.Enabled) {
                throw new Exception("Coinslot is disabled");
            }
            if (this.isValid(coin) && this.valid.HasSpace()) {
                this.valid.Deliver(coin);

                if (this.CoinAccepted != null) {
                    this.CoinAccepted(this, new CoinEventArgs() { Coin = coin });
                }            
            }
            else if (this.invalid.HasSpace()) {
                this.invalid.Deliver(coin);

                if (this.CoinRejected != null) {
                    this.CoinRejected(this, new CoinEventArgs() { Coin = coin });
                }
            }
            else {
                throw new Exception("Unable to route coin: All channels full");
            }
        }
    }
}