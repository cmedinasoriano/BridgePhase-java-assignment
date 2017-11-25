package com.bridgephase.store;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.bridgephase.helper.*;
import com.bridgephase.store.interfaces.IInventory;

public class CashRegisterUnitTests {
	
	static Inventory inventory;
	static CashRegister register;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		InputStream input = Helper.inputStreamFromString(
				"upc,name,wholesalePrice,retailPrice,quantity\n" + 
				"A123,Apple,0.50,1.00,100\n" +
				"B234,Peach,0.35,0.75,200\n" +
				"C123,Milk,2.15,4.50,40" );
		
		inventory = new Inventory();
		inventory.replenish(input);
		register = new CashRegister( inventory );
	}
	
	private void presetCashRegister() {
		register.beginTransaction();
		for(int i=0; i<3; i++) register.scan("A123");
		for(int i=0; i<2; i++) register.scan("C123");
		for(int i=0; i<12; i++) register.scan("B234");
	}

	@Test
	public void TransactionTotalStartsWithZero() {
		register.beginTransaction();
		assertEquals( new BigDecimal( 0 ), register.getTotal() );
	}

	@Test
	public void TransactionTotalIsCorrect() {
		presetCashRegister();

		assertEquals( new BigDecimal( "21.00" ), register.getTotal() );

		register.beginTransaction();
		assertEquals( new BigDecimal( 0 ), register.getTotal() );
	}

	@Test
	public void TransactionChangeIsCorrect() {
		presetCashRegister();
		BigDecimal change = register.pay( new BigDecimal( 25 ) );

		assertEquals( new BigDecimal( "4.00" ), change );
	}

	@Test
	public void TransactionTotalBoughtItemsIsCorrect() {
		presetCashRegister();
		
		int count = 0;
		for(Iterator<Product> i = register.list().iterator(); i.hasNext(); ) {
			Product item = i.next();
			count += item.getQuantity();
		}

		assertEquals( 17, count );
	}

	@Test(expected = UnsupportedOperationException.class)
	public void transactionItemsListIsUnmodifiable() throws Exception {
		
		// Get unmodified list
		List<Product> list = register.list();
		
		// This should throw an UnsupportedOperationException
		list.add( new Product( "A123", "Apple", 0.50f, 1.0f, 100 ) );
	}

}
