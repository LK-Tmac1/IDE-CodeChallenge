package kunliu;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;

/*
 * Scan the whole file is very fast using BufferedReader, 1.77GB used 5 seconds.
 * But if the naive procedure is used, very slow, costed rouggly 4~ mins.
 * If more complicated regex is used, say [.;:\\s], the matching will be much slower
 */
public class TweetWordCount {

	public static final int MAX_WORD = 1000;
	public static final int MAX_HASHFILE = 100;

	private TreeMap<String, Integer> wcMap;

	public TweetWordCount() {
		this.wcMap = new TreeMap<String, Integer>();
	}

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

	public static boolean procedureWordCountNaive(String input, String output) {
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
		return true;
	}

	public static boolean procedureWordCountDist(String input, String output) {
		String tempSplitDir = splitInputToHashFiles(input);
		String tempSortGroupDir = sortGroupWordDirectory(tempSplitDir);
		if (mergeWordCount(tempSortGroupDir, output)) {
			IOManager.deleteDirectory(new File(tempSplitDir).getParent());
			return true;
		}
		return false;
	}

	private static boolean mergeWordCount(String mergePath, String output) {
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
				for (String word : Utility.splitTweet(line)) {
					int hashFile = Math.abs(word.hashCode()) % MAX_HASHFILE;
					if (bfwArray[hashFile] == null) {
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
		args[1] = parDir + "tweet_output/ft1.txt";
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar
				.getInstance().getTime()));
		if (Utility.validateArgument(args)
				&& procedureWordCountDist(args[0], args[1])) {
			System.out.println("Word count calaulated successfully.");
		}
		System.out.println(new SimpleDateFormat("HH:mm:ss").format(Calendar
				.getInstance().getTime()));
	}

}
