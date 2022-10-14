package lib280.tree;

/**
 * Implementation of a Huffman coder.
 * 
 * @author Mark Eramian
 *
 */
public class HuffmanCoder280 {
	
	/**
	 * For collecting the frequency of each character in the message.
	 */
	protected CharFrequency280[] frequencies;
	
	/**
	 * The message to be encoded.
	 */
	protected String message;
	
	/**
	 * Encoded message
	 */
	protected String encodedMessage;
	
	/** Codes for each ascii value between 0 and 127
	 * 
	 */
	protected String[] codes;
	
	/** 
	 * Forest of partial Huffman trees used in the build process.
	 */
	protected ArrayedMinHeap280<RootComparableLinkedSimpleTree280<CharFrequency280>> huffmanForest;
	
	/**
	 * Final Huffman lib280.tree.
	 */
	protected RootComparableLinkedSimpleTree280<CharFrequency280> huffmanTree;
	
	
	/**
	 * Obtain the encoded message as a string of bits.
	 * @return The encoded message as a bitstring.
	 */
	public String getEncodedMessage() {
		return encodedMessage;
	}

	/**
	 * Obtain the original, uncompressed message.
	 * @return The original message as a string.
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Count the frequency of characters in this.message.  
	 * Results are stored in this.frequencies.
	 */
	private void countFrequencies() {
		// Initialize the frequency of each possible character to zero.
		
		for(int i=0; i < 128; i++) {
			this.frequencies[i] = new CharFrequency280((char) i, 0);
		}
		
		// Count the frequency of each character.
		for(int i=0; i < message.length(); i++) {
			this.frequencies[message.charAt(i)].incrementFreq();
		}
	
	}
	
	
	/**
	 * Build the huffman lib280.tree.
	 * @precond huffmanForest is initialized to an empty forest, this.countFrequences() has been called.
	 * @postcond the final huffman lib280.tree is stored in this.huffmanTree
	 */
	private void buildHuffmanTree() {
		// For each charcater with non-zero frequency, add its character-frequency pair 
		// as a single-node lib280.tree to the Huffman Forest.
		// The min heap will allow us to easily obtain the trees in the forest with the smallest
		// frequency.

		for(int i=0; i < 128; i++) {
			if( this.frequencies[i].secondItem() > 0 )
				this.huffmanForest.insert(new RootComparableLinkedSimpleTree280<CharFrequency280>(null, this.frequencies[i], null));
		}
		
		// As long as there is more than one lib280.tree in the forest...
		while( this.huffmanForest.count() > 1 ) {
			// Obtain two trees in the forest with smallest total frequencies and remove them from the forest.
			
			RootComparableLinkedSimpleTree280<CharFrequency280> t1 = this.huffmanForest.item();
			this.huffmanForest.deleteItem();
			
			RootComparableLinkedSimpleTree280<CharFrequency280> t2 = this.huffmanForest.item();
			this.huffmanForest.deleteItem();
			
			// Create a new lib280.tree, with t1 and t2 as the left and right subtrees.
			RootComparableLinkedSimpleTree280<CharFrequency280> newTree = new RootComparableLinkedSimpleTree280<CharFrequency280>(t1, new CharFrequency280('\0', t1.rootItem().secondItem() + t2.rootItem().secondItem()), t2);
			
			// Put the combined lib280.tree back in the min heap.
			this.huffmanForest.insert(newTree);
		}
		
		this.huffmanTree = this.huffmanForest.item();
	}
	
	/**
	 * Extracts the bitstring codes in the huffman lib280.tree for each characater
	 * to the string array this.codes.
	 * @precond this.buildHuffmanTree() has been called.
	 * @param r root of the huffman lib280.tree (should pass in this.huffmanTree)
	 * @param bitString the bitstring prefix (normally "")
	 * @postcond bitstring for ascii code i is stored in this.codes[i]
	 */
	private void extractCodes(LinkedSimpleTree280<CharFrequency280> r, String bitString) {
		
		// Obtain the subtrees of r.
		LinkedSimpleTree280<CharFrequency280> leftSubtree = r.rootLeftSubtree();
		LinkedSimpleTree280<CharFrequency280> rightSubtree = r.rootRightSubtree();
		
		// If both subtrees are empty, we're at a leaf, put the code in to the codes array.
		if( leftSubtree.isEmpty() && rightSubtree.isEmpty() && r.rootItem().secondItem() > 0)
			this.codes[r.rootItem().firstItem()] = bitString;
			           
		// Otherwise, recurse.
		if( !leftSubtree.isEmpty() ) this.extractCodes(r.rootLeftSubtree(), bitString + "0");
		if( !rightSubtree.isEmpty() ) this.extractCodes(r.rootRightSubtree(), bitString +"1");
		
	}
	
	/**
	 * Helper method for the toString() method.
	 * @param r Root of the huffman lib280.tree to output. (normally this.huffmanTree)
	 * @param bitString bitstring prefix (normally called with "")
	 * @return String representation of the lib280.tree showing the mapping of each character/frequency
	 * pair onto its encoded bitstring.
	 */
	private String codesToString(LinkedSimpleTree280<CharFrequency280> r, String bitString) {
		
		// Obtain the subtrees of r.
		LinkedSimpleTree280<CharFrequency280> leftSubtree = r.rootLeftSubtree();
		LinkedSimpleTree280<CharFrequency280> rightSubtree = r.rootRightSubtree();
		
		// If both subtrees are empty, we're at a leaf, append the character stored in this node, and it's code.
		// (Do not append anything in the frequency of the characater is zero)
		if( leftSubtree.isEmpty() && rightSubtree.isEmpty() )
			return r.rootItem().firstItem() + " (frequency " + r.rootItem().secondItem() + "): " + bitString + "\n";
		
		// Otherwise, return the concatenation of the codes for each subtree that is non-empty.
		String temp = "";
		if( !leftSubtree.isEmpty() ) temp = temp + this.codesToString(r.rootLeftSubtree(), bitString + "0");
		if( !rightSubtree.isEmpty() ) temp = temp + this.codesToString(r.rootRightSubtree(), bitString +"1");
		
		return temp;
	}
	
	@Override
	public String toString() {
		// Print out mapping from characters to codes.
		return this.codesToString(this.huffmanTree, "");
		
	}
	
	/**
	 * Encode the message using the huffman code.
	 * @precond this.extractCodes() has been called.
	 * @postcond encoded message is stored in this.encodedMessage()
	 */
	private void encodeMessage() {
		this.encodedMessage = "";
		for(int i=0; i < this.message.length(); i++) {
			this.encodedMessage += codes[this.message.charAt(i)];
		}
	}
	
	/**
	 * Decode the encoded version of the message and return it.
	 * @return A string containing the decoded message.
	 */
	public String decodeMessage() {
		int i = 0;
		String decodedMessage = "";
		RootComparableLinkedSimpleTree280<CharFrequency280> t = this.huffmanTree;
		
		while( i < encodedMessage.length()) {
			while(!(t.rootLeftSubtree().isEmpty() && t.rootRightSubtree().isEmpty())) {
				if(this.encodedMessage.charAt(i) == '0') {
					t = t.rootLeftSubtree();
				}
				else t = t.rootRightSubtree();
				i++;
			}
			decodedMessage += t.rootItem().firstItem();
			t = this.huffmanTree;
		}
		return decodedMessage;
	}
	
	
	/**
	 * Encode a message using a huffman coder.
	 * 
	 * @param message The message to encode.
	 */
	HuffmanCoder280(String message) {
		this.message = message;

		// big enough for entire extended ASCII
		this.frequencies = new CharFrequency280[128];  
		
		// Create an empty Huffman forest
		this.huffmanForest = new ArrayedMinHeap280<RootComparableLinkedSimpleTree280<CharFrequency280>>(128);
		
		// Count the frequency of characters in the message.
		this.countFrequencies();
		
		// Build the Huffman lib280.tree.
		this.buildHuffmanTree();
		
		// Extract the Huffman codes for each character to a string array
		// where the index corresponds to the ASCII code.
		this.codes = new String[128];
		this.extractCodes(huffmanTree, "");
		
		// Encode the original message.
		this.encodeMessage();
	}
	
	
	public static void main(String args[]) {
		
		HuffmanCoder280 C;
		
		if( args.length < 1 )  C = new HuffmanCoder280("see the bees");  // The default message
		else {
			C = new HuffmanCoder280(args[0]);
		}
		
		// Print original message
		System.out.println("Original Message:\n" + C.getMessage()+"\n");

		// Print the code table.
		System.out.println("The Huffman Code:\n");
		System.out.println(C);
		
		// Print encoded message and message lengths.
		System.out.println("Encoded message:\n" + C.getEncodedMessage()+"\n");
		System.out.printf("Original message would require %d bytes.\nEncoded message requires %.0f bytes if encoded with 8-bits per byte.\n\n",
				C.getMessage().length(), Math.ceil((float)C.getEncodedMessage().length()/8.0) );
		
		// Decode the message and print it out.
		System.out.println("Decoded message:\n" + C.decodeMessage());
	}
}
