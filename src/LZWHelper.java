
/*
 * Praise be to Ms. Kaufman and Computer Science A teachers.
 * They spoke the truth when they spoke of handwritten code and BlueJ. 
 */
import java.io.BufferedWriter;
import java.util.HashMap;

public class LZWHelper {
	/*
	 * We use inheritance here for two reasons. One: Almost all of the following variables are
	 * needed in both the Decode and Encode classes. Two: Both the Encode and Decode classes
	 * implement slight variants on handleSubstringNotInDictionary, and we can implement both those
	 * slight variants while still being able to easily modify the algorithm if necessary using
	 * polymorphism.
	 */
	// decodingDictionary will keep track of what ciphertext integers correspond to which plaintext
	// strings. We will return this at the end of the function.
	HashMap<Integer, String> decodingDictionary = new HashMap<Integer, String>();
	// encodingDictionary will do the opposite of decodingDictionary, keeping track of which
	// plaintext strings correspond to which plaintext integers. We will need this to compute which
	// ciphertext strings correspond to which plaintext integers.
	HashMap<String, Integer> encodingDictionary = new HashMap<String, Integer>();
	// currentLongestSubstringInDictionary will keep track of the longest consecutive substring in
	// the plaintext which is already in our dictionary. Every time we run the decryption algorithm,
	// the starting character will be set to the character we are currently running the algorithm on
	// every time currentLongestSubstringInDictionary becomes not in the dictionary, and grows in
	// length by one otherwise.
	// we use a StringBuilder instead of a String so that we can modify the value stored inside
	// currentLongestSubstringInDictionary from within a function. (In Java, Strings are immutable,
	// so we cannot simply concatenate values to a string.)
	StringBuilder currentLongestSubstringInDictionary = new StringBuilder();
	// We need to initialize the encoding and decoding dictionaries with the charset we are using.
	// CHARSET_SIZE represents the size of our charset.
	final int CHARSET_SIZE = 256;
	// This is the maximum size of our dictionary.
	final int MAX_DICTIONARY_SIZE = 5000;

	/**
	 * Handles the case when the substring used for LZW encoding is not in the encoding dictionary.
	 * Adds the substring to the dictionary, outputs the necessary codeword to the codestream, then
	 * resets the substring.
	 * 
	 * @param ciphertext                          the integer representing one block of ciphertext
	 *                                            to be decoded into plaintext
	 * @param currentLongestSubstringInDictionary the longest consecutive substring in the plaintext
	 *                                            which is already in our dictionary
	 * @param encodingDictionary                  the dictionary we will use to keep track of the
	 *                                            encoding key-value pairs
	 * @param decodingDictionary                  the dictionary we will use to keep track of the
	 *                                            decoding key-value pairs
	 */
	protected void HandleSubstringNotInDictionary(char plaintextChar, StringBuilder currentLongestSubstringInDictionary,
			HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary) {
		AddNewSymbolToDictionary(currentLongestSubstringInDictionary, encodingDictionary, decodingDictionary);
		// resetting currentLongestSubstringInDictionary
		currentLongestSubstringInDictionary.setLength(0);
		currentLongestSubstringInDictionary.append(plaintextChar + "");
	}

	protected void HandleSubstringNotInDictionary(char plaintextChar, StringBuilder currentLongestSubstringInDictionary,
			HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary,
			BufferedWriter encodeWriter) {
		OutputToCodestream(currentLongestSubstringInDictionary.toString().substring(0,
				currentLongestSubstringInDictionary.length() - 1), encodeWriter);
		HandleSubstringNotInDictionary(plaintextChar, currentLongestSubstringInDictionary, encodingDictionary,
				decodingDictionary);
	}

	/**
	 * Adds a key-value pair derived from currentLongestSubstring to a dictionary.
	 * 
	 * @param currentLongestSubstringInDictionary the symbol to be added to the dictionaries
	 * @param encodingDictionary                  the encoding dictionary
	 * @param decodingDictionary                  the decoding dictionary
	 */
	public void AddNewSymbolToDictionary(StringBuilder currentLongestSubstringInDictionary,
			HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary) {
		System.out.println(
				"If you're seeing this, there has been an issue with how a class inherits from LZWHelper. Check to make sure that you have not changed any names or protections of functions which appear in LZWHelper.");
	}

	/**
	 * Outputs the output to a codestream.
	 * 
	 * @param output the output to be sent to a codestream
	 */
	public void OutputToCodestream(String output, BufferedWriter encodeWriter) {
		System.out.println(
				"If you're seeing this, there has been an issue with how a class inherits from LZWHelper. Check to make sure that you have not changed any names or protections of functions which appear in LZWHelper.");
	}
	/*  
  __        ___
 / o\      /o o\
|   <      |   |
 \__/      |,,,|
	 */

}