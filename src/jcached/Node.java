package jcached;
/**
 * The LRU Queue Node. Has a Key and a pointer to the previous and next node
 * @author Fernando Scasserra @fersca
 */
public class Node {

	private String key;
	private Node previous;
	private Node next;
	
	public void setKey(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void setNext(Node next) {
		this.next = next;
	}
	public Node getNext() {
		return next;
	}
	public void setPrevious(Node previous) {
		this.previous = previous;
	}
	public Node getPrevious() {
		return previous;
	}

	
}
