using System;
using PerCederberg.Grammatica.Runtime;
using Frontend2;
using Frontend2.Parser;
using System.Collections;
using System.Collections.Generic;

/**
    * <remarks>A class providing callback methods for the
    * parser.</remarks>
    */

namespace Frontend2.Parser {
    public class SENG301VMAnalyzer : VendingMachineAnalyzer {

        private IVendingMachineFactory vm;
        private List<IDeliverable> extraction;
        private VendingMachineStoredContents teardown;

        public void RegisterVendingMachineFactory(IVendingMachineFactory vm) {
            this.vm = vm;
            this.extraction = new List<IDeliverable>();
            this.teardown = new VendingMachineStoredContents();
        }

        private void announceCreate(List<int> coinKinds, int selectionButtonCount, int coinRackCapacity, int popRackCapacity, int receptacleCapacity) {
            vm.CreateVendingMachine(coinKinds, selectionButtonCount, coinRackCapacity, popRackCapacity, receptacleCapacity);
        }

        private void announceConfigure(int vmIndex, List<string> popNames, List<int> popCosts) {
            vm.ConfigureVendingMachine(vmIndex, popNames, popCosts);
        }

        private void announceCoinLoad(int vmIndex, int coinKindIndex, List<Coin> coins) {
            vm.LoadCoins(vmIndex, coinKindIndex, coins);
        }

        private void announcePopLoad(int vmIndex, int popKindIndex, List<PopCan> pops) {
            vm.LoadPops(vmIndex, popKindIndex, pops);
        }

        private void announceUnload(int vmIndex) {
            this.teardown = vm.UnloadVendingMachine(vmIndex);
        }

        private void announceExtract(int vmIndex) {
            this.extraction.Clear();
            this.extraction.AddRange(vm.ExtractFromDeliveryChute(vmIndex));
        }

        private void announcePress(int vmIndex, int index) {
            vm.PressButton(vmIndex, index);
        }

        private void announceInsert(int vmIndex, Coin coin) {
            vm.InsertCoin(vmIndex, coin);
        }

        private bool CheckTeardown(int totalChangeRemaining, int totalCoinsUsed, List<PopCan> popsRemaining) {
            var result = true;
            var coinsInCoinRacks = this.teardown.CoinsInCoinRacks;
            var coinsUsedForPayment = this.teardown.PaymentCoinsInStorageBin;
            var unsoldPopCanRacks = this.teardown.PopCansInPopCanRacks;

            foreach (var rack in coinsInCoinRacks) {
                foreach (var coin in rack) {
                    totalChangeRemaining -= ((Coin) coin).Value;
                }
            }
            foreach (var coin in coinsUsedForPayment) {
                totalCoinsUsed -= ((Coin) coin).Value;
            }
            if (! ((totalChangeRemaining == 0) && (totalCoinsUsed == 0))) {
                result = false;
            }
            else {
                foreach (var popCanRack in unsoldPopCanRacks) {
                    foreach (var popCan in popCanRack) {
                        if (popsRemaining.Contains((PopCan) popCan)) {
                            popsRemaining.Remove((PopCan) popCan);
                        }
                        else {
                            result = false;
                            break;
                        }
                    }
                }
                if (popsRemaining.Count > 0) {
                    result = false;
                }
            }
            return result;
        }

        private bool CheckDelivery(int totalCoinValue, List<PopCan> popsDelivered) {
            var result = true;
            foreach (var item in this.extraction) {
                if (item is Coin) {
                    totalCoinValue -= ((Coin) item).Value;
                }
                else if (item is PopCan) {
                    if (popsDelivered.Contains((PopCan) item)) {
                        popsDelivered.Remove((PopCan) item);
                    }
                    else {
                        result = false;
                        break;
                    }
                }
            }
            if (! ((totalCoinValue == 0) && (popsDelivered.Count == 0))) {
                result = false;
            }
            return result;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitNumber(Token node) {
            node.AddValue(Int32.Parse(node.GetImage()));
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitString(Token node) {
            node.AddValue(node.GetImage());
            return node;
        }

        public override Node ExitPopString(Production node) {
            node.AddValue(node.GetChildAt(1).GetValue(0));
            return node;
        }
        
        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitVmselection(Production node) {
            node.AddValue((int)node.GetChildAt(1).GetValue(0));
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitCreate(Production node) {
            // Create = CREATE_COMMAND "(" NUMBER {"," NUMBER} ";" NUMBER ";" NUMBER ";" NUMBER ";" NUMBER ")" ;
            // int coinRackCapacity, int popRackCapacity, int receptacleCapacity
            var count = node.Count;
            var selectionButtonCount = (int) node[count - 8].GetValue(0);
            var coinRackCapacity = (int) node[count - 6].GetValue(0);
            var popRackCapacity = (int) node[count - 4].GetValue(0);
            var receptacleCapacity = (int) node[count - 2].GetValue(0);
            var coinKinds = new List<int>();
            foreach (var value in this.GetChildValues(node)) {
                coinKinds.Add((int) value);
            }
            coinKinds.RemoveRange(coinKinds.Count - 4, 4);

            this.announceCreate(coinKinds, selectionButtonCount, coinRackCapacity, popRackCapacity, receptacleCapacity);
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitConfigure(Production node) {
            var values = this.GetChildValues(node);
            var vmIndex = Int32.Parse(values[0].ToString());
            var popNames = new List<string>();
            var popCosts = new List<int>();
            for(int i = 1; i < values.Count; i+=2) {
                popNames.Add((string)values[i]);
            }
            for (int i = 2; i < values.Count; i+=2) {
                popCosts.Add((int)values[i]);
            }
            
            this.announceConfigure(vmIndex, popNames, popCosts);

            return node;
        }


        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitCoinLoad(Production node) {
            var values = this.GetChildValues(node);
            var vmIndex = (int) values[0];
            var coinKindIndex = (int) values[1];
            var coinValue = (int) values[2];
            var coinCount = (int) values[3];
            var coins = new List<Coin>();
            for (int i = 0; i < coinCount; i++) {
                coins.Add(new Coin(coinValue));
            }

            this.announceCoinLoad(vmIndex, coinKindIndex, coins);
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitPopLoad(Production node) {
            var values = this.GetChildValues(node);
            var vmIndex = (int) values[0];
            var popKindIndex = (int) values[1];
            var popKind = (string) values[2];
            var popCount = (int) values[3];
            var pops = new List<PopCan>();
            for (int i = 0; i < popCount; i++) {
                pops.Add(new PopCan(popKind));
            }

            this.announcePopLoad(vmIndex, popKindIndex, pops);
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitUnload(Production node) {
            var values = this.GetChildValues(node);
            var vmIndex = (int) values[0];
            
            this.announceUnload(vmIndex);
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitExtract(Production node) {
            var values = this.GetChildValues(node);
            var vmIndex = (int) values[0];
            
            this.announceExtract(vmIndex);
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitInsert(Production node) {
            var values = this.GetChildValues(node);
            var vmIndex = (int) values[0];
            var coinValue = (int) values[1];
            
            this.announceInsert(vmIndex, new Coin(coinValue));
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitPress(Production node) {
            var values = this.GetChildValues(node);
            var vmIndex = (int) values[0];
            var buttonIndex = (int) values[1];        

            this.announcePress(vmIndex, buttonIndex);
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitCheckDelivery(Production node) {
            var values = this.GetChildValues(node);
            var totalCoinValue = (int) values[0];
            var pops = new List<PopCan>();
            for (int i = 1; i< values.Count; i++) {
                pops.Add(new PopCan((string) values[i]));
            }

            if (!this.CheckDelivery(totalCoinValue, pops)) {
                throw new Exception("CHECK_DELIVERY: FAIL");
            }
            return node;
        }

        /**
            * <summary>Called when exiting a parse tree node.</summary>
            *
            * <param name='node'>the node being exited</param>
            *
            * <returns>the node to add to the parse tree, or
            *          null if no parse tree should be created</returns>
            *
            * <exception cref='ParseException'>if the node analysis
            * discovered errors</exception>
            */
        public override Node ExitCheckTeardown(Production node) {
            var values = this.GetChildValues(node);
            var totalChangeRemaining = (int) values[0];
            var totalCoinsUsed = (int) values[1];
            var pops = new List<PopCan>();
            for (int i = 2; i< values.Count; i++) {
                pops.Add(new PopCan((string) values[i]));
            }

            if (!this.CheckTeardown(totalChangeRemaining, totalCoinsUsed, pops)) {
                throw new Exception("CHECK_TEARDOWN: FAIL");
            }
            return node;
        }
    }

}