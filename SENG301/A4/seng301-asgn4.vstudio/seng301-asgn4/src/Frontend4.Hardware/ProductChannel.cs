using System;

namespace Frontend4.Hardware {
    /**
    * Represents the hardware through which a product is carried from one device to
    * another. Once the hardware is configured, product channels will not be used
    * directly by other applications.
    */
    public class ProductChannel : IProductAcceptor {
        
        private IProductAcceptor sink;

        /**
        * Creates a new product channel whose output will go to the indicated sink.
        * 
        * @param sink
        *            The output of the channel. Can be null, which disconnects any
        *            current output device.
        */
        public ProductChannel(IProductAcceptor sink) {
            this.sink = sink;
        }

        /**
        * This method should only be called from hardware devices.
        */
        public void AcceptProduct(Product product) {
            this.sink.AcceptProduct(product);
        }
    }
}