using System;
using System.Collections.Generic;
using Frontend2;
using Frontend2.Hardware;

public class VendingMachineFactory : IVendingMachineFactory {

    List<VendingMachine> vendingMachines;

    public VendingMachineFactory() {
        this.vendingMachines = new List<VendingMachine>();
    }

    public int CreateVendingMachine(List<int> coinKinds, int selectionButtonCount, int coinRackCapacity, int popRackCapcity, int receptacleCapacity) {
        var coinKindArray = coinKinds.ToArray();
        var vm = new VendingMachine(coinKindArray, selectionButtonCount, coinRackCapacity, popRackCapcity, receptacleCapacity);
        this.vendingMachines.Add(vm);
        new VendingMachineLogic(vm);
        return this.vendingMachines.Count - 1;
    }

    public void ConfigureVendingMachine(int vmIndex, List<string> popNames, List<int> popCosts) {
        var vm = this.vendingMachines[vmIndex];
        vm.Configure(popNames, popCosts);
    }

    public void LoadCoins(int vmIndex, int coinKindIndex, List<Coin> coins) {
        this.vendingMachines[vmIndex].CoinRacks[coinKindIndex].LoadCoins(coins);
    }

    public void LoadPops(int vmIndex, int popKindIndex, List<PopCan> pops) {
        this.vendingMachines[vmIndex].PopCanRacks[popKindIndex].LoadPops(pops);
    }

    public void InsertCoin(int vmIndex, Coin coin) {
        this.vendingMachines[vmIndex].CoinSlot.AddCoin(coin);
    }

    public void PressButton(int vmIndex, int value) {
        this.vendingMachines[vmIndex].SelectionButtons[value].Press();
    }

    public List<IDeliverable> ExtractFromDeliveryChute(int vmIndex) {
        var vm = this.vendingMachines[vmIndex];
        var items = vm.DeliveryChute.RemoveItems();
        var itemsAsList = new List<IDeliverable>(items);

        return itemsAsList;
    }

    public VendingMachineStoredContents UnloadVendingMachine(int vmIndex) {
        var storedContents = new VendingMachineStoredContents();
        var vm = this.vendingMachines[vmIndex];

        foreach(var coinRack in vm.CoinRacks) {
            storedContents.CoinsInCoinRacks.Add(coinRack.Unload());
        }
        storedContents.PaymentCoinsInStorageBin.AddRange(vm.StorageBin.Unload());
        foreach(var popCanRack in vm.PopCanRacks) {
            storedContents.PopCansInPopCanRacks.Add(popCanRack.Unload());
        }
                
        return storedContents;
    }
}