
/* 
 * Order.java 
 *  
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This Order class represents blueprint for user requested orders
 * 
 * @author Vinay More
 *
 */
public class Order implements OrderInterface, Cloneable {

	int order_id;
	Date order_time;
	String order_instrument;
	double order_price;
	String order_side;
	int order_trade_count;

	Order(int order_id, Date order_time, String order_instrument, double order_price, String order_side, int order_trade_count) {
		this.order_id = order_id;
		this.order_time = order_time;
		this.order_instrument = order_instrument;
		this.order_price = order_price;
		this.order_side = order_side;
		this.order_trade_count = order_trade_count;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int getOrderId() {
		// TODO Auto-generated method stub
		return order_id;
	}

	@Override
	public Date getOrderTime() {
		// TODO Auto-generated method stub
		return order_time;
	}

	@Override
	public String getOrderInstrument() {
		// TODO Auto-generated method stub
		return order_instrument;
	}

	@Override
	public double getOrderPrice() {
		// TODO Auto-generated method stub
		return order_price;
	}

	@Override
	public String getOrderSide() {
		// TODO Auto-generated method stub
		return order_side;
	}

	@Override
	public int getOrderTradeCount() {
		// TODO Auto-generated method stub
		return order_trade_count;
	}

	@Override
	public void setOrderId(int order_id) {
		// TODO Auto-generated method stub
		this.order_id = order_id;
	}

	@Override
	public void setOrderTime(Date order_time) {
		// TODO Auto-generated method stub
		this.order_time = order_time;
	}

	@Override
	public void setOrderInstrument(String order_instrument) {
		// TODO Auto-generated method stub
		this.order_instrument = order_instrument;
	}

	@Override
	public void setOrderPrice(double order_price) {
		// TODO Auto-generated method stub
		this.order_price = order_price;
	}

	@Override
	public void setOrderSide(String order_side) {
		// TODO Auto-generated method stub
		this.order_side = order_side;
	}

	@Override
	public void setOrderTradeCount(int order_trade_count) {
		// TODO Auto-generated method stub
		this.order_trade_count = order_trade_count;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return order_id + " | " + order_time + " | " + order_instrument + " | " + order_price + " | " + order_side
				+ " | " + order_trade_count;
	}
}
