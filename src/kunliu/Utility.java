package kunliu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * A general purpose helper class.
 * 
 * @author Kun
 *
 */
public class Utility {

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

	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static final String TWEET_DELIMITER = " ";

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

	public static BufferedReader getBufferedReader(String inputPath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputPath));
		} catch (FileNotFoundException e) {
			System.err.println("File " + inputPath + " does not exit");
			e.printStackTrace();
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

	public static boolean procedureWordCount(String inputPath, String outputPath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputPath));
			for (String line = br.readLine(); line != null; line = br
					.readLine()) {
			}
		} catch (FileNotFoundException e) {
			System.err.println("File " + inputPath + " does not exit");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return writeOutputFile(outputPath, "");
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
			} catch (FileNotFoundException e) {
				int index = outputPath.lastIndexOf(File.separator);
				if (index > -1) {
					final File parent = new File(outputPath.substring(0, index));
					final String fileName = outputPath.substring(index + 1,
							outputPath.length());
					if (!parent.mkdirs()) {
						System.err.println("Could not create parent directory");
					}
					try {
						new File(parent, fileName).createNewFile();
						return writeOutputFile(outputPath, output);
					} catch (IOException e1) {
						System.err.println("Could not create " + outputPath);
					}
				} else {
					e.printStackTrace();
				}
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
