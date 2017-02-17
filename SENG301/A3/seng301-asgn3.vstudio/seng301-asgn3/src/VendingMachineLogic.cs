using System;
using System.Collections.Generic;
using System.Linq;
using Frontend2;
using Frontend2.Hardware;

public class VendingMachineLogic {

    VendingMachine vm;
    Dictionary<SelectionButton, int> selectionButtonToIndex;
    Dictionary<int, int> coinKindToCoinRackIndex;
    int availableFunds;

    public VendingMachineLogic(VendingMachine vm) {
        this.vm = vm;
        this.availableFunds = 0;
        this.vm.CoinSlot.CoinAccepted += new EventHandler<CoinEventArgs>(CoinSlot_CoinAccepted);

        this.selectionButtonToIndex = new Dictionary<SelectionButton, int>();
        for (int i = 0; i < this.vm.SelectionButtons.Length; i++) {
            this.vm.SelectionButtons[i].Pressed += new EventHandler(SelectionButton_Pressed);
            this.selectionButtonToIndex[this.vm.SelectionButtons[i]] = i;
        }

        this.coinKindToCoinRackIndex = new Dictionary<int, int>();
        for (int i = 0; i < this.vm.CoinRacks.Length; i++) {
            this.coinKindToCoinRackIndex[this.vm.GetCoinKindForCoinRack(i)] = i;
        }
    }

    void CoinSlot_CoinAccepted(object sender, CoinEventArgs e) {
        this.availableFunds += e.Coin.Value;
    }

    void SelectionButton_Pressed(object sender, EventArgs e) {
        var index = this.selectionButtonToIndex[(SelectionButton) sender];
        var popName = this.vm.PopCanNames[index];
        var popCost = this.vm.PopCanCosts[index];
        //Console.WriteLine("Button {0} pressed for {1}, cost is {2}", index, popName, popCost);
        if (this.availableFunds >= popCost) {
            var popCanRack = this.vm.PopCanRacks[index];
            popCanRack.DispensePopCan();
            this.vm.CoinReceptacle.StoreCoins();
            this.availableFunds = this.deliverChange(popCost, this.availableFunds);
        }
        else {
            this.vm.Display.DisplayMessage("Cost for " + popName + ": " + popCost + "; Available funds: " + this.availableFunds);
        }
    }

    int deliverChange(int cost, int availableFunds) {
        var changeNeeded = availableFunds - cost;

        while (changeNeeded > 0) {
            var coinRacksWithMoney = this.coinKindToCoinRackIndex.Where(ck => ck.Key <= changeNeeded && this.vm.CoinRacks[ck.Value].Count > 0).OrderByDescending(ck => ck.Key);

            if (coinRacksWithMoney.Count() == 0) {
                return changeNeeded; // this is what's left as available funds
            }

            var biggestCoinRackCoinKind = coinRacksWithMoney.First().Key;
            var biggestCoinRackIndex = coinRacksWithMoney.First().Value;
            var biggestCoinRack = this.vm.CoinRacks[biggestCoinRackIndex];

            changeNeeded = changeNeeded - biggestCoinRackCoinKind;
            biggestCoinRack.ReleaseCoin();
        }

        return 0;
    }


        // var money = this.paidMoney();
        // var coinsInChute = new List<Coin>();
        // var changeNeeded = money - cost;

        // while (changeNeeded > 0) {
        //     var hoppersWithMoney = this.coinKindToHopperIndex.Where(h => h.Key <= changeNeeded && this.coinHopper[h.Value].Count > 0).OrderByDescending(h => h.Key);

        //     if (hoppersWithMoney.Count() == 0) {
        //         //Console.WriteLine("Woohoo! Customer is screwed out of " + changeNeeded + " pennies!");
        //         return coinsInChute;
        //     }

        //     var biggestHopper = hoppersWithMoney.First();

        //     changeNeeded = changeNeeded - biggestHopper.Key;            
        //     var coin = this.coinHopper[biggestHopper.Value][0];
        //     this.coinHopper[biggestHopper.Value].Remove(coin);
        //     coinsInChute.Add(coin);
        // }

        // return coinsInChute;
}