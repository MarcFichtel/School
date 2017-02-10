using System;

namespace Frontend2.Hardware {
    public interface IIndicatorLight : IHardware {
        event EventHandler Activated;
        event EventHandler Deactivated;
    }
}
