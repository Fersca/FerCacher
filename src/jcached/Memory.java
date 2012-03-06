package jcached;

import java.util.HashMap;

/**
 * Memory class, holds the HashMap that has the stored elements
 * @author Fernando Scasserra @fersca
 */
public class Memory {

	//HashMap to store the data objects
	private static HashMap<String, DataNode> memory = new HashMap<String, DataNode>();
	
	//Max number of objects in the cache
	private static int maxQuantity = 10;
	
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
		DataNode previoudDataNode = memory.put(key, dataNode);
		
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
			
			//TODO: hacer que esto ponga la clave en un lugar y que luego un thread se encargue de actualizar la LRU
			//No se va a tener perfectamente actualizala la LRU pero no importa. el get no va a ser synchronizado.
			
			//pensar si todas las operaciones no podrían ser iguales, la de update y la de delete también. que todas sean en diferido y se guarden en una cola.
			
			Queue.moveFirst(dataNode.getNode());
			
			return dataNode.getData();			
		} else {
			return null;
		}
	}

	/**
	 * Remove data from the cache without affecting LRU Queue
	 * @param key
	 */
	public static void remove(String key){
		memory.remove(key);
	}
	
	/**
	 * Remove data from the cache affecting LRU Queue
	 * @param key
	 * @return
	 */
	public static String delete(String key){
		DataNode dataNode = memory.remove(key); 
		if (dataNode!=null){
			Queue.delete(dataNode.getNode());
			return key;
		} else {
			return null;
		}
		
	}

	/**
	 * Returns cache statistics
	 * @return
	 */
	public static int stats(){
		return memory.size();
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
