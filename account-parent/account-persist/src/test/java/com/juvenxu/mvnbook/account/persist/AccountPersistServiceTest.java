package com.juvenxu.mvnbook.account.persist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AccountPersistServiceTest {
	private AccountPersistService service;

	@Before
	public void prepare() throws Exception {
		final File persistDataFile = new File("target/test-classes/persist-data.xml");
		// final File persistDataFile = new File("persist-data.xml");
		//

		if (persistDataFile.exists()) {
			persistDataFile.delete();
		}

		final ApplicationContext ctx = new ClassPathXmlApplicationContext("account-persist.xml");

		service = (AccountPersistService) ctx.getBean("accountPersistService");

		final Account account = new Account();
		account.setId("juven");
		account.setName("Juven Xu");
		account.setEmail("juven@changeme.com");
		account.setPassword("this_should_be_encrypted");
		account.setActivated(true);

		service.createAccount(account);
	}

	@Test
	public void testCreateAccount() throws Exception {
		assertNull(service.readAccount("mike"));

		final Account account = new Account();
		account.setId("mike");
		account.setName("Mike");
		account.setEmail("mike@changeme.com");
		account.setPassword("this_should_be_encrypted");
		account.setActivated(true);

		service.createAccount(account);

		assertNotNull(service.readAccount("mike"));
	}

	@Test
	public void testDeleteAccount() throws Exception {
		assertNotNull(service.readAccount("juven"));
		service.deleteAccount("juven");
		assertNull(service.readAccount("juven"));
	}

	@Test
	public void testReadAccount() throws Exception {
		final Account account = service.readAccount("juven");

		assertNotNull(account);
		assertEquals("juven", account.getId());
		assertEquals("Juven Xu", account.getName());
		assertEquals("juven@changeme.com", account.getEmail());
		assertEquals("this_should_be_encrypted", account.getPassword());
		assertTrue(account.isActivated());
	}

	@Test
	public void testUpdateAccount() throws Exception {
		Account account = service.readAccount("juven");

		account.setName("Juven Xu 1");
		account.setEmail("juven1@changeme.com");
		account.setPassword("this_still_should_be_encrypted");
		account.setActivated(false);

		service.updateAccount(account);

		account = service.readAccount("juven");

		assertEquals("Juven Xu 1", account.getName());
		assertEquals("juven1@changeme.com", account.getEmail());
		assertEquals("this_still_should_be_encrypted", account.getPassword());
		assertFalse(account.isActivated());
	}
}
