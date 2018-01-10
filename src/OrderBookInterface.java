/* 
 * OrderBookInterface.java 
 *  
 */

/**
 * OrderBookInterface is interface for OrderBook class implementation
 * 
 * @author Vinay More
 *
 */
public interface OrderBookInterface {

	/**
	 * function addOrder
	 * 
	 * description - Addition of each new order (Buy/Sell) in respective orders
	 * queue. Function first tries to fulfill the current order (even partial
	 * fulfillment is tried) by peeking into respective priority lists. Orders
	 * or portion of orders which are not fulfilled are again added in the
	 * priority list and dynamically gets sorted and finds their position in the
	 * queue.
	 * 
	 * @param placed_order
	 *            - Order object based on user request
	 */
	void addOrder(Order placed_order);

}
