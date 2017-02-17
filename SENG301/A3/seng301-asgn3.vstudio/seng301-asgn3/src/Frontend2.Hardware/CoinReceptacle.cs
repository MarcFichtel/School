using System;
using System.Collections.Generic;
using System.Linq;

namespace Frontend2.Hardware {

    /**
    * A temporary storage device for coins. A coin receptacle can be disabled to
    * prevent more coins from being placed within it. A coin receptacle has a
    * maximum capacity of coins that can be stored within it. A coin receptacle can
    * be connected to specialized channels depending on the denomination of each
    * coin (usually used for storing to coin racks) and another for the coin
    * return.
    */
    public class CoinReceptacle : AbstractHardware, ICoinReceptacle, ICoinAcceptor {

        public int Capacity { get; protected set; }
        public int Count {
            get {
                return this.coinsEntered.Count;
            }
        }
        public bool HasSpace { 
            get {
                return (this.Count < this.Capacity);
            }
        }

        private List<Coin> coinsEntered;
        private Dictionary<int, CoinChannel> coinRacks;
        private CoinChannel coinReturnChannel, otherChannel;

        public event EventHandler<CoinEventArgs> CoinAdded;
        public event EventHandler CoinsRemoved;
        public event EventHandler ReceptacleFull;
        public event EventHandler<CoinEventArgs> CoinsLoaded;
        public event EventHandler<CoinEventArgs> CoinsUnloaded;


        /**
        * Creates a coin receptacle with the indicated capacity.
        * 
        * @param capacity
        *            The maximum number of coins that can be stored. Must be
        *            positive.
        */
        public CoinReceptacle(int capacity) {
            if (capacity <= 0) {
                throw new Exception("Capacity must be positive: " + capacity);
            }
            this.Capacity = capacity;
            this.coinsEntered = new List<Coin>();
            this.coinRacks = new Dictionary<int, CoinChannel>();
        }

        /**
        * Connects the output channels for use by this receptacle. Causes no
        * events.
        * 
        * @param rackChannels
        *            One channel is expected for each valid denomination.
        * @param coinReturn
        *            This is used when coins are to be returned to the user.
        * @param other
        *            This is another channel that can be used to discard coins; it
        *            can be the same as the coin return channel.
        */
        public void Connect(Dictionary<int, CoinChannel> rackChannels, CoinChannel coinReturn, CoinChannel other) {
            this.coinRacks.Clear();
            this.coinRacks = rackChannels; // hmm
            this.coinReturnChannel = coinReturn;
            this.otherChannel = other;
        }

        /**
        * Loads the indicated coins into the receptacle, to simulate direct,
        * physical loading. Causes a "CoinsLoaded" event to be announced.
        * 
        * @param coins
        *            A sequence of coins to be added. None can be null.
        */
        public void LoadCoins(List<Coin> coins) {
            if (this.Capacity < this.Count + coins.Count) {
                throw new Exception("Capacity exceeded by attempt to load");
            }
            this.coinsEntered.AddRange(coins);

            if (this.CoinsLoaded != null) {
                this.CoinsLoaded(this, new CoinEventArgs() { Coins = coins });
            }
        }

        /**
        * Unloads coins from the receptacle, to simulate direct, physical
        * unloading. Causes a "CoinsUnloaded" event to be announced.
        * 
        * @return A list of coins unloaded. None will be null. The list can be
        *         empty.
        */
        public List<Coin> Unload() {
            var result = new List<Coin>(this.coinsEntered);
            this.coinsEntered.Clear();

            if (this.CoinsUnloaded != null) {
                this.CoinsUnloaded(this, new CoinEventArgs() { Coins = result });
            }

            return result;
        }

        /**
        * Causes the indicated coin to be added to the receptacle if it has space.
        * A successful addition causes a "CoinAdded" event to be announced to its
        * listeners. If a successful addition causes the receptacle to become full,
        * it will also announce a "ReceptacleFull" event to its listeners.
        */
        public void AcceptCoin(Coin coin) {
            if (! this.Enabled) {
                throw new Exception("Coin receptacle is disabled.");
            }
            if (this.Count >= this.Capacity) {
                throw new Exception("Capacity exceeded");
            }

            this.coinsEntered.Add(coin);
            
            if (this.CoinAdded != null) {
                this.CoinAdded(this, new CoinEventArgs() { Coin = coin });
            }

            if (this.Count >= this.Capacity) {
                if (this.ReceptacleFull != null) {
                    this.ReceptacleFull(this, new EventArgs());
                }
            }
        }

        /**
        * Causes the receptacle to attempt to move its coins to the coin racks. Any
        * coins that either do not fit in the coin racks or for which no coin rack
        * exists are delivered to the "other" channel, which might be another
        * permanent storage receptacle, a coin return, etc. A successful storage
        * will cause a "CoinsRemoved" event to be announced to its listeners.
        * 
        */
        public void StoreCoins() {
            if (! this.Enabled) {
                new Exception("Coin receptacle is disabled");
            }

            foreach (var coin in this.coinsEntered) {
                var coinValue = coin.Value;
                var coinChannel = this.coinRacks.ContainsKey(coinValue) ? this.coinRacks[coinValue] : null;
                if (coinChannel != null && coinChannel.HasSpace()) {
                    coinChannel.Deliver(coin);
                }
                else if (this.otherChannel != null && this.otherChannel.HasSpace()) {
                    this.otherChannel.Deliver(coin);
                }
                else {
                    throw new Exception("Cannot route coin, as all channels are full");
                }
            }

            if (this.coinsEntered.Count > 0) {
                this.coinsEntered.Clear();
                if (this.CoinsRemoved != null) {
                    this.CoinsRemoved(this, new EventArgs());
                }
            }
        }

        /**
        * Instructs this coin receptacle to return all of its coins to the user. 
        */
        public void ReturnCoins() {
            if (! this.Enabled) {
                throw new Exception("Coinreceptacle disabled");
            }

            foreach (var coin in this.coinsEntered) {
                this.coinReturnChannel.Deliver(coin);
            }

            if (this.coinsEntered.Count > 0) {
                this.coinsEntered.Clear();
                if (this.CoinsRemoved != null) {
                    this.CoinsRemoved(this, new EventArgs());
                }
            }
        }
    }
}