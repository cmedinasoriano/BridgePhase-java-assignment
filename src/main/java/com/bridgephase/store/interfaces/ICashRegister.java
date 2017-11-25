package com.bridgephase.store.interfaces;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import com.bridgephase.store.Product;

/**
 * Interface that represents a Cash Register.
 * 
 * @author cmedina
 */
public interface ICashRegister {
	
	/**
	 * Setups the `CashRegister` to create a new transaction which means 
	 * that a new customer is checking out
	 */
	public void beginTransaction();

	/**
	 * Scan a product based on its UPC (Universal Product Code).
	 * 
	 * @param upc a string with the Universal Product Code
	 * @return returns true if successful
	 */
	public boolean scan( String upc );

	/**
	 * @return returns the total retail price of all goods purchased
	 */
	public BigDecimal getTotal();

	/**
	 * Mimics a customer paying for their total and ends transaction.
	 * 
	 * @param cashAmount the amount paid by the customer
	 * @return returns the amount of change due to the customer
	 */
	public BigDecimal pay( BigDecimal cashAmount );

	/**
	 * Prints the receipt in the following format:
	 * <pre>
	```
	BridgePhase Convenience Store
	-----------------------------
	Total Products Bought: 17
	
	3 Apple @ $1.00: $3.00
	2 Milk @ $4.50: $9.00
	12 Peach @ $0.75: $9.00
	-----------------------------
	Total: $21.00
	Paid: $25.00
	Change: $4.00
	-----------------------------
	```
	 * </pre>
	 * 
	 * @param os the OutputStream
	 */
	public void printReceipt( OutputStream os ) throws Exception;
	
	/**
	 * @return returns an unmodifiable <code>List</code> of <code>Product</code> representing 
	 * products bought as part of the transaction.
	 */
	public List<Product> list();
}
