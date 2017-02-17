using System;

namespace Frontend2.Hardware {
    public class MessageEventArgs : EventArgs {
        public string NewMessage { get; set; }
        public string OldMessage { get; set; }
    }
}