
/* 
 * MatchingEngine.java 
 *  
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.Date;

/**
 * This MatchingEngine class tests order matching of buy/sell orders
 * dynamically.
 * 
 * @author Vinay More
 *
 */

public class MatchingEngine {

	// stores mapping of instrument and its orderBook
	static Map<String, OrderBook> orderBooks;

	public static void main(String args[]) {

		// date-time format assumption - MM/dd/yyyy HH:mm:ss
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		orderBooks = new HashMap<String, OrderBook>();
		OrderBook cursorOrderBook;

		// input file selection
		JFileChooser fileSelector = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv files", "csv");
		fileSelector.setFileFilter(filter);
		if (fileSelector.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println("Opening file: " + fileSelector.getSelectedFile().getName());
		}
		try {
			BufferedReader buffer = new BufferedReader(
					new FileReader(fileSelector.getSelectedFile().getAbsolutePath()));
			String currentRow = buffer.readLine();

			while ((currentRow = buffer.readLine()) != null) {
				String[] rowElements = currentRow.split(",");

				if (!orderBooks.containsKey(rowElements[1])) {
					orderBooks.put(rowElements[1], new OrderBook(rowElements[1]));
				}

				cursorOrderBook = orderBooks.get(rowElements[1]);

				cursorOrderBook.addOrderHelper(new Order((Date) format.parse(rowElements[0]), rowElements[1],
						Double.parseDouble(rowElements[2]), rowElements[3], Integer.parseInt(rowElements[4])));
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrintWriter writer=null;

		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("output.txt")));

			for (String key : orderBooks.keySet()) {
				OrderBook itrOrderBook = orderBooks.get(key);

                /*
				System.out.println("\n\n---------------- OrderBook for " + key + " ----------------");
				System.out.println("id | time | instrument | price | side | trade_count");

				System.out.println("------------------- Filled Orders ------------------");
				for (Integer filledOrderkey : itrOrderBook.filled_orders.keySet()) {
					System.out.println(itrOrderBook.filled_orders.get(filledOrderkey).toString());
				}

				System.out.println("--------------------- BUY Orders --------------------");
				while (!itrOrderBook.buy_orders.isEmpty()) {
					System.out.println(itrOrderBook.buy_orders.poll());
				}

				System.out.println("--------------------- SELL Orders -------------------");
				while (!itrOrderBook.sell_orders.isEmpty()) {
					System.out.println(itrOrderBook.sell_orders.poll());
				} 
                */
				
				writer.println("\n\n---------------- OrderBook for " + key + " ----------------");
				writer.println("id | time | instrument | price | side | trade_count");

				writer.println("------------------- Filled Orders ------------------");
				for (Integer filledOrderkey : itrOrderBook.filled_orders.keySet()) {
					writer.println(itrOrderBook.filled_orders.get(filledOrderkey).toString());
					writer.flush();
				}

				writer.println("--------------------- BUY Orders --------------------");
				while (!itrOrderBook.buy_orders.isEmpty()) {
					writer.println(itrOrderBook.buy_orders.poll().toString());
					writer.flush();
				}

				writer.println("--------------------- SELL Orders -------------------");
				while (!itrOrderBook.sell_orders.isEmpty()) {
					writer.println(itrOrderBook.sell_orders.poll().toString());
					writer.flush();
				}
				writer.flush();			
			}
	
		} catch (IOException ex) {
			System.out.println("Error writing to file");
		} finally {
			try {
				writer.flush();
				writer.close();
			} catch (Exception ex) {
				System.out.println("Error closing the writer for output file");
			}
		}
	}
}
