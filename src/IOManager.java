import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * An object that encapsulates necessary data structures and methods for I/O
 * operations. Each instance will have a buffered reader and a buffered writer,
 * for efficiency purposes.
 * 
 * @author Kun
 *
 */
public class IOManager {

	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");
	public static final String TWEET_DELIMITER = " ";
	public static final String WC_DELIMITER = " ";

	private BufferedReader bfr;
	private BufferedWriter bfw;
	private boolean isWriterOpen;

	public IOManager() {
		bfr = null;
		bfw = null;
		isWriterOpen = false;
	}

	/**
	 * Open the buffered reader based on the input path. If no such file found
	 * in the path, exit the program and print error messages.
	 * 
	 * @param inputPath
	 */
	public void openBufferedReader(String inputPath) {
		try {
			closeBufferedReader();
			bfr = new BufferedReader(new FileReader(inputPath));
		} catch (FileNotFoundException e) {
			System.err.println("File " + inputPath + " does not exist");
			System.exit(-1);
		}
	}

	/**
	 * If the buffered reader variable is opened, read the next line from the
	 * reader.
	 * 
	 * @return the next line in the file being read
	 */
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
		if (bfr != null) {
			try {
				bfr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Tries to open a buffered writer to a file. If such file or its parent
	 * directory does not exist, create the file first and then write to it.
	 * 
	 * @param outputPath
	 */
	public void openBufferedWriter(String outputPath) {
		try {
			closeBufferedWriter();
			bfw = new BufferedWriter(new FileWriter(outputPath));
			isWriterOpen = true;
		} catch (FileNotFoundException e) {
			String parentDir = new File(outputPath).getParent();
			final File parent = new File(parentDir);
			if (!parent.mkdirs()) {
				System.err.println("File " + outputPath + " does not exist");
				System.err.println("Could not create parent directory");
				System.exit(-1);
			} else {
				this.openBufferedWriter(outputPath);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When the buffered writer is open, write output to the buffer.
	 * 
	 * @param output
	 */
	public void writeOutput(String output) {
		if (isWriterOpen && output != null) {
			try {
				bfw.write(output);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeBufferedWriter() {
		try {
			if (this.isWriterOpen) {
				bfw.close();
				isWriterOpen = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recursively delete all the files and directories in a directory, and also
	 * the directory itself.
	 * 
	 * @param fileDir
	 */
	public static void deleteDirectory(String fileDir) {
		if (fileDir != null && !fileDir.trim().isEmpty()) {
			File dir = new File(fileDir);
			if (dir.exists() && dir.isDirectory()) {
				for (File file : dir.listFiles()) {
					deleteDirectory(file.getAbsolutePath());
				}
			}
			dir.delete();
		}
	}
}
