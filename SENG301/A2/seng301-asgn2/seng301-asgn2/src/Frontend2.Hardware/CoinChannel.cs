namespace Frontend2.Hardware {
        
    /**
    * Represents a simple device (like, say, a tube) that allows coins to move
    * between other devices.
    */
    public class CoinChannel {

        public ICoinAcceptor Sink { get; protected set; }

       /**
        * Constructs a new coin channel whose output is connected to the indicated
        * sink.
        * 
        * @param sink
        *            The device at the output end of the channel.
        */
        public CoinChannel(ICoinAcceptor sink) {
            this.Sink = sink;
        }

        /**
        * Moves the indicated coin to the sink. This method should be called by the
        * source device, and not by an external application.
        * 
        * @param coin
        *            The coin to transport via the channel.
        */
        public void Deliver(Coin coin) { 
            this.Sink.AcceptCoin(coin);
        }

        /**
        * Returns whether the sink has space for at least one more coin.
        * 
        * @return true if the channel can accept a coin; false otherwise.
        */
        public bool HasSpace() {
            return this.Sink.HasSpace;
        }
    }
}