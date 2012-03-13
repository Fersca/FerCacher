package test;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import ferCacher.Memory;
import ferCacher.Node;
import ferCacher.Queue;

/**
 * @author Fernando Scasserra @fersca
 */
public class MemoryTests {

	@Before
	/**
	 * Init the LRU-Queue
	 */
	public void setUp() throws Exception {
		Queue.init();
	}

	@Test
	/**
	 * Tests if the put on the memory works
	 */
	public void testPut() {
		int cant1 = Queue.getElements().size();
		Memory.put("car", "red");
		int cant2 = Queue.getElements().size();
		assert (cant1+1)==cant2;
	}

	@Test
	/**
	 * Tests if the get on the memory works
	 */
	public void testGet() {
		Memory.put("bird", "eagle");
		assert Memory.get("bird")=="eagle";
	}

	@Test
	/**
	 * Tests if the delete on the memory works
	 */

	public void testDelete() {
		Memory.put("tree", "leaf");
		Memory.delete("tree");
		assert Memory.get("tree")==null;
	}

	@Test
	/**
	 * Test it the Last inserted Node goes to the first position in the queue
	 */
	public void testMoveFirstOnInsert() {
		Memory.put("name", "peter");
		LinkedList<Node> list = (LinkedList<Node>)Queue.getElements();
		Node node = list.getFirst();
		assert node.getKey()=="name";
		assert node.getNext()!=null;
		assert node.getPrevious()==null;
	}

	@Test
	/**
	 * Test it the Last get Node goes to the first position in the queue
	 */
	public void testMoveFirstOnget() {
		
		//Add two elements to the cache
		Memory.put("cat", "cat");
		Memory.put("dog", "dog");
		
		//Check that the first in the queue is the last added
		LinkedList<Node> list = (LinkedList<Node>)Queue.getElements();
		Node node = list.getFirst();
		assert node.getKey()=="dog";
		assert node.getNext()!=null;
		assert node.getNext().getKey()=="cat";
		assert node.getPrevious()==null;
		
		//Get the cat to set it to the first position
		Memory.get("cat");
		
		//Check if the last get node goes to the first position and the old first is in second
		list = (LinkedList<Node>)Queue.getElements();
		node = list.getFirst();
		assert node.getKey()=="cat";
		assert node.getNext()!=null;
		assert node.getNext().getKey()=="dog";
		assert node.getPrevious()==null;
		
	}

	//TODO: test the removeLast method from the queue
	
	//TODO: test each method with concurrency
	
}
