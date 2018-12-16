/* SRPN RECODE */

import java.io.*;

public class SRPN {
	
	IntegerStack numberStack = new IntegerStack();

	// Used for telling which Strings to Parse to a number
	public boolean isNumeric(String str) { // Based on CraigTP's method https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
		try {
			double d = Double.parseDouble(str);
		}
		catch(NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public boolean isOctal(String str) {
		if (str.charAt(0) == '0') {
			return true;
		}
		return false;
	}

	public double parseDecimalToOctal (String s) {
		int total = 0;
		int startIndex = s.length();
		int endIndex = 0;
		if (s.charAt(0) == '-') {
			endIndex = 1;
		}
		if (s.contains("8") || s.contains("9")) {
			startIndex = s.indexOf('8');
			if (((s.indexOf('9') < s.indexOf('8')) && s.indexOf('9') != -1) || (s.indexOf('8') == -1)) {
				startIndex = s.indexOf('9');
			}	
		}
		for (int i = startIndex; i > endIndex; --i) {
			total += (double) (s.charAt(i - 1) - '0') * Math.pow(8, (startIndex - i));
		}
		if (endIndex == 1) {
			total *= -1;
		}
		return total;
	}

	// Check which type of character(/number) is inputted and process it accordingly
	public void processCommand(String s) {
		s = s.split("#")[0].trim();
		if (s.isEmpty()) {
			return;
		}
		else if (isNumeric(s)) { /// 
			if (isOctal(s.replace("-", "")) && s.charAt(0) == '-') {
				numberStack.push(parseDecimalToOctal(s));
			}
			else if (isOctal(s)) {
				numberStack.push(parseDecimalToOctal(s));
			}
			else {
			numberStack.push(Double.parseDouble(s));
			}
		}
		else if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("%") || s.equals("^")) {
			numberStack.operation(s);
		}
		else if (s.equals("=")) {
			numberStack.peekPrint();
		}
		else if (s.equals("d")) {
			numberStack.printStack();
		}
		else if (s.equals("r")) {
			numberStack.pushRandom();
		}
		else if (s.length() > 1) {
			complexCommand(s);
		}
		else {
			System.out.println("Unrecognised operator or operand \"" + s + "\".");
		}
	}

	// Split a command consisting of multiple commands into its parts and run all of those through processCommand() again individually
	public void complexCommand(String s) {
		String stringParts[] = s.split("((?<=\\D)|(?=\\D))"); // Split method by NawaMan https://stackoverflow.com/questions/2206378/how-to-split-a-string-but-also-keep-the-delimiters 
		for (int i = 0; i < stringParts.length; ++i) {
			processCommand(stringParts[i]);
		}
	}

	public static void main(String[] args) {
		SRPN sprn = new SRPN();

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			// Keep on accepting input from the command-line
			while (true) {
				String command = reader.readLine();
				
				// Close on an End-of-file (EOF) (Ctrl-D on the terminal)
				if (command == null) {
					// Exit code 0 for a graceful exit
					System.exit(0);
				}
				
				// Otherwise, (attempt to) process the character
				sprn.processCommand(command);
			}
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
}