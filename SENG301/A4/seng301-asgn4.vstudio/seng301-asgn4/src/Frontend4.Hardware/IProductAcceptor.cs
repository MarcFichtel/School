
namespace Frontend4.Hardware {
    /**
    * A simple interface to allow a device to communicate with another device that
    * accepts products.
    */
    public interface IProductAcceptor {
        /**
        * Instructs the device to take the product as input.
        * 
        * @param product
        *            The product can to be taken as input.
        */
        void AcceptProduct(Product product); 
    }
}
