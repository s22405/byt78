package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika", new Account("SEK", SEK));
		SweBank.openAccount("Bob", new Account("SEK", SEK));
		Nordea.openAccount("Bob", new Account("SEK", SEK));
		DanskeBank.openAccount("Gertrud", new Account("DKK", DKK));
	}

	@Test
	public void testGetName() {
		assertEquals(SweBank.getName(), "SweBank");
		assertEquals(Nordea.getName(), "Nordea");
		assertEquals(DanskeBank.getName(), "DanskeBank");
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SweBank.getCurrency(), SEK);
		assertEquals(Nordea.getCurrency(), SEK);
		assertEquals(DanskeBank.getCurrency(), DKK);
	}

	@Test(expected=AccountExistsException.class)
	public void testOpenAccount() throws AccountExistsException {
		//test if we can open an account with a unique ID
		SweBank.openAccount("Test", new Account("SEK", SEK));
		assertTrue(SweBank.accountExists("Test"));

		//test if we can open an account with a non-unique ID
		SweBank.openAccount("Test", new Account("SEK", SEK));
	}

	@Test(expected=AccountDoesNotExistException.class)
	public void testDeposit() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(100, SEK));
		assertEquals(SweBank.getBalance("Ulrika").toString(), "1.00 SEK");

		SweBank.deposit("Joe Mama", new Money(100, SEK));
	}

	@Test(expected=AccountDoesNotExistException.class)
	public void testWithdraw() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(100, SEK));
		assertEquals(SweBank.getBalance("Ulrika").toString(), "1.00 SEK");

		SweBank.withdraw("Ulrika", new Money(100, SEK));
		assertEquals(SweBank.getBalance("Ulrika").toString(), "0.00 SEK");

		SweBank.withdraw("Dee's Gnats", new Money(100, SEK));
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testGetBalance() throws AccountDoesNotExistException {
		assertEquals(SweBank.getBalance("Ulrika").toString(), "0.00 SEK");
		SweBank.getBalance("IceWallowCome");
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testTransfer() throws AccountDoesNotExistException {
		//same bank transfer
		SweBank.deposit("Ulrika", new Money(100, SEK));
		SweBank.deposit("Bob", new Money(100, SEK));
		SweBank.transfer("Ulrika", "Bob", new Money(100, SEK));
		assertEquals(SweBank.getBalance("Ulrika").toString(), "0.00 SEK");
		assertEquals(SweBank.getBalance("Bob").toString(), "2.00 SEK");

		//different bank and currency transfer
		SweBank.deposit("Ulrika", new Money(100, SEK));
		DanskeBank.deposit("Gertrud", new Money(100, DKK));
		SweBank.transfer("Ulrika", DanskeBank, "Gertrud", new Money(100, SEK));
		assertEquals(SweBank.getBalance("Ulrika").toString(), "0.00 SEK");
		assertEquals(DanskeBank.getBalance("Gertrud").toString(), "1.74 DKK");

		//exception
		SweBank.transfer("Mind Goblin", DanskeBank, "Tha Tsac", new Money(100, SEK));
	}
	
	@Test(expected=AccountDoesNotExistException.class)
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.deposit("Ulrika", new Money(100, SEK));

		SweBank.addTimedPayment("Ulrika", "NotDrugs", 2, 0, new Money(100 ,SEK), SweBank, "Bob");
		SweBank.addTimedPayment("Bob", "AgainNotDrugs", 2, 1, new Money(100 ,SEK), SweBank, "Ulrika");

		assertTrue(SweBank.getAccount("Ulrika").timedPaymentExists("NotDrugs"));
		assertTrue(SweBank.getAccount("Bob").timedPaymentExists("AgainNotDrugs"));
		assertEquals(SweBank.getBalance("Ulrika").toString(), "1.00 SEK");
		assertEquals(SweBank.getBalance("Bob").toString(), "0.00 SEK");

		SweBank.tick();

		assertEquals(SweBank.getBalance("Ulrika").toString(), "0.00 SEK");
		assertEquals(SweBank.getBalance("Bob").toString(), "1.00 SEK");

		SweBank.tick();

		assertEquals(SweBank.getBalance("Ulrika").toString(), "1.00 SEK");
		assertEquals(SweBank.getBalance("Bob").toString(), "0.00 SEK");

		SweBank.tick();

		assertEquals(SweBank.getBalance("Ulrika").toString(), "1.00 SEK");
		assertEquals(SweBank.getBalance("Bob").toString(), "0.00 SEK");

		SweBank.tick();

		assertEquals(SweBank.getBalance("Ulrika").toString(), "0.00 SEK");
		assertEquals(SweBank.getBalance("Bob").toString(), "1.00 SEK");

		SweBank.tick();

		assertEquals(SweBank.getBalance("Ulrika").toString(), "1.00 SEK");
		assertEquals(SweBank.getBalance("Bob").toString(), "0.00 SEK");

		SweBank.removeTimedPayment("Ulrika", "NotDrugs");
		SweBank.removeTimedPayment("Bob", "AgainNotDrugs");

		assertFalse(SweBank.getAccount("Ulrika").timedPaymentExists("NotDrugs"));
		assertFalse(SweBank.getAccount("Bob").timedPaymentExists("AgainNotDrugs"));

		SweBank.addTimedPayment("Ulrika", "MaybeDrugs", 2, 0, new Money(100 ,SEK), SweBank, "NonExistantAccount");
	}
}
