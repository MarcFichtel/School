using System;
using System.Collections.Generic;


namespace Frontend2.Hardware {
    /**
    * Represents a simple delivery chute device. The delivery chute has a finite
    * capacity of objects (pop cans or coins) that it can hold. This is obviously
    * not a realistic element of the simulation, but sufficient here.
    */
    public class DeliveryChute : AbstractHardware, IDeliveryChute, ICoinAcceptor, IPopCanAcceptor {

        private List<IDeliverable> chute;

        public int Capacity { get; protected set; }
        public int Count {
            get {
                return this.chute.Count;
            }
        }
        public bool HasSpace {
            get {
                return (this.Capacity > this.Count);
            }
        }

        public event EventHandler<DeliverableEventArgs> ItemDelivered;
        public event EventHandler ChuteDoorOpened;
        public event EventHandler ChuteDoorClosed;
        public event EventHandler ChuteFull;

        /**
        * Creates a delivery cute with the indicated maximum capacity of pop cans
        * and/or coins.
        * 
        * @param capacity
        *            The maximum number of items that the delivery chute can
        *            contain. Must be positive.
        */
        public DeliveryChute(int capacity) {
            if(capacity <= 0) {
                throw new Exception("Capacity must be a positive value: " + capacity);
            }

            this.Capacity = capacity;
            this.chute = new List<IDeliverable>();
        }

        /**
        * Tells this delivery chute to deliver the indicated pop can. If the
        * delivery is successful, an "ItemDelivered" event is announced to its
        * listeners. If the successful delivery causes the chute to become full, a
        * "ChuteFull" event is announced to its listeners.
        */
        public void AcceptPopCan(PopCan popCan) {
            if (! this.Enabled) {
                throw new Exception("Delivery chute disabled");
            }
            
            if (this.Count >= this.Capacity) {
                throw new Exception("Delivery chute already full - cannot accept new pop can");
            }

            this.chute.Add(popCan);

            if (this.ItemDelivered != null) {
                this.ItemDelivered(this, new DeliverableEventArgs() { Item = popCan });
            }

            if (this.Count >= this.Capacity) {
                if (this.ChuteFull != null) {
                    this.ChuteFull(this, new EventArgs());
                }
            }
        }

        /**
        * Tells this delivery chute to deliver the indicated coin. If the delivery
        * is successful, an "ItemDelivered" event is announced to its listeners. If
        * the successful delivery causes the chute to become full, a "ChuteFull"
        * event is announced to its listeners.
        */
        public void AcceptCoin(Coin coin) {
           if (! this.Enabled) {
                throw new Exception("Delivery chute disabled");
            }
            
            if (this.Count >= this.Capacity) {
                throw new Exception("Delivery chute already full - cannot accept new pop can");
            }

            this.chute.Add(coin);

            if (this.ItemDelivered != null) {
                this.ItemDelivered(this, new DeliverableEventArgs() { Item = coin });
            }

            if (this.Count >= this.Capacity) {
                if (this.ChuteFull != null) {
                    this.ChuteFull(this, new EventArgs());
                }
            }
        }

        /**
        * Simulates the opening of the door of the delivery chute and the removal
        * of all items therein. Announces a "ChuteDoorOpened" event to its listeners
        * before the items are removed, and a "ChuteDoorClosed" event after the items
        * are removed. Disabling the chute does not prevent this.
        * 
        * @return The items that were in the delivery chute.
        */
        public IDeliverable[] RemoveItems() {
            if (this.ChuteDoorOpened != null) {
                this.ChuteDoorOpened(this, new EventArgs());
            }
            var items = this.chute.ToArray();
            this.chute.Clear();
            
            if (this.ChuteDoorClosed != null) {
                this.ChuteDoorClosed(this, new EventArgs());
            }
            return items;
        }
    }
}