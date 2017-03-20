using System;
using System.Collections.Generic;

namespace Frontend4.Hardware {

    /**
    * Represents a storage rack for products within the vending machine. More than
    * one would typically exist within the same vending machine. The product rack
    * has finite, positive capacity. A product rack can be disabled, which prevents
    * it from dispensing products.
    */
    public class ProductRack : AbstractHardware, IProductRack {

        public int Capacity { get; protected set; }
        public int Count {
            get {
                return this.queue.Count;
            }
        }

        public event EventHandler<ProductEventArgs> ProductAdded;
        public event EventHandler<ProductEventArgs> ProductRemoved;
        public event EventHandler ProductRackFull;
        public event EventHandler ProductRackEmpty;
        public event EventHandler<ProductEventArgs> ProductsLoaded;
        public event EventHandler<ProductEventArgs> ProductsUnloaded;

        private ProductChannel sink;
        private Queue<Product> queue;
        
        /**
        * Creates a new product rack with the indicated maximum capacity. The product
        * rack initially is empty.
        * 
        * @param capacity
        *            Positive integer indicating the maximum capacity of the rack.
        */
        public ProductRack(int capacity) {
            if(capacity <= 0) {
                throw new Exception("Capacity cannot be non-positive: " + capacity);
            }

            this.Capacity = capacity;
            this.queue = new Queue<Product>();
        }


        /**
        * Connects the product rack to an outlet channel, such as the delivery
        * chute. Causes no events.
        * 
        * @param sink
        *            The channel to be used as the outlet for dispensed products.
        */
        public void Connect(ProductChannel sink) {
            this.sink = sink;
        }

        /**
        * Adds the indicated product to this product rack if there is sufficient
        * space available. If the product is successfully added to this product
        * rack, a "ProductAdded" event is announced to its listeners. If, as a result
        * of adding this product, this product rack has become full, a "ProductRackFull"
        * event is announced to its listeners.
        * 
        * @param Product
        *            The product to be added.
        */
        public void AddProduct(Product Product) {
            if (! this.Enabled) {
                throw new Exception("product rackdisabled");
            }
            
            if (this.Count >= this.Capacity) {
                throw new Exception("product rack already full - cannot accept new product");
            }

            this.queue.Enqueue(Product);
            
            if (this.ProductAdded != null) {
                this.ProductAdded(this, new ProductEventArgs() { Product = Product });
            }

            if (this.Count >= this.Capacity) {
                if (this.ProductRackFull != null) {
                    this.ProductRackFull(this, new EventArgs());
                }
            }
        }

        /**
        * Causes one product to be removed from this product rack, to be placed in
        * the output channel to which this product rack is connected. If a product
        * is removed from this product rack, a "ProductRemoved" event is announced to
        * its listeners. If the removal of the product causes this product rack to
        * become empty, a "ProductRackEmpty" event is announced to its listeners.
        * 
        */
        public void DispenseProduct() {
            if (! this.Enabled) {
                throw new Exception("product rackdisabled");
            }
            if (this.Count == 0) {
                throw new Exception("No Products in the Product rack!");
            }

            var Product = this.queue.Dequeue();

            if (this.ProductRemoved != null) {
                this.ProductRemoved(this, new ProductEventArgs() { Product = Product });
            }

            if(this.sink == null) {
                throw new Exception("The output channel is not connected");
            }

            this.sink.AcceptProduct(Product);

            if (this.Count == 0) {
                if (this.ProductRackEmpty != null) {
                    this.ProductRackEmpty(this, new EventArgs());
                }
            }
        }

        /**
        * Allows products to be loaded into the product rack, to simulate direct,
        * physical loading. Note that any existing products in the rack are not
        * removed. Causes a "ProductsLoaded" event to be announced.
        * 
        * @param Products
        *            One or more products to be loaded into this product rack.
        */
        public void LoadProducts(List<Product> products) {
            if (this.Capacity < this.Count + products.Count) {
                throw new Exception ("Capacity exceeded by attempt to load");
            }

            foreach(var product in products) {
                this.queue.Enqueue(product);
            }

            if (this.ProductsLoaded != null) {
                this.ProductsLoaded(this, new ProductEventArgs() { Products = products });
            }
        }

        /**
        * Unloads products from the rack, to simulate direct, physical unloading.
        * Causes a "ProductsUnloaded" event to be announced.
        * 
        * @return A list of the items unloaded.
        */
        public List<Product> Unload() {
            var result = new List<Product>(this.queue);
            this.queue.Clear();

            if (this.ProductsUnloaded != null) {
                this.ProductsUnloaded(this, new ProductEventArgs() { Products = result });
            }

            return result;
        }
    }
}