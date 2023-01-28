package controller;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import model.Cart;
import model.Product;
import model.Transaction;
import util.Database;

public class TransactionController {
	ArrayList<Transaction> transactions = new ArrayList<>();
	
	public void addNewTransaction(Database db, ArrayList<Cart> carts, ArrayList<Product> products, String paymentMethod, String cardNumber, Double totalPrice) {
		String query = "INSERT INTO transactionheaders (paymentmethod, cardnumber, transactiondate, totalprice) "+"VALUES (?, ?, ?, ?)";
		PreparedStatement prepQuery = db.preparedStatement(query);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		try {
			prepQuery.setString(1, paymentMethod);
			prepQuery.setString(2, cardNumber);
			prepQuery.setTimestamp(3, timestamp);
			prepQuery.setDouble(4, totalPrice);
			prepQuery.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		addNewTransactionDetail(db, carts);
//		decreaseProductStock(db, carts, products);
	}
	
//	public void decreaseProductStock(Database db, ArrayList<Cart> carts, ArrayList<Product> products) {
//		String query = "UPDATE products SET productstock = ? WHERE id = ?";
//		PreparedStatement prepQuery = db.preparedStatement(query);
//		
//		try {
//			for(Cart cart : carts) {
//				for(Product product : products) {
//					if(cart.getProductId() == product.getId()) {
//						prepQuery.setInt(1, product.getStock()-cart.getQuantity());
//						prepQuery.setInt(2, product.getId());
//						prepQuery.executeUpdate();
//					}
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	public void getLatestTransactionId(Database db) {
		String query = "SELECT * FROM transactionheaders";
		PreparedStatement psQuery = db.preparedStatement(query);
		
		try {
			db.rs = psQuery.executeQuery();
			while(db.rs.next()) {
				transactions.add(new Transaction(db.rs.getInt("id"), db.rs.getString("paymentmethod"), db.rs.getString("cardnumber"), db.rs.getTimestamp("transactiondate").toString(), db.rs.getInt("totalprice")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addNewTransactionDetail(Database db, ArrayList<Cart> carts) {
		String query = "INSERT INTO transactiondetails (transactionid, userid, productid, quantity) "+"VALUES (?, ?, ?, ?)";
		PreparedStatement prepQuery = db.preparedStatement(query);
		
		getLatestTransactionId(db);
		
		int transactionId = 0;
		
		for(Transaction transaction : transactions) {
			transactionId = transaction.getTransactionId();
		}
		
		try {
			for(Cart cart : carts) {
				prepQuery.setInt(1, transactionId);
				prepQuery.setInt(2, cart.getUserId());
				prepQuery.setInt(3, cart.getProductId());
				prepQuery.setInt(4, cart.getQuantity());
				prepQuery.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Transaction> getTransaction(Database db, ArrayList<Transaction> transactions) {
		transactions.clear();
		
		String query = "SELECT * FROM transactionheaders";
		PreparedStatement psQuery = db.preparedStatement(query);
		
		try {
			db.rs = psQuery.executeQuery();
			while(db.rs.next()) {
				transactions.add(new Transaction(db.rs.getInt("id"), db.rs.getString("paymentmethod"), db.rs.getString("cardnumber"), db.rs.getTimestamp("transactiondate").toString(), db.rs.getInt("totalprice")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}
	
	public ArrayList<Transaction> getSearchedTransaction(Database db, ArrayList<Transaction> transactions, Integer month, Integer year) {
		transactions.clear();
		
		String query = "SELECT * FROM transactionheaders WHERE MONTH(transactiondate) = ? AND YEAR(transactiondate) = ?";
		PreparedStatement psQuery = db.preparedStatement(query);
		
		try {
			psQuery.setInt(1, month);
			psQuery.setInt(2, year);
			db.rs = psQuery.executeQuery();
			
			while(db.rs.next()) {
				transactions.add(new Transaction(db.rs.getInt("id"), db.rs.getString("paymentmethod"), db.rs.getString("cardnumber"), db.rs.getTimestamp("transactiondate").toString(), db.rs.getInt("totalprice")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return transactions;
	}
}
