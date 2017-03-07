using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace UTP {

    /*
    T07:
        CREATE(5, 10, 25, 100; 3; 10; 10; 10)
        CONFIGURE([0] "A", 5; "B", 10; "C", 25)
        COIN_LOAD([0] 0; 5, 1)
        COIN_LOAD([0] 1; 10, 1)
        COIN_LOAD([0] 2; 25, 2)
        COIN_LOAD([0] 3; 100, 0)
        POP_LOAD([0] 0; "A", 1)
        POP_LOAD([0] 1; "B", 1)
        POP_LOAD([0] 2; "C", 1)
        CONFIGURE([0] "Coke", 250; "water", 250; "stuff", 205)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(0)
        INSERT([0] 100)
        INSERT([0] 100)
        INSERT([0] 100)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(50, "A")
        UNLOAD([0])
        CHECK_TEARDOWN(315; 0; "B", "C")
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
        UNLOAD([0])
        CHECK_TEARDOWN(315; 0; "water", "stuff")
    */

    [TestClass]
    public class T07 {

        [TestMethod]
        public void TestMethod1() {
        }
    }
}
