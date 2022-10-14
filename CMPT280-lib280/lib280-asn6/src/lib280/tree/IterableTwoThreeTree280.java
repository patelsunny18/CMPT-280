/*
Sunny Ka Patel
SDK438
11267665
CMPT 280.A6
 */

package lib280.tree;

import lib280.base.CursorPosition280;
import lib280.base.Keyed280;
import lib280.base.Pair280;
import lib280.dictionary.KeyedDict280;
import lib280.exception.*;

public class IterableTwoThreeTree280<K extends Comparable<? super K>, I extends Keyed280<K>> extends TwoThreeTree280<K, I> implements KeyedDict280<K,I> {

	// References to the leaf nodes with the smallest and largest keys.
	LinkedLeafTwoThreeNode280<K,I> smallest, largest;
	
	// These next two variables represent the cursor which
	// the methods inherited from KeyedLinearIterator280 will
	// manipulate.  The cursor may only be positioned at leaf
	// nodes, never at internal nodes.
	
	// Reference to the leaf node at which the cursor is positioned.
	LinkedLeafTwoThreeNode280<K,I> cursor;
	
	// Reference to the predecessor of the node referred to by 'cursor' 
	// (or null if no such node exists).
	LinkedLeafTwoThreeNode280<K,I> prev;
	
	
	protected LinkedLeafTwoThreeNode280<K,I> createNewLeafNode(I newItem) {
		return new LinkedLeafTwoThreeNode280<K,I>(newItem);
	}


	@Override
	public void insert(I newItem) {

		if( this.has(newItem.key()) ) 
			throw new DuplicateItems280Exception("Key already exists in the tree.");

		// If the tree is empty, just make a leaf node. 
		if( this.isEmpty() ) {
			this.rootNode = createNewLeafNode(newItem);
			// Set the smallest and largest nodes to be the one leaf node in the tree.
			this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
			this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
		}
		// If the tree has one node, make an internal node, and make it the parent
		// of both the existing leaf node and the new leaf node.
		else if( !this.rootNode.isInternal() ) {
			LinkedLeafTwoThreeNode280<K,I> newLeaf = createNewLeafNode(newItem);
			LinkedLeafTwoThreeNode280<K,I> oldRoot = (LinkedLeafTwoThreeNode280<K,I>)rootNode;
			InternalTwoThreeNode280<K,I> newRoot;
			if( newItem.key().compareTo(oldRoot.getKey1()) < 0) {
				// New item's key is smaller than the existing item's key...
				newRoot = createNewInternalNode(newLeaf, oldRoot.getKey1(), oldRoot, null, null);	
				newLeaf.setNext(oldRoot);
				oldRoot.setPrev(newLeaf);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = newLeaf;
				this.largest = oldRoot;
			}
			else {
				// New item's key is larger than the existing item's key. 
				newRoot = createNewInternalNode(oldRoot, newItem.key(), newLeaf, null, null);
				oldRoot.setNext(newLeaf);
				newLeaf.setPrev(oldRoot);
				
				// There was one leaf node, now there's two.  Update smallest and largest nodes.
				this.smallest = oldRoot;
				this.largest = newLeaf;
			}
			this.rootNode = newRoot;
		}
		else {
			Pair280<TwoThreeNode280<K,I>, K> extra = this.insert((InternalTwoThreeNode280<K,I>)this.rootNode, newItem);

			// If extra returns non-null, then the root was split and we need
			// to make a new root.
			if( extra != null ) {
				InternalTwoThreeNode280<K,I> oldRoot = (InternalTwoThreeNode280<K,I>)rootNode;

				// extra always contains larger keys than its sibling.
				this.rootNode = createNewInternalNode(oldRoot, extra.secondItem(), extra.firstItem(), null, null);				
			}
		}
	}


	/**
	 * Recursive helper for the public insert() method.
	 * @param root Root of the (sub)tree into which we are inserting.
	 * @param newItem The item to be inserted.
	 */
	protected Pair280<TwoThreeNode280<K,I>, K> insert(TwoThreeNode280<K,I> root,
                                                      I newItem) {

		if( !root.isInternal() ) {
			// If root is a leaf node, then it's time to create a new
			// leaf node for our new element and return it so it gets linked
			// into root's parent.
			Pair280<TwoThreeNode280<K,I>, K> extraNode;
			LinkedLeafTwoThreeNode280<K,I> oldLeaf = (LinkedLeafTwoThreeNode280<K, I>) root;

			// If the new element is smaller than root, copy root's element to
			// a new leaf node, put new element in existing leaf node, and
			// return new leaf node.
			if( newItem.key().compareTo(root.getKey1()) < 0) {
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(root.getData()), root.getKey1());
				((LeafTwoThreeNode280<K,I>)root).setData(newItem);
			}
			else {
				// Otherwise, just put the new element in a new leaf node
				// and return it.
				extraNode = new Pair280<TwoThreeNode280<K,I>, K>(createNewLeafNode(newItem), newItem.key());
			}
			
			LinkedLeafTwoThreeNode280<K,I> newLeaf= (LinkedLeafTwoThreeNode280<K, I>) extraNode.firstItem();

			newLeaf.setNext(oldLeaf.next());
			newLeaf.setPrev(oldLeaf);
			if (oldLeaf.next() != null) {
				oldLeaf.next().setPrev(newLeaf);
			}
			oldLeaf.setNext(newLeaf);
			if (newLeaf.next() == null) {
				this.largest = newLeaf;
			}
			return extraNode;
		}
		else { // Otherwise, recurse! 
			Pair280<TwoThreeNode280<K,I>, K> extra;
			TwoThreeNode280<K,I> insertSubtree;

			if( newItem.key().compareTo(root.getKey1()) < 0 ) {
				// decide to recurse left
				insertSubtree = root.getLeftSubtree();
			}
			else if(!root.isRightChild() || newItem.key().compareTo(root.getKey2()) < 0 ) {
				// decide to recurse middle
				insertSubtree = root.getMiddleSubtree();
			}
			else {
				// decide to recurse right
				insertSubtree = root.getRightSubtree();
			}

			// Actually recurse where we decided to go.
			extra = insert(insertSubtree, newItem);

			// If recursion resulted in a new node needs to be linked in as a child
			// of root ...
			if( extra != null ) {
				// Otherwise, extra.firstItem() is an internal node... 
				if( !root.isRightChild() ) {
					// if root has only two children.  
					if( insertSubtree == root.getLeftSubtree() ) {
						// if we inserted in the left subtree...
						root.setRightSubtree(root.getMiddleSubtree());
						root.setMiddleSubtree(extra.firstItem());
						root.setKey2(root.getKey1());
						root.setKey1(extra.secondItem());
						return null;
					}
					else {
						// if we inserted in the right subtree...
						root.setRightSubtree(extra.firstItem());
						root.setKey2(extra.secondItem());
						return null;
					}
				}
				else {
					// otherwise root has three children
					TwoThreeNode280<K, I> extraNode;
					if( insertSubtree == root.getLeftSubtree()) {
						// if we inserted in the left subtree
						extraNode = createNewInternalNode(root.getMiddleSubtree(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setMiddleSubtree(extra.firstItem());
						root.setRightSubtree(null);
						K k1 = root.getKey1();
						root.setKey1(extra.secondItem());
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k1);
					}
					else if( insertSubtree == root.getMiddleSubtree()) {
						// if we inserted in the middle subtree
						extraNode = createNewInternalNode(extra.firstItem(), root.getKey2(), root.getRightSubtree(), null, null);
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, extra.secondItem());
					}
					else {
						// we inserted in the right subtree
						extraNode = createNewInternalNode(root.getRightSubtree(), extra.secondItem(), extra.firstItem(), null, null);
						K k2 = root.getKey2();
						root.setKey2(null);
						root.setRightSubtree(null);
						return new Pair280<TwoThreeNode280<K,I>, K>(extraNode, k2);
					}
				}
			}
			// Otherwise no new node was returned, so there is nothing extra to link in.
			else return null;
		}		
	}


	@Override
	public void delete(K keyToDelete) {
		if( this.isEmpty() ) return;

		if( !this.rootNode.isInternal()) {
			if( this.rootNode.getKey1() == keyToDelete ) {
				this.rootNode = null;
				this.smallest = null;
				this.largest = null;
			}
		}
		else {
			delete(this.rootNode, keyToDelete);	
			// If the root only has one child, replace the root with its
			// child.
			if( this.rootNode.getMiddleSubtree() == null) {
				this.rootNode = this.rootNode.getLeftSubtree();
				if( !this.rootNode.isInternal() ) {
					this.smallest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
					this.largest = (LinkedLeafTwoThreeNode280<K, I>) this.rootNode;
				}
			}
		}
	}


	/**
	 * Given a key, delete the corresponding key-item pair from the tree.
	 * @param root root of the current tree
	 * @param keyToDelete The key to be deleted, if it exists.
	 */
	protected void delete(TwoThreeNode280<K, I> root, K keyToDelete ) {
		if( root.getLeftSubtree().isInternal() ) {
			// root is internal, so recurse.
			TwoThreeNode280<K,I> deletionSubtree;
			if( keyToDelete.compareTo(root.getKey1()) < 0){
				// recurse left
				deletionSubtree = root.getLeftSubtree();
			}
			else if( root.getRightSubtree() == null || keyToDelete.compareTo(root.getKey2()) < 0 ){
				// recurse middle
				deletionSubtree = root.getMiddleSubtree();
			}
			else {
				// recurse right
				deletionSubtree = root.getRightSubtree();
			}

			delete(deletionSubtree, keyToDelete);

			// Do the first possible of:
			// steal left, steal right, merge left, merge right
			if( deletionSubtree.getMiddleSubtree() == null)  
				if(!stealLeft(root, deletionSubtree))
					if(!stealRight(root, deletionSubtree))
						if(!giveLeft(root, deletionSubtree))
							if(!giveRight(root, deletionSubtree))
								throw new InvalidState280Exception("This should never happen!");

		}
		else {
			// children of root are leaf nodes
			if( root.getLeftSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on left

				LinkedLeafTwoThreeNode280<K, I> itemToBeDeleted = (LinkedLeafTwoThreeNode280<K, I>) root.getLeftSubtree();
				if (itemToBeDeleted.next() != null) {
					itemToBeDeleted.next().setPrev(itemToBeDeleted.prev());
				}
				if (itemToBeDeleted.prev() != null) {
					itemToBeDeleted.prev().setNext(itemToBeDeleted.next());
				}
				if (itemToBeDeleted.prev() == null) {
					this.smallest = itemToBeDeleted.next();
				}

				// Proceed with deletion of leaf from the 2-3 tree.
				root.setLeftSubtree(root.getMiddleSubtree());
				root.setMiddleSubtree(root.getRightSubtree());
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());
				if( root.getRightSubtree() != null) root.setKey2(null);
				root.setRightSubtree(null);					
			}
			else if( root.getMiddleSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is in middle

				LinkedLeafTwoThreeNode280<K, I> itemToBeDeleted = (LinkedLeafTwoThreeNode280<K, I>) root.getMiddleSubtree();
				if (itemToBeDeleted.next() != null) {
					itemToBeDeleted.next().setPrev(itemToBeDeleted.prev());
				}
				if (itemToBeDeleted.prev() != null) {
					itemToBeDeleted.prev().setNext(itemToBeDeleted.next());
				}
				if (itemToBeDeleted.next() == null) {
					this.largest = itemToBeDeleted.prev();
				}
				
				// Proceed with deletion from the 2-3 tree.
				root.setMiddleSubtree(root.getRightSubtree());				
				if(root.getMiddleSubtree() == null)
					root.setKey1(null);
				else 
					root.setKey1(root.getKey2());

				if( root.getRightSubtree() != null) {
					root.setKey2(null);
					root.setRightSubtree(null);
				}
			}
			else if( root.getRightSubtree() != null && root.getRightSubtree().getKey1().compareTo(keyToDelete) == 0 ) {
				// leaf to delete is on the right

				LinkedLeafTwoThreeNode280<K, I> itemToBeDeleted = (LinkedLeafTwoThreeNode280<K, I>) root.getRightSubtree();
				if (itemToBeDeleted.next() != null) {
					itemToBeDeleted.next().setPrev(itemToBeDeleted.prev());
				}
				if (itemToBeDeleted.prev() != null) {
					itemToBeDeleted.prev().setNext(itemToBeDeleted.next());
				}
				if (itemToBeDeleted.next() == null) {
					this.largest = itemToBeDeleted.prev();
				}

				// Proceed with deletion of the node from the 2-3 tree.
				root.setKey2(null);
				root.setRightSubtree(null);
			}
			else {
				// key to delete does not exist in tree.
			}
		}		
	}	
	
	
	@Override
	public K itemKey() throws NoCurrentItem280Exception {
		if (!itemExists()) {
			throw new NoCurrentItem280Exception("Current item does not exist!");
		}
		return cursor.getKey1();
	}


	@Override
	public Pair280<K, I> keyItemPair() throws NoCurrentItem280Exception {
		// Return a pair consisting of the key of the item
		// at which the cursor is positioned, and the entire
		// item in the node at which the cursor is positioned.
		if( !itemExists() ) 
			throw new NoCurrentItem280Exception("There is no current item from which to obtain its key.");
		return new Pair280<K, I>(this.itemKey(), this.item());
	}


	@Override
	public I item() throws NoCurrentItem280Exception {
		if (!itemExists()) {
			throw new NoCurrentItem280Exception("Current item does not exist!");
		}
		return cursor.getData();
	}


	@Override
	public boolean itemExists() {
		return this.cursor != null;
	}


	@Override
	public boolean before() {
		return this.cursor == null && this.prev == null;
	}


	@Override
	public boolean after() {
		return this.cursor == null && this.prev != null || this.isEmpty();
	}


	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if( this.after() ) throw new AfterTheEnd280Exception("Cannot advance the cursor past the end.");
		if( this.before() ) this.goFirst();
		else {
			this.prev = this.cursor;
			this.cursor = this.cursor.next();
		}
	}


	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if(this.isEmpty()) throw new ContainerEmpty280Exception("Attempted to move linear iterator to first element of an empty tree.");
		this.prev = null;
		this.cursor = this.smallest;
	}


	@Override
	public void goBefore() {
		this.prev = null;
		this.cursor = null;
	}


	@Override
	public void goAfter() {
		this.prev = this.largest;
		this.cursor = null;
	}


	@Override
	public CursorPosition280 currentPosition() {
		return new TwoThreeTreePosition280<K,I>(this.cursor, this.prev);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 c) {
		if(c instanceof TwoThreeTreePosition280 ) {
			this.cursor = ((TwoThreeTreePosition280<K,I>) c).cursor;
			this.prev = ((TwoThreeTreePosition280<K,I>) c).prev;		
		}
		else {
			throw new InvalidArgument280Exception("The provided position was not a TwoThreeTreePosition280 object.");
		}
	}


	public void search(K k) {
		LinkedLeafTwoThreeNode280<K, I> found = (LinkedLeafTwoThreeNode280<K, I>) super.find(this.rootNode, k);
		if (found == null) {
			goAfter();
		}
		else {
			this.cursor = found;
			this.prev = found.prev();
		}

	}


	@Override
	public void searchCeilingOf(K k) {
		// Position the cursor at the smallest item that
		// has key at least as large as 'k', if such an
		// item exists.  If no such item exists, leave 
		// the cursor in the after position.
		
		// This one is easier to do with a linear search.
		// Could make it potentially faster but the solution is
		// not obvious -- just use linear search via the cursor.
		
		// If it's empty, do nothing; itemExists() will be false.
		if( this.isEmpty() ) 
			return;
		
		// Find first item item >= k.  If there is no such item,
		// cursor will end up in after position, and that's fine
		// since itemExists() will be false.
		this.goFirst();
		while(this.itemExists() && this.itemKey().compareTo(k) < 0) {
			this.goForth();
		}
		
	}

	@Override
	public void setItem(I x) throws NoCurrentItem280Exception, InvalidArgument280Exception {
		if (!itemExists()) {
			throw new NoCurrentItem280Exception("Current item does not exist!");
		}
		if (this.cursor.getKey1().compareTo(x.key()) != 0) {
			throw new InvalidArgument280Exception("Cannot set the current item because the key does not match!");
		}
		this.cursor.setData(x);
	}


	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		// Leave the cursor on the successor of the deleted item.
		if (!itemExists()) {
			throw new NoCurrentItem280Exception("No current item exists!");
		}
		K temp = this.itemKey();
		this.goForth();
		this.delete(temp);
	}


	
	
	
    @Override
    public String toStringByLevel() {
        String s = super.toStringByLevel();
        
        s += "\nThe Linear Ordering is: ";
        CursorPosition280 savedPos = this.currentPosition();
        this.goFirst();
        while(this.itemExists()) {
            s += this.itemKey() + ", ";
            this.goForth();
        }
        this.goPosition(savedPos);
        
        if( smallest != null)
            s += "\nSmallest: " + this.smallest.getKey1();
        if( largest != null ) {
            s += "\nLargest: " + this.largest.getKey1();
        }
        return s;
    }

	public static void main(String args[]) {

		// A class for an item that is compatible with our 2-3 Tree class.  It has to implement Keyed280
		// as required by the class header of the 2-3 tree.  Keyed280 just requires that the item have a method
		// called key() that returns its key.  You *must* test your tree using Loot objects.

		class Loot implements Keyed280<String> {
			protected int goldValue;
			protected String key;

			@Override
			public String key() {
				return key;
			}
			
			@SuppressWarnings("unused")
			public int itemValue() {
				return this.goldValue;
			}

			Loot(String key, int i) {
				this.goldValue = i;
				this.key = key;
			}

		}
		
		// Create a tree to test with.
		IterableTwoThreeTree280<String, Loot> T = new IterableTwoThreeTree280<String, Loot>();

		Loot l1 = new Loot("Leather Armor", 10);
		Loot l2 = new Loot("Potion of Healing", 100);
		Loot l3 = new Loot("Plate Armor", 350);
		Loot l4 = new Loot("+1 Mace", 2000);
		Loot l5 = new Loot("Vampiric Blade", 12000);
		Loot l6 = new Loot("Blue Loun Stone", 20000);

		// testing goFirst() on empty tree
		try {
			T.goFirst();
			System.out.println("ERROR: goFirst() should have thrown exception but found none!");
		} catch (ContainerEmpty280Exception ignored) {}

		// insert the first item
		T.insert(l1);

		// testing goFirst() and itemKey()
		T.goFirst();
		if (!T.itemKey().equals("Leather Armor")) {
			System.out.println("ERROR: Current cursor position should be at Leather Armor but is not!");
		}

		// add the rest of the items to the tree
		T.insert(l2);
		T.insert(l3);
		T.insert(l4);
		T.insert(l5);
		T.insert(l6);

		// printing the current state of the tree
		System.out.println("Initial tree state: " + T.toStringByLevel() + "\n");

		// testing search() and after() with non-existing item
		T.search("Illegal Loot");
		if (!T.after()) {
			System.out.println("ERROR: Cursor should be positioned at after position due to search for non-existing item in tree!");
		}

		// testing search() with valid item argument
		T.search("Potion of Healing");
		if (!T.itemKey().equals("Potion of Healing")) {
			System.out.println("ERROR: Cursor should be positioned at Potion of Healing but is not!");
		}

		// saving the current cursor position at Potion of Healing for later use
		CursorPosition280 savedPos = T.currentPosition();

		// testing goForth() and largest
		T.goForth();
		if (!T.largest.getData().key().equals("Vampiric Blade")) {
			System.out.println("ERROR: Cursor should be positioned at Vampiric Blade but is not!");
		}

		// move cursor to the after position and test itemKey()
		T.goForth();
		try {
			T.itemKey();
			System.out.println("ERROR: itemKey() should have thrown exception but did not!");
		} catch (NoCurrentItem280Exception ignored) {}

		// testing after()
		if (!T.after()) {
			System.out.println("ERROR: Cursor should be in after position but was not!");
		}

		// testing goForth() to move cursor beyond the after position
		try {
			T.goForth();
			System.out.println("ERROR: goForth() should have thrown an exception for moving the cursor beyond after position but did not!");
		} catch (AfterTheEnd280Exception ignored) {}

		// testing itemExists() when no item exists at the cursor position
		T.goBefore();
		if (T.itemExists()) {
			System.out.println("ERROR: itemExists() should return false when cursor is in after position but did not!");
		}

		// move cursor to the first item
		T.goForth();

		if (!T.smallest.data.key().equals("+1 Mace")) {
			System.out.println("ERROR: The key of the smallest item should be +1 Mace but was not!");
		}
		// deleting the item at the current cursor position
		T.deleteItem();
		if (!T.item().key().equals("Blue Loun Stone")) {
			System.out.println("ERROR: Cursor should be at Blue Loun Stone after deleting +1 Mace but was not!");
		}
		System.out.println("Tree after deleting first item in the tree: " + T.toStringByLevel() + "\n");

		// testing keyItemPair()
		if (!T.keyItemPair().firstItem().equals("Blue Loun Stone")) {
			System.out.println("ERROR: The item at the current cursor position should be Blue Loun Stone but was not!");
		}

		// testing searchCeilingOf()
		T.searchCeilingOf("Plate Armor");
		if (T.item().key().compareTo("Plate Armor") > 0) {
			System.out.println("ERROR: Cursor should be positioned at Plate Armor but was not!");
		}
		T.deleteItem();
		System.out.println("Tree state after deleting Plate Armor: " + T.toStringByLevel() + "\n");

		// testing goBefore(), before() and goPosition()
		T.goBefore();
		if (T.before()) {
			T.goPosition(savedPos);
		}
		if (!T.item().key().equals("Potion of Healing")) {
			System.out.println("ERROR: Cursor should be positioned at Potion of Healing but is not!");
		}
		T.deleteItem();

		// testing setItem()
		T.search("Leather Armor");
		T.setItem(new Loot("Leather Armor", 300));
		if (T.item().itemValue() != 300) {
			System.out.println("ERROR: Value of Leather Armor should have been updated to 300 but did not!");
		}
		System.out.println("Tree state after deleting Potion of Healing: " + T.toStringByLevel() + "\n");

		// deleting the rest of the items in the tree
		T.goFirst();
		while (T.itemExists()) {
			T.deleteItem();
		}

		if (!T.isEmpty()) {
			System.out.println("ERROR: Tree should be empty but is not!");
		}

		System.out.println("Regression Test Complete!");

		/* ---------- DO NOT CODE BEYOND HERE ---------- */
	}
}
