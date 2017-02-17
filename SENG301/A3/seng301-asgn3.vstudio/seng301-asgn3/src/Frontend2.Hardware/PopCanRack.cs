using System;
using System.Collections.Generic;

namespace Frontend2.Hardware {

    /**
    * Represents a storage rack for pop cans within the vending machine. More than
    * one would typically exist within the same vending machine. The pop can rack
    * has finite, positive capacity. A pop can rack can be disabled, which prevents
    * it from dispensing pop cans.
    */
    public class PopCanRack : AbstractHardware, IPopCanRack {

        public int Capacity { get; protected set; }
        public int Count {
            get {
                return this.queue.Count;
            }
        }

        public event EventHandler<PopCanEventArgs> PopCanAdded;
        public event EventHandler<PopCanEventArgs> PopCanRemoved;
        public event EventHandler PopCanRackFull;
        public event EventHandler PopCanRackEmpty;
        public event EventHandler<PopCanEventArgs> PopCansLoaded;
        public event EventHandler<PopCanEventArgs> PopCansUnloaded;

        private PopCanChannel sink;
        private Queue<PopCan> queue;
        
        /**
        * Creates a new pop can rack with the indicated maximum capacity. The pop
        * can rack initially is empty.
        * 
        * @param capacity
        *            Positive integer indicating the maximum capacity of the rack.
        */
        public PopCanRack(int capacity) {
            if(capacity <= 0) {
                throw new Exception("Capacity cannot be non-positive: " + capacity);
            }

            this.Capacity = capacity;
            this.queue = new Queue<PopCan>();
        }


        /**
        * Connects the pop can rack to an outlet channel, such as the delivery
        * chute. Causes no events.
        * 
        * @param sink
        *            The channel to be used as the outlet for dispensed pop cans.
        */
        public void Connect(PopCanChannel sink) {
            this.sink = sink;
        }

        /**
        * Adds the indicated pop can to this pop can rack if there is sufficient
        * space available. If the pop can is successfully added to this pop can
        * rack, a "PopCanAdded" event is announced to its listeners. If, as a result
        * of adding this pop can, this pop can rack has become full, a "PopCanRackFull"
        * event is announced to its listeners.
        * 
        * @param popCan
        *            The pop can to be added.
        */
        public void AddPopCan(PopCan popCan) {
            if (! this.Enabled) {
                throw new Exception("Pop can rackdisabled");
            }
            
            if (this.Count >= this.Capacity) {
                throw new Exception("Pop can rack already full - cannot accept new pop can");
            }

            this.queue.Enqueue(popCan);
            
            if (this.PopCanAdded != null) {
                this.PopCanAdded(this, new PopCanEventArgs() { PopCan = popCan });
            }

            if (this.Count >= this.Capacity) {
                if (this.PopCanRackFull != null) {
                    this.PopCanRackFull(this, new EventArgs());
                }
            }
        }

        /**
        * Causes one pop can to be removed from this pop can rack, to be placed in
        * the output channel to which this pop can rack is connected. If a pop can
        * is removed from this pop can rack, a "PopCanRemoved" event is announced to
        * its listeners. If the removal of the pop can causes this pop can rack to
        * become empty, a "PopCanRackEmpty" event is announced to its listeners.
        * 
        */
        public void DispensePopCan() {
            if (! this.Enabled) {
                throw new Exception("Pop can rackdisabled");
            }
            if (this.Count == 0) {
                throw new Exception("No popcans in the popcan rack!");
            }

            var popCan = this.queue.Dequeue();

            if (this.PopCanRemoved != null) {
                this.PopCanRemoved(this, new PopCanEventArgs() { PopCan = popCan });
            }

            if(this.sink == null) {
                throw new Exception("The output channel is not connected");
            }

            this.sink.AcceptPopCan(popCan);

            if (this.Count == 0) {
                if (this.PopCanRackEmpty != null) {
                    this.PopCanRackEmpty(this, new EventArgs());
                }
            }
        }

        /**
        * Allows pop cans to be loaded into the pop can rack, to simulate direct,
        * physical loading. Note that any existing pop cans in the rack are not
        * removed. Causes a "PopCansLoaded" event to be announced.
        * 
        * @param popCans
        *            One or more pop cans to be loaded into this pop can rack.
        */
        public void LoadPops(List<PopCan> popCans) {
            if (this.Capacity < this.Count + popCans.Count) {
                throw new Exception ("Capacity exceeded by attempt to load");
            }

            foreach(var popCan in popCans) {
                this.queue.Enqueue(popCan);
            }

            if (this.PopCansLoaded != null) {
                this.PopCansLoaded(this, new PopCanEventArgs() { PopCans = popCans });
            }
        }

        /**
        * Unloads pop cans from the rack, to simulate direct, physical unloading.
        * Causes a "PopCansUnloaded" event to be announced.
        * 
        * @return A list of the items unloaded.
        */
        public List<PopCan> Unload() {
            var result = new List<PopCan>(this.queue);
            this.queue.Clear();

            if (this.PopCansUnloaded != null) {
                this.PopCansUnloaded(this, new PopCanEventArgs() { PopCans = result });
            }

            return result;
        }
    }
}