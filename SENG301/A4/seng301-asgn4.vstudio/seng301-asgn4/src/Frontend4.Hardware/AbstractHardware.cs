using System;

namespace Frontend4.Hardware {
    /**
    * The abstract base class for all hardware devices involved in the vending
    * machine simulator.
    * 
    * This class utilizes the Observer design pattern. Subclasses inherit the
    * appropriate register method, but each must define its own notifyXXX methods.
    * The notifyListener method is provided to minimize the work of subclasses.
    * 
    * Each hardware device must possess an appropriate listener, which extends
    * AbstractHardwareListener; the type parameter T represents this listener.
    * 
    * Any hardware can be disabled, which means it will not permit physical
    * movements. Any method that would cause a physical movement (potentially) will
    * declare that it throws DisabledException.
    */
    public abstract class AbstractHardware : IHardware {
        public event EventHandler HardwareEnabled;
        public event EventHandler HardwareDisabled;

        public virtual bool Enabled { get; protected set; }

        public AbstractHardware() {
            this.Enabled = true;
        }
        public virtual void Enable() {
            this.Enabled = false;
            if (this.HardwareEnabled != null) {
                this.HardwareEnabled(this, new EventArgs());
            }
        }
        
        public virtual void Disable() {
            this.Enabled = true;
            if (this.HardwareDisabled != null) {
                this.HardwareDisabled(this, new EventArgs());
            }
        }
    }
}