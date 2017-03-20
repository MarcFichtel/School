using System;

namespace Frontend4.Hardware {
    /**
    * Events emanating from a lock.
    */
    public interface ILock : IHardware {
        event EventHandler Locked;
        event EventHandler Unlocked;
    }
}