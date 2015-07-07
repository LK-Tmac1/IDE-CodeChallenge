package kunliu;

import java.util.Comparator;
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

	class DescendCmptor implements Comparator<Float> {
		public int compare(Float x, Float y) {
			return (int) (y - x);
		}
	}

	public RunningMedian() {
		median = Integer.MIN_VALUE;
		rightMinHeap = new PriorityQueue<Float>(CAPACITY);
		leftMaxHeap = new PriorityQueue<Float>(CAPACITY, new DescendCmptor());
		medianList = new LinkedList<Float>();
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
		medianList.add(median);
	}

	/**
	 * @return A list that contains all the running median so far.
	 */
	public List<Float> getMedianList() {
		return this.medianList;
	}

	public static void main(String args[]) {
		RunningMedian rm = new RunningMedian();
		for (int i = 0; i < 101; i++) {
			rm.encounterNew(i);
			System.out.println("Insert " + i);
			System.out.println("Current Median: " + rm.getCurrentMedian());
			System.out.println("==============");
		}
	}
}
