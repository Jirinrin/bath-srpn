import java.util.*;

//Class roughly inspired by 'stack' explanation in lecture 4
public class IntegerStack {

	private int maxSize = 23;
	private List<Integer> stack = new ArrayList<Integer>(); // Even after realising that the class 'Stack' already exists, I chose to keep it as an ArrayList because it still works and this way I can demonstrate my understanding of the inner workings.
	private int randomIndex = 0;
	private RandomCNumbersArchive randomNumber = new RandomCNumbersArchive();

	public IntegerStack() {}

	public IntegerStack(int size) {
			maxSize = size;
	}

	// Makes numbers that are too small/large stay at the maximum/minimum of 32-bits
	private int fit32Bit(double number) {
		if (number > 2147483647) {
			return 2147483647;
		}
		else if (number < -2147483647) {
			return -2147483648;
		}
		else {
			return (int) number;
		}
	}

	// Push a number onto the last slot of the stack
	public void push(double newNumber) {
		if (stack.size() < maxSize) {
			stack.add(fit32Bit(newNumber));
		}
		else {
			System.out.println("Stack overflow.");
			return;
		}
	}

	// Push a random following the given archive onto the last slot of the stack
	public void pushRandom() {
		push(randomNumber.getRandomNumber(randomIndex));
		if (stack.size() < maxSize) {
		randomIndex += 1;
		}
	}

	// Carry out an operation possible in SRPN on two integers, taking care of exceptions in the process
	private java.lang.Double operationAB(int a, int b, String operator) throws MathException { // java.lang.Double thing suggested by Nathan Hughes https://stackoverflow.com/questions/17221759/returning-null-in-a-method-whose-signature-says-return-int
		double aa = a; // Integers are first casted into doubles so that the calculation going 'out of the bounds of 32-bit' can be handled
		double bb = b;
		switch (operator) {
			case "+":
				return aa + bb;
			case "-":
				return aa - bb;
			case "*":
				return aa * bb;
			case "/":
				if (b == 0) {
					throw new MathException("Divide by 0.");
				}
				return aa / bb;
			case "%":
				return aa % bb;
			case "^":
				if (b < 0) {
					throw new MathException("Negative power.");
				}
				return Math.pow(aa, bb);
		}
		return null;
	}

	// Apply submitted operator to the last 2 items on the stack
	public void operation(String operator) {
		if (stack.size() < 2) {
			System.out.println("Stack underflow.");
			return;
		}
		try {
			stack.set(stack.size() - 2, fit32Bit(operationAB(stack.get(stack.size() - 2), stack.get(stack.size() - 1), operator)));
			stack.remove(stack.size() - 1);
		}
		catch (MathException e) {
			return;
		}
	}

	// Return the last number in the stack
	public int peek() {
		return stack.get(stack.size() - 1);
	}

	// Print the last number in the stack (if available)
	public void peekPrint() {
		if (stack.isEmpty()) {
			System.out.println("Stack empty.");
			return;
		}
		System.out.println(peek());
	}

	// Print all the numbers stored in memory in order
	public void printStack() { // 'd' command in calculator
		for (int i = 0; i < stack.size(); ++i) {
			System.out.println(stack.get(i));
		}
	}
}