using System;
using System.Linq;
using System.Collections.Generic;
using Frontend4;
using Frontend4.Hardware;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace VendingMachineTester {
    [TestClass]
    public class GoodMisc {

        /// <summary>
        /// T11
        /// </summary>
        [TestMethod]
        public void GoodExtractBeforeSaleComplex() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 1, 10, 10, 10);
            var hardware = vm.Hardware;
            vm.Configure(new List<ProductKind>() { new ProductKind("stuff", new Cents(140)) });
            hardware.LoadCoins(new int[] { 0, 5, 1, 1 });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 155, new string[] { "stuff" });
            var unload = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(unload, 320, 0, new string[] { });
            hardware.LoadCoins(new int[] { 10, 10, 10, 10 });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(25)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(10)));
            hardware.SelectionButtons[0].Press();
            delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 0, new string[] { "stuff" });
            unload = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(unload, 1400, 135, new string[] { });
        }

        /// <summary>
        /// T13
        /// </summary>
        [TestMethod]
        public void GoodNeedToStorePayment() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 1, 10, 10, 10);
            var hardware = vm.Hardware;

            vm.Configure(new List<ProductKind>() { new ProductKind("stuff", new Cents(135)) });
            hardware.LoadCoins(new int[] { 10, 10, 10, 10 });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(25)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(10)));
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);
            var unload = VMUtility.Unload(hardware);
            VMUtility.CheckDelivery(delivery, 0, new string[] { "stuff" });
            VMUtility.CheckUnload(unload, 1400, 135, new string[] { });
        }
    }
}
