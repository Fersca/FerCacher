package ferCacher;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * This class process each connection accepted by the main class
 * @author Fernando Scasserra
 *
 */
public class Worker implements Runnable {

	//Current connection
	Socket connection;
	
	public Worker(Socket connection){
		this.connection=connection;
	}
	
	@Override
	/**
	 * Process each connection, read the instructions, process instruction, send the response to the client
	 */
	public void run() {

        BufferedReader reader=null;
        DataOutputStream output=null;

		try {
			
			String instruction;
	
			//Reader and Writer channels to the socket
	        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        output = new DataOutputStream(connection.getOutputStream());
	       
	        //Capture the instructions until the END command is sent
	        boolean reading=true;
	        String key;
	        String data;
	        
	        while (reading){
	        	
	        	output.writeBytes("\n");
	        	output.writeBytes("Instruction: ");
	        	
	        	instruction = reader.readLine();
	        	
	        	if (instruction!=null){
	       
	        		instruction = instruction.toUpperCase();
	        		
	        		if (instruction.equals("GET")){
	        			
	        			output.writeBytes("Key: ");
	        			key = reader.readLine();
	        			data = Memory.get(key);
	        			if (data!=null){
	        				output.writeBytes(data);
	        			} else {
	        				output.writeBytes("Empty");
	        			}
	        			
	        		} else if (instruction.equals("PUT")){

	        			output.writeBytes("Key: ");
	        			key = reader.readLine();
	        			output.writeBytes("Data: ");
	        			data = reader.readLine();
	        			Memory.put(key,data);
	        			output.writeBytes("Stored");
	        			
	        		} else if (instruction.equals("DELETE")){

	        			output.writeBytes("Key: ");
	        			key = reader.readLine();
	        			data = Memory.delete(key);
	        			if (data!=null){
	        				output.writeBytes("Deleted");	
	        			} else {
	        				output.writeBytes("Empty");
	        			}
	        				        		
	        		} else if (instruction.equals("STATS")){
	        				        	    
	        	        output.writeBytes(Memory.stats());
	        	        output.writeBytes("\n");
	        	        output.writeBytes("Keys:");
	        	        output.writeBytes("\n");
	        	        output.writeBytes(Queue.stats());
	        	        
	        		} else if (instruction.equals("END")){
	        			
	        			reading = false;
	        	        output.writeBytes("Disconnected");
	        	        output.writeBytes("\n");
	        	        
	        		}
	        		
	        	}
	        		        	
	        }
	       	       		
	        System.out.println("Connection ended");
	        
		} catch (IOException io){
			System.out.println("Error reading from socket");
		} finally{
	        //Close the reader, writer and connections objects.
	        try{
	        	reader.close();
	        	output.close();
	        	connection.close();
	        } catch (IOException e){}
		}
	}

	
}
