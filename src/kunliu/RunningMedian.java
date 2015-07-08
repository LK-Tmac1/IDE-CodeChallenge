package kunliu;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * An object that encapsulates necessary data structures and operations for
 * calculating running median of a sequence of input values.
 * <p>
 * The idea is to use two priority queues, i.e. a max heap and an min heap to
 * store the elements smaller and larger than the current median value
 * respectively.
 * 
 * @author Kun
 */
public class RunningMedian {

	private float median;
	private static final int CAPACITY = 1000;
	private PriorityQueue<Float> rightMinHeap;
	private PriorityQueue<Float> leftMaxHeap;
	private List<Float> medianList;
	private final boolean saveSpace;

	/**
	 * Default constructor for a RunningMedian object. The saveSpace flag is set
	 * to true.
	 */
	public RunningMedian() {
		this(true);
	}

	/**
	 * The saveSpace is a flag that indicates if the historical running median
	 * should be recorded or not.
	 * 
	 * @param saveSpace
	 *            If true then the list will be null otherwise will be used to
	 *            store historical running median values.
	 */
	public RunningMedian(boolean saveSpace) {
		median = Integer.MIN_VALUE;
		rightMinHeap = new PriorityQueue<Float>(CAPACITY);
		leftMaxHeap = new PriorityQueue<Float>(CAPACITY,
				Collections.reverseOrder());
		medianList = new LinkedList<Float>();
		this.saveSpace = saveSpace;
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
	public void encounterNew(float val) {
		if (rightMinHeap.size() == leftMaxHeap.size()) {
			if (val > median) {
				rightMinHeap.add(val);
				median = rightMinHeap.peek();
			} else {
				leftMaxHeap.add(val);
				median = leftMaxHeap.peek();
			}
		} else {
			if (rightMinHeap.size() > leftMaxHeap.size()) {
				if (val > median) {
					leftMaxHeap.add(rightMinHeap.poll());
					rightMinHeap.add(val);
				} else {
					leftMaxHeap.add(val);
				}
			} else {
				if (val > median) {
					rightMinHeap.add(leftMaxHeap.poll());
					leftMaxHeap.add(val);
				} else {
					rightMinHeap.add(val);
				}
			}
			median = (rightMinHeap.peek() + leftMaxHeap.peek()) / 2;
		}
		if (!saveSpace) {
			medianList.add(median);
		}
	}

	/**
	 * If save space mode is enabled, then will return null object for the list.
	 * 
	 * @return A list that contains all the running median so far.
	 */
	public List<Float> getMedianList() {
		return saveSpace ? null : this.medianList;
	}

	public static void main(String args[]) {
		args = new String[2];
		args[0] = "/Users/Kun/Git/IDE-CodeChallenge/tweet_input/tweets.txt";
		args[1] = "/Users/Kun/Git/IDE-CodeChallenge/tweet_output/result3.txt";
		if (Utility.validateArgument(args)) {
			Utility.procedureRunMed(args[0], args[1]);
		}
	}
}