using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using Frontend2.Hardware;
using Frontend2;

namespace UTP {

    /*
       1. Negative coin kind: This edge case tests whether negative coin kinds throw an exception (the given test scripts only cover the edge case of a coin kind equalling 0) 
       2. Bad configure: Configure a vending machine with a negative amount of selection buttons
    */ 

    [TestClass]
    public class BonusTests {

        [TestMethod]
        [ExpectedException(typeof(Exception))]
        public void Bonus1() {

            // CREATE(-1; 1; 10; 10; 10)
            int[] coinKinds = { -1 };
            int buttonCount = 1;
            int coinRackCap = 10;
            int popsRackCap = 10;
            int receptacCap = 10;
            VendingMachine vm = new VendingMachine(coinKinds, buttonCount, coinRackCap, popsRackCap, receptacCap);

            // Initialize vending machine logic object
            VendingMachineLogic vml = new VendingMachineLogic(vm);

        }

        [TestMethod]
        [ExpectedException(typeof(Exception))]
        public void Bonus2()
        {

            // CREATE(-1; 1; 10; 10; 10)
            int[] coinKinds = { 0 };
            int buttonCount = -1;
            int coinRackCap = 10;
            int popsRackCap = 10;
            int receptacCap = 10;
            VendingMachine vm = new VendingMachine(coinKinds, buttonCount, coinRackCap, popsRackCap, receptacCap);

            // Initialize vending machine logic object
            VendingMachineLogic vml = new VendingMachineLogic(vm);

        }
    }
}
