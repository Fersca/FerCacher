package jcached;
/**
 * Holds the stored data and a reference to the LRU Queue Node
 * @author Fernando Scasserra @fersca
 */
public class DataNode {

	private String Data;
	private Node node;
	
	public void setData(String data) {
		Data = data;
	}
	public String getData() {
		return Data;
	}
	public void setNode(Node node) {
		this.node = node;
	}
	public Node getNode() {
		return node;
	}
	
}
