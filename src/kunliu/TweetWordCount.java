package kunliu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class TweetWordCount {

	/**
	 * When the max word count number is reached, dump the word count map into a
	 * file.
	 */
	public static final int MAX_WORDCOUNT = 1000;

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
			sb.append(key + Utility.WC_DELIMITER + wordCountMap.get(key)
					+ Utility.LINE_SEPARATOR);
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
		return this.wordCountMap.size() < MAX_WORDCOUNT;
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
		BufferedReader br = Utility.openBufferedReader(inputPath);
		String line;
		TweetWordCount twc = new TweetWordCount();
		try {
			while ((line = br.readLine()) != null) {
				if (!line.trim().isEmpty()) {
					if (twc.isMapFull()) {

					} else {

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Utility.writeOutputFile(outputPath, "");
	}

	public static void main(String args[]) {
		if (Utility.validateArgument(args)) {
			// Utility.procedureWordCount(args[0], args[1]);
		}
	}

}
