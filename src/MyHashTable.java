/*
 * TCSS 342 martensk
 * Assignment 5: Compressed Literature 2
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * This class represents a hash table which can store (Key Value) pairs to access
 * in constant time.
 * 
 * @author Kyle
 * @version 1.0
 * 
 * @param <K> The type of keys in this hash table.
 * @param <V> The type of values in this hash table.
 */
public class MyHashTable<K, V> {
	
	/**
	 * This class represents a pairing for a key and a value.
	 * i.e. this key => this value.
	 * 
	 * @author Kyle
	 * @version 1.0
	 */
	private class Pairing {
		
		/**
		 * The key in this pair.
		 */
		private K key;
		
		/**
		 * The value in this pair.
		 */
		private V value;
		
		/**
		 * Constructs a new pairing for the key and value.
		 * This method will run in O(1) time.
		 * 
		 * @param key The key for the pair.
		 * @param value The value for the pair.
		 */
		private Pairing(K key, V value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Checks whether the key given is equal to the key for this pair.
		 * This method will run in O(1) time.
		 * 
		 * @param key The key to check for equality.
		 * @return Whether the key given is equal to the key for this pair.
		 */
		private boolean haveEqualKeys(K key) {
			return this.key.equals(key);
		}
		
		/**
		 * Gets the the value for this pairing.
		 * This method will run in O(1) time.
		 * 
		 * @return This pair's value.
		 */
		private V getValue() {
			return value;
		}
	}
	
	/**
	 * The list of all the values that are recognized in this hash table.
	 */
	private List<List<Pairing>> values;
	
	/**
	 * The set of keys recognized by this hash table.
	 */
	private Set<K> keySet;
	
	/**
	 * The number of buckets in this hash table.
	 */
	private int capacity;
	
	/**
	 * The number of entries to this hash table.
	 */
	private int entryCounter;
	
	/**
	 * A histogram for the size of the chain in each bucket.
	 */
	private List<Integer> bucketSizeHist;
	
	/**
	 * Constructs a new instance of the hash table with the given capacity.
	 * This method will run in O(n) time.
	 * 
	 * @param capacity the number of buckets for this hash table.
	 */
	public MyHashTable(int capacity) {
		this.capacity = capacity;
		values = new ArrayList<List<Pairing>>(capacity);
		keySet = new HashSet<K>();
		entryCounter = 0;
		bucketSizeHist = new ArrayList<Integer>();
		bucketSizeHist.add(capacity);
		for(int i = 0; i < capacity; i++) {
			values.add(new LinkedList<Pairing>());
		}
	}
	
	/**
	 * Puts the key value pair into the hash table. We use chaining to account for collisions
	 * in buckets. If the pair is already in the hash table, the value is Overridden
	 * This method will run in O(1) time, because the size of each chain is small.
	 * 
	 * @param key The key to find the bucket for the value.
	 * @param value the value to be placed in the bucket.
	 */
	public void put(K key, V value) {
		int index = hash(key);
		if(values.get(index) == null) {
			values.set(index, new LinkedList<Pairing>());
		}
		List<Pairing> chain = (LinkedList<Pairing>) values.get(index);
		if(chain == null) {
			chain = new LinkedList<Pairing>();
		}
		
		boolean found = false;
		for(int i = 0; i < chain.size(); i++) {
			Pairing curPair = chain.get(i);
//			overrides the current value.
			if(curPair.haveEqualKeys(key)) {
				curPair.value = value;
				chain.set(i, new Pairing(key, value));
				found = true;
			}
		}
//		if the pair is not found, the bucket must be empty.
		if(!found) {
			chain.add(new Pairing(key, value));
		}
		keySet.add(key);
		while((bucketSizeHist.size() <= chain.size())) {
			bucketSizeHist.add(0);
		}
//		applies the changes to the bucket sizes to the histogram.
		bucketSizeHist.set(chain.size(), bucketSizeHist.get(chain.size()) + 1);
		bucketSizeHist.set(chain.size() - 1, bucketSizeHist.get(chain.size() - 1) - 1);		
		entryCounter++;
	}
	
	/**
	 * Finds the current value for the given key. If no value is found, returns null.
	 * This method will run in O(1) time, because the size of each chain is small.
	 * 
	 * @param key The key to search for.
	 * @return the value for the given key, null if the key is not found.
	 */
	public V get(K key) {
		int index = hash(key);
		List<Pairing> chain = (LinkedList<Pairing>) values.get(index);
		if(chain != null) {
			for(int i = 0; i < chain.size(); i++) {
				if(chain.get(i).haveEqualKeys(key)) {
					return chain.get(i).getValue();
				}
			}
		}
		return null;
	}
	
	/**
	 * This method prints out some stats about the hash table about how full each
	 * bucket is, how many entries are store in this hash table, the average number
	 * of items per bucket, etc.
	 * This method runs in constant time.
	 */
	public void stats() {
		double fillPerc =  1 - (double) bucketSizeHist.get(0) / capacity;
		double nonEmptyBucket = ((double) entryCounter / (capacity - bucketSizeHist.get(0)));
		
		System.out.println("Hash Table Stats");
		System.out.println("===================");
		System.out.println("Number of Entries: " + entryCounter);
		System.out.println("Number of Buckets: " + capacity);
		System.out.println("Histogram of Bucket Sizes: " + bucketSizeHist.toString());
		System.out.println("Fill Percentage: " + fillPerc + "%");
		System.out.println("Average Non-Empty Bucket: " + nonEmptyBucket);
		System.out.println();
	}
	
	/**
	 * Gets a unique code for the key to find its location in the hash table.
	 * This method will run in constant time.
	 * 
	 * @param key The key to find the hashcode for.
	 * @return The key's location in the hash table.
	 */
	private int hash(K key) {
		return key.hashCode()%(capacity/2) + (capacity/2);
	}
	
	/**
	 * @return The set of all keys recognized by the hash table.
	 * This method will run in constant time.
	 */
	public Set<K> keySet() {
		return keySet;
	}
	
	/**
	 * This method returns a String to represent the data stored in the hash table,
	 * in the form "(key = value)" surrounded by brackets.
	 * This method will run in O(n) time, where n is the number of keys in the
	 * hash table.
	 * @return A String representation of the hash table.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[");
		Iterator<K> iter = keySet.iterator();
		if(iter.hasNext()) {
			K first = iter.next();
			result.append("(");
			result.append(first.toString());
			result.append(" = ");
			result.append(get(first).toString());
			result.append(")");
		}
		while(iter.hasNext()) {
			K key = iter.next();
			result.append(", ");
			result.append("(");
			result.append(key);
			result.append(" = ");
			result.append(get(key).toString());
			result.append(")");
		}
		result.append("]");
		return result.toString();
	}
}
