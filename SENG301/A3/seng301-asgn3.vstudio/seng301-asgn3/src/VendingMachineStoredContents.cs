using System.Collections.Generic;

namespace Frontend2 {

    /// <summary>
    /// Data class used to record contents of vending machine when it is unloaded.
    /// </summary>
    public class VendingMachineStoredContents {
        public VendingMachineStoredContents() {
            this.CoinsInCoinRacks = new List<List<Coin>>();
            this.PaymentCoinsInStorageBin = new List<Coin>();
            this.PopCansInPopCanRacks = new List<List<PopCan>>();
        }

        /// <summary>
        /// This list records separate lists of Coins for each coin rack, in the same
        /// order as they are in the vending machine.
        /// </summary>
        /// <returns></returns>
        public List<List<Coin>> CoinsInCoinRacks { get; protected set; }

        /// <summary>
        ///  This list records the coins that were used as payment that had to be///
        ///  stored in the storage bin.
        /// </summary>
        /// <returns></returns>
        public List<Coin> PaymentCoinsInStorageBin { get; protected set; }

        /// <summary>
        /// This list records separate lists of PopCans for each pop can rack
        /// in the same order they are in the vending machine.
        /// </summary>
        /// <returns></returns>
        public List<List<PopCan>> PopCansInPopCanRacks { get; protected set; }
    }
}