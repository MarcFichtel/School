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
    public class DummyVendingMachineFactory : IVendingMachineFactory {

        public DummyVendingMachineFactory() {
            Console.WriteLine("Called Constructor");
            // TODO: Implement
        }

        public int createVendingMachine(List<int> coinKinds, int selectionButtonCount) {
            Console.WriteLine("Called createVendingMachine" +
            "\n coinKinds: " + coinKinds + 
            "\n selectionButtonCount: " + selectionButtonCount);
            return 0;
        }

        public void configureVendingMachine(int vmIndex, List<string> popNames, List<int> popCosts) {
            Console.WriteLine("Called configureVendingMachine" +
            "\n vmIndex: " + vmIndex + 
            "\n popNames: " + popNames +
            "\n popCosts: " + popCosts);
            // TODO: Implement
        }

        public void loadCoins(int vmIndex, int coinKindIndex, List<Coin> coins) {
            Console.WriteLine("Called loadCoins" +
            "\n vmIndex: " + vmIndex + 
            "\n coinKindIndex: " + coinKindIndex +
            "\n coins: " + coins);
            // TODO: Implement
        }

        public void loadPops(int vmIndex, int popKindIndex, List<Pop> pops) {
            Console.WriteLine("Called loadPops" +
            "\n vmIndex: " + vmIndex + 
            "\n popKindIndex: " + popKindIndex +
            "\n pops: " + pops);
            // TODO: Implement
        }

        public void insertCoin(int vmIndex, Coin coin) {
            Console.WriteLine("Called insertCoin" +
            "\n vmIndex: " + vmIndex + 
            "\n coin: " + coin);
            // TODO: Implement
        }

        public void pressButton(int vmIndex, int value) {
            Console.WriteLine("Called pressButton" +
            "\n vmIndex: " + vmIndex + 
            "\n value: " + value);
            // TODO: Implement
        }

        public List<Deliverable> extractFromDeliveryChute(int vmIndex) {
            Console.WriteLine("Called extractFromDeliveryChute" +
            "\n vmIndex: " + vmIndex);
            // TODO: Implement
            return new List<Deliverable>();
        }

        public List<IList> unloadVendingMachine(int vmIndex) {
            Console.WriteLine("Called unloadVendingMachine" +
            "\n vmIndex: " + vmIndex);
            // TODO: Implement
            return new List<IList>() {
                new List<Coin>(),
                new List<Coin>(),
                new List<Pop>() };
            }
    }
}