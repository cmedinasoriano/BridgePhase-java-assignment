package com.bridgephase.helper;

import java.util.Iterator;
import java.util.List;

import com.bridgephase.store.Product;

public class ProductHelper {
	public static Product getProductFromList(List<Product> list, String upc) {
		// Iterate through inventory
		for( Iterator<Product> i = list.iterator(); i.hasNext(); ) {
			Product item = i.next();
			
			// If scanned upc is found
			if( item.getUpc().equals( upc ) ) {
				return item;
			}
		}
		return null;
	}
}
