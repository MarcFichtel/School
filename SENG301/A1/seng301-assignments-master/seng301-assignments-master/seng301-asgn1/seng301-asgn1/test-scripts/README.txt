Each of these scripts is like a set of actions one could take on the vending machine.

* U01-bad-script1 should fail on the first line, because you can't configure a vending machine before you've created it
* U02-bad-script2 should fail because 0 is not allowed as a cost for a drink! Then, when you get that to work, the teardown should also be broken
* T01-good-script should succeed, but does not without a working implementation.

You ought to populate this directory with your own scripts to drive the vending machine factory, and vending machines. For instance, what happens if you create a script that has the same coin kinds in the vending machine? Is this allowed? Does it correctly output the correct pop when you press a button? What if you keep loading money in, and vend several drinks? Will it deliver it properly? Etc.