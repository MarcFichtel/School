using System;

namespace Frontend2 {
    /// <summary>
    /// Each instance represents a pop can.
    /// </summary>
    public class PopCan : IDeliverable {
        public string Name { get; protected set; }
        
        public PopCan(string name) {
            if (name == null || name.Length == 0) {
                throw new Exception("PopCan needs to have a name: cannot be null or empty string.");
            }
            this.Name = name;
        }

        public override bool Equals(object obj) {
            var equals = true;
            if (! (obj is PopCan)) {
                equals = false;
            }
            else if (((PopCan) obj).Name != this.Name) {
                equals = false;
            }
            return equals;
        }

        public override string ToString() {
            return "" + this.Name;
        }
    }
}