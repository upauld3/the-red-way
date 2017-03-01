package red.steady.way;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ContractUtils {
	// ////////////////////////
	// Contract Programming //
	// ////////////////////////

	public static enum CheckType {
		Ensure,
		Require;
	};

	// Invariants

	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		return ret;
	}

	public static List<Object> createInvariants(Object... startingValues) {
		List<Object> resultsList = new ArrayList<Object>();

		for (Object startingObject : startingValues) {
			if (isWrapperType(startingObject.getClass()) == true) {
				resultsList.add(startingObject.hashCode());
			} else if (startingObject instanceof String) {
				resultsList.add(startingObject);
			} else {
				throw new RuntimeException("Invariants do not support type, " + startingObject.getClass().getName());
			}
		}

		return resultsList;
	}

	public static void checkInvariants(List<Object> invariants, Object... endingValues) {
		String[] valueNames = new String[invariants.size()];

		for (int index = 0; index < valueNames.length; index++) {
			valueNames[index] = "Invariant-" + index;
		}

		checkInvariants(invariants, valueNames, endingValues);
	}

	public static void checkInvariants(List<Object> invariants, String[] valueNames, Object... endingValues) {
		checkIsEqual(invariants.size(), endingValues.length, "Number of Invariant starting and ending do not match");
		checkIsEqual(invariants.size(), valueNames.length, "Number of Invariant values does not match number of value names");

		for (int index = 0; index < invariants.size(); index++) {
			Object startingValue = invariants.get(index);

			Object endingObject = endingValues[index];

			if (isWrapperType(endingObject.getClass()) == true) {
				checkIsEqual((Integer) startingValue, endingObject.hashCode(), "Invariants, " + valueNames[index] + ", do not match: ");
			} else if (endingObject instanceof String) {
				checkIsEqual((String) startingValue, (String) endingObject, "Invariants, " + valueNames[index] + ", do not match: ");
			} else {
				throw new RuntimeException("Invariants do not support type, " + endingObject.getClass().getName());
			}
		}
	}

	public static void checkIsEqual(int value1, int value2, String message) {
		if (value1 != value2) {
			throw new InvariantCheckException(message + "(" + String.format("%1$d", value1) + " != " + String.format("%1$d", value2) + ")");
		}
	}

	public static void checkIsEqual(String value1, String value2, String message) {
		if (value1 != value2) {
			throw new InvariantCheckException(message + "(" + quoted(value1) + " != " + quoted(value2) + ")");
		}
	}

	// Ensures

	@SafeVarargs
	public static <T> void ensureNotNullValues(T... values) {
		checkNotNullValues(CheckType.Ensure, values);
	}

	public static <T> T ensureNotNull(T value) {
		return checkNotNull(CheckType.Ensure, value, "value");
	}

	public static <T> T ensureNotNull(T value, String valueName) {
		return checkNotNull(CheckType.Ensure, value, valueName);
	}

	public static String ensureNotEmpty(String value) {
		return checkNotEmpty(CheckType.Ensure, value, "value");
	}

	public static String ensureNotEmpty(String value, String valueName) {
		return checkNotEmpty(CheckType.Ensure, value, valueName);
	}

	public static Collection<?> ensureNotEmptyCollection(Collection<?> aCollection, String valueName) {
		return checkNotEmptyCollection(CheckType.Ensure, aCollection, valueName);
	}

	public static Collection<?> ensureNotEmptyCollection(Collection<?> aCollection) {
		return checkNotEmptyCollection(CheckType.Ensure, aCollection, "collection");
	}

	public static <T> T ensure(T value, Function<T, Boolean> ensureFunction, String ensureMessage) {
		return check(CheckType.Ensure, value, ensureFunction, ensureMessage);
	}

	// Requires

	@SafeVarargs
	public static <T> void requireNotNullValues(T... values) {
		checkNotNullValues(CheckType.Require, values);
	}

	public static <T> T requireNotNull(T value) {
		return checkNotNull(CheckType.Require, value, "value");
	}

	public static <T> T requireNotNull(T value, String valueName) {
		return checkNotNull(CheckType.Require, value, valueName);
	}

	public static String requireNotEmpty(String value) {
		return checkNotEmpty(CheckType.Require, value, "value");
	}

	public static String requireNotEmpty(String value, String valueName) {
		return checkNotEmpty(CheckType.Require, value, valueName);
	}

	public static Collection<?> requireNotEmptyCollection(Collection<?> aCollection) {
		return checkNotEmptyCollection(CheckType.Require, aCollection, "collection");
	}

	public static Collection<?> requireNotEmptyCollection(Collection<?> aCollection, String valueName) {
		return checkNotEmptyCollection(CheckType.Require, aCollection, valueName);
	}

	public static <T> T require(T value, Function<T, Boolean> requireFunction, String requireMessage) {
		return check(CheckType.Require, value, requireFunction, requireMessage);
	}

	// Timing Requires

	public static boolean byPassTimeOutCheck = false;

	/**
	 * Nanotime in nanoseconds where there are 1000000 nanoseconds per second
	 *
	 * @return
	 */
	public static long getNanoStartTime() {
		return System.nanoTime();
	}

	/**
	 *
	 * @param startTime
	 *            in nanoseconds where there are 1000000 nanoseconds per second
	 * @param maximumTime
	 *            in nanoseconds where there are 1000000 nanoseconds per second
	 */
	public static void totalTimeCheck(long startTime, long maximumTime) {
		if (byPassTimeOutCheck == true) {
			return;
		}

		long endTime = System.nanoTime();

		long totalTime = (endTime - startTime);

		if (totalTime >= maximumTime) {
			throw new TimeOverCheckException("Over Time Execption: " + "(" + totalTime + " >= " + maximumTime + ")");
		}
	}

	//

	@SafeVarargs
	private static <T> void checkNotNullValues(CheckType checkType, T... values) {
		for (T value : values) {
			checkNotNull(checkType, value, "value");
		}
	}

	private static <T> T checkNotNull(CheckType checkType, T value, String valueName) {
		return check(checkType,
						value,
						(T innerValue) -> {
							return (value != null);
						},
						valueName + " must not be null");
	}

	private static String checkNotEmpty(CheckType checkType, String value, String valueName) {
		return check(checkType,
						value,
						(String innerValue) -> {
							return isNonEmpty(value);
						},
						valueName + " must not be null nor empty");
	}

	private static Collection<?> checkNotEmptyCollection(CheckType checkType, Collection<?> aCollection, String valueName) {
		checkNotNull(checkType, aCollection, valueName);

		return check(checkType,
						aCollection,
						(Collection<?> innerCollection) -> {
							return !innerCollection.isEmpty();
						},
						valueName + " must not be null nor empty Collction");
	}

	private static <T> T check(CheckType checkType, T value, Function<T, Boolean> checkFunction, String checkMessage) {
		switch (checkType) {
			case Ensure:
				if (checkFunction.apply(value) == false) {
					throw new EnsureException("Ensure Violoation: " + checkMessage);
				}
				break;
			case Require:
				if (checkFunction.apply(value) == false) {
					throw new RequiresException("Require Violoation: " + checkMessage);
				}
				break;
			default:
				break;
		}

		return value;
	}

	// Check Exceptions

	// ////////////////////////////////////////////////

	public static String quoted(String value) {
		return "\"" + value + "\"";
	}

	public static boolean isEmpty(String value) {
		return !isNonEmpty(value);
	}

	public static boolean isNonEmpty(String value) {
		return ((value != null)
		&& (value.trim().isEmpty() == false));
	}

}
