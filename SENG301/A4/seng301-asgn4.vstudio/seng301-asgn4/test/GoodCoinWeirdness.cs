using System;
using System.Linq;
using System.Collections.Generic;
using Frontend4;
using Frontend4.Hardware;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace VendingMachineTester {
    [TestClass]
    public class GoodCoinWeirdness {

        /// <summary>
        /// T08
        /// </summary>
        [TestMethod]
        public void GoodApproximateChange() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 1, 10, 10, 10);
            var hardware = vm.Hardware;

            vm.Configure(new List<ProductKind>() { new ProductKind("stuff", new Cents(140)) });
            hardware.LoadCoins(new int[] { 0, 5, 1, 1, });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 155, new string[] { "stuff" });
            var contents = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(contents, 320, 0, new string[] { });
        }
        /// <summary>
        /// T09
        /// </summary>
        [TestMethod]
        public void GoodHardForChange() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 1, 10, 10, 10);

            var hardware = vm.Hardware;
            vm.Configure(new List<ProductKind>() { new ProductKind("stuff", new Cents(140)) });
            hardware.LoadCoins(new int[] { 1, 6, 1, 1, });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 160, new string[] { "stuff" });
            var contents = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(contents, 330, 0, new string[] { });
        }
        /// <summary>
        /// T10
        /// </summary>
        [TestMethod]
        public void GoodInvalidCoin() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 1, 10, 10, 10);

            var hardware = vm.Hardware;
            vm.Configure(new List<ProductKind>() { new ProductKind("stuff", new Cents(140)) });
            hardware.LoadCoins(new int[] { 1, 6, 1, 1 });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(1)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(139)));
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 140, new string[] { });
            var contents = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(contents, 190, 0, new string[] { "stuff" });
        }

        /// <summary>
        /// T12
        /// </summary>
        [TestMethod]
        public void GoodApproximateChangeWithCredit() {
            var vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 1, 10, 10, 10);

            var hardware = vm.Hardware;
            vm.Configure(new List<ProductKind>() { new ProductKind("stuff", new Cents(140)) });
            hardware.LoadCoins(new int[] { 0, 5, 1, 1, });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.SelectionButtons[0].Press();
            var delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 155, new string[] { "stuff" });
            var contents = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(contents, 320, 0, new string[] { });

            hardware.LoadCoins(new int[] { 10, 10, 10, 10, });
            hardware.LoadProducts(new int[] { 1 });
            hardware.CoinSlot.AddCoin(new Coin(new Cents(25)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            hardware.CoinSlot.AddCoin(new Coin(new Cents(10)));
            hardware.SelectionButtons[0].Press();
            delivery = VMUtility.ExtractDelivery(hardware);
            VMUtility.CheckDelivery(delivery, 0, new string[] { "stuff" });
            contents = VMUtility.Unload(hardware);
            VMUtility.CheckUnload(contents, 1400, 135, new string[] { });
        }
    }
}
