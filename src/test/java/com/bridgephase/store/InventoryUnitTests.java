package com.bridgephase.store;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.bridgephase.store.Inventory;
import com.bridgephase.store.Product;
import com.bridgephase.store.interfaces.IInventory;

import junit.framework.AssertionFailedError;

public class InventoryUnitTests {

	@Test
	public void productsAreCreated() throws UnsupportedEncodingException {
		
		// Set a custom error handler as the default exception handler in current thread
		Thread.currentThread().setUncaughtExceptionHandler( new MyUncaughtExceptionHandler() );

		InputStream input = inputStreamFromString(
				"upc,name,wholesalePrice,retailPrice,quantity\n" + 
				"A123,Apple,0.50,1,100\n" + 
				"B234,Peach,.35,0.75,200.0\n" +
				"C123,Milk,2.15,4.50,40.\n" +
				"D987,WrongQty,2.15,4.50,1.123\n" +
				"This, is, an, ignored, footer" );
		
		IInventory inventory = new Inventory();
		
		inventory.replenish( input );
		assertEquals( 3, inventory.list().size() );
		
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void productsListIsUnmodifiable() throws Exception {
		
		IInventory inventory = new Inventory();
		
		// Get unmodified list
		List<Product> list = inventory.list();
		
		// This should throw an UnsupportedOperationException
		list.add( new Product( "A123","Apple",0.50,1,100 ) );
	}
	
	/**
	 * This is a simple way to convert a string to an input stream.
	 * 
	 * @param value
	 *            the String value to convert
	 * @return an InputStream that can read the values from the
	 *         <code>value</code> parameter
	 * @throws UnsupportedEncodingException
	 */
	private static InputStream inputStreamFromString( String value ) throws UnsupportedEncodingException {
		return new ByteArrayInputStream( value.getBytes( "UTF-8" ) );
	}
	
}
