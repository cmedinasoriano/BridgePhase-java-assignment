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
	public void transactionTotalStartsWithZero() {
		register.beginTransaction();
		assertEquals( new BigDecimal( 0 ), register.getTotal() );
	}

	@Test
	public void transactionTotalIsCorrect() {
		presetCashRegister();

		assertTrue( new BigDecimal( 21 ).compareTo( register.getTotal() ) == 0 );

		register.beginTransaction();
		assertTrue( new BigDecimal( 0 ).compareTo( register.getTotal() ) == 0 );
	}

	@Test
	public void transactionPaidAmmountIsCorrect() {
		presetCashRegister();
		
		BigDecimal paid = new BigDecimal( 25 );
		register.pay( paid );
		assertTrue( paid.compareTo( new BigDecimal( register.getPaidAmount() ) ) == 0 );
		
		register.beginTransaction();
		paid = new BigDecimal( register.getPaidAmount() );
		assertTrue( paid.compareTo( new BigDecimal( 0 ) ) == 0 );
	}

	@Test
	public void transactionChangeAmmountIsCorrect() {
		presetCashRegister();
		
		BigDecimal change = register.pay( new BigDecimal( 25 ) );

		assertTrue( change.compareTo( new BigDecimal( 4 ) ) == 0 );
		assertTrue( change.compareTo( new BigDecimal( register.getChangeAmount() ) ) == 0 );
		
		register.beginTransaction();
		change = new BigDecimal( register.getChangeAmount() );
		assertTrue( change.compareTo( new BigDecimal( 0 ) ) == 0 );
	}

	@Test
	public void transactionTotalBoughtItemsIsCorrect() {
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
	
	@Test
	public void inventoryIsConsumedWhenTransactionIsPaid() {
		presetCashRegister();
		
		String upc = "A123";
		Product product = ProductHelper.getProductFromList( inventory.list(), upc );
		Integer quantityBefore = product.getQuantity();
		
		register.pay(new BigDecimal( 25 ) );

		assertNotEquals( quantityBefore, product.getQuantity() );
	}
	
	@Test
	public void inventoryValidatesInventoryAvailability() {
		presetCashRegister();
		
		String upc = "A123";
		Product product = ProductHelper.getProductFromList( inventory.list(), upc );
		Product item = ProductHelper.getProductFromList( register.list(), upc );
		
		// Scan remaining items in inventory
		for(int i = item.getQuantity(); i < product.getQuantity() ; i++) register.scan("A123");

		// Should assert to false since there aren't enough quantities in inventory
		assertFalse( register.scan("A123") );
	}

}
