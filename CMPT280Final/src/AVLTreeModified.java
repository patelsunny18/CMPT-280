/*
Sunny Ka Patel
SDK438
11267665
CMPT280.Final.Q1
 */

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.AVLTree280;

public class AVLTreeModified<I extends Comparable<? super I>> extends AVLTree280<I> {

    /**	The current node as set by search. */
    protected AVLTreeNodeModified<I> cur;

    /**	The parent node of the current node as set by search. */
    protected AVLTreeNodeModified<I> parent;

    /**
     * Creates a new AVLTreeNodeModified
     * @param item the value of the node
     * @return the newly created AVLTreeNodeModified
     */
    public AVLTreeNodeModified<I> createNewNodeModified(I item) {
        return new AVLTreeNodeModified<>(item);
    }

    /**
     * Gets the left subtree of the root node
     * @return the left subtree of the root node
     * @throws ContainerEmpty280Exception when the left subtree does not exists
     */
    public AVLTreeModified<I> rootLeftSubtreeModified() throws ContainerEmpty280Exception {
        return (AVLTreeModified<I>) super.rootLeftSubtree();
    }

    /**
     * Gets the right subtree of the root node
     * @return the right subtree of the root node
     * @throws ContainerEmpty280Exception when the right subtree does not exists
     */
    public AVLTreeModified<I> rootRightSubtreeModified() throws ContainerEmpty280Exception {
        return (AVLTreeModified<I>) super.rootRightSubtree();
    }

    /**
     * Gets the root node
     * @return the root node
     */
    public AVLTreeNodeModified<I> rootNodeModified() {
        return  (AVLTreeNodeModified<I>) super.rootNode();
    }

    @Override
    public I item() throws NoCurrentItem280Exception {
        if( !itemExists() ) {
            throw new NoCurrentItem280Exception("Error retrieving current item: There is no current item.");
        }
        return cur.item();
    }

    @Override
    public boolean itemExists() {
        return cur != null;
    }

    /**
     * Inserts the item to the tree
     * @param item the item to be added
     */
    public void insertMain(I item) {
        this.insertModified(item, null);
    }

    /**
     * Protected method to insert the item to the tree
     * NOTE: This method also accepts duplicates. When a duplicate is found, it increases the number of copies by 1.
     * @param item the value to be added
     * @param parent the parent node
     */
    protected void insertModified(I item, AVLTreeNodeModified<I> parent) {
        if (this.isEmpty()) {
            this.setRootNode(this.createNewNodeModified(item));
            return;
        }

        // Recursively find the insertion point
        AVLTreeNodeModified<I> v = (AVLTreeNodeModified<I>) this.rootNode();

        // If a duplicate is found then just increase the number of copies by 1. Note: Does not edit the tree
        if (item.compareTo(v.item()) == 0) {
            v.count++;
        }
        else if( item.compareTo(v.item()) < 0) {
            // go left
            if( v.leftNode() == null ) {
                v.setLeftNode(new AVLTreeNodeModified<>(item));
            }
            else {
                this.rootLeftSubtreeModified().insertModified(item, (AVLTreeNodeModified<I>) this.rootNode());
            }
            v.setLtHeight(
                    Math.max(v.leftNode().getLtHeight(), v.leftNode().getRtHeight()) + 1
            );

        }
        else {
            // go right
            if( v.rightNode() == null ) {
                v.setRightNode(new AVLTreeNodeModified<>(item));
            }
            else {
                this.rootRightSubtreeModified().insertModified(item, (AVLTreeNodeModified<I>) this.rootNode());
            }
            v.setRtHeight(
                    Math.max(v.rightNode().getLtHeight(), v.rightNode().getRtHeight()) + 1
            );

        }

        // As the recursion unwinds, check if the current node is critical
        // if it is, then we have to do rotations.
        this.restoreAVLProperty(parent);
    }

    /**
     * Deletes the node at the current cursor position
     * @throws NoCurrentItem280Exception when no item exists at the current cursor position
     */
    public void deleteItemModified() throws NoCurrentItem280Exception {
        if(!this.itemExists()) throw new NoCurrentItem280Exception("There is no item to delete.");

        I toBeDeleted = this.item();

        // save the inorder successor of the current node
        AVLTreeNodeModified<I> iosParent = this.cur;
        AVLTreeNodeModified<I> iosCur = this.cur.rightNodeModified();
        while( iosCur != null && iosCur.leftNodeModified() != null ) {
            iosParent = iosCur;
            iosCur = iosCur.leftNodeModified();
        }

        this.deleteModified(toBeDeleted, null, this);

        // Restore the cursor to the deleted node's inorder successor.
        this.cur = iosCur;
        this.parent = iosParent;

    }

    /**
     * Protected method to delete a node from the tree
     * Note: If more than 1 copies of the node is found then it decreases the number of copies by 1 until
     * it reaches 0 when it delete the node completely
     * @param toBeDeleted the item to be deleted
     * @param parent the parent node
     * @param originalTree the original tree
     */
    protected void deleteModified(I toBeDeleted, AVLTreeNodeModified<I> parent, AVLTreeModified<I> originalTree) {

        if( this.isEmpty() ) {
            throw new RuntimeException("Didn't find the item at the cursor while deleting.  This should be impossible.");		}

        else if( toBeDeleted.compareTo(this.rootItem()) < 0 ) {
            // Recurse left
            this.rootLeftSubtreeModified().deleteModified(toBeDeleted, this.rootNodeModified(), originalTree);

            // Update left subtree's height.
            if(this.rootNodeModified().leftNodeModified() != null)
                this.rootNodeModified().setLtHeight(Math.max(this.rootNodeModified().leftNodeModified().getLtHeight(), this.rootNodeModified().leftNodeModified().getRtHeight())+1);
            else this.rootNodeModified().setLtHeight(0);

            // Verify the AVL property and repair if needed.
            restoreAVLProperty(parent);
        }
        else if( toBeDeleted.compareTo(this.rootItem()) > 0 ) {
            // Recurse right
            this.rootRightSubtreeModified().deleteModified(toBeDeleted, this.rootNodeModified(), originalTree);

            // Update right subtree's height.
            if(this.rootNodeModified().rightNodeModified() != null)
                this.rootNodeModified().setRtHeight(Math.max(this.rootNodeModified().rightNodeModified().getLtHeight(), this.rootNodeModified().rightNodeModified().getRtHeight())+1);
            else this.rootNodeModified().setRtHeight(0);

            // Verify the AVL property and repair if needed.
            restoreAVLProperty(parent);
        }
        else {
            // BASE CASE: We've reached the node we want to delete.

            // If the number of copies is greater than 1 then decrease the number of copies by 1
            if (rootNodeModified().count > 1) {
                rootNodeModified().count--;
            }
            // Else if there are no more copies left then search for a replacement node
            else {
                AVLTreeNodeModified<I> replacementNode = null;
                boolean foundReplacement = false;
                if( this.rootNodeModified().rightNodeModified() == null ) {
                    replacementNode = this.rootNodeModified().leftNodeModified();
                    foundReplacement = true;
                }
                else if( this.rootNodeModified().leftNodeModified() == null ) {
                    replacementNode = this.rootNodeModified().rightNodeModified();
                    foundReplacement = true;
                }

                // If we found a replacement, then...
                if( foundReplacement ) {
                    // ... N has zero or 1 child.

                    // If the node we are replacing is the root of the lib280.tree,
                    // make the replacement node the new root.
                    if( parent == null) this.rootNode = replacementNode;
                        // Otherwise, set the appropriate subtree reference in the
                        // parent to the replacement node.
                    else if( parent.leftNodeModified() == this.rootNodeModified())  {
                        parent.setLeftNode(replacementNode);
                    }
                    else parent.setRightNode(replacementNode);
                }
                else {
                    // Deletion by copying

                    // Find the in-order successor
                    AVLTreeNodeModified<I> iosCur = this.rootNodeModified().rightNodeModified();
                    while(iosCur.leftNodeModified() != null) {
                        iosCur = iosCur.leftNodeModified();
                    }

                    // Save a copy of the item to be copied into N, and
                    // a copy of where it should be copied to.
                    // The latter needs to be done because deletion of
                    // the inorder successor could change the root of this
                    // lib280.tree.
                    I itemToBeCopied = iosCur.item();
                    AVLTreeNodeModified<I> destinationForCopy = this.rootNodeModified();

                    // Delete the in-order successor.
                    this.deleteModified(itemToBeCopied, parent, originalTree);

                    // Set place the item to be copied into its destination.
                    destinationForCopy.setItem(itemToBeCopied);
                }
            }
        }
    }

    /**
     * Go to item x, if it is in the lib280.tree.  If searchesContinue, continue in the right subtree.
     * @param x the item to be searched for
     */
    public void searchModified(I x)
    {
        boolean found = false;
        if (!searchesContinue || above())
        {
            parent = null;
            cur = rootNodeModified();
        }
        else if (!below())
        {
            parent = cur;
            cur = cur.rightNodeModified();
        }
        while (!found && itemExists())
        {
            if (x.compareTo(item()) < 0)
            {
                parent = cur;
                cur = parent.leftNodeModified();
            }
            else if (x.compareTo(item()) > 0)
            {
                parent = cur;
                cur = parent.rightNodeModified();
            }
            else
                found = true;
        }
    }

    @Override
    public String toString() {
        return toStringByLevel(1);
    }

    protected String toStringByLevel(int i)
    {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("            ");

        String result = new String();
        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
            result += rootRightSubtreeModified().toStringByLevel(i+1);

        result += "\n" + blanks + i + "/";
        if( !isEmpty() ) result += this.rootNode().getLtHeight() + "/" + this.rootNode().getRtHeight();
        result += ": ";
        if (isEmpty())
            result += "-";
        else
        {
            result += rootNodeModified().item();
            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                result += rootLeftSubtreeModified().toStringByLevel(i+1);
        }
        return result;
    }

    public static void main(String[] args) {
        AVLTreeModified<Integer> T = new AVLTreeModified<>();
        System.out.println("Initial tree: ");
        T.insertMain(10);

        T.insertMain(20);
        T.insertMain(20);

        T.insertMain(25);
        T.insertMain(25);
        T.insertMain(25);

        T.insertMain(30);

        T.insertMain(40);
        T.insertMain(40);
        System.out.println(T);

        System.out.println("\nDeleting 25");
        T.searchModified(25);
        T.deleteItemModified();
        System.out.println(T);

        System.out.println("\nDeleting 25 x2");
        T.searchModified(25);
        T.deleteItemModified();
        System.out.println(T);

        System.out.println("\nDeleting 25 x3");
        T.searchModified(25);
        T.deleteItemModified();
        System.out.println(T);
    }
}
