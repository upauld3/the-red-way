package red.steady.way;

@SuppressWarnings("serial")
public class InvariantCheckException extends RuntimeException {
	public InvariantCheckException(String message) {
		super(message);
	}
}