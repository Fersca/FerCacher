package ferCacher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This Class is the Main class of the ferCacher system, it's used to manage the incoming connections.
 * It starts listening on port 5555.
 * 
 * You can access to the cache, put and get elements in constant time.
 * 
 * The system uses a HashMap to store objects and implements a double-linked-queue to manage the LRU algorithm.
 * The Queue doesn't performs a full scan in order to get the element and move it to the first position,
 * it's acceded directly to the node because the hashMap element has a reference to the Queue node to do a direct access.
 * that's why you can access it in constant-time.
 * 
 * The system in thread-safe, it synchronize any write action to the hashMap and any Queue modification, only when it's needed.
 * 
 * @author Fernando Scasserra - @fersca - March 13th, 2012
 *
 */
public class FerCacher {

	public static void main(String[] args) {
		
		FerCacher ferCacher = new FerCacher();
		ferCacher.runRemote();

	}
	
	private void runRemote() {
	
		try {
				        
			//Create server socket listening at port 5555
	        ServerSocket socket = new ServerSocket(5555);
	        Socket connection;
	        
	        //Print hello in the server
	        System.out.println("Listening at port 5555:");
	        
	        //Initialize the LRU Queue
	        Queue.init();
	        
	        while(true){
	        	
	        	//Wait fir the connection to be accepted
		        connection = socket.accept();
		    
		        System.out.println("New connection accepted");
		        
		        //Process the new connection in a separate thread 
		        Worker worker = new Worker(connection);
		        Thread t = new Thread(worker);
		        t.run();
	        }
	        
		} catch (IOException e){
			e.printStackTrace();
			System.out.println("Error reading from socket");
		}
		
	}
			
}
