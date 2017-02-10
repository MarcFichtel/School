using System;
using System.Collections.Generic;
using Frontend2;
using Frontend2.Hardware;
using System.Linq;

public class VendingMachineFactory : IVendingMachineFactory {

    private int activeMachines;                                                 // Number of active vending machines
    private List<VendingMachine> VMs;                                           // List of active vending machines

    public VendingMachineFactory() {
        activeMachines = 0;                                                     // Initialize an index activeMachines
        VMs = new List<VendingMachine>();                                       // Create new list to track active machines
    }

    public int CreateVendingMachine(List<int> coinKinds, int selectionButtonCount, int coinRackCapacity, int popRackCapcity, int receptacleCapacity) {
        activeMachines++;                                                       // Increment # active VMs
        VMs.Add(new VendingMachine(coinKinds.ToArray(), selectionButtonCount,   // Add new VM with given parameters to list of active machines
                                   coinRackCapacity, popRackCapcity, 
                                   receptacleCapacity));
        return 0;
    }

    public void ConfigureVendingMachine(int vmIndex, List<string> popNames, List<int> popCosts) {
        VMs[vmIndex].Configure(popNames, popCosts);                             // Call this VM's Configure() with the given parameters
    }

    public void LoadCoins(int vmIndex, int coinKindIndex, List<Coin> coins) {
        Dictionary<Coin, int> coinCountDict = new Dictionary<Coin, int>();      // Dictionary counts number of coins per coin type
        foreach (Coin c in coins) {                                             // Iterate over coins
            if (!coinCountDict.ContainsKey(c)) {                                // If coin type is not yet in dictionary
                coinCountDict.Add(c, 1);                                        // Track the first one
            }
            else {
                coinCountDict[c]++;                                             // Else increment the number of tracked coins
            }
        }
        List<int> coinCounts = new List<int>();                                 // Create list for coins counts
        foreach (Coin c in coins) {                                             // Iterate over coins
            coinCounts.Add(coinCountDict[c]);                                   // Add each coin type count from dictionary
        }
        VMs[vmIndex].LoadCoins(coinCounts.ToArray());                           // Call this VM's LoadCoins() with an array of the coin counts
    }

    public void LoadPops(int vmIndex, int popKindIndex, List<PopCan> pops) {
        Dictionary<PopCan, int> popCountDict = new Dictionary<PopCan, int>();   // Dictionary counts number of pops per pop type
        foreach (PopCan p in pops) {                                            // Iterate over pops
            if (!popCountDict.ContainsKey(p)) {                                 // If pop type is not yet in dictionary
                popCountDict.Add(p, 1);                                         // Track the first one
            } else {                                                            
                popCountDict[p]++;                                              // Else increment the number of tracked pops
            }
        }
        List<int> popCounts = new List<int>();                                  // Create list for pop counts
        foreach (PopCan p in pops) {                                            // Iterate over pops
            popCounts.Add(popCountDict[p]);                                     // Add each pop type count from dictionary
        }
        VMs[vmIndex].LoadPopCans(popCounts.ToArray());                          // Call this VM's LoadPopCans() with an array of the pop counts
    }

    public void InsertCoin(int vmIndex, Coin coin) {
        VMs[vmIndex].CoinSlot.AddCoin(coin);                                    // Add coin to this VM's coin slot
    }

    public void PressButton(int vmIndex, int value) {
        VMs[vmIndex].SelectionButtons[value].Press();                           // Press the given button on this VM
    }

    public List<IDeliverable> ExtractFromDeliveryChute(int vmIndex) {
        return VMs[vmIndex].DeliveryChute.RemoveItems().ToList();               // Return a list of items in this VM's delivery chute
    }

    public VendingMachineStoredContents UnloadVendingMachine(int vmIndex) {
        VendingMachineStoredContents contents =                                 // Create new stored contents object
            new VendingMachineStoredContents();
        for (int i = 0; i < VMs[vmIndex].CoinRacks.Length; i++) {               // Iterate over VM's coin racks
            contents.CoinsInCoinRacks[i] = VMs[vmIndex].CoinRacks[i].Unload();  // Add a list of each rack's coins to the object's list of lists of coins
        }
        for (int i = 0; i < VMs[vmIndex].StorageBin.Capacity; i++) {            // Iterate over coins in this VM's storage bin  
            // cannot get unloaded coins directly since the setter here is protected
            //contents.PaymentCoinsInStorageBin = VMs[vmIndex].StorageBin.Unload();  // Add a list of each rack's coins to the object's list of lists of coins
        }

        return contents;                                                        // Return the contents of this VM
    }
}