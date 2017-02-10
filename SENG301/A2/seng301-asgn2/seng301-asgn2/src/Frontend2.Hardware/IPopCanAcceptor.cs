
namespace Frontend2.Hardware {
    /**
    * A simple interface to allow a device to communicate with another device that
    * accepts pop cans.
    */
    public interface IPopCanAcceptor {
        /**
        * Instructs the device to take the pop can as input.
        * 
        * @param popCan
        *            The pop can to be taken as input.
        */
        void AcceptPopCan(PopCan popCan); 
    }
}
