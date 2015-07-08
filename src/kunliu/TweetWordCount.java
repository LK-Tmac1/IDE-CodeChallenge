package kunliu;

import java.util.Iterator;
import java.util.TreeMap;

public class TweetWordCount {

	/**
	 * When the max word count number is reached, dump the word count map into a
	 * file.
	 */
	public static final int MAX_WORDCOUNT = 1000;

	private TreeMap<String, Integer> wordCountMap;

	public TweetWordCount() {
		this.wordCountMap = new TreeMap<String, Integer>();
	}

	public TreeMap<String, Integer> getWordCountMap() {
		return this.wordCountMap;
	}

	public void addWordCount(String word, int count) {
		if (word != null) {
			this.wordCountMap.put(word, count);
		}
	}

	public void updateWordCountByOne(String word) {
		int c = wordCountMap.containsKey(word) ? wordCountMap.get(word) + 1 : 1;
		wordCountMap.put(word, c);
	}

	public boolean isMapFull() {
		return this.wordCountMap.size() < MAX_WORDCOUNT;
	}

	public static void main(String args[]) {
		if (Utility.validateArgument(args)) {
			// Utility.procedureWordCount(args[0], args[1]);
		}
		TreeMap<String, Integer> wordCountMap = new TreeMap<String, Integer>();
		wordCountMap.put("abdd", 1);
		wordCountMap.put("abd", 2);
		Iterator<String> iter = wordCountMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key);
			System.out.println(wordCountMap.get(key));
		}
	}

}
