// Sunny Ka Patel
// SDK438
// 11267665
// CMPT 280.A4

package lib280.tree;

public class AVLTreeNode280<I extends Comparable<? super I>> extends BinaryNode280<I> {

    /**
     * height of node's subtree
     */
    int height;

    /**
     * Construct a new node with item x.
     * @param x the item placed in the new node
     */
    public AVLTreeNode280(I x) {
        super(x);
        height = 1;
    }

    /**
     * Sets the height of the current node
     * @param height height to be set
     */
    protected void setHeight(int height) {
        this.height = height;
    }

    /**
     * get the height of the left subtree
     * @return height of the left subtree
     */
    public int getLeftHeight() {
        if (this.leftNode == null) {
            return 0;
        }
        return ((AVLTreeNode280<I>) this.leftNode).height;
    }

    /**
     * get the height of the right subtree
     * @return height of the right subtree
     */
    public int getRightHeight() {
        if (rightNode == null) {
            return 0;
        }
        return ((AVLTreeNode280<I>) this.rightNode).height;
    }

    /**
     * gets the left node
     * @return the left node
     */
    @Override
    public AVLTreeNode280<I> leftNode() {
        return (AVLTreeNode280<I>) super.leftNode();
    }

    /**
     * gets the right node
     * @return the right node
     */
    @Override
    public AVLTreeNode280<I> rightNode() {
        return (AVLTreeNode280<I>) super.rightNode();
    }

    /**
     *
     * @return Returns a string representation of the item contained within the node
     */
    @Override
    public String toString() {
        return "AVLTreeNode280: {" + "Item: " + item + ", Height: " + height + ", Left Height: " + this.getLeftHeight() + ", Right Height: " + this.getRightHeight() + "}";
    }
}
