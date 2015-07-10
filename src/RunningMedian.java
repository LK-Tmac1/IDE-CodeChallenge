import java.util.Collections;
import java.util.PriorityQueue;

/**
 * An object that encapsulates necessary data structures and operations for
 * calculating running median of a sequence of input values.
 * <p>
 * The idea is to use two priority queues, i.e. a max heap and an min heap to
 * store the elements smaller and larger than the current median value
 * respectively. Whenever a new value is inserted, update the two heaps based on
 * certain rules.
 * 
 * @author Kun
 */
public class RunningMedian {

	private float median;
	private static final int QUEUE_CAPACITY = 1000;
	private PriorityQueue<Float> rightMinHeap;
	private PriorityQueue<Float> leftMaxHeap;

	public RunningMedian() {
		median = Integer.MIN_VALUE;
		rightMinHeap = new PriorityQueue<Float>(QUEUE_CAPACITY);
		leftMaxHeap = new PriorityQueue<Float>(QUEUE_CAPACITY,
				Collections.reverseOrder());
	}

	/**
	 * @return The current median.
	 */
	public float getCurrentMedian() {
		return this.median;
	}

	/**
	 * Use one max heap for the left side of the median, and one min heap for
	 * the right side of the median.
	 * <p>
	 * When a new value is encountered, update the two heaps by corresponding
	 * rules: <br>
	 * 1. Ensure the difference in size of the two heaps is no larger than 1. <br>
	 * 2. Insert the new value to the correct side, i.e. if val > median then
	 * the right side otherwise the left side. <br>
	 * 3. Calculate the new median based on if there is odd or even number of
	 * elements in total in the two heaps.
	 * <p>
	 * Time complexity O(logn) because of heap operations, and space complexity
	 * O(n), assuming that there are n elements in the two heaps in total.
	 * 
	 * @param val
	 *            The new value encountered.
	 */
	public float encounterNew(float val) {
		if (rightMinHeap.size() == leftMaxHeap.size()) {
			if (val > median) {
				rightMinHeap.add(val);
				median = rightMinHeap.peek();
			} else {
				leftMaxHeap.add(val);
				median = leftMaxHeap.peek();
			}
		} else {
			if (val > median) {
				if (leftMaxHeap.size() < rightMinHeap.size()) {
					leftMaxHeap.add(rightMinHeap.poll());
				}
				rightMinHeap.add(val);
			} else {
				if (leftMaxHeap.size() > rightMinHeap.size()) {
					rightMinHeap.add(leftMaxHeap.poll());
				}
				leftMaxHeap.add(val);
			}
			median = (rightMinHeap.peek() + leftMaxHeap.peek()) / 2;
		}
		return this.median;
	}

	/**
	 * This procedure will read the input data line by line and then calculate
	 * the running median by using the RunningMedian class, and finally save the
	 * calculation result into output path.
	 * <p>
	 * Note that if a line is empty, i.e. contains no content except whitespace,
	 * it will not be counted.
	 * 
	 * @param inputPath
	 * @param outputPath
	 */
	public static void procedureRunMed(String inputPath, String outputPath) {
		IOManager bfrw = new IOManager();
		StringBuilder sb = new StringBuilder();
		RunningMedian rm = new RunningMedian();
		bfrw.openBufferedReader(inputPath);
		String line;
		while ((line = bfrw.readNextLine()) != null) {
			if (!line.trim().isEmpty()) {
				float count = Utility.uniqueWordNumber(line);
				sb.append(Utility.formatFloatString(rm.encounterNew(count)));
				sb.append(IOManager.LINE_SEPARATOR);
			}
		}
		bfrw.closeBufferedReader();
		bfrw.openBufferedWriter(outputPath);
		bfrw.writeOutput(sb.toString());
		bfrw.closeBufferedWriter();
	}

	public static void main(String args[]) {
		if (Utility.validateArgument(args)) {
			procedureRunMed(args[0], args[1]);
		}
	}
}
