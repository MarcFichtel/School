using Frontend4;

namespace Frontend4.Hardware {
    /**
    * A simple interface to allow a device to communicate with another device that
    * accepts coins.
    */
    public interface ICoinAcceptor {
        /**
        * Instructs the device to take the coin as input.
        * 
        * @param coin
        *            The coin to be taken as input.
        */
        void AcceptCoin(Coin coin);// throws CapacityExceededException, DisabledException;

        /**
        * Checks whether the device has enough space to expect one more item. If
        * this method returns true, an immediate call to acceptCoin should not
        * throw CapacityExceededException, unless an asynchronous addition has
        * occurred in the meantime.
        * 
        * @return true if there is space, false if there is not space
        */
        bool HasSpace { get; }
        int Capacity { get; }
        int Count { get; }
    }

}