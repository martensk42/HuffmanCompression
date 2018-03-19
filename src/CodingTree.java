/*
 * TCSS 342 martensk
 * Assignment 5: Compressed Literature 2
 */

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This class represents a tree of codes that have had the Huffman Compression algorithm
 * applied to them.
 * 
 * @author Kyle
 * @version 1.0
 */
public class CodingTree {
	
	/**
	 * This class represents a manager that keeps track of the frequency of letters in a
	 * supplied text, and uses Huffman's encoding to generate a tree.
	 * 
	 * @author Kyle
	 * @version 1.0
	 */
	private class FrequencyManager {

		/**
		 * The tree of letter encodings.
		 */
		Queue<HuffNode> huffTree;
		
		/**
		 * Constructs a new instance of the manager for the given message.
		 * This method will run in O(nlog(n))
		 * 
		 * @param message the string with which to manage the frequency of letters in.
		 */
		private FrequencyManager(String message) {
			huffTree = new PriorityQueue<HuffNode>();
			
			StringBuffer wordBuffer = new StringBuffer();
			MyHashTable<String, Integer> letterFrequency = new MyHashTable<String, Integer>(16384);
//			assigns a frequency for each word and separator in the message
			for(int i = 0; i < message.length(); i++) {
				Character letter = message.charAt(i);
//				if the letter is an element of a word
				if((letter.compareTo('A') >= 0 && letter.compareTo('Z') <= 0)
						|| (letter.compareTo('a') >= 0 && letter.compareTo('z') <= 0)
						|| (letter.compareTo('0') >= 0 && letter.compareTo('9') <= 0)
						|| letter.equals('\'') || letter.compareTo('-') == 0) {
					wordBuffer.append(letter);
				}
//				the letter is a separator
				else {
					String word = new String(wordBuffer);
					if(word.length() > 0) {
//						add the word to the frequency map
						if(letterFrequency.get(word) == null) {
							letterFrequency.put(word, 0);
						}
						letterFrequency.put(word, letterFrequency.get(word) + 1);

					}
//					add the separator to the frequency map
					String separator = letter.toString();
					if(letterFrequency.get(separator) == null) {
						letterFrequency.put(separator, 0);
					}
					letterFrequency.put(separator, letterFrequency.get(separator) + 1);
//					empty the word buffer to start a new word
					wordBuffer = new StringBuffer();
				}
			}
//			add the words and separators into a priority queue
			for(String word: letterFrequency.keySet()) {
				huffTree.offer(new HuffNode(word, letterFrequency.get(word)));
			}
		}
		
		/**
		 * Performs the Huffman encoding algorithm.
		 * This method will run in O(nlog(n))
		 * 
		 * @return the resulting tree of letter codes.
		 */
		private HuffNode compressQueue() {
			while(huffTree.size() > 1) {
				HuffNode first = huffTree.poll();
				HuffNode second = huffTree.poll();
				HuffNode newNode = new HuffNode("", first.freq + second.freq,
												first, second);
				huffTree.offer(newNode);
			}
			
			return huffTree.poll();
		}
	}
	
	/**
	 * The string of codes for the words.
	 */
	public MyHashTable<String, String> codes;
	
	/**
	 * Constructs a new instance of the coding tree, performs Huffman encoding,
	 * and populates the map of codes.
	 * This method will run in O(nlog(n)) time
	 * 
	 * @param message The message to compress.
	 */
	public CodingTree(String message) {
		codes = new MyHashTable<String, String>(16384);
		FrequencyManager manager = new FrequencyManager(message);
		HuffNode compressedTree = manager.compressQueue();
		
		buildMap(compressedTree, "");
	}
	
	/**
	 * Adds the nodes to the map with its respective binary code.
	 * This method has a runtime of O(n) where n is the number of nodes
	 * to add to the map.
	 * 
	 * @param node The node to add to the map.
	 * @param code The code to add to the map.
	 */
	private void buildMap(HuffNode node, String code){
		if((node.zero == null) && (node.one == null)) {
			codes.put(node.data, code);
		} else {
			buildMap(node.zero, code + "0");
			buildMap(node.one, code + "1");
		}
	}
}
