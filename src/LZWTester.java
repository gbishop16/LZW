
/*
 * Praise be to Ms. Kaufman and Computer Science A teachers.
 * They spoke the truth when they spoke of handwritten code and BlueJ. 
 */
import java.io.*;
import java.util.*;

public class LZWTester {
	public static void Main(String[] args) throws IOException {
//		Encode testEncoder = new Encode();
//		testEncoder.encoding("file1.txt");
		Decode testDecoder = new Decode();
		testDecoder.decode("file1.txt.lzw");
	}
	/*
	 * _..----.._
    _-'_..----.._'-_
  .'.  \       ( `'.'.
 / / `\ `\       )  \ \
| |   _`\ `\____(    | |
| |  [__]_\ `\__()   | |
| |        `\ `\     | |
 \ \         `\ `\  / /
  '.'-._       `\ `'.'
    `-._`'----'`_.-'
        `"----"`
	 */
}
