/*
Sunny Ka Patel
SDK438
11267665
CMPT280.Final.Q1
 */

import lib280.tree.AVLTreeNode280;

public class AVLTreeNodeModified<I extends Comparable<? super I>> extends AVLTreeNode280<I> {

    /** A counter for the number of copies */
    protected int count;

    public AVLTreeNodeModified(I x) {
        super(x);
        count = 1;
    }

    public int getCount() {
        return this.count;
    }

    public AVLTreeNodeModified<I> leftNodeModified() {
        return (AVLTreeNodeModified<I>) super.leftNode();
    }

    public AVLTreeNodeModified<I> rightNodeModified() {
        return (AVLTreeNodeModified<I>) super.rightNode();
    }
}
