package com.bridgephase.store;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bridgephase.store.Inventory;
import com.bridgephase.store.Product;
import com.bridgephase.store.interfaces.IInventory;
import com.bridgephase.helper.*;

public class InventoryUnitTests {

	static Inventory inventory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		// Set a custom error handler as the default exception handler in current thread
		Thread.currentThread().setUncaughtExceptionHandler( new MyUncaughtExceptionHandler() );
		
		InputStream input = Helper.inputStreamFromString(
				"upc,name,wholesalePrice,retailPrice,quantity\n" + 
				"A123,Apple,0.50,1.00,100\n" +
				"B234,Peach,0.35,0.75,200\n" +
				"C123,Milk,2.15,4.50,40" );
		
		inventory = new Inventory();
		inventory.replenish(input);
	}
	
	@Test
	public void productsAreCreated() {
		assertEquals( 3, inventory.list().size() );
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void productsListIsUnmodifiable() throws Exception {
		
		IInventory inventory = new Inventory();
		
		// Get unmodified list
		List<Product> list = inventory.list();
		
		// This should throw an UnsupportedOperationException
		list.add( new Product( "A123", "Apple", 0.50f, 1.0f, 100 ) );
	}
	
	@Test
	public void inventoryIsConsumed() {
		
		String upc = "A123";
		Product product = ProductHelper.getProductFromList( inventory.list(), upc );
		Integer quantityBefore = product.getQuantity();
		
		inventory.consume( upc, 99 );
		
		assertNotEquals( quantityBefore, product.getQuantity() );
		
	}
	
	@Test
	public void inventoryIsReplenished() throws Exception {
		
		InputStream input = Helper.inputStreamFromString(
				"upc,name,wholesalePrice,retailPrice,quantity\n" + 
				"A123,Apple Red,0.60,1.20,100" );
		
		String upc = "A123";
		Product product = ProductHelper.getProductFromList( inventory.list(), upc );
		String nameBefore = product.getName();
		Float wholesalePriceBefore = product.getWholesalePrice();
		Float retailPriceBefore = product.getRetailPrice();
		Integer quantityBefore = product.getQuantity();
		
		inventory.replenish(input);

		assertNotEquals( nameBefore, product.getName() );
		assertNotEquals( wholesalePriceBefore, product.getWholesalePrice() );
		assertNotEquals( retailPriceBefore, product.getRetailPrice() );
		assertNotEquals( quantityBefore, product.getQuantity() );
		assertNotEquals( new Integer( 100 ), product.getQuantity() );
		
	}
	
}
