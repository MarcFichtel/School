using System;

namespace Frontend4.Hardware {
    
    public interface IHardware {
        event EventHandler HardwareEnabled;
        event EventHandler HardwareDisabled;
    }
}