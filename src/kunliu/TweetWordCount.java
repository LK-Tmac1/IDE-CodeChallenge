package kunliu;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class TweetWordCount {

	public static final int MAX_HASHFILE = 1000;

	private TreeMap<String, Integer> wordCountMap;
	private List<String> tempFileList;

	public TweetWordCount() {
		this.wordCountMap = new TreeMap<String, Integer>();
		this.tempFileList = new LinkedList<String>();
	}

	public String dumpWordCountMap() {
		StringBuilder sb = new StringBuilder();
		Iterator<String> iter = wordCountMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			sb.append(key + BufferedReadWrite.WC_DELIMITER
					+ wordCountMap.get(key) + BufferedReadWrite.LINE_SEPARATOR);
		}
		return sb.toString();
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
		return this.wordCountMap.size() < MAX_HASHFILE;
	}

	/**
	 * Convert the linked list of temporary file names to an array, in that
	 * array is faster than linked list in reading values by random access.
	 * 
	 * @return an string array that contains all the temp file names.
	 */
	public String[] getTempFileArray() {
		return tempFileList.toArray(new String[tempFileList.size()]);
	}

	public static boolean procedureWordCount(String inputPath, String outputPath) {

		return true;
	}

	public static void main(String args[]) {
		if (Utility.validateArgument(args)) {
			// Utility.procedureWordCount(args[0], args[1]);
		}
	}

}
