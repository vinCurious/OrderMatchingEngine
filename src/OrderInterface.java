
/* 
 * OrderInterface.java 
 *  
 */

import java.util.Date;

/**
 * OrderInterface is interface for Order class implementation
 * 
 * @author Vinay More
 *
 */
public interface OrderInterface {

	Object clone() throws CloneNotSupportedException;

	int getOrderId();

	Date getOrderTime();

	String getOrderInstrument();

	double getOrderPrice();

	String getOrderSide();

	int getOrderTradeCount();

	String toString();

	void setOrderId(int order_id);

	void setOrderTime(Date order_time);

	void setOrderInstrument(String order_instrument);

	void setOrderPrice(double order_price);

	void setOrderSide(String order_side);

	void setOrderTradeCount(int order_trade_count);
}
