using System;

namespace Frontend4.Hardware {

    /**
    * Events emanating from a product rack.
    */
    public interface IProductRack : IHardware {
        /**
        * An event announced when the indicated product is added to the indicated
        * product rack.
        */
        event EventHandler<ProductEventArgs> ProductAdded;

        /**
        * An event announced when the indicated product is removed from the
        * indicated product rack.
        */
        event EventHandler<ProductEventArgs> ProductRemoved;

        /**
        * An event announced when the indicated product rack becomes full.
        * 
        */
        event EventHandler ProductRackFull;

        /**
        * An event announced when the indicated product rack becomes empty.
        * 
        */
        event EventHandler ProductRackEmpty;

        /**
        * Announces that the indicated sequence of products has been added to the
        * indicated rack. Used to simulate direct, physical loading of
        * the rack.
        * 
        */
        event EventHandler<ProductEventArgs> ProductsLoaded;

        /**
        * Announces that the indicated sequence of products has been removed to the
        * indicated product rack. Used to simulate direct, physical unloading of
        * the rack.
        * 
        */
        event EventHandler<ProductEventArgs> ProductsUnloaded;
    }
}