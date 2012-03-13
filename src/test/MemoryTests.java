package test;

import java.util.LinkedList;
import static org.junit.Assert.*;

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
	public void testMemoryPut() {
		int cant1 = Queue.getElements().size();
		Memory.put("car", "red");
		int cant2 = Queue.getElements().size();
		if ((cant1+1)!=cant2)
			fail("Different size");
	}

	@Test
	/**
	 * Tests if the get on the memory works
	 */
	public void testMemoryGet() {
		Memory.put("bird", "eagle");
		if (!Memory.get("bird").equals("eagle"))
			fail("Invalid value: "+Memory.get("bird"));
	}

	@Test
	/**
	 * Tests if the delete on the memory works
	 */

	public void testMemoryDelete() {
		Memory.put("tree", "leaf");
		Memory.delete("tree");
		if (Memory.get("tree")!=null)
			fail("Invalid element, it should be null");
	}

	@Test
	/**
	 * Test it the Last inserted Node goes to the first position in the queue
	 */
	public void testQueueMoveFirstOnInsert() {
		Memory.put("name", "peter");
		LinkedList<Node> list = (LinkedList<Node>)Queue.getElements();
		Node node = list.getFirst();
		assertEquals(node.getKey(), "name");
		assertTrue(node.getNext()!=null);
		assertTrue(node.getPrevious()==null);
	}

	@Test
	/**
	 * Test it the Last get Node goes to the first position in the queue
	 */
	public void testQueueMoveFirstOnGet() {
		
		//Add two elements to the cache
		Memory.put("cat", "cat");
		Memory.put("dog", "dog");
		
		//Check that the first in the queue is the last added
		LinkedList<Node> list = (LinkedList<Node>)Queue.getElements();
		Node node = list.getFirst();
		assertTrue (node.getKey()=="dog");
		assertTrue (node.getNext()!=null);
		assertTrue (node.getNext().getKey()=="cat");
		assertTrue (node.getPrevious()==null);
		
		//Get the cat to set it to the first position
		Memory.get("cat");
		
		//Check if the last get node goes to the first position and the old first is in second
		list = (LinkedList<Node>)Queue.getElements();
		node = list.getFirst();
		assertTrue (node.getKey()=="cat");
		assertTrue (node.getNext()!=null);
		assertTrue (node.getNext().getKey()=="dog");
		assertTrue (node.getPrevious()==null);
		
	}

	@Test
	/**
	 * Test it the Last inserted Node that is in the first position remains as first when it's get again.
	 */
	public void testQueueMoveFirstWhenIsFirst() {
		
		//Add an element
		Memory.put("surname", "parker");
		LinkedList<Node> list = (LinkedList<Node>)Queue.getElements();
		Node node = list.getFirst();
		
		//Check if it's in the first position
		assertTrue (node.getKey()=="surname");
		assertTrue (node.getNext()!=null);
		assertTrue (node.getPrevious()==null);
		
		//Get the first element
		Memory.get("surname");
		node = list.getFirst();
		
		//Check if the element remains in the first position
		assertTrue (node.getKey()=="surname");
		assertTrue (node.getNext()!=null);
		assertTrue (node.getPrevious()==null);
	}

	@Test
	/**
	 * Test if a new last element is created and if the last element is destroyed from the queue when we execute the 
	 * remove last from queue method.
	 */
	public void testQueueRemoveLastElement() {
		
		//Add an element
		Memory.put("animal", "bird");
		LinkedList<Node> list = (LinkedList<Node>)Queue.getElements();
		Node node = list.getLast();
		
		//Check if the last element has a null pointer in the next reference
		assertTrue (node.getNext()==null);
		
		//Execute the remove last element
		String value = node.getKey();
		Queue.removeLast();
		list = (LinkedList<Node>)Queue.getElements();
		node = list.getLast();
		assertTrue (node.getKey()!=value);
			
		//Check the next pointer of the new last element
		assertTrue (node.getNext()==null);
		
	}

	@Test
	/**
	 * Test if the Queue remains consistent after a deletion 
	 */
	public void testQueueConsistencyOnDeletion() {
		
		//Add elements
		Memory.put("car1", "ferrari");
		Memory.put("car2", "toyota");
		Memory.put("car3", "honda");
		
		//Get the first version of the queue
		LinkedList<Node> list1 = (LinkedList<Node>)Queue.getElements();
		
		//Create a backup listo to compare
		LinkedList<String> backupList = new LinkedList<String>();
		for (Node node2 : list1) {
			backupList.add(node2.getKey());
		}
		
		//Delete the element
		Memory.delete("car1"); 
		
		//Get the new list of elements
		LinkedList<Node> list2 = (LinkedList<Node>)Queue.getElements();
		
		//Create the second backup queue
		LinkedList<String> backupList2 = new LinkedList<String>();
		for (Node node2 : list2) {
			backupList2.add(node2.getKey());
		}
		
		//Check the queue sizes
		if ((backupList.size()-backupList2.size())!=1)
			fail("Incorrect sizes: "+backupList2.size()+", "+backupList.size());
			
		//Remove the previous removed element from the first queue
		backupList.remove("car1");
		
		//Compare the queue elements
		for (int i = 0;i<backupList.size();i++) {
			if (!(backupList.get(i).equals(backupList2.get(i))))
				fail("Different Elements");
		}
				
	}
	
	//TODO: test each method with concurrency
	
}
