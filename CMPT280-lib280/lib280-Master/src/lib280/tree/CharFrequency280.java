package lib280.tree;
import lib280.base.Pair280;

/**
 * Stores information about the frequency of a character for
 * use in a Huffman encoder.
 * 
 * This is an extension of a Pair280<Character, Integer> where
 * this.firstItem() is a character and this.secondItem() is
 * the frequency of the character.  
 * 
 * This object implements the java.lang.Comparable interface 
 * and ordering is based on the character frequency
 * (i.e. this.secondItem()).
 * 
 * @author Mark Eramian
 *
 */
public class CharFrequency280 extends Pair280<Character, Integer> implements Comparable<CharFrequency280> {

	/**
	 * Create a new charcter-frequency pair.
	 * @param c Character whose frequency is to be recorded.
	 * @param freq Frequency (number of occurrences) of c.
	 */
	public CharFrequency280(Character c, Integer freq) {
		super(c, freq);
	}

	/**
	 * Increase the frequency of this character by 1.
	 */
	public void incrementFreq() {
		this.setSecondItem(this.secondItem() + 1);
	}
	
	/**
	 * Ordering is based only on the frequency (second component) of the pair.
	 */
	@Override
	public int compareTo(CharFrequency280 o) {
		return this.secondItem().compareTo(o.secondItem());
	}

}
