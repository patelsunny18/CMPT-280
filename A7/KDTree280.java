/*
Sunny Ka Patel
SDK438
11267665
CMPT 280.A7
 */

package lib280.tree;
import lib280.exception.*;
import lib280.base.NDPoint280;
import lib280.list.LinkedList280;

/**
 * The k-D tree class
 * @param <I> a generic type parameter
 */
public class KDTree280<I extends Comparable<? super I>> extends OrderedSimpleTree280<I> {
    /**
     * The root node of the KDTree280
     */
    private KDNode280<I> rootNode;

    /**
     * The dimensionality of the KDTree280
     */
    private int k;


    /**
     * Sets the root node of the KDTree280 to the new newRootNode node
     * @param newRootNode the new root node
     */
    private void setRootNode(KDNode280<I> newRootNode){
        this.rootNode = newRootNode;
    }


    /**
     * Gets the dimensionality of the KDTree280
     * @return the dimensionality of the KDTree280
     */
    private int getDimension() {
        return this.k;
    }

    /**
     * A public constructor for the KDTree280
     * @param nodes an array of KDNodes280
     */
    public KDTree280(KDNode280<I>[] nodes) {
        if (nodes.length == 0) {
            return;
        }
        this.setRootNode(KDTree280(nodes, 0, nodes.length - 1, 0));
    }


    /**
     * A private constructor for the KDTree280
     * @param pointArray an array of k-dimensional points
     * @param left offset of start of subarray  from which to build a KDTree280
     * @param right offset of end of subarray  from which to build a KDTree280
     * @param depth the current depth
     * @return the built KDTree280
     */
    private KDNode280<I> KDTree280(KDNode280<I>[] pointArray, int left, int right, int depth) {
        // get the dimensionality of the KDTree280
        this.k = pointArray[0].getDimension();

        // if it turns out that the right offset of the array
        // is smaller than the left offset then return null
        if (right < left) {
            return null;
        }
        else {
            // set the axis based on the current depth
            int d = depth % this.k;
            int medianOffset = (left + right) / 2;

            // put the median element in the correct position
            jSmallest(pointArray, left, right, d, medianOffset);

            // create a new KDNode280 and construct the subtrees
            KDNode280<I> newNode = new KDNode280<>(this.k);
            newNode.setItem(pointArray[medianOffset].item());
            newNode.setLeftNode(KDTree280(pointArray, left, medianOffset - 1, depth + 1));
            newNode.setRightNode(KDTree280(pointArray, medianOffset + 1, right, depth + 1));
            return newNode;
        }
    }


    /**
     * Algorithm to set the elements of the given array in their appropriate position
     * @param list array containing the KDNode280s to be inserted in the KDTree280
     * @param left offset of the starting of the list; i.e. 0
     * @param right offset of the end of the list; i.e. list.length - 1
     * @param d denotes the k within the n-dimensional points to be used for comparing the NDPoints280
     * @param j index in KDList where the corresponding appropriate value should be placed
     */
    private void jSmallest(KDNode280<I>[] list, int left, int right, int d, int j) {
        // if the right offset is bigger than the left offset...
        if (right > left) {
            // partition the subarray and get the index of the pivot
            int pivotIndex = partition(list, left, right, d);

            // if position j is smaller than the pivot index then
            // search recursively for j between left and pivotIndex - 1
            if (j < pivotIndex) {
                jSmallest(list, left, pivotIndex - 1, d, j);
            }
            // else search recursively for between pivotIndex + 1 and right
            else if (j > pivotIndex) {
                jSmallest(list, pivotIndex + 1, right, d,j);
            }
        }
    }


    /**
     * Algorithm to partition the given array using its last element as pivot
     * @param list an array containing the KDNodes280s to be inserted in the KDTree280
     * @param left lower limit of the array to be partitioned
     * @param right upper limit of the array to be partitioned
     * @param d denotes the k within the n-dimensional points to be used for comparing the NDPoints280
     * @return the offset at which the pivot element ended up
     */
    private int partition(KDNode280<I>[] list, int left, int right, int d) throws DuplicateItems280Exception {
        // get the element at the end of the list
        KDNode280<I> pivot = list[right];

        // set the swapOffset to the left index
        int swapOffset = left;

        for (int i = left; i < right; i++) {
            // case to check for duplicate values
            if (list[i].item().compareTo(pivot.item()) == 0) {
                throw new DuplicateItems280Exception("Duplicate points found! All points need to be unique!");
            }

            // if the item at pivot is greater than or equal to item at index i in respect to the k...
            if (pivot.item().compareByDim(d, list[i].item()) >= 0) {
                // swap the item at swapOffset with the item at offset i
                swap(list, i, swapOffset);

                // move swapOffset to the right
                swapOffset += 1;
            }
        }
        // switch the item at right with the item at swapOffset
        swap(list, right, swapOffset);

        // return the offset where pivot was at the last
        return swapOffset;
    }


    /**
     * Swaps the item at location idx1 with the item at location idx2 of the list
     * @param list the list for which the swap needs to be done
     * @param idx1 the offset of the item to be swapped to idx2
     * @param idx2 the offset of the item to be swapped to idx1
     */
    private void swap(KDNode280<I>[] list, int idx1, int idx2) {
        KDNode280<I> temp = list[idx1];
        list[idx1] = list[idx2];
        list[idx2] = temp;
    }


    /**
     * Performs a range search on the KDTree280
     * @param lowerLimit the lower limit of the range search
     * @param upperLimit the upper limit of the range search
     * @return a string containing the items between the lowerLimit and the upperLimit
     */
    public String rangeSearch(KDNode280<I> lowerLimit, KDNode280<I> upperLimit) {
        // a linked list to store the KDNode280s within the range
        LinkedList280<KDNode280<I>> l = new LinkedList280<>();

        // the function which performs the range search
        rangeSearch(this.rootNode, lowerLimit, upperLimit, l);

        String result = "";

        // get the items within the linked-list
        l.goFirst();
        while (l.itemExists()) {
            result += l.item() + "\n";
            l.goForth();
        }
        // return the string containing the result
        return result;
    }


    /**
     * Performs a range search on the KDTree280
     * @param root the root node of the tree (subtree)
     * @param lowerLimit the lower limit of the range search
     * @param upperLimit the upper limit of the range search
     * @param l a linked list to store all the values that are between the lower and upper limit
     */
    private void rangeSearch(KDNode280<I> root, KDNode280<I> lowerLimit, KDNode280<I> upperLimit, LinkedList280<KDNode280<I>> l) {
        // if the root is null then return
        if (root == null) {
            return;
        }
        // if the root is greater than the lowerLimit and...
        if (compare(root, lowerLimit)) {
            // less than the upperLimit then add it to the list
            if (compare(upperLimit, root)) {
                l.insert(root);
            }
        }
        // perform a range search in the left subtree
        rangeSearch(root.getLeftNode(), lowerLimit, upperLimit, l);

        // perform a range search in the right subtree
        rangeSearch(root.getRightNode(), lowerLimit, upperLimit, l);
    }

    /**
     * Helper function to compare two KDNode280
     * @param n1 the first KDNode280
     * @param n2 the second KDNode280
     * @return true if the i'th coordinate of n1 is not smaller than the i'th coordinate of n2; false otherwise
     * @throws InvalidArgument280Exception when the dimensionality of the given nodes are different
     */
    private boolean compare(KDNode280<I> n1, KDNode280<I> n2) throws InvalidArgument280Exception {
        // first check if their dimensionality are same or not
        if (n1.item().dim() != n2.item().dim()) {
            throw new IllegalArgumentException("The dimensionality of the given nodes doesn't match!");
        }
        for (int i = 0; i < this.getDimension(); i++) {
            // if the i'th coordinate of n1 is smaller than the i'th coordinate of n2, return false
            if (n1.item().idx(i) < n2.item().idx(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a string representation of the KDTree280 by level
     * @return a string representation of the KDTree280 by level
     */
    public String toString() {
        return this.rootNode.toStringByLevel(1);
    }

    public static void main(String[] args) {
        System.out.println("\n<--------------- Testing creating a k-D tree for k = 2 --------------->\n");

        KDNode280[] pointsArray = new KDNode280[7];
        Double[][] setOfPoints = {{5.0, 2.0}, {9.0, 10.0}, {11.0, 1.0}, {4.0, 3.0}, {2.0, 12.0}, {3.0, 7.0}, {1.0, 5.0}};

        for (int i = 0; i < pointsArray.length; i++) {
            pointsArray[i] = new KDNode280(setOfPoints[i]);
        }

        KDTree280 T1 = new KDTree280(pointsArray);

        System.out.println("Input 2D points:");

        for (int i = 0; i < pointsArray.length; i++) {
            System.out.println(pointsArray[i]);
        }

        System.out.println("\nThe 2D tree built from these points is:");
        System.out.println(T1);

        System.out.println("\n<--------------- Testing creating a k-D tree for k = 3 --------------->\n");

        KDNode280[] pointsArray2 = new KDNode280[8];
        Double[][] setOfPoints2 = {{1.0, 12.0, 0.0}, {18.0, 1.0, 2.0}, {2.0, 13.0, 16.0}, {7.0, 3.0, 3.0},
                {3.0, 7.0, 5.0}, {16.0, 4.0, 4.0}, {4.0, 6.0, 1.0}, {5.0, 5.0, 17.0}};

        for (int i = 0; i < pointsArray2.length; i++) {
            pointsArray2[i] = new KDNode280(setOfPoints2[i]);
        }

        KDTree280 T2 = new KDTree280(pointsArray2);

        System.out.println("Input 3D points:");

        for (int i = 0; i < pointsArray2.length; i++) {
            System.out.println(pointsArray2[i]);
        }

        System.out.println("\nThe 3D tree built from these points is:");
        System.out.println(T2);

        System.out.println("\n<--------------- Testing range search on a k-D tree for k = 3 --------------->\n");

        Double[] l1 = {0.0, 1.0, 0.0};
        Double[] l2 = {4.0, 6.0, 3.0};

        KDNode280 lowLimit = new KDNode280(l1);
        KDNode280 upperLimit = new KDNode280(l2);

        System.out.println("Looking for points between (0.0, 1.0, 0.0) and (4.0, 6.0, 3.0).\nFound:");
        System.out.println(T2.rangeSearch(lowLimit, upperLimit));

        Double[] l3 = {0.0, 1.0, 0.0};
        Double[] l4 = {8.0, 7.0, 4.0};

        KDNode280 lowLimit2 = new KDNode280(l3);
        KDNode280 upperLimit2 = new KDNode280(l4);

        System.out.println("Looking for points between (0.0, 1.0, 0.0) and (8.0, 7.0, 4.0).\nFound:");
        System.out.println(T2.rangeSearch(lowLimit2, upperLimit2));

        Double[] l5 = {0.0, 1.0, 0.0};
        Double[] l6 = {17.0, 9.0, 10.0};

        KDNode280 lowLimit3 = new KDNode280(l5);
        KDNode280 upperLimit3 = new KDNode280(l6);

        System.out.println("Looking for points between (0.0, 1.0, 6.0) and (17.0, 9.0, 10.0).\nFound:");
        System.out.println(T2.rangeSearch(lowLimit3, upperLimit3));

        System.out.println("\n<--------------- Testing completed! --------------->");
    }
}