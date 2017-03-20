using System;

namespace Frontend4 {
    /// <summary>
    /// Each instance represents a product.
    /// </summary>
    public class Product : IDeliverable {
        public string Name { get; protected set; }
        
        public Product(string name) {
            if (name == null || name.Length == 0) {
                throw new Exception("Product needs to have a name: cannot be null or empty string.");
            }
            this.Name = name;
        }

        public override string ToString() {
            return "" + this.Name;
        }
    }
}