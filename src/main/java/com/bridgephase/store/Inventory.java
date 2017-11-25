package com.bridgephase.store;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Collections;
import java.util.Iterator;

import com.bridgephase.store.interfaces.IInventory;
import com.bridgephase.helper.*;

public class Inventory implements IInventory {
	
	List<Product> products = null;
	
	public Inventory() {
		products = new ArrayList<Product>();
	}

	@Override
	public void replenish(InputStream inputStream) {
		// Use scanner to parse input stream as string line by line
		Scanner scanner = new Scanner( inputStream );
		String line = null;
		
		// Parse InputStream line by line
		while( scanner.hasNextLine() ) {
			line = scanner.nextLine();
			
			// Parse and create a product for this line if matches pattern
			Product product = productFromString( line );
			if( product != null )
			{
				products.add( product );
			}
		}
	}

	@Override
	public List<Product> list() {
		// Returns an unmodifiable Products list
		return Collections.unmodifiableList( products );
	}

	@Override
	public void consume( String upc, int quantity ) {
		
		// Find product in inventory by upc
		Product product = ProductHelper.getProductFromList(products, upc);
		if( product != null ) {
			product.setQuantity( product.getQuantity() - quantity );
		}
	}
	
	/**
	 * Creates a product from a string as long as it matches the pattern (string,string,float,float,int)
	 * 
	 * @param string the String to parse
	 * @return a new Product object or null if string doesn't match the pattern
	 */
	private Product productFromString( String string ) {
		
		// Create regex matcher
		String pattern = "^ *([a-zA-Z0-9]+),(\\w+),(\\d+(?:\\.\\d*)?|\\.\\d+),(\\d+(?:\\.\\d*)?|\\.\\d+),(\\d+)(?:\\.0*)? *$";
		Matcher m = Pattern.compile( pattern, Pattern.DOTALL ).matcher( string );
		
		// If matches pattern
		if( m.matches() ) {

			String upc = m.group( 1 );
			String name = m.group( 2 );
			Float wholesalePrice = Float.parseFloat( m.group( 3 ) );
			Float retailPrice = Float.parseFloat( m.group( 4 ) );
			Integer quantity = Integer.parseInt( m.group( 5 ) );
			
			// Return a new product
			return new Product( upc, name, wholesalePrice, retailPrice, quantity );	
		} else {
			// Didn't match the pattern
			return null;
		}
	}
	
	
	
}
