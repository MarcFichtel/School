﻿using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace UTP {

    /*
    T10:
        CREATE(5, 10, 25, 100; 1; 10; 10; 10)
        CONFIGURE([0] "stuff", 140)
        COIN_LOAD([0] 0; 5, 1)
        COIN_LOAD([0] 1; 10, 6)
        COIN_LOAD([0] 2; 25, 1)
        COIN_LOAD([0] 3; 100, 1)
        POP_LOAD([0] 0; "stuff", 1)
        INSERT([0] 1)
        INSERT([0] 139)
        PRESS([0] 0)
        EXTRACT([0])
        CHECK_DELIVERY(140)
        UNLOAD([0])
        CHECK_TEARDOWN(190; 0; "stuff")
    */

    [TestClass]
    public class T10 {

        [TestMethod]
        public void TestMethod1() {
        }
    }
}
