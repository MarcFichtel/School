using System;
using System.Collections.Generic;

namespace Frontend4.Hardware {

    /// <summary>
    /// Event args that contains an IDeliverable
    /// </summary>
    public class DeliverableEventArgs : EventArgs {
        public IDeliverable Item { get; set; }
    }
}