# OrderMatchingEngine
Simple order matching engine implemented using priority queues in OrderBook for BUY/SELL orders

How to run the OrderMatchingEngine:
1. Run the .jar file
2. Select .csv test file in the file chooser
3. Locate output.txt file at same path for verifying output

Files:
1. Inside src folder - Code files
OrderInterface.java - Interface for Order class implementation
Order.java - Order class represents blueprint for user requested orders
OrderBookInterface.java - Interface for OrderBook class implementation
OrderBook.java - OrderBook class represents blueprint for each instrument's orderbook
MatchingEngine.java - MatchingEngine class tests order matching of buy/sell orders

2. OrderMatchingEngine.jar - executable file
3. csv files - Trading test data files
4. sample_output.txt - Sample output file for one of the input test data


Approach: (Main matching logic can be found in OrderBook.java)
1. Designed and implemented Order and OrderBook class

2. Customized priority queues for buy and sell orders for each instrument's orderbook.
   Dynamically sorted in descending order of the price 
   If Price matches - 
   Sell Orders are sorted in ascending order of timestamp
   Buy Orders are sorted in descending order of timestamp
   
3. Whenever new order comes, based on Side valye i.e. BUY/SELL it is matched with 
   sell orders or buy orders in priority queue.
   Orders are fulfilled one after the other (even partial fulfillment is done).
   Orders/ portion of orders which are not fulffiled are then added to the respective queue
   with remaining trades.   
     

