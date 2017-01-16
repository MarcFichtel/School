using System;

namespace Frontend1 {
    public class Pop : Deliverable {
        private string name;
        public string Name {
            get {
                return this.name;
            }
        }

        public Pop(string name) {
            if (name == null || name.Length == 0) {
                throw new Exception("The name of the pop cannot be null or of 0 length.");
            }
            this.name = name;
        }

        public override string ToString() {
            return this.Name;
        }

        public override bool Equals(object obj) {
            var equals = true;
            if (! (obj is Pop)) {
                equals = false;
            }
            else if (((Pop) obj).Name != this.Name) {
                equals = false;
            }
            return equals;
        }
    }
}