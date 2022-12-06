package b_Money;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice", new Account("SEK", SEK));
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("1", 1, 1, new Money(100, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("1"));
		testAccount.removeTimedPayment("1");
		assertFalse(testAccount.timedPaymentExists("1"));
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment("1", 2, 0, new Money(100, SEK), SweBank, "Alice");
		assertTrue(testAccount.timedPaymentExists("1"));
		assertEquals(SweBank.getBalance("Alice").toString(), "10000.00 SEK");

		testAccount.tick();
		assertEquals(SweBank.getBalance("Alice").toString(), "10001.00 SEK");

		testAccount.tick();
		assertEquals(SweBank.getBalance("Alice").toString(), "10001.00 SEK");

		testAccount.tick();
		assertEquals(SweBank.getBalance("Alice").toString(), "10001.00 SEK");

		testAccount.tick();
		assertEquals(SweBank.getBalance("Alice").toString(), "10002.00 SEK");

		testAccount.addTimedPayment("2",1,1,new Money(100, SEK), SweBank, "no");
	}

	@Test
	public void testDepositWithdraw() { //changed from testAddWithdraw to testDepositWithdraw bc that's what the methods are called
		assertEquals(testAccount.getBalance().toString(), "100000.00 SEK");

		testAccount.deposit(new Money(10000000, SEK));
		assertEquals(testAccount.getBalance().toString(), "200000.00 SEK");

		testAccount.withdraw(new Money(10000000, SEK));
		assertEquals(testAccount.getBalance().toString(), "100000.00 SEK");
	}
	
	@Test
	public void testGetBalance() {
		assertEquals(testAccount.getBalance().toString(), "100000.00 SEK");
	}
}
