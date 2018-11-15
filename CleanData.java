import java.io.*;
import java.util.*;

public class CleanData {
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File("text.txt");
		Scanner sc = new Scanner(file);
		
		List<String> lines = new ArrayList<String>();
		
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			line = cleanUp(line);
			lines.add(line);
		}
		
		writeLine(lines);
	}
	
	private static String cleanUp(String line) {
		line = line.replaceAll("\\\\n", " ").replaceAll("\\\\u", " ").replaceAll("[^a-zA-Z0-9 ]", "");
		return line;
	}
	
	private static void writeLine(List<String> lines) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter("clean.txt", "UTF-8");
		
		for (String line: lines)
			writer.println(line);
		
		writer.close();
	}
}
