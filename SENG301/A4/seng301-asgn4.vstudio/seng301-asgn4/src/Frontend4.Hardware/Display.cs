using System;

namespace Frontend4.Hardware {
    /**
    * A simple device that displays a string. How it does this is not part of the
    * simulation. A very long string might scroll continuously, for example.
    */
    public class Display : AbstractHardware, IDisplay {

        public string Message { get; protected set; }

        public event EventHandler<MessageEventArgs> MessageChanged;

        public void DisplayMessage(string message) {
            var oldMessage = this.Message;
            this.Message = message;
            if (this.MessageChanged != null) {
                this.MessageChanged(this, new MessageEventArgs() { NewMessage = this.Message, OldMessage = oldMessage });
            }
        }
    }
}