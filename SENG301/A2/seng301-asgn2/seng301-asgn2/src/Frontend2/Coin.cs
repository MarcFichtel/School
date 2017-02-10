using System;

namespace Frontend2 {
    
    public class Coin : IDeliverable {
        private int value;

        public int Value {
            get {
                return this.value;
            }
        }

        public Coin (int value) {
            if (value <= 0) {
                throw new Exception("The coin value must be greater than 0. The argument passed was: " + value);
            }
            this.value = value;
        }

        public override string ToString() {
            return "" + this.Value;
        }
    }
}
