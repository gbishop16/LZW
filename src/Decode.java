import java.util.HashMap;

public class Decode{
	
	/**
	 * Returns a HashMap<Integer, String> object that represents the dictionary that an LZW algorithm would create to encode a plaintext string.
	 * Takes in ciphertext formatted as an array of integers.
	 * For example, if the input is [97,98,99,256,99] where the plaintext is abcabc, then the HashMap would have as key-value pairs (256, ab), (257, bc), (258, ca), (259, abc)
	 *  
	 * @param ciphertextAsArray the ciphertext that will correspond to the HashMap we return. 
	 * @return the HashMap that can be used to decode the ciphertext.
	 */
	public static HashMap<Integer, String> getLZWDecodingHashMap(int[] ciphertextAsArray)
	{
		//decodingDictionary will keep track of what ciphertext integers correspond to which plaintext strings. We will return this at the end of the function.
		HashMap<Integer, String> decodingDictionary = new HashMap<Integer, String>();
		//We will essentially perform LZW encoding again to find the decoding dictionary. The following variables will all be needed for this algorithm.
		//encodingDictionary will do the opposite of decodingDictionary, keeping track of which plaintext strings correspond to which plaintext integers. We will need this to compute which ciphertext strings correspond to which plaintext integers.
		HashMap<String, Integer> encodingDictionary = new HashMap<String, Integer>();
		//currentLongestSubstringInDictionary will keep track of the longest consecutive substring in the plaintext which is already in our dictionary. Every time we run the decryption algorithm, the starting character will be set to the character we are currently running the algorithm on every time currentLongestSubstringInDictionary becomes not in the dictionary, and grows in length by one otherwise. 
		//we use a StringBuilder instead of a String so that we can modify the value stored inside currentLongestSubstringInDictionary from within a function. (In Java, Strings are immutable, so we cannot simply concatenate values to a string.)
		StringBuilder currentLongestSubstringInDictionary = new StringBuilder();
		//We need to initialize the encoding and decoding dictionaries with the charset we are using. CHARSET_SIZE represents the size of our charset.
		final int CHARSET_SIZE = 256;
		//The following two methods will initialize our encoding and decoding dictionaries, respectively.
		initializeEncodingDictionary(encodingDictionary, CHARSET_SIZE);
		initializeDecodingDictionary(decodingDictionary, CHARSET_SIZE);
		//We will decode all of the ciphertext that we can, perform LZW encoding on it to get a portion of the decoding dictionary, use that portion of the decoding dictionary to decode more of the ciphertext, and iterate this until the ciphertext is completely decoded. 
		for(int i = 0; i < ciphertextAsArray.length; i++)
		{
			decode(ciphertextAsArray[i], currentLongestSubstringInDictionary, encodingDictionary, decodingDictionary);
		}
		return decodingDictionary;
	}
	/**
	 * Performs one step of the decoding algorithm. Takes in the ciphertext, uses the decodingDictionary to decode it, then for each character in the decoded chunk, it applies the LZW encoding algorithm.
	 * Namely, for each character in the decoded chunk, it appends it to currentLongestSubstringInDictionary, then checks whether currentLongestSubstringInDictionary is in encodingDictionary.
	 * If it is, then it moves on to the next character, or if there is no next character, returns.
	 * If it is not, then it adds the key-value pair (currentLongestSubstringInDictionary, encodingDictionary.size()) to encodingDictionary and the key-value pair (decodingDictionary.size(), currentLongestSubstringInDictionary) to decodingDictionary.
	 * It then sets currentLongestSubstringInDictionary to the character currently being observed. 
	 * @param ciphertext the integer representing one block of ciphertext to be decoded into plaintext
	 * @param currentLongestSubstringInDictionary the longest consecutive substring in the plaintext which is already in our dictionary 
	 * @param encodingDictionary the dictionary we will use to keep track of the encoding key-value pairs
	 * @param decodingDictionary the dictionary we will use to keep track of the decoding key-value pairs
	 */
	private static void decode(int ciphertext, StringBuilder currentLongestSubstringInDictionary, HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary)
	{
		//plaintextChunk will contain the plaintext that ciphertext corresponds to.
		//System.out.println(ciphertext);
		String plaintextChunk = new String();
		plaintextChunk = decodingDictionary.get(ciphertext);
		//System.out.println(plaintextChunk);
		//System.out.println(currentLongestSubstringInDictionary);
		//we will now iterate through the LZW encoding-esque algorithm for each character in plaintextChunk
		for(int i = 0; i < plaintextChunk.length(); i++)
		{
			currentLongestSubstringInDictionary.append(plaintextChunk.charAt(i) + "");
			if(encodingDictionary.get(currentLongestSubstringInDictionary.toString()) == null)
			{
				addNewSymbolToDictionary(currentLongestSubstringInDictionary, encodingDictionary, decodingDictionary);
				//System.out.println(decodingDictionary);
				//resetting currentLongestSubstringInDictionary
				currentLongestSubstringInDictionary.setLength(0); 
				currentLongestSubstringInDictionary.append(plaintextChunk.charAt(i) + "");
			}
		}
	}
	/**
	 * Adds the key-value pair (currentLongestSubstringInDictionary, encodingDictionary.size()) to encodingDictionary and the key-value pair (decodingDictionary.size(), currentLongestSubstringInDictionary) to decodingDictionary.
	 * @param currentLongestSubstringInDictionary the symbol to be added to the dictionaries
	 * @param encodingDictionary the encoding dictionary
	 * @param decodingDictionary the decoding dictionary
	 */
	private static void addNewSymbolToDictionary(StringBuilder currentLongestSubstringInDictionary, HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary)
	{
		String symbol = currentLongestSubstringInDictionary.toString();
		encodingDictionary.put(symbol, encodingDictionary.size());
		decodingDictionary.put(decodingDictionary.size(), symbol);
	}
	/**
	 * This function will run through a loop CHARSET_SIZE times in which it adds the key value pair (c, i) where i is the index of the loop and c is the character corresponding to that index, as a string.
	 * @param encodingDictionary the dictionary to be initialized
	 * @param CHARSET_SIZE the size of the charset
	 */
	private static void initializeEncodingDictionary(HashMap<String, Integer> encodingDictionary, int CHARSET_SIZE)
	{
		for(int i = 0; i < CHARSET_SIZE; i++)
		{
			encodingDictionary.put((char)(i) + "", i);
		}
	}
	/**
	 * This function will run through a loop CHARSET_SIZE times in which it adds the key value pair (i, c) where i is the index of the loop and c is the character corresponding to that index, as a string.
	 * @param decodingDictionary the dictionary to be initialized
	 * @param CHARSET_SIZE the size of the charset
	 */
	private static void initializeDecodingDictionary(HashMap<Integer, String> decodingDictionary, int CHARSET_SIZE)
	{
		for(int i = 0; i < CHARSET_SIZE; i++)
		{
			decodingDictionary.put(i, (char)(i) + "");
		}
	}
	
	

}