using System;

namespace Frontend2.Hardware {
    /**
    * Events emanating from a lock.
    */
    public interface ILock : IHardware {
        event EventHandler Locked;
        event EventHandler Unlocked;
    }
}