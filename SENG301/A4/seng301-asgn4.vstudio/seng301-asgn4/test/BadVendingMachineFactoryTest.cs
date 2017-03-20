using System;
using System.Linq;
using System.Collections.Generic;
using Frontend4;
using Frontend4.Hardware;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace VendingMachineTester {

    [TestClass]
    public class BadSetup {

        private HardwareFacade hardware;

        [TestInitialize]
        public void Initialize() {
            VendingMachine vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            this.hardware = vm.Hardware;
        }

        /**
         * bad-script2
         */
        [TestMethod]
        [ExpectedException(typeof(ArgumentOutOfRangeException))]
        public void testBadScript2() {
            this.hardware.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                              new ProductKind("water", new Cents(250)),
                                                              new ProductKind("stuff", new Cents(0)) });
        }

        /**
         * U02
         */
        [TestMethod]
        [ExpectedException(typeof(ArgumentOutOfRangeException))]
        public void testBadCostsList() {
            this.hardware.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                             new ProductKind("water", new Cents(250)),
                                                             new ProductKind("stuff", new Cents(0)) });
        }

        /**
         * U03
         */
        [TestMethod]
        [ExpectedException(typeof(ArgumentException))]
        public void testBadNamesList() {
            this.hardware.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                             new ProductKind("water", new Cents(250)) });
        }

        /**
         * U04
         */
        [TestMethod]
        [ExpectedException(typeof(Exception))]
        public void testNonUniqueDenomination() {
            new VendingMachine(new Cents[] { new Cents(1), new Cents(1) }, 1, 10, 10, 10);
        }

        /**
         * U05
         */
        [TestMethod]
        [ExpectedException(typeof(Exception))]
        public void testBadCoinKind() {
            new VendingMachine(new Cents[] { new Cents(0) }, 1, 10, 10, 10);
        }

        /**
         * U06
         */
        [TestMethod]
        [ExpectedException(typeof(IndexOutOfRangeException))]
        public void testBadButton() {
            this.hardware.SelectionButtons[3].Press();
        }

        /**
         * U07
         */
        [TestMethod]
        [ExpectedException(typeof(IndexOutOfRangeException))]
        public void testBadButton2() {
            this.hardware.SelectionButtons[-1].Press();            
        }

        /**
         * U08
         */
        [TestMethod]
        [ExpectedException(typeof(IndexOutOfRangeException))]
        public void testBadButton3() {
            this.hardware.SelectionButtons[4].Press();            
        }
    }
}
