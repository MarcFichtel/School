using System;
using System.Collections.Generic;
using System.Linq;

namespace Frontend2.Hardware {

    /**
    * Represents a standard configuration of the vending machine hardware:
    * 
    *   - one coin slot;
    *   - one coin receptacle (called the coin receptacle) to temporarily store
    * coins entered by the user;
    *   - one coin receptacle (called the storage bin) to store coins that have
    * been accepted as payment;
    *   - a set of one or more coin racks (the number and the denomination of coins
    * stored by each is specified in the constructor);
    *   - one delivery chute used to deliver pop cans and to return coins;
    *   - a set of one or more pop can racks (the number, cost, and pop name stored
    * in each is specified in the constructor);
    *   - one textual display;
    *   - a set of one or more selection buttons (exactly one per pop can rack);
    * and
    *   - two indicator lights: one to indicate that exact change should be used by
    * the user; the other to indicate that the machine is out of order.
    *
    * The component devices are interconnected as follows:
    * 
    *   - the output of the coin slot is connected to the input of the coin
    * receptacle;
    *   - the outputs of the coin receptacle are connected to the inputs of the
    * coin racks (for valid coins to be stored therein), the delivery chute (for
    * invalid coins or other coins to be returned), and the storage bin (for coins
    * to be accepted that do not fit in the coin racks);
    *   - the output of each coin rack is connected to the delivery chute; and
    *   - the output of each pop can rack is connected to the delivery chute.
    * 
    * 
    * Each component device can be disabled to prevent any physical movements.
    * Other functionality is not affected by disabling a device; hence devices that
    * do not involve physical movements are not affected by "disabling" them.
    * 
    * Most component devices have some sort of maximum capacity (e.g., of the
    * number of pop cans that can be stored therein). In some cases, this is a
    * simplification of the physical reality for the sake of simulation.
    */
    public class VendingMachine {
        public bool SafetyOn { get; protected set; }

        private int[] coinKinds;
        
        private Dictionary<int, CoinChannel> coinRackChannels;
        
        public int[] PopCanCosts { get; protected set; }
        public string[] PopCanNames { get; protected set; }

        public CoinRack[] CoinRacks { get; protected set; }
        public PopCanRack[] PopCanRacks { get; protected set; }

        public CoinSlot CoinSlot { get; protected set; }
        public CoinReceptacle CoinReceptacle { get; protected set; }
        public CoinReceptacle StorageBin { get; protected set; }
        public DeliveryChute DeliveryChute { get; protected set; }
        public Display Display { get; protected set; }
        public SelectionButton[] SelectionButtons { get; protected set; }

        public IndicatorLight ExactChangeLight { get; protected set; }
        public IndicatorLight OutOfOrderLight { get; protected set; }

        /**
        * Creates a standard arrangement for the vending machine. All the
        * components are created and interconnected. The machine is initially
        * empty. The pop kind names and costs are initialized to "&lt;default&gt;"
        * and 1 respectively.
        * 
        * All pop kinds
        * 
        * @param coinKinds
        *            The values (in cents) of each kind of coin. The order of the
        *            kinds is maintained. One coin rack is produced for each kind.
        *            Each kind must have a unique, positive value.
        * @param selectionButtonCount
        *            The number of selection buttons on the machine. Must be
        *            positive.
        * @param coinRackCapacity
        *            The maximum capacity of each coin rack in the machine. Must be
        *            positive.
        * @param popCanRackCapacity
        *            The maximum capacity of each pop can rack in the machine. Must
        *            be positive.
        * @param receptacleCapacity
        *            The maximum capacity of the coin receptacle, storage bin, and
        *            delivery chute. Must be positive.
        */
        public VendingMachine(int[] coinKinds, int selectionButtonCount, int coinRackCapacity, int popCanRackCapacity, int receptacleCapacity) {

            if(coinKinds == null) {
                throw new Exception("Arguments may not be null");
            }

            if(selectionButtonCount < 1 || coinRackCapacity < 1 || popCanRackCapacity < 1) {
                throw new Exception("Counts and capacities must be positive");
            }

            if(coinKinds.Length < 1) {
                throw new Exception("At least one coin kind must be accepted");
            }

            this.coinKinds = coinKinds;

            var coinKindsSet = new HashSet<int>(coinKinds);
            if (coinKindsSet.Count != this.coinKinds.Length) {
                throw new Exception("Coin kinds must have unique values");
            }
            if (coinKindsSet.Where(ck => ck < 1).Count() > 0) {
                throw new Exception("Coin kind must have a positive value");
            }
            
            this.Display = new Display();
            this.CoinSlot = new CoinSlot(this.coinKinds);
            this.CoinReceptacle = new CoinReceptacle(receptacleCapacity);
            this.StorageBin = new CoinReceptacle(receptacleCapacity);
            this.DeliveryChute = new DeliveryChute(receptacleCapacity);
            this.CoinRacks = new CoinRack[this.coinKinds.Length];
            this.coinRackChannels = new Dictionary<int, CoinChannel>();
            for(int i = 0; i < this.coinKinds.Length; i++) {
                this.CoinRacks[i] = new CoinRack(coinRackCapacity);
                this.CoinRacks[i].Connect(new CoinChannel(this.DeliveryChute));
                this.coinRackChannels[this.coinKinds[i]] = new CoinChannel(CoinRacks[i]);
            }

            this.PopCanRacks = new PopCanRack[selectionButtonCount];
            for(int i = 0; i < selectionButtonCount; i++) {
                this.PopCanRacks[i] = new PopCanRack(popCanRackCapacity);
                this.PopCanRacks[i].Connect(new PopCanChannel(DeliveryChute));
            }

            this.PopCanNames = new string[selectionButtonCount];
            for(int i = 0; i < selectionButtonCount; i++) {
                this.PopCanNames[i] = "<default>";
            }
            this.PopCanCosts = new int[selectionButtonCount];
            for(int i = 0; i < selectionButtonCount; i++) {
                this.PopCanCosts[i] = 1;
            }

            this.SelectionButtons = new SelectionButton[selectionButtonCount];
            for(int i = 0; i < selectionButtonCount; i++) {
                this.SelectionButtons[i] = new SelectionButton();
            }

            this.CoinSlot.Connect(new CoinChannel(this.CoinReceptacle), new CoinChannel(this.DeliveryChute));
            this.CoinReceptacle.Connect(this.coinRackChannels, new CoinChannel(this.DeliveryChute), new CoinChannel(this.StorageBin));

            this.ExactChangeLight = new IndicatorLight();
            this.OutOfOrderLight = new IndicatorLight();

            this.SafetyOn = false;
        }

        /**
        * Configures the hardware to use a set of names and costs for pop cans.
        * 
        * @param popCanNames
        *            A list of names for pop cans, each position of which will
        *            correspond to a selection button. No name can be null or
        *            empty.
        * @param popCanCosts
        *            A list of costs for pop cans, each position of which will
        *            correspond to a selection button. No cost can be non-positive.
        */
        public void Configure(List<String> popCanNames, List<int> popCanCosts) {
            if(popCanNames.Count != this.PopCanNames.Length || popCanCosts.Count != this.PopCanCosts.Length) {
                throw new Exception("The number of names and costs must be identical to the number of pop can racks in the machine");
            }
            
            if (popCanNames.Where(pcn => pcn == "").Count() > 0) {
                throw new Exception("Pop can names cannot be an empty string");
            }
            
            if (popCanCosts.Where(pc => pc < 1).Count() > 0) {
                throw new Exception("Pop can costs cannot be less than 1");
            }

            this.PopCanNames = popCanNames.ToArray();
            this.PopCanCosts = popCanCosts.ToArray();
        }

        /**
        * Disables all the components of the hardware that involve physical
        * movements. Activates the out of order light.
        */
        public void EnableSafety() {
            this.SafetyOn = true;
            this.CoinSlot.Disable();
            this.DeliveryChute.Disable();

            foreach(var popCanRack in this.PopCanRacks) {
                popCanRack.Disable();
            }

            foreach(var coinRack in this.CoinRacks) {
                coinRack.Disable();
            }

            this.OutOfOrderLight.Activate();
        }

        /**
        * Enables all the components of the hardware that involve physical
        * movements. Deactivates the out of order light.
        */
        public void DisableSafety() {
            this.SafetyOn = false;
            this.CoinSlot.Enable();
            this.DeliveryChute.Enable();

            foreach(var popCanRack in this.PopCanRacks) {
                popCanRack.Enable();
            }
            foreach(var coinRack in this.CoinRacks) {
                coinRack.Enable();
            }

            this.OutOfOrderLight.Deactivate();
        }

        /**
        * Accesses the coin rack that handles coins of the specified kind. If none
        * exists, null is returned.
        * 
        * @param kind
        *            The value of the coin kind for which the rack is sought.
        * @return The relevant device.
        */
        public CoinRack GetCoinRackForCoinKind(int kind) {
            var cc = this.coinRackChannels[kind];
            if(cc != null) {
                return (CoinRack)cc.Sink;
            }
            return null;
        }

        /**
        * Accesses a coin kind that corresponds to a coin rack at the specified
        * index.
        * 
        * @param index
        *            The index of the coin rack.
        * @return The coin kind at the specified index.
        */
        public int GetCoinKindForCoinRack(int index) {
            return this.coinKinds[index];
        }


        /**
        * A convenience method for constructing and loading a set of pop cans into
        * the machine.
        * 
        * @param popCanCounts
        *            A list representing the number of pops to create and load into
        *            the corresponding rack.
        */
        public void LoadPopCans(int[] popCanCounts) {
            if (popCanCounts.Length != this.PopCanRacks.Length) {
                throw new Exception("Pop can counts must equal number of racks");
            }
            if (popCanCounts.Where(pcc => pcc < 0).Count() > 0) {
                throw new Exception("Each count must not be negative");
            }
            for (int i = 0; i < popCanCounts.Length; i++) {
                var popCanCount = popCanCounts[i];
                var pcr = this.PopCanRacks[i];
                var name = this.PopCanNames[i];
                var popCans = new List<PopCan>();
                for (int j = 0; j < popCanCount; j++) {
                    popCans.Add(new PopCan(name));
                }
                pcr.LoadPops(popCans);
            }
        }

        /**
        * A convenience method for constructing and loading a set of coins into the
        * machine.
        * 
        * @param coinCounts
        *            A variadic list of ints each representing the number of coins
        *            to create and load into the corresponding rack.
        */
        public void LoadCoins(int[] coinCounts) {
            if (coinCounts.Length != this.CoinRacks.Length) {
                throw new Exception("Coin counts have to equal number of racks.");
            }
            if (coinCounts.Where(cc => cc < 0).Count() > 0) {
                throw new Exception("Each count must not be negative");
            }
            for (int i = 0; i < coinCounts.Length; i++) {
                var coinCount = coinCounts[i];
                var coinValue = this.GetCoinKindForCoinRack(i);
                var coinRack = this.CoinRacks[i];
                var coins = new List<Coin>();
                for (int j = 0; j < coinCount; j++) {
                    coins.Add(new Coin(coinValue));
                }
                coinRack.LoadCoins(coins);
            }
        }
    }
}