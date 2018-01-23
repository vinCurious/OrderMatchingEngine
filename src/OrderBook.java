
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
import java.util.Queue;

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

	Map<Integer, Order> orderMap;

	// constructor
	OrderBook(String order_instrument) {
		this.order_instrument = order_instrument;

		filled_orders = new HashMap<Integer, Order>();

		orderMap = new HashMap<Integer, Order>();

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

	void removeExistingOrder(int order_id) {
		if (orderMap.containsKey(order_id)) {
			if (orderMap.get(order_id).order_side.equals("BUY")) {
				buy_orders.remove(orderMap.get(order_id));
			} else {
				sell_orders.remove(orderMap.get(order_id));
			}
			orderMap.remove(order_id);
		}
	}

	void addOrderHelper(Order placed_order) {
		removeExistingOrder(placed_order.order_id);

		if (!filled_orders.containsKey(placed_order.order_id)) {
			if (placed_order.order_side.equals("BUY")) {
				addOrder(placed_order, sell_orders, buy_orders);
				orderMap.put(placed_order.order_id, placed_order);
			} else if (placed_order.order_side.equals("SELL")) {
				addOrder(placed_order, buy_orders, sell_orders);
				orderMap.put(placed_order.order_id, placed_order);
			}
		}
	}

	void fillPartialOrders(Order orderToBeFilled, int orderCount) {
		if (filled_orders.containsKey(orderToBeFilled.order_id)) {
			filled_orders.get(orderToBeFilled.order_id).order_trade_count = filled_orders
					.get(orderToBeFilled.order_id).order_trade_count + orderCount;
		} else {
			try {
				Order partialPlacedOrder = (Order) orderToBeFilled.clone();
				partialPlacedOrder.order_trade_count = orderCount;
				filled_orders.put(orderToBeFilled.order_id, partialPlacedOrder);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void completeOrderFromQueue(Order currentOrderFromQueue, int currentOrderFromQueueCount) {
		if (filled_orders.containsKey(currentOrderFromQueue.order_id)) {
			filled_orders.get(currentOrderFromQueue.order_id).order_trade_count = filled_orders
					.get(currentOrderFromQueue.order_id).order_trade_count + currentOrderFromQueueCount;
		} else {
			filled_orders.put(currentOrderFromQueue.order_id, currentOrderFromQueue);
		}
	}

	int executeOrder(Order placed_order, int placedOrderTradeCount, Queue ordersQueue, Order currentOrderFromQueue,
			int currentOrderFromQueueCount) {

		if (currentOrderFromQueueCount <= placedOrderTradeCount) {
			completeOrderFromQueue(currentOrderFromQueue, currentOrderFromQueueCount);
			ordersQueue.poll();
			fillPartialOrders(placed_order, currentOrderFromQueueCount);
			placedOrderTradeCount = placedOrderTradeCount - currentOrderFromQueueCount;
		} else {
			int currentFilledSellOrder = currentOrderFromQueueCount - placedOrderTradeCount;
			currentOrderFromQueue.order_trade_count = currentFilledSellOrder;
			try {
				Order partialPlacedOrder = (Order) currentOrderFromQueue.clone();
				partialPlacedOrder.order_trade_count = placedOrderTradeCount;
				completeOrderFromQueue(partialPlacedOrder, placedOrderTradeCount);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fillPartialOrders(placed_order, placedOrderTradeCount);
			placedOrderTradeCount = 0;
		}
		return placedOrderTradeCount;
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
	void addOrder(Order placed_order, Queue<Order> ordersQueue, Queue<Order> addPartialOrders) {

		int placedOrderTradeCount = placed_order.order_trade_count;

		while (!ordersQueue.isEmpty() && placedOrderTradeCount != 0) {
			Order currentOrderFromQueue = ordersQueue.peek();
			int currentOrderFromQueueCount = currentOrderFromQueue.order_trade_count;
			if (placed_order.order_side.equals("BUY")) {
				if (placed_order.order_price >= currentOrderFromQueue.order_price) {
					placedOrderTradeCount = executeOrder(placed_order, placedOrderTradeCount, ordersQueue,
							currentOrderFromQueue, currentOrderFromQueueCount);
				} else {
					break;
				}
			} else {
				if (placed_order.order_price <= currentOrderFromQueue.order_price) {
					placedOrderTradeCount = executeOrder(placed_order, placedOrderTradeCount, ordersQueue,
							currentOrderFromQueue, currentOrderFromQueueCount);
				} else {
					break;
				}
			}
		}

		if (placedOrderTradeCount > 0) {
			try {
				Order partialRemainingOrder = (Order) placed_order.clone();
				partialRemainingOrder.order_trade_count = placedOrderTradeCount;
				addPartialOrders.add(partialRemainingOrder);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
