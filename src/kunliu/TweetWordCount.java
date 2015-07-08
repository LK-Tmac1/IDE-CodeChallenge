package kunliu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

/*
 * Scan the whole file is very fast using BufferedReader, 1.77GB used 5 seconds.
 * But if the naive procedure is used, very slow, costed rouggly 4~ mins.
 * If more complicated regex is used, say [.;:\\s], the matching will be much slower
 */
public class TweetWordCount {

	public static final int MAX_HASHFILE = 10;

	private TreeMap<String, Integer> wordCountMap;

	public TweetWordCount() {
		this.wordCountMap = new TreeMap<String, Integer>();
	}

	public String dumpWordCountMap() {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, Integer>> iter = wordCountMap.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Entry<String, Integer> entry = iter.next();
			sb.append(entry.getKey() + IOManager.WC_DELIMITER
					+ entry.getValue() + IOManager.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	public void clearMap() {
		this.wordCountMap.clear();
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

	public static boolean procedureWordCountNaive(String inputPath,
			String outputPath) {
		IOManager bfrw = new IOManager();
		bfrw.openBufferedReader(inputPath);
		TweetWordCount wc = new TweetWordCount();
		String line;
		while ((line = bfrw.readNextLine()) != null) {
			if (!line.trim().isEmpty()) {
				String[] wordArray = Utility.splitTweet(line);
				for (String word : wordArray) {
					wc.updateWordCountByOne(word);
				}
			}
		}
		bfrw.closeBufferedReader();
		bfrw.openBufferedWriter(outputPath);
		bfrw.writeOutput(wc.dumpWordCountMap());
		bfrw.closeBufferedWriter();
		return true;
	}

	public static boolean procedureWordCount(String inputPath, String outputPath) {
		String tempSplitDir = splitInputToHashFiles(inputPath);
		String tempSortGroupDir = sortGroupWordDirectory(tempSplitDir);
		IOManager.deleteDirectory(new File(tempSplitDir).getParent());
		return true;
	}

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
				String[] wordArray = Utility.splitTweet(line);
				for (String word : wordArray) {
					int hashFile = Math.abs(word.hashCode()) % MAX_HASHFILE;
					if (bfwArray[hashFile] == null
							|| !bfwArray[hashFile].isWriterOpen()) {
						bfwArray[hashFile] = new IOManager();
						bfwArray[hashFile].openBufferedWriter(tempDir
								+ hashFile + ".txt");
					}
					bfwArray[hashFile].writeOutput(word
							+ IOManager.LINE_SEPARATOR);
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
		args = new String[2];
		String parDir = "/Users/Kun/Git/IDE-CodeChallenge/";
		args[0] = parDir + "tweet_input/tweets.txt";
		args[1] = parDir + "tweet_output/2.txt";
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar
				.getInstance().getTime()));
		if (Utility.validateArgument(args)
				&& procedureWordCount(args[0], args[1])) {
			System.out.println("Word count calaulated successfully.");
		}
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar
				.getInstance().getTime()));
	}

}
