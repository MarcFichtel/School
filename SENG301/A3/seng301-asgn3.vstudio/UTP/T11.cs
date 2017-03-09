using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using Frontend2.Hardware;
using Frontend2;

namespace UTP {

    /*
    T11:
        CREATE(100, 5, 25, 10; 3; 10; 10; 10)
        CONFIGURE([0] "Coke", 250; "water", 250; "stuff", 205)
        COIN_LOAD([0] 0; 100, 0)
        COIN_LOAD([0] 1; 5, 1)
        COIN_LOAD([0] 2; 25, 2)
        COIN_LOAD([0] 3; 10, 1)
        POP_LOAD([0] 0; "Coke", 1)
        POP_LOAD([0] 1; "water", 1)
        POP_LOAD([0] 2; "stuff", 1)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(0)
        INSERT([0] 100)
        INSERT([0] 100)
        INSERT([0] 100)
        EXTRACT([0])
        CHECK_DELIVERY(0)
        UNLOAD([0])
        CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
        COIN_LOAD([0] 0; 100, 0)
        COIN_LOAD([0] 1; 5, 1)
        COIN_LOAD([0] 2; 25, 2)
        COIN_LOAD([0] 3; 10, 1)
        POP_LOAD([0] 0; "Coke", 1)
        POP_LOAD([0] 1; "water", 1)
        POP_LOAD([0] 2; "stuff", 1)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(50, "Coke")
        UNLOAD([0])
        CHECK_TEARDOWN(315; 0; "water", "stuff")
        
        CREATE(100, 5, 25, 10; 3; 10; 10; 10)
        CONFIGURE([1] "Coke", 250; "water", 250; "stuff", 205)
        CONFIGURE([1] "A", 5; "B", 10; "C", 25)
        UNLOAD([1])
        CHECK_TEARDOWN(0; 0)
        COIN_LOAD([1] 0; 100, 0)
        COIN_LOAD([1] 1; 5, 1)
        COIN_LOAD([1] 2; 25, 2)
        COIN_LOAD([1] 3; 10, 1)
        POP_LOAD([1] 0; "A", 1)
        POP_LOAD([1] 1; "B", 1)
        POP_LOAD([1] 2; "C", 1)
        INSERT([1] 10)
        INSERT([1] 5)
        INSERT([1] 10)
        PRESS([1] 2)
        EXTRACT([1])
        CHECK_DELIVERY(0, "C")
        UNLOAD([1])
        CHECK_TEARDOWN(90; 0; "A", "B")
    */

    [TestClass]
    public class T11 {

        [TestMethod]
        public void Test11() {
        
            // CREATE(100, 5, 25, 10; 3; 10; 10; 10)
            int[] coinKinds = { 100, 5, 25, 10 };
            int buttonCount = 3;
            int coinRackCap = 10;
            int popsRackCap = 10;
            int receptacCap = 10;
            VendingMachine vm1 = new VendingMachine(coinKinds, buttonCount, coinRackCap, popsRackCap, receptacCap);

            // Initialize vending machine logic object
            VendingMachineLogic vml = new VendingMachineLogic(vm1);        

            // CONFIGURE([0] "Coke", 250; "water", 250; "stuff", 205)
            List<string> popNames = new List<string>{ "Coke", "water", "stuff" };
            List<int> popCosts = new List<int>{ 250, 250, 205 };
            vm1.Configure(popNames, popCosts);

            // COIN_LOAD([0] 3; 100, 0)
            // COIN_LOAD([0] 0; 5, 1)
            // COIN_LOAD([0] 2; 25, 2)
            // COIN_LOAD([0] 1; 10, 1)
            int[] coinCounts = { 0, 1, 2, 1 };
            vm1.LoadCoins(coinCounts);

            // POP_LOAD([0] 0; "Coke", 1)
            // POP_LOAD([0] 1; "water", 1)
            // POP_LOAD([0] 2; "stuff", 1)
            int[] popCounts = { 1, 1, 1 };
            vm1.LoadPopCans(popCounts);      
            
            // PRESS([0] 0)
            vm1.SelectionButtons[0].Press();

            // EXTRACT([0])
            IDeliverable[] contentsList = vm1.DeliveryChute.RemoveItems();  // Remove items from delivery chute
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

            // CHECK_DELIVERY(0)
            // TODO Check if its possible to assert two lists or arrays are the same
            string[] expected = { null, null };             // Set up expected result
            for (int i = 0; i < contents.Length; i++) {     // Iterate over contents
                Assert.AreEqual(contents[i], expected[i]);  // Assert each content element is as expected
            }  
            
            // INSERT([0] 100)
            // INSERT([0] 100)
            // INSERT([0] 100)
            Coin coin = new Coin(100);
            vm1.CoinSlot.AddCoin(coin);
            vm1.CoinSlot.AddCoin(coin);
            vm1.CoinSlot.AddCoin(coin);
            
            // EXTRACT([0])
            contentsList = vm1.DeliveryChute.RemoveItems();                 // Remove items from delivery chute
            contents = new string[2];                                       // List tracks dispensed pop and change
            coinsValue = 0;                                                 // Variable to hold value of change
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

            // CHECK_DELIVERY(0)
            // TODO Check if its possible to assert two lists or arrays are the same
            expected = new string[] { null, null };         // Set up expected result
            for (int i = 0; i < contents.Length; i++) {     // Iterate over contents
                Assert.AreEqual(contents[i], expected[i]);  // Assert each content element is as expected
            } 
            

            // UNLOAD([0])
            int storedCoinsValue = 0;                   // Variable for value of all stored coins
            int storageBinValue = 0;                    // Variable for value of coins in storage bin
            List<Coin> storedCoins = new List<Coin>();  // variable for tracking a set of coins

            foreach (CoinRack cr in vm1.CoinRacks) {    // Iterate over coin racks
                storedCoins = cr.Unload();              // Unload coin rack
                foreach (Coin c in storedCoins) {       // Iterate over coins in coin rack
                    storedCoinsValue += c.Value;        // Add each coin's value to value of all stored coins
                }
            }

            storedCoins = vm1.StorageBin.Unload();      // Unload storage bin
            foreach (Coin c in storedCoins) {           // Iterate over coins in storage bin
                storageBinValue += c.Value;             // Add each coin's value to value of all stored coins
            }

            List<string> pops = new List<string>();         // Variable for tracking stored pop names
            List<PopCan> temp = new List<PopCan>();         // Temporary variable for stored pops
            foreach (PopCanRack pcr in vm1.PopCanRacks) {   // Iterate over pop can racks
                temp.AddRange(pcr.Unload());                // Add each rack's contents to temp
            }
            foreach (PopCan pc in temp) {               // Iterate over all pops in temp
                pops.Add(pc.Name);                      // Add each pop's name to pops
            }

            // CHECK_TEARDOWN(65; 0; "Coke", "water", "stuff")
            int expected1 = 65;                                                     // Variable holds expected result 1
            int expected2 = 0;                                                      // Variable holds expected result 2
            List<string> expected3 = new List<string>{ "Coke", "water", "stuff" };  // Variable holds expected result 3
            Assert.AreEqual(storedCoinsValue, expected1);                           // Assert that stored coins value is as expected
            Assert.AreEqual(storageBinValue, expected2);                            // Assert that storage bin value is as expected
            for (int i = 0; i < pops.Count; i++) {                                  // Iterate over pops
                Assert.AreEqual(pops[i], expected3[i]);                             // Assert each unloaded pop is as expected
            }
            
            // COIN_LOAD([0] 3; 100, 0)
            // COIN_LOAD([0] 0; 5, 1)
            // COIN_LOAD([0] 2; 25, 2)
            // COIN_LOAD([0] 1; 10, 1)
            vm1.LoadCoins(coinCounts);

            // POP_LOAD([0] 0; "Coke", 1)
            // POP_LOAD([0] 1; "water", 1)
            // POP_LOAD([0] 2; "stuff", 1)
            vm1.LoadPopCans(popCounts);      
            
            // PRESS([0] 0)
            vm1.SelectionButtons[0].Press();

            // EXTRACT([0])
            contentsList = vm1.DeliveryChute.RemoveItems();                 // Remove items from delivery chute
            contents = new string[2];                                       // List tracks dispensed pop and change 
            coinsValue = 0;                                                 // Variable to hold value of change
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

            // CHECK_DELIVERY(50, "Coke")
            // TODO Check if its possible to assert two lists or arrays are the same
            expected = new string[] { "Coke", "50" };       // Set up expected result
            for (int i = 0; i < contents.Length; i++) {     // Iterate over contents
                Assert.AreEqual(contents[i], expected[i]);  // Assert each content element is as expected
            }
            

            // UNLOAD([0])
            storedCoinsValue = 0;                       // Variable for value of all stored coins
            storageBinValue = 0;                        // Variable for value of coins in storage bin
            storedCoins = new List<Coin>();             // Variable for tracking a set of coins

            foreach (CoinRack cr in vm1.CoinRacks) {    // Iterate over coin racks
                storedCoins = cr.Unload();              // Unload coin rack
                foreach (Coin c in storedCoins) {       // Iterate over coins in coin rack
                    storedCoinsValue += c.Value;        // Add each coin's value to value of all stored coins
                }
            }

            storedCoins = vm1.StorageBin.Unload();      // Unload storage bin
            foreach (Coin c in storedCoins) {           // Iterate over coins in storage bin
                storageBinValue += c.Value;             // Add each coin's value to value of all stored coins
            }

            List<string> pops = new List<string>();         // Variable for tracking stored pop names
            List<PopCan> temp = new List<PopCan>();         // Temporary variable for stored pops
            foreach (PopCanRack pcr in vm1.PopCanRacks) {   // Iterate over pop can racks
                temp.AddRange(pcr.Unload());                // Add each rack's contents to temp
            }
            foreach (PopCan pc in temp) {               // Iterate over all pops in temp
                pops.Add(pc.Name);                      // Add each pop's name to pops
            }

            // CHECK_TEARDOWN(315; 0; "water", "stuff")
            expected1 = 315;                                                // Variable holds expected result 1
            expected2 = 0;                                                  // Variable holds expected result 2
            expected3 = new List<string>{ "water", "stuff" };               // Variable holds expected result 3
            Assert.AreEqual(storedCoinsValue, expected1);                   // Assert that stored coins value is as expected
            Assert.AreEqual(storageBinValue, expected2);                    // Assert that storage bin value is as expected
            for (int i = 0; i < pops.Count; i++) {                          // Iterate over pops
                Assert.AreEqual(pops[i], expected3[i]);                     // Assert each unloaded pop is as expected
            }
            
            // CREATE(100, 5, 25, 10; 3; 10; 10; 10)
            VendingMachine vm2 = new VendingMachine(coinKinds, buttonCount, coinRackCap, popsRackCap, receptacCap);

            // Initialize vending machine logic object
            VendingMachineLogic vml = new VendingMachineLogic(vm2);        

            // CONFIGURE([1] "Coke", 250; "water", 250; "stuff", 205)
            vm2.Configure(popNames, popCosts);
            
            // CONFIGURE([1] "A", 5; "B", 10; "C", 25)
            popNames = new List<string>{ "A", "B", "C" };
            popCosts = new List<int>{ 5, 10, 25 };
            vm2.Configure(popNames, popCosts);
            
            // UNLOAD([1])
            storedCoinsValue = 0;                       // Variable for value of all stored coins
            storageBinValue = 0;                        // Variable for value of coins in storage bin
            storedCoins = new List<Coin>();             // Variable for tracking a set of coins

            foreach (CoinRack cr in vm2.CoinRacks) {    // Iterate over coin racks
                storedCoins = cr.Unload();              // Unload coin rack
                foreach (Coin c in storedCoins) {       // Iterate over coins in coin rack
                    storedCoinsValue += c.Value;        // Add each coin's value to value of all stored coins
                }
            }

            storedCoins = vm2.StorageBin.Unload();      // Unload storage bin
            foreach (Coin c in storedCoins) {           // Iterate over coins in storage bin
                storageBinValue += c.Value;             // Add each coin's value to value of all stored coins
            }

            List<string> pops = new List<string>();         // Variable for tracking stored pop names
            List<PopCan> temp = new List<PopCan>();         // Temporary variable for stored pops
            foreach (PopCanRack pcr in vm2.PopCanRacks) {   // Iterate over pop can racks
                temp.AddRange(pcr.Unload());                // Add each rack's contents to temp
            }
            foreach (PopCan pc in temp) {               // Iterate over all pops in temp
                pops.Add(pc.Name);                      // Add each pop's name to pops
            }

            // CHECK_TEARDOWN(0; 0)
            expected1 = 0;                                                  // Variable holds expected result 1
            expected2 = 0;                                                  // Variable holds expected result 2
            expected3 = new List<string>{ null };                           // Variable holds expected result 3
            Assert.AreEqual(storedCoinsValue, expected1);                   // Assert that stored coins value is as expected
            Assert.AreEqual(storageBinValue, expected2);                    // Assert that storage bin value is as expected
            for (int i = 0; i < pops.Count; i++) {                          // Iterate over pops
                Assert.AreEqual(pops[i], expected3[i]);                     // Assert each unloaded pop is as expected
            }
            
            // COIN_LOAD([1] 3; 100, 0)
            // COIN_LOAD([1] 0; 5, 1)
            // COIN_LOAD([1] 2; 25, 2)
            // COIN_LOAD([1] 1; 10, 1)
            vm2.LoadCoins(coinCounts);

            // POP_LOAD([1] 0; "A", 1)
            // POP_LOAD([1] 1; "B", 1)
            // POP_LOAD([1] 2; "C", 1)
            vm2.LoadPopCans(popCounts);
            
            // INSERT([1] 10)
            // INSERT([1] 5)
            // INSERT([1] 10)
            coin = new Coin(10);
            vm2.CoinSlot.AddCoin(coin);
            coin = new Coin(5);
            vm2.CoinSlot.AddCoin(coin);
            coin = new Coin(10);
            vm2.CoinSlot.AddCoin(coin);
            
            // PRESS([1] 2)
            vm2.SelectionButtons[2].Press();
            
            // EXTRACT([1])
            contentsList = vm2.DeliveryChute.RemoveItems();                 // Remove items from delivery chute
            contents = new string[2];                                       // List tracks dispensed pop and change 
            coinsValue = 0;                                                 // Variable to hold value of change
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
            
            // CHECK_DELIVERY(0, "C")
            // TODO Check if its possible to assert two lists or arrays are the same
            expected = new string[] { "C", null };          // Set up expected result
            for (int i = 0; i < contents.Length; i++) {     // Iterate over contents
                Assert.AreEqual(contents[i], expected[i]);  // Assert each content element is as expected
            }
            
            // UNLOAD([1])
            storedCoinsValue = 0;                       // Variable for value of all stored coins
            storageBinValue = 0;                        // Variable for value of coins in storage bin
            storedCoins = new List<Coin>();             // Variable for tracking a set of coins

            foreach (CoinRack cr in vm2.CoinRacks) {    // Iterate over coin racks
                storedCoins = cr.Unload();              // Unload coin rack
                foreach (Coin c in storedCoins) {       // Iterate over coins in coin rack
                    storedCoinsValue += c.Value;        // Add each coin's value to value of all stored coins
                }
            }

            storedCoins = vm2.StorageBin.Unload();      // Unload storage bin
            foreach (Coin c in storedCoins) {           // Iterate over coins in storage bin
                storageBinValue += c.Value;             // Add each coin's value to value of all stored coins
            }

            List<string> pops = new List<string>();         // Variable for tracking stored pop names
            List<PopCan> temp = new List<PopCan>();         // Temporary variable for stored pops
            foreach (PopCanRack pcr in vm2.PopCanRacks) {   // Iterate over pop can racks
                temp.AddRange(pcr.Unload());                // Add each rack's contents to temp
            }
            foreach (PopCan pc in temp) {               // Iterate over all pops in temp
                pops.Add(pc.Name);                      // Add each pop's name to pops
            }
            
            // CHECK_TEARDOWN(90; 0; "A", "B")
            expected1 = 90;                                                 // Variable holds expected result 1
            expected2 = 0;                                                  // Variable holds expected result 2
            expected3 = new List<string>{ "A", "B" };                       // Variable holds expected result 3
            Assert.AreEqual(storedCoinsValue, expected1);                   // Assert that stored coins value is as expected
            Assert.AreEqual(storageBinValue, expected2);                    // Assert that storage bin value is as expected
            for (int i = 0; i < pops.Count; i++) {                          // Iterate over pops
                Assert.AreEqual(pops[i], expected3[i]);                     // Assert each unloaded pop is as expected
            }
        }
    }
}
