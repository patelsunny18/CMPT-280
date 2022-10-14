// Sunny Ka Patel
// SDK438
// 11267665
// CMPT 280.A4

package lib280.tree;

import lib280.base.Dispenser280;
import lib280.base.Searchable280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class AVLTree280<I extends Comparable<? super I>> extends OrderedSimpleTree280<I> implements Searchable280<I>, Dispenser280<I> {

    /**
     * Constructor for the AVL Tree
     */
    public AVLTree280() {
        super();
    }

    /**
     * Gets the height of the given node
     * @param node the node for which the height is sought
     * @return height of the given node
     */
    protected int getHeight(AVLTreeNode280<I> node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    /**
     * Gets the root node of the AVL Tree
     * @return the root node of the AVL Tree
     */
    @Override
    protected AVLTreeNode280<I> rootNode() {
        return (AVLTreeNode280<I>) super.rootNode();
    }

    /**
     * Gets the left subtree of the current node
     * @return the left subtree
     * @throws ContainerEmpty280Exception when tree is empty
     */
    public AVLTree280<I> rootLeftSubtree() throws ContainerEmpty280Exception {
        return (AVLTree280<I>) super.rootLeftSubtree();
    }

    /**
     * Gets the right subtree of the current Node
     * @return the right subtree
     * @throws ContainerEmpty280Exception when tree is empty
     */
    public AVLTree280<I> rootRightSubtree() throws ContainerEmpty280Exception {
        return (AVLTree280<I>) super.rootRightSubtree();
    }

    /**
     * Checks if the given node is a leaf node or not
     * @param node the node for which the check needs to be done
     * @return true if given node is a leaf node; false otherwise
     * @throws NullPointerException when given node does not exist
     */
    protected boolean isLeaf(AVLTreeNode280<I> node) throws NullPointerException {
        if (node == null) {
            throw new NullPointerException("Given node is null!");
        }
        return node.leftNode == null && node.rightNode == null;
    }

    /**
     * Gets the imbalance factor of the given node
     * @param node the node for which the imbalance factor is sought
     * @return the imbalance factor of the given node
     */
    protected int getImbalance(AVLTreeNode280<I> node) {
        return getHeight(node.leftNode()) - getHeight(node.rightNode());
    }

    /**
     * Method to fix RR imbalance
     * @param parent the node for which the rotation is to be performed
     * @return the new parent node with the updated children
     */
    protected AVLTreeNode280<I> rotateLeft(AVLTreeNode280<I> parent) {
        // get the right node of the given parent node
        AVLTreeNode280<I> rightChild = parent.rightNode();

        // make parent's right node the right node's left node
        parent.setRightNode(rightChild.leftNode());
        // make the parent node as the right child's left node
        rightChild.setLeftNode(parent);

        // update the heights of the parent node and the new parent node (right-child)
        parent.setHeight(Math.max(getHeight(parent.leftNode()), getHeight(parent.rightNode())) +  1);
        rightChild.setHeight(Math.max(getHeight(rightChild.leftNode()), getHeight(rightChild.rightNode())) + 1);

        return rightChild;
    }

    /**
     * Method to fix LL imbalance
     * @param parent the node for which the rotation is to be performed
     * @return the new parent node with the updated children
     */
    protected AVLTreeNode280<I> rotateRight(AVLTreeNode280<I> parent) {
        // get the left child of the parent node
        AVLTreeNode280<I> leftChild = parent.leftNode();

        // make parent node's left node the left child's right node
        parent.setLeftNode(leftChild.rightNode());
        // make the parent node as the right child's right node
        leftChild.setRightNode(parent);

        // update the heights of the parent node and the new parent node (left-child)
        parent.setHeight(Math.max(getHeight(parent.leftNode()), getHeight(parent.rightNode())) + 1);
        leftChild.setHeight(Math.max(getHeight(leftChild.leftNode()), getHeight(leftChild.rightNode())) + 1);

        return leftChild;
    }

    /**
     * Restores the AVL property of the entire tree
     * @param parent the node for which the AVL property is checked
     * @return the updated node imbalance
     */
    protected AVLTreeNode280<I> restoreAVLProperty(AVLTreeNode280<I> parent) {
        // get the imbalance factor of the given parent node
        int imbalance = getImbalance(parent);

        // if imbalance is <= 1 then the node is not the critical node
        if (Math.abs(imbalance) <= 1) {
            return parent;
        }

        // if parent is left-heavy
        if (imbalance == 2) {
            // if parent's left node is left heavy then do a right rotation
            if (getImbalance(parent.leftNode()) < 0) {
                parent.leftNode = rotateLeft(parent.leftNode());
            }
            return rotateRight(parent);
        }
        // if parent is right heavy
        else {
            // if parent's right node is right heavy then do a left rotation
            if (getImbalance(parent.rightNode()) > 0) {
                parent.rightNode = rotateRight(parent.rightNode());
            }
            return rotateLeft(parent);
        }
    }

    /**
     * Checks if the given item is present in the tree
     * @param x the item to be checked
     * @return true if item exists; false otherwise
     */
    @Override
    public boolean has(I x) {
        return super.has(x);
    }

    /**
     * Searches for the given item and move the cursor to the item location if found
     * @param x item being sought
     */
    @Override
    public void search(I x) {
        super.search(x);
    }

    /**
     * Inserts the item to the tree
     * @param item item to be added
     */
    public void insert(I item) {
        this.rootNode = insert(item, this.rootNode());
    }

    /**
     * Deletes the item from the tree
     * @param item item to be deleted
     */
    public void delete(I item) {
        this.rootNode = delete(item, this.rootNode());
    }

    /**
     * Deletes the item at the current cursor position
     * @throws NoCurrentItem280Exception if no item is found at the current cursor postion
     */
    @Override
    public void deleteItem() throws NoCurrentItem280Exception {
        this.delete(this.cur.item());
    }

    /**
     * Inserts the given item to the tree
     * @param item item to be added
     * @param parent the parent node
     * @return a AVL property maintained tree
     * @throws DuplicateItems280Exception when item already exists in the tree
     */
    protected AVLTreeNode280<I> insert(I item, AVLTreeNode280<I> parent) throws DuplicateItems280Exception {
        // if parent node is empty, add the item as the new root
        if (parent == null) {
            parent = new AVLTreeNode280<>(item);
        }

        // if item already exists, throw exception
        else if (parent.item == item) {
            throw new DuplicateItems280Exception("Item already exist!");
        }
        // else search for the insertion location
        else {
            if (parent.item.compareTo(item) > 0) {
                parent.leftNode = insert(item, parent.leftNode());
            }
            else {
                parent.rightNode = insert(item, parent.rightNode());
            }
        }
        // update the height
        parent.setHeight(Math.max(getHeight(parent.leftNode()), getHeight(parent.rightNode())) + 1);

        // restore the tree after insertion
        return restoreAVLProperty(parent);
    }

    /**
     * Deletes the item
     * @param item item to be deleted
     * @param parent parent node
     * @return an AVL property maintained tree
     * @throws NoCurrentItem280Exception when the given items does not exist in the tree
     */
    protected AVLTreeNode280<I> delete(I item, AVLTreeNode280<I> parent) throws ItemNotFound280Exception {
        // if given parent node is empty, throw exception
        if (parent == null) {
            throw new ItemNotFound280Exception("Item not found!");
        }

        // if item is found
        if (parent.item == item) {
            // if its the leaf node, just return null
            if (isLeaf(parent)) {
                return null;
            }
            // if left child is null, delete the right child
            else if (parent.leftNode == null) {
                return parent.rightNode();
            }
            // if right child is null, delete the left child
            else if (parent.rightNode == null) {
                return parent.leftNode();
            }
            // if both child exists then find the parent's successor node
            else {
                I succ = findSuccessor(parent);
                parent.setItem(succ);
                parent.setRightNode(delete(succ, parent.rightNode()));
            }
        }
        // if item is less than parent, then go left and delete recursively
        else if (parent.item.compareTo(item) > 0) {
            parent.leftNode = delete(item, parent.leftNode());
        }
        // if item is greater than parent, then go right and delete recursively
        else {
            parent.rightNode = delete(item, parent.rightNode());
        }
        // update the height after deletion
        parent.setHeight(Math.max(getHeight(parent.leftNode()), getHeight(parent.rightNode())) + 1);

        // update the tree to maintain the AVL property
        return restoreAVLProperty(parent);
    }

    /**
     * Helper method for finding the successor of the given node
     * @param node node for which the successor is sought
     * @return the given node's successor
     */
    protected I findSuccessor(AVLTreeNode280<I> node) {
        // get the right child of the given node
        AVLTreeNode280<I> rightChild = node.rightNode();

        // if the right child is not a leaf node then keep on updating the right node's left child as the right node
        if (!isLeaf(rightChild)) {
            while (rightChild.leftNode != null) {
                rightChild = rightChild.leftNode();
            }
        }
        return rightChild.item;
    }

    /**
     * Gives a string representation of the tree
     * @return a string representation of the tree
     */
    public String toString() {
        return this.toStringByLevel(1);
    }

    /**
     * testing method
     */
    public static void main(String[] args) {
        AVLTree280<Integer> T = new AVLTree280<>();
        int failed = 0;

        // testing
        System.out.println("<--------------- Testing insert() --------------->\n");

        // testing constructor
        if (!T.isEmpty()) {
            System.out.println("ERROR: Tree should be empty but not wasn't!\n");
            failed++;
        }
        else {
            System.out.println("Constructor passed!\n");
        }

        System.out.println("<--------------- Testing RR imbalance --------------->\n");

        System.out.println("Inserting 10...");
        T.insert(10);
        if (T.rootNode().item() != 10) {
            System.out.println("ERROR: insert() failed! Current item should be 10 but found: " + T.rootNode().item() + "\n");
            failed++;
        }
        // insert 20 so that goes to the right of 10
        System.out.println("Inserting 20...");
        T.insert(20);
        // insert 30 so that it goes to the right-of-right of 10 and generate a degenerate tree
        System.out.println("Inserting 30...");
        T.insert(30);

        if (T.rootLeftSubtree().rootNode().item() != 10) {
            System.out.println("ERROR: Left subtree should be 11 but wasn't!\n");
            failed++;
        }
        if (T.rootRightSubtree().rootNode().item() != 30) {
            System.out.println("ERROR: Right subtree should be 30 but wasn't!\n");
            failed++;
        }
        if (T.rootNode().item() != 20) {
            System.out.println("ERROR: Root node should be 20 but wasn't!\n");
            failed++;
        }
        // if no error were found then RR test cases were successful
        if (failed == 0) {
            System.out.println("No errors found. RR imbalance restoration successful!");
        }
        System.out.println(T.toString() + "\n");


        System.out.println("<--------------- Testing LL imbalance --------------->\n");

        // insert 5 so that it goes to the left of 20 and left of 10
        System.out.println("Inserting 5...");
        T.insert(5);
        // insert 2 so that it goes to the left of 5 making a LL imbalance on 10
        System.out.println("Inserting 2...");
        T.insert(2);

        if (T.rootNode().leftNode().item() != 5) {
            System.out.println("ERROR: Left subtree root should be 5 but wasn't!\n");
            failed++;
        }
        if (T.rootNode().leftNode().leftNode().item() != 2) {
            System.out.println("ERROR: Item should be 2 but wasn't!\n");
            failed++;
        }
        if (T.rootNode().leftNode().rightNode().item() != 10) {
            System.out.println("ERROR: Item should be 10 but wasn't!\n");
            failed++;
        }
        // if no error were found then LL test cases were successful
        if (failed == 0) {
            System.out.println("No errors found. LL imbalance restoration successful!");
        }
        System.out.println(T.toString() + "\n");


        System.out.println("<--------------- Testing RL imbalance --------------->\n");

        // insert 40 so that it goes to the right of 30
        System.out.println("Inserting 40...");
        T.insert(40);
        // insert 35 so it goes to the left of 40 creating a RL imbalance on 30
        System.out.println("Inserting 35...");
        T.insert(35);
        if (T.rootNode().rightNode().item() != 35) {
            System.out.println("ERROR: Root's right subtree's root should be 35 but wasn't!");
            failed++;
        }
        if (T.rootRightSubtree().rootNode().leftNode().item() != 30) {
            System.out.println("ERROR: Root's right subtree's left child should be 30 but wasn't!");
            failed++;
        }
        if (T.rootRightSubtree().rootNode().rightNode().item() != 40) {
            System.out.println("ERROR: Root's right subtree's right child should be 40 but wasn't!");
            failed++;
        }
        // if no errors were found then RL imbalance test cases were successful
        if (failed == 0) {
            System.out.println("No errors found. RL imbalance restoration successful!\n");
        }
        System.out.println(T.toString() + "\n");


        System.out.println("<--------------- Testing LR imbalance --------------->\n");

        // insert 7 so that it goes to the left of 10
        System.out.println("Inserting 7...");
        T.insert(7);
        // insert 9 so that it goes to the right of 7 and makes a LR imbalance
        System.out.println("Inserting 9...");
        T.insert(9);
        if (T.rootNode().leftNode().rightNode().item() != 9) {
            System.out.println("ERROR: Root's left subtree's root's right child should be 9 but wasn't!");
            failed++;
        }
        if (T.rootNode().leftNode().rightNode().leftNode().item() != 7) {
            System.out.println("ERROR: Root's left subtree's root's right child's left child should be 7 but wasn't!");
            failed++;
        }
        if (T.rootNode().leftNode().rightNode().rightNode().item() != 10) {
            System.out.println("ERROR: Root's left subtree's root's right child's right child should be 10 but wasn't!");
            failed++;
        }
        // if no errors were found then LR imbalance test cases were successful
        if (failed == 0) {
            System.out.println("No errors found. LR imbalance restoration successful!");
        }
        System.out.println(T.toString() + "\n");


        System.out.println("<--------------- Testing duplicate input --------------->\n");

        // inserting another 40
        try {
            T.insert(40);
        } catch (DuplicateItems280Exception e) {
            System.out.println("Correct exception thrown!\n");
        } catch (Exception e) {
            System.out.println("ERROR: Incorrect exception thrown! insert() failed!");
            failed++;
        }

        System.out.println("<--------------- Testing Dispenser and Searchable methods --------------->\n");

        System.out.println("Testing search(40)...");
        T.search(40);
        if (T.cur.item() != 40) {
            System.out.println("Error: Current item should be 40 but isn't!");
            failed++;
        }
        System.out.println("Testing deleteItem()...");
        T.deleteItem();
        System.out.println("Testing has(40)...");
        if (T.has(40)) {
            System.out.println("ERROR: Found 40 even though it should be deleted!");
            failed++;
        }
        System.out.println(T.toString() + "\n");


        System.out.println("Testing search(20)...");
        T.search(20);
        if (T.cur.item() != 20) {
            System.out.println("Error: Current item should be 7 but isn't!");
            failed++;
        }
        System.out.println("Testing deleteItem()...");
        T.deleteItem();
        System.out.println("Testing has(20)...");
        if (T.has(20)) {
            System.out.println("ERROR: Found 20 even though it should be deleted!");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        System.out.println("Testing search(5)...");
        T.search(5);
        if (T.cur.item() != 5) {
            System.out.println("Error: Current item should be 5 but isn't!");
            failed++;
        }
        System.out.println("Testing deleteItem()...");
        T.deleteItem();
        System.out.println("Testing has(5)...");
        if (T.has(5)) {
            System.out.println("ERROR: Found 7 even though it should be deleted!");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        System.out.println("Testing search(10)...");
        T.search(10);
        if (T.cur.item() != 10) {
            System.out.println("Error: Current item should be 10 but isn't!");
            failed++;
        }
        System.out.println("Testing deleteItem()...");
        T.deleteItem();
        System.out.println("Testing has(10)...");
        if (T.has(10)) {
            System.out.println("ERROR: Found 10 even though it should be deleted!");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        if (failed == 0) {
            System.out.println("No errors found. search(), deleteItem() and has() passed!\n");
        }

        System.out.println("<--------------- Testing delete() --------------->\n");

        System.out.println("Testing deleting a leaf node (2)...");
        T.delete(2);
        if (T.has(2)) {
            System.out.println("ERROR: Found 2 even though it should be deleted!\n");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        System.out.println("Testing deleting a node with a right child (30)...");
        T.delete(30);
        if (T.has(30)) {
            System.out.println("ERROR: Found 30 even though it should be deleted!\n");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        System.out.println("Testing deleting a node with a left child (9)...");
        T.delete(9);
        if (T.has(9)) {
            System.out.println("ERROR: Found 9 even though it should be deleted!\n");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        System.out.println("Testing deleting the head node (35)...");
        T.delete(35);
        if (T.has(35)) {
            System.out.println("ERROR: Found 35 even though it should be deleted!\n");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        System.out.println("Testing deleting the only node in the tree (7)...");
        T.delete(7);
        if (T.has(7)) {
            System.out.println("ERROR: Found 7 even though it should be deleted!\n");
            failed++;
        }
        System.out.println(T.toString() + "\n");

        System.out.println("Testing deleting an invalid item (15)...");
        try {
            T.delete(15);
        } catch (ItemNotFound280Exception e) {
            System.out.println("Correct exception thrown!\n");
        } catch (RuntimeException e) {
            System.out.println("Incorrect exception thrown!");
            failed++;
        }

        System.out.println("Final tree: " + T.toString() + "\n");

        if (failed == 0) {
            System.out.println("<--------------- Regression Test Complete --------------->");
        }
    }
}