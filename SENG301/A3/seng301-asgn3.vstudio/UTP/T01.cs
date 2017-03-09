using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using Frontend2.Hardware;
using Frontend2;

namespace UTP {

    /*
    T01:
        CREATE(5, 10, 25, 100; 3; 10; 10; 10)
        CONFIGURE([0] "Coke", 250; "water", 250; "stuff", 205)
        COIN_LOAD([0] 0; 5, 1)
        COIN_LOAD([0] 1; 10, 1)
        COIN_LOAD([0] 2; 25, 2)
        COIN_LOAD([0] 3; 100, 0)
        POP_LOAD([0] 0; "Coke", 1)
        POP_LOAD([0] 1; "water", 1)
        POP_LOAD([0] 2; "stuff", 1)
        INSERT([0] 100)
        INSERT([0] 100)
        INSERT([0] 25)
        INSERT([0] 25)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(0, "Coke")
        UNLOAD([0])
        CHECK_TEARDOWN(315; 0; "water", "stuff")
    */

    [TestClass]
    public class T01 {

        [TestMethod]
        public void Test01() {

            // CREATE(5, 10, 25, 100; 3; 10; 10; 10)
            int[] coinKinds = { 5, 10, 25, 100 };
            int buttonCount = 3;
            int coinRackCap = 10;
            int popsRackCap = 10;
            int receptacCap = 10;
            VendingMachine vm = new VendingMachine(coinKinds, buttonCount, coinRackCap, popsRackCap, receptacCap);

            // Initialize vending machine logic object
            VendingMachineLogic vml = new VendingMachineLogic(vm);

            // CONFIGURE([0] "Coke", 250; "water", 250; "stuff", 205)
            List<string> popNames = new List<string>{ "Coke", "water", "stuff" };
            List<int> popCosts = new List<int>{ 250, 250, 205 };
            vm.Configure(popNames, popCosts);

            // COIN_LOAD([0] 0; 5, 1)
            // COIN_LOAD([0] 1; 10, 1)
            // COIN_LOAD([0] 2; 25, 2)
            // COIN_LOAD([0] 3; 100, 0)
            int[] coinCounts = { 1, 1, 2, 0 };
            vm.LoadCoins(coinCounts);

            // POP_LOAD([0] 0; "Coke", 1)
            // POP_LOAD([0] 1; "water", 1)
            // POP_LOAD([0] 2; "stuff", 1)
            int[] popCounts = { 1, 1, 1 };
            vm.LoadPopCans(popCounts);

            // INSERT([0] 100)
            // INSERT([0] 100)
            Coin coin = new Coin(100);
            vm.CoinSlot.AddCoin(coin);
            vm.CoinSlot.AddCoin(coin);

            // INSERT([0] 25)
            // INSERT([0] 25)
            coin = new Coin(25);
            vm.CoinSlot.AddCoin(coin);
            vm.CoinSlot.AddCoin(coin);

            // PRESS([0] 0)
            vm.SelectionButtons[0].Press();

            // EXTRACT([0])
            IDeliverable[] contentsList = vm.DeliveryChute.RemoveItems();   // Remove items from delivery chute
            string[] contents = new string[2];                              // List tracks dispensed pop and change
            int coinsValue = 0;                                             // Variable to hold value of change
            for (int i = 0; i < contentsList.Length; i++) {                 // Iterate over dispensed items
                if (contentsList[i].GetType() == typeof(Coin)) {            // if dispensed item is a coin...
                    Coin c = (Coin)contentsList[i];                         // Cast it as a coin, then...
                    coinsValue += c.Value;                                  // Add its value to coinsValue
                } else {                                                    // Else the dispensed item is a pop, so... 
                    contents[i] = contentsList[i].ToString();               // Add each pop's name to contents
                }
                if (coinsValue > 0) {                                       // If change was dispensed,...
                    contents[1] = coinsValue.ToString();                    // Add its value to contents
                } 
            }

            // CHECK_DELIVERY(0, "Coke")
            // TODO Check if its possible to assert two lists or arrays are the same
            string[] expected = { "Coke", null };                 // Set up expected result
            for (int i = 0; i < contents.Length; i++) {     // Iterate over contents
                Assert.AreEqual(contents[i], expected[i]);  // Assert each content element is as expected
            }

            // UNLOAD([0])
            int storedCoinsValue = 0;                   // Variable for value of all stored coins
            int storageBinValue = 0;                    // Variable for value of coins in storage bin
            List<Coin> storedCoins = new List<Coin>();  // variable for tracking a set of coins

            foreach (CoinRack cr in vm.CoinRacks) {     // Iterate over coin racks
                storedCoins = cr.Unload();              // Unload coin rack
                foreach (Coin c in storedCoins) {       // Iterate over coins in coin rack
                    storedCoinsValue += c.Value;        // Add each coin's value to value of all stored coins
                }
            }

            storedCoins = vm.StorageBin.Unload();       // Unload storage bin
            foreach (Coin c in storedCoins) {           // Iterate over coins in storage bin
                storageBinValue += c.Value;             // Add each coin's value to value of all stored coins
            }

            List<string> pops = new List<string>();     // Variable for tracking stored pop names
            List<PopCan> temp = new List<PopCan>();     // Temporary variable for stored pops
            foreach (PopCanRack pcr in vm.PopCanRacks) {// Iterate over pop can racks
                temp.AddRange(pcr.Unload());            // Add each rack's contents to temp
            }
            foreach (PopCan pc in temp) {               // Iterate over all pops in temp
                pops.Add(pc.Name);                      // Add each pop's name to pops
            }

            // CHECK_TEARDOWN(315; 0; "water", "stuff")
            int expected1 = 315;                                            // Variable holds expected result 1
            int expected2 = 0;                                              // Variable holds expected result 2
            List<string> expected3 = new List<string>{ "water", "stuff" };  // Variable holds expected result 3
            Assert.AreEqual(storedCoinsValue, expected1);                   // Assert that stored coins value is as expected
            Assert.AreEqual(storageBinValue, expected2);                    // Assert that storage bin value is as expected
            for (int i = 0; i < pops.Count; i++) {                          // Iterate over pops
                Assert.AreEqual(pops[i], expected3[i]);                     // Assert each unloaded pop is as expected
            }
        }
    }
}
