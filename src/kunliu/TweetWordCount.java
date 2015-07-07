package kunliu;

public class TweetWordCount {

	private String word;
	private int count;

	public TweetWordCount(String w, int c) {
		this.setWord(w);
		this.setCount(c);
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
