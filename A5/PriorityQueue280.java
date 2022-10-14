// Sunny Ka Patel
// SDK438
// 11267665
// CMPT 280.A5

package lib280.dispenser;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.tree.ArrayedBinaryTreeIterator280;
import lib280.tree.IterableArrayedHeap280;

public class PriorityQueue280<I extends Comparable<? super I>> {
	
	// This is the heap that we are restricting.
	// Items in the priority queue get stored in the heap.
	protected IterableArrayedHeap280<I> items;
	
	
	/**
	 * Create a new priority queue with a given capacity.
	 * @param cap The maximum number of items that can be in the queue.
	 */
	public PriorityQueue280(int cap) {
		items = new IterableArrayedHeap280<I>(cap);
	}

	/**
	 * Gives a string representation of the priority queue
	 * @return a string representation of the priority queue
	 */
	public String toString() {
		return items.toString();	
	}

	/**
	 * Inserts the given item to the priority queue
	 * @param g the item to be inserted
	 * @precond queue is not full
	 * @throws ContainerFull280Exception when the queue is full
	 */
	public void insert(I g) throws ContainerFull280Exception {
		// check pre-condition
		if (items.isFull()) {
			throw new ContainerFull280Exception("Cannot insert item to an already full queue!");
		}
		items.insert(g);
	}

	/**
	 * Checks if the queue is full or not
	 * @return true if queue is full; false otherwise
	 */
	public boolean isFull() {
		return items.isFull();
	}

	/**
	 * Checks if the queue is empty or not
	 * @return true if queue is empty; false otherwise
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}

	/**
	 * Obtain the number of items in the queue
	 * @return the number of items in the queue
	 */
	public int count() {
		return items.count();
	}

	/**
	 * Obtains the largest (highest priority) item in the queue
	 * @precond queue is not empty
	 * @return the largest (highest priority) item in the queue
	 * @throws ContainerEmpty280Exception when the queue is empty
	 */
	public I maxItem() {
		// check pre-condition
		if (items.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot obtain largest (highest priority) item from an empty queue!");
		}
		return items.item();
	}

	/**
	 * Obtains the smallest (lowest priority) item in the queue
	 * @precond queue is not empty
	 * @return the smallest (lowest priority) item in the queue
	 * @throws ContainerEmpty280Exception when the queue is empty
	 */
	public I minItem() {
		// check pre-condition
		if (items.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot obtain smallest (lowest priority) item from an empty queue.");
		}

		// get the inner heap iterator and set it to the head of the heap
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();
		iter.goFirst();

		// set the minimum item to be the head initially and then go forth
		I min = iter.item();
		iter.goForth();

		// while item exists...
		while (iter.itemExists()) {
			// compare the current item with the smallest item seen so far
			if (iter.item().compareTo(min) < 0) {
				// if it is smaller then set the current item to be the smallest item
				min = iter.item();
			}
			// else go forth
			iter.goForth();
		}
		// return the smallest item
		return min;
	}

	/**
	 * Delete the largest (highest priority) item from the queue
	 * @precond queue is not empty
	 * @throws ContainerEmpty280Exception when queue is empty
	 */
	public void deleteMax() {
		// check pre-condition
		if (items.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete largest (highest priority) item from an empty queue!");
		}
		items.deleteItem();
	}

	/**
	 * Deletes the smallest (lowest priority) item from the queue
	 * @precond queue is not empty
	 */
	public void deleteMin() {
		// check pre-condition
		if (items.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete smallest (lowest priority) item from an empty queue!");
		}

		// get the smallest item
		I min = minItem();

		// get the inner heap iterator and set it to the head of the heap
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();
		iter.goFirst();

		// while items exist...
		while (iter.itemExists()) {
			// if current item is the smallest item then delete it and return
			if (iter.item() == min) {
				items.deleteAtPosition(iter);
				return;
			}
			// else go forth
			iter.goForth();
		}
	}

	/**
	 * Deletes all the occurrences of the largest (highest priority) item of the queue
	 * @precond queue is not empty
	 */
	public void deleteAllMax() {
		//  check pre-condition
		if (items.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete largest (highest priority) items from an empty queue!");
		}

		// get the largest item
		I max = items.item();

		// while item exists and the head of the queue is still equal to the largest item, delete the head
		while (items.itemExists() && items.item().compareTo(max) == 0) {
			items.deleteItem();
		}
	}

	public static void main(String args[]) {
		class PriorityItem<I> implements Comparable<PriorityItem<I>> {
			I item;
			Double priority;
						
			public PriorityItem(I item, Double priority) {
				super();
				this.item = item;
				this.priority = priority;
			}

			public int compareTo(PriorityItem<I> o) {
				return this.priority.compareTo(o.priority);
			}
			
			public String toString() {
				return this.item + ":" + this.priority;
			}
		}
		
		PriorityQueue280<PriorityItem<String>> Q = new PriorityQueue280<PriorityItem<String>>(5);
		
		// Test isEmpty()
		if( !Q.isEmpty()) 
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");
		
		// Test insert() and maxItem()
		Q.insert(new PriorityItem<String>("Sing", 5.0));
		if( Q.maxItem().item.compareTo("Sing") != 0) {
			System.out.println("??Error: Front of queue should be 'Sing' but it's not. It is: " + Q.maxItem().item);
		}
		
		// Test isEmpty() when queue not empty
		if( Q.isEmpty()) 
			System.out.println("Error: Queue is not empty, but isEmpty() says it is.");
		
		// test count()
		if( Q.count() != 1 ) {
			System.out.println("Error: Count should be 1 but it's not.");			
		}

		// test minItem() with one element
		if( Q.minItem().item.compareTo("Sing")!=0) {
			System.out.println("Error: min priority item should be 'Sing' but it's not.");
		}	
		
		// insert more items
		Q.insert(new PriorityItem<String>("Fly", 5.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Dance", 3.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Jump", 7.0));
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");
		
		if(Q.minItem().item.compareTo("Dance") != 0) System.out.println("minItem() should be 'Dance' but it's not.");
		
		if( Q.count() != 4 ) {
			System.out.println("Error: Count should be 4 but it's not.");			
		}
		
		// Test isFull() when not full
		if( Q.isFull()) 
			System.out.println("Error: Queue is not full, but isFull() says it is.");
		
		Q.insert(new PriorityItem<String>("Eat", 10.0));
		if( Q.maxItem().item.compareTo("Eat")!=0) System.out.println("Front of queue should be 'Eat' but it's not.");

		if( !Q.isFull()) 
			System.out.println("Error: Queue is full, but isFull() says it isn't.");

		// Test insertion on full queue
		try {
			Q.insert(new PriorityItem<String>("Sleep", 15.0));
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got none.");
		}
		catch(ContainerFull280Exception e) {
			// Expected exception
		}
		catch(Exception e) {
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got a different exception.");
			e.printStackTrace();
		}
		
		// test deleteMin
		Q.deleteMin();
		if(Q.minItem().item.compareTo("Sing") != 0) System.out.println("Min item should be 'Sing', but it isn't.");
		
		Q.insert(new PriorityItem<String>("Dig", 1.0));
		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		// Test deleteMax
		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");

		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		Q.deleteMin();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		Q.insert(new PriorityItem<String>("Scream", 2.0));
		Q.insert(new PriorityItem<String>("Run", 2.0));

		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");
		
		// test deleteAllMax()
		Q.deleteAllMax();
		if( Q.maxItem().item.compareTo("Scream")!=0) System.out.println("Front of queue should be 'Scream' but it's not.");
		if( Q.minItem().item.compareTo("Scream") != 0) System.out.println("minItem() should be 'Scream' but it's not.");
		Q.deleteAllMax();

		// Queue should now be empty again.
		if( !Q.isEmpty()) 
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");

		System.out.println("Regression test complete.");
	}
}
