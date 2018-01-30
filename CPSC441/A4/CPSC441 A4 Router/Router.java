
import java.util.*;
import java.io.*;
import java.net.*;
import cpsc441.a4.shared.*;

/**
 * Router Class
 *
 * This class implements the functionality of a router
 * when running the distance vector routing algorithm.
 *
 * The operation of the router is as follows:
 * 1. send/receive HELLO message
 * 2. while (!QUIT)
 *      receive ROUTE messages
 *      update mincost/nexthop/etc
 * 3. Cleanup and return
 *
 *
 * @author 	Majid Ghaderi
 * @version	3.0
 *
 */
public class Router {

	// Some variables
	int routerId, serverPort, updateInterval;
	String serverName;
	Timer timer; 						// Timer used to periodically update neighbors
	int[] linkcost; 				// linkcost[i] = cost of link to router i
	int[] nexthop; 					// nexthop[i] = next hop node to reach router i
	int[] neighbors; 				// this router's neighbors
	int[][] mincost; 				// mincost[i] = mincost vector of router i
	boolean quit = false;		// Used for termination
	Socket socket; 					// TCP Socket to connect to RServer
	RtnTable routingTable;	// The routing table
	ObjectInputStream ois;	// For reading dvr packets
	ObjectOutputStream oos;	// For sending dvr packets

    /**
     * Constructor to initialize the rouer instance
     *
     * @param routerId			Unique ID of the router starting at 0
     * @param serverName		Name of the host running the network server
     * @param serverPort		TCP port number of the network server
     * @param updateInterval	Time interval for sending routing updates to neighboring routers (in milli-seconds)
     */
	public Router(int routerId, String serverName, int serverPort, int updateInterval) {
		this.routerId = routerId;
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.updateInterval = updateInterval;
	}


    /**
     * starts the router
     *
     * @return The forwarding table of the router
     */
	public RtnTable start() {
		try {
			// Open a TCP connection to the server
			socket = new Socket(serverName, serverPort);

			// Create Object Streams
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());

			// Send HELLO packet to server
			DvrPacket dvrSnd_HELLO = new DvrPacket(routerId, DvrPacket.SERVER, DvrPacket.HELLO);
			oos.writeObject(dvrSnd_HELLO);

			// Receive HELLO packet from server
			DvrPacket dvrRcv_HELLO = (DvrPacket)ois.readObject();

			// Process HELLO packet (get link cost vector)
			linkcost = dvrRcv_HELLO.getMinCost();

			// Initialize other data structures
			mincost = new int[linkcost.length][linkcost.length];
			nexthop = new int[linkcost.length];
			neighbors = new int[linkcost.length];
			for (int i = 0; i < linkcost.length; i++) {
				// Next hop to oneself is oneself
				// Next hop to a neighbor is that neighbor (until proven otherwise)
				// Next hop to non-neighbors is nobody (until proven otherwise)
				if (i == routerId) { nexthop[i] = routerId; }
				else if (linkcost[i] != DvrPacket.INFINITY) { nexthop[i] = i; }
				else { nexthop[i] = -1; }
				if (i == routerId || linkcost[i] == DvrPacket.INFINITY) { neighbors[i] = 0; }
				else { neighbors[i] = 1; }
			}
			routingTable = new RtnTable(mincost[routerId], nexthop);

			// Initialize mincost table to INFINITY for all neighbor entries
			mincost[routerId] = linkcost;
			for (int i = 0; i < linkcost.length; i++) {
				for (int j = 0; j < linkcost.length; j++) {
					if (i != routerId) mincost[i][j] = DvrPacket.INFINITY;
				}
			}

			// 3: start Timer
			timer = new Timer(true);
			timer.schedule(new TimeoutHandler(this), updateInterval);

			// While not QUIT, read and process packet
			while (!quit) {
				DvrPacket dvr = (DvrPacket)ois.readObject();
				routingTable = processDvr(dvr);
			}

			// Cancel timer, close socket, clean up
			timer.cancel();
			socket.close();

		// Catch & display any exceptions
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		// Return routing table
		return routingTable;
	}

	/*
	 * Distance vector routing algorithm (implements Bellman-Ford algorithm)
	 * Operation of this method depends on the packet sender
	 */
	public RtnTable processDvr (DvrPacket dvr) {
		System.out.print("Processing packet: ");

		// QUIT
		if (dvr.type == DvrPacket.QUIT) {
			print("QUIT.");
			quit = true;

		// ROUTE from SERVER
		} else if (dvr.sourceid == DvrPacket.SERVER) {
			print("SERVER.");

			// Update link cost vector, next hop vector, min cost table, and neighbors
			linkcost = dvr.getMinCost();
			for (int i = 0; i < linkcost.length; i++) {
				if (i == routerId) { nexthop[i] = routerId; }
				else if (linkcost[i] != DvrPacket.INFINITY) { nexthop[i] = i; }
				else { nexthop[i] = -1; }
			}
			computeMincost(linkcost, routerId);
			updateNeighbors();

		// ROUTE from neighbor
		} else {
			print("ROUTE from Router#" + dvr.sourceid + ".");

			// If anything changed, update min cost vector & neighbors
			if (!Arrays.equals(dvr.getMinCost(), mincost[dvr.sourceid])) {
				computeMincost(dvr.getMinCost(), dvr.sourceid);
				updateNeighbors();
			}
		}

		// Take care of timer and return routing table
		timer.cancel();
		return new RtnTable(linkcost, nexthop);
	}

	/*
	 * When the mincost vector changes, or the timer runs out, update neighbors
	 */
	public void updateNeighbors() {
		try {

			// send local mincost vector to neighbors only
			for (int i = 0; i < linkcost.length; i++) {
				if (i != routerId && neighbors[i] != 0) {
					DvrPacket dvr = new DvrPacket(routerId, i, DvrPacket.ROUTE, linkcost);
					oos.writeObject(dvr);
				}
			}

		// Catch & display any exceptions
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		// re-start timer
		timer = new Timer(true);
		timer.schedule(new TimeoutHandler(this), updateInterval);
	}

	// Compute Min Cost Vector with Bellman-Ford Algorithm
	public void computeMincost(int[] neighborMinCost, int neighborId) {

		// Update mincost at other router
		mincost[neighborId] = neighborMinCost;

		// Recompute mincost (Bellman-Ford equation)
		for (int i = 0; i < linkcost.length; i++) {
			for (int j = 0; j < linkcost.length; j++) {

				// For all neighbors
				if (i != routerId && j != routerId && i != j) {

					// Update nexthop
					if (mincost[routerId][j] + mincost[j][i] < mincost[routerId][i]) {
						nexthop[i] = j;
					}

					// Bellman-Ford Equation
					mincost[routerId][i] = Math.min(mincost[routerId][i], mincost[routerId][j] + mincost[j][i]);
				}
			}
		}
	}


    /**
     * A simple test driver
     *
     */
	public static void main(String[] args) {
		// default parameters
		int routerId = 0;
		String serverName = "localhost";
		int serverPort = 2227;
		int updateInterval = 1000; //milli-seconds

		if (args.length == 4) {
			routerId = Integer.parseInt(args[0]);
			serverName = args[1];
			serverPort = Integer.parseInt(args[2]);
			updateInterval = Integer.parseInt(args[3]);
		} else {
			System.out.println("incorrect usage, try again.");
			System.exit(0);
		}

		// print the parameters
		System.out.printf("starting Router #%d with parameters:\n", routerId);
		System.out.printf("Relay server host name: %s\n", serverName);
		System.out.printf("Relay server port number: %d\n", serverPort);
		System.out.printf("Routing update intwerval: %d (milli-seconds)\n", updateInterval);

		// start the router
		// the start() method blocks until the router receives a QUIT message
		Router router = new Router(routerId, serverName, serverPort, updateInterval);
		RtnTable rtn = router.start();
		System.out.println("Router terminated normally");

		// print the computed routing table
		System.out.println();
		System.out.println("Routing Table at Router #" + routerId);
		System.out.print(rtn.toString());
	}

	public void print(String msg) { System.out.println(msg); }
}
