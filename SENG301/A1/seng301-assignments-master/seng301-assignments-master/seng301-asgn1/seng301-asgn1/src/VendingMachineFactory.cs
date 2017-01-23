using System;
using System.Collections;
using System.Collections.Generic;

using Frontend1;
using seng301_asgn1.src.Frontend1;

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
    public class VendingMachineFactory : IVendingMachineFactory {

        private int activeMachines;         // Number of active vending machines
        private List<VendingMachine> VMs;   // List of active vending machines

        // Vending Machine Factory constructor initializes global variables
        public VendingMachineFactory() {
            activeMachines = 0;
            VMs = new List<VendingMachine>();
        }

        // Create Vending Machine
        public int createVendingMachine(List<int> coinKinds, int selectionButtonCount) {
            
            // Validate number of selection buttons
            if (selectionButtonCount <= 0) 
            {
                throw new Exception("ERROR: Number of selection buttons is invalid.");
            }

            // Validate coin kinds
            for (int i = 0; i < coinKinds.Count; i++)
            {
                // Check if any coin kind balue is <= zero
                if (coinKinds[i] <= 0) 
                {
                    throw new Exception("ERROR: Coin kind invalid.");
                }

                // Check if all coin kinds are unique
                for (int j = coinKinds.Count - 1; j > i; j--)
                {
                    if (coinKinds[i] == coinKinds[j]) 
                    {
                        throw new Exception("ERROR: Coin kinds are not unique.");
                    }
                }
            }

            // Add new vending machine
            VMs.Add(new VendingMachine(coinKinds, selectionButtonCount));

            // Increment vmindex
            activeMachines++;

            // Returns the index of the created vending machine
            return activeMachines - 1;
        }

        // Configure Vending Machine
        public void configureVendingMachine(int vmIndex, List<string> popNames, List<int> popCosts) {
            
            // Validate pop costs
            foreach (int i in popCosts)
            {
                if (i <= 0)
                    throw new Exception("ERROR: Pop cost is <= zero.");
            }

            // Validate equal number of names and costs
            if (popNames.Count != popCosts.Count)
                throw new Exception("ERROR: Number of pop names does not equal number of pop costs.");

            // Configure the indicated machine with the pop names and costs
            VMs[vmIndex].Configure(popNames, popCosts);
        }

        // Load coins into Vending Machine
        public void loadCoins(int vmIndex, int coinKindIndex, List<Coin> coins) {

            // Load given coins into the given coin chute of this machine
            VMs[vmIndex].LoadCoins(coinKindIndex, coins);
        }

        // Load pops into Vending Machine
        public void loadPops(int vmIndex, int popKindIndex, List<Pop> pops) {

            // Load given pops into the given pop chute of this machine
            VMs[vmIndex].LoadPops(popKindIndex, pops);
        }

        // Insert coin into Vending Machine
        public void insertCoin(int vmIndex, Coin coin) {
            VMs[vmIndex].InsertCoin(coin);
        }

        // Press a button on the Vending Machine
        public void pressButton(int vmIndex, int value) {
            
            // Validate given value
            if (value < 0 ||
                value > VMs[vmIndex].GetSelectButtons())
            {
                throw new Exception("ERROR: Select button value is invalid.");
            }

            // Press the indicated button on the vending machine
            VMs[vmIndex].PressButton(value);

        }

        // Empty the Vending Machine's delivery chute
        public List<Deliverable> extractFromDeliveryChute(int vmIndex) {
            return VMs[vmIndex].EmptyDeliveryChute();
        }

        // Unload Vending Machine
        public List<IList> unloadVendingMachine(int vmIndex) {
            return VMs[vmIndex].UnloadMachine();
        }
    }
}