using System;
using System.Collections.Generic;
using Frontend2;
using Frontend2.Hardware;
using System.Linq;

public class VendingMachineFactory : IVendingMachineFactory {

    private int activeMachines = 0;                                             // Number of active vending machines
    private List<VendingMachine> VMs = new List<VendingMachine>();              // List of active vending machines
    private List<int> insertedCoinsValue = new List<int>();                     // List to track inserted coins' values per machine
    private List<List<int>> validCoins = new List<List<int>>();                 // List to track valid voin kinds per machine
    private List<List<List<Coin>>> loadedCoins = new List<List<List<Coin>>>();  // List tracks loaded coins per coin type per machine

    public VendingMachineFactory() {
        activeMachines = 0;                                                     // Initialize an index activeMachines
        VMs = new List<VendingMachine>();                                       // Create new list to track active machines
    }

    ////////////////////////////////////////////////////////////
    // Create Machine
    ////////////////////////////////////////////////////////////

    public int CreateVendingMachine(List<int> coinKinds, int selectionButtonCount, int coinRackCapacity, int popRackCapcity, int receptacleCapacity) {

        // Add new VM with given parameters to list of active machines
        VMs.Add(new VendingMachine(coinKinds.ToArray(), selectionButtonCount, coinRackCapacity, popRackCapcity, receptacleCapacity));

        insertedCoinsValue.Add(0);                              // Add new value of inserted coins for the new machine
        validCoins.Add(coinKinds);                              // Track valid coin kinds
        loadedCoins.Add(new List<List<Coin>>());                // Add new list to list of loaded coins
        foreach (int val in coinKinds)                          // Iterate over this machine's coin kinds
            loadedCoins[activeMachines].Add(new List<Coin>());  // Add each coin kind to the lists of loaded coins
        

        // Add event listeners for coin slot & select buttons
        VMs[activeMachines].CoinSlot.CoinAccepted += new EventHandler<CoinEventArgs>(printSlotCoinAccepted);
        VMs[activeMachines].CoinSlot.CoinRejected += new EventHandler<CoinEventArgs>(printSlotCoinRejected);
        foreach (SelectionButton sb in VMs[activeMachines].SelectionButtons) {
            sb.Pressed += new EventHandler(printButtonPressed);
        }

        // Increment # active VMs
        activeMachines++;  

        return 0;
    }

    ////////////////////////////////////////////////////////////
    // Configure Machine
    ////////////////////////////////////////////////////////////

    public void ConfigureVendingMachine(int vmIndex, List<string> popNames, List<int> popCosts) {

        // Call this VM's Configure() with the given parameters
        VMs[vmIndex].Configure(popNames, popCosts);

        // Add event listeners for coin racks
        foreach (CoinRack cr in VMs[vmIndex].CoinRacks) {
            cr.CoinsLoaded += new EventHandler<CoinEventArgs>(printCoinsLoaded);
            cr.CoinsUnloaded += new EventHandler<CoinEventArgs>(printCoinsUnloaded);
        }

        // Add listeners for the coin receptacle
        VMs[vmIndex].CoinReceptacle.CoinAdded += new EventHandler<CoinEventArgs>(printReceptacleCoinAccepted);
        VMs[vmIndex].CoinReceptacle.CoinsRemoved += new EventHandler(printReceptacleCoinsRemoved);

        // Add event listeners for pop racks
        foreach (PopCanRack pcr in VMs[vmIndex].PopCanRacks) {
            pcr.PopCansLoaded += new EventHandler<PopCanEventArgs>(printPopsLoaded);
            pcr.PopCansUnloaded += new EventHandler<PopCanEventArgs>(printPopsUnloaded);
        }
    }

    ////////////////////////////////////////////////////////////
    // Load Coin
    ////////////////////////////////////////////////////////////

    public void LoadCoins(int vmIndex, int coinKindIndex, List<Coin> coins) {
        int coinKind = VMs[vmIndex].GetCoinKindForCoinRack(coinKindIndex);      // Get the coin kind corresponding to this index
        CoinRack rack = VMs[vmIndex].GetCoinRackForCoinKind(coinKind);          // Get the coin rack corresponding to this coin kind
        foreach (Coin c in coins) {                                             // Iterate over given coins
            rack.AcceptCoin(c);                                                 // Add each coin to the coin rack
            loadedCoins[vmIndex][coinKindIndex].Add(c);                         // Track each loaded coin for this machine
        }
    }

    ////////////////////////////////////////////////////////////
    // Load Pop
    ////////////////////////////////////////////////////////////

    public void LoadPops(int vmIndex, int popKindIndex, List<PopCan> pops) {
        PopCanRack pcr = VMs[vmIndex].PopCanRacks[popKindIndex];                // Get pop can rack for given pop kind index
        foreach (PopCan pc in pops) {                                           // Iterate over given pops
            pcr.AddPopCan(pc);                                                  // Add each pop to the specified pop rack
        }
    }

    ////////////////////////////////////////////////////////////
    // Insert Coin
    ////////////////////////////////////////////////////////////

    public void InsertCoin(int vmIndex, Coin coin) {
        CoinRack rack = VMs[vmIndex].GetCoinRackForCoinKind(coin.Value);        // Get the coin rack corresponding to this coin kind
        VMs[vmIndex].CoinSlot.AddCoin(coin);                                    // Add coin to this VM's coin slot
        insertedCoinsValue[vmIndex] += coin.Value;                              // Add coin value to insertedCoinsValue for this machine 
    }

    ////////////////////////////////////////////////////////////
    // Press Button
    ////////////////////////////////////////////////////////////

    public void PressButton(int vmIndex, int value) {
        
        VMs[vmIndex].SelectionButtons[value].Press();                           // Press the given button on this VM
        int price = VMs[vmIndex].PopCanCosts[value];                            // Get price of ordered pop
        int inserted = insertedCoinsValue[vmIndex];

        if (inserted >= price) {                                                // Proceed only if enough money has been inserted
            VMs[vmIndex].PopCanRacks[value].DispensePopCan();                   // Dispense pop
            VMs[vmIndex].CoinReceptacle.StoreCoins();                           // Store inserted coins

            if (inserted > price) {                                             // If inserted coins' value > price, dispense change
                int changeReq = inserted - price;                               // Compute required change
                List<Coin> change = GetChange(vmIndex, changeReq);              // Get change

                foreach (Coin c in change) {                                    // Iterate over every change coin
                    for (int i = 0; i < validCoins[vmIndex].Count; i++) {       // Iterate over valid coin kinds for this machine
                        if (c.Value == validCoins[vmIndex][i]) {                // If the change coin value is valid
                            VMs[vmIndex].CoinRacks[i].ReleaseCoin();            // Release one of these coins
                            loadedCoins[vmIndex][i].Remove(c);                  // Remove change coin rom loaded coins
                            break;                                              // Break out of inner for loop (repeat for next change coin)
                        }
                    }
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////
    // Extract Delivery Chute
    ////////////////////////////////////////////////////////////

    public List<IDeliverable> ExtractFromDeliveryChute(int vmIndex) {
        return VMs[vmIndex].DeliveryChute.RemoveItems().ToList();               // Return a list of items in this VM's delivery chute
    }

    ////////////////////////////////////////////////////////////
    // Unload Machine
    ////////////////////////////////////////////////////////////

    public VendingMachineStoredContents UnloadVendingMachine(int vmIndex) {

        VendingMachineStoredContents contents =                                 // Create new stored contents object with the unloaded items
            new VendingMachineStoredContents();

        for (int i = 0; i < VMs[vmIndex].CoinRacks.Length; i++) {               // Iterate over VM's coin racks
            contents.CoinsInCoinRacks.Add(VMs[vmIndex].CoinRacks[i].Unload());  // Add a list of each rack's coins to the object's list of lists of coins
        }

        List<Coin> storedCoins = VMs[vmIndex].StorageBin.Unload();              // Get List of stored coins
        foreach (Coin c in storedCoins) {                                       // Iterate over list of stored coins
            contents.PaymentCoinsInStorageBin.Add(c);                           // Add each coin to a list of unloaded storage coins
        }

        for (int i = 0; i < VMs[vmIndex].PopCanRacks.Length; i++) {             // Iterate over VM's coin racks
            contents.PopCansInPopCanRacks.Add(                                  // Add a list of each rack's coins to the object's list of lists of coins
                VMs[vmIndex].PopCanRacks[i].Unload());
        }

        return contents;                                                        // Return the contents of this VM
    }

    ////////////////////////////////////////////////////////////
    // Get Change
    ////////////////////////////////////////////////////////////
    public List<Coin> GetChange (int vmIndex, int changeRequired) {
        int changeAvailable = 0;                                                // Compute available change 
        int changeActual = 0;                                                   // Initialize actual change
        List<Coin> changeCoins = new List<Coin>();                              // List of the actual change coins

        // Dispense change, if enough is available, else dispense nothing
        foreach (List<Coin> l in loadedCoins[vmIndex]) {                        // Iterate over this machine's valid coin kinds
            foreach (Coin c in l) {                                             // Iterate over this machine's loaded coins
                changeAvailable += c.Value;                                     // Add each coin's value to changeAvailable
            }
        }

        /*
        Check if coins can be combined to the required change
        --> Add max number of greatest/lowest value coins until sum is above required change
        --> Don't include last coin, then repeat for smaller/larger value coins
        --> If combination cannot be created, repeat by starting at second highest/lowest value coin and so on
        --> If combination cannot be created after cycling through all values, do no dispense change
        */

        if (changeAvailable >= changeRequired) {                                    // Check if enough change is present
            
            // Look for matching coin combinations starting at min value
            for (int h = 0; h < loadedCoins[vmIndex].Count; h++) {                           // Cycle forward through coin kinds
                changeActual = 0;                                                   // Reset changeActual to 0
                changeCoins = new List<Coin>();                                     // Reset list of the actual change coins
                for (int i = h; i < loadedCoins[vmIndex].Count; i++) {                       // Cycle forward through remaining coin kinds
                    for (int j = 0; j < loadedCoins[vmIndex][i].Count; j++) {                // Cycle through coins
                        
                        // Add coin value to changeActual if the sum doesn't exceed changeRequired
                        if (changeActual + loadedCoins[vmIndex][i][j].Value <= changeRequired) {
                            changeActual += loadedCoins[vmIndex][i][j].Value;                // Add coin's value
                            changeCoins.Add(loadedCoins[vmIndex][i][j]);                     // Track this specific coin

                        // If this combination matches changeRequired, return these coins
                        } if (changeActual == changeRequired) break;                // If a successful match was found, break out of third for loop
                    } if (changeActual == changeRequired) break;                    // If a successful match was found, break out of second for loop
                } if (changeActual == changeRequired) break;                        // If a successful match was found, break out of first for loop
            }

            // Only do the reverse run-through if the first was unsuccessful
            if (changeActual != changeRequired) {
                
                // Look for matching coin combinations starting at max value
                for (int h = loadedCoins[vmIndex].Count - 1; h >= 0; h--) {                      // Cycle backwards through coin kinds
                    changeActual = 0;                                                   // Reset changeActual to 0
                    changeCoins = new List<Coin>();                                     // Reset list of the actual change coins
                    for (int i = h; i >= 0; i--) {                                      // Cycle backwards through remaining coin kinds
                        for (int j = 0; i < loadedCoins[vmIndex][i].Count; j++) {                // Cycle through coins
                            
                            // Add coin value to changeActual if the sum doesn't exceep changeRequired
                            if (changeActual + loadedCoins[vmIndex][i][j].Value <= changeRequired) {
                                changeActual += loadedCoins[vmIndex][i][j].Value;                // Add coin's value
                                changeCoins.Add(loadedCoins[vmIndex][i][j]);                     // Track this specific coin

                            // If this combination matches changeRequired, return these coins
                            } if (changeActual == changeRequired) break;                // If a successful match was found, break out of third for loop
                        } if (changeActual == changeRequired) break;                    // If a successful match was found, break out of second for loop
                    } if (changeActual == changeRequired) break;                        // If a successful match was found, break out of first for loop
                }
            }
        }
        return changeCoins;
    }


    ////////////////////////////////////////////////////////////
    // Event Handlers
    ////////////////////////////////////////////////////////////

    public void printCoinsLoaded(object sender, CoinEventArgs e) {
        Console.WriteLine("Coins loaded: " + e.Coins);
    }

    public void printCoinsUnloaded(object sender, CoinEventArgs e) {
        Console.WriteLine("Coins unloaded: " + e.Coins);
    }

    public void printPopsLoaded(object sender, PopCanEventArgs e) {
        Console.WriteLine("Pops loaded: " + e.PopCans);
    }

    public void printPopsUnloaded(object sender, PopCanEventArgs e) {
        Console.WriteLine("Pops unloaded: " + e.PopCans);
    }

    public void printSlotCoinRejected(object sender, CoinEventArgs e) {
        Console.WriteLine("Coin slot just rejected this coin: " + e.Coin);
    }

    public void printSlotCoinAccepted(object sender, CoinEventArgs e) {
        Console.WriteLine("Coin slot just accepted this coin: " + e.Coin);
    }

    public void printReceptacleCoinAccepted(object sender, CoinEventArgs e) {
        Console.WriteLine("Coin valid! Receptacle just accepted this coin: " + e.Coin);
    }

    public void printReceptacleCoinsRemoved(object sender, EventArgs e) {
        Console.WriteLine("Coins removed from receptacle");
    }

    public void printItemDelivered(object sender, DeliverableEventArgs e) {
        Console.WriteLine("Item delievered to delivery chute: " + e.Item);
    }

    public void printButtonPressed(object sender, EventArgs e) {
        Console.WriteLine("Button pressed.");
    }
}