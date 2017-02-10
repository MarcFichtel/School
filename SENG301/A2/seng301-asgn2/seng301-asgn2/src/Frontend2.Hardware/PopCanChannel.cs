using System;

namespace Frontend2.Hardware {
    /**
    * Represents the hardware through which a pop can is carried from one device to
    * another. Once the hardware is configured, pop can channels will not be used
    * directly by other applications.
    */
    public class PopCanChannel : IPopCanAcceptor {
        
        private IPopCanAcceptor sink;

        /**
        * Creates a new pop can channel whose output will go to the indicated sink.
        * 
        * @param sink
        *            The output of the channel. Can be null, which disconnects any
        *            current output device.
        */
        public PopCanChannel(IPopCanAcceptor sink) {
            this.sink = sink;
        }

        /**
        * This method should only be called from hardware devices.
        */
        public void AcceptPopCan(PopCan popCan) {
            this.sink.AcceptPopCan(popCan);
        }
    }
}