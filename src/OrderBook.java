
/* 
 * OrderBook.java 
 *  
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This OrderBook class represents blueprint for each instrument's orderbook
 * 
 * @author Vinay More
 *
 */
public class OrderBook {

	String order_instrument;

	// dynamically sorts buy orders based on price and then time
	PriorityQueue<Order> buy_orders;

	// dynamically sorts sell orders based on price and then time
	PriorityQueue<Order> sell_orders;

	// stores map of filled orders against their respective order ids for
	// constant
	// lookup
	Map<Integer, Order> filled_orders;

	// constructor
	OrderBook(String order_instrument) {
		this.order_instrument = order_instrument;

		filled_orders = new HashMap<Integer, Order>();

		// customized priority queue for buy orders
		buy_orders = new PriorityQueue<Order>(new Comparator<Order>() {

			@Override
			public int compare(Order o1, Order o2) {
				// TODO Auto-generated method stub

				if (o1.order_price < o2.order_price) {
					return 1;
				} else if (o1.order_price > o2.order_price) {
					return -1;
				} else {
					if (o1.order_time.before(o2.order_time)) {
						return 1;
					} else {
						return -1;
					}
				}
			}
		});

		// customized priority queue for sell orders
		sell_orders = new PriorityQueue<Order>(new Comparator<Order>() {

			@Override
			public int compare(Order o1, Order o2) {
				// TODO Auto-generated method stub

				if (o1.order_price < o2.order_price) {
					return 1;
				} else if (o1.order_price > o2.order_price) {
					return -1;
				} else {
					if (o1.order_time.before(o2.order_time)) {
						return -1;
					} else {
						return 1;
					}
				}
			}
		});
	}

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
	void addOrder(Order placed_order) {

		int placedOrderTradeCount = placed_order.order_trade_count;

		if (placed_order.order_side.equals("BUY")) {

			while (sell_orders.size() > 0 && placedOrderTradeCount != 0) {

				Order currentSellOrder = sell_orders.peek();
				int currentSellOrderCount = currentSellOrder.order_trade_count;

				// check - buying price >= selling price
				if (placed_order.order_price >= currentSellOrder.order_price) {

					// check - available selling trade count <= buying trade
					// count
					if (currentSellOrderCount <= placedOrderTradeCount) {

						if (filled_orders.containsKey(currentSellOrder.order_id)) {
							filled_orders.get(currentSellOrder.order_id).order_trade_count = filled_orders
									.get(currentSellOrder.order_id).order_trade_count + currentSellOrderCount;
						} else {
							filled_orders.put(currentSellOrder.order_id, currentSellOrder);
						}

						sell_orders.poll();

						if (filled_orders.containsKey(placed_order.order_id)) {
							filled_orders.get(placed_order.order_id).order_trade_count = filled_orders
									.get(placed_order.order_id).order_trade_count + currentSellOrderCount;
						} else {
							try {
								Order partialPlacedOrder = (Order) placed_order.clone();
								partialPlacedOrder.order_trade_count = currentSellOrderCount;
								filled_orders.put(placed_order.order_id, partialPlacedOrder);

							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						placedOrderTradeCount = placedOrderTradeCount - currentSellOrderCount;

					} else {

						int currentFilledSellOrder = currentSellOrderCount - placedOrderTradeCount;
						currentSellOrder.order_trade_count = currentFilledSellOrder;

						try {
							Order partialPlacedOrder = (Order) currentSellOrder.clone();
							partialPlacedOrder.order_trade_count = placedOrderTradeCount;

							if (filled_orders.containsKey(partialPlacedOrder.order_id)) {
								filled_orders.get(partialPlacedOrder.order_id).order_trade_count = filled_orders
										.get(partialPlacedOrder.order_id).order_trade_count + placedOrderTradeCount;
							} else {

								filled_orders.put(partialPlacedOrder.order_id, partialPlacedOrder);
							}

						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (filled_orders.containsKey(placed_order.order_id)) {
							filled_orders.get(placed_order.order_id).order_trade_count = filled_orders
									.get(placed_order.order_id).order_trade_count + placedOrderTradeCount;
						} else {
							try {
								Order partialPlacedOrder = (Order) placed_order.clone();
								partialPlacedOrder.order_trade_count = placedOrderTradeCount;
								filled_orders.put(placed_order.order_id, partialPlacedOrder);
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						placedOrderTradeCount = 0;
					}
				} else {
					break;
				}

			}

			if (placedOrderTradeCount > 0) {
				try {
					Order partialRemainingOrder = (Order) placed_order.clone();
					partialRemainingOrder.order_trade_count = placedOrderTradeCount;
					buy_orders.add(partialRemainingOrder);

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {

			while (!buy_orders.isEmpty() && placedOrderTradeCount != 0) {

				Order currentBuyOrder = buy_orders.peek();

				int currentBuyOrderCount = currentBuyOrder.getOrderTradeCount();

				if (placed_order.order_price <= currentBuyOrder.order_price) {

					if (currentBuyOrderCount <= placedOrderTradeCount) {

						if (filled_orders.containsKey(currentBuyOrder.order_id)) {
							filled_orders.get(currentBuyOrder.order_id).order_trade_count = filled_orders
									.get(currentBuyOrder.order_id).order_trade_count + currentBuyOrderCount;
						} else {
							filled_orders.put(currentBuyOrder.order_id, currentBuyOrder);
						}

						buy_orders.poll();

						if (filled_orders.containsKey(placed_order.order_id)) {
							filled_orders.get(placed_order.order_id).order_trade_count = filled_orders
									.get(placed_order.order_id).order_trade_count + currentBuyOrderCount;
						} else {
							try {
								Order partialPlacedOrder = (Order) placed_order.clone();
								partialPlacedOrder.order_trade_count = currentBuyOrderCount;
								filled_orders.put(placed_order.order_id, partialPlacedOrder);

							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						placedOrderTradeCount = placedOrderTradeCount - currentBuyOrderCount;

					} else {
						int currentFilledBuyOrder = currentBuyOrderCount - placedOrderTradeCount;

						currentBuyOrder.order_trade_count = currentBuyOrder.order_trade_count - placedOrderTradeCount;

						if (filled_orders.containsKey(currentBuyOrder.order_id)) {
							filled_orders.get(currentBuyOrder.order_id).order_trade_count = filled_orders
									.get(currentBuyOrder.order_id).order_trade_count + placedOrderTradeCount;
						} else {
							try {
								Order partialPlacedOrder = (Order) currentBuyOrder.clone();
								partialPlacedOrder.order_trade_count = placedOrderTradeCount;
								filled_orders.put(currentBuyOrder.order_id, partialPlacedOrder);
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						if (filled_orders.containsKey(placed_order.order_id)) {
							filled_orders.get(placed_order.order_id).order_trade_count = filled_orders
									.get(placed_order.order_id).order_trade_count + placedOrderTradeCount;
						} else {
							try {
								Order partialPlacedOrder = (Order) placed_order.clone();
								partialPlacedOrder.order_trade_count = placedOrderTradeCount;
								filled_orders.put(placed_order.order_id, partialPlacedOrder);
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						placedOrderTradeCount = 0;
					}
				} else {
					break;
				}
			}

			if (placedOrderTradeCount > 0) {
				try {
					Order partialRemainingOrder = (Order) placed_order.clone();
					partialRemainingOrder.order_trade_count = placedOrderTradeCount;
					sell_orders.add(partialRemainingOrder);

				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}
}
