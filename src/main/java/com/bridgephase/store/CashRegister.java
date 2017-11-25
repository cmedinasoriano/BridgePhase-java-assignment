package com.bridgephase.store;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Iterator;

import com.bridgephase.store.interfaces.ICashRegister;

public class CashRegister implements ICashRegister {

	private Inventory inventory;
	private BigDecimal total;

	public CashRegister(Inventory inventory) {
		this.inventory = inventory;
	}

	@Override
	public void beginTransaction() {
		total = new BigDecimal(0);
	}

	@Override
	public boolean scan(String upc) {
		
		// Iterate through inventory
		for( Iterator<Product> i = inventory.list().iterator(); i.hasNext(); ) {
			Product item = i.next();
			
			// If scanned upc is found
			if(item.getUpc().equals(upc)) {
				// Add to total of transaction and exit loop
				total = total.add( new BigDecimal( item.getRetailPrice() ) );
				return true;
			}
		}
		
		return false;
	}

	@Override
	public BigDecimal getTotal() {
		return total;
	}

	@Override
	public BigDecimal pay(BigDecimal cashAmount) {
		return cashAmount.subtract( getTotal() );
	}

	@Override
	public void printReceipt(OutputStream os) {
		
	}

}
