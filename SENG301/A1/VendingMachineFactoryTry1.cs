using System;
using System.Collections;
using System.Collections.Generic;

using Frontend1;

namespace seng301_asgn1 {
    /// <summary>
    /// Represents the concrete virtual vending machine factory that you will implement.
    /// This implements the IVendingMachineFactory interface, and so all the functions
    /// are already stubbed out for you.
    /// 
    /// Your task will be to replace the TODO statements with actual code.
    /// 
    /// Pay particular attention to extractFromDeliveryChute and unloadVendingMachine:
    /// 
    /// 1. These are different: extractFromDeliveryChute means that you take out the stuff
    /// that has already been dispensed by the machine (e.g. pops, money) -- sometimes
    /// nothing will be dispensed yet; unloadVendingMachine is when you (virtually) open
    /// the thing up, and extract all of the stuff -- the money we've made, the money that's
    /// left over, and the unsold pops.
    /// 
    /// 2. Their return signatures are very particular. You need to adhere to this return
    /// signature to enable good integration with the other piece of code (remember:
    /// this was written by your boss). Right now, they return "empty" things, which is
    /// something you will ultimately need to modify.
    /// 
    /// 3. Each of these return signatures returns typed collections. For a quick primer
    /// on typed collections: https://www.youtube.com/watch?v=WtpoaacjLtI -- if it does not
    /// make sense, you can look up "Generic Collection" tutorials for C#.
    /// </summary>
    public class VendingMachineFactoryTry1 : IVendingMachineFactory {

        // List of Vending Machines
        private static List<VendingMachineFactoryTry1> VMs;

        private List<int> coinKinds;        // List of coin kinds
        private int selectionButtonCount;   // Number of select buttons
        private List<string> popNames;      // List of pop names
        private List<int> popCosts;         // List of pop costs
        private List<Coin> coinsLoaded;     // List of loaded (change) coins
        private List<Coin> coinsGiven;      // List of given (profit) coins
        private List<Pop> popsLoaded;       // List of loaded pops

        public VendingMachineFactoryTry1() {
            // ???
        }

        public int createVendingMachine(List<int> coinKinds, int selectionButtonCount) {

            // Validate selectionButtonCount
            if (selectionButtonCount <= 0) {
                throw new Exception("ERROR: Number of select buttons too low.");
            }

            // Validate coinkinds
            for (int i = 0; i < coinKinds.Count; i++) {

                // Check if any coin kind is <= zero
                if (coinKinds[i] <= 0) {
                    throw new Exception("ERROR: Coin value too low.");
                }

                // Check if every coin kind is unique
                for (int j = i + 1; j < coinKinds.Count; j++) {
                    if (coinKinds[i] == coinKinds[j]) {
                        throw new Exception("ERROR: Coin kinds not unique.");
                    }
                }
            }

            // Assign given values
            this.coinKinds = coinKinds;
            this.selectionButtonCount = selectionButtonCount;

            // Add new VM
            VMs.Add(this);

            // Return Vending Machine index
            return VMs.Count - 1;
        }

        public void configureVendingMachine(int vmIndex, List<string> popNames, List<int> popCosts) {

            // Validate popCosts
            for (int i = 0; i < popCosts.Count; i++) {
                if (popCosts[i] <= 0) {
                    throw new Exception("ERROR: Pop cost value too low.");
                }
            }

            // Validate # of pop costs and names
            if (popCosts.Count != popNames.Count) {
                throw new Exception("ERROR: Numbers of pop names and costs do not match.");
            }

            //Assign given values
            VMs[vmIndex].popNames = popNames;
            VMs[vmIndex].popCosts = popCosts;
        }

        public void loadCoins(int vmIndex, int coinKindIndex, List<Coin> coins) {

            // Validate coins (each must have same value)
            for (int i = 1; i < coins.Count; i++) {
                if (coins[0].Value != coins[i].Value) {
                    throw new Exception("ERROR: Loaded coins have varying values.");
                }
            }

            // Validate coin kinds (the correct chute must be specified for this coin kind)
            if (coinKinds[coinKindIndex] != coins[0].Value) {
                throw new Exception("ERROR: Coin kind does not match coin chute.");
            }

            //Assign given values
            VMs[vmIndex].coinsLoaded = coins;

        }

        public void loadPops(int vmIndex, int popKindIndex, List<Pop> pops) {

        }

        public void insertCoin(int vmIndex, Coin coin) {

        }

        public void pressButton(int vmIndex, int value) {

        }

        public List<Deliverable> extractFromDeliveryChute(int vmIndex) {

            return new List<Deliverable>();
        }

        public List<IList> unloadVendingMachine(int vmIndex) {

            return new List<IList>() {
                new List<Coin>(),
                new List<Coin>(),
                new List<Pop>() };
            }
    }
}