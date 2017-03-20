using System;

namespace Frontend4.Hardware {

    /**
    * A simple lock device to prevent or to permit the interior of the vending
    * machine to be accessed. The lock does not directly act on the hardware
    * otherwise. By default the lock is initially locked. It ignores the
    * enabled/disabled state.
    */
    public class Lock : AbstractHardware, ILock {

        public bool LockLocked { get; protected set; }

        public event EventHandler Locked;
        public event EventHandler Unlocked;

        public Lock() {
            this.LockLocked = true;
        }

        public void LockLock() {
            this.LockLocked = true;
            if (this.Locked != null) {
                this.Locked(this, new EventArgs());
            }
        }
        
        public void Unlock() {
            this.LockLocked = false;
            if (this.Unlocked != null) {
                this.Unlocked(this, new EventArgs());
            }
        }
    }
}