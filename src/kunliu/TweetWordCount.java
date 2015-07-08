package kunliu;

public class TweetWordCount {

	public static void main(String args[]) {
		if (Utility.validateArgument(args)) {
			Utility.procedureWordCount(args[0], args[1]);
		}
	}

}
