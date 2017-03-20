

/**
 * Represents a value of money, including zero and negative quantities.
 * 
 * @author Robert J. Walker
 * @modified Tony Tang
 */
namespace Frontend4 {
    public class Cents {
        public int Value { get; protected set; }

        /**
        * Basic constructor.
        * 
        * @param value
        *            The value, in units of the specified currency.
        */
        public Cents(int value) {
            this.Value = value;
        }

        public override int GetHashCode() {
            return this.Value;
        }

        public override bool Equals(object obj) {
            if(obj is Cents) {
                Cents cents = (Cents)obj;
                return this.Value == cents.Value;
            }
            return false;
        }

        public static Cents operator+ (Cents a, Cents b) {
            return new Cents(a.Value + b.Value);
        }

        public static Cents operator- (Cents a, Cents b) {
            return new Cents(a.Value - b.Value);
        }

        public static bool operator< (Cents a, Cents b) {
            return a.Value < b.Value;
        }

        public static bool operator> (Cents a, Cents b) {
            return a.Value > b.Value;
        }
        
        public static bool operator<= (Cents a, Cents b) {
            return a.Value <= b.Value;
        }

        public static bool operator>= (Cents a, Cents b) {
            return a.Value >= b.Value;
        }

        public static bool operator== (Cents a, Cents b) {
            return a.Equals(b);
        }

        public static bool operator!= (Cents a, Cents b) {
            return !a.Equals(b);
        }
    }
}