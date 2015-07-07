package kunliu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Utility {

	public static Map<String, Integer> wordCountMap = new HashMap<String, Integer>();

	public static final String TWEET_DELIMITER = " ";

	public static String[] splitTweet(String tweet) {
		if (tweet != null) {
			return tweet.split(TWEET_DELIMITER);
		}
		return null;
	}

	public static void parseWordCountInput(String inputPath) {
		if (inputPath != null) {
			File file = new File(inputPath);
			Scanner input = null;
			try {
				input = new Scanner(file);
				while (input.hasNext()) {
					for (String word : splitTweet(input.next())) {
						if (wordCountMap.containsKey(word)) {
							int val = wordCountMap.get(word);
							wordCountMap.put(word, val + 1);
						} else {
							wordCountMap.put(word, 0);
							System.out.println(word);
						}
					}
				}
				System.out.println("-----" + Math.abs(wordCountMap.size())
						/ 1000);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				input.close();
			}
		}
	}
	
	public static void parseRunningMedianInput(String inputPath){
		
	}

	public static int getUniqueWordCount(String tweet) {
		Set<String> wordSet = new HashSet<String>();
		for (String word : splitTweet(tweet)) {
			if (!wordSet.contains(word)) {
				wordSet.add(word);
			}
		}
		return wordSet.size();
	}

	public static String formatFloatString(float median) {
		// If 2.0, return 2, else if 2.1, return 2.1
		if ((int) (median * 10) / 10 != median) {
			return "" + median;
		} else {
			return "" + (int) (median * 10) / 10;
		}
	}

}
