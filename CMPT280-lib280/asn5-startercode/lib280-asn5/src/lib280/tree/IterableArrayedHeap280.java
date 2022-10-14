// Sunny Ka Patel
// SDK438
// 11267665
// CMPT 280.A5

package lib280.tree;

import lib280.exception.InvalidArgument280Exception;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I> {

	/**
	 * Create an iterable heap with a given capacity.
	 * @param cap The maximum number of elements that can be in the heap.
	 */
	public IterableArrayedHeap280(int cap) {
		super(cap);
	}

	/**
	 * Returns a new iterator object for the tree
	 * @return a new iterator object for the tree
	 */
	public ArrayedBinaryTreeIterator280<I> iterator() {
		return new ArrayedBinaryTreeIterator280<I>(this);
	}

	/**
	 * Deletes the item at the current iterator position
	 * @param iter an iterator for the tree
	 */
	public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> iter) {
		// Delete the item at the iter.currentNode by moving in the last item
		// if there is more than one item, and we aren't deleting the last item,
		// copy the last item in the array to the current iterator position
		if (!iter.itemExists()) {
			throw new InvalidArgument280Exception("The given position is invalid!");
		}

		if (this.count > 1 && this.itemExists()) {
			this.items[iter.currentNode] = this.items[count];
		}
		this.count--;

		// if we deleted the last remaining item, make the current item invalid and just return
		if (this.count == 0) {
			this.currentNode = 0;
			return;
		}

		// get the index of the current iterator position
		int iterPosition = iter.currentNode;

		// while offset iterPosition has a left child...
		while (findLeftChild(iterPosition) <= this.count) {
			// select the left child of the iterator offset
			int child = findLeftChild(iterPosition);

			// if the right child exists and is larger, select it instead
			if (child + 1 <= this.count && items[child].compareTo(items[child + 1]) < 0) {
				child++;
			}

			// if the parent is smaller than the root
			if (items[iterPosition].compareTo(items[child]) < 0) {
				// swap them
				I temp = items[iterPosition];
				items[iterPosition] = items[child];
				items[child] = temp;
				iterPosition = child;
			}
			else return;
		}
	}
}
