using System;
using System.Collections.Generic;

namespace Frontend2.Hardware {

    /// <summary>
    /// A set of pop cans. Some events fire with the PopCan property set; others fire with the
    /// PopCans property set.
    /// </summary>
    public class PopCanEventArgs : EventArgs {
        public PopCan PopCan { get; set; }
        public List<PopCan> PopCans { get; set; }
    }
}