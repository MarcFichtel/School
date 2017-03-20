using System;

namespace Frontend4.Hardware {
    /**
    * A simple device that can be on or off as an indication to users. By default
    * it is initially off. It ignores the enabled/disabled state.
    */
    public class IndicatorLight : AbstractHardware, IIndicatorLight {
        public bool Active { get; protected set; }

        public event EventHandler Activated;
        public event EventHandler Deactivated;

        public IndicatorLight() {
            this.Active = false;
        }

        public void Activate() {
            this.Active = true;
            if (this.Activated != null) {
                this.Activated(this, new EventArgs());
            }
        }

        public void Deactivate() {
            this.Active = false;
            if (this.Deactivated != null) {
                this.Deactivated(this, new EventArgs());
            }
        }

        public new void Enabled() {
        }

        public new void Disable() {
        }
    }
}