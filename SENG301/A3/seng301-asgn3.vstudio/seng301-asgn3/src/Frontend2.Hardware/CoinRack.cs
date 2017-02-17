using System;
using System.Collections.Generic;
using System.Linq;

namespace Frontend2.Hardware {
    /**
    * Represents a device that stores coins of a particular denomination to
    * dispense them as change.
    * 
    * Coin racks can receive coins from other sources. To simplify the simulation,
    * no check is performed on the value of each coin, meaning it is an external
    * responsibility to ensure the correct routing of coins.
    */
    public class CoinRack : AbstractHardware, ICoinRack, ICoinAcceptor {

        public int Capacity { get; protected set; }
        public int Count { 
            get {
                return this.queue.Count;
            }
         }
        public bool Full { 
            get { 
                return (this.Capacity == this.Count);
            }
        }
        public bool HasSpace {
            get {
                return !this.Full;
            }
        }

        public event EventHandler CoinRackFull;
        public event EventHandler CoinRackEmpty;
        public event EventHandler<CoinEventArgs> CoinAdded;
        public event EventHandler<CoinEventArgs> CoinRemoved;
        public event EventHandler<CoinEventArgs> CoinsLoaded;
        public event EventHandler<CoinEventArgs> CoinsUnloaded;

        private CoinChannel sink;
        private Queue<Coin> queue;

        /**
        * Creates a coin rack with the indicated maximum capacity.
        * 
        * @param capacity
        *            The maximum number of coins that can be stored in the rack.
        *            Must be positive.
        */
        public CoinRack(int capacity) {
            this.queue = new Queue<Coin>();
            if (capacity <= 0) {
                throw new Exception("Capacity must be positive");
            }
            this.Capacity = capacity;
        }

        /**
        * Allows a set of coins to be loaded into the rack directly. Existing coins
        * in the rack are not removed. Causes a "CoinsLoaded" event to be
        * announced.
        * 
        * @param coins
        *            A sequence of coins to be added. Each cannot be null.
        */
        public void LoadCoins(List<Coin> coins) {
            if (this.Capacity < this.Count + coins.Count) {
                throw new Exception("Capacity of rack is exceeded by load");
            }
            foreach(var coin in coins) {
                this.queue.Enqueue(coin);
            }

            if (this.CoinsLoaded != null) {
                this.CoinsLoaded(this, new CoinEventArgs() { Coins = coins });
            }
        }

        /**
        * Unloads coins from the rack directly. Causes a "CoinsUnloaded" event to
        * be announced.
        * 
        * @return A list of the coins unloaded. May be empty. Will never be null.
        */
        public List<Coin> Unload() {
            var result = new List<Coin>(this.queue);
            this.queue.Clear();

            if (this.CoinsUnloaded != null) {
                this.CoinsUnloaded(this, new CoinEventArgs() { Coins = result });
            }

            return result;
        }

        /**
        * Connects an output channel to this coin rack. Any existing output
        * channels are disconnected. Causes no events to be announced.
        * 
        * @param sink
        *            The new output device to act as output. Can be null, which
        *            leaves the channel without an output.
        */
        public void Connect(CoinChannel sink) {
            this.sink = sink;
        }

        /**
        * Causes the indicated coin to be added into the rack. If successful, a
        * "CoinAdded" event is announced to its listeners. If a successful coin
        * addition causes the rack to become full, a "CoinRackFull" event is announced
        * to its listeners.
        */
        public void AcceptCoin(Coin coin) {
            if (!this.Enabled) {
                throw new Exception("CoinRack is disabled");
            }
            if (this.queue.Count > this.Capacity) {
                throw new Exception("Capacity of coinrack has been exceeded.");
            }

            this.queue.Enqueue(coin);
            
            if (this.CoinAdded != null) {
                this.CoinAdded(this, new CoinEventArgs() { Coin = coin });
            }

            if (this.queue.Count >= this.Capacity) {
                if (this.CoinRackFull != null) {
                    this.CoinRackFull(this, new EventArgs());
                }
            }
        }

        /**
        * Releases a single coin from this coin rack. If successful, a
        * "CoinRemoved" event is announced to its listeners. If a successful coin
        * removal causes the rack to become empty, a "CoinRackEmpty" event is
        * announced to its listeners.
        * 
        */
        public void ReleaseCoin() {
            if (!this.Enabled) {
                throw new Exception("Coinrack disabled");
            }
            if (this.queue.Count == 0) {
                throw new Exception("Coinrack empty");
            }
            var coin = queue.Dequeue();
            if (this.CoinRemoved != null) {
                this.CoinRemoved(this, new CoinEventArgs() { Coin = coin });
            }
            this.sink.Deliver(coin);

            if (this.queue.Count == 0) {
                if (this.CoinRackEmpty != null) {
                    this.CoinRackEmpty(this, new EventArgs());
                }
            }
        }
    }
}