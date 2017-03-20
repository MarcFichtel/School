using System;
using System.Collections.Generic;
using System.Linq;
using Frontend4;
using Frontend4.Hardware;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace VendingMachineTester {
    public class VMUtility {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="hardware"></param>
        /// <returns>A tuple: [value of coins], [array of product names]</returns>
        public static Tuple<int, string[]> ExtractDelivery(HardwareFacade hardware) {
            var items = hardware.DeliveryChute.RemoveItems();
            var cents = items.OfType<Coin>().Sum(coin => coin.Value.Value);
            var products = items.OfType<Product>().Select(product => product.Name).ToArray();
            
            return new Tuple<int, string[]>(cents, products);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="hardware"></param>
        /// <returns>A tuple: [value of coins in coinracks], [value of coins in storagebin], [array of product names remaining]</returns>
        public static Tuple<int, int, string[]> Unload(HardwareFacade hardware) {
            int centsInCoinRacks = 0;        
            foreach (var coinRack in hardware.CoinRacks) {
                centsInCoinRacks += coinRack.Unload().Sum(coin => coin.Value.Value);
            }
            var centsInStorageBin = hardware.StorageBin.Unload().Sum(coin => coin.Value.Value);
            var remainingProducts = new List<Product>();
            foreach (var pr in hardware.ProductRacks) {
                remainingProducts.AddRange(pr.Unload());
            }

            return new Tuple<int, int, string[]>(centsInCoinRacks, centsInStorageBin, remainingProducts.Select(product => product.Name).ToArray());
        }

        public static void CheckDelivery(Tuple<int, string[]> delivery, int cents, string[] productNames) {
            Assert.AreEqual(cents, delivery.Item1);
            Assert.IsTrue(productNames.SequenceEqual(delivery.Item2));
        }

        public static void CheckUnload(Tuple<int, int, string[]> contents, int centsRemaining, int centsInStorage, string[] productNames) {
            Assert.AreEqual(centsRemaining, contents.Item1);
            Assert.AreEqual(centsInStorage, contents.Item2);
            Assert.IsTrue(productNames.SequenceEqual(contents.Item3));
        }
    }
}
