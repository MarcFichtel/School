using System;
using System.Collections.Generic;

namespace Frontend4.Hardware {

    /// <summary>
    /// A set of products. Some events fire with the Product property set; others fire with the
    /// Products property set.
    /// </summary>
    public class ProductEventArgs : EventArgs {
        public Product Product { get; set; }
        public List<Product> Products { get; set; }
    }
}