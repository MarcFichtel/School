using System;
using System.Linq;
using System.Collections.Generic;
using Frontend4;
using Frontend4.Hardware;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace VendingMachineTester {

    [TestClass]
    public class GoodWithWeirdOrdering {

        /// <summary>
        /// T03
        /// </summary>
        [TestMethod]
        public void GoodTeardownWithoutConfigureOrLoad() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            var hardware = vm.Hardware;
            
            var delivery = VMUtility.ExtractDelivery(hardware);
            var unload = VMUtility.Unload(hardware);

            VMUtility.CheckDelivery(delivery, 0, new string[] { });
            VMUtility.CheckUnload(unload, 0, 0, new string[] { });
        }

        /// <summary>
        /// T05
        /// </summary>
        [TestMethod]
        public void GoodScrambledCoins() {
            var vm = new VendingMachine(new Cents[] { new Cents(100), new Cents(5), new Cents(25), new Cents(10) }, 3, 2, 10, 10);
            vm.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                            new ProductKind("water", new Cents(250)),
                                                            new ProductKind("stuff", new Cents(205)) });
            var hardware = vm.Hardware;
            hardware.LoadCoins(new int[] { 0, 1, 2, 1 });
            hardware.LoadProducts(new int[] { 1, 1, 1 });
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);

            VMUtility.CheckDelivery(delivery, 0, new string[] { });

            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.SelectionButtons[0].Press();
            delivery = VMUtility.ExtractDelivery(hardware);

            VMUtility.CheckDelivery(delivery, 50, new string[] { "Coke" });

            var unload = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(unload, 215, 100, new string[] { "water", "stuff" });
        }

        /// <summary>
        /// T06
        /// </summary>
        [TestMethod]
        public void GoodExtractBeforeSale() {
            var vm = new VendingMachine(new Cents[] { new Cents(100), new Cents(5), new Cents(25), new Cents(10) }, 3, 10, 10, 10);

            vm.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                            new ProductKind("water", new Cents(250)),
                                                            new ProductKind("stuff", new Cents(205)) });
            var hardware = vm.Hardware;
            hardware.LoadCoins(new int[] { 0, 1, 2, 1 });
            hardware.LoadProducts(new int[] { 1, 1, 1 });
            hardware.SelectionButtons[0].Press();

            var delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 0, new string[] { });

            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));

            delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 0, new string[] { });

            var unload = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(unload, 65, 0, new string[] { "Coke", "water", "stuff" });
        }

        /// <summary>
        /// T07
        /// </summary>
        [TestMethod]
        public void GoodChangingConfiguration() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            
            vm.Configure(new List<ProductKind>() { new ProductKind("A", new Cents(5)),
                                                            new ProductKind("B", new Cents(10)),
                                                            new ProductKind("C", new Cents(25)) });
            var hardware = vm.Hardware;
            hardware.LoadCoins(new int[] { 1, 1, 2, 0 });
            hardware.LoadProducts(new int[] { 1, 1, 1 });
                        
            vm.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                            new ProductKind("water", new Cents(250)),
                                                            new ProductKind("stuff", new Cents(205)) });
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 0, new string[] { });

            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.SelectionButtons[0].Press();

            delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 50, new string[] { "A" });
            
            var unload = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(unload, 315, 0, new string[] { "B", "C" });

            hardware.LoadCoins(new int[] { 1, 1, 2, 0 });
            hardware.LoadProducts(new int[] { 1, 1, 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.SelectionButtons[0].Press();
            delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 50, new string[] { "Coke" });
            
            unload = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(unload, 315, 0, new string[] { "water", "stuff" });

        }

    }
}
