using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections.Generic;
using Frontend2.Hardware;
using Frontend2;

namespace UTP {

    /*
    T12:
        CREATE(5, 10, 25, 100; 1; 10; 10; 10)
        CONFIGURE([0] "stuff", 140)
        COIN_LOAD([0] 0; 5, 0)
        COIN_LOAD([0] 1; 10, 5)
        COIN_LOAD([0] 2; 25, 1)
        COIN_LOAD([0] 3; 100, 1)
        POP_LOAD([0] 0; "stuff", 1)
        INSERT([0] 100)
        INSERT([0] 100)
        INSERT([0] 100)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(155, "stuff")
        UNLOAD([0])
        CHECK_TEARDOWN(320; 0)
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
    public class T12 {

        [TestMethod]
        public void Test12() {
        
        
        
        
        }
    }
}
