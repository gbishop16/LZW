
/*
 * Praise be to Ms. Kaufman and Computer Science A teachers.
 * They spoke the truth when they spoke of handwritten code and BlueJ. 
 */
import java.util.*;

import java.io.*;

public class Encode extends LZWHelper {

	public Encode() {

	}

	/**
	 * This function encodes plaintext inside a .txt file and writes an LZW-encoded version of that
	 * string to a file named "(original file name) encoded.txt". The format of the content of the
	 * encoded file is a string of space-delimited integers (e.g. 96, 97, 98, 256).
	 * 
	 * @param inputFilename the name of the file containing the plaintext to be encoded.
	 * @throws IOException I'm going to be honest, I don't actually know what an IOException is. I
	 *                     didn't write this code. Sorry. You do get documentation and refactoring
	 *                     on literally everything else, though, so I'd say that's a fair trade.
	 */
	public void encoding(String inputFilename) throws IOException {
		// inputFileReader reads in the file containing the plaintext to be encrypted.
		BufferedReader inputFileReader = new BufferedReader(new FileReader(inputFilename));
		// encodeWriter writes to (and creates, if it does not already exist) the file that will
		// contain
		BufferedWriter encodeWriter = new BufferedWriter(new FileWriter(getEncodedFilename(inputFilename)));
		// we will initialize our dictionary with all the characters in our charset.
		initializeEncodingDictionary(encodingDictionary, CHARSET_SIZE);
		// currentCharacter is the character we are currently running the LZW algorithm on. It is
		// set to q as an arbitrary choice; it must be initialized to something, and chars are not
		// nullable.
		char currentCharacter = 'q';

		while (inputFileReader.ready()) {
			LZWStep(currentCharacter, inputFileReader, encodeWriter, encodingDictionary,
					currentLongestSubstringInDictionary);
		}
		// close writers
		inputFileReader.close();
		encodeWriter.close();

	}

	/**
	 * Performs one step of the LZW algorithm. Gets the next character from the input file, appends
	 * it to the currentLongestSubstringInDictionary, then checks whether the result is in the
	 * encodingDictionary or not. If it is, continue iterating this function. If not, write the code
	 * word corresponding to the previous currentLongestSubstringInDictionary to the
	 * 
	 * @param inputFileReader
	 * @param encodeWriter
	 * @param encodingDictionary
	 * @param currentLongestSubstringInDictionary
	 */
	private void LZWStep(char currentCharacter, BufferedReader inputFileReader, BufferedWriter encodeWriter,
			HashMap<String, Integer> encodingDictionary, StringBuilder currentLongestSubstringInDictionary) {
		try {
			currentCharacter = (char) (inputFileReader.read());
		} catch (Exception e) {

		}
		currentLongestSubstringInDictionary.append(currentCharacter);
		if (encodingDictionary.get(currentLongestSubstringInDictionary.toString()) == null) {
			handleSubstringNotInDictionary(currentCharacter, currentLongestSubstringInDictionary, encodingDictionary,
					decodingDictionary, encodeWriter);
		}
		try {
			if (!inputFileReader.ready()) {
				handleEndCase(currentCharacter, currentLongestSubstringInDictionary, encodeWriter);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Takes in the name of an unencoded .txt file and returns the name of the encoded version.
	 * 
	 * @param filename the name of the unencoded file
	 * @return the name of the encoded file
	 */
	private String getEncodedFilename(String filename) {
		return (filename.substring(0, filename.length()) + ".lzw");
	}

	/**
	 * This function will run through a loop CHARSET_SIZE times in which it adds the key value pair
	 * (c, i) where i is the index of the loop and c is the character corresponding to that index,
	 * as a string.
	 * 
	 * @param encodingDictionary the dictionary to be initialized
	 * @param CHARSET_SIZE       the size of the charset
	 */
	public void initializeEncodingDictionary(HashMap<String, Integer> encodingDictionary, int CHARSET_SIZE) {
		for (int i = 0; i < CHARSET_SIZE; i++) {
			encodingDictionary.put((char) (i) + "", i);
		}
	}

	/**
	 * Adds the key-value pair (currentLongestSubstringInDictionary, encodingDictionary.size()) to
	 * encodingDictionary.
	 * 
	 * @param currentLongestSubstringInDictionary the symbol to be added to the dictionaries
	 * @param encodingDictionary                  the encoding dictionary
	 * @param decodingDictionary                  the decoding dictionary
	 */
	public void addNewSymbolToDictionary(StringBuilder currentLongestSubstringInDictionary,
			HashMap<String, Integer> encodingDictionary, HashMap<Integer, String> decodingDictionary) {
		if (encodingDictionary.size() < MAX_DICTIONARY_SIZE) {
			String symbol = currentLongestSubstringInDictionary.toString();
			encodingDictionary.put(symbol, encodingDictionary.size());
		} else {
			System.out.println("ERROR: Max Dictionary Size Reached");
		}

	}

	/**
	 * Outputs the output to the output file.
	 * 
	 * @param output       the output to be sent to the output file
	 * @param encodeWriter the BufferedWriter that writes to the output file
	 */
	public void outputToCodestream(String output, BufferedWriter encodeWriter) {
		try {
			encodeWriter.write((int) encodingDictionary.get(output) + " ");
		} catch (Exception e) {

		}
	}

	/**
	 * Handles the case when the end of the input is reached. Puts in any characters that have not
	 * been added to the codestream yet, as well as the last character to be read.
	 * 
	 * @param finalCharacter                      the last character to be read
	 * @param currentLongestSubstringInDictionary Substring to be output
	 * @param encodeWriter                        the BufferedWriter that writes to the output file
	 */
	public void handleEndCase(Character finalCharacter, StringBuilder currentLongestSubstringInDictionary,
			BufferedWriter encodeWriter) {
		outputToCodestream(currentLongestSubstringInDictionary.toString().substring(0,
				currentLongestSubstringInDictionary.length()), encodeWriter);
	}
	/*
	 *  ||  ||  
     *  \\()// 
     * //(__)\\
     * ||    ||
	 */
}
