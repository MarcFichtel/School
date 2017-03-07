using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace UTP {

    /*
    T02:
        CREATE(5, 10, 25, 100; 3; 10; 10; 10)
        CONFIGURE([0] "Coke", 250; "water", 250; "stuff", 205)
        COIN_LOAD([0] 0; 5, 1)
        COIN_LOAD([0] 1; 10, 1)
        COIN_LOAD([0] 2; 25, 2)
        COIN_LOAD([0] 3; 100, 0)
        POP_LOAD([0] 0; "Coke", 1)
        POP_LOAD([0] 1; "water", 1)
        POP_LOAD([0] 2; "stuff", 1)
        INSERT([0] 100)
        INSERT([0] 100)
        INSERT([0] 100)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(50, "Coke")
        UNLOAD([0] )
        CHECK_TEARDOWN(315; 0; "water", "stuff")
    */

    [TestClass]
    public class T02 {

        [TestMethod]
        public void Test02() {
        }
    }
}
