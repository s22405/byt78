package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		assertEquals(SEK100.getAmount(), 10000);
		assertEquals(EUR10.getAmount(), 1000);
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK100.getCurrency(), SEK);
		assertEquals(EUR10.getCurrency(), EUR);
	}

	@Test
	public void testToString() {
		assertEquals(SEK100.toString(), "100.00 SEK");
		assertEquals(EUR10.toString(), "10.00 EUR");
	}

	@Test
	public void testGlobalValue() {
		assertEquals(Optional.of(SEK100.universalValue()), Optional.of(1500));
		assertEquals(Optional.of(EUR10.universalValue()), Optional.of(1500));
	}

	@Test
	public void testEqualsMoney() {
		assertFalse(SEK100.equals(SEK200));
	}

	@Test
	public void testAdd() {
		assertEquals(SEK100.add(EUR10).toString(), "200.00 SEK");
	}

	@Test
	public void testSub() {
		assertEquals(SEK100.sub(EUR10).toString(), "0.00 SEK");
	}

	@Test
	public void testIsZero() {
		assertTrue(SEK100.sub(EUR10).isZero());
		assertTrue(SEK0.isZero());
		assertFalse(SEK100.isZero());
	}

	@Test
	public void testNegate() {
		assertEquals(SEK100.negate().getAmount(), SEKn100.getAmount());
	}

	@Test
	public void testCompareTo() {
		assertEquals(SEK100.compareTo(SEK100), 0);
		assertEquals(SEK100.compareTo(SEK0), 1);
		assertEquals(SEK0.compareTo(SEK100), -1);
	}
}
