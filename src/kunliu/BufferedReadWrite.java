package kunliu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedReadWrite {
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static final String TWEET_DELIMITER = "[;,:\\s]";
	public static final String WC_DELIMITER = "\t";

	private BufferedReader bfr;
	private BufferedWriter bfw;

	public BufferedReadWrite() {
		bfr = null;
		bfw = null;
	}

	public void openBufferedReader(String inputPath) {
		try {
			bfr = new BufferedReader(new FileReader(inputPath));
		} catch (FileNotFoundException e) {
			System.err.println("File " + inputPath + " does not exist");
			System.exit(-1);
		}
	}

	public String readNextLine() {
		String line = null;
		try {
			if (bfr != null) {
				line = bfr.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

	public void closeBufferedReader() {
		if (bfr != null)
			try {
				bfr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public void openBufferedWriter(String outputPath) {
		try {
			bfw = new BufferedWriter(new FileWriter(outputPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the output string to the given output path, assuming the output
	 * path is valid. Otherwise, tries to create the file in that path first and
	 * write again.
	 * 
	 * @param outputPath
	 * @param output
	 * @return true if succeed to write output to the target path.
	 */
	public void writeNextLine(String output) {
		if (bfw != null && output != null) {
			try {
				bfw.write(output);
				bfw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeBufferedWriter() {
		try {
			if (bfw != null) {
				bfw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
