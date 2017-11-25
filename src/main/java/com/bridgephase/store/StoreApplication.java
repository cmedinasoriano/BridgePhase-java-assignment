package com.bridgephase.store;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.math.BigDecimal;

import com.bridgephase.store.interfaces.IInventory;
import com.bridgephase.store.MyUncaughtExceptionHandler;
import com.bridgephase.store.CashRegister;
import com.bridgephase.helper.*;


/**
 * This can be used as an entry point to ensure that you have a working implementation for Part 1.
 * 
 * This class will initially contain compilation errors because the classes in
 * the exercise have not been implemented. Once you've implemented them the
 * compilation errors should disappear.
 * 
 * You are free to modify this class as you deem necessary although it's highly
 * recommended that you produce jUnit tests to verify the logic in your code.
 * 
 * @author Jaime Garcia Ramirez (jramirez@bridgephase.com)
 */
public class StoreApplication {

	/**
	 * This is the main entry point to this application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) throws IOException, UnsupportedEncodingException {
		
		// Set a custom error handler as the default exception handler in main/current thread
		Thread.currentThread().setUncaughtExceptionHandler( new MyUncaughtExceptionHandler() );
		
		InputStream input = Helper.inputStreamFromString(
				"upc,name,wholesalePrice,retailPrice,quantity\n" + 
				"A123,Apple,0.50,1.00,100\n" +
				"B234,Peach,0.35,0.75,200\n" +
				"C123,Milk,2.15,4.50,40" );
		IInventory inventory = new Inventory();
		inventory.replenish(input);
		
		CashRegister register = new CashRegister( inventory );
		
		// Begin transaction and scan items
		register.beginTransaction();
		for(int i=0; i<3; i++) register.scan("A123");
		for(int i=0; i<2; i++) register.scan("C123");
		for(int i=0; i<12; i++) register.scan("B234");
		register.pay( new BigDecimal(25) );
		
		OutputStream output = System.out;
		register.printReceipt(output);
		
		output.flush();
	}
}