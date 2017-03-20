using System;
using System.Collections.Generic;
using System.Linq;

namespace Frontend4.Hardware {

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
    *   - one delivery chute used to deliver products and to return coins;
    *   - a set of one or more product racks (the number, cost, and product name stored
    * in each is specified in the constructor);
    *   - one textual display;
    *   - a set of one or more selection buttons (exactly one per product rack);
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
    *   - the output of each product rack is connected to the delivery chute.
    * 
    * 
    * Each component device can be disabled to prevent any physical movements.
    * Other functionality is not affected by disabling a device; hence devices that
    * do not involve physical movements are not affected by "disabling" them.
    * 
    * Most component devices have some sort of maximum capacity (e.g., of the
    * number of products that can be stored therein). In some cases, this is a
    * simplification of the physical reality for the sake of simulation.
    */
    public class HardwareFacade {
        public bool SafetyOn { get; protected set; }

        private Cents[] coinKinds;
        
        private Dictionary<Cents, CoinChannel> coinRackChannels;
        
        public ProductKind[] ProductKinds { get; protected set; }

        public CoinRack[] CoinRacks { get; protected set; }
        public ProductRack[] ProductRacks { get; protected set; }

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
        * empty. The product kind names and costs are initialized to "&lt;default&gt;"
        * and 1 respectively.
        * 
        * All product kinds
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
        * @param ProductRackCapacity
        *            The maximum capacity of each product rack in the machine. Must
        *            be positive.
        * @param receptacleCapacity
        *            The maximum capacity of the coin receptacle, storage bin, and
        *            delivery chute. Must be positive.
        */
        public HardwareFacade(Cents[] coinKinds, int selectionButtonCount, int coinRackCapacity, int productRackCapacity, int receptacleCapacity) {

            if(coinKinds == null) {
                throw new Exception("Arguments may not be null");
            }

            if(selectionButtonCount < 1 || coinRackCapacity < 1 || productRackCapacity < 1) {
                throw new Exception("Counts and capacities must be positive");
            }

            if(coinKinds.Length < 1) {
                throw new Exception("At least one coin kind must be accepted");
            }

            this.coinKinds = coinKinds;

            var coinKindsSet = new HashSet<Cents>(coinKinds);
            if (coinKindsSet.Count != this.coinKinds.Length) {
                throw new Exception("Coin kinds must have unique values");
            }
            if (coinKindsSet.Where(ck => ck.Value < 1).Count() > 0) {
                throw new Exception("Coin kind must have a positive value");
            }
            
            this.Display = new Display();
            this.CoinSlot = new CoinSlot(this.coinKinds);
            this.CoinReceptacle = new CoinReceptacle(receptacleCapacity);
            this.StorageBin = new CoinReceptacle(receptacleCapacity);
            this.DeliveryChute = new DeliveryChute(receptacleCapacity);
            this.CoinRacks = new CoinRack[this.coinKinds.Length];
            this.coinRackChannels = new Dictionary<Cents, CoinChannel>();
            for(int i = 0; i < this.coinKinds.Length; i++) {
                this.CoinRacks[i] = new CoinRack(coinRackCapacity);
                this.CoinRacks[i].Connect(new CoinChannel(this.DeliveryChute));
                this.coinRackChannels[this.coinKinds[i]] = new CoinChannel(CoinRacks[i]);
            }

            this.ProductRacks = new ProductRack[selectionButtonCount];
            for(int i = 0; i < selectionButtonCount; i++) {
                this.ProductRacks[i] = new ProductRack(productRackCapacity);
                this.ProductRacks[i].Connect(new ProductChannel(DeliveryChute));
            }

            this.ProductKinds = new ProductKind[selectionButtonCount];
 
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
        * Configures the hardware to use a set of names and costs for products.
        * 
        * @param kinds
        *            A list of product kinds, each position of which will
        *            correspond to a selection button. No kind object can be null.
        *            The same kind can be used for more than one position.
        */
        public void Configure(List<ProductKind> productKinds) {
            if(productKinds.Count != this.SelectionButtons.Length) {
                throw new ArgumentException("The number of product kinds must be equal to the number of selection buttons");
            }

            if (productKinds.Where(pk => pk.Name == null).Count() > 0) {
                throw new ArgumentNullException("No product kind may be null");
            }

            this.ProductKinds = productKinds.ToArray();
        }

        /**
        * Disables all the components of the hardware that involve physical
        * movements. Activates the out of order light.
        */
        public void EnableSafety() {
            this.SafetyOn = true;
            this.CoinSlot.Disable();
            this.DeliveryChute.Disable();

            foreach(var productRack in this.ProductRacks) {
                productRack.Disable();
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

            foreach(var productRack in this.ProductRacks) {
                productRack.Enable();
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
        public CoinRack GetCoinRackForCoinKind(Cents kind) {
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
        public Cents GetCoinKindForCoinRack(int index) {
            return this.coinKinds[index];
        }


        /**
        * A convenience method for constructing and loading a set of products into
        * the machine.
        * 
        * @param productCounts
        *            A list representing the number of products to create and load into
        *            the corresponding rack.
        */
        public void LoadProducts(int[] productCounts) {
            if (productCounts.Length != this.ProductRacks.Length) {
                throw new Exception("Product counts must equal number of racks");
            }
            if (productCounts.Where(pcc => pcc < 0).Count() > 0) {
                throw new Exception("Each count must not be negative");
            }
            for (int i = 0; i < productCounts.Length; i++) {
                var productCount = productCounts[i];
                var pcr = this.ProductRacks[i];
                var name = this.ProductKinds[i].Name;
                var products = new List<Product>();
                for (int j = 0; j < productCount; j++) {
                    products.Add(new Product(name));
                }
                pcr.LoadProducts(products);
            }
        }

        /**
        * A convenience method for constructing and loading a set of coins into the
        * machine.
        * 
        * @param coinCounts
        *            A list of ints each representing the number of coins
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