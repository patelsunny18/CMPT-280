/*
Sunny Ka Patel
SDK438
11267665
CMPT 280.A7
 */

package lib280.tree;

import lib280.base.NDPoint280;
import lib280.exception.InvalidState280Exception;

/**
 * The KDNode280 class
 * @param <I> the generic type parameter
 */
public class KDNode280<I extends Comparable<? super I>> extends BinaryNode280<NDPoint280> {

    /**
     * Constructor for the KDNode280 class
     * @param dimension the dimension of the node
     */
    public KDNode280(int dimension) {
        super(new NDPoint280(dimension));
        this.leftNode = null;
        this.rightNode = null;
    }

    /**
     * Constructor for the KDNode280 class using an array of Doubles
     * @param pts array containing the points of the new node
     */
    public KDNode280(Double[] pts) {
        super(new NDPoint280(pts));
        this.leftNode = null;
        this.rightNode = null;
    }

    /**
     * Gets the NDPoint280 of the node
     * @return the NDPoint280 of the node
     */
    public NDPoint280 item() {
        return super.item();
    }

    /**
     * Gets the dimension the current KDNode280 node
     * @return the dimension the current KDNode280 node
     */
    public int getDimension() {
        return this.item().dim();
    }

    /**
     * Checks if the current node is empty or not
     * @return true if node is empty; false otherwise
     */
    public boolean isEmpty() {
        return this.item == null;
    }

    /**
     * Gets the right node
     * @return the right node
     */
    public KDNode280<I> getRightNode() {
        return (KDNode280<I>) this.rightNode();
    }

    /**
     * Gets the left node
     * @return the left node
     */
    public KDNode280<I> getLeftNode() {
        return (KDNode280<I>) this.leftNode();
    }

    /**
     * Sets the right node to the given new node
     * @param newNode the replacement node
     */
    public void setRightNode(KDNode280<I> newNode) {
        this.rightNode = newNode;
    }

    /**
     * Sets the left node to the given new node
     * @param newNode the replacement node
     */
    public void setLeftNode(KDNode280<I> newNode) {
        this.leftNode = newNode;
    }

    /**
     * Gets a string representation of the points of a node
     * @return a string representation of the points of a node
     */
    public String toString() {
        return this.item().toString();
    }

    /**
     * Form a string representation that includes level numbers
     * @param level the level for the node in KDTree280
     * @return a string representation of the KDTree280
     */
    public String toStringByLevel(int level) {
        StringBuffer blanks = new StringBuffer((level - 1) * 5);
        for (int i = 0; i < level - 1; i++) {
            blanks.append("     ");
        }
        String result = "";

        if (this.item() != null && this.getRightNode() != null) {
            result += getRightNode().toStringByLevel(level + 1);
        }
        result += "\n" + blanks + level + ": ";
        if (this.item == null) {
            result += "-";
        }
        else {
            result += this;
            if (this.getLeftNode() != null) {
                result += getLeftNode().toStringByLevel(level + 1);
            }
        }
        return result;
    }

}
