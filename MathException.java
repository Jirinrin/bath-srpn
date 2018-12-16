public class MathException extends Exception {

	private String message;

	public MathException() {}

	public MathException(String message) {
		super(message);
		System.err.println(message);
	}
}