using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace UTP {

    /*
    T13:
        CREATE(5, 10, 25, 100; 1; 10; 10; 10)
        CONFIGURE([0] "stuff", 135)
        COIN_LOAD([0] 0; 5, 10)
        COIN_LOAD([0] 1; 10, 10)
        COIN_LOAD([0] 2; 25, 10)
        COIN_LOAD([0] 3; 100, 10)
        POP_LOAD([0] 0; "stuff", 1)
        INSERT([0] 25)
        INSERT([0] 100)
        INSERT([0] 10)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(0, "stuff")
        UNLOAD([0])
        CHECK_TEARDOWN(1400; 135)
    */

    [TestClass]
    public class T13 {

        [TestMethod]
        public void Test13() {
        }
    }
}
