using System;

namespace Frontend4.Hardware {
    public interface IIndicatorLight : IHardware {
        event EventHandler Activated;
        event EventHandler Deactivated;
    }
}
