package com.bridgephase.store;

import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.Collections;

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
		
		// Parse and create product from input stream
		Product product;
		
		while( scanner.hasNextLine() ) {
			String line =  scanner.nextLine();
			
			// Parse and create a product for this line if matches pattern
			product = productFromString( line );
			
			if( product != null )
			{
				Product existent = ProductHelper.getProductFromList( products, product.getUpc() );
				if( existent != null ) {
					updateProduct(existent, product);
				} else {
					products.add( product );
				}
			}
		}
		
		scanner.close();
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
		String pattern = "^ *([a-zA-Z0-9]+),([\\w ]+),(\\d+(?:\\.\\d*)?|\\.\\d+),(\\d+(?:\\.\\d*)?|\\.\\d+),(\\d+)(?:\\.0*)? *$";
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
	
	/**
	 * Updates a product from one to another incrementing quantity
	 * 
	 * @param from the <code>Product</code> to update
	 * @param to the <code>Product</code> to update to
	 */
	private void updateProduct( Product from, Product to ) {
		
		if( from.getUpc().equals( to.getUpc() ) ) {
			
			from.setName( to.getName() );
			from.setWholesalePrice( to.getWholesalePrice() );
			from.setRetailPrice( to.getRetailPrice() );
			from.setQuantity( from.getQuantity() + to.getQuantity() );
		}
	}
	
}
