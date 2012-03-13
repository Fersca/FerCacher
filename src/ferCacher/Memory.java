package ferCacher;

import java.util.HashMap;

/**
 * Memory class, holds the HashMap that has the stored elements
 * @author Fernando Scasserra @fersca
 */
public class Memory {

	//HashMap to store the data objects
	private static HashMap<String, DataNode> memory = new HashMap<String, DataNode>();
	
	//Max number of objects in the cache
	private static final int maxQuantity = 10;
	
	//Quantity of miss elements
	private static volatile int missQuantity = 0;

	//Quantity of hit elements
	private static volatile int hitQuantity = 0;

	/**
	 * Add data to the cache
	 * @param key
	 * @param data
	 */
	public static void put(String key, String data){
		
		//If the max size is reached, remove the last element from the LRU Queue
		if (memory.size()==(maxQuantity+2)){
			Queue.removeLast();
		}
		
		//Create the new node
		DataNode dataNode = new DataNode();
		dataNode.setData(data);
		DataNode previoudDataNode = putKey(key, dataNode);
		
		//Add the node to the LRU Queue if the key didn't exist or update the node
		if (previoudDataNode==null){
			Node node = Queue.add(key);
			dataNode.setNode(node);
		} else {
			Queue.moveFirst(previoudDataNode.getNode());
			dataNode.setNode(previoudDataNode.getNode());
		}
		
	}
	
	/**
	 * Get data from the cache
	 * @param key
	 * @return
	 */
	public static String get(String key){
		
		DataNode dataNode = memory.get(key);
		
		//Update the LRU Queue
		if (dataNode!=null){
			hitQuantity++;
			Queue.moveFirst(dataNode.getNode());
			return dataNode.getData();			
		} else {
			missQuantity++;
			return null;
		}
	}
	
	/**
	 * Remove data from the cache affecting LRU Queue
	 * @param key
	 * @return
	 */
	public static String delete(String key){
		DataNode dataNode = removeKey(key); 
		if (dataNode!=null){
			Queue.delete(dataNode.getNode());
			return key;
		} else {
			return null;
		}
		
	}
	
	/**
	 * Remove the key and value from the memory in a synchronized way without affecting the LRU Queue
	 * @param key
	 * @return
	 */
	public static synchronized DataNode removeKey(String key){
		return memory.remove(key);
	}

	public static synchronized DataNode putKey(String key, DataNode dataNode){
		return memory.put(key, dataNode);
	}
	
	/**
	 * Returns cache statistics
	 * @return
	 */
	public static String stats(){
		return "Size: "+memory.size()+", Hit: "+hitQuantity+", Miss: "+missQuantity ;
	}

	/**
	 * Initialize the cache, create the first and last elements
	 * @param f
	 * @param l
	 */
	public static void init(Node f, Node l) {
		DataNode dataNodeFirst = new DataNode();
		DataNode dataNodeLast = new DataNode();
		dataNodeFirst.setData(Constants.FIRST);
		dataNodeFirst.setNode(f);
		dataNodeLast.setData(Constants.LAST);
		dataNodeLast.setNode(l);
		memory.put(Constants.FIRST, dataNodeFirst);
		memory.put(Constants.LAST, dataNodeLast);
	}
	
}
