package jcached;
/**
 * @author Fernando Scasserra @fersca
 */
public class Queue {

	private static Node first;
	private static Node last;
	
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
	
	public static void init(){
		Node f = new Node();
		Node l = new Node();
		f.setKey("FIRST");
		f.setNext(l);
		l.setKey("LAST");
		l.setPrevious(f);
		first = f;
		last = l;
		Memory.init(f,l);
	}
	
	public static synchronized void moveFirst(Node node){
		
		if (node.getPrevious()==null) return;

		if (node.getNext()!=null){
			node.getNext().setPrevious(node.getPrevious());
			node.getPrevious().setNext(node.getNext());			
		} else{
			node.getPrevious().setNext(null);
		}
		
		node.setNext(first);
		node.setPrevious(null);
		first.setPrevious(node);
		first = node;
		
	}
	
	public static synchronized void removeLast(){
		Memory.remove(last.getKey());
		Node nextLast = last.getPrevious();
		nextLast.setNext(null);
		last.setNext(null);
		last.setPrevious(null);
		last.setKey(null);
		last = nextLast;		
	}
		
	public static Node add(String key){
		Node node = new Node();
		node.setKey(key);
		setNewFirst(node);
		return node;
	}
	
	private static synchronized void setNewFirst(Node node){
		node.setNext(first);
		first.setPrevious(node);
		first = node;
	}
	
	public static synchronized void delete(Node node){
		
		//TODO: fijarme que pasa si es el primero o el último
		
		node.getPrevious().setNext(node.getNext());
		node.getNext().setPrevious(node.getPrevious());
		node.setKey(null);
		node.setNext(null);
		node.setPrevious(null);
	}
	
}
