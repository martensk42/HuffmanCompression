/*
 * TCSS 342 martensk
 * Assignment 5: Compressed Literature 2
 */

/**
 * This class represents a simple node, which has access to its letter, frequency,
 * and two references to other nodes.
 * 
 * @author Kyle
 * @version 1.0
 */
public class HuffNode implements Comparable <HuffNode>{
	
	/**
 	 * The word stored in this node.
 	 */
	public String data;
	
	/**
	 * The frequency of this node's character.
	 */
	public int freq;
	
	/**
	 * The node to the zero of this node.
	 */
	public HuffNode zero;
	
	/**
	 * The node to the one of this node.
	 */
	public HuffNode one;
	
	/**
	 * Constructs a default/ no-arg node.
	 * This method will run in constant time.
	 */	
	public HuffNode() {
		this("", 0, null, null);
	}
	
	/**
	 * Constructs a node with the given word and frequency.
	 * This method will run in constant time.
	 * 
	 * @param word the word for this node.
	 * @param freq the frequency for this node.
	 */
	public HuffNode(String word, int freq) {
		this(word, freq, null, null);
	}
	
	/**
	 * Constructs a node with the given word, frequency, and zero and one references.
	 * This method will run in constant time.
	 * 
	 * @param word the word for this node.
	 * @param freq the frequency for this node.
	 * @param zero the node to the zero of this node.
	 * @param one the node to the one of this node.
	 */
	public HuffNode(String word, int freq, HuffNode zero, HuffNode one) {
		data = word;
		this.freq = freq;
		this.zero = zero;
		this.one = one;
	}

	/**
	 * Compares the frequency of this node and the other node.
	 * This method will run in constant time.
	 * 
	 * @param other The other node to compare.
	 */
	@Override
	public int compareTo(HuffNode other) {
		return this.freq - other.freq;
	}
	
}