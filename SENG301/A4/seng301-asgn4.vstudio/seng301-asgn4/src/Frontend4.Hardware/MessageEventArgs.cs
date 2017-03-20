using System;

namespace Frontend4.Hardware {
    public class MessageEventArgs : EventArgs {
        public string NewMessage { get; set; }
        public string OldMessage { get; set; }
    }
}