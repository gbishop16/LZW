import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class Decode extends LZWHelper{


	/*
	 * TODO: Handle IOExceptions better
	 * TODO: Merge initializeEncodingDictionary and initializeDecodingDictionary into a single function that calls addNewSymbolToDictionary CHARSET_SIZE times
	 */

	
	/**
	 * This method decodes a sequence of space-delimited integers representing an LZW-encoded file.
	 * It does so by reading in a file containing nothing but a sequence of comma-delimited integers, running a function which finds the hashmap that the original LZW encoding algorithm used to encode the plaintext, and uses it to decode the ciphertext.
	 * Takes in a filename that ends in .lzw and writes the plaintext to a file whose name is filename.substring(0, filename.length()-4).
	 * It is known that the getLZWDecodingHashMap function more or less already decodes the input on its own. 
	 * This is an artifact of the way work was divided up. 
	 * A future optimization would be to use the getLZWDecodingHashMap function to directly decode the file, instead of using this moderately slow method. However, the big O is still O(n) where n is the length of the file, so it is probably alright.
	 * @param filename 
	 * @throws IOException 
	 */
	public void decode(String filename) throws IOException
	{
		//convert the encoded file into an int array
		int[] encodedInts = convertFileToArray(filename);
		
		//retrieve the hashmap dictionary based on the encoded file
		HashMap<Integer,String> dictionary= getLZWDecodingHashMap(encodedInts);
		
		//create a blank file where the decoded string would be written
		String decodedFilename = filename.substring(0, filename.length()-4);
		File decodedFile = new File(decodedFilename);
		FileWriter decodeWriter = new FileWriter(decodedFile);
		
		//loop through the encoded integers, convert them into string using the dictionary and write them to the file
		for(int i=0;i<encodedInts.length;i++)
		{
			decodeWriter.write(dictionary.get(encodedInts[i]));
		}
		decodeWriter.close();
	}
	
	/**
	 * Returns an int[] that contains the encoded file expressed in terms of integers (converted from chars)
	 * Takes in the filename of the encoded file as a String
	 * It uses a BufferedReader to read the original file and converts it into an arraylist by looping through all the characters in the file. Afterwards, it converts the arraylist into an int[] for ease of use.
	 * 
	 * @param filename
	 * @return the int array that will be used as the base to deciper the original file 
	 */
	private int[] convertFileToArray(String filename)
	{
		
		//create an arraylist that will take in the encoded file in the form of ints from chars
		ArrayList<Integer> encodedFileInts = new ArrayList<Integer>();
		
		try {
			//read the encoded file as an input
			BufferedReader encodedFileReader = new BufferedReader(new FileReader(filename));
			
			int currentChar;
			String currentBlock = "";

			//iterate through the encoded file and convert it into a string
			while((currentChar = encodedFileReader.read())!=-1)
			{
				if(currentChar == (int)' ')
				{
					encodedFileInts.add(Integer.parseInt(currentBlock));
					currentBlock = "";
					
				}
				else {
					currentBlock += (char)currentChar;
				}
			}
			encodedFileReader.close();
		} catch (Exception e) {
			
		}

		//convert the arraylist into an integer array
		int[] encodedFileIntsArray = new int[encodedFileInts.size()];
		for(int i = 0; i < encodedFileInts.size(); i++)
		{
			encodedFileIntsArray[i] = encodedFileInts.get(i); 
		}
		

		return encodedFileIntsArray;
	}

	/**
	 * Returns a HashMap<Integer, String> object that represents the dictionary that an LZW algorithm would create to encode a plaintext string.
	 * Takes in ciphertext formatted as an array of integers.
	 * For example, if the input is [97,98,99,256,99] where the plaintext is abcabc, then the HashMap would have as key-value pairs (256, ab), (257, bc), (258, ca), (259, abc)
	 *
	 * @param ciphertextAsArray the ciphertext that will correspond to the HashMap we return.
	 * @return the HashMap that can be used to decode the ciphertext.
	 */
	private HashMap<Integer, String> getLZWDecodingHashMap(int[] ciphertextAsArray)
	{

		//The following two methods will initialize our encoding and decoding dictionaries, respectively.
		initializeDictionaries(encodingDictionary,decodingDictionary,CHARSET_SIZE);
		//We will decode all of the ciphertext that we can, perform LZW encoding on it to get a portion of the decoding dictionary, use that portion of the decoding dictionary to decode more of the ciphertext, and iterate this until the ciphertext is completely decoded.
		for(int i = 0; i < ciphertextAsArray.length; i++)
		{
			decodeSection(ciphertextAsArray[i], currentLongestSubstringInDictionary, encodingDictionary, decodingDictionary);
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
	private void decodeSection(int ciphertext, StringBuilder currentLongestSubstringInDictionary, HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary)
	{
		//plaintextChunk will contain the plaintext that ciphertext corresponds to.
		String plaintextChunk = new String();
		plaintextChunk = decodingDictionary.get(ciphertext);
		if(plaintextChunk == null)
		{
			handleCiphertextNotInDictionaryError(currentLongestSubstringInDictionary, encodingDictionary, decodingDictionary);
			plaintextChunk = decodingDictionary.get(ciphertext);
		}
		//We will now iterate through the LZW encoding-esque algorithm for each character in plaintextChunk
		for(int i = 0; i < plaintextChunk.length(); i++)
		{
			currentLongestSubstringInDictionary.append(plaintextChunk.charAt(i) + "");
			if(encodingDictionary.get(currentLongestSubstringInDictionary.toString()) == null)
			{
				handleSubstringNotInDictionary(plaintextChunk.charAt(i), currentLongestSubstringInDictionary, encodingDictionary, decodingDictionary);
			}
		}
	}
	/**
	 * Adds the key-value pair (currentLongestSubstringInDictionary, encodingDictionary.size()) to encodingDictionary and the key-value pair (decodingDictionary.size(), currentLongestSubstringInDictionary) to decodingDictionary.
	 * @param currentLongestSubstringInDictionary the symbol to be added to the dictionaries
	 * @param encodingDictionary the encoding dictionary
	 * @param decodingDictionary the decoding dictionary
	 */
	public void addNewSymbolToDictionary(StringBuilder currentLongestSubstringInDictionary, HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary)
	{
		if(encodingDictionary.size() < MAX_DICTIONARY_SIZE)
		{
			String symbol = currentLongestSubstringInDictionary.toString();
			encodingDictionary.put(symbol, encodingDictionary.size());
			decodingDictionary.put(decodingDictionary.size(), symbol);
		}else{
			System.out.println ("ERROR: Max Dictionary Size Reached");
		}
		
	}
	/**
	 * This method initializes the encoding and decoding dictionaries with ascii characters
	 * @param encodingDictionary the encoding dictionary to be initialized
	 * @param decodingDictionary the decoding dictionary to be initialized
	 * @param CHARSET_SIZE the size of the charset
	 */
	public void initializeDictionaries (HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary, int CHARSET_SIZE){
		for(int i = 0; i < CHARSET_SIZE ; i++){
			String asciiChar=(char)(i)+"";
			addNewSymbolToDictionary(new StringBuilder (asciiChar),encodingDictionary,decodingDictionary);
		}
	}
	/**
	 * We will have to handle a strange edge case in which, during LZW encoding, the information required to add a symbol into the dictionary is contained within the first encoded instance of that symbol.
	 * In other words, the information required to add, say, "bb=256" cannot be accessed, because to add "bb" you need to first decode "256", but to decode "256" you must first add "bb."
	 * By way of example, consider encoding the string "bbbb". Each of the steps are enumerated below:
	 * P = null, C = b, no output, no change in dictionary
	 * P = b, C = b, output 98 (b), add bb (256) to dictionary
	 * P = b, C = b, no output, no change in dictionary
	 * P = bb, C = b, output bb (256) and b (98) [both are outputted because we have reached the end and triggered the edge case], no change in dictionary
	 * The final output will be 98,256,98. But when we perform our algorithm of decoding everything that we can, adding every key that we can create from the decoded part to the dictionary, and iterating, we find a problem.
	 * Initially, we can only decode 98 (b). But from here, without the edge case handling which is about to occur, we will not be able to assign a symbol to 256.
	 * The only way in which this error can occur is if a symbol s is put into the dictionary, then immediately afterwards, a symbol which consists of s plus another character is added into the dictionary.
	 * The only way in which that scenario can occur is if a symbol starts and ends with the same character. (A proof of this is trivial.)
	 * Therefore, if and only if we encounter a symbol not in our dictionary, we may safely handle it by taking the last observed symbol s and adding s+s.charAt(0) to our dictionary.
	 * @param currentLongestSubstringInDictionary the symbol we will use to handle this edge case
	 * @param encodingDictionary the encoding dictionary
	 * @param decodingDictionary the decoding dictionary
	 */
	private void handleCiphertextNotInDictionaryError(StringBuilder currentLongestSubstringInDictionary, HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary)
	{
		addNewSymbolToDictionary(new StringBuilder(currentLongestSubstringInDictionary.toString()+currentLongestSubstringInDictionary.toString().charAt(0)), encodingDictionary, decodingDictionary);
	}
	

}
