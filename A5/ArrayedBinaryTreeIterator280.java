// Sunny Ka Patel
// SDK438
// 11267665
// CMPT 280.A5

package lib280.tree;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedBinaryTreeIterator280<I> extends ArrayedBinaryTreePosition280 implements LinearIterator280<I> {

	// This is a reference to the tree that created this iterator object.
	ArrayedBinaryTree280<I> tree;
	
	/**
	 * Create a new iterator from a given heap.
	 * @param t The heap for which to create a new iterator.
	 */
	public ArrayedBinaryTreeIterator280(ArrayedBinaryTree280<I> t) {
		super(t.currentNode);
		this.tree = t;
	}

	/**
	 * Checks if the iterator is in the before position
	 * @return true is iterator is at before position; false otherwise
	 */
	@Override
	public boolean before() {
		return this.currentNode == 0;
	}

	/**
	 * Checks if the iterator is in the after position
	 * @return true if the iterator is in the after position; false otherwise
	 */
	@Override
	public boolean after() {
		return this.currentNode > this.tree.count || this.tree.isEmpty();
	}

	/**
	 * Moves the iterator 1 position ahead
	 * @throws AfterTheEnd280Exception if iterator is in after position and cannot move any ahead
	 */
	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if (this.after()) {
			throw new AfterTheEnd280Exception("Cannot advance the iterator in the after position");
		}
		this.currentNode++;
	}

	/**
	 * Moves the iterator to the first element of the tree
	 * @throws ContainerEmpty280Exception if tree is empty
	 */
	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if (this.tree.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot move to first item of an empty tree.");
		}
		this.currentNode = 1;
	}

	/**
	 * Moves the iterator to the before position
	 */
	@Override
	public void goBefore() {
		this.currentNode = 0;
	}

	/**
	 * Moves the iterator to the after position
	 */
	@Override
	public void goAfter() {
		if (this.tree.isEmpty()) {
			this.currentNode = 0;
		}
		this.currentNode = this.tree.count() + 1;
	}

	/**
	 * Gets the item at the current iterator position
	 * @return the item at the current iteratot position
	 * @throws NoCurrentItem280Exception if no item exists at the current iterator position
	 */
	@Override
	public I item() throws NoCurrentItem280Exception {
		if (!this.itemExists()) {
			throw new NoCurrentItem280Exception("Cannot access item when it does not exist");
		}
		return tree.items[currentNode];
	}

	/**
	 * Checks if an item exists at the current iterator position
	 * @return true if item exists at the current iterator position; false otherwise
	 */
	@Override
	public boolean itemExists() {
		return tree.count() > 0 && (currentNode > 0 && currentNode <= tree.count());
	}
}
