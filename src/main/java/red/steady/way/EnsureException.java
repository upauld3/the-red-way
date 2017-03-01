package red.steady.way;

@SuppressWarnings("serial")
public class EnsureException extends IllegalArgumentException {
	public EnsureException(String message) {
		super(message);
	}
}