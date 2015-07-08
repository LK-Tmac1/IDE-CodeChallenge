package kunliu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A general purpose helper class.
 * 
 * @author Kun
 *
 */
public class Utility {

	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static final String TWEET_DELIMITER = " ";
	public static final String WC_DELIMITER = "\t";

	/**
	 * Validate the arguments number.
	 * 
	 * @param args
	 * @return true if two arguments.
	 */
	public static boolean validateArgument(String[] args) {
		if (args != null && args.length == 2 && !args[0].isEmpty()
				&& !args[1].isEmpty()) {
			return true;
		}
		System.err.println("Invalid argument, input and output path expected.");
		return false;
	}

	/**
	 * @param tweet
	 * @return an array of words in the tweet delimited by TWEET_DELIMITER
	 */
	public static String[] splitTweet(String tweet) {
		if (tweet != null) {
			return tweet.split(TWEET_DELIMITER);
		}
		return null;
	}

	public static BufferedReader openBufferedReader(String inputPath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputPath));
		} catch (FileNotFoundException e) {
			System.err.println("File " + inputPath + " does not exist");
			System.exit(-1);
		}
		return br;
	}

	public static void closeBufferedReader(BufferedReader br) {
		if (br != null)
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static void writeTempFile(List<String> tempFileList,
			String parentDir, String output) {
		String tempFileName = "temp_" + System.currentTimeMillis();
		if (writeOutputFile(parentDir + tempFileName, output)) {
			tempFileList.add(tempFileName);
		}
	}

	/**
	 * Write the output string to the given output path, assuming the output
	 * path is valid. Otherwise, tries to create the file in that path first and
	 * write again.
	 * 
	 * @param outputPath
	 * @param output
	 * @return true if succeed to write output to the target path.
	 */
	public static boolean writeOutputFile(String outputPath, String output) {
		if (output != null) {
			BufferedWriter bfw = null;
			try {
				bfw = new BufferedWriter(new FileWriter(outputPath));
				bfw.write(output);
				bfw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					bfw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * Given a file path, return its parent directory.
	 * 
	 * @param filePath
	 * @return parent directory of the file, if existed.
	 */
	public static String parentDirPath(String filePath) {
		if (filePath != null && !filePath.trim().isEmpty()) {
			int index = filePath.lastIndexOf(File.separator);
			return index > -1 ? filePath.substring(0, index) : null;
		}
		return null;
	}

	/**
	 * Use a hash set to calculate the total number of unique words in a line of
	 * tweet.
	 * 
	 * @param tweet
	 * @return the size of the hash set
	 */
	public static int uniqueWordNumber(String tweet) {
		Set<String> wordSet = new HashSet<String>();
		for (String word : splitTweet(tweet)) {
			if (!wordSet.contains(word)) {
				wordSet.add(word);
			}
		}
		return wordSet.size();
	}

	/**
	 * Format a float value to string, e.g. if 2.0, return 2; if 2.1, return 2.1
	 * 
	 * @param a
	 * @return
	 */
	public static String formatFloatString(float a) {
		return a == (int) (a) ? String.format("%d", (int) a) : String.format(
				"%s", a);
	}

}
