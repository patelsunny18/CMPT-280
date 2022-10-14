// Sunny Ka Patel
// SDK438
// 11267665
// CMPT 280.A4

package lib280.dispenser;

import lib280.base.Dispenser280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.ArrayedBinaryTree280;

public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {

    /**
     * Constructor.
     *
     * @param cap Maximum number of elements that can be in the lib280.tree.
     */
    public ArrayedHeap280(int cap) {
        super(cap);
        this.items = (I[]) new Comparable[capacity + 1];
    }

    /**
     * add the item to the heap
     * @param x item to be inserted into the data structure
     * @throws ContainerFull280Exception when inserting to a already full heap
     */
    @Override
    public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
        // add the item normally to the end of the list
        if (this.isFull()) {
            throw new ContainerFull280Exception("Cannot add item to an already full tree!");
        }
        else {
            count++;
            items[count] = x;
        }
        // bring the cursor to the head of the array
        this.currentNode = 1;

        // when heap contains only 1 item
        if (count == 1) {
            return;
        }

        // get the index of the added item and its corresponding parent
        int nodeIndex = count;
        int parentIndex = findParent(nodeIndex);

        // swap until the added item is bigger than its parent element
        while (nodeIndex > 1 && items[nodeIndex].compareTo(items[parentIndex]) > 0) {
            I temp = items[parentIndex];
            items[parentIndex] = items[nodeIndex];
            items[nodeIndex] = temp;

            // update the indices
            nodeIndex = parentIndex;
            parentIndex = findParent(nodeIndex);
        }
    }

    /**
     * Deletes the largest item from the heap
     * @throws NoCurrentItem280Exception when heap is empty
     */
    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        if (this.isEmpty()) {
            throw new ContainerEmpty280Exception("Cannot delete an item from an empty heap.");
        }

        // if there are more than 1 items in the heap and we are not
        // deleting the last item then copy the last item to the current position
        if (count > 1) {
            currentNode = 1;
            items[currentNode] = items[count];
        }
        this.count--;

        // if we deleted the last item in the heap then update the current item and return
        if (count == 0) {
            currentNode = 0;
            return;
        }

        int nodeIndex = 1;
        while (findLeftChild(nodeIndex) <= count) {
            // select the left child first
            int leftChild = findLeftChild(nodeIndex);

            // if right child exists and is larger then select it instead
            if (leftChild + 1 <= count && items[leftChild].compareTo(items[leftChild +1]) < 0) {
                leftChild++;
            }

            // if the parent item is smaller than the root then swap them
            if (items[nodeIndex].compareTo(items[leftChild]) < 0) {
                I temp = items[nodeIndex];
                items[nodeIndex] = items[leftChild];
                items[leftChild] = temp;
                nodeIndex = leftChild;
            }
            else {
                return;
            }
        }
    }

    /**
     * Helper for the regression test.  Verifies the heap property for all nodes.
     */
    private boolean hasHeapProperty() {
        for (int i = 1; i <= count; i++) {
            if (findRightChild(i) <= count) {  // if i Has two children...
                // ... and i is smaller than either of them, , then the heap property is violated.
                if (items[i].compareTo(items[findRightChild(i)]) < 0) return false;
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else if (findLeftChild(i) <= count) {  // if n has one child...
                // ... and i is smaller than it, then the heap property is violated.
                if (items[i].compareTo(items[findLeftChild(i)]) < 0) return false;
            } else break;  // Neither child exists.  So we're done.
        }
        return true;
    }

    /**
     * Regression test
     */
    public static void main(String[] args) {

        ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);

        // Empty heap should have the heap property.
        if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");

        // Insert items 1 through 10, checking after each insertion that
        // the heap property is retained, and that the top of the heap is correctly i.
        for (int i = 1; i <= 10; i++) {
            H.insert(i);
            if (H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
            if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");
        }

        // Remove the elements 10 through 1 from the heap, chekcing
        // after each deletion that the heap property is retained and that
        // the correct item is at the top of the heap.
        for (int i = 10; i >= 1; i--) {
            // Remove the element i.
            H.deleteItem();
            // If we've removed item 1, the heap should be empty.
            if (i == 1) {
                if (!H.isEmpty()) System.out.println("Expected the heap to be empty, but it wasn't.");
            } else {
                // Otherwise, the item left at the top of the heap should be equal to i-1.
                if (H.item() != i - 1) System.out.println("Expected current item to be " + i + ", got " + H.item());
                if (!H.hasHeapProperty()) System.out.println("Does not have heap property.");
            }
        }

        System.out.println("Regression Test Complete.");
    }

}
