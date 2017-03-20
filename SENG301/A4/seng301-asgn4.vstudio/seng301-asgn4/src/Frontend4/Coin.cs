using System;

namespace Frontend4 {
    
    public class Coin : IDeliverable {
        private Cents value;

        public Cents Value {
            get {
                return this.value;
            }
        }

        public Coin(Cents value) {
            if (value.Value <= 0) {
                throw new Exception("The coin value must be greater than 0. The argument passed was: " + value);
            }
            this.value = value;
        }

        public Coin(int value) {
            if (value <= 0) {
                throw new Exception("The coin value must be greater than 0.");
            }
            this.value = new Cents(value);
        }

        public override string ToString() {
            return "" + this.Value;
        }
    }
}
