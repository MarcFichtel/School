using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using Frontend2.Hardware;
using Frontend2;

namespace UTP {

    /*
    U03:
        CREATE(5, 10, 25, 100; 3; 10; 10; 10)
        CONFIGURE([0] "Coke", 250; "water", 250)
    */

    [TestClass]
    public class U03 {

        [TestMethod]
        [ExpectedException(typeof(Exception))]
        public void Test16() {

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
            List<string> popNames = new List<string> { "Coke", "water" };
            List<int> popCosts = new List<int> { 250, 250 };
            vm.Configure(popNames, popCosts);
        }
    }
}
