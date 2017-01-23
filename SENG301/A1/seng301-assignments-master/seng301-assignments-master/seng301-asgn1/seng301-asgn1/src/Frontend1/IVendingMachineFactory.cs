using System.Collections;
using System.Collections.Generic;

namespace Frontend1 {

    /// <summary>
    /// The interface for VendingMachineFactory implementations. Having this interface
    /// specified means that we can plug-and-play different VendingMachineFactory
    /// implementations while "consumers"/"clients"/"components that use 
    /// VendingMachineFactory" will continue to work without re-implementat
    /// (i.e. so long as we follow the function signatures).
    /// 
    /// For instance, we could make several (hint hint!), instantiating them as
    /// appropriate in ScriptProcessor.
    /// 
    ///   * Iteration 1: DummyVendingMachineFactory
    ///     - this one "implements" all the functions, but doesn't do anything --
    ///       all it does is print to console the inputs that it received, and
    ///       what it is expected to do.
    ///     - in ScriptProcessor.cs, you'd change the line to:
    /// 
    ///       var scriptParser = new ScriptProcessor(script, new DummyVendingMachineFactory());
    /// 
    ///   * Iteration 2: VendingMachineFactoryTry1
    ///     - this one implements some or all of the functions, but may be does
    ///       so in a really "hacky" way. For instance, maybe it does not maintain
    ///       a whole set of vending machines, and only creates/works with one for now.
    ///     - in ScriptProcessor.cs, you'd change the line to:
    /// 
    ///       var scriptParser = new ScriptProcessor(script, new VendingMachineFactoryTry1());
    /// 
    ///   * Iteration 3: VendingMachineFactoryEfficient
    ///     - now we've done it once, we can do it more efficiently (say by
    ///       using less memory, or by doing sorts more efficiently)
    ///     - in ScriptProcessor.cs, you'd change the line to:
    /// 
    ///       var scriptParser = new ScriptProcessor(script, new VendingMachineFactoryEfficient());
    /// 
    /// </summary>
    public interface IVendingMachineFactory {

        /// <summary>
        /// Creates a vending machine with a list of the kinds of coins (based on value)
        /// that the vending machine will accept, and the number of buttons (to select
        /// pops) the machine will have. This function returns an index that refers to the
        /// specific vending machine that has been created.
        /// 
        /// Note that this implies that we can maintain several vending machines at once
        /// (once they are created), as subsequent functions all use an index to indicate
        /// which vending machine is being acted on.
        /// 
        /// This should throw an Exception if the coinKinds are not unique, or one of them
        /// is negative or zero.
        /// </summary>
        /// <param name="coinKinds">List of integers that represent the coin values 
        ///   accepted by the vending machine.</param>
        /// <param name="selectionButtonCount">Number of pop selection buttons the vending
        ///   machine has.</param>
        /// <returns>An index referring to this specific vending machine.</returns>
        int createVendingMachine(List<int> coinKinds, int selectionButtonCount);

        /// <summary>
        /// Configures the specified vending machine with a set of names of pops, as well
        /// as how much each pop costs.
        /// 
        /// This should throw an Exception if any of the popCosts are zero or negative.
        /// This should throw an Exception if the number of popNames and popCosts are
        /// different.
        /// </summary>
        /// <param name="vmIndex">An index referring to a vending machine.</param>
        /// <param name="popNames">List of strings that represents the names of pops
        ///   sold by this vending machine.</param>
        /// <param name="popCosts">List of integers that represent the cost for each
        ///   of the pops.</param>
        void configureVendingMachine(int vmIndex, List<string> popNames, List<int> popCosts);

        /// <summary>
        /// Loads coins into the specified vending machine into the specified coin chute. 
        /// 
        /// HINT: This signature implies that your vending machine has several return change
        /// coin chutes (e.g. one for each coin type); however, it also implies that it is
        /// possible to load the wrong coin into a particular coin chute.
        /// </summary>
        /// <param name="vmIndex">An index referring to a vending machine.</param>
        /// <param name="coinKindIndex">An index referring to a specific coin chute in 
        ///   the vending machine</param>
        /// <param name="coins">A list of coins to load into the coin chute</param>
        void loadCoins(int vmIndex, int coinKindIndex, List<Coin> coins);

        /// <summary>
        /// Loads pops into the specified vending machine into the specified pop chute. 
        /// 
        /// HINT: This signature implies that your vending machine has several pop
        /// chutes (e.g. one for each pop type); however, it also implies that it is
        /// possible to load the wrong pop into a particular pop chute.
        /// </summary>
        /// <param name="vmIndex">An index referring to a vending machine.</param>
        /// <param name="popKindIndex">An index referring to a specific pop chute in
        ///   the vending machine</param>
        /// <param name="pops">A list of pops to load into the pop chute</param>
        void loadPops(int vmIndex, int popKindIndex, List<Pop> pops);

        /// <summary>
        /// Inserts a coin into the specified vending machine.
        /// 
        /// If the coin is not one of the accepted coin types, then it is dispensed
        /// immediately into the delivery chute.
        /// </summary>
        /// <param name="vmIndex">An index referring to a vending machine.</param>
        /// <param name="coin">The coin that will be inserted into the vending 
        ///   machine</param>
        void insertCoin(int vmIndex, Coin coin);

        /// <summary>
        /// Presses a button on the specified vending machine.
        /// 
        /// If there is enough money for the pop, it should be dispensed. If change is
        /// required, then change should be dispensed (where it favours the vending
        /// machine if there isn't enough change). If there isn't enough money for the
        /// pop, nothing happens.
        /// 
        /// This should throw an Exception if the value of the button is negative or
        /// higher than the number of buttons on the vending machine.
        /// </summary>
        /// <param name="vmIndex">An index referring to a vending machine.</param>
        /// <param name="value">An index of the button to be pressed (0-indexed)</param>
        void pressButton(int vmIndex, int value);

        /// <summary>
        /// Takes everything out of the vending machine's dispenser hopper, including 
        /// coins (either change or coin types that we don't accept) and pops dispensed
        /// (i.e. emptying the hopper).
        /// 
        /// The return type is a List of these things in whatever order is convenient.
        /// Note: this is a flat list of pops and coins (not a list of lists).
        /// </summary>
        /// <param name="vmIndex">An index referring to a vending machine.</param>
        /// <returns>A flat list of pops and coins that have been dispensed</returns>
        List<Deliverable> extractFromDeliveryChute(int vmIndex);

        /// <summary>
        /// Unloads a vending machine of all its contents: money that is still in the
        /// change maker, money that we have made, and all the unsold pops.
        /// 
        /// The return type is a three-tuple list of lists of the money still in the 
        /// change maker, the money that we have made (i.e. from dispensed pops), and
        /// the unsold pops.
        /// </summary>
        /// <param name="vmIndex">An index referring to a vending machine.</param>
        /// <returns>A three-tuple list of strongly-typed lists that represents the
        ///   remaining money in the change maker, the money we have made from dispensed
        ///   pops, and unsold pops. Each of these three things is its own List in that
        ///   order.</returns>
        List<IList> unloadVendingMachine(int vmIndex);

    }
}