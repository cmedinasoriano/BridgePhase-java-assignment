package com.bridgephase.store;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import com.bridgephase.store.interfaces.ICashRegister;
import com.bridgephase.store.interfaces.IInventory;
import com.bridgephase.helper.*;

public class CashRegister implements ICashRegister {

	private IInventory inventory;
	private ArrayList<Product> transactionItems;
	private float paid;
	private float change;

	public CashRegister(IInventory inventory) {
		this.inventory = inventory;
		transactionItems = new ArrayList<Product>();
	}

	@Override
	public void beginTransaction() {
		transactionItems.clear();
		paid = 0;
		change = 0;
	}

	@Override
	public boolean scan(String upc) {
		
		// Get Product from Inventory
		Product product = ProductHelper.getProductFromList( inventory.list(), upc );
		
		// If Product exists in Inventory
		if( product != null ) {
		
			// Get Product from Items in transaction
			Product item = ProductHelper.getProductFromList( transactionItems, upc );
			
			// If Product was already added to transaction
			if( item != null ) {
				item.setQuantity(item.getQuantity() + 1);
			} else {
				// Adds 1 item to transaction
				transactionItems.add( 
						new Product(
								upc, 
								product.getName(), 
								product.getWholesalePrice(),
								product.getRetailPrice(),
								1 ) );
			}
			
			return true;
		}
		
		return false;
	}

	@Override
	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal( 0 );
		
		// Iterate through bought items
		for( Iterator<Product> i = transactionItems.iterator(); i.hasNext(); ) {
			Product item = i.next();
			
			// total += retailPrice * quantity
			total = total.add( new BigDecimal( item.getRetailPrice() )
					.multiply( new BigDecimal( item.getQuantity() ) ) );
		}
		
		return total;
	}

	@Override
	public BigDecimal pay(BigDecimal cashAmount) {

		BigDecimal changeOutput = cashAmount.subtract( getTotal() );
		paid = cashAmount.floatValue();
		change = changeOutput.floatValue();
		
		// Iterate through bought items list
		for( Iterator<Product> i = list().iterator(); i.hasNext(); ) {
			Product item = i.next();
			
			// Consume inventory
			inventory.consume( item.getUpc(), item.getQuantity() );
		}
		
		return changeOutput;
	}

	@Override
	public void printReceipt(OutputStream os) throws IOException {
		String output = "";
		
		int itemsBought = 0;
		for( Iterator<Product> i = transactionItems.iterator(); i.hasNext(); ) {
			Product item = i.next();

			String format = "%d %s @ $%1.2f: $%1.2f\n";
			String name = item.getName();
			Integer quantity = item.getQuantity();
			Float price = item.getRetailPrice();
			BigDecimal subTotal = new BigDecimal( price ).multiply( new BigDecimal( quantity ) );
			
			itemsBought = itemsBought + quantity;

			output = output + String.format( format, quantity, name, price, subTotal.floatValue() );
		}
		
		output = 
				"BridgePhase Convenience Store\n" + 
				"-----------------------------\n" + 
				String.format("Total Products Bought: %d\n\n", itemsBought ) + 
				output + 
				"-----------------------------\n" + 
				String.format( "Total: $%1.2f\n", getTotal().floatValue() ) +
				String.format( "Paid: $%1.2f\n", getPaidAmount() ) +
				String.format( "Change: $%1.2f\n", getChangeAmount() ) +
				"-----------------------------\n";
		
		os.write( output.getBytes() );
	}
	
	public float getPaidAmount() {
		return paid;
	}

	public float getChangeAmount() {
		return change;
	}
	
	@Override
	public List<Product> list() {
		// Returns an unmodifiable Products list
		return Collections.unmodifiableList( transactionItems );
	}
	

}
