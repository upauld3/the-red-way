package red.steady.way;

import static org.assertj.core.api.Assertions.assertThat;
import static red.steady.way.ContractUtils.checkInvariants;
import static red.steady.way.ContractUtils.createInvariants;
import static red.steady.way.ContractUtils.ensure;
import static red.steady.way.ContractUtils.ensureNotEmpty;
import static red.steady.way.ContractUtils.ensureNotEmptyCollection;
import static red.steady.way.ContractUtils.ensureNotNull;
import static red.steady.way.ContractUtils.ensureNotNullValues;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ContractUtilsTest {

	// Test Invariants

	@Test
	public void testInvariants() {
		List<Object> invariants = createInvariants(true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);

		checkInvariants(invariants, true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsBooleanChanged() {
		List<Object> invariants = createInvariants(true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);

		checkInvariants(invariants, false, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsByteChanged() {
		List<Object> invariants = createInvariants(true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);

		checkInvariants(invariants, true, 'a', (byte) 1, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsShortChanged() {
		List<Object> invariants = createInvariants(true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);

		checkInvariants(invariants, true, 'a', (byte) 3, (short) 1, (int) 5, (long) 6, (float) 7.1, (double) 8.2);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsIntChanged() {
		List<Object> invariants = createInvariants(true, 'a', (byte) 3, (short) 4, (int) 10, (long) 6, (float) 7.1, (double) 8.2);

		checkInvariants(invariants, true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsFloatChanged() {
		List<Object> invariants = createInvariants(true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 1.9, (double) 8.2);

		checkInvariants(invariants, true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsDoubleChanged() {
		List<Object> invariants = createInvariants(true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 8.2);

		checkInvariants(invariants, true, 'a', (byte) 3, (short) 4, (int) 5, (long) 6, (float) 7.1, (double) 12.344);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsWrongNumber() {
		List<Object> invariants = createInvariants(1, 2, 3);

		checkInvariants(invariants, 1, 2, 3, 4);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsWrongNumber2() {
		List<Object> invariants = createInvariants(1, 2, 3, 4);

		checkInvariants(invariants, 1, 2, 3);
	}

	@Test(expected = InvariantCheckException.class)
	public void testInvariantsWrongNumberNames() {
		List<Object> invariants = createInvariants(1, 2, 3, 4);

		checkInvariants(invariants, new String[] {}, 1, 2, 3);
	}

	@Test
	public void testInvariantsWithNames() {
		List<Object> invariants = createInvariants(1, 2, 3);

		checkInvariants(invariants, new String[] { "one", "two", "three" }, 1, 2, 3);
	}

	// Test Ensures

	@Test
	public void testEnsureNotNullValues() {
		ensureNotNullValues(1, 2, "Hello", 3);
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotNullValuesFailure() {
		ensureNotNullValues(1, 2, null, 3);
	}

	@Test
	public void testEnsureNotNull() {
		Object object = new Object();

		Object reference = ensureNotNull(object);

		assertThat(object).isEqualTo(reference);
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotNullFailure() {
		ensureNotNull(null);
	}

	@Test
	public void testEnsureNotNullWithName() {
		Object object = new Object();

		Object reference = ensureNotNull(object, "name");

		assertThat(object).isEqualTo(reference);
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotNullWithNameFailure() {
		ensureNotNull(null, "name");
	}

	@Test
	public void testEnsureNotEmpty() {
		String string = "Hello";

		String reference = ensureNotEmpty(string);

		assertThat(string).isEqualTo(reference);
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotEmptyFailure() {
		ensureNotEmpty(null);
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotEmptyFailure2() {
		ensureNotEmpty("");
	}

	@Test
	public void testEnsureNotEmptyWithName() {
		String string = "Hello";

		String reference = ensureNotEmpty(string, "name");

		assertThat(string).isEqualTo(reference);
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotEmptyWithNameFailure() {
		ensureNotEmpty(null, "name");
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotEmptyWithNameFailure2() {
		ensureNotEmpty("", "name");
	}

	@Test
	public void testEnsureNotEmptyCollection() {
		List<String> list = new ArrayList<String>();
		list.add("Hello");
		
		ensureNotEmptyCollection(list);
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotEmptyCollectionFailure() {
		ensureNotEmptyCollection(new ArrayList<String>());
	}

	@Test
	public void testEnsureNotEmptyCollectionWithName() {
		List<String> list = new ArrayList<String>();
		list.add("Hello");
		
		ensureNotEmptyCollection(list, "name");
	}

	@Test(expected = EnsureException.class)
	public void testEnsureNotEmptyCollectionWithNameFailure() {
		ensureNotEmptyCollection(new ArrayList<String>(), "name");
	}

	@Test
	public void testEnsure() {
		ensure("Hello world!", (String value) -> {
			return value.equals("Hello world!");
		},
				"Not \"Hello world!\"");
	}

	@Test(expected = EnsureException.class)
	public void testEnsureFailure() {
		ensure("Hello world!", (String value) -> {
			return value.equals("NOT MATCHING");
		},
				"Not \"Hello world!\"");
	}

}
