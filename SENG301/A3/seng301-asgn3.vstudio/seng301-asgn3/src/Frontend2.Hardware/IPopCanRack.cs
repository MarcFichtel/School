using System;

namespace Frontend2.Hardware {

    /**
    * Events emanating from a pop can rack.
    */
    public interface IPopCanRack : IHardware {
        /**
        * An event announced when the indicated pop can is added to the indicated
        * pop can rack.
        */
        event EventHandler<PopCanEventArgs> PopCanAdded;

        /**
        * An event announced when the indicated pop can is removed from the
        * indicated pop can rack.
        */
        event EventHandler<PopCanEventArgs> PopCanRemoved;

        /**
        * An event announced when the indicated pop can rack becomes full.
        * 
        */
        event EventHandler PopCanRackFull;

        /**
        * An event announced when the indicated pop can rack becomes empty.
        * 
        */
        event EventHandler PopCanRackEmpty;

        /**
        * Announces that the indicated sequence of pop cans has been added to the
        * indicated rack. Used to simulate direct, physical loading of
        * the rack.
        * 
        */
        event EventHandler<PopCanEventArgs> PopCansLoaded;

        /**
        * Announces that the indicated sequence of pop cans has been removed to the
        * indicated pop can rack. Used to simulate direct, physical unloading of
        * the rack.
        * 
        */
        event EventHandler<PopCanEventArgs> PopCansUnloaded;
    }
}