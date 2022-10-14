//Sunny Ka Patel
//SDK438
//11267665
//CMPT 280 - Assn. 1


package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 * @timing O(1)
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{
		return new BilinkedNode280<>(item);
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list
	 * @timing O(1)
	 */
	public void insertFirst(I x) 
	{
		BilinkedNode280<I> newNode = createNewNode(x);

		newNode.setNextNode(this.head);
		newNode.setPreviousNode(null);

		// update the cursor to point at the new node
		if (!this.isEmpty() && this.position == this.head) {
			this.prevPosition = newNode;
		}

		//special case: if list is empty the tail is the new node
		if (this.isEmpty()) {
			this.tail = newNode;
		}
		this.head = newNode;
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list
	 * @timing O(1)
	 */
	public void insertLast(I x) 
	{
		BilinkedNode280<I> newNode = new BilinkedNode280<>(x);

		//if cursor is after(), then cursor predecessor becomes the new node
		if (this.after() && !this.isEmpty()) {
			this.prevPosition = newNode;
		}

		//special case: when list is empty
		if (this.isEmpty()) {
			this.head = newNode;
		}
		//edit the references for the new node
		else {
			this.tail.setNextNode(newNode);
			newNode.setPreviousNode((BilinkedNode280<I>) this.tail);
		}
		newNode.setNextNode(null);
		this.tail = newNode;
	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 * @timing O(1)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		//case when item does not exist
		if (!this.itemExists()) {
			throw new NoCurrentItem280Exception("No item at the cursor to delete.");
		}

		//case if we are deleting the first item
		if (this.position == this.head) {
			this.deleteFirst();
			this.position = this.head;
		}
		else {
			// a <=> b <=> c
			// a <=> c (cursor point to c)

			//set the previous node to point to the successor node
			this.prevPosition.setNextNode(this.position.nextNode());
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>) this.position).previousNode());

			//case when we are deleting the last node
			if (this.position == this.tail) {
				this.tail = this.prevPosition;
			}
			this.position = this.position.nextNode();
		}
	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 * @timing O(1)
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
		//case when list is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");
		}

		//if cursor is on the second node, set the prev pointer to null
		if (this.prevPosition == this.head) {
			this.prevPosition = null;
		}
		//if cursor is on the first node, set the cursor to the next node
		else if (this.position == this.head) {
			this.position = this.position.nextNode();
		}

		//case when list has only one item
		if (this.head == this.tail) {
			this.tail = null;
		}

		//unlinking the first node
		BilinkedNode280<I> oldHead = (BilinkedNode280<I>) this.head;
		this.head = this.head.nextNode();
		oldHead.setNextNode(null);
	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 * @timing O(n)
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
		//special case when list has 0 or 1 node
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");
		}
		else if (this.head != null && this.head == this.tail) {
			deleteFirst();
		}
		else {
			//update cursor when cursor on the last node
			if (this.position == this.tail) {
				// only 2 items in list
				// a <=> b (cursor at b)
				if (prevPosition == head) {
					tail = head;
					position = head;
					prevPosition = null;
					this.head.nextNode = null;
					return;
				}

				//find the node prior to this.position
				LinkedNode280<I> newPrev = this.head;
				while (newPrev.nextNode() != this.prevPosition) {
					newPrev = newPrev.nextNode();
				}
				this.position = this.prevPosition;
				this.prevPosition = newPrev;
			}

			//find the second-last node
			LinkedNode280<I> penultimate = this.head;
			while (penultimate.nextNode() != this.tail) {
				penultimate = penultimate.nextNode();
			}

			//if the cursor is in the after() position, then prevPosition has to become the second last node
			if (this.after()) {
				this.prevPosition = penultimate;
			}

			//unlink the last node
			penultimate.setNextNode(null);
			this.tail = penultimate;
			this.prevPosition = ((BilinkedNode280<I>) this.tail).previousNode;
		}
	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 * @timing O(1)
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot move the cursor to the last element when list is empty.");
		}
		this.position = this.tail;
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		if (this.position == this.head) {
			throw new BeforeTheStart280Exception("Cannot position the cursor before the first node");
		}
		else {
			this.position = this.prevPosition;
		}
	}

	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {
		//create an empty bilinked list
		BilinkedList280<Integer> L = new BilinkedList280<>();

		//testing isEmpty()
		System.out.println("List should be empty...");
		if (L.isEmpty()) {
			System.out.println("and it is.");
		}
		else {
			System.out.println("ERROR: and it is *NOT*.");
		}

		//testing insertFirst() and insertLast()
		System.out.println("\nTesting insert methods...");
		L.insertFirst(1);
		L.insertFirst(2);
		L.insertLast(9);
		L.insertLast(10);
		System.out.println("List should be: 2, 1, 9, 10, ");
		System.out.println("     and it is: " + L);
		System.out.println("List head should be: 2 1 9 10");
		System.out.println("          and it is: " + L.head);
		System.out.println("List tail should be: 10");
		System.out.println("          and it is: " + L.tail);

		//testing deleteFirst() and deleteLast()
		System.out.println("\nTesting deleteFirst()...");
		L.deleteFirst();
		System.out.println("List should be: 1, 9, 10, ");
		System.out.println("     and it is: " + L);

		System.out.println("\nTesting deleteLast()...");
		L.deleteLast();
		System.out.println("List should be: 1, 9, ");
		System.out.println("     and it is: " + L);

		//testing goBack() and goLast()
		System.out.println("\nTesting goLast()...");
		L.goLast();
		System.out.println("Cursor should be at 9...");
		if (L.item() == 9) {
			System.out.println("     and it is. OK!");
		}
		else {
			System.out.println("     and it is not. ERROR!");
		}

		System.out.println("\nTesting goBack()...");
		L.goBack();
		System.out.println("Cursor should be at 1...");
		if (L.item() == 1) {
			System.out.println("     and it is. OK!");
		}
		else {
			System.out.println("     and it is not. ERROR!");
		}

		//testing deleteItem()
		System.out.println("\nTesting deleteItem()...");
		L.deleteItem();
		System.out.println("deleteItem() should delete 1.\nThe only remaining number should be: 9...");
		System.out.println("     and it is: " + L.item().toString());

		L.deleteItem();
		System.out.println("deleteItem() should delete 9 and list should be empty...");
		if (L.isEmpty()) {
			System.out.println("     and it is.");
		}
		else {
			System.out.println("ERROR: and it is *NOT*.");
		}

		//test deleting from an empty list
		System.out.println("\nDeleting item using deleteItem() from empty list...");
		try {
			L.deleteItem();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		} catch (NoCurrentItem280Exception e) {
			System.out.println("Caught exception. OK!");
		}

		System.out.println("\nDeleting item using deleteFirst() from empty list...");
		try {
			L.deleteFirst();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		} catch (ContainerEmpty280Exception e) {
			System.out.println("Caught exception. OK!");
		}

		System.out.println("\nDeleting item using deleteLast() from empty list...");
		try {
			L.deleteLast();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		} catch (ContainerEmpty280Exception e) {
			System.out.println("Caught exception. OK!");
		}

		//testing goLast() and goBack() on empty list
		System.out.println("\nTesting goLast() on empty list...");
		try {
			L.goLast();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		} catch (ContainerEmpty280Exception e) {
			System.out.println("Caught exception. OK!");
		}

		System.out.println("\nTesting goBack() on empty list...");
		try {
			L.goBack();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		} catch (BeforeTheStart280Exception e) {
			System.out.println("Caught exception. OK!");
		}

		//testing goBack() on before
		System.out.println("\nTesting goBack() on first item...");
		L.insertFirst(1);
		L.goFirst();
		try {
			L.goBack();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");
		} catch (BeforeTheStart280Exception e) {
			System.out.println("Caught exception. OK!");
		}
	}
} 
