
/**
 * A test driver for the WebServer
 * 
 * @author 	Majid Ghaderi
 * @version	5.0
 *
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import cpsc441.a2.client.*;


public class Tester {
	private static final Logger logger = Logger.getLogger(Tester.class.getName());
	
	private static final String[] badRequests = {"POST /files/a.pdf HTTP/1.1",
												 "GET /files/a.pdf",
												 "GET"};
	
	
	private static ArrayList<URL> urlList = new ArrayList<>(8);
	private static String urlFileName = "";
	private static String serverName = "";
	private static int serverPort = 0;
	

	/**
	 * main method
	 */
	public static void main(String[] args) {
		//
		// parse command line args
		//
		parseCommandLine(args);
		
		//
		// fetch URLs
		//
		if (!readUrls())
			error("problem reading URLs");

		//
		// run the tests
		//
		getKey("bad request tests");
		testBadRequest();
		
		getKey("not found test");
		testNotFound();
		
		getKey("single-threaded functionality tests");
		basicTest();
		
		getKey("multi-threaded functionality tests");
		parallelTest();
		
		System.out.println("test finished");
	}
	

	
	/**
	 * wait for user to press any key
	 * 
	 */
	private static void getKey(String msg) {
		System.out.println("press <ENTER> to start: " + msg);
		try {
			while (System.in.read() != '\n');
		} catch (IOException e) {
			logger.severe("error reading standard in");
		}
	}
	
	
	/**
	 * send GET request for no existing object
	 * 
	 */
	private static boolean testNotFound() {
		// use ExecutorService to manage threads
		ExecutorService executor = Executors.newFixedThreadPool(1);
		
		logger.finest("sending request");
		URL url = urlList.get(0);
		
		try {
			URL notFoundURL = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getFile() + "random");
			HttpClient client = new HttpClient(notFoundURL);
			executor.execute(client);
		} catch (MalformedURLException e) {
			logger.severe("problem formatting URL");
		}
		
		// wait for test to finish
		logger.finest("waiting for request to complete...");
		executor.shutdown(); 
		while (!executor.isTerminated())
			Thread.yield(); // return the control to system
		logger.finest("not found request completed");
		
		return true;
	}


	
	/**
	 * send malforemd GET requests
	 * 
	 */
	private static boolean testBadRequest() {
		// use ExecutorService to manage threads
		ExecutorService executor = Executors.newFixedThreadPool(1);
		
		// send all rquests sequentially
		logger.finest("sending bad requests");
		URL url = urlList.get(0);
		
		for (String request : badRequests) { 
			HttpHandler client = new HttpHandler(request, url);
			executor.execute(client);
		}
		
		// wait for test to finish
		logger.finest("waiting for requests to complete...");
		executor.shutdown(); 
		while (!executor.isTerminated())
			Thread.yield(); // return the control to system
		logger.finest("all bad requests completed");
		
		return true;
	}
	
	
	/**
	 * send GET requests in parallel
	 * 
	 */
	private static boolean parallelTest() {
		// create a thread for each URL
		int threadPoolSize = urlList.size();
		logger.finest("thread pool size = " + threadPoolSize);

		//
		// open a background TCP connection
		// send parallel HTTP requests
		//
		try (Socket backgroundSocket = new Socket(serverName, serverPort)) {
			// open IO sterams for background connection
			backgroundSocket.getOutputStream();
			backgroundSocket.getInputStream();
			logger.info("background TCP connection opened");
			
			// use ExecutorService to manage threads
			ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
			
			// send all rquests in parallel
			logger.fine("sending parallel requests");
			for (URL url : urlList) { 
				HttpClient client = new HttpClient(url);
				executor.execute(client);
			}
			
			// wait for test to finish
			logger.fine("waiting for requests to complete...");
			executor.shutdown(); 
			while (!executor.isTerminated())
				Thread.yield(); // return the control to system
			logger.fine("all requests completed");
		} catch (IOException e) {
			logger.info("failed to open background TCP connection: " + e.getMessage());
		}
		
		return true;
	}
	
	
	/**
	 * send GET requests sequantially
	 * 
	 */
	private static boolean basicTest() {
		// use ExecutorService to manage threads
		ExecutorService executor = Executors.newFixedThreadPool(1);
		
		// send all rquests sequentially
		logger.finest("sending requests sequentially");
		for (URL url : urlList) { 
			HttpClient client = new HttpClient(url);
			executor.execute(client);
		}
		
		// wait for test to finish
		logger.fine("waiting for requests to complete...");
		executor.shutdown(); 
		while (!executor.isTerminated())
			Thread.yield(); // return the control to system
		logger.fine("all requests completed");
		
		return true;
	}

	
	/**
	 * read object paths from the input file
	 * 
	 * one object per line
	 */
	private static boolean readUrls() {
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(urlFileName))) {
			line = br.readLine();
			while ((line != null) && !line.isEmpty()) {
				try {
					urlList.add(new URL("http", serverName, serverPort, line));
				} catch (MalformedURLException e) {
					logger.info("malforemd URL: " + line);
				}
				line = br.readLine();
			}
			
			return true;
		} catch (IOException e) {
			logger.info("error reading URL file: " + e.getMessage());
		}
		
		return false;
	}
	
	
	/**
	 * parse command line for options/etc
	 * usage: Tester server port url-file
	 */
	private static void parseCommandLine(String[] args) {
		if (args.length != 3)
			error("usage: Tester server port url-file");
			
		serverName = args[0];
		serverPort = Integer.parseInt(args[1]);
		urlFileName = args[2];
	}

	
	/**
	 * print error message and exit 
	 */
	private static void error(String msg) {
		logger.severe(msg);
		System.exit(0);
	}
}
