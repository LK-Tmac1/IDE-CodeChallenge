import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * An object that encapsulates data structures and methods for tweet word count.
 * <p>
 * There are two procedures, one is for small input data that can be fit in
 * memory directly, and the other one is a Map-Reduce like procedure.
 * 
 * @author Kun
 *
 */
public class TweetWordCount {

	/**
	 * The threshold for intermediate hashing files number.
	 */
	private static final int MAX_HASHFILE = 100;

	/**
	 * Used to sort and word-count pair based on the word key.
	 */
	private TreeMap<String, Integer> wcMap;

	public TweetWordCount() {
		this.wcMap = new TreeMap<String, Integer>();
	}

	/**
	 * Convert the sorted word-count pairs in the tree map to a string object.
	 * 
	 * @return string format of word-count pairs
	 */
	public String dumpWordCountMap() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, Integer>> iter = wcMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			sb.append(entry.getKey() + IOManager.WC_DELIMITER
					+ entry.getValue() + IOManager.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	public void clearMap() {
		this.wcMap.clear();
	}

	public void updateWordCount(String word, int count) {
		if (word != null) {
			int c = wcMap.containsKey(word) ? wcMap.get(word) + count : count;
			wcMap.put(word, c);
		}
	}

	public void updateWordCountByOne(String word) {
		updateWordCount(word, 1);
	}

	/**
	 * A naive procedure for processing small amount of data that can be fit in
	 * memory.
	 * <p>
	 * The idea is straightforward, i.e. use a hash map to store and update the
	 * word-count pair.
	 */
	public static void procedureWordCountNaive(String input, String output) {
		IOManager bfrw = new IOManager();
		bfrw.openBufferedReader(input);
		TweetWordCount wc = new TweetWordCount();
		String line;
		while ((line = bfrw.readNextLine()) != null) {
			if (!line.trim().isEmpty()) {
				for (String word : Utility.splitTweet(line)) {
					wc.updateWordCountByOne(word);
				}
			}
		}
		bfrw.closeBufferedReader();
		bfrw.openBufferedWriter(output);
		bfrw.writeOutput(wc.dumpWordCountMap());
		bfrw.closeBufferedWriter();
	}

	/**
	 * A procedure that split input data that cannot be fit to memory to
	 * partitions, each of which contains the keywords with the same hash code
	 * value, and then sort and group every word in each of those files, and
	 * finally merge those files into a single one, i.e. the output, and delete
	 * all temporary intermediate files created.
	 */
	public static void procedureWordCountDist(String input, String output) {
		String tempSplitDir = splitInputToHashFiles(input);
		String tempSortGroupDir = sortGroupWordDirectory(tempSplitDir);
		mergeWordCount(tempSortGroupDir, output);
		IOManager.deleteDirectory(new File(tempSplitDir).getParent());
	}

	/**
	 * A procedure that merge all the files in the merge path into the output
	 * file.
	 * <p>
	 * The assumption is that all the word-count values in every file are sorted
	 * already. The idea is to use a priority queue, with each word-count pair
	 * as the priority key, (no two word-count pair will be same), and each time
	 * extract the first value from the queue, write it to the buffered writer,
	 * update the corresponding reader that reading word-count pair by a map of
	 * word-count to file index, until all word-pair are processed.
	 * <p>
	 * If there are k files, this procedure will take extra space in O(k) level,
	 * as the priority queue will have k elements at most, there will be an
	 * array of k buffered reader, and a map of k line-file-index pair.
	 * <p>
	 * The time complexity is, assuming there are w distinct words, in O(wlogk)
	 * level, because of the priority queue property.
	 */
	private static void mergeWordCount(String mergePath, String output) {
		File mergeDir = new File(mergePath);
		if (mergeDir.isDirectory()) {
			File[] allFile = mergeDir.listFiles();
			IOManager[] readerArray = new IOManager[allFile.length];
			PriorityQueue<String> mergeQ = new PriorityQueue<String>(
					allFile.length);
			Map<String, Integer> lineFileMap = new HashMap<String, Integer>();
			for (int i = 0; i < allFile.length; i++) {
				readerArray[i] = new IOManager();
				readerArray[i].openBufferedReader(allFile[i].getAbsolutePath());
				String line = readerArray[i].readNextLine();
				lineFileMap.put(line, i);
				mergeQ.add(line);
			}
			IOManager writer = new IOManager();
			writer.openBufferedWriter(output);
			while (mergeQ.size() > 0) {
				String line = mergeQ.remove();
				writer.writeOutput(line + IOManager.LINE_SEPARATOR);
				int fileIndex = lineFileMap.get(line);
				lineFileMap.remove(line);
				String newline = readerArray[fileIndex].readNextLine();
				if (newline != null) {
					mergeQ.add(newline);
					lineFileMap.put(newline, fileIndex);
				} else {
					readerArray[fileIndex].closeBufferedReader();
				}
			}
			writer.closeBufferedWriter();
		}
	}

	/**
	 * A function that takes all the files in the input directory path, and for
	 * each file, sort and group the word and calculate the word count number,
	 * by using a tree map data structure.
	 * 
	 * @param dirPath
	 * @return the directory that contains the sorted and grouped files
	 */
	private static String sortGroupWordDirectory(String dirPath) {
		File path = new File(dirPath);
		if (path.exists()) {
			if (path.isDirectory()) {
				String tempDir = path.getParent() + File.separator
						+ "sortgroup" + File.separator;
				IOManager ioM = new IOManager();
				TweetWordCount wc = new TweetWordCount();
				for (File file : path.listFiles()) {
					ioM.openBufferedReader(file.getAbsolutePath());
					String line;
					while ((line = ioM.readNextLine()) != null) {
						wc.updateWordCountByOne(line.trim());
					}
					ioM.closeBufferedReader();
					ioM.openBufferedWriter(tempDir + file.getName());
					ioM.writeOutput(wc.dumpWordCountMap());
					ioM.closeBufferedWriter();
					wc.clearMap();
				}
				return tempDir;
			}
		}
		return null;
	}

	/**
	 * A function receives the input path, and split the input file into
	 * multiple files based on the following rules:
	 * <p>
	 * 1. All words that have the same hashing value (after offset), will be
	 * stored in the same file.<br>
	 * 2. The order of words does not matter as may be sorted and grouped later.
	 * 
	 * @param inputPath
	 * @return the directory that contains the split files
	 */
	private static String splitInputToHashFiles(String inputPath) {
		IOManager ioM = new IOManager();
		ioM.openBufferedReader(inputPath);
		String tempDir = new File(inputPath).getParent() + File.separator
				+ "temp" + System.currentTimeMillis() + File.separator
				+ "split" + File.separator;
		String line;
		IOManager[] bfwArray = new IOManager[MAX_HASHFILE];
		while ((line = ioM.readNextLine()) != null) {
			if (!line.trim().isEmpty()) {
				for (String word : Utility.splitTweet(line)) {
					int h = Math.abs(word.hashCode()) % MAX_HASHFILE;
					if (bfwArray[h] == null) {
						bfwArray[h] = new IOManager();
						bfwArray[h].openBufferedWriter(tempDir + h + ".txt");
					}
					bfwArray[h].writeOutput(word + IOManager.LINE_SEPARATOR);
				}
			}
		}
		for (IOManager bfw : bfwArray) {
			if (bfw != null) {
				bfw.closeBufferedWriter();
			}
		}
		return tempDir;
	}

	public static void main(String args[]) {
		if (Utility.validateArgument(args)) {
			if (args.length < 3 || !args[2].equalsIgnoreCase("-d")) {
				procedureWordCountDist(args[0], args[1]);
			} else {
				procedureWordCountNaive(args[0], args[1]);
			}
		}
	}
}
