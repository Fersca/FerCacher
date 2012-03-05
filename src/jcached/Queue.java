package jcached;
/**
 * LRU Double linked Queue, it's updated each time an instruction is acepted by the cache. It's used to remove the LRU element.
 * @author Fernando Scasserra @fersca
 */
public class Queue {

	private static Node first;
	private static Node last;
		
	/**
	 * Initialize the first and last element in the Queue
	 */
	public static void init(){
		Node f = new Node();
		Node l = new Node();
		f.setKey(Constants.FIRST);
		f.setNext(l);
		l.setKey(Constants.LAST);
		l.setPrevious(f);
		first = f;
		last = l;
		Memory.init(f,l);
	}
	
	/**
	 * Move the element to the first position, because it was accessed.
	 * @param node
	 */
	public static synchronized void moveFirst(Node node){
		
		//Do nothing is the node is in the first position
		if (node.getPrevious()==null) return;

		//If the node isn't in the last position, update the previous and next element, else only the previous.
		if (node.getNext()!=null){
			node.getNext().setPrevious(node.getPrevious());
			node.getPrevious().setNext(node.getNext());			
		} else{
			node.getPrevious().setNext(null);
		}
		
		//Update pointers to first and last elements
		node.setNext(first);
		node.setPrevious(null);
		first.setPrevious(node);
		first = node;
		
	}
	
	/**
	 * Remove the last element.
	 */
	public static synchronized void removeLast(){
		
		//Remove the element from the memory.
		Memory.remove(last.getKey());
		
		//Update the new last node
		Node nextLast = last.getPrevious();
		nextLast.setNext(null);
		
		//Update local pointers
		last.setNext(null);
		last.setPrevious(null);
		last.setKey(null);
		last = nextLast;		
	}
		
	/**
	 * Add a new node to the queue.
	 * @param key
	 * @return
	 */
	public static Node add(String key){
		
		//Create a new node
		Node node = new Node();
		
		//Set the key
		node.setKey(key);
		
		//Synchronized update of local pointers
		setNewFirst(node);
		return node;
	}
	
	/**
	 * Synchronized access to the variables.
	 * @param node
	 */
	private static synchronized void setNewFirst(Node node){
		node.setNext(first);
		first.setPrevious(node);
		first = node;
	}
	
	/**
	 * Delete element from the queue
	 * @param node
	 */
	public static synchronized void delete(Node node){
		
		//TODO: fijarme que pasa si es el primero o el último el que quiere eliminarse, tengo sueño ahora.
		
		node.getPrevious().setNext(node.getNext());
		node.getNext().setPrevious(node.getPrevious());
		node.setKey(null);
		node.setNext(null);
		node.setPrevious(null);
	}
	
	/**
	 * Shows the Queue on screen
	 * @return
	 */
	public static String stats(){
		
		StringBuilder sb = new StringBuilder();
		
		Node node = first;
		
		while (node!=null){
			
			Node previous = node.getPrevious();
			Node next = node.getNext();
			
			String prev="";
			String nex="";
			
			if (previous!=null)
				prev = previous.getKey(); 
			else 
				prev = "null";
			
			if (next!=null)
				nex = next.getKey(); 
			else 
				nex = "null";

			sb.append("Key: "+node.getKey()+", prev: "+prev+", next:"+nex);
		
			node = next;
		}
		
		return sb.toString();
	}
	
	
}
