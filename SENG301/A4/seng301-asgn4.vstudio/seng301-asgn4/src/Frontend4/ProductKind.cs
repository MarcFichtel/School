using System;


/**
 * Represents certain properties of products in order to permit sets of products
 * to share these without having to subclass. These properties will, in general,
 * be globally changeable.
 * 
 * @author Robert J. Walker
 */
namespace Frontend4 {
    public class ProductKind {

        public Cents Cost { 
            get {
                return this.cost;
            }
            set {
                if (value.Value == 0) {
                    throw new ArgumentOutOfRangeException("The cost cannot be non-positive");
                }
                if (value != this.cost) {
                    var oldCost = this.cost;
                    this.cost = value;
                    this.notifyCostChanged(oldCost, value);
                }
            } 
        }
        public string Name { 
            get {
                return this.name;
            } 
            set {
                if (value == null || value == "") {
                    throw new ArgumentException("Name cannot be null or empty");
                }
                if (value != this.name) {
                    var oldName = this.name;
                    this.name = value;
                    this.notifyNameChanged(oldName, value);
                }
            }
        }

        public event EventHandler<NameChangedEventArgs> NameChanged;
        public event EventHandler<CostChangedEventArgs> CostChanged;

        private Cents cost;
        private string name;

        /**
        * Basic constructor.
        * 
        * @param name
        *            The name of the product kind. Cannot be null.
        * @param cost
        *            The cost of the product kind. Cannot be non-positive.
        */
        public ProductKind(String name, Cents cost) {
            if(name == null)
                throw new ArgumentException("The name cannot be null");

            if(cost.Value <= 0)
                throw new ArgumentOutOfRangeException("The cost cannot be non-positive");

            this.Name = name;
            this.Cost = cost;
        }

        private void notifyNameChanged(string oldName, string newName) {
            if (this.NameChanged != null) {
                this.NameChanged(this, new NameChangedEventArgs() { OldName = oldName, NewName = newName });
            }
        }

        private void notifyCostChanged(Cents oldCost, Cents newCost) {
            if (this.CostChanged != null) {
                this.CostChanged(this, new CostChangedEventArgs() { OldCost = oldCost, NewCost = newCost });
            }
        }

        public override bool Equals(Object other) {
            if (other is ProductKind) {
                return this.Name == ((ProductKind)other).Name;
            }
            return false;
        }

        public override int GetHashCode() {
            return this.Name.GetHashCode();
        }
    }

    public class NameChangedEventArgs : EventArgs {
        public string OldName { get; set; }
        public string NewName { get; set; }
    }

    public class CostChangedEventArgs : EventArgs {
        public Cents OldCost { get; set; }
        public Cents NewCost { get; set; }
    }
}