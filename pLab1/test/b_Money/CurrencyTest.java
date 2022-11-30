package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		assertEquals(SEK.getName(), "SEK");
		assertEquals(DKK.getName(), "DKK");
		assertEquals(EUR.getName(), "EUR");
	}
	
	@Test
	public void testGetRate() {
		assertEquals(Optional.of(SEK.getRate()), Optional.of(0.15));
		assertEquals(Optional.of(DKK.getRate()), Optional.of(0.20));
		assertEquals(Optional.of(EUR.getRate()), Optional.of(1.5));
	}
	
	@Test
	public void testSetRate() {
		assertEquals(Optional.of(SEK.getRate()), Optional.of(0.15));
		SEK.setRate(0.20);
		assertEquals(Optional.of(SEK.getRate()), Optional.of(0.20));
	}
	
	@Test
	public void testGlobalValue() {
		assertEquals(Optional.of(SEK.universalValue(100)), Optional.of(15));
		assertEquals(Optional.of(DKK.universalValue(100)), Optional.of(20));
		assertEquals(Optional.of(EUR.universalValue(100)), Optional.of(150));
	}
	
	@Test
	public void testValueInThisCurrency() {
		assertEquals(Optional.of(SEK.valueInThisCurrency(100,EUR)), Optional.of(1000));
	}

}
