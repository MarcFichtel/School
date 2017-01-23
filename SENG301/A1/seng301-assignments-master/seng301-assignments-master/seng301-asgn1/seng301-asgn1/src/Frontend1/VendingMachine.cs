using Frontend1;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace seng301_asgn1.src.Frontend1
{
    class VendingMachine
    {
        // Fields
        private List<int> coinKinds;                                                            // Type of coins accepted by this machine
        private int selectButtons;                                                              // Number of selection buttons on this machine
        private Dictionary<string , int> popNamesAndCosts = new Dictionary<string, int>();            // Dictionary maps pops to costs accepted by this machine
        private Dictionary<int, List<Coin>> loadedCoins = new Dictionary<int, List<Coin>>();    // Dictionary maps a coin kind index to the number of coins for loaded (i.e. change) coins
        private Dictionary<int, List<Pop>> loadedPops = new Dictionary<int, List<Pop>>();       // Dictionary maps a pop kind index to the number of pops for loaded pops
        private List<Coin> insertedCoins = new List<Coin>();                                                       // A list of inserted (i.e. profit) coins
        private List<Deliverable> deliveryChute = new List<Deliverable>();                                                // List of contents in the machine's delivery chute (change, unaccepted coins, pop) 

        // Constructor
        public VendingMachine (List<int> coinKinds, int selectButtons)
        {
            this.coinKinds = coinKinds;
            this.selectButtons = selectButtons;
        }

        #region Getters
        // Coin kinds getter
        public List<int> GetCoinKinds ()
        {
            return coinKinds;
        }

        // Selection buttons getter
        public int GetSelectButtons()
        {
            return selectButtons;
        }
        #endregion

        // Configure machine
        public void Configure (List<string> popNames, List<int> popCosts)
        {
            for (int i = 0; i < popNames.Count; i++)
            {
                popNamesAndCosts.Add(popNames[i], popCosts[i]);
            }
        }

        // Load coins
        public void LoadCoins (int coinKindIndex, List<Coin> coins)
        {
            loadedCoins.Add(coinKindIndex, coins);
        }

        // Load pops
        public void LoadPops(int popKindIndex, List<Pop> pops)
        {
            loadedPops.Add(popKindIndex, pops);
        }

        // Insert coins
        public void InsertCoin (Coin coin)
        {
            // Check if coin is accepted by this machine
            if (coinKinds.Contains(coin.Value)) 
            {
                insertedCoins.Add(coin);                                // If accepted, add to profit coins
            } else {
                deliveryChute.Add(coin);                                // Else dispense coin in delivery chute
            }
        }

        // Press button
        public void PressButton (int button)
        {
            // Compute sum of inserted coins, if there are any
            int insertedCoinsValue = 0;
            foreach (Coin c in insertedCoins) 
            {
                insertedCoinsValue += c.Value;
            }

            // Get price of pop associated with this button
            Pop orderedPop = loadedPops[button][0];
            int price = popNamesAndCosts[orderedPop.ToString()];

            // Compare pop price to inserted coins sum
            if (insertedCoinsValue >= price)
            {
                // Dispense pop only (and remove from loaded pops)
                deliveryChute.Add(orderedPop);
                loadedPops[button].Remove(orderedPop);

                if (insertedCoinsValue > price)
                {
                    // Dispense change, if enough is available, else dispense nothing
                    int changeRequired = insertedCoinsValue - price;        // Compute required change
                    int changeAvailable = 0;                                // Compute available change 
                    for (int i = 0; i < loadedCoins.Count; i++) 
                    {
                        int numCoins = loadedCoins[i].Count;
                        for (int j = 0; j < numCoins; j++) {
                            changeAvailable += loadedCoins[i][j].Value;
                        }
                    }

                    /*
                     Check if coins can be combined to the required change
                     --> Add max number of greatest value coins until sum is above required change
                     --> Don't include last coin, then repeat for smaller value coins
                     --> If combination cannot be created, repeat by starting at second highest value coin and so on
                     --> If combination cannot be created after cycling through all values, do no dispense change
                     --> For edge cases, repeat the above starting at the minimum

                     Note: This assumes that coins are ordered by value, but this is not necessary
                    */
        
                    int changeActual = 0;                                                       // Initialize actual change
                    if (changeAvailable >= changeRequired)                                      // Check if enough change is present
                    {
                        // Look for matching coin combinations starting at min value
                        for (int h = 0; h < loadedCoins.Count; h++)                             // Cycle forward through coin kinds
                        {
                            changeActual = 0;                                                   // Reset changeActual to 0
                            List<Coin> changeCoins = new List<Coin>();                          // List of the actual change coins
                            for (int i = h; i < loadedCoins.Count; i++)                         // Cycle forward through remaining coin kinds
                            {
                                for (int j = 0; j < loadedCoins[i].Count; j++)                  // Cycle through coins
                                {
                                    // Add coin value to changeActual if the sum doesn't exceep changeRequired
                                    if (changeActual + loadedCoins[i][j].Value <= changeRequired) 
                                    {
                                        changeActual += loadedCoins[i][j].Value;
                                        changeCoins.Add(loadedCoins[i][j]);                     // Track this specific coin
                                    }

                                    // If this combination matches changeRequired, dispense these coins
                                    if (changeActual == changeRequired) {
                                        
                                        // Dispense coins
                                        foreach (Coin c in changeCoins) {
                                            deliveryChute.Add(c);                               // Add coin to delivery chute

                                            for (int m = 0; m < loadedCoins.Count; m++) {       // Remove coin from loaded coins
                                                if (loadedCoins[m].Contains(c)) {
                                                    loadedCoins[m].Remove(c);
                                                }
                                            }
                                        }

                                        // Break out of third for loop
                                        break;
                                    }
                                }
                                // If a successful match was found, break out of second for loop
                                if (changeActual == changeRequired)
                                    break;
                            }
                            // If a successful match was found, break out of first for loop
                            if (changeActual == changeRequired)
                                break;
                        }

                        // Only do the reverse run-through if the first was unsuccessful
                        if (changeActual != changeRequired) {
                            // Look for matching coin combinations starting at max value
                            for (int h = loadedCoins.Count - 1; h >= 0; h--)                        // Cycle backwards through coin kinds
                            {
                                changeActual = 0;                                                   // Reset changeActual to 0
                                List<Coin> changeCoins = new List<Coin>();                          // List of the actual change coins
                                for (int i = h; i >= 0; i--)                                        // Cycle backwards through remaining coin kinds
                                {
                                    for (int j = 0; i < loadedCoins[i].Count; j++)                  // Cycle through coins
                                    {
                                        // Add coin value to changeActual if the sum doesn't exceep changeRequired
                                        if (changeActual + loadedCoins[i][j].Value <= changeRequired)
                                            changeActual += loadedCoins[i][j].Value;
                                            changeCoins.Add(loadedCoins[i][j]);                     // Track this specific coin

                                        // If this combination matches changeRequired, dispense these coins
                                        if (changeActual == changeRequired) {

                                            // Dispense coins
                                            foreach (Coin c in changeCoins) {
                                                deliveryChute.Add(c);                               // Add coin to delivery chute

                                                for (int m = 0; m < loadedCoins.Count; m++) {       // Remove coin from loaded coins
                                                    if (loadedCoins[m].Contains(c)) {
                                                        loadedCoins[m].Remove(c);
                                                    }
                                                }
                                            }

                                            // Break out of third for loop
                                            break;
                                        }
                                    }
                                    // If a successful match was found, break out of second for loop
                                    if (changeActual == changeRequired)
                                        break;
                                }
                                // If a successful match was found, break out of first for loop
                                if (changeActual == changeRequired)
                                    break;
                            }
                        }
                    }
                }
            } 
        }

        // Empty delivery chute
        public List<Deliverable> EmptyDeliveryChute()
        {
            List<Deliverable> x = new List<Deliverable>(deliveryChute);     // Get delivery chute stuff in temporary list
            deliveryChute.Clear();                                          // Clear actual delivery chute
            return x;                                                       // Return the temporary list
        }

        // Unload machine
        public List<IList> UnloadMachine()
        {
            // Create and return a list of lists for loaded coins, inserted coins, and pops
            List<IList> unloads = new List<IList>();
            unloads.Add(loadedCoins.SelectMany(d => d.Value).ToList());
            unloads.Add(insertedCoins);
            unloads.Add(loadedPops.SelectMany(d => d.Value).ToList());
            return unloads;
        }
    }
}
