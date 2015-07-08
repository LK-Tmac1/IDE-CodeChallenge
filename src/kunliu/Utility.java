package kunliu;

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

	/**
	 * @param tweet
	 * @return an array of words in the tweet delimited by TWEET_DELIMITER
	 */
	public static String[] splitTweet(String tweet) {
		if (tweet != null) {
			return tweet.split(IOManager.TWEET_DELIMITER);
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
