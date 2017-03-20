using System;
using System.Linq;
using System.Collections.Generic;
using Frontend4;
using Frontend4.Hardware;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace VendingMachineTester {

    [TestClass]
    public class BadSetup {

        /// <summary>
        /// U01
        /// </summary>
        [TestMethod]
        public void BadConfigureBeforeConstruct() {
            // This test does not make any sense
        }

        /// <summary>
        /// U02
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ArgumentOutOfRangeException))]
        public void BadCostsList() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            vm.Hardware.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                            new ProductKind("water", new Cents(250)),
                                                            new ProductKind("stuff", new Cents(0)) });                                                            
        }

        /// <summary>
        /// U03
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ArgumentException))]
        public void BadNamesList() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            vm.Hardware.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                            new ProductKind("water", new Cents(250)) });
        }

        /// <summary>
        /// U04
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(Exception))]
        public void BadUniqueDenomination() {
            var vm = new VendingMachine(new Cents[] { new Cents(1), new Cents(1) }, 1, 10, 10, 10);
        }

        /// <summary>
        /// U05
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(Exception))]
        public void BadCoinKind() {
            var vm = new VendingMachine(new Cents[] { new Cents(0) }, 1, 10, 10, 10);
        }

        /// <summary>
        /// U06
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(IndexOutOfRangeException))]
        public void BadButton1() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            vm.Hardware.SelectionButtons[3].Press();
        }

        /// <summary>
        /// U07
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(IndexOutOfRangeException))]
        public void BadButton2() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            vm.Hardware.SelectionButtons[-1].Press();
        }

        /// <summary>
        /// U08
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(IndexOutOfRangeException))]
        public void BadButton3() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            vm.Hardware.SelectionButtons[4].Press();
        }
    }
}
