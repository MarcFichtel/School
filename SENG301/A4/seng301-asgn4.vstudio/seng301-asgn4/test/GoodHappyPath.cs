using System;
using System.Linq;
using System.Collections.Generic;
using Frontend4;
using Frontend4.Hardware;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace VendingMachineTester {

    [TestClass]
    public class GoodHappyPath {
        VendingMachine vm;
        HardwareFacade hardware;

        [TestInitialize]
        public void Initialize() {
            this.vm = new VendingMachine(new Cents[] { new Cents(5), new Cents(10), new Cents(25), new Cents(100) }, 3, 10, 10, 10);
            vm.Configure(new List<ProductKind>() { new ProductKind("Coke", new Cents(250)),
                                                   new ProductKind("water", new Cents(250)),
                                                   new ProductKind("stuff", new Cents(205)) });
            this.hardware = vm.Hardware;
            this.hardware.LoadCoins(new int[] { 1, 1, 2, 0 });
            this.hardware.LoadProducts(new int[] { 1, 1, 1 });            
        }

        /// <summary>
        /// T01
        /// </summary>
        [TestMethod]
        public void GoodInsertAndPressExactChange() {
            this.hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            this.hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            this.hardware.CoinSlot.AddCoin(new Coin(new Cents(25)));
            this.hardware.CoinSlot.AddCoin(new Coin(new Cents(25)));
            this.hardware.SelectionButtons[0].Press();

            var delivery = VMUtility.ExtractDelivery(this.hardware);
            var contents = VMUtility.Unload(this.hardware);

            VMUtility.CheckDelivery(delivery, 0, new string[] { "Coke" });
            VMUtility.CheckUnload(contents, 315, 0, new string[] { "water", "stuff" });         
        }

        /// <summary>
        /// T02
        /// </summary>
        [TestMethod]
        public void GoodInsertAndPressChangeExpected() {
            this.hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            this.hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            this.hardware.CoinSlot.AddCoin(new Coin(new Cents(100)));
            this.hardware.SelectionButtons[0].Press();

            var delivery = VMUtility.ExtractDelivery(this.hardware);
            var contents = VMUtility.Unload(this.hardware);

            VMUtility.CheckDelivery(delivery, 50, new string[] { "Coke" });
            VMUtility.CheckUnload(contents, 315, 0, new string[] { "water", "stuff" });
        }

        /// <summary>
        /// T04
        /// </summary>
        [TestMethod]
        public void GoodPressWithoutInsert() {
            this.hardware.SelectionButtons[0].Press();

            var delivery = VMUtility.ExtractDelivery(this.hardware);
            var contents = VMUtility.Unload(this.hardware);

            VMUtility.CheckDelivery(delivery, 0, new string[] { });
            VMUtility.CheckUnload(contents, 65, 0, new string[] { "Coke", "water", "stuff" });
        }
    }
}
