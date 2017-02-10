using System;

namespace Frontend2.Hardware {
    
    public interface IHardware {
        event EventHandler HardwareEnabled;
        event EventHandler HardwareDisabled;
    }
}