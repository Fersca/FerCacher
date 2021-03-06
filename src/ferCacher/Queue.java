package ferCacher;

import java.util.LinkedList;
import java.util.List;

/**
 * LRU Double linked Queue, it's updated each time an instruction is accepted by the cache. It's used to remove the LRU element.
 * 
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
	public static void moveFirst(Node node){
		
		//Do nothing is the node is in the first position
		if (node.getPrevious()==null) return;
		
		Node previous = node.getPrevious();
		
		//Lock the node and the previous one
		synchronized(node){
			synchronized(previous){	
			
				//If the node isn't in the last position, update the previous and next element, else only the previous.
				if (node.getNext()!=null){
					
					Node next = node.getNext();
					synchronized(next){
						next.setPrevious(node.getPrevious());
					}
					
					previous.setNext(node.getNext());
								
				} else{
					previous.setNext(null);
				}
				
			}
		}

		//Set as first node
		setAsFirst(node);
	}
	
	/**
	 * Remove the last element.
	 */
	public static void removeLast(){
		
		Node l = setNewLastAndGetOldLast();
		
		//Remove the element from the memory.
		Memory.removeKey(l.getKey());

	}
		
	/**
	 * Set the new last node and returns the previous one
	 * @return
	 */
	private static synchronized Node setNewLastAndGetOldLast(){
		Node l = last;
		Node prev = last.getPrevious();
		synchronized(prev){
			prev.setNext(null);
			last = prev;
		}
		return l;
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
		
		//Set as first node
		setAsFirst(node);
		return node;
	}
	
	/**
	 * Set the node as the first one
	 * @param node
	 */
	private static synchronized void setAsFirst(Node node){
		
		synchronized(node){
			node.setNext(first);
			node.setPrevious(null);
		}
		
		first.setPrevious(node);
		first = node;		
	}
	
	/**
	 * Delete element from the queue
	 * @param node
	 */
	public static void delete(Node node){
				
		synchronized(node){
			
			Node previous = node.getPrevious();
			Node next = node.getNext();
			
			if (previous!=null && next!=null){
				synchronized(previous){
					synchronized(next){
						next.setPrevious(previous);
						previous.setNext(next);
					}
				}				
			} else if (previous!=null){
				synchronized(previous){
					previous.setNext(null);
				}				
			} else {
				synchronized(next){
					next.setPrevious(null);
				}				
			}
			
			//Set the entire object to null
			node.setKey(null);
			node.setNext(null);
			node.setPrevious(null);
			
		}
		
	}
	
	/**
	 * Shows the Queue on screen
	 * @return
	 */
	public static String stats(){
		
		StringBuilder sb = new StringBuilder();
		
		for (Node node : getElements()) {
			
			String prev = node.getPrevious()!=null	?node.getPrevious().getKey():"Null";
			String next = node.getNext()!=null		?node.getNext().getKey()	:"Null";
			
			sb.append("\nKey: "+node.getKey()+", previous: "+prev+", next: "+next);
			
		}
	
		return sb.toString();
	}
	
	/**
	 * Return a LinkedList equivalent to the LRU-Queue
	 * @return
	 */
	public static List<Node> getElements(){
		
		Node node = first;
		List<Node> list = new LinkedList<Node>();
		
		while (node!=null){
			list.add(node);
			node = node.getNext();
		}
		
		return list;
	}
	
}
